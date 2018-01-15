package db;

import data.WallPost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * -Description of the class-
 *
 * @author
 * matseda
 */

public class WallpostDao {

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ArrayList<WallPost> getWallposts(int partyId) throws SQLException{
        connection=Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM wallpost WHERE party_id=?");
            ps.setInt(1,partyId);
            rs = ps.executeQuery();
            ArrayList<WallPost> resultat = new ArrayList<>();
            while(rs.next()){
                WallPost wallPost = new WallPost(rs.getTimestamp("time"),rs.getString("message"),rs.getString("user_email"),rs.getInt("party_id"));
                resultat.add(wallPost);
            }
            rs.close();
            ps.close();
            return resultat;
        }finally {
            connection.close();
        }
    }

    public static boolean postWallpost(WallPost wallPost) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("INSERT INTO wallpost(message, party_id, user_email) VALUES (?,?,?)");
            ps.setString(1,wallPost.getMessage());
            ps.setInt(2,wallPost.getPostedTo());
            ps.setString(3,wallPost.getPostedBy());
            int res = ps.executeUpdate();
            ps.close();
            return res==1;
        }
        finally {
            connection.close();
        }
    }

    public static ArrayList<WallPost> getWallposts(String email, int partyId) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * WHERE party_id=? AND user_email=?");
            ps.setInt(1,partyId);
            ps.setString(2,email);
            ArrayList<WallPost> resultat = new ArrayList<>();
            rs = ps.executeQuery();
            while(rs.next()){
                WallPost wallPost = new WallPost(rs.getTimestamp("time"),rs.getString("message"),rs.getString("user_email"),rs.getInt("party_id"));
                resultat.add(wallPost);
            }
            rs.close();
            ps.close();
            return resultat;
        }
        finally{
            connection.close();
        }
    }

    public static boolean deleteWallpost(int id) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM wallpost WHERE id=?");
            ps.setInt(1,id);
            int resultat = ps.executeUpdate();
            ps.close();
            return resultat == 1;
        }
        finally {
            connection.close();
        }
    }
}
