package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

/**
 * Message to ask to change the currently selected worker
 */
public class ChangeWorkerResponse extends Response {
    private static final long serialVersionUID = -6990617562260746862L;
    private static final MessageType staticType = MessageType.CHANGE_WORKER;

    /**
     * Constructor of the class ChangeWorkerResponse
     * @param player username of the sender
     */
    public ChangeWorkerResponse(String player) { super(player, "", staticType); }

    @Override
    public void handle() {
        //possible as checked in RemoteView.update()
        GameState.getInstance().getCurrPlayer().getState().changeWorker();
    }
}
