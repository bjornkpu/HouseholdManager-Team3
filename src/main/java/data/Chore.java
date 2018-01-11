package data;

public class Chore {
    private int id;
    private String description;
    private int[] completedBy;
    private boolean finished;

    public Chore() {
    }

    public Chore(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Chore(int id, String description, int[] completedBy, boolean finished) {
        this.id = id;
        this.description = description;
        this.completedBy = completedBy;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
