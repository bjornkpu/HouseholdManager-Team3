package db;

import data.*;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingListDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ShoppingList getShoppingList(String id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM shoppinglist WHERE id=?");
            ps.setString(1,id);
            rs = ps.executeQuery();

            ShoppingList sl = null;
            if(rs.next()) {
                log.info("Found shoppinglist " + id);
                sl = new ShoppingList();
                //sl.setShoppingListId(rs.getInt("id"));
                //sl.setShoppingListName(rs.getString("id"));
//                sl.setItemList(rs.getArray("id"));
//                sl.setUserList(rs.getString("id"));

            } else {
                log.info("Could not find user " + id);
            }
            rs.close();
            ps.close();
            return sl;
        } finally {
            connection.close();
        }
    }

}
