package db;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import data.*;
import org.junit.*;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class DisbursementDaoTest {
    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static DisbursementDao disbursementDao;

    private static Member member;

    private static final String dName="disbursementTest1";
    private static final Timestamp dDate = new Timestamp(System.currentTimeMillis());
    private static final double dValue = 200;
    private static final int id1 = 2000000001;
    private static final ArrayList<Item> items = new ArrayList<Item>();
    private static final ArrayList<User> participants = new ArrayList<User>();

    private static Group group = new Group(id1,"disbursementtestgroup","","");
    private static final Member dPayer=new Member("payeremail","payername","payerphone","payerpw","payersalt",1000,Member.ACCEPTED_STATUS);
    private static final Disbursement disbursement = new Disbursement();
    private static final Disbursement disbursement2 = new Disbursement();
    private static final Item item1 = new Item(id1,"item1",Item.STANDARD,id1,-1);
    private static final Item item2 = new Item(id1+1,"item2",Item.STANDARD,-1,id1);
    private static final Member other = new Member("otheremail","o","o","o","o",1000,Member.ACCEPTED_STATUS);

    @BeforeClass
    public static void setUpClass() throws Exception {
        items.add(item1);
        items.add(item2);
//        participants.add(other);
        participants.add(dPayer);

        disbursement.setId(id1);
        disbursement.setDisbursement(dValue);
        disbursement.setName(dName);
        disbursement.setDate(dDate);
        disbursement.setPayer(dPayer);
        disbursement.setItems(items);
        disbursement.setParticipants(participants);

        disbursement2.setId(id1+1);
        disbursement2.setDisbursement(dValue);
        disbursement2.setName(dName+"2");
        disbursement2.setDate(dDate);
        disbursement2.setPayer(dPayer);
        disbursement2.setItems(items);
        disbursement2.setParticipants(participants);
        try{
            connection = Db.instance().getConnection();
            connection.setAutoCommit(false);
            disbursementDao = new DisbursementDao(connection);

            int updatedrowcount=0;
            //Group
            ps = connection.prepareStatement("INSERT INTO party (id, name) VALUES (?,?);");
            ps.setInt(1,id1);
            ps.setString(2,"disbursementtestgroup");
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);

            //User
            ps=connection.prepareStatement("INSERT INTO user (name, email, password, salt, phone) VALUES (?,?,?,?,?)");
            for(User u: participants){
                ps.setString(1,u.getName());
                ps.setString(2,u.getEmail());
                ps.setString(3,u.getPassword());
                ps.setString(4,u.getSalt());
                ps.setString(5,u.getPhone());
                ps.addBatch();
            }
            int r[]=ps.executeBatch();
            for(int i: r){
                updatedrowcount+=i;
            }
            Db.close(ps);

            //Member
            ps=connection.prepareStatement("INSERT INTO user_party (user_email, party_id, balance, status) VALUES (?,?,?,?)");
            ps.setString(1,dPayer.getEmail());
            ps.setInt(2,group.getId());
            ps.setDouble(3,dPayer.getBalance());
            ps.setInt(4,dPayer.getStatus());
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);


            //Disbursement
            ps=connection.prepareStatement("INSERT INTO disbursement (id, price, name, date, payer_id, party_id) VALUES (?,?,?,?,?,?)");
            ps.setInt(1,disbursement.getId());
            ps.setDouble(2,disbursement.getDisbursement());
            ps.setString(3,disbursement.getName());
            ps.setTimestamp(4,disbursement.getDate());
            ps.setString(5,disbursement.getPayer().getEmail());
            ps.setInt(6,group.getId());
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);
            //Disbursement2
            ps=connection.prepareStatement("INSERT INTO disbursement (id, price, name, date, payer_id, party_id) VALUES (?,?,?,?,?,?)");
            ps.setInt(1,disbursement2.getId());
            ps.setDouble(2,disbursement2.getDisbursement());
            ps.setString(3,disbursement2.getName());
            ps.setTimestamp(4,disbursement2.getDate());
            ps.setString(5,disbursement2.getPayer().getEmail());
            ps.setInt(6,group.getId());
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);

            //User_Disbursement
            ps=connection.prepareStatement("INSERT INTO user_disbursement (user_email, disp_id) VALUES (?,?)");
            for(int i=0;i<=1;i++){
                for(User u: disbursement.getParticipants()){
                    ps.setString(1,u.getEmail());
                    ps.setInt(2,disbursement.getId()+i);
                    ps.addBatch();
                }
            }
            System.out.println("\ndisbID: "+disbursement.getId());
            r=ps.executeBatch();
            for(int i: r){
                updatedrowcount+=i;
            }
            Db.close(ps);
        }catch(MySQLIntegrityConstraintViolationException e){
            log.error("Primary key failure",e);
        }
        catch(SQLException e){
            log.error("SQL error",e);
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        try{
            connection.rollback();
            connection.setAutoCommit(true);
        }finally {
            Db.close(connection);
        }
    }

    @Test
    public void getDisbursementsInGroup() {
        ArrayList<Disbursement> disbursementsFromDao=null;
        try {
            disbursementsFromDao=disbursementDao.getDisbursementsInGroup
                    (group.getId(),disbursement.getParticipants().get(0).getEmail());
        } catch (SQLException e) {
            log.error("getDisbursementsInGroup errored",e);
        }
        assertNotNull(disbursementsFromDao);
        assertEquals(2,disbursementsFromDao.size());
        Disbursement disbursementFromDao = disbursementsFromDao.get(0);
        assertEquals(disbursement.getId(),disbursementFromDao.getId());
        assertEquals(disbursement.getName(),disbursementFromDao.getName());
        assertEquals(disbursement.getPayer().getEmail(),disbursementFromDao.getPayer().getEmail());
        assertEquals(disbursement.getDisbursement(),disbursementFromDao.getDisbursement(),0.009);
        assertEquals(disbursement.getDate().getTime(),disbursementFromDao.getDate().getTime(),999);
    }

    @Test
    public void getDisbursementDetails() {
        Disbursement disbursementFromDao = null;
        try{
            disbursementFromDao=disbursementDao.getDisbursementDetails
                    (disbursement.getId(),disbursement.getParticipants().get(0).getEmail());
        }catch(SQLException e){
            log.error("getDisbursementDetails errored", e);
        }
        assertEquals(disbursement.getId(),disbursementFromDao.getId());
        assertEquals(disbursement.getName(),disbursementFromDao.getName());
        assertEquals(disbursement.getPayer(),disbursementFromDao.getPayer());
        assertEquals(disbursement.getDisbursement(),disbursementFromDao.getDisbursement(),0.009);
        assertEquals(disbursement.getDate(),disbursementFromDao.getDate());
        assertEquals(disbursement.getParticipants().size(),disbursementFromDao.getParticipants().size());
        assertEquals(disbursement.getItems().size(),disbursementFromDao.getItems().size());
        assertEquals(disbursement.getItems().get(0).getId(),disbursementFromDao.getItems().get(0).getId());
        assertEquals(disbursement.getItems().get(1).getId(),disbursementFromDao.getItems().get(1).getId());
        assertEquals(disbursement.getParticipants().get(0).getEmail(),disbursementFromDao.getParticipants().get(0).getEmail());
        assertEquals(disbursement.getParticipants().get(1).getEmail(),disbursementFromDao.getParticipants().get(1).getEmail());
    }

    @Test
    public void addDisbursement() {
    }
}