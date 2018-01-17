package data;
import java.util.ArrayList;
import java.util.Date;

/**
 * -Description of the class-
 *
 * @author
 */
public class RepeatedChore extends Chore {
    private int regularity;

    public RepeatedChore() {
    }

    public RepeatedChore(int regularity){
        this.regularity=regularity;
    }

    public RepeatedChore(int id, String description, int regularity) {
        super(id, description);
        this.regularity = regularity;
    }

    public RepeatedChore(int id, String description, ArrayList<String> completedBy, String assignedTo, Date deadline, int partyId, int regularity) {
        super(id, description, completedBy, assignedTo, deadline, partyId);
        this.regularity = regularity;
    }

    public int getRegularity() {
        return regularity;
    }

    public void setRegularity(int regularity) {
        this.regularity = regularity;
    }
}
