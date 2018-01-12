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
            ps = connection.prepareStatement("SELECT * FROM party WHERE id = ?");
            ps.setString(1,groupId + "");
            rs = ps.executeQuery();

            Group group = null;
            if (rs.next()){
                log.info("Found party " + groupId);
                group = new Group();
                group.setId(groupId);
                group.setName(rs.getString("name"));
                group.setAdmin(rs.getString("admin"));
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
    public static Group getGroupByName(String name) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM party WHERE name = ?");
            ps.setString(1,name);
            rs = ps.executeQuery();
            Group group = null;
            if (rs.next()){
                log.info("Found party with name: " + name);
                group = new Group();
                group.setId(rs.getInt("id"));
                group.setName(name);
                group.setAdmin(rs.getString("admin"));
            } else {
                log.info("Could not find party " + name);
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
                Group g = new Group();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setAdmin(rs.getString("admin"));
                groups.add(g);
            }
            rs.close();
            ps.close();
            log.info("Found " + groups.size() + " parties from database");
            return groups;
        } finally {
            connection.close();
        }
    }
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
                    group.setAdmin(rs.getString("admin"));
                    group.setName(rs.getString("name"));
                    groups.add(group);
                }
                rs.close();
                ps.close();
                log.info("Retrieving " + amountOfGroups + " groups from database. Amount retrieved: " + groups.size());
                return groups;
            } finally {
                connection.close();
            }
        }
        return null;
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
            ps = connection.prepareStatement("INSERT INTO party (name,admin) VALUES(?,?)");
            ps.setString(1,group.getName());
            ps.setString(2,group.getAdmin());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add party result:" + (result == 1 ? "ok": "failed"));
            return result == 1;
        } finally {
            connection.close();
        }

    }

    public static boolean addParty(String partyName,String adminId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO party (name,admin) VALUES(?,?)");
            ps.setString(1,partyName);
            ps.setString(2,adminId);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add party result:" + (result == 1 ? "ok": "failed"));
            return result == 1;
        } finally {
            connection.close();
        }

    }
    public static boolean deleteParty(int groupId) throws SQLException {
        connection = Db.instance().getConnection(); // heu
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


    public static boolean deleteParty(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM party WHERE id=?");
            ps.setInt(1,group.getId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete party, result: " + (result == 1 ? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }


    /*
    Should not be used. Groups could have same names.
     */
    @Deprecated
    public static boolean deleteParty(String name) throws SQLException {
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM party WHERE name=?");
            ps.setString(1,name);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete party, result: " + (result == 1 ? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public static boolean updateParty(Group group) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE party set name=?,admin=? WHERE id = ?");
            ps.setString(1,group.getName());
            ps.setString(2,group.getAdmin());
            ps.setInt(3,group.getId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update party, result: " + (result == 1? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
    public static boolean updateAdmin(int partyid,String newAdmin) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE party set admin=? WHERE id = ?");
            ps.setString(1,newAdmin);
            ps.setInt(2,partyid);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update party, result: " + (result == 1? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
    public static boolean updateName(int partyid,String newName) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE party set name=? WHERE id = ?");
            ps.setString(1,newName);
            ps.setInt(2,partyid);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update party, result: " + (result == 1? "ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}
