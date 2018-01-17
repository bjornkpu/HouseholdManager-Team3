package services;
import data.Session;
import data.User;
import db.Db;
import db.UserDao;
import java.sql.SQLException;

import util.ForgottenPassword;
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
        String pw=LoginCheck.getHash(user.getPassword()+salt);
        user.setPassword(pw);
        log.info("Salt: "+salt+" PW: "+pw);
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
        Session session = (Session)request.getSession().getAttribute("session");
        String currentUserEmail = session.getEmail();
        System.out.println(currentUserEmail);
        try {
            User oldUser = userDao.getUser(currentUserEmail);
            if(user.getPassword()==null || user.getPassword().equals("")){
                user.setPassword(oldUser.getPassword());
                user.setSalt(oldUser.getSalt());
            }if(user.getName()==null || user.getName().equals("")){
                user.setName(oldUser.getName());
                user.setPhone(oldUser.getPhone());
            }
            user.setEmail(currentUserEmail);
            userDao.updateUser(user);
            log.info("Updated user!");
        } catch(SQLException e) {
            log.error("Failed to update user", e);
            throw new ServerErrorException("Failed to update user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
    @PUT
    @Path("/forgotPw/{toEmail}")
    public void genNewPassword(@PathParam("toEmail") String toEmail) {
            String newPassword = null;
            log.info("Reset password request recieved!");
        try {
            User user = UserDao.getUser(toEmail);
            if(user!=null){
                newPassword = ForgottenPassword.generateNewPassword(toEmail);
                newPassword = LoginCheck.getHash(newPassword);
                String salt = LoginCheck.getSalt();
                user.setSalt(salt);
                String pw=LoginCheck.getHash(newPassword+salt);
                user.setPassword(pw);
                log.info("Salt: "+salt+" PW: "+pw);
                userDao.updateUser(user);
                log.info("Updated user!");
            }
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