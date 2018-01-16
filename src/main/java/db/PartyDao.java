package db;
import data.User;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * -Description of the class-
 *
 * @author
 */
public class PartyDao {

    private static final Logger log = Logger.getLogger();

    /** gets a user by the email
     * @param email the id of the user you want to get
     * @return the user by the given email
     * @throws SQLException if teh query fails
     */
    public static User getUser(String email) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE email=?");
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            User user = null;
            if(rs.next()) {
                log.info("Found user " + email);
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setName(rs.getString("name"));
            } else {
                log.info("Could not find user " + email);
            }
            rs.close();
            ps.close();
            return user;
        } finally {
            connection.close();
        }
    }

    /** adds a user to the database
     * @param user the user you want to add
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public static boolean addUser(User user) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user (email,name,phone,password) VALUES(?,?,?,?)");
            ps.setString(1,user.getEmail());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPhone());
            ps.setString(4,user.getPassword());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    /** updates a user by the user id, this will find the user id and overwrite the other values
     * @param user the user you want to update to
     * @return true if the update is a success
     * @throws SQLException if the query fails
     */
    public static boolean updateUser(User user) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user set name=?, phone=?, password=? where email=?");
            ps.setString(1,user.getName());
            ps.setString(2,user.getPhone());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getEmail());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}