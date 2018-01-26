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

public class NotificationSender {



    public boolean distursementNotification(Disbursement disbursement, Group group){
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            for(User user: disbursement.getParticipants()) {
                String disbursmentString = user.getName() + " have added you to a receipt! To see details and accept, go to receipts in group "
                        + group.getName();
                Notification notification = new Notification(user.getEmail(), disbursmentString);
                notificationDao.addNotification(notification);
            }
            return true;
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

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

    public boolean newShoppingListNotification(ShoppingList shoppingList){

        try (Connection connection = Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            String shoppingListString = "You have been added to shoppinglist '"+shoppingList.getName()+"' in group "
                    +groupDao.getGroup(shoppingList.getGroupId());
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
