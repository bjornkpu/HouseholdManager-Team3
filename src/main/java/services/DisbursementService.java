package services;

import data.Disbursement;
import data.Group;
import data.Notification;
import data.StatisticsHelp;
import db.*;
import util.Logger;
import util.NotificationSender;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.showMessageDialog;

@Path("/groups/{groupId}/disbursement/")
public class DisbursementService {
    private NotificationSender notificationSender = new NotificationSender();
    private final Logger log = Logger.getLogger();

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{user_email}/user")
    @Produces("application/json")
    public ArrayList<Disbursement> getDisbursementList(@PathParam("groupId") int groupId, @PathParam("user_email") String user_email) throws SQLException {
//		Session session = (Session)request.getSession();
        try (Connection connection = Db.instance().getConnection()){
            DisbursementDao dDao = new DisbursementDao(connection);
            return dDao.getDisbursementsInGroup(groupId, user_email);
        } catch (SQLException e) {
            log.error("Failed to get Disbursement", e);
            throw new ServerErrorException("Failed to get Disbursement", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Consumes("application/json")
    public void addDisbursement(@PathParam("groupId") int groupId, Disbursement disbursement){
        log.info(disbursement.getName()+" to be added.");
        try (Connection connection = Db.instance().getConnection()){
            DisbursementDao dDao = new DisbursementDao(connection);
            if(dDao.addDisbursement(disbursement,groupId)){
                GroupDao groupDao = new GroupDao(connection);
                Group group = groupDao.getGroup(groupId);
                notificationSender.distursementNotification(disbursement,group);
            }
        } catch (SQLException e) {
            log.error("Failed to add disbursement", e);
            throw new ServerErrorException("Failed to add disbursement", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
    @PUT
    @Path("/{userEmail}/{response}")
    public void disbursementResponse(@PathParam("userEmail") String userEmail, @PathParam("response") int response,
                                     @PathParam("groupId") int groupId, Disbursement disbursement){
        log.info(disbursement.getId()+" to be responded to by "+userEmail+"with int: "+response);
        try (Connection connection = Db.instance().getConnection()){
            DisbursementDao dDao = new DisbursementDao(connection);
            if(dDao.respondToDisbursement(disbursement,groupId,userEmail,response)){
                disbursement = dDao.getDisbursementDetails(disbursement.getId(),userEmail);
                UserDao userDao = new UserDao(connection);
                notificationSender.distursementAcceptNotification(userDao.getUser(userEmail),disbursement);
            }
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
