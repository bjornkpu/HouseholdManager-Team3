package data;

import java.util.ArrayList;

/**
 * Class for the Group-object
 */
public class Group {
    private int id;
    private String name;
    private int adminid;
    private ArrayList<Integer> users = new ArrayList<Integer>();

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Integer> users) {
        this.users = users;
    }
}
