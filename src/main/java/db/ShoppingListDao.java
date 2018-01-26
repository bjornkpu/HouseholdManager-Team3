package db;
import data.*;
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
 * @author jmska
 * @author enoseber
 */
public class ShoppingListDao {

    private static final Logger log = Logger.getLogger();

    private Connection connection;
    private UserDao userDao;
    private ItemDao itemDao;
    private PreparedStatement ps;
    private ResultSet rs;

    public ShoppingListDao(Connection connection) {
        this.connection = connection;
    }

    /** Method that gets a shopping list given the shopping list id, if the user has permission.
     * @param id The ID of the shopping list you are trying to get.
     * @param email The email of the requesting user.
     * @return Returns the shopping list that corresponds to the id given.
     * @throws SQLException when failing to get shopping list.
     */
    public ShoppingList getShoppingList(int id, String email) throws SQLException {
//        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        try {
            ps = connection.prepareStatement("SELECT * FROM shoppinglist JOIN user_party ON shoppinglist.party_id WHERE user_email=? AND id=?");
            ps.setString(1,email);
            ps.setInt(2,id);
            rs = ps.executeQuery();

            ShoppingList sl = null;
            if(rs.next()) {
                log.info("Found shopping list " + id);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(itemDao.getItemsInShoppingList(id));
                sl.setUserList(userDao.getUsersInShoppingList(id));

            } else {
                log.info("Could not find shopping list " + id);
            }
            return sl;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

	/** Method that gets an ArrayList of shopping lists given the shopping list name.
	 * @param name The name of the shopping list you are trying to get.
	 * @return an ArrayList of shopping lists with the given name
	 * @throws SQLException when failing to get shopping lists.
	 */
    public ArrayList<ShoppingList> getShoppingListByName(String name) throws SQLException{
//        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        try {
            ps = connection.prepareStatement("SELECT * FROM shoppinglist WHERE name=?");
            ps.setString(1,name);
            rs = ps.executeQuery();

            ShoppingList sl = null;
            ArrayList<ShoppingList> shoppinglistList = null;

            while(rs.next()) {
                log.info("Found shopping list(s) " + name);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(itemDao.getItemsInShoppingList(sl.getId()));
                sl.setUserList(userDao.getUsersInShoppingList(sl.getId()));
                shoppinglistList.add(sl);
            }
            return shoppinglistList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    public ArrayList<ShoppingList> getShoppingListByUserInGroup(int groupId, String email) throws SQLException{
//        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        try {
            ps = connection.prepareStatement(
                    "SELECT * " +
                            "FROM shoppinglist " +
                            "JOIN shoppinglist_user " +
                            "ON shoppinglist.id = shoppinglist_user.shoppinglist_id " +
                            "AND shoppinglist.party_id = ? " +
                            "AND shoppinglist_user.user_email = ?");
            ps.setInt(1, groupId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            ShoppingList sl = new ShoppingList();
            ArrayList<ShoppingList> shoppinglistList = new ArrayList<ShoppingList>();

            while(rs.next()) {
                log.info("Found shopping list(s) for user " + email + " in group " + groupId);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(itemDao.getItemsInShoppingList(sl.getId()));
                sl.setUserList(userDao.getUsersInShoppingList(sl.getId()));
                shoppinglistList.add(sl);
            }
            return shoppinglistList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** Method that gets an ArrayList of shopping lists given a user.
	 * @param u The user you are trying to get all shopping lists on.
	 * @return an ArrayList of shopping lists on the given user
	 * @throws SQLException when failing to get shopping lists.
	 */
    public ArrayList<ShoppingList> getShoppingListByUser(User u) throws SQLException{
//        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        try {
            ps = connection.prepareStatement(
                    "SELECT *" +
                    "FROM shoppinglist " +
                    "WHERE shoppinglist.id " +
                    "IN (" +
                    "SELECT shoppinglist_user.shoppinglist_id " +
                    "   FROM shoppinglist_user " +
                    "   WHERE shoppinglist_user.user_email = ?" +
                    "   )");
            ps.setString(1,u.getEmail());
            rs = ps.executeQuery();

            ShoppingList sl = new ShoppingList();
            ArrayList<ShoppingList> shoppinglistList = new ArrayList<ShoppingList>();

            while(rs.next()) {
                log.info("Found shopping list(s) for user " + u.getEmail());
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(itemDao.getItemsInShoppingList(sl.getId()));
                sl.setUserList(userDao.getUsersInShoppingList(sl.getId()));
                shoppinglistList.add(sl);
            }
            return shoppinglistList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** Method that gets an ArrayList of shopping lists given a group ID.
	 * @param groupId The ID you are trying to get all shopping lists on.
	 * @return an ArrayList of shopping lists on the given group.
	 * @throws SQLException when failing to get shopping lists.
	 */
    public ArrayList<ShoppingList> getShoppingListByGroupid(int groupId) throws SQLException{
//        connection = Db.instance().getConnection();
        try {

//            if(!groupCheck(groupId)){ //checks if the party-object exists
//                log.info("Could not find party " + groupId);
////              TODO should this return null
//                return null;
//            }

            ps = connection.prepareStatement("SELECT * FROM shoppinglist WHERE party_id = ?");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();

	        ShoppingList sl = new ShoppingList();
	        ArrayList<ShoppingList> shoppinglistList = new ArrayList<ShoppingList>();
	        while(rs.next()) {
                log.info("Found ShoppingList in group " + groupId);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
//                sl.setItemList(ItemDao.getItemsInShoppingList(sl.getId()));
//                sl.setUserList(UserDao.getUsersInShoppingList(sl.getId()));
                shoppinglistList.add(sl);
            }
            return shoppinglistList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** Method that adds a shopping list.
	 * @param shoppingList The shopping list you are adding.
	 * @throws SQLException when failing to add shopping list.
	 */
    public boolean addShoppingList(ShoppingList shoppingList) throws SQLException {
//        connection = Db.instance().getConnection();
        itemDao = new ItemDao(connection);
        userDao = new UserDao(connection);
        try {

            if(!groupCheck(shoppingList.getGroupId())){ //checks if the party-object exists
                log.info("Could not find party " + shoppingList.getGroupId());
                return false;
            }

//          creates new shoppinglist object in shoppinglist table
            ps = connection.prepareStatement("INSERT INTO " +
                    "shoppinglist(name, party_id) VALUES (?,?)");
            ps.setString(1, shoppingList.getName());
            ps.setInt(2, shoppingList.getGroupId());
            int createShoppingListResult = ps.executeUpdate();
            log.info("Create shoppinglist " + (createShoppingListResult == 1?"ok":"failed"));

//          gets last inserted id in shoppinglist_table
            ps = connection.prepareStatement("SELECT shoppinglist.id " +
                    "FROM shoppinglist " +
                    "ORDER BY shoppinglist.id  " +
                    "DESC LIMIT 1");
            rs = ps.executeQuery();
            log.info("Found last id in shoppinglist!");

            int id = 0;
            if(rs.next()){
                id = rs.getInt(1);
            } else {
                log.info("Nothing returned from prepared statement");
                return false;
            }

            ps = connection.prepareStatement("INSERT INTO `shoppinglist_user`(`shoppinglist_id`, `user_email`) " +
                    "VALUES (?, ?)");

            for(int i = 0; i < shoppingList.getUserList().size(); i++){
                if(shoppingList.getUserList().get(i).getEmail().equals("SKIP")) continue;
                ps.setInt(1, id);
                ps.setString(2, shoppingList.getUserList().get(i).getEmail());
                ps.addBatch();
            }

            if(createShoppingListResult == 1){
                ps.executeBatch();
            }


            boolean insertShoppingList = true;


            boolean addedUsers = true;
//            for(int i = 0; i < batch.length; i++){
//                if(batch[i] != 1){
//                    addedUsers = false;
//                    break;
//                }
//            }

            insertShoppingList = ((createShoppingListResult==1) && addedUsers);

            log.info("Added shoppinglist with users " + insertShoppingList);

//          TODO clean up
            return insertShoppingList;

        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

//  TODO should this be here?
	/** Checks if a party exists with specified id
	 * @param groupId the groupId.
	 * @throws SQLException when failing the SQL-query.
	 */
    private boolean groupCheck(int groupId) throws SQLException{
        GroupDao groupDao = new GroupDao(connection);
        return groupDao.getGroup(groupId) != null;
    }

	/** Method that updates the name and/or party_id of a shopping list. NOT the item/user-array.
	 * @param shoppingList The shopping list you are updating to.
	 * @throws SQLException when failing to update shopping list.
	 */
    public boolean updateShoppingList(ShoppingList shoppingList) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE shoppinglist set name=?, party_id=? where id=?");
            ps.setString(1, shoppingList.getName());
            ps.setInt(2, shoppingList.getGroupId());
            ps.setInt(3, shoppingList.getId());
            int result = ps.executeUpdate();
            log.info("Update shopping list " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** Method that deletes a shopping list given the shopping list id.
	 * @param id The ID of the shopping list you are trying to delete.
	 * @throws SQLException when failing to delete shopping list.
	 */
    public boolean delShoppingList(int id) throws SQLException {
//        connection = Db.instance().getConnection();

        try {
//          deletes user_shoppinglist dependency
            ps = connection.prepareStatement("DELETE FROM shoppinglist_user where shoppinglist_id=?");
            ps.setInt(1,id);
            int deleteDependancyResult = ps.executeUpdate();
            log.info("Delete shoppinglist_user dependancy " + (deleteDependancyResult == 1 ? "ok":"failed"));

//          deletes shopping list
            /*
            ps = connection.prepareStatement("DELETE FROM shoppinglist where id=?");
            ps.setInt(1,id);
            int deleteShoppingListResult = ps.executeUpdate();
            log.info("Delete shoppinglist_ " + (deleteShoppingListResult == 1 ? "ok":"failed"));
            */

//            TODO clean up
            log.info("Delete shoppinglist " + (deleteDependancyResult == 1?"ok":"failed"));
            return deleteDependancyResult == 1;

        } finally {

            Db.close(ps);
//            Db.close(connection);
        }
    }

	/** Method that gets an ArrayList given the shopping list id.
	 * @param id The ID of the shopping list you are trying to get the users for.
	 * @return Returns an ArrayList containing the users in a given shopping list that corresponds to the id given.
	 * @throws SQLException when failing to get the list of users.
	 */
//    private ArrayList<User> getUserList(int id) throws SQLException {
//        connection = Db.instance().getConnection();
//        try {
//            return getUserList(id, connection);
//        } finally {
//            Db.close(connection);
//        }
//    }
//	private ArrayList<User> getUserList(int id, Connection connection)throws SQLException {
//		return getUserListMethod(id, connection);
//	}

//	private ArrayList<User> getUsersInShoppingList(int id, Connection connection) throws SQLException {
//		try {
//			ps = connection.prepareStatement("SELECT u.email, u.name, u.phone, u.password, u.salt " +
//					"FROM user u, shoppinglist sl, shoppinglist_user slu \n" +
//					"WHERE u.email=slu.user_email AND slu.shoppinglist_id=sl.id AND sl.id=?");
//			ps.setInt(1,id);
//			userRs = ps.executeQuery();
//
//			log.info("Result set found successfully in User. ");
//
//			int i = 0;
//			User user = new User();
//			ArrayList<User> userList = new ArrayList<User>();
//			while(rs.next()) {
//				log.info("Found user(s) in shoppinglist " + id + " while index: " + i);
//				user = new User();
//				user.setEmail(rs.getString("email"));
//				user.setPassword(rs.getString("password"));
//				user.setPhone(rs.getString("phone"));
//				user.setName(rs.getString("name"));
//				userList.add(user);
//				i++;
//			}
//			return userList;
//		} finally {
////			Db.close(userRs);
//			Db.close(ps);
//		}
//	}

	/** adds a user to a shoppingList
	 * @param email the id of the user you want to add
	 * @param shoppingListId the id of the shoppingList you want to add the user to
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
//  TODO teste
    public boolean addUserToShoppingList(String email, int shoppingListId) throws SQLException {
	    try {
		    ps = connection.prepareStatement("INSERT INTO `shoppinglist_user`(`shoppinglist_id`, `user_email`) " +
                    "VALUES (?, ?)");
		    ps.setInt(1, shoppingListId);
		    ps.setString(2, email);
		    int result = ps.executeUpdate();
		    log.info("Added user to shopping list " + (result == 1 ? "ok":"failed"));
		    return result == 1;
	    } finally {

            Db.close(ps);
//            Db.close(connection);
	    }
    }

	/** remove a user from a givenshoppinglist
	 * @param email the user id you want to remove from the shopping list
	 * @param shoppingListId the id of the shopping list you want to remove the user from
	 * @return true if the query succeeds
	 * @throws SQLException if the query fails
	 */
//  TODO teste
	public boolean removeUserFromShoppingList(String email, int shoppingListId) throws SQLException {
		try {
			ps = connection.prepareStatement("DELETE FROM shoppinglist_user " +
                    "WHERE shoppinglist_id = ? AND user_email = ?");
            ps.setInt(1, shoppingListId);
            ps.setString(2, email);
			int result = ps.executeUpdate();
			log.info("Removing user from shopping list " + (result == 1 ? "ok":"failed"));
			return result == 1;
		} finally {
            Db.close(ps);
//            Db.close(connection);
		}
	}
}
