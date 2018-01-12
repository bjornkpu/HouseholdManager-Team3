package db;

import data.*;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public static ArrayList<ShoppingList> getShoppingListByGroupid(int groupId) throws SQLException{
        connection = Db.instance().getConnection();
        try {

            if(!groupCheck(groupId)){ //checks if the party-object exists
                log.info("Could not find party " + groupId);
                //TODO should this return null
                return null;
            }

            ps = connection.prepareStatement("SELECT * FROM shoppinglist WHERE party_id = ?");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();

//            TODO kan ikke teste her, hvordan
//            if(!rs.next()) {
//                log.info("could not find item " + id);
//            }

            ShoppingList sl = new ShoppingList();
            ArrayList<ShoppingList> shoppinglistList = new ArrayList<ShoppingList>();

            while(rs.next()) {
                log.info("Found ShoppingList in group " + groupId);
                sl = new ShoppingList();
                sl.setId(rs.getInt("id"));
                sl.setName(rs.getString("name"));
                sl.setGroupId(rs.getInt("party_id"));
                sl.setItemList(getItemList(sl.getId()));
                sl.setUserList(getUserList(sl.getId()));
                shoppinglistList.add(sl);
            }

            rs.close();
            ps.close();
            return shoppinglistList;
        } finally {
            connection.close();
        }
    }

    public static boolean addShoppingList(ShoppingList shoppingList) throws SQLException {
        connection = Db.instance().getConnection();
        try {

            if(!groupCheck(shoppingList.getGroupId())){ //checks if the party-object exists
                log.info("Could not find party " + shoppingList.getGroupId());
                return false;
            }

            //creates new shoppinglist object in shoppinglist table
            ps = connection.prepareStatement("INSERT INTO " +
                    "shoppinglist(id, name, party_id) VALUES (?,?,?)");
            ps.setInt(1, shoppingList.getId());
            ps.setString(2, shoppingList.getName());
            ps.setInt(3, shoppingList.getGroupId());
            int result = ps.executeUpdate();
            log.info("Create shoppinglist " + (result == 1?"ok":"failed"));
//            ps.close();

            //creates new connection between created shoppinglist and creator (User)
            ps = connection.prepareStatement("INSERT INTO " +
                    "shoppinglist_user(shoppinglist_id, user_email) VALUES (?,?)");
            ps.setInt(1, shoppingList.getId());
            ps.setString(2, shoppingList.getUserList().get(0).getEmail());
            ps.close();
            log.info("Add shoppinglist_user dependancy " + (result == 1?"ok":"failed"));
            return result == 1;

        } finally {
            connection.close();
        }
    }

    private static boolean groupCheck(int i) throws SQLException{ //tests if a party exists with specified id
        return GroupDao.getGroup(i) != null;
    }

    public static boolean updateShoppingList(ShoppingList shoppingList) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE shoppinglist set name=?, party_id=? where id=?");
            ps.setString(1, shoppingList.getName());
            ps.setInt(2, shoppingList.getGroupId());
            ps.setInt(3, shoppingList.getId());
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update shoppinglist " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }

//    TODO: delete the items in the list, not just the list itself
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

            Item item = new Item();
            ArrayList<Item> itemList = null;
            while(rs.next()) {
                log.info("Found item " + id);
                item = new Item();
                item.setItemId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setStatus(rs.getInt("status"));
                itemList.add(item);
            }

//            TODO clean this up
//            rs.close();
//            ps.close();
            return itemList;
        } finally {
//            connection.close();
        }
    }

    private static ArrayList<User> getUserList(int id) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT u.email, u.name, u.phone, u.password, u.salt " +
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
//        TODO clean this up
//            rs.close();
//            ps.close();
            return userList;
        } finally {
//            connection.close();
        }
    }
}
