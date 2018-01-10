package data;

public class Todo {
    int todoId;
    String description;
    int[] completedBy;
    boolean finished;

    public Todo() {
    }

    public Todo(int todoId, String description, int[] completedBy, boolean finished) {
        this.todoId = todoId;
        this.description = description;
        this.completedBy = completedBy;
        this.finished = finished;
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

    public int[] getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(int[] completedBy) {
        this.completedBy = completedBy;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


}
