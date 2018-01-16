package db;

import data.User;

import java.sql.*;
import java.util.ArrayList;

import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * -Description of the class-
 *
 * @author BK
 * @author johanmsk
 */
public class UserDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    /** Method that gets a user given an email.
     * @param email The email of the user that is to be returned.
     * @return A User with the information relative to the email entered.
     * @throws SQLException when failing to get User.
     */
    public static User getUser(String email) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM user WHERE email=?");
            ps.setString(1,email);
            rs = ps.executeQuery();

            User user = null;
            if(rs.next()) {
                log.info("Found user " + email);
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setName(rs.getString("name"));
                user.setSalt(rs.getString("salt"));
            } else {
                log.info("Could not find user " + email);
            } return user;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

	/** adds a user to the database
	 * @param user the user you want to add to det database
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public static boolean addUser(User user) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO user (email,name,phone,password) VALUES(?,?,?,?)");
            ps.setString(1,user.getEmail());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPhone());
            ps.setString(4,user.getPassword());
            int result = ps.executeUpdate();
            log.info("Add user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
            Db.close(connection);
        }
    }

	/** update a user with a user object. it will see the id and overwrite the rest
	 * @param user the user you want to update
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public static boolean updateUser(User user) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE user set name=?, phone=?, password=? where email=?");
            ps.setString(1,user.getName());
            ps.setString(2,user.getPhone());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getEmail());
            int result = ps.executeUpdate();
            log.info("Update user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
            Db.close(connection);
        }
    }

	/** deletes a user with the given user id
	 * @param email the id of the user you want to delete
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public static boolean delUser(String email) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM user where email=?");
            ps.setString(1,email);
            int result = ps.executeUpdate();
            log.info("Delete user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
            Db.close(connection);
        }
    }

	/** gets all users in a shopping list by the shopping list id
	 * @param shoppingListId the id of the shopping list you want to get all users of
	 * @return an ArrayList of users that is in the shopping list with the given id
	 * @throws SQLException if the query fails
	 */
    public static ArrayList<User> getUsersInShoppingList(int shoppingListId) throws SQLException{
	    connection = Db.instance().getConnection();
	    try {
		    ps = connection.prepareStatement("SELECT *" +
                    "FROM user " +
                    "WHERE user.email " +
                    "IN( SELECT user_email FROM shoppinglist_user WHERE shoppinglist_user.shoppinglist_id = ?)");
		    ps.setInt(1,shoppingListId);
		    rs = ps.executeQuery();

		    User user = new User();
		    ArrayList<User> userList = new ArrayList<User>();
		    while(rs.next()) {
			    log.info("Found user in shopping list" + shoppingListId);
			    user = new User();
			    user.setEmail(rs.getString("email"));
			    user.setName(rs.getString("name"));
			    user.setPassword(rs.getString("password"));
			    user.setPhone(rs.getString("phone"));
			    user.setSalt(rs.getString("salt"));
			    userList.add(user);
		    } return userList;
	    } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
	    }
    }

    //TODO: doc and test
    public static ArrayList<User> getUsersInDisbursement (int disbursementId) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT *" +
                    "FROM user " +
                    "WHERE user.email " +
                    "IN( SELECT user_email FROM user_disbursement ud WHERE ud.disp_id=?)");
            ps.setInt(1,disbursementId);
            rs = ps.executeQuery();

            User user = null;
            ArrayList<User> userList= null;
            if(rs.next()){
                log.info("Found users in disbursement" + disbursementId);
                userList = new ArrayList<User>();
                do{
                    user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setSalt(rs.getString("salt"));
                    userList.add(user);
                }while(rs.next());
            }else{
                log.info("Could not find any users in disbursement: "+disbursementId);
            }

            rs.close();
            ps.close();
            return userList;
        } finally {
            connection.close();
        }
    }

}