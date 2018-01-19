package db;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;

/**
 * -Description of the class-
 *
 * @author
 * matseda
 */
public class StatisticsDao {

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    static HashMap<String, Integer> getChoresPerUser(int groupId) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT COUNT(chore_log.chore_id), chore_log.user_email FROM chore NATURAL JOIN chore_log WHERE chore.party_id=? GROUP BY chore_log.user_email");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();
            HashMap<String, Integer> resultat = new HashMap<>();
            while(rs.next()){
                String email=rs.getString("user_email");
                int num = rs.getInt("Count(chore_log.chore_id)");
                resultat.put(email,num);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }


    //Let you find number of chores for user during the "dayNr" recent days.
    static HashMap<String, Integer> getChoresPerUser(int groupId, int dayNr) throws SQLException{
        connection = Db.instance().getConnection();
        Timestamp ts = timestamp;
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.DAY_OF_WEEK, dayNr);
        ts.setTime(cal.getTime().getTime()); // or
        ts = new Timestamp(cal.getTime().getTime());
        try{
            ps = connection.prepareStatement("SELECT COUNT(chore_log.chore_id), chore_log.user_email FROM chore NATURAL JOIN chore_log WHERE chore.party_id=? AND done >=? GROUP BY chore_log.user_email");
            ps.setInt(1,groupId);
            ps.setTimestamp(2,ts);
            rs = ps.executeQuery();
            HashMap<String, Integer> resultat = new HashMap<>();
            while(rs.next()){
                String email=rs.getString("user_email");
                int num = rs.getInt("Count(chore_log.chore_id)");
                resultat.put(email,num);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

}
