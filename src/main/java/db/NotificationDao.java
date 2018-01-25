package db;

import data.Notification;
import data.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * -Description of the class-
 *
 * @author Martin Wangen
 */

public class NotificationDao {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    public NotificationDao(){}

    public NotificationDao(Connection connection) {
        this.connection=connection;
    }

    /** gets a list of wallposts in a group
     * @param userEmail the id of the group you want to get all wallposts from
     * @return an ArrayList of Notifications on the given group ID
     * @throws SQLException if the query fails
     */

    public ArrayList<Notification> getNotifications(String userEmail) throws SQLException{
//        connection=Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("SELECT * FROM notification WHERE user_email=?");
            ps.setString(1,userEmail);
            rs = ps.executeQuery();
            ArrayList<Notification> resultat = new ArrayList<>();
            while(rs.next()){
                Notification notification = new Notification(rs.getInt("id"),rs.getString("user_email"),rs.getString("text"),rs.getTimestamp("time"));
                notification.setSeen(rs.getInt("seen"));
                resultat.add(notification);
            } return resultat;
        }finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

    /** adds a wallpost to the database including the group id the wallpost belongs to
     * @param notification the notification you want to "post"
     * @return true if the wallpost is added successfully
     * @throws SQLException if the query fails
     */
    public boolean addNotification(Notification notification) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps = connection.prepareStatement("INSERT INTO notification(user_email, text) VALUES (?,?)");
            ps.setString(1,notification.getUserEmail());
            ps.setString(2,notification.getText());
            int res = ps.executeUpdate();
            return res==1;
        }
        finally {
            Db.close(ps);
        }
    }

    /** gets a wallpost by the party ID
     * @param notificationId the id of the seen notification
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public boolean seenNotification(int notificationId) throws SQLException{
        try{
            ps = connection.prepareStatement("UPDATE notification SET seen=1 WHERE id=?");
            ps.setInt(1,notificationId);
            rs = ps.executeQuery();
            int result = ps.executeUpdate();
            return result==1;
        }
        finally{
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /** deletes a wallpost from teh databse
     * @param notificationId the id of the notification
     * @return true if the query succeeds
     * @throws SQLException if the query fails
     */
    public boolean deleteNotification(int notificationId) throws SQLException{
        try{
            ps = connection.prepareStatement("DELETE FROM notification WHERE id=?");
            ps.setInt(1,notificationId);
            int resultat = ps.executeUpdate();
            return resultat == 1;
        }
        finally {
            Db.close(ps);
        }
    }
}
