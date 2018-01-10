package db;

import data.User;

import java.sql.*;

import util.Logger;

/**
 * @author jmska
 */
public class UserDao {

    private static final Logger log = Logger.getLogger();

    public User getUser(String email) throws SQLException {
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

    public boolean addUser(User user) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user (email,name,phone,password) VALUES(?,?,?,?)");
            ps.setString(1,user.getEmail());
            ps.setString(1,user.getName());
            ps.setString(1,user.getPhone());
            ps.setString(1,user.getPassword());
            int result = ps.executeUpdate();
            s.close();
            log.info("Add user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public boolean updateUser(User user) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            Statement s = connection.createStatement();
            int result = s.executeUpdate(
                    "UPDATE user set first_name='" + user.getFirstName() +
                            "',last_name='" + user.getLastName() +
                            "',password='" + user.getPassword() +
                            "' where email='" + user.getEmail() + "'");
            s.close();
            log.info("Update user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}