package services;
import data.Group;
import db.Db;
import db.GroupDao;
import db.MemberDao;
import db.UserDao;
import util.Logger;

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
 * @version 0.1
 */
@Path("/groups/")
public class GroupService {

    private static final Logger log = Logger.getLogger();
    private Connection connection;
    private GroupDao groupDao;
    private MemberDao memberDao;

    public GroupService() {
        try{
            connection= Db.instance().getConnection();
            this.groupDao = new GroupDao(connection);
            this.memberDao = new MemberDao(connection);
        }catch(SQLException e){
            log.error("Failed to get connection", e);
        }
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
        Group group = null;
        try {
            log.info("Group #" + groupid + " found.");
            return groupDao.getGroup(groupid);
        } catch (SQLException e) {
            log.info("Could not find group #" + groupid);
            throw new ServerErrorException("Failed to get group", Response.Status.INTERNAL_SERVER_ERROR,e);
        }finally {
            Db.close(connection);
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
        List<Group> groups = null;
        try {
            log.info("Retrieving all groups");
            groups = groupDao.getAllGroups();
            return groups;
        } catch (SQLException e) {
            log.info("Unable to get all groups");
            throw new ServerErrorException("Failed to get groups", Response.Status.INTERNAL_SERVER_ERROR, e);
        }finally {
            Db.close(connection);
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
        try{
            log.info("Retrieving group invites for member " + email);
            return memberDao.getGroupInvites(email);
        } catch (SQLException e){
            log.info("Failed to retrieve invites for member " + email);
            throw new ServerErrorException("Failed to retrieve invites",Response.Status.INTERNAL_SERVER_ERROR,e);
        }finally {
            Db.close(connection);
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
        try {
            log.info("Retrieving groups by member.");
            return memberDao.getGroupsByMember(email);
        } catch (SQLException e){
            log.info("Could not get groups");
            throw new ServerErrorException("Failed to get groups.",Response.Status.INTERNAL_SERVER_ERROR,e);
        }finally {
            Db.close(connection);
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
        try {
            log.info("Group added. ID: " + group.getId());
            int s = groupDao.addGroup(group);
            if (s != -1){
                return Response.status(200).entity(s).build();
            }
            return Response.status(404).entity(-1).build();

        } catch (SQLException e) {
            log.info("Add group failed. ID:"+group.getId());
            throw new ServerErrorException("Failed to create group", Response.Status.INTERNAL_SERVER_ERROR, e);
        }finally {
            Db.close(connection);
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
        try {
            groupDao.deleteGroup(groupId);
        } catch (SQLException e){
            log.info("Deleting group failed. Check constraints in database");
            throw new ServerErrorException("Failed to delete group", Response.Status.INTERNAL_SERVER_ERROR,e);
        }finally {
            Db.close(connection);
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
        try {
            log.info("Updating group. ID: " + group.getId());
            groupDao.updateGroup(group);
        } catch (SQLException e){
            log.info("Updating group failed. ID " + group.getId());
            throw new ServerErrorException("Failed to update group",Response.Status.INTERNAL_SERVER_ERROR);
        }finally {
            Db.close(connection);
        }

    }
}
