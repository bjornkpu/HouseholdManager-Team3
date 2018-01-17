package db;

import data.Disbursement;
import data.Item;
import data.User;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * -Description of the class-
 *
 * @author johanmsk
 */
public class DisbursementDao {
    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ArrayList<Disbursement> getDisbursementsInGroup(int groupId, String email) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud ON d.id = ud.disp_id WHERE d.party_id=? AND ud.user_email=?");
            ps.setInt(1,groupId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            ArrayList<Disbursement> disbursements= null;
            Disbursement disbursement = null;
            if(rs.first()){
                disbursements = new ArrayList<Disbursement>();
                do{
                    log.info("Found disbursement " + groupId + " in group: " +groupId+ " with user: "+email);
                    disbursement = new Disbursement();
                    disbursement.setId(rs.getInt("id"));
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    disbursements.add(disbursement);
                }while(rs.next());
            } else {
                log.info("Could not find any disbursement in group: "+groupId+" user: " + email);
            }
            rs.close();
            ps.close();
            return disbursements;
        } finally {
            connection.close();
        }
    }

    public static  Disbursement getDisbursementDetails(int disbursementId, String email)throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud ON d.id = ud.disp_id WHERE d.id=? AND ud.user_email=?");
            ps.setInt(1,disbursementId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            ArrayList<Item> items = new ArrayList<Item>();
            ArrayList<User> users = new ArrayList<User>();
            Disbursement disbursement = null;
            if(rs.first()){
                do{
                    log.info("Found disbursement " + disbursementId);
                    disbursement = new Disbursement();
                    disbursement.setId(rs.getInt("id"));
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    disbursement.setParticipants(UserDao.getUsersInDisbursement(disbursementId));
                    disbursement.setItems(ItemDao.getItemsInDisbursement(disbursementId));
                }while(rs.next());
            } else {
                log.info("Could not find any disbursement with id: "+disbursementId);
            }
            rs.close();
            ps.close();
            return disbursement;
        } finally {
            connection.close();
        }
    }

    public static boolean addDisbursement(Disbursement disbursement, int groupid)throws SQLException{
        connection = Db.instance().getConnection();
        connection.setAutoCommit(false);
        try {
            disbursement.setId(makeDisbursement(disbursement,groupid));
            if(disbursement.getId()>=0){
                if(addDisbursementToUsers(disbursement)){
                    connection.commit();
                    return true;
                }
            }

        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return false;
    }

    private static boolean addDisbursementToUsers(Disbursement disbursement) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO user_disbursement " +
                "(user_email,disp_id)" +
                "VALUES (?,?)");
        int i=0;
        for (User u : disbursement.getParticipants()) {
            ps.setString(1,u.getEmail());
            ps.setInt(2,disbursement.getId());
            ps.addBatch();
            i++;
            if(i % 1000 == 0 || i == disbursement.getParticipants().size()){
                int[] result =ps.executeBatch();
                for(int r : result){
                    if(r==Statement.EXECUTE_FAILED){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int makeDisbursement(Disbursement disbursement, int groupid) throws SQLException {
        Timestamp ts = new Timestamp(disbursement.getDate().getTime());
        String payerEmail = disbursement.getPayer().getEmail();
        ps = connection.prepareStatement("INSERT INTO disbursement " +
                "(price, name, date, payer_id, party_id) " +
                "VALUES (?,?,?,?,?)");
        ps.setDouble(1,disbursement.getDisbursement());
        ps.setString(2,disbursement.getName());
        ps.setTimestamp(3,new Timestamp(disbursement.getDate().getTime()));
        ps.setString(4,payerEmail);
        ps.setInt(5,groupid);
        int result = ps.executeUpdate();
        ps.close();
        log.info("Add user " + (result == 1?"ok":"failed"));
        ps=connection.prepareStatement("SELECT id FROM disbursement " +
                "WHERE date=? and payer_id=?");
        ps.setTimestamp(1,ts);
        ps.setString(2,payerEmail);
        rs=ps.executeQuery();
        if(rs.next()){
            return rs.getInt("id");
        }
        return -1;
    }
}
