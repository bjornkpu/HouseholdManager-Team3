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

    public RepeatedTodo(int choreId, String description, int regularity) {
        super(choreId, description);
        this.regularity = regularity;
    }

    public RepeatedTodo(int choreId, String description, ArrayList<String> completedBy, String assignedTo, Date deadline, int regularity) {
        super(choreId, description, completedBy, assignedTo, deadline);
        this.regularity = regularity;
    }

    public int getRegularity() {

        return regularity;
    }

    public void setRegularity(int regularity) {
        this.regularity = regularity;
    }
}
