package db;

import data.Disbursement;
import data.Group;
import data.Member;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;


//TODO: class
public class DisbursementDaoTest {
    private static Group group;
    private static Member member;
    private static String userEmail="payer@email.com";
    private static int groupId;
    private static Disbursement testDisbursement;




    @BeforeClass
    public static void setUp()throws SQLException {
        testDisbursement = new Disbursement();
        testDisbursement.setDate(new Date());
        testDisbursement.setName("disburse");
        member = new Member(userEmail, "name", "phone", "password","123",0,2);
        group = new Group();
        group.setName("groupname");
        group.setAdmin(userEmail);
        //groupId=GroupDao.addGroup(group);
        testDisbursement.setPayer(member);
        UserDao.addUser(member);



    }
/*
    @Test
    public void addDisbursementTest() {
        try {
            assertTrue(DisbursementDao.addDisbursement(testDisbursement,groupId)>=1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void getDisbursementsInGroupTest() {

    }

    @Test
    public void getDisbursementDetailsTest() {
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        MemberDao.deleteMember(member.getEmail(),groupId);
       // DisbursementDao.deleteDisbursment();
        GroupDao.deleteGroup(group);

    }
}