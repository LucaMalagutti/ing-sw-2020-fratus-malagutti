package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.controller.turnStates.StandardBuildState;
import it.polimi.ingsw.PSP4.controller.turnStates.StandardMoveState;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.*;
import it.polimi.ingsw.PSP4.utils.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class AtlasPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsable_standardBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Atlas");
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 2)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 0)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(3, Arrays.asList(new Coordinates(1, 0), new Coordinates(1, 1)));
        buildings.put(4, Collections.singletonList(new Coordinates(0, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 1)
        );
        Coordinates movePosition = new Coordinates(0, 0);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 0), new Coordinates(1, 1)
        );
        Coordinates buildPosition = new Coordinates(1, 0);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        buildings.replace(3, Collections.singletonList(new Coordinates(1, 1)));
        buildings.replace(4, Arrays.asList(buildPosition, new Coordinates(0, 1)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void powerNotUsed_standardBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Atlas");
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 2)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 0)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 1)));
        buildings.put(4, Collections.singletonList(new Coordinates(0, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 1)
        );
        Coordinates movePosition = new Coordinates(0, 0);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 0), new Coordinates(1, 1)
        );
        Coordinates buildPosition = new Coordinates(1, 0);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        Actions.confirmPosition(buildPosition, false);
        buildings.remove(2);
        buildings.replace(3, Arrays.asList(buildPosition, new Coordinates(1, 1)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void powerUsed_forceDomeBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Atlas");
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 2)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 0)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 1)));
        buildings.put(4, Collections.singletonList(new Coordinates(0, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 1)
        );
        Coordinates movePosition = new Coordinates(0, 0);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 0), new Coordinates(1, 1)
        );
        Coordinates buildPosition = new Coordinates(1, 0);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        Actions.confirmPosition(buildPosition, true);
        buildings.remove(2);
        buildings.replace(4, Arrays.asList(buildPosition, new Coordinates(0, 1)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
