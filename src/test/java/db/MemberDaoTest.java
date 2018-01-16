package db;

import data.Group;
import data.Member;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static db.MemberDao.getMembers;

public class MemberDaoTest {
    private static final Logger log = Logger.getLogger();
    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;


    private static MemberDao md;
    private static String name1 = "Vennegjengen";
    private static String name2 = "Kollektivet";
    private static String adminnavn = "admin";
    private static String email1 = "en@test1.no";
    private static String email2 = "to@test1.no";
    private static String email3 = "tre@test1.no";
    private static String email4 = "fire@test1.no";
    private static int groupId1 = 1001;
    private static int groupId2 = 1002;
    private static ArrayList<Member> groupmembers = new ArrayList<>();


    @BeforeClass
    public static void setUp() throws Exception {

        Group group1 = new Group();
        group1.setId(groupId1);
        group1.setName(name1);

        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO party(id,name) VALUES(1001,'testgroup');");
            int result = ps.executeUpdate();
            log.info("Added group " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user(name, email, password, salt, phone) VALUES( 'Testesen', 'en@test1.no', 'pw','123',123456);");
            result = ps.executeUpdate();
            log.info("Added user " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user(name, email, password, salt, phone) VALUES( 'Testesen', 'to@test1.no', 'pw','123',123456);");
            result = ps.executeUpdate();
            log.info("Added user " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user(name, email, password, salt, phone) VALUES( 'Testesen', 'tre@test1.no', 'pw','123',123456);");
            result = ps.executeUpdate();
            log.info("Added user " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user(name, email, password, salt, phone) VALUES( 'Testesen', 'fire@test1.no', 'pw','123',123456);");
            result = ps.executeUpdate();
            log.info("Added user " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'en@test1.no', 1001,0,0);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'to@test1.no', 1001,0,1);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'tre@test1.no', 1001,0,2);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'fire@test1.no', 1001,0,3);");
            result = ps.executeUpdate();
            ps.close();
            log.info("Added member " + (result == 1?"ok":"failed"));
        } finally {
            connection.close();

        Member mem1 = new Member(email1, "User1", "90706060", "123",0,Member.PENDING_STATUS);
        Member mem2 = new Member(email2, "User1", "90706060", "123",0,Member.ACCEPTED_STATUS);
        Member mem3 = new Member(email3, "User1", "90706060", "123",0,Member.ADMIN_STATUS);
        Member mem4 = new Member(email4, "User1", "90706060", "123",0,Member.BLOCKED_STATUS);

        groupmembers.add(mem1);
        groupmembers.add(mem2);
        groupmembers.add(mem3);
        groupmembers.add(mem4);
        }
    }
    //TODO finn feilen...
    @Test
    public void getMembersTest() throws SQLException {
        ArrayList<Member> members = getMembers(groupId1);
        String gMember1 = members.get(0).getEmail();
        String gMember2 = members.get(1).getEmail();
        log.info("Test run");
        assertTrue(gMember1.equalsIgnoreCase(email3)&&gMember2.equalsIgnoreCase(email2)||gMember1.equalsIgnoreCase(email2)&&gMember2.equalsIgnoreCase(email3));
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM user_party WHERE party_id=1001;");
            int result = ps.executeUpdate();
            log.info("Deleted member " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM user WHERE email='en@test1.no';");
            result = ps.executeUpdate();
            log.info("Deleted user " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM user WHERE email='to@test1.no';");
            result = ps.executeUpdate();
            log.info("Deleted user " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM user WHERE email='tre@test1.no';");
            result = ps.executeUpdate();
            log.info("Deleted user " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM user WHERE email='fire@test1.no';");
            result = ps.executeUpdate();
            log.info("Deleted user " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM party WHERE id=1001");
            result = ps.executeUpdate();
            ps.close();
            log.info("Deleted users " + (result == 1 ? "ok" : "failed"));
        } finally {
            connection.close();

        }
    }
}