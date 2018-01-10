package data;

import java.util.Date;

public class RepeatedTodo extends Todo {
    Date refreshDate;

    public RepeatedTodo() {
    }

    public RepeatedTodo(int todoId, String description, int[] completedBy, boolean finished, Date refreshDate) {
        super(todoId, description, completedBy, finished);
        this.refreshDate = refreshDate;
    }

    public Date getRefreshDate() {

        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }
}
