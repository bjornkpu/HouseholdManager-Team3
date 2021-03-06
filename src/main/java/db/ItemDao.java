package db;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import data.Item;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
/**
 * Data access object for Item
 *
 * @author BK
 * @author jmska
 */
public class ItemDao {
    private static final Logger log = Logger.getLogger();

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public ItemDao(Connection connection) {
        this.connection = connection;
    }

    /** Method that gets an Item given an id.
     * @param id The id of the item that is to be returned.
     * @return An item with information relative to the id given.
     * @throws SQLException when failing to get Item.
     */
    public Item getItem(int id) throws SQLException {
//        connection = Db.instance().getConnection();
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
                item.setDisbursementId(rs.getInt("disbursement_id"));
            } else {
                log.info("could not find item " + id);
            }
            return item;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

//    /** Method that gets any items connected to a given ShoppingList.
//     * @param id The id of the ShoppingList the items are connected to.
//     * @return An ArrayList of items connected to the given ShoppingList.
//     * @throws SQLException when failing to get Item.
//     */
//    public ArrayList<Item> getItemsInShoppingList(int id) throws SQLException {
//        connection = Db.instance().getConnection();
//        try{
//	        return getItemsInShoppingListMethod(id, connection);
//        }finally {
//	        Db.close(connection);
//        }
//    }
//
//    /** Method that gets any items connected to a given ShoppingList.
//     * @param id The id of the ShoppingList the items are connected to.
//     * @return An ArrayList of items connected to the given ShoppingList.
//     * @throws SQLException when failing to get Item.
//     */
//    public ArrayList<Item> getItemsInShoppingList(int id, Connection connection) throws SQLException {
//	    return getItemsInShoppingListMethod(id, connection);
//    }

    /**
     * Method returning items contained by a given shoppinglist
     * @param id of the shoppinglist containig the
     * @return
     * @throws SQLException
     */
    public ArrayList<Item> getItemsInShoppingList(int id) throws SQLException {
        connection = Db.instance().getConnection();
	    try {
		    ps = connection.prepareStatement("SELECT * FROM item WHERE shoppinglist_id=?");
		    ps.setInt(1,id);
		    rs = ps.executeQuery();

		    log.info("Result set found successfully in Item. ");

		    int i =0;
		    Item item = new Item();
		    ArrayList<Item> itemList = new ArrayList<Item>();
		    while(rs.next()) {
			    //log.info("Found item in shoppinglist" + id + " while index: " + i);
			    item = new Item();
			    item.setId(rs.getInt("id"));
			    item.setName(rs.getString("name"));
			    item.setStatus(rs.getInt("status"));
			    item.setShoppingListId(rs.getInt("shoppinglist_id"));
			    item.setDisbursementId(rs.getInt("disbursement_id"));
			    itemList.add(item);
			    i++;
		    }
		    return itemList;
	    } finally {
		    Db.close(rs);
		    Db.close(ps);
		    Db.close(connection);
	    }
    }

    /** Method that gets any items connected to a given Disbursement.
     * @param disbursementId The id of the Disbursement the items are connected to.
     * @return An ArrayList of items connected to the given Disbursement.
     * @throws SQLException when failing to get Item.
     */
    public ArrayList<Item> getItemsInDisbursement(int disbursementId) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM item WHERE disbursement_id=?");
            ps.setInt(1,disbursementId);
            rs = ps.executeQuery();

            Item item;
            ArrayList<Item> itemList = new ArrayList<Item>();
            if(rs.next()){
                do{
                    log.info("Found item in disbursement" + disbursementId);
                    item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setName(rs.getString("name"));
                    item.setStatus(rs.getInt("status"));
                    item.setShoppingListId(rs.getInt("shoppinglist_id"));
                    item.setDisbursementId(rs.getInt("disbursement_id"));
                    itemList.add(item);
                }while(rs.next());
            }else{
                log.info("Found no items in disbursement "+ disbursementId);
            }
            return itemList;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** Method that adds an item to the database.
     * @param item The item that is to be added to the database.
     * @throws SQLException when failing to add Item.
     */
    public Boolean addItem(Item item) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO `item`(`name`, `status`, `shoppinglist_id`, `disbursement_id`,id) " +
                    "VALUES (?,?,?,?,?)");
            ps.setString(1,item.getName());
            ps.setInt(2,item.getStatus());
            if(item.getShoppingListId()==-1){
                ps.setNull(3, Types.INTEGER);
            }else {
                ps.setInt(3,item.getShoppingListId());
            }if (item.getDisbursementId()==-1){
                ps.setNull(4, Types.INTEGER);
            }else {
                ps.setInt(4,item.getDisbursementId());
            }if (item.getId()==0){
                ps.setNull(5, Types.INTEGER);
            }else {
                ps.setInt(5,item.getId());
            }

            int result = ps.executeUpdate();
            log.info("Add item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** Method that updates any and all information in an item given an id.
     * @param item The item with an id of the item that is to be updated, as well as any information to be updated.
     * @throws SQLException when failing to update Item.
     */
    public Boolean updateItem(Item item) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE item set id=?, name=?, status=?, shoppinglist_id=?, disbursement_id=? where id=?");
            ps.setInt(1,item.getId());
            ps.setString(2,item.getName());
            ps.setInt(3,item.getStatus());
            if(item.getShoppingListId() == -1 || item.getShoppingListId() == 0){
                ps.setNull(4, Types.INTEGER);
            }else {
                ps.setInt(4,item.getShoppingListId());
            }
            log.info("disb id = " + item.getDisbursementId());
            if (item.getDisbursementId() == -1 || item.getDisbursementId()== 0){
                log.info("Sets null");
                ps.setNull(5, Types.INTEGER);
            }else {
                log.info("Sets disb id");
                ps.setInt(5,item.getDisbursementId());
            }
            ps.setInt(6,item.getId());
            int result = ps.executeUpdate();
            log.info("Update item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** Method that deletes an item from the database.
     * @param id The id of the item that is to be deleted.
     * @throws SQLException when failing to delete Item.
     */
    public boolean delItem(int id) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM item where id=?");
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            log.info("Delete item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }
}
