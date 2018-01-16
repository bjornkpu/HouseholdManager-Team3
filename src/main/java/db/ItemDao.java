package db;
import data.Item;
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
public class ItemDao {
    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static Item getItem(Integer id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM item WHERE id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

            Item item = null;
            if(rs.next()) {
                log.info("Found item " + id);
                item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
                item.setShoppingListId(rs.getInt("shoppinglist_id"));
                item.setDispId(rs.getInt("dips_id"));
            } else {
                log.info("could not find item " + id);
            }

            rs.close();
            ps.close();
            return item;
        } finally {
            connection.close();
        }
    }

    public static ArrayList<Item> getItemsInShoppingList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM item WHERE shoppinglist_id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

//            TODO kan ikke teste her, hvordan
//            if(!rs.next()) {
//                log.info("could not find item " + id);
//            }

            Item item = new Item();
            ArrayList<Item> itemList = new ArrayList<Item>();
            while(rs.next()) {
                log.info("Found item in shoppinglist" + id);
                item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
                item.setShoppingListId(rs.getInt("shoppinglist_id"));
                item.setDispId(rs.getInt("dips_id"));
                itemList.add(item);
            }

            rs.close();
            ps.close();
            return itemList;
        } finally {
            connection.close();
        }
    }

    public static ArrayList<Item> getItemsInDisbursement(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM item WHERE dips_id=?");
            ps.setInt(1,id);
            rs = ps.executeQuery();

//            TODO kan ikke teste her, hvordan
//            if(!rs.next()) {
//                log.info("could not find item " + id);
//            }

            Item item = new Item();
            ArrayList<Item> itemList = new ArrayList<Item>();
            while(rs.next()) {
                log.info("Found item in disbursement" + id);
                item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
                item.setShoppingListId(rs.getInt("shoppinglist_id"));
                item.setDispId(rs.getInt("dips_id"));
                itemList.add(item);
            }

            rs.close();
            ps.close();
            return itemList;
        } finally {
            connection.close();
        }
    }

    public static Boolean addItem(Item item) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO `item`(`name`, `status`, `shoppinglist_id`, `dips_id`) " +
                    "VALUES (?,?,?,?)");
            ps.setString(1,item.getName());
            ps.setInt(2,item.getStatus());
            ps.setInt(3,item.getShoppingListId());
            ps.setInt(4,item.getDispId());
            int result = ps.executeUpdate();

            ps.close();
            log.info("Add item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public static Boolean updateItem(Item item) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE item set id=?, name=?, status=?, shoppinglist_id=?, dips_id=? where id=?");
            ps.setInt(1,item.getId());
            ps.setString(2,item.getName());
            ps.setInt(3,item.getStatus());
            ps.setInt(4,item.getStatus());
            ps.setInt(5,item.getStatus());
            ps.setInt(6,item.getId());
            int result = ps.executeUpdate();

            ps.close();
            log.info("Update item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public static boolean delItem(int itemId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM item where id=?");
            ps.setInt(1, itemId);
            int result = ps.executeUpdate();

            ps.close();
            log.info("Delete item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}
