package services;

import data.Group;
import data.Member;
import db.GroupDao;
import db.MemberDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
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
    @GET
    @Produces("application/json")
    @Path("/{email}")
    public ArrayList<Group> getGroupsConnectedToMember(@PathParam("email") String email){
        try {
            log.info("Retrieving groups by member.");
            return memberDao.getGroupsByMember(email);
        } catch (SQLException e){
            log.info("Could not get groups");
            throw new ServerErrorException("Failed to get groups.",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
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
        }
    }

    /**
     * curl -H "Content-Type: application/json" -X POST -d {"email":"to@h.no"} http://localhost:8080/scrum/rest/groups/2/members/to@h.no
     * @param email
     * @param groupId
     * @return
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
    @PUT
    @Produces("application/json")
    @Path("(/{email}")
    public Response updateToMember(Member member, @PathParam("groupId") int groupId){
        try{
            log.info("Updating user" + member.getEmail());
            return  Response.status(200).entity(memberDao.updateUser(member,groupId)).build();
        } catch (SQLException e){
            log.info("Failed to update to member");
            throw new ServerErrorException("Failed to upgrade user",Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
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
