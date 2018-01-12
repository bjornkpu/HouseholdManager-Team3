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
                item.setItemId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
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

    public static Boolean addItem(Item item) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO item (id,name,status) VALUES(?,?,?)");
            ps.setInt(1,item.getItemId());
            ps.setString(2,item.getName());
            ps.setInt(3,item.getStatus());
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
            ps = connection.prepareStatement("UPDATE item set id=?, name=?, status=? where id=?");
            ps.setInt(1,item.getItemId());
            ps.setString(2,item.getName());
            ps.setInt(3,item.getStatus());
            ps.setInt(4,item.getItemId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

    public static Boolean delItem(Item item) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM item where id=?");
            ps.setString(1,""+item.getItemId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Delete item " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}
