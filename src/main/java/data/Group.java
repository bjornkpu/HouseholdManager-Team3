package data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * Class for the Group-object
 *
 * @author
 */
public class Group implements Serializable {
    private int groupId;
    private String name;
    private String description;
    private String admin;

    public Group(){
    }

    public Group(int groupId, String name, String description, String admin) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.admin = admin;
    }

    public int getId() {
        return groupId;
    }

    public void setId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

}
