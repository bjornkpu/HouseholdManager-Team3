package data;
import java.util.ArrayList;
import java.util.Date;

public class Todo {
    private int choreId;
    private String description;
    private ArrayList<String> completedBy;
    private String assignedTo;
    private Date deadline;

    public Chore() {
    }

    public Chore(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Chore(int choreID, String description, int[] completedBy, boolean finished) {
        this.choreId = choreID;
        this.description = description;
        this.completedBy = completedBy;
        this.assignedTo = assignedTo;
        this.deadline = deadline;
    }

    public int getChoreId() {
        return todoId;
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

    public Date getDeadline(){
        return deadline;
    }

    public void setDeadline(Date deadline){
        this.deadline=deadline;
    }


}
