package db;

import data.WallPost;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * -Data access object for Wallpost-
 *
 * @author matseda
 */

public class WallpostDao {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    public WallpostDao(){}

    public WallpostDao(Connection connection) {
        this.connection=connection;
    }

    /** gets a list of wallposts in a group
     * @param partyId the id of the group you want to get all wallposts from
     * @return an ArrayList of WallPosts on the given group ID
     * @throws SQLException if the query fails
     */
    public ArrayList<WallPost> getWallposts(int partyId) throws SQLException{
//        connection=Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM wallpost WHERE party_id=?");
            ps.setInt(1,partyId);
            rs = ps.executeQuery();
            ArrayList<WallPost> resultat = new ArrayList<>();
            while(rs.next()){
                WallPost wallPost = new WallPost(rs.getInt("id"),rs.getTimestamp("time"),rs.getString("message"),rs.getString("user_email"),rs.getInt("party_id"));
                resultat.add(wallPost);
            } return resultat;
        }finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** adds a wallpost to the database including the group id the wallpost belongs to
     * @param wallPost the wallpost you want to "post"
     * @return true if the wallpost is added successfully
     * @throws SQLException if the query fails
     */
    public boolean postWallpost(WallPost wallPost) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("INSERT INTO wallpost(message, party_id, user_email) VALUES (?,?,?)");
            ps.setString(1,wallPost.getMessage());
            ps.setInt(2,wallPost.getPostedTo());
            ps.setString(3,wallPost.getPostedBy());
            int res = ps.executeUpdate();
            return res==1;
        }
        finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** gets a wallpost by the party ID
     * @param email the id of the one asking, for authentication
     * @param partyId the id of the group you want to get all wallposts from
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public ArrayList<WallPost> getWallposts(String email, int partyId) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM wallpost WHERE party_id=? AND user_email=?");
            ps.setInt(1,partyId);
            ps.setString(2,email);
            ArrayList<WallPost> resultat = new ArrayList<WallPost>();
            rs = ps.executeQuery();
            while(rs.next()){
                WallPost wallPost = new WallPost();
                wallPost.setMessage(rs.getString("message"));
                wallPost.setDatePosted(rs.getTimestamp("time"));
                wallPost.setId(rs.getInt("id"));
                wallPost.setPostedBy(rs.getString("user_email"));
                wallPost.setPostedTo(rs.getInt("party_id"));
                resultat.add(wallPost);
            } return resultat;
        }
        finally{
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** deletes a wallpost from teh databse
     * @param id the idof the wallpost
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public boolean deleteWallpost(int id) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM wallpost WHERE id=?");
            ps.setInt(1,id);
            int resultat = ps.executeUpdate();
            return resultat == 1;
        }
        finally {
            Db.close(ps);
//            Db.close(connection);
        }
    }
}
