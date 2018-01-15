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
 */
public class ShoppingListDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ShoppingList getShoppingList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM shoppinglist WHERE id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

            ShoppingList sl = null;
            if(rs.next()) {
                log.info("Found shoppinglist " + id);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(getItemList(id));
                sl.setUserList(getUserList(id));

            } else {
                log.info("Could not find shoppinglist " + id);
            }
            rs.close();
            ps.close();
            return sl;
        } finally {
            connection.close();
        }
    }

    public static boolean addShoppingList(ShoppingList shoppingList) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO user () VALUES(?,?,?,?)");
            // TODO: Insert add-code here.
            int result = ps.executeUpdate();
            ps.close();
            log.info("Add shoppinglist " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public static boolean updateShoppingList(ShoppingList shoppingList) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE user set name=?, phone=?, password=? where email=?");
            // TODO: Insert update-code here.
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update shoppinglist " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

//TODO: delete the items in the list, not just the list itself
    public static boolean delShoppingList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM shoppinglist where id=?");
            ps.setInt(1,id);
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete shoppinglist " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    private static ArrayList<Item> getItemList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM item WHERE shoppinglist_id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

//            TODO kan ikke teste her, hvordan
//            if(!rs.next()) {
//                log.info("could not find item " + id);
//            }

            Item item = null;
            ArrayList<Item> itemList = null;
            while(rs.next()) {
                log.info("Found item " + id);
                item = new Item();
                item.setItemId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
                itemList.add(item);
            }
            rs.close();
            ps.close();
            return itemList;
        } finally {
            connection.close();
        }
    }

    private static ArrayList<User> getUserList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("" +
                    "SELECT u.email, u.name, u.phone, u.password, u.salt " +
                    "FROM user u, shoppinglist sl, shoppinglist_user slu \n" +
                    "WHERE u.email=slu.user_email AND slu.shoppinglist_id=sl.id AND sl.id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

//            TODO kan ikke teste her, hvordan
//            if(!rs.next()) {
//                log.info("could not find item " + id);
//            }

            User user = new User();
            ArrayList<User> userList = new ArrayList<User>();

            while(rs.next()) {
                log.info("Found user(s) in shoppinglist " + id);
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setName(rs.getString("name"));
                userList.add(user);
            }

            rs.close();
            ps.close();
            return userList;
        } finally {
            connection.close();
        }
    }
}
