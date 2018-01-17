package db;

import data.Group;
import data.Member;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Logger;
import util.LoginCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static db.MemberDao.getMembers;
import static org.junit.Assert.assertTrue;

/**
 * -Description of the class-
 *
 * @author
 * Martin Wangen
 */
public class MemberDaoTest {
    private static final Logger log = Logger.getLogger();
    private static Connection connection;
    private static PreparedStatement ps;
    private static String name1 = "Vennegjengen";
    private static String email1 = "en@test1.no";
    private static String email2 = "to@test1.no";
    private static String email3 = "tre@test1.no";
    private static String email4 = "fire@test1.no";
    private static int groupId1 = 1001;
    private static int groupId2 = 1002;


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
            ps = connection.prepareStatement("INSERT INTO party(id,name) VALUES(1002,'Kollektivet');");
            result = ps.executeUpdate();
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
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'tre@test1.no', 1002,0,2);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'fire@test1.no', 1001,0,3);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
            ps = connection.prepareStatement("INSERT INTO user_party(user_email,party_id,balance,status) VALUES( 'en@test1.no', 1002,0,0);");
            result = ps.executeUpdate();
            log.info("Added member " + (result == 1?"ok":"failed"));
        } finally {
            Db.close(ps);
            Db.close(connection);
        }
    }


    @Test
    public void getMembersTest() throws SQLException {
        ArrayList<Member> members = getMembers(groupId1);
        String gMember1 = members.get(0).getEmail();
        String gMember2 = members.get(1).getEmail();
        log.info("Test run");
        Boolean ok = false;
        if(gMember1.equalsIgnoreCase(email3)&&gMember2.equalsIgnoreCase(email2)){
            ok=true;
        }else if(gMember1.equalsIgnoreCase(email2)&&gMember2.equalsIgnoreCase(email3)){
            ok=true;
        }

        assertTrue(ok);
    }

    @Test
    public void getGroupsByMember() throws SQLException{
        ArrayList<Group> groups = MemberDao.getGroupsByMember(email3);
        Boolean ok = false;
        if(groups.get(0).getId()==groupId1&&groups.get(1).getId()==groupId2){
            ok=true;
        }else if(groups.get(1).getId()==groupId1&&groups.get(2).getId()==groupId2){
            ok=true;
        }
        assertTrue(ok);
    }

    @Test
    public void getGroupInvitesTest() throws SQLException{
        ArrayList<Group> groups = MemberDao.getGroupInvites(email1);
        assertTrue(groups.get(0).getId()==1001);
    }

    @Test
    public void inviteUserTest() throws SQLException{
        MemberDao.inviteUser(email4,groupId2);
        ArrayList<Group> invites = MemberDao.getGroupInvites(email4);
        assertTrue(invites.get(0).getId()==groupId2);
    }

    @Test
    public void updateUserTest()throws SQLException{
        Member mem1 = new Member(email1, "User1", "90706060", "123", LoginCheck.getSalt(),0,Member.ACCEPTED_STATUS);
        MemberDao.updateUser(mem1,groupId2);
        ArrayList<Member> members = MemberDao.getMembers(groupId2);
        Boolean ok = false;
        for(Member memb : members){
            if(memb.getEmail().equalsIgnoreCase(email1)){
                ok=true;
            }
        }
        assertTrue(ok);
    }

    @Test
    public void deleteMemberTest() throws SQLException{
        Boolean test = false;
        Boolean test2 = false;
        Member mem2 = new Member(email2, "User1", "90706060", "123", LoginCheck.getSalt(),0,Member.ACCEPTED_STATUS);
        MemberDao.inviteUser(email2,groupId2);
        MemberDao.updateUser(mem2,groupId2);
        ArrayList<Member> members = MemberDao.getMembers(groupId2);
        for(Member memb : members){
            if(memb.getEmail().equalsIgnoreCase(email2)){
                test=true;
                MemberDao.deleteMember(email2,groupId2);
                break;
            }
        }
        members = MemberDao.getMembers(groupId2);
        for(Member memb : members){
            if(memb.getEmail().equalsIgnoreCase(email2)){
                test2=true;
                break;
            }
        }
        assertTrue(test&&(!test2));
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM user_party WHERE party_id=1001;");
            int result = ps.executeUpdate();
            log.info("Deleted members " + (result >= 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM user_party WHERE party_id=1002;");
            result = ps.executeUpdate();
            log.info("Deleted members " + (result >= 1 ? "ok" : "failed"));
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
            log.info("Deleted party " + (result == 1 ? "ok" : "failed"));
            ps = connection.prepareStatement("DELETE FROM party WHERE id=1002");
            result = ps.executeUpdate();
            log.info("Deleted party " + (result == 1 ? "ok" : "failed"));
        } finally {
            Db.close(ps);
            Db.close(connection);

        }
    }
}