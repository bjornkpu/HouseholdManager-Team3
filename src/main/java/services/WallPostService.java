package services;

import data.WallPost;
import db.Db;
import db.WallpostDao;
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
 * Service for class {@code WallPost}
 *
 * @author nybakk1
 * @version 0.1
 */

@Path("/wallposts")
public class WallPostService {

    private static final Logger log = Logger.getLogger();


    public WallPostService(){
    }

    @Context
    private HttpServletRequest request;

    /**
     * Gets all the {@code Wallposts} posted in a {@code Group}.
     *
     * @param groupId Id of the {@code Group}.
     * @return Returns an {@code ArrayList} of {@code WallPost} objects if successful, else returns HTTP status code 500.
     */

    @GET
    @Path("/{groupId}")
    @Produces("application/json")
    public ArrayList<WallPost> getWallPostForGroup(@PathParam("groupId") int groupId){
        try (Connection connection= Db.instance().getConnection()){
            WallpostDao wallpostDao = new WallpostDao(connection);

            return wallpostDao.getWallposts(groupId);
        } catch (SQLException e){
            throw new ServerErrorException("Failed to get wallposts from group" + groupId, Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Gets all the {@code WallPosts} from a {@code user} in a {@code Group}.
     * @param groupId Id of the {@code Group}.
     * @param email {@code email} of the {@code User}.
     * @return Returns an {@code ArrayList} of {@code WallPost} objects if successful, else returns HTTP status code 500.
     */
    @GET
    @Path("/{groupId}/{email}")
    @Produces("application/json")
    public ArrayList<WallPost> getWallPostsForUser(@PathParam("groupId") int groupId,@PathParam("email") String email){
        try (Connection connection= Db.instance().getConnection()){
            WallpostDao wallpostDao = new WallpostDao(connection);

            return wallpostDao.getWallposts(email,groupId);
        } catch (SQLException e){
            throw new ServerErrorException("Failed to get wallposts for user", Response.Status.INTERNAL_SERVER_ERROR,e);
        }
    }

    /**
     * Posts a new {@code WallPost} to the database.
     * @param wallPost The {@code Wallpost} object to be added.
     * @return {@code Response Code 200} if successful, else returns {@code Respons Code 500} if not.
     */
    @POST
    @Produces("application/json")
    public Response postWallPost(WallPost wallPost){
        try (Connection connection= Db.instance().getConnection()) {
            WallpostDao wallpostDao = new WallpostDao(connection);

            return Response.status(200).entity(wallpostDao.postWallpost(wallPost)).build();
        } catch (SQLException e) {
            throw new ServerErrorException("Failed to post to wall", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Deletes a {@code WallPost} from the database.
     * @param wallPost The {@code Wallpost} object to be added.
     * @return {@code Response Code 200} if successful, else returns {@code Respons Code 500} if not.
     */
    @DELETE
    @Produces("application/json")
    public Response deleteWallPost(WallPost wallPost){
        try (Connection connection= Db.instance().getConnection()){
            WallpostDao wallpostDao = new WallpostDao(connection);
            return Response.status(200).entity(wallpostDao.deleteWallpost(wallPost.getId())).build();
        } catch (SQLException e){
            throw new ServerErrorException("Failed to delete wallpost", Response.Status.INTERNAL_SERVER_ERROR,e);
        }

    }
}
