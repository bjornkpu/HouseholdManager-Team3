package services;
import data.Group;
import data.User;
import db.GroupDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.*;
/**
 * -Description of the class-
 *
 * @author
 */
@Path("/groups/")
    public class GroupService {

        @GET
        @Path("/{groupid}")
        @Produces(MediaType.APPLICATION_JSON)
        public Group getGroup(@PathParam("groupid") int groupid) throws SQLException {
            Group result = GroupDao.getGroup(groupid);
            if(result!=null){
                return result;
            }else {
                throw new javax.ws.rs.NotFoundException();
            }
    }
}
