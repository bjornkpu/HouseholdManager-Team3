package services;

import data.Disbursement;
import data.StatisticsHelp;
import db.Db;
import db.DisbursementDao;
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

@Path("/groups/{groupId}/disbursement/")
public class DisbursementService {
    private final Logger log = Logger.getLogger();

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{user_email}/user")
    @Produces("application/json")
    public ArrayList<Disbursement> getDisbursementList(@PathParam("groupId") int groupId, @PathParam("user_email") String user_email) throws SQLException {
//		Session session = (Session)request.getSession();
        try (Connection connection = Db.instance().getConnection()){
            DisbursementDao dDao = new DisbursementDao(connection);
            return dDao.getDisbursementsInGroup(groupId, user_email);
        } catch (SQLException e) {
            log.error("Failed to get Disbursement", e);
            throw new ServerErrorException("Failed to get Disbursement", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Consumes("application/json")
    public void addDisbursement(@PathParam("groupId") int groupId, Disbursement disbursement){
        log.info(disbursement.getName()+" to be added.");
        try (Connection connection = Db.instance().getConnection()){
            DisbursementDao dDao = new DisbursementDao(connection);
            dDao.addDisbursement(disbursement,groupId);
        } catch (SQLException e) {
            log.error("Failed to get Statistics", e);
            throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
