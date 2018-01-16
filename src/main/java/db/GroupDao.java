package db;
import data.Group;
import data.Member;
import util.Logger;
import util.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class handles the database connections for the Group class.
 *
 * @author nybakk1
 * @version 0.2
 */
public class GroupDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static UserStatus userStatus = new UserStatus();

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
            ps = connection.prepareStatement("SELECT party.id,party.name,user_party.user_email FROM party,user_party WHERE party.id = ? AND user_party.party_id = party.id AND user_party.status = ?");
            ps.setInt(1,groupId);
            ps.setInt(2,userStatus.ADMIN);
            rs = ps.executeQuery();

            Group group = null;
            if (rs.next()){
                log.info("Found group " + groupId);
                group = new Group();
                group.setId(groupId);
                group.setName(rs.getString("name"));
                group.setAdmin(rs.getString("user_party.user_email"));

                List<Member> members = new ArrayList<Member>();
                try {
                    ps = connection.prepareStatement("SELECT * FROM user_party WHERE party_id = ? AND status = ?");
                    ps.setInt(1,groupId);
                    ps.setInt(2,UserStatus.MEMBER);
                    rs = ps.executeQuery();

                    while (rs.next()){
                        Member member = new Member();
                        member.setBalance(rs.getDouble("balance"));
                        member.setStatus(rs.getInt("status"));
                        member.setEmail(rs.getString("user_email"));
                        members.add(member);
                    }
                    group.setMembers(members);
                } finally {

                }
            } else {
                log.info("Could not find group " + groupId);
            }
            rs.close();
            ps.close();
            return group;
        } finally {
            connection.close();
        }
    }

    /**
     * Retrieves a list of groups given the groupname as a argument from the database.
     * NOTE: There could be several groups with same name.
     * @param name Name of group.
     * @return A List of groups with same name as argument. Null if no groups with the name exists in database.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static List<Group> getGroupByName(String name) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM party WHERE name = ?");
            ps.setString(1,name);
            rs = ps.executeQuery();
            List<Group> g = new ArrayList<Group>();
            while (rs.next()){
                log.info("Found group with name: " + name);
                Group group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(name);
                g.add(group);
            }
            rs.close();
            ps.close();
            return g.size() == 0 ? null : g;
        } finally {
            connection.close();
        }
    }

    /**
     * Retrieves all the groups from database.
     * @return Returns a List of groups if found. Null if empty.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static List<Group> getAllGroups() throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM party");
            rs = ps.executeQuery();
            List<Group> groups = new ArrayList<Group>();
            while(rs.next()){
                Group g = new Group();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                List<Member> members = new ArrayList<Member>();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_party WHERE party_id = ? AND status = ?");
                    preparedStatement.setInt(1,g.getId());
                    preparedStatement.setInt(2,UserStatus.MEMBER);
                    ResultSet resultSets = preparedStatement.executeQuery();

                    while (resultSets.next()) {
                        Member member = new Member();
                        member.setBalance(resultSets.getDouble("balance"));
                        member.setStatus(resultSets.getInt("status"));
                        member.setEmail(resultSets.getString("user_email"));
                        members.add(member);
                    }
                    preparedStatement.close();
                    resultSets.close();
                } finally { }
                g.setMembers(members);
                groups.add(g);
            }
            rs.close();
            ps.close();
            log.info("Found " + groups.size() + " groups from database");
            return groups.size() == 0 ? null : groups;
        } finally {
            connection.close();
        }
    }

    /**
     *
     * @param amountOfGroups Number of groups you wish to receive.
     * @return A list of X amount of groups from database. Null if no groups exist in database.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static List<Group> getGroups(int amountOfGroups) throws SQLException {
        if (amountOfGroups > 0) {
            connection = Db.instance().getConnection();
            try {
                ps = connection.prepareStatement("SELECT * FROM party ORDER BY id LIMIT ?");
                ps.setInt(1,amountOfGroups);
                rs = ps.executeQuery();
                List<Group> groups = new ArrayList<Group>();
                while (rs.next()) {
                    Group group = new Group();
                    group.setId(rs.getInt("id"));
                    group.setName(rs.getString("name"));
                    groups.add(group);
                }
                rs.close();
                ps.close();
                log.info("Retrieving " + amountOfGroups + " groups from database. Amount retrieved: " + groups.size());
                return groups.size() == 0 ? null:groups;
            } finally {
                connection.close();
            }
        }
        return null;
    }

    /**
     * Adds a group to the database.
     * @param group The group to be added to database.
     * @return Returns true if adding a group was successful, else false.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static boolean addGroup(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        if (group.getName() == null || group.getAdmin() == null){
            log.info("Name or admin missing. Group not added.");
            return false;
        }
        try {
            ps = connection.prepareStatement("INSERT INTO party (name) VALUES(?)");
            ps.setString(1,group.getName());
            int result = ps.executeUpdate();

            ps = connection.prepareStatement("SELECT * FROM party WHERE NOT EXISTS (SELECT * FROM user_party WHERE user_party.party_id = party.id) AND name = ?");
            ps.setString(1,group.getName());
            rs = ps.executeQuery();
            Group g  = new Group();
            if (rs.next()){
                g.setId(rs.getInt("id"));
                g.setName(group.getName());
            }
            ps = connection.prepareStatement("INSERT INTO user_party (user_email,party_id,balance,status) VALUES (?,?,?,?)");
            ps.setString(1,group.getAdmin());
            ps.setInt(2,g.getId());
            ps.setDouble(3,0);
            ps.setInt(4,UserStatus.ADMIN);
            result = ps.executeUpdate();

            ps.close();
            log.info("Add party result:" + (result == 1 ? "ok": "failed"));
            return result == 1;
        } finally {
            connection.close();
        }

    }

    /**
     * Deletes a group given an groupid.
     * @param groupId The GroupId of the group.
     * @return Returns true if the deletion was succesful, else false.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static boolean deleteGroup(int groupId) throws SQLException {
        connection = Db.instance().getConnection(); // heu
        try{
            ps = connection.prepareStatement("DELETE FROM user_party WHERE party_id = ?");
            ps.setInt(1, groupId);
            int dependencyResult = ps.executeUpdate();
            ps.close();
            log.info("Delete dependency " + (dependencyResult == 1 ? "ok" : "failed"));

            ps = connection.prepareStatement("DELETE FROM party WHERE id=?");
            ps.setInt(1,groupId);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete group, result: " + (result == 1 ? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    /**
     * Deletes a group given an group object.
     * @param group The group to be deleted.
     * @return Returns true if the deletion was succesful, else false.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static boolean deleteGroup(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try{
//          deletes a user_party dependency
            ps = connection.prepareStatement("DELETE FROM user_party WHERE party_id = ?");
            ps.setInt(1, group.getId());
            int dependencyResult = ps.executeUpdate();
            ps.close();
            log.info("Delete dependency " + (dependencyResult == 1 ? "ok" : "failed"));

//          deletes a party from the database
            ps = connection.prepareStatement("DELETE FROM party WHERE id=?");
            ps.setInt(1,group.getId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete group, result: " + (result == 1 ? "ok":"failed"));

            return result == 1 && dependencyResult == 1;
        } finally {
            connection.close();
        }
    }



    /**
     * Updates a group using a group object.
     *
     * @param group The group to be updated.
     * @return Returns true if the deletion was succesful, else false.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static boolean updateGroup(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE party set name=? WHERE id = ?");
            ps.setString(1,group.getName());
            ps.setInt(2,group.getId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update group, result: " + (result == 1? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    /**
     *  Updates a group with a new name.
     *
     * @param groupid The id of the group.
     * @param newName New name of group.
     * @return Returns true if the deletion was succesful, else false.
     * @throws SQLException Throws SQLException if connection is not successful.
     */
    public static boolean updateName(int groupid,String newName) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE party set name=? WHERE id = ?");
            ps.setString(1,newName);
            ps.setInt(2,groupid);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update group, result: " + (result == 1? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}
