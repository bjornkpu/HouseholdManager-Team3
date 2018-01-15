package services;
import data.User;
import db.UserDao;
import java.sql.SQLException;
import util.Logger;
import util.LoginCheck;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
/**
 * Service that handles reading, making, updating and deleting user information.
 *
 * @author BK
 * @author johanmsk
 */
@Path("user")
public class UserService {

    private static final Logger log = Logger.getLogger();

    private UserDao userDao = new UserDao();

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{email}")
    @Produces("application/json")
    public User get(@PathParam("email") String currentUserEmail) {
        try {
            return userDao.getUser(currentUserEmail);
        } catch(SQLException e) {
            log.error("Failed to get user", e);
            throw new ServerErrorException("Failed to get user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Consumes("application/json")
    public void add(User user) {
        String salt = LoginCheck.getSalt();
        user.setSalt(salt);
        user.setPassword(LoginCheck.getHash(user.getPassword()+salt));
        try {
            userDao.addUser(user);
            log.info("Added user!");
        } catch(SQLException e) {
            log.error("Failed to Add user", e);
            throw new ServerErrorException("Failed to Add user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PUT
    @Path("/{user}")
    @Consumes("application/json")
    public void update(User user) {
        try {
            userDao.updateUser(user);
            log.info("Updated user!");
        } catch(SQLException e) {
            log.error("Failed to update user", e);
            throw new ServerErrorException("Failed to update user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @DELETE
    @Path("/{email}")
    @Consumes("application/json")
    public void delete(String email) {
        try {
            userDao.delUser(email);
            log.info("Deleted user!");
        } catch(SQLException e) {
            log.error("Failed to Delete user", e);
            throw new ServerErrorException("Failed to Delete user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }


}