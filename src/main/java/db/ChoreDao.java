package db;
import data.RepeatedChore;
import util.Logger;
import data.Chore;

import java.sql.*;
import java.util.ArrayList;
/**
 * -Description of the class-
 *
 * @author
 * matseda
 */
public class ChoreDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ResultSet res;
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    /** Find what user completed the chore
     * @param choreID id of the chore you want to check
     * @return the email of teh user who is on the chore
     * @throws SQLException if the query fails
     */
    static ArrayList<String> getCompletedBy(int choreID) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps= connection.prepareStatement("SELECT user_email FROM chore_log WHERE chore_id=?");
            ps.setInt(1,choreID);
            res = ps.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            while(res.next()){
                result.add(res.getString("user_email"));
            }
            return result;
        }
        finally {
            Db.close(res);
            Db.close(ps);
            Db.close(connection);
        }
    }

    /** gets the chore by id
     * @param choreId the chore you want to get
     * @return the chore
     * @throws SQLException if the query fails
     */
    public static Chore getChore(int choreId) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM chore WHERE id=?");
            ps.setInt(1,choreId);
            rs = ps.executeQuery();
            Chore chore = null;
            if(rs.next()){
                log.info("Found Todo with id: " + choreId);
                int regularity = rs.getInt("regularity");
                if(regularity>0){
                    chore =new RepeatedChore(regularity);
                }
                else{
                    chore = new Chore();
                }
                chore.setChoreId(rs.getInt("id"));
                chore.setDescription(rs.getString("name"));
                ArrayList<String> completedBy = getCompletedBy(choreId);
                chore.setCompletedBy(completedBy);
                chore.setAssignedTo(rs.getString("user_email"));
                chore.setDeadline(rs.getDate("deadline"));
                chore.setPartyId(rs.getInt("party_id"));
            }
            else{
                log.info("Could not find Todo");
            }
            return chore;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

    public static boolean setCompletedBy(int choreId, ArrayList<String> users) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            //Removes all previous data on completed by for this chore.
            ps = connection.prepareStatement("DELETE FROM chore_log WHERE chore_id=?");
            ps.setInt(1,choreId);
            ps.executeUpdate();
            for (String user : users) {
                ps = connection.prepareStatement("INSERT INTO chore_log(user_email,chore_id,done) VALUES (?,?,?)");
                ps.setString(1, user);
                ps.setInt(2, choreId);
                ps.setTimestamp(3, timestamp);
                int result = ps.executeUpdate();
                if (result != 1) return false;
            }
            return true;
        }
        finally {
            Db.close(ps);
            Db.close(connection);
        }
    }

    public static ArrayList<Chore> getChores(int partyId) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM chore WHERE party_id=?");
            ps.setInt(1,partyId);
            res = ps.executeQuery();
            ArrayList<Chore> resultat = new ArrayList<>();
            while(res.next()){
                Chore chore;
                if(res.getInt("regularity")>0){
                    chore = new RepeatedChore(res.getInt("regularity"));
                }
                else{
                    chore = new Chore();
                }
                chore.setPartyId(partyId);
                chore.setChoreId(res.getInt("id"));
                chore.setDescription(res.getString("name"));
                chore.setAssignedTo(res.getString("user_email"));
                chore.setDeadline(res.getDate("deadline"));
                resultat.add(chore);
            }
            return resultat;
        }
        finally {
            res.close();
            ps.close();
            connection.close();
        }
    }

    public static boolean addChore(Chore chore) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO chore(name, regularity, deadline, party_id) VALUES (?,0,?,?)");
            ps.setString(1, chore.getDescription());
            ps.setDate(2, (Date) chore.getDeadline());
            ps.setInt(3, chore.getPartyId());
            int result = ps.executeUpdate();
            return result == 1;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

    /** assign a user to a chore
     * @param email the id of the user
     * @param id the id of the chore you want to add the user to
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public static boolean assignChore(String email, int id) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("UPDATE chore set user_email=? where id=?");
            ps.setString(1,email);
            ps.setInt(2,id);
            int result = ps.executeUpdate();
            return result==1;
        }
        finally{
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

    public static boolean deleteChore(int id) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM chore WHERE id=?");
            ps.setInt(1,id);
            int result = ps.executeUpdate();
            return result == 1;
        }
        finally{
            ps.close();
            connection.close();
        }
    }




}
