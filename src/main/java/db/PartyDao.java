package db;
import data.User;

import java.sql.*;

import util.Logger;
/**
 * -Description of the class-
 *
 * @author
 */
public class PartyDao {

    private static final Logger log = Logger.getLogger();

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