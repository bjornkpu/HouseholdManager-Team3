package db;

import data.Group;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    /**
     * Retrieves a group object with groupname,groupId and the id of the administrator from database
     *
     * @param groupId Id of group.
     * @return Returns the group if found, else null.
     * @throws SQLException Throws SQLException if connection is not successful.
     */

    public static Group getGroup(int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM party WHERE party_id = ?");
            ps.setString(1,groupId + "");
            rs = ps.executeQuery();

            Group group = null;
            if (rs.next()){
                log.info("Found party " + groupId);
                group = new Group();
                group.setGroupId(groupId);
                group.setGroupName(rs.getString("name"));
                group.setAdmin(rs.getInt("admin_id"));
            } else {
                log.info("Could not find party " + groupId);
            }
            rs.close();
            ps.close();
            return group;
        } finally {
            connection.close();
        }
    }

    /**
     * Retrieves all the parties from database.
     * @return Returns a List of parties.
     * @throws SQLException Throws SQLException if connection is not successful.
     */

    public static List<Group> getAllGroups() throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM party");
            rs = ps.executeQuery();
            List<Group> groups = new ArrayList<Group>();
            while(rs.next()){
                Group group = new Group();
                group.setGroupId(rs.getInt("id"));
                group.setGroupName(rs.getString("name"));
                group.setAdmin(rs.getInt("admin_id"));
                groups.add(group);
            }
            rs.close();
            ps.close();
            log.info("Found " + groups.size() + " parties from database");
            return groups;
        } finally {
            connection.close();
        }
    }

    /**
     *
     * @param group
     * @return
     * @throws SQLException
     */

    public static boolean addParty(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO party (name,admin_id) VALUES(?,?)");
            ps.setString(1,group.getGroupName());
            ps.setInt(2,group.getAdmin());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add party result:" + (result == 1 ? "ok": "failed"));
            return result == 1;
        } finally {
            connection.close();
        }

    }

    public static boolean addParty(String partyName,int adminId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO party (name,admin_id) VALUES(?,?)");
            ps.setString(1,partyName);
            ps.setInt(2,adminId);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add party result:" + (result == 1 ? "ok": "failed"));
            return result == 1;
        } finally {
            connection.close();
        }

    }
    public static boolean deleteGroup(int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM party WHERE id=?");
            ps.setInt(1,groupId);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete party, result: " + (result == 1 ? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
    public static boolean deleteGroup(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM party WHERE id=?");
            ps.setInt(1,group.getGroupId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete party, result: " + (result == 1 ? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public void updateGroup(int groupId) throws SQLException {



    }
}
