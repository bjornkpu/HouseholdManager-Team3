package services;
import data.Session;
import data.User;
import db.Db;
import db.UserDao;

import java.sql.Connection;
import java.sql.SQLException;

import util.EmailSender;
import util.InputChecker;
import util.Logger;
import util.LoginCheck;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static util.EmailSender.sendConfirmationMail;
import static util.InputChecker.isEMail;
import static util.InputChecker.isName;

/**
 * Service that handles reading, making, updating and deleting user information.
 *
 * @author BK
 * @author johanmsk
 */
@Path("user")
public class UserService {

    private static final Logger log = Logger.getLogger();

    public UserService() {
    }

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{email}")
    @Produces("application/json")
    public User get(@PathParam("email") String currentUserEmail) {
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao = new UserDao(connection);
            return userDao.getUser(currentUserEmail);
        } catch(SQLException e) {
            log.error("Failed to get user", e);
            throw new ServerErrorException("Failed to get user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Consumes("application/json")
    public Response add(User user) {
        String salt = LoginCheck.getSalt();
        user.setSalt(salt);
        String pw=LoginCheck.getHash(user.getPassword()+salt);
        user.setPassword(pw);
        log.info("Salt: "+salt+" PW: "+pw);
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao = new UserDao(connection);
            boolean okEmail = isEMail(user.getEmail());
            boolean okName = isName(user.getName());
            boolean okAll = (okEmail && okName);
            log.info("name: " + user.getName());
            log.info("Email: " + user.getEmail());
            log.info("okEmail: " + okEmail);
            log.info("okName: " + okName);
            log.info("okAll: "+okAll);
            if (okAll){
                if(userDao.addUser(user)) {
                    sendConfirmationMail(user.getEmail());
                    log.info("Added user!");
                    return Response.status(200).entity(okAll).build();
                }
            }return Response.status(400).entity(okAll).build();
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
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao = new UserDao(connection);
            User oldUser = userDao.getUser(currentUserEmail);
            if(user.getPassword()==null || user.getPassword().equals("")){
                user.setPassword(oldUser.getPassword());
                user.setSalt(oldUser.getSalt());
            }if(user.getName()==null || user.getName().equals("")){
                user.setName(oldUser.getName());
                user.setPhone(oldUser.getPhone());
                String salt = LoginCheck.getSalt();
                user.setSalt(salt);
                String pw=LoginCheck.getHash(user.getPassword()+salt);
                user.setPassword(pw);
                log.info("Salt: "+salt+" PW: "+pw);
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
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao = new UserDao(connection);
            User user = userDao.getUser(toEmail);
            if(user!=null){
                newPassword = EmailSender.generateNewPassword(toEmail);
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
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao = new UserDao(connection);

            userDao.delUser(email);
            log.info("Deleted user!");
        } catch(SQLException e) {
            log.error("Failed to Delete user", e);
            throw new ServerErrorException("Failed to Delete user", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }


    @GET
	@Path("/emailCheck/{email}")
    @Produces("application/json")
	public boolean checkEmail(@PathParam("email") String email){
        return isEMail(email);     //returns true if the format is: *@*.**
    }
}