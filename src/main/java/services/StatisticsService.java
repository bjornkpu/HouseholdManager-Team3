package services;

import data.Session;
import data.StatisticsHelp;
import db.Db;
import db.StatisticsDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/groups/{groupId}/statistics/")
public class StatisticsService {
    private final Logger log = Logger.getLogger();

    @Context
    private HttpServletRequest request;

    @GET
    @Produces("application/json")
    public ArrayList<StatisticsHelp> getChoresPerUser(@PathParam("groupId") int groupId) throws SQLException {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }    try(Connection connection= Db.instance().getConnection()) {
            StatisticsDao sDao = new StatisticsDao(connection);
            return sDao.getChoresPerUser(groupId);
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Path("/costs")
    @Produces("application/json")
    public ArrayList<StatisticsHelp> getDisbursementCostPerUser(@PathParam("groupId") int groupId) throws SQLException {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }try(Connection connection= Db.instance().getConnection()) {
            StatisticsDao sDao = new StatisticsDao(connection);
            return sDao.getDisbursementCostPerUser(groupId);
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Path("/choreStatus")
    @Produces("application/json")
    public ArrayList<StatisticsHelp> getChoreStatus(@PathParam("groupId") int groupId) throws SQLException {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }try(Connection connection= Db.instance().getConnection()) {
            StatisticsDao sDao = new StatisticsDao(connection);
            return sDao.getChorStatusCount(groupId);
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @GET
    @Path("/userDebt")
    @Produces("application/json")
    public ArrayList<StatisticsHelp> getUserDebt(@PathParam("groupId") int groupId) throws SQLException {
        Session session = (Session)request.getSession().getAttribute("session");
        //Check if there is a session
        if(session == null) {
            log.info("Session not found");
            throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
        }try(Connection connection= Db.instance().getConnection()) {
            StatisticsDao sDao = new StatisticsDao(connection);
            return sDao.getUserDebt(groupId);
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
