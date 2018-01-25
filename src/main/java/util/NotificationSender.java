package util;

import data.*;
import db.Db;
import db.NotificationDao;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

public class NotificationSender {
    public NotificationSender() {}
    private static final Logger log = Logger.getLogger();



    public boolean distursementNotification(User user, Group group){
        String disbursmentString = user.getName()+" have added you to a receipt! To see details and accept, go to receipts in group "+group.getName();
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            Notification notification = new Notification(user.getEmail(),disbursmentString);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
    /*
    public boolean paymentNotification(Payment payment){
        String text = user.getName()+disbursmentString+group.getName();

        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            Notification notification = new Notification(user.getEmail(),text);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }*/
    /*
    public boolean distursementAcceptNotification(User user, Disbursement disbursement){
        String disbursmentString = user.getName()+" have added you to a receipt! To see details and accept, go to receipts in group "+group.getName();
        if(disbursement.getAccepted()==1){

        }else if(disbursement.getAccepted()==2){

        }
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            Notification notification = new Notification(user.getEmail(),disbursmentString);
            return notificationDao.addNotification(notification);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to send notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }*/

}
