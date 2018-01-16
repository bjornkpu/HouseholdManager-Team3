package db;

import data.Group;
import data.Member;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static db.MemberDao.getMembers;

public class MemberDaoTest {
    private static MemberDao md;
    private static String name1 = "Vennegjengen";
    private static String name2 = "Kollektivet";
    private static String adminnavn = "admin";
    private static String email1 = "en@test2.no";
    private static String email2 = "to@test2.no";
    private static String email3 = "tre@test2.no";
    private static String email4 = "fire@test2.no";
    private static int groupId1 = 1001;
    private static int groupId2 = 1002;
    private static ArrayList<Member> groupmembers = new ArrayList<>();


    @BeforeClass
    public static void setUp() throws Exception {
        Group group1 = new Group();
        group1.setId(groupId1);
        group1.setName(name1);

        Member mem1 = new Member(email1, "User1", "90706060", "123",0,Member.PENDING_STATUS);
        Member mem2 = new Member(email2, "User1", "90706060", "123",0,Member.ACCEPTED_STATUS);
        Member mem3 = new Member(email3, "User1", "90706060", "123",0,Member.ADMIN_STATUS);
        Member mem4 = new Member(email4, "User1", "90706060", "123",0,Member.BLOCKED_STATUS);

        groupmembers.add(mem1);
        groupmembers.add(mem2);
        groupmembers.add(mem3);
        groupmembers.add(mem4);

        for(Member member :groupmembers) {
            UserDao.addUser(member);
            MemberDao.inviteUser(member.getEmail(),groupId1);
            MemberDao.updateUser(member, groupId1);
        }


    }

    @Test
    public void getMembersTest() throws SQLException {
        ArrayList<Member> members = getMembers(groupId1);
        String gMember1 = members.get(0).getEmail();
        String gMember2 = members.get(0).getEmail();
        assertTrue(gMember1.equalsIgnoreCase(email3)&&gMember2.equalsIgnoreCase(email2)||
                gMember1.equalsIgnoreCase(email2)&&gMember2.equalsIgnoreCase(email3));
    }

    /*
    public static ArrayList<Member> getMembers(int groupId) throws SQLException {
        String partyId = ""+groupId;
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM user NATURAL JOIN user_party WHERE party_id=? AND (status=? OR status=?");
            ps.setString(1,partyId);
            ps.setString(2,String.valueOf(Member.ACCEPTED_STATUS));
            ps.setString(2,String.valueOf(Member.ADMIN_STATUS));
            rs = ps.executeQuery();

            ArrayList<Member> members = new ArrayList<>();
            Member member = null;
            while(rs.next()) {
                member = new Member();
                member.setEmail(rs.getString("email"));
                member.setPassword(rs.getString("password"));
                member.setPhone(rs.getString("phone"));
                member.setName(rs.getString("name"));
                member.setBalance(Integer.parseInt(rs.getString("balance")));
                members.add(member);
            }
            rs.close();
            ps.close();
            return members;
        } finally {
            connection.close();
        }
    }

    public static ArrayList<Group> getGroupInvites(String email) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement(
                    "SELECT party.name,party_id FROM user_party LEFT JOIN party ON party.id= user_party.party_id WHERE user_email=? AND status=?");
            ps.setString(1,email);
            ps.setString(2,String.valueOf(Member.PENDING_STATUS));
            rs = ps.executeQuery();

            ArrayList<Group> invites = new ArrayList<>();
            Group group = null;
            while(rs.next()) {
                group = new Group();
                group.setName(rs.getString("name"));
                invites.add(group);
            }
            rs.close();
            ps.close();
            return invites;
        } finally {
            connection.close();
        }
    }


    public static boolean inviteUser(String email, int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("INSERT INTO user_party (user_email,party_id,balance,status) VALUES(?,?,?,?)");
            ps.setString(1, email);
            ps.setString(2, String.valueOf(groupId));
            ps.setString(3, "0");
            ps.setString(4, String.valueOf(Member.PENDING_STATUS));
            int result = ps.executeUpdate();
            ps.close();
            log.info("Invite user " + (result == 1 ? "ok" : "failed"));
            return result == 1;
        }catch (SQLException e){
            return false;
        } finally {
            connection.close();
        }
    }

    public static boolean updateUser(Member member, int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE user_party set balance=?, status = ? where email=? AND party_id = ?");
            ps.setString(1,String.valueOf(member.getBalance()));
            ps.setString(2,String.valueOf(member.getStatus()));
            ps.setString(3,member.getEmail());
            ps.setString(4,String.valueOf(groupId));
            int result = ps.executeUpdate();
            ps.close();
            log.info("Update member " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            connection.close();
        }
    }
}
*/
    @AfterClass
    public static void tearDown() throws SQLException{
        for (Member member : groupmembers){
            MemberDao.deleteMember(member.getEmail(),groupId1);
            UserDao.delUser(member.getEmail());

        }
    }
}