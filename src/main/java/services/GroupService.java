package services;
import data.Group;
import data.Payment;
import data.Session;
import data.StatisticsHelp;
import db.Db;
import db.GroupDao;
import db.MemberDao;
import db.UserDao;
import util.Logger;
import util.NotificationSender;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * Service class for class Group. Retrieves information from database and posts to rest.
 * Javadoc for put,post and delete has curl commands for testing.
 *
 * @author nybakk1
 * @author KnutWiig
 * @version 0.1
 */
@Path("/groups/")
public class GroupService {
    private static final Logger log = Logger.getLogger();
    private NotificationSender notificationSender = new NotificationSender();

    public GroupService() {
    }

    @Context
    private HttpServletRequest request;

    /**
     * Retrieves one group given an ID from database.
     *
     * @param groupid The groupId of the group requested.
     * @return A Group object containing the requested group.
     */
    @GET
    @Path("/{groupid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroup(@PathParam("groupid") int groupid) {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        Group group = null;
        try (Connection connection= Db.instance().getConnection()){
            GroupDao groupDao = new GroupDao(connection);
            log.info("Group #" + groupid + " found.");
            return groupDao.getGroup(groupid);
        } catch (SQLException e) {
            log.info("Could not find group #" + groupid);
            throw new ServerErrorException("Failed to get group", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Returns all groups in the database.
     *
     * @return Returns the full list of groups from database.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroups() {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        List<Group> groups = null;
        try (Connection connection= Db.instance().getConnection()){
            GroupDao groupDao = new GroupDao(connection);
            log.info("Retrieving all groups");
            groups = groupDao.getAllGroups();
            return groups;
        } catch (SQLException e) {
            log.info("Unable to get all groups");
            throw new ServerErrorException("Failed to get groups", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
    /**
     * Retrieves all the groups a user is invited to join.
     *
     * @param email Email of user.
     * @return ArrayList of groups which the user is invited to join.
     */
    @GET
    @Path("/{email}/invites")
    @Produces("application/json")
    public ArrayList<Group> getGroupInvites(@PathParam("email") String email){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()){
            MemberDao memberDao = new MemberDao(connection);
            log.info("Retrieving group invites for member " + email);
            return memberDao.getGroupInvites(email);
        } catch (SQLException e){
            log.info("Failed to retrieve invites for member " + email);
            throw new ServerErrorException("Failed to retrieve invites",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
    /**
     * Retrieves all the groups a user is a member of.
     * @param email Email of the user.
     * @return An ArrayList of groups.
     *
     */
    @GET
    @Path("/list/{email}")
    @Produces("application/json")
    public ArrayList<Group> getGroupsConnectedToMember(@PathParam("email") String email){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()) {
            MemberDao memberDao = new MemberDao(connection);

            log.info("Retrieving groups by member.");
            return memberDao.getGroupsByMember(email);
        } catch (SQLException e){
            log.info("Could not get groups");
            throw new ServerErrorException("Failed to get groups.",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Adds a group to database.
     *
     curl -H "Content-Type: application/json" -X POST -d '{"name":"KOLLEK10V","description":null,"admin":"en@h.no"}' http://localhost:8080/scrum/rest/groups
     *
     * @param group The new group to be added.
     */



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGroup(Group group) {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);

            log.info("Group added. ID: " + group.getId());
            int s = groupDao.addGroup(group);
            if (s != -1){
                return Response.status(200).entity(s).build();
            }
            return Response.status(404).entity(-1).build();

        } catch (SQLException e) {
            log.info("Add group failed. ID:"+group.getId());
            throw new ServerErrorException("Failed to create group", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Deletes a group from database.
     *
     * Change the "X" to the id of group you wish to delete.
     * curl -v -X DELETE http://localhost:8080/scrum/rest/groups/X
     * @param groupId Id of the group to be deleted from database.
     */


    @DELETE
    @Path("/{groupid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteGroup(@PathParam("groupid") int groupId){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            groupDao.deleteGroup(groupId);
        } catch (SQLException e){
            log.info("Deleting group failed. Check constraints in database");
            throw new ServerErrorException("Failed to delete group", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     *
     * Updates a group in the database.
     *
     * Change the "name" variable before testing the curl command.
     curl -H "Content-Type: application/json" -X POST -d '{"name":"KOLLEK10V","description":null,"admin":"en@h.no"}' http://localhost:8080/scrum/rest/groups
     *
     * @param group The group to be updated.
     */

    @PUT
    @Path("/{groupid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateGroup(Group group){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            log.info("Updating group. ID: " + group.getId());
            log.info("Updating group. Name: " + group.getName());
            groupDao.updateGroup(group);
        } catch (SQLException e){
            log.info("Updating group failed. ID " + group.getId());
            throw new ServerErrorException("Failed to update group",Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    @GET
    @Path("/balance/{groupId}")
    @Produces("application/json")
    public ArrayList<StatisticsHelp> getBalance(@PathParam("groupId") int groupId){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try (Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            log.info("Retrieving balance by member.");
            return groupDao.getUserBalance(groupId);
        } catch (SQLException e) {
            log.info("Could not get balance");
            throw new ServerErrorException("Failed to get balance.", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Path("/payment/{groupId}/{email}")
    @Produces("application/json")
    public ArrayList<Payment> getPaymentRequests(@PathParam("groupId") int groupId, @PathParam("email") String email){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try (Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            log.info("Retrieving payments to member.");
            return groupDao.getPayments(email,groupId);
        } catch (SQLException e) {
            log.info("Could not get payments");
            throw new ServerErrorException("Failed to get payments.", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PUT
    @Path("/updatePayment/{payId}")
    @Consumes("application/json")
    public boolean updatePayment(@PathParam("payId") int payId){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try (Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            log.info("Updating payment.");
            return groupDao.updatePayment(payId);
        } catch (SQLException e) {
            log.info("Could not update payment");
            throw new ServerErrorException("Failed to update payment.", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PUT
    @Path("/updateBalances/{groupId}/{email1}/{email2}/{amount}")
    @Consumes("application/json")
    public boolean updateUserBalance(@PathParam("groupId") int groupId, @PathParam("amount") double amount, @PathParam("email1") String email1, @PathParam("email2") String email2){
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try (Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            log.info("Upating balance.");
            return groupDao.updateBalances(email1,email2,amount,groupId);
        } catch (SQLException e) {
            log.info("Could not update balance");
            throw new ServerErrorException("Failed to update balance.", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Path("/newPayment/")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addPayment(Payment payment) {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        try(Connection connection= Db.instance().getConnection()) {
            GroupDao groupDao = new GroupDao(connection);
            return(groupDao.setPayment(payment)&&notificationSender.paymentNotification(payment));

        } catch (SQLException e) {
            log.info("Add payment failed");
            throw new ServerErrorException("Failed to add payment", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

}
