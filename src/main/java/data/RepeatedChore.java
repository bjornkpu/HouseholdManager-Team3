package data;
import java.util.ArrayList;
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

    public RepeatedChore(int id, String description, ArrayList<String> completedBy, boolean finished, int regularity) {
        super(id, description, completedBy, finished);
        this.regularity = regularity;
    }

    public int getRegularity() {
        return regularity;
    }

    public void setRegularity(int regularity) {
        this.regularity = regularity;
    }
}
