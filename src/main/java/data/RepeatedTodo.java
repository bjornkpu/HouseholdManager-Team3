package data;

import java.util.ArrayList;
import java.util.Date;

public class RepeatedTodo extends Todo {
    private int regularity;

    public RepeatedTodo() {
    }

    public RepeatedTodo(int regularity){
        this.regularity=regularity;
    }

    public RepeatedTodo(int todoId, String description, int regularity) {
        super(todoId, description);
        this.regularity = regularity;
    }

    public RepeatedTodo(int todoId, String description, ArrayList<String> completedBy, String assignedTo, int regularity) {
        super(todoId, description, completedBy, assignedTo);
        this.regularity = regularity;
    }

    public int getRegularity() {

        return regularity;
    }

    public void setRegularity(int regularity) {
        this.regularity = regularity;
    }
}
