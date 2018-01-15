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
 */
public class ChoreDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    private static ArrayList<String> findCompletedBy(int choreID) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps= connection.prepareStatement("SELECT user_id FROM chore_log WHERE chore_id=?");
            ps.setInt(1,choreID);
            rs = ps.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            while(rs.next()){
                result.add(rs.getString("user_id"));
            }
            rs.close();
            ps.close();
            return result;
        }
        finally {
            connection.close();
        }
    }

    public static Chore getChore(int choreId) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM chore WHERE id=?");
            ps.setInt(1,choreId);
            ResultSet res = ps.executeQuery();
            Chore chore = null;
            if(res.next()){
                log.info("Found Todo with id: " + choreId);
                String regularityRead = rs.getString("regularity");
                int regularity = Integer.parseInt(regularityRead);
                if(regularity>0){
                    chore =new RepeatedChore(regularity);
                }
                else{
                    chore = new Chore();
                }
                chore = new Chore();
                chore.setDescription(rs.getString("description"));
                ArrayList<String> completedBy = findCompletedBy(choreId);
                chore.setCompletedBy(completedBy);
                chore.setAssignedTo(rs.getString("assignedTo"));
                chore.setDeadline(rs.getDate("deadline"));
            }
            else{
                log.info("Could not find Todo");
            }
            rs.close();
            ps.close();
            return chore;
        }
        finally {
            connection.close();
        }
    }

    public static boolean addChore(Chore chore, int partyId) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO chore(description, regularity, deadline, party_id, user_email) VALUES (?,?,?,?,?,?)");
            ps.setString(1, chore.getDescription());
            ps.setInt(2, 0);
            ps.setDate(3, (Date) chore.getDeadline());
            ps.setInt(4, partyId);
            ps.setString(5, chore.getAssignedTo());
            int result = ps.executeUpdate();
            ps.close();
            return result == 1;
        }
        finally {
            connection.close();
        }
    }

    public static boolean assignChore(String email, int id) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("UPDATE chore set user_email=? where id=?");
            ps.setString(1,email);
            ps.setInt(2,id);
            int result = ps.executeUpdate();
            ps.close();
            return result==1;
        }
        finally{
            connection.close();
        }
    }




}
