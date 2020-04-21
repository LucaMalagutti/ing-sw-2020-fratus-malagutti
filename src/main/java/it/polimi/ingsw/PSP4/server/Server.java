package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.controller.Controller;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.view.RemoteView;
import it.polimi.ingsw.PSP4.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private final static int port = 31713;
    private final ServerSocket socket;
    private final ExecutorService executor = Executors.newFixedThreadPool(64);

    private final Map<SocketClientConnection, String> waitingConnections = new LinkedHashMap<>();
    private final Map<SocketClientConnection, Player> playingConnections = new LinkedHashMap<>();

    private int numPlayers = -1;                                                 //number of playing clients
    private SocketClientConnection firstClientConnected;                        //first client to connect to the lobby
    private final List<String> usernamesTaken = Collections.synchronizedList(new ArrayList<>());

    private synchronized void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    public Server() throws IOException {
        this.socket = new ServerSocket(port);
    }

    /**
     * Removes a connection after its client exits
     * @param c connection to be unregistered
     */
    public synchronized void unregisterConnection(SocketClientConnection c) {
        usernamesTaken.remove(waitingConnections.get(c));
        playingConnections.remove(c);
        waitingConnections.remove(c);
        //Handles first client disconnection when the game is still in lobby phase
        if (c == firstClientConnected) {
            numPlayers = -1;
            firstClientConnected = null;
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                connection.closeConnection("The lobby creator has left.");
            }
            waitingConnections.clear();
        }
    }

    /**
     * Asks the client to provide a username. Checks its uniqueness
     * @param c client that has to provide a unique username
     * @return unique username selected. Will be used as a parameter for later lobby() call
     */
    public String selectUsername(SocketClientConnection c) {
        if (firstClientConnected != null && numPlayers == -1) {
            c.asyncSend("Wait for the first player to set up the lobby");
//            c.discardScanner();
        }
        synchronized (this) {
            try {
                while(firstClientConnected != null && numPlayers == -1) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        c.stopDiscarding();
        String selectedUsername = c.selectClientUsername();
        while (usernamesTaken.contains(selectedUsername) || selectedUsername.equals("")) {
            selectedUsername = c.selectClientUsername(selectedUsername);
        }
        usernamesTaken.add(selectedUsername);
        return selectedUsername;
    }

    /**
     * Handles lobby phase of the yet to start game on this server
     * @param c connection entering lobby
     * @param name username of the incoming client
     */
    public synchronized void lobby(SocketClientConnection c, String name) {
        //drops connections started with an ongoing game
        if (playingConnections.size() > 0) {
            c.closeConnection("A game has already started. Try again later!");
        }
        waitingConnections.put(c, name);
        if (waitingConnections.size() == 1) {
            firstClientConnected = c;
            try {
                setNumPlayers(c.initializeGameNumPlayer(name));
                c.asyncSend("\nWaiting for the other players to join your lobby");
            } catch (Exception e) {
                unregisterConnection(c);
            } finally {
                notifyAll();
            }
        }
        else {
            c.asyncSend("Entering lobby as "+name);
        }
        //When the number of waiting players is reached, initializes GameState and its dependencies and starts the game
        if (waitingConnections.size() == numPlayers) {
            GameState.getInstance(true);
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                Player player = new Player(waitingConnections.get(connection));
                GameState.getInstance().addPlayer(player);
                if (GameState.getInstance().getPlayers().size() == 1) {
                    GameState.getInstance().setCurrPlayer(player);
                }
                View playerView = new RemoteView(player, connection);
                Controller controller = new Controller();
                GameState.getInstance().addObserver(playerView);
                playerView.addObserver(controller);
                playingConnections.put(connection, player);
                connection.asyncSend("\nSTARTING A NEW SANTORINI GAME\n");
            }
            GameState.getInstance().setNumPlayer(numPlayers);
            GameState.getInstance().startGame();
            waitingConnections.clear();
        }
    }

    /**
     * Accepts a new connection on receiving Socket and creates corresponding SocketClientConnection on another thread
     */
    public void run() {
        while (true) {
            try {
                Socket newSocket = socket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
}