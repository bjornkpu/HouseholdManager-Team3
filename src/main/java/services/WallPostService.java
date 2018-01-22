package services;

import data.WallPost;
import db.GroupDao;
import db.MemberDao;
import db.WallpostDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * Service for class {@code WallPost}
 *
 * @author nybakk1
 * @version 0.1
 */

@Path("/wallposts")
public class WallPostService {

    private static final Logger log = Logger.getLogger();
    private static final WallpostDao wallpostDao = new WallpostDao();

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{groupId}")
    @Produces("application/json")
    public static ArrayList<WallPost> getWallPostForGroup(@PathParam("groupId") int groupId){
        try {
            return wallpostDao.getWallposts(groupId);
        } catch (SQLException e){
            e.printStackTrace();
            throw new ServerErrorException("Failed to get wallposts from group" + groupId, Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
    @GET
    @Path("/{groupId}/{email}")
    public static ArrayList<WallPost> getWallPostsForUser(@PathParam("groupId") int groupId,@PathParam("email") String email){
        try {
            return wallpostDao.getWallposts(email,groupId);
        } catch (SQLException e){
            e.printStackTrace();
            throw new ServerErrorException("Failed to get wallposts for user", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }
}
