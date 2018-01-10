package db;

import data.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.Logger;

/**
 * @author jmska
 */
public class UserDao {

    private static final Logger log = Logger.getLogger();

    public User getUser(String email) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM user WHERE email='" + email + "'");
            User user = null;
            if(rs.next()) {
                log.info("Found user " + email);
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPassword(rs.getString("password"));
            } else {
                log.info("Could not find user " + email);
            }
            rs.close();
            statement.close();
            return user;
        } finally {
            connection.close();
        }
    }

    public boolean addUser(User user) throws SQLException {
        Connection connection = Db.instance().getConnection();
        try {
            Statement s = connection.createStatement();
            int result = s.executeUpdate(
                    "INSERT INTO user (email,first_name,last_name,password) VALUES('" +
                            user.getEmail() + "','" +
                            user.getFirstName() + "','" +
                            user.getLastName()+ "','" +
                            user.getPassword() + "')");
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