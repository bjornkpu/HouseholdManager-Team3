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
                sl.setItemList(ItemDao.getItemList(id));
//                sl.setUserList(UserDao.getUserList(id));

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
