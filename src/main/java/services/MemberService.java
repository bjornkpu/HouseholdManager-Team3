package services;

import data.Group;
import data.Member;
import db.GroupDao;
import db.MemberDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * This service handles the membership of a user within a group.
 *
 * @author Bjørn Kristian Punsvik
 * @author nybakk1
 * @version 0.1
 */

@Path("/groups/{groupId}/members")
public class MemberService {
    private static final Logger log = Logger.getLogger();
    private static MemberDao memberDao = new MemberDao();

    @Context
    private HttpServletRequest request;

    /**
     * Retrieves all the members in the group of a given groupId.
     * @param groupId The group to get the members of.
     * @return An arrayList of members in the given group.
     */

    @GET
    @Produces("application/json")
    public ArrayList<Member> getMemberByGroupId(@PathParam("groupId") int groupId) {
        try {
            return memberDao.getMembers(groupId);
        } catch (SQLException e){
            log.info("Could not fetch members.");
            throw new ServerErrorException("Failed to get members of group " + groupId, Response.Status.INTERNAL_SERVER_ERROR,e);
        }

    }


    /**
     * Invites a user to become a member of a group.
     *
     *
     * curl -H "Content-Type: application/json" -X POST -d {"email":"to@h.no"} http://localhost:8080/scrum/rest/groups/2/members/to@h.no
     * @param email Email of the user
     * @param groupId The group which the user is invited to.
     * @return Returns HTTP status 200 if successful, else HTTP status 500.
     */

    @POST
    @Path("/{email}")
    @Produces("application/json")
    public Response inviteAMember(@PathParam("email") String email,@PathParam("groupId") int groupId){
        try {
            return Response.status(200).entity(memberDao.inviteUser(email,groupId)).build();
        } catch (SQLException e){
            log.info("Could not invite user " + email);
            throw new ServerErrorException("Failed to invite user", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Updates a member of the group.
     *
     * @param email The member to be updated.
     * @param groupId Id of the group.
     * @return Returns HTTP status 200 if successful, else HTTP status 500.
     */


    @PUT
    @Produces("application/json")
    @Path("/{email}/{status}")
    public Response updateToMember(@PathParam("email") String email, @PathParam("groupId") int groupId,@PathParam("status") int status){
        try{
            log.info("Updating user " + email);
            Member member = new Member(email,null,null,null,null,-1,status);
            return  Response.status(200).entity(memberDao.updateUser(member,groupId)).build();
        } catch (SQLException e){
            log.info("Failed to update to member");
            throw new ServerErrorException("Failed to upgrade user",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Deletes a member from the group.
     * @param email Email of the member to be deleted.
     * @param groupId Id of the group.
     * @return Returns HTTP status 200 if successful, else HTTP status 500.
     */
    @DELETE
    @Produces("application/json")
    @Path("/{email}")
    public Response deleteMember(@PathParam("email") String email,@PathParam("groupId") int groupId){
        try {
            log.info("Deleting member " + email + " from group "  + groupId);
            return Response.status(200).entity(memberDao.deleteMember(email,groupId)).build();
        } catch (SQLException e){
            log.info("Failed to delete user");
            throw new ServerErrorException("Failed to delete user",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
}
