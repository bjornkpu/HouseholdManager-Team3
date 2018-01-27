package db;

import data.StatisticsHelp;
import data.User;

import java.util.ArrayList;

import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.EmailSender.sendConfirmationMail;

/**
 * -Data access object for User-
 *
 * @author BK
 * @author johanmsk
 * @version     %I%, %G%
 * @since       1.0
 */
public class UserDao {

    private static final Logger log = Logger.getLogger();

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDao(Connection connection) {
        this.connection=connection;
    }

    /** Method that gets a user given an email.
     * @param email The email of the user that is to be returned.
     * @return A User with the information relative to the email entered.
     * @throws SQLException when failing to get User.
     */
    public User getUser(String email) throws SQLException {
//        connection = Db.instance().getConnection();
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
//            Db.close(connection);
        }
    }

	/** adds a user to the database
	 * @param user the user you want to add to det database
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public boolean addUser(User user) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO user (email,name,phone,password,salt) VALUES(?,?,?,?,?)");
            ps.setString(1,user.getEmail());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPhone());
            ps.setString(4,user.getPassword());
            ps.setString(5,user.getSalt());
            int result = ps.executeUpdate();
            log.info("Add user " + (result == 1?"ok":"failed"));

            return result == 1;
        } finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** update a user with a user object. it will see the id and overwrite the rest
	 * @param user the user you want to update
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public boolean updateUser(User user) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE user set name=?, phone=?, password=?, salt=? where email=?");
            ps.setString(1,user.getName());
            ps.setString(2,user.getPhone());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getSalt());
            ps.setString(5,user.getEmail());
            int result = ps.executeUpdate();
            log.info("Update user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }


	/** deletes a user with the given user id
	 * @param email the id of the user you want to delete
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
    public boolean delUser(String email) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM user where email=?");
            ps.setString(1,email);
            int result = ps.executeUpdate();
            log.info("Delete user " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** gets all users in a shopping list by the shopping list id
	 * @param shoppingListId the id of the shopping list you want to get all users of
	 * @return an ArrayList of users that is in the shopping list with the given id
	 * @throws SQLException if the query fails
	 */
    public ArrayList<User> getUsersInShoppingList(int shoppingListId) throws SQLException{
//	    connection = Db.instance().getConnection();
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
//            Db.close(connection);
	    }
    }

    /**Gets an ArrayList of the participants in a disbursement
     *
     * @param disbursementId the ID of the disbursements to get the participants from
     * @return an {@linkplain ArrayList} with {@linkplain User}s
     * @throws SQLException if the query errors
     */
    public ArrayList<User> getUsersInDisbursement (int disbursementId) throws SQLException{
//        connection = Db.instance().getConnection();
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

            return userList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            connection.close();
        }
    }

}