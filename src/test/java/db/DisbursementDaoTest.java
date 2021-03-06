package db;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import data.*;
import org.junit.*;
import util.Logger;

import java.sql.*;
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
    private static final ArrayList<User> users = new ArrayList<>();

    private static Group group = new Group(id1,"disbursementtestgroup","","");
    private static final Member dPayer=new Member("payeremail#unique","payername","payerphone","payerpw","payersalt",1000,Member.ACCEPTED_STATUS);
    private static final Member other = new Member("qtheremail#unique","o","o","o","o",1000,Member.ACCEPTED_STATUS);
    private static final Member other2 =new Member(other.getEmail()+2,"o","o","o","o",500,Member.ACCEPTED_STATUS);
    private static final Disbursement disbursement = new Disbursement();
    private static final Disbursement disbursement2 = new Disbursement();
    private static final Item item1 = new Item(id1,"item1",Item.STANDARD,id1,-1);
    private static final Item item2 = new Item(id1+1,"item2",Item.PURCHASED,-1,id1);

    @BeforeClass
    public static void setUpClass() throws Exception {
        users.add(other);
        users.add(other2);
        users.add(dPayer);
        items.add(item1);
        items.add(item2);
        participants.add(other);
        participants.add(other2);

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
        }catch(SQLException e){
            log.error("Error when getting connection",e);
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception{
        try{
            connection.setAutoCommit(true);
        }finally {
            Db.close(connection);
        }
    }

    @Before
    public void setUp() throws Exception {
        try{
            int updatedrowcount=0;
            //Group
            ps = connection.prepareStatement("INSERT INTO party (id, name) VALUES (?,?);");
            ps.setInt(1,id1);
            ps.setString(2,"disbursementtestgroup");
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);
            //Group2
            ps = connection.prepareStatement("INSERT INTO party (id, name) VALUES (?,?);");
            ps.setInt(1,id1+1);
            ps.setString(2,"disbursementtestgroup2");
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);

            //User
            ps=connection.prepareStatement("INSERT INTO user (name, email, password, salt, phone) VALUES (?,?,?,?,?)");
            for(User u: users){
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
            }Db.close(ps);

            //Member
            ps=connection.prepareStatement("INSERT INTO user_party (user_email, party_id, balance, status) VALUES (?,?,?,?)");
            for(User uNotM: users){
                Member u = (Member) uNotM;
                ps.setString(1,u.getEmail());
                ps.setInt(2,group.getId());
                ps.setDouble(3,u.getBalance());
                ps.setInt(4,u.getStatus());
                ps.addBatch();
            }
            r=ps.executeBatch();
            for(int i: r){
                updatedrowcount+=i;
            }Db.close(ps);


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
            //DisbursementDummy
            ps=connection.prepareStatement("INSERT INTO disbursement (price, name, date, payer_id, party_id) VALUES (?,?,?,?,?)");
            ps.setDouble(1,disbursement2.getDisbursement());
            ps.setString(2,disbursement2.getName());
            ps.setTimestamp(3,disbursement2.getDate());
            ps.setString(4,disbursement2.getPayer().getEmail());
            ps.setInt(5,id1+1);
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
            r=ps.executeBatch();
            for(int i: r){
                updatedrowcount+=i;
            }
            Db.close(ps);

            //Shoppinglist
            ps=connection.prepareStatement("INSERT INTO shoppinglist (id, party_id,name) VALUES (?,?,?)");
            ps.setInt(1, id1);
            ps.setInt(2, id1);
            ps.setString(3, "testnavn");
            updatedrowcount+=ps.executeUpdate();
            Db.close(ps);

            //Items
            ps=connection.prepareStatement("INSERT INTO item (id, name, status, shoppinglist_id, disbursement_id) VALUES (?,?,?,?,?)");
            for(Item i: disbursement.getItems()){
                ps.setInt(1,i.getId());
                ps.setString(2,i.getName());
                ps.setInt(3,i.getStatus());
                if(i.getShoppingListId()>0){
                    ps.setInt(4,i.getShoppingListId());
                }else{
                    ps.setNull(4, Types.INTEGER);
                }if(i.getDisbursementId()>0){
                    int disbid=i.getDisbursementId();
                    ps.setInt(5,disbid);
                }else{
                    ps.setNull(5,Types.INTEGER);
                }
                ps.addBatch();
            }
            r=ps.executeBatch();
            for(int i: r){
                updatedrowcount+=i;
            }
            Db.close(ps);

            assertEquals(18,updatedrowcount);

        }catch(MySQLIntegrityConstraintViolationException e){
            log.error("Primary key failure",e);
        }
        catch(SQLException e){
            log.error("SQL error",e);
        }
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();
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
        assertTrue(disbursementsFromDao.size()>=2);
        Disbursement disbursementFromDao = disbursementsFromDao.get(0);
        assertEquals(disbursement.getId(),disbursementFromDao.getId());
        assertEquals(disbursement.getName(),disbursementFromDao.getName());
        assertEquals(disbursement.getPayer().getEmail(),disbursementFromDao.getPayer().getEmail());
        assertEquals(disbursement.getDisbursement(),disbursementFromDao.getDisbursement(),0.0051);
        assertEquals(disbursement.getDate().getTime(),disbursementFromDao.getDate().getTime(),999);
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
        assertEquals(disbursement.getPayer().getEmail(),disbursementFromDao.getPayer().getEmail());
        assertEquals(disbursement.getDisbursement(),disbursementFromDao.getDisbursement(),0.009);
        assertEquals(disbursement.getDate().getTime(),disbursementFromDao.getDate().getTime(),999);
        assertEquals(disbursement.getParticipants().size(),disbursementFromDao.getParticipants().size());
        assertEquals(disbursement.getItems().size()-1,disbursementFromDao.getItems().size());
        assertEquals(disbursement.getItems().get(1).getId(),disbursementFromDao.getItems().get(0).getId());
        assertEquals(disbursement.getParticipants().get(0).getEmail(),disbursementFromDao.getParticipants().get(0).getEmail());
        assertEquals(disbursement.getParticipants().get(1).getEmail(), disbursementFromDao.getParticipants().get(1).getEmail());
    }

    @Test
    public void addDisbursement() {
        final Disbursement disbursement3=new Disbursement();
        disbursement3.setDisbursement(dValue);
        disbursement3.setName(dName+3);
        disbursement3.setDate(dDate);
        disbursement3.setPayer(dPayer);
        ArrayList<Item> items3 = new ArrayList<>();
        items3.add(item1);
        disbursement3.setItems(items3);
        disbursement3.setParticipants(participants);
        Member m;

        boolean dbUpdated=false;
        boolean added=false;
        int itemDisbId=0;
        double payerbalance=0;
        double otherbalance=0;

        ResultSet rs=null;
        try{
            added = disbursementDao.addDisbursement(disbursement3,id1);
//            connection.commit();
            System.out.println("disb3ID : "+disbursement3.getId());

            //Check that disbursement is in DB
            ps=connection.prepareStatement("SELECT COUNT(*) FROM disbursement WHERE id=?");
            ps.setInt(1,disbursement3.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                dbUpdated=(rs.getInt(1)==1);
            }
            Db.close(rs);
            Db.close(ps);

            //Check that items are in disbursement
            ps=connection.prepareStatement("SELECT disbursement_id FROM item WHERE id=? AND status=?");
            Item i=disbursement3.getItems().get(0);
            ps.setInt(1,i.getId());
            ps.setInt(2,Item.PURCHASED);
            rs=ps.executeQuery();
            if(rs.next()){
                itemDisbId=rs.getInt(1);
            }

        }catch(SQLException e){
            log.error("addDisbursement errored",e);
        }finally {
            Db.close(rs);
            Db.close(ps);
        }

        assertTrue(added);
        assertTrue(dbUpdated);
//        assertEquals(1100,payerbalance,0.0051);
//        assertEquals(900,otherbalance,0.0051);
        assertEquals(disbursement3.getId(),itemDisbId);
    }

    @Test
    public void updateBalances(){
        final Disbursement disbursement3=disbursement.copy();
        ArrayList<User> participants1 = new ArrayList<>();
        participants1.add(other);
        participants1.add(other2);
        disbursement3.setParticipants(participants1);

        boolean added=false;
        double payerbalance=0;
        double otherbalance=0;
        double other2balance=0;

        Member m;
        ResultSet rs=null;
        try{
            added = disbursementDao.updateBalances(disbursement3,id1);
//            connection.commit();

            //Check that participants and payer balances are updated.
            ps=connection.prepareStatement("SELECT balance FROM user_party WHERE user_email=? AND party_id=?");
            m = (Member) disbursement3.getPayer();
            ps.setString(1,m.getEmail());
            ps.setInt(2,group.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                payerbalance=rs.getDouble(1);
            }
            Db.close(rs);
            Db.close(ps);

            ps=connection.prepareStatement("SELECT balance FROM user_party WHERE user_email=? AND party_id=?");
            m=(Member) disbursement3.getParticipants().get(0);
            ps.setString(1,m.getEmail());
            ps.setInt(2,group.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                otherbalance=rs.getDouble(1);
            }
            Db.close(rs);
            Db.close(ps);

            ps=connection.prepareStatement("SELECT balance FROM user_party WHERE user_email=? AND party_id=?");
            m=(Member) disbursement3.getParticipants().get(1);
            ps.setString(1,m.getEmail());
            ps.setInt(2,group.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                other2balance=rs.getDouble(1);
            }
        }catch(SQLException e){
            log.error("addDisbursement errored",e);
        }finally {
            Db.close(rs);
            Db.close(ps);
        }

        assertTrue(added);
        assertEquals(1200,payerbalance,0.0051);
        assertEquals(900,otherbalance,0.0051);
        assertEquals(400,other2balance,0.0051);


    }

    @Test
    public void deleteDisbursement() {
        final Disbursement disbursement3=disbursement.copy();
        ArrayList<Item> items3 = new ArrayList<>();
        items3.add(item2);
        disbursement3.setItems(items3);

        ResultSet rs=null;
        boolean dbUpdated=false;
        boolean deleted=false;
        boolean itemUpdated=false;

        try{
            //Delete disbursement
            deleted=disbursementDao.deleteDisbursement(disbursement3);

            //Check that disbursement is not in DB
            ps=connection.prepareStatement("SELECT COUNT(*) FROM disbursement WHERE id=?");
            ps.setInt(1,disbursement.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                dbUpdated=(rs.getInt(1)==0);
            }
            Db.close(rs);
            Db.close(ps);

            //Check that items are not in disbursement
            Item i=disbursement.getItems().get(0);
            ps=connection.prepareStatement("SELECT COUNT(*) FROM item WHERE disbursement_id=? OR (id=? AND status=3)");
            ps.setInt(1,disbursement.getId());
            ps.setInt(2,i.getId());
            rs=ps.executeQuery();
            if(rs.next()){
                itemUpdated=rs.getInt(1)==0;
            }
        }catch (SQLException e){
            log.error("deletedisbursemetn error",e);
        }finally {
            Db.close(rs);
            Db.close(ps);
        }
        assertTrue(deleted);
        assertTrue(dbUpdated);
        assertTrue(itemUpdated);
    }
}