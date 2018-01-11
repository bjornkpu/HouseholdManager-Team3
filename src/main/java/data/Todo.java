package data;

import java.util.ArrayList;

public class Todo {
    private int todoId;
    private String description;
    private ArrayList<String> completedBy;
    private String assignedTo;

    public Todo() {
    }

    public Todo(int todoId, String description) {
        this.todoId = todoId;
        this.description = description;
    }

    public Todo(int todoId, String description, ArrayList<String> completedBy, String assignedTo) {
        this.todoId = todoId;
        this.description = description;
        this.completedBy = completedBy;
        this.assignedTo = assignedTo;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
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


}
