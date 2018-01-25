package services;
import data.*;
import db.Db;
import db.GroupDao;
import db.NotificationDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * Service for class {@code Notification}
 *
 * @author nybakk1
 * @version 0.1
 */

@Path("/notifications/{userEmail}")
public class NotificationService {

    private static final Logger log = Logger.getLogger();


    public NotificationService() {
    }

    @Context
    private HttpServletRequest request;

    /**
     * Gets all the {@code Notifications} posted in a {@code Group}.
     *
     * @param userEmail Id of the {@code Notifiation}.
     * @return Returns an {@code ArrayList} of {@code Notification} objects if successful, else returns HTTP status code 500.
     */

    @GET
    @Produces("application/json")
    public ArrayList<Notification> getNotificationForUser(@PathParam("userEmail") String userEmail) {
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);

            return notificationDao.getNotifications(userEmail);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to get notifications for " + userEmail, Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Gets all the {@code Notifications} from a {@code user} in a {@code Group}.
     *
     * @param notificationId Id of the {@code Notification}.
     */
    @PUT
    public void seenNotification(int notificationId) {
        try (Connection connection = Db.instance().getConnection()) {
            NotificationDao notificationDao = new NotificationDao(connection);
            notificationDao.seenNotification(notificationId);
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to registrer seen notification", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Updates all the {@code notifications} from a {@code user} to seen.
     * @param userEmail Email of {@code user}.
     * @return {@code HTTP} status of 200 if successful. If unsuccessful throws {@code HTTP} status 500.
     */

    @PUT
    @Path("/seenAll")
    public Response seenAllNotifications(@PathParam("userEmail") String userEmail){
        try (Connection connection = Db.instance().getConnection()){
            NotificationDao notificationDao = new NotificationDao(connection);
            boolean ok = notificationDao.seenAllNotifications(userEmail);
            return Response.status(200).entity(ok).build();
        } catch (SQLException e){
            throw new ServerErrorException("Failed to register seen notification", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }


}