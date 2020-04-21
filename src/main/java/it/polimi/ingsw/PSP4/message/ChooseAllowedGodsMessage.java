package it.polimi.ingsw.PSP4.message;

import java.util.List;

/**
 * Message to ask the first player connected to choose the gods to be used during the game
 */
public class ChooseAllowedGodsMessage extends Message {
    private static final long serialVersionUID = -8220436766263764688L;
    private final static MessageType staticType = MessageType.CHOOSE_ALLOWED_GODS;

    //Array of the implemented god names to choose from (request), and the selected ones (response)
    private final List<String> selectableGods;

    public List<String> getSelectableGods() {return selectableGods;}

    public ChooseAllowedGodsMessage(String player, String message, List<String> selectableGods) {
        super(player, message);
        this.selectableGods = selectableGods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage() + "\n");
        for (String godName: selectableGods) {
            sb.append(godName.substring(0, 1).toUpperCase()).append(godName.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString();
    }
}
