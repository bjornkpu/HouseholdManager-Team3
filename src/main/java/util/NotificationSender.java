package util;

import data.*;
import db.Db;
import db.DisbursementDao;
import db.GroupDao;
import db.NotificationDao;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class contains methods to generate notifications for users.
 */
public class NotificationSender {


    /**
     * Sends a notification to participants of disbusement
     * @param disbursement conains the info about participants and name
     * @param group the group containing the disbursement
     * @return Boolean telling if the sending was successful
     */
    public boolean distursementNotification(Disbursement disbursement, Group group){
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            for(User user: disbursement.getParticipants()) {
                String disbursmentString = disbursement.getPayer().getName() + " have added you to a receipt! To see details and accept, go to receipts in group "
                        + group.getName();
                Notification notification = new Notification(user.getEmail(), disbursmentString);
                notificationDao.addNotification(notification);
            }
            return true;
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Sends a notification to the payer if the participant accepted or not.
     * @param user payer
     * @param disbursement Disbursement details
     * @return Boolean telling if the sending was successful
     */
    public boolean distursementAcceptNotification(User user, Disbursement disbursement){
        String accepted="";
        if(disbursement.getAccepted()==1){
            accepted=" have accepted ";
        }else if(disbursement.getAccepted()==2){
            accepted=" have declined ";
        }
        String disbursmentAcceptString = user.getName()+accepted+ "receipt '"+disbursement.getName()+"'.";
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            Notification notification = new Notification(disbursement.getPayer().getEmail(),disbursmentAcceptString);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Sends a notification to a member about a requesed payment
     * @param payment payment info
     * @return Boolean telling if the sending was successful
     */
    public boolean paymentNotification(Payment payment){
        String text = "You have got a payment request from '"+payment.getPayerName()+"'. ";
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            Notification notification = new Notification(payment.getPayer(),text);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Sends a notification to participants that they are aadded to a shoppinglist
     * @param shoppingList participants, nameand group
     * @return Boolean telling if the sending was successful
     */
    public boolean newShoppingListNotification(ShoppingList shoppingList){

        try (Connection connection = Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            String shoppingListString = "You have been added to shoppinglist '"+shoppingList.getName()+"' in group "
                    +groupDao.getGroup(shoppingList.getGroupId()).getName();
            NotificationDao notificationDao = new NotificationDao(connection);
            for (User user : shoppingList.getUserList()) {
                Notification notification = new Notification(user.getEmail(), shoppingListString);
                notificationDao.addNotification(notification);
            }
            return true;
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Sends a notification to participants that a shopinglist is closed.
     * @param shoppingList info about the closed shoppinglist
     * @return Boolean telling if the sending was successful
     */
    public boolean closeShoppingListNotification(ShoppingList shoppingList){
        try (Connection connection = Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            String shoppingListString = "Shoppinglist '"+shoppingList.getName()+"' in group '"
                    +groupDao.getGroup(shoppingList.getGroupId())+"' have been closed.";
            NotificationDao notificationDao = new NotificationDao(connection);
            for (User user : shoppingList.getUserList()) {
                Notification notification = new Notification(user.getEmail(), shoppingListString);
                notificationDao.addNotification(notification);
            }
            return true;
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Sends a notification to a user that he is invited to a user
     * @param email the invited user
     * @param group the group he is invited to
     * @return Boolean telling if the sending was successful
     */
    public boolean invitedUserNotification(String email, Group group){
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            String invitedUserString ="You have been invited to group; "+group.getName();
            Notification notification = new Notification(email,invitedUserString);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
