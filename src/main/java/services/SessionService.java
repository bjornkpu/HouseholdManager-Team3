package services;
import data.LoginData;
import data.Session;
import data.User;
import db.Db;
import db.UserDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import util.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static util.LoginCheck.correctLogin;
/**
 * Service to handle logon and logout using the web-session
 *
 * @author johanmsk
 */
@Path("/session/")
public class SessionService {

    private static final Logger log = Logger.getLogger();

    public SessionService() {
    }

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Session create(LoginData data) {
        log.info("Trying to logon");
        try(Connection connection= Db.instance().getConnection()) {
            UserDao userDao= new UserDao(connection);
            User user = userDao.getUser(data.getEmail());
            if(!correctLogin(data,user)) {
                log.info("Failed login. Username: "+ data.getEmail());
                throw new NotAuthorizedException("Wrong username or password");
            }
        } catch(SQLException e) {
            log.error("Failed to check user", e);
            throw new ServerErrorException("DB error", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
        Session session = new Session();
        session.setEmail(data.getEmail());
        session.setLoggedOn(new Date());
        request.getSession().invalidate();
        request.getSession().setAttribute("session", session);
        log.info("Logged on!");
        return session;
    }

    @GET
    @Produces("application/json")
    public Session get() {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }
        log.info("Returning session info!");
        return session;
    }

    @DELETE
    public void delete() {
        request.getSession().invalidate();
        log.info("Logged out!");
    }
}