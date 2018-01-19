package services;

import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;

@Path("/groups/{groupId}/statistics/")
public class StatisticsService {
        private final Logger log = Logger.getLogger();

        @Context
        private HttpServletRequest request;

        @GET
        @Produces("application/json")
        public HashMap<String,Integer> getChoresPerUser(@PathParam("groupId") int groupId) throws SQLException {
//		Session session = (Session)request.getSession();
            try {
                return getChoresPerUser(groupId);
            } catch (SQLException e) {
                log.error("Failed to get Statistics", e);
                throw new ServerErrorException("Failed to get Statistics", Response.Status.INTERNAL_SERVER_ERROR, e);
            }
        }
    }
