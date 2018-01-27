package data;
import java.util.ArrayList;
/**
 * Object for the chores.
 *
 * @author BK
 */
public class Chore {
    private int choreId;
    private String description;
    private ArrayList<String> completedBy;
    private String assignedTo;
    private java.sql.Timestamp deadline;
    private int partyId;

    public Chore() {
    }

    public Chore(int choreId, String description) {
        this.choreId = choreId;
        this.description = description;
    }

    public Chore(int choreID, String description, ArrayList<String> completedBy, String assignedTo, java.sql.Timestamp deadline, int partyId) {
        this.choreId = choreID;
        this.description = description;
        this.completedBy = completedBy;
        this.assignedTo = assignedTo;
        this.deadline = deadline;
        this.partyId=partyId;
    }

    public Chore(String description, java.sql.Timestamp deadline, int partyId) {
        this.description = description;
        this.deadline = deadline;
        this.partyId=partyId;
    }

    public int getChoreId() {
        return choreId;
    }

    public void setChoreId(int choreId) {
        this.choreId = choreId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(ArrayList<String> completedBy) {
        this.completedBy = completedBy;
    }

    public void setAssignedTo(String assignedTo){
        this.assignedTo=assignedTo;
    }

    public String getAssignedTo(){
        return assignedTo;
    }

    public java.sql.Timestamp getDeadline(){
        return deadline;
    }

    public void setDeadline(java.sql.Timestamp deadline) {
        this.deadline = deadline;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }
}
