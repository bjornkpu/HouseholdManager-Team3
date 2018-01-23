package db;

import data.Group;
import data.Member;
import data.User;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * -Description of the class-
 *
 * @author
 * Martin Wangen
 */
public class MemberDao {
    private static final Logger log = Logger.getLogger();

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private UserDao userDao;

    public MemberDao(Connection connection) {
        this.connection=connection;
    }

    /**
     * Gets the members of a given group
     * @param groupId Id of the group.
     * @return Returns ArrayList of Members in the group.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public ArrayList<Member> getMembers(int groupId) throws SQLException {
//        connection = Db.instance().getConnection();
        String partyId = ""+groupId;
        try {
            ps = connection.prepareStatement("SELECT * FROM user LEFT JOIN user_party ON user.email = user_party.user_email WHERE party_id=? AND (status=? OR status=?);");
            ps.setString(1,partyId);
            ps.setString(2,String.valueOf(Member.ACCEPTED_STATUS));
            ps.setString(3,String.valueOf(Member.ADMIN_STATUS));
            rs = ps.executeQuery();

            ArrayList<Member> members = new ArrayList<>();
            Member member = new Member();
            while(rs.next()) {
                member = new Member();
                member.setEmail(rs.getString("email"));
                member.setPassword(rs.getString("password"));
                member.setSalt(rs.getString("salt"));
                member.setPhone(rs.getString("phone"));
                member.setName(rs.getString("name"));
                member.setBalance(rs.getDouble("balance"));
                member.setStatus(rs.getInt("status"));
                members.add(member);
            }

            return members;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     * Retrieves all the groups a user is a part of.
     *
     * @param email Email of the user.
     * @return ArrayList of groups.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public ArrayList<Group> getGroupsByMember(String email) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT id,name FROM party LEFT JOIN user_party ON party.id = user_party.party_id WHERE user_email=? AND (status=? OR status=?);");
            ps.setString(1, email);
            ps.setString(2, String.valueOf(Member.ACCEPTED_STATUS));
            ps.setString(3, String.valueOf(Member.ADMIN_STATUS));
            rs = ps.executeQuery();

            ArrayList<Group> groups = new ArrayList<>();
            Group group = null;
            while (rs.next()) {
                group = new Group();
                group.setId(Integer.parseInt(rs.getString("id")));
                group.setName(rs.getString("name"));
                groups.add(group);
            }
            return groups;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     * Retrieves all the requests from a user to join a group.
     *
     * @param email Email of the user.
     * @return ArrayList of groups.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public ArrayList<Group> getGroupInvites(String email) throws SQLException{
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement(
                    "SELECT name,party_id FROM user_party LEFT JOIN party ON party.id= user_party.party_id WHERE user_email=? AND status=?");
            ps.setString(1,email);
            ps.setString(2,String.valueOf(Member.PENDING_STATUS));
            rs = ps.executeQuery();

            ArrayList<Group> invites = new ArrayList<>();
            Group group = null;
            while(rs.next()) {
                group = new Group();
                group.setId(Integer.parseInt(rs.getString("party_id")));
                group.setName(rs.getString("name"));
                invites.add(group);
            }
            return invites;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     * Invites a user to join the group.
     *
     * @param email Email of user.
     * @param groupId Group to join.
     * @return A boolean which indicates the outcome.True->success, false-> failed.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public boolean inviteUser(String email, int groupId) throws SQLException {
//        connection = Db.instance().getConnection();
        userDao= new UserDao(connection);
        try {
            User user = userDao.getUser(email);
            if(user != null){
                ps = connection.prepareStatement("INSERT INTO user_party (user_email,party_id,balance,status) VALUES(?,?,?,?)");
                ps.setString(1, email);
                ps.setString(2, String.valueOf(groupId));
                ps.setString(3, "0");
                ps.setString(4, String.valueOf(Member.PENDING_STATUS));
                int result = ps.executeUpdate();
                log.info("Invite user " + (result == 1 ? "ok" : "failed"));
                return result == 1;
            } else {
                return false;
            }
        }catch (SQLException e){
            return false;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     * Updates a member of a group. One could update the balance or status of a member in a group.
     *
     * @param member Member object with new values. A member with balance == -1 updates the status only,
     *               status == -1 updates balance only.
     * @param groupId Id of the group.
     * @return A boolean which indicates the outcome. True->success, false-> failed.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public boolean updateMember(Member member, int groupId) throws SQLException {
//        connection = Db.instance().getConnection();
        try {
            if (member.getBalance() == -1 && member.getName() == null && member.getPassword() == null){
                ps = connection.prepareStatement("UPDATE user_party set status = ? where user_email=? AND party_id = ?");
                ps.setInt(1,member.getStatus());
                ps.setString(2,member.getEmail());
                ps.setInt(3,groupId);
                int result = ps.executeUpdate();
                log.info("Update member status " + (result == 1?"ok":"failed"));
                return result == 1;
            }
            if(member.getStatus() == -1){
                int result = -1;
                ps = connection.prepareStatement("SELECT user_email, balance FROM user_party WHERE user_email=? AND party_id=? FOR UPDATE");
                ps.setString(1,member.getEmail());
                ps.setInt(2,groupId);
                rs = ps.executeQuery();
                if(rs.next()){
                    double oldBalance = rs.getDouble("balance");
                    Db.close(rs);
                    Db.close(ps);
                    ps = connection.prepareStatement("UPDATE user_party set balance = ? where user_email=? AND party_id = ?");
                    ps.setDouble(1,oldBalance+member.getBalance());
                    ps.setString(2,member.getEmail());
                    ps.setInt(3,groupId);
                    result = ps.executeUpdate();
                    log.info("Update member balance " + (result == 1?"ok":"failed"));
                }
                return result == 1;

            }
            ps = connection.prepareStatement("UPDATE user_party set balance=?, status = ? where user_email=? AND party_id = ?");
            ps.setDouble(1,member.getBalance());
            ps.setInt(2,member.getStatus());
            ps.setString(3,member.getEmail());
            ps.setInt(4,groupId);
            int result = ps.executeUpdate();
            log.info("Update member " + (result == 1?"ok":"failed"));
            return result == 1;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**
     * Deletes a member from the group.
     * @param email Email of the member.
     * @param groupId Id of the group.
     * @return A boolean which indicates the outcome. True->success, false-> failed.
     * @throws SQLException Throws exception when the connection is not successful.
     */

    public boolean deleteMember(String email, int groupId) throws SQLException {
//        connection = Db.instance().getConnection();
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
//            Db.close(connection);
        }
    }
}
