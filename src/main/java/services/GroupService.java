

package services;
import data.Group;
import data.User;
import db.GroupDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.*;



@Path("/groups/")
    public class GroupService {

    private static final Logger log = Logger.getLogger();
    private static GroupDao groupDao = new GroupDao();

    @Context
    private HttpServletRequest request;

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
            }
        }
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public List<Group> getGroup() {
            List<Group> groups = null;
            try {
                log.info("Retrieving all groups");
                return groupDao.getAllGroups();
            } catch (SQLException e) {
                log.info("Unable to get all groups");
                throw new ServerErrorException("Failed to get groups", Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        }

        /*
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public List<User> getUserlist(int groupId) {
            return kunder.values();
        }


        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public void addGroup(Group group) {
            try {
                groupDao.addParty(group);
            } catch (SQLException e) {
                log.info("Add group failed");
                throw new ServerErrorException("Failed to create group", Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        }

        @DELETE
        @Path("/{groupid}")
        @Produces(MediaType.APPLICATION_JSON)
        public void deleteGroup(@PathParam("groupid") Group group){
            try {
                groupDao.deleteParty(group.getId());
            } catch (SQLException e){
                log.info("Deleting group failed. Check constraints in database");
                throw new ServerErrorException("Failed to delete group", Response.Status.INTERNAL_SERVER_ERROR,e)
            }
        }

        @PUT
        @Path("/{email}")
        @Produces(MediaType.APPLICATION_JSON)
        public void updateGroup(Group kunde){
            if(kunder.get(kunde.getId())!=null){
                kunder.put(kunde.getId(),kunde);
            }else {
                throw new javax.ws.rs.NotFoundException();

            }
        }

    }
    */

}
