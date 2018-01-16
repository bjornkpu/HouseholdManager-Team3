package db;

import data.User;
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
            }
            rs.close();
            ps.close();
            return user;
        } finally {
            connection.close();
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
            ps.close();
            log.info("Add user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
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
            ps.close();
            log.info("Update user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
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
            ps.close();
            log.info("Delete user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
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
		    }

		    rs.close();
		    ps.close();
		    return userList;
	    } finally {
		    connection.close();
	    }
    }

	/** adds a user to a shopping list
	 * @param user the user you want to add
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
	//TODO: make this method
    public static boolean addUserToShoppingList(User user) throws SQLException {
    	return true; //to make it compile
    }

	/** idk wft dis is. spør johan, han laga den her
	 * @throws SQLException
	 */
    public static void startTest() throws SQLException {
        connection.setAutoCommit(false);
    }

	/** idk wft dis is. spør johan, han laga den her
	 * @throws SQLException
	 */
    public static void endTest() throws SQLException{
        connection.rollback();
        connection.setAutoCommit(true);
    }

}