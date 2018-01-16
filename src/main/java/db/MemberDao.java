package db;

import data.Group;
import data.Member;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberDao {
    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ArrayList<Member> getMembers(int groupId) throws SQLException {
        String partyId = ""+groupId;
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM user NATURAL JOIN user_party WHERE party_id=? AND (status=? OR status=?);");
            ps.setString(1,partyId);
            ps.setString(2,String.valueOf(Member.ACCEPTED_STATUS));
            ps.setString(3,String.valueOf(Member.ADMIN_STATUS));
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

            return members;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }
    public static ArrayList<Group> getGroupsByMember(String email) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT party.id,party.name FROM party NATURAL JOIN user_party WHERE user_email=? AND (status=? OR status=?);");
            ps.setString(1, email);
            ps.setString(2, String.valueOf(Member.ACCEPTED_STATUS));
            ps.setString(3, String.valueOf(Member.ADMIN_STATUS));
            rs = ps.executeQuery();

            ArrayList<Group> groups = new ArrayList<>();
            Group group = null;
            while (rs.next()) {
                group = new Group();
                group.setId(Integer.parseInt(rs.getString("id")));
                group.setName(rs.getString("password"));
            }
            return groups;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
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
            return invites;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
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
            log.info("Invite user " + (result == 1 ? "ok" : "failed"));
            return result == 1;
        }catch (SQLException e){
            return false;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

    public static boolean updateUser(Member member, int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("UPDATE user_party set balance=?, status = ? where user_email=? AND party_id = ?");
            ps.setString(1,String.valueOf(member.getBalance()));
            ps.setString(2,String.valueOf(member.getStatus()));
            ps.setString(3,member.getEmail());
            ps.setString(4,String.valueOf(groupId));
            int result = ps.executeUpdate();
            log.info("Update member " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);;
        }
    }
    public static boolean deleteMember(String email, int groupId) throws SQLException {
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("DELETE FROM user_party where user_email=? AND party_id = ?");
            ps.setString(1,email);
            ps.setString(2,String.valueOf(groupId));
            int result = ps.executeUpdate();
            log.info("Delete member " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
            Db.close(connection);
        }
    }

}
