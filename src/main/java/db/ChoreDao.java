package db;

import data.Chore;
import data.RepeatedChore;
import data.User;
import util.Logger;

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

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private ResultSet res;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public ChoreDao(Connection connection) {
        this.connection = connection;
    }

    /** Find what user completed the chore
     * @param choreID id of the chore you want to check
     * @return the email of teh user who is on the chore
     * @throws SQLException if the query fails
     */
    public ArrayList<String> getCompletedBy(int choreID) throws SQLException{
//        connection = Db.instance().getConnection();
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
//            Db.close(connection);
        }
    }

    /** gets the chore by id
     * @param choreId the chore you want to get
     * @return the chore
     * @throws SQLException if the query fails
     */
    public Chore getChore(int choreId) throws SQLException{
//        connection = Db.instance().getConnection();
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
                chore.setDeadline(rs.getTimestamp("deadline"));
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
//            Db.close(connection);
        }
    }

    /**
     * This method is called upon when a chore is completed and is to be logged.
     * @param choreId  Id of the chore wich is completed
     * @param users Wich members of the group completed this task
     * @return Boolean telling if the method was successful
     * @throws SQLException in case of error when connection to database.
     */
    public boolean setCompletedBy(int choreId, ArrayList<String> users) throws SQLException{
//        connection = Db.instance().getConnection();
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
//            Db.close(connection);
        }
    }

    /**
     * This method is called uppon when someone wants to display chores registered to a group.
     * @param partyId The id of the groups, who the chores are registered to.
     * @return List of the chores registered to the group
     * @throws SQLException in case of error when connection to database.
     */
    public ArrayList<Chore> getChores(int partyId) throws SQLException{
//        connection = Db.instance().getConnection();
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
                chore.setDeadline(res.getTimestamp("deadline"));
                resultat.add(chore);
            }
            return resultat;
        }
        finally {
            res.close();
            ps.close();
//            connection.close();
        }
    }

    /**
     * When adding a chore to a groups To-Do list
     * @param chore wich needs to be done.
     * @return boolean to tell if the registration was successful.
     * @throws SQLException in case of error when connection to database.
     */
    public boolean addChore(Chore chore) throws SQLException{
//        connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO chore(name, regularity, deadline, party_id) VALUES (?,0,?,?)");
            ps.setString(1, chore.getDescription());
            ps.setTimestamp(2, (java.sql.Timestamp) chore.getDeadline());
            ps.setInt(3, chore.getPartyId());
            int result = ps.executeUpdate();
            return result == 1;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** assign a user to a chore
     * @param user the user you want to assign to the task
     * @param id the id of the chore you want to add the user to
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public boolean assignChore(User user, int id) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("UPDATE chore set user_email=? where id=?");
            ps.setString(1,user.getEmail());
            ps.setInt(2,id);
            int result = ps.executeUpdate();
            return result==1;
        }
        finally{
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     *  When a chore is completed and no longer is needed to be stored
     * @param id of the chore
     * @return boolean to tell if the deletion was successful.
     * @throws SQLException in case of error when connection to database.
     */

    public boolean deleteChore(int id) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("DELETE FROM chore WHERE id=?");
            ps.setInt(1,id);
            int result = ps.executeUpdate();
            return result == 1;
        }
        finally{
            ps.close();
//            connection.close();
        }
    }




}
