package db;

import data.StatisticsHelp;

import java.sql.*;
import java.util.ArrayList;
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

    public StatisticsDao(Connection connection) {
        this.connection=connection;
    }
    public ArrayList<StatisticsHelp> getChoresPerUser(int groupId) throws SQLException{
        try{
            ps = connection.prepareStatement("SELECT COUNT(c1.chore_id), u.name FROM user u JOIN (chore c2 INNER JOIN chore_log c1 ON c2.id = c1.chore_id) ON u.email=c1.user_email WHERE c2.party_id=? GROUP BY c1.user_email");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();
            ArrayList<StatisticsHelp> resultat = new ArrayList<>();
            while(rs.next()){
                StatisticsHelp h = new StatisticsHelp(rs.getString("name"),rs.getInt("COUNT(c1.chore_id)"));
                resultat.add(h);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

    public ArrayList<StatisticsHelp> getDisbursementCostPerUser(int groupId) throws SQLException{
        try{
            ps = connection.prepareStatement("SELECT SUM(d.price), u.name FROM user u JOIN disbursement d ON u.email=d.payer_id WHERE d.party_id=? GROUP BY u.name;");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();
            ArrayList<StatisticsHelp> resultat = new ArrayList<>();
            while(rs.next()){
                StatisticsHelp h = new StatisticsHelp(rs.getString("name"),rs.getDouble("SUM(d.price)"));
                resultat.add(h);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

    public ArrayList<StatisticsHelp> getChorStatusCount(int groupId) throws SQLException{
        try{
            ps = connection.prepareStatement("SELECT COUNT(*) AS status, 'available' AS description FROM chore WHERE party_id=? AND user_email IS NULL UNION SELECT COUNT(*) AS status, 'assigned' AS description FROM chore WHERE party_id=? AND user_email IS NOT NULL");
            ps.setInt(1,groupId);
            ps.setInt(2,groupId);
            rs = ps.executeQuery();
            ArrayList<StatisticsHelp> resultat = new ArrayList<>();
            while(rs.next()){
                StatisticsHelp h = new StatisticsHelp(rs.getString("description"),rs.getInt("status"));
                resultat.add(h);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

    public ArrayList<StatisticsHelp> getUserDebt(int groupId) throws SQLException{
        try{
            ps = connection.prepareStatement("SELECT name, balance*-1 FROM user_party up JOIN user u ON up.user_email=u.email WHERE balance < 0 AND party_id=? AND (status LIKE 2 OR status LIKE 1)");
            ps.setInt(1,groupId);
            rs = ps.executeQuery();
            ArrayList<StatisticsHelp> resultat = new ArrayList<>();
            while(rs.next()){
                StatisticsHelp h = new StatisticsHelp(rs.getString("name"),rs.getInt("balance*-1"));
                resultat.add(h);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
        }
    }


    //Let you find number of chores for user during the "dayNr" recent days.
    public ArrayList<StatisticsHelp> getChoresPerUser(int groupId, int dayNr) throws SQLException{
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
            ArrayList<StatisticsHelp> resultat = new ArrayList<>();
            while(rs.next()){
                StatisticsHelp h = new StatisticsHelp(rs.getString("user_email"),rs.getInt("COUNT(chore_log.chore_id)"));
                resultat.add(h);
            }
            return resultat;
        }
        finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

}
