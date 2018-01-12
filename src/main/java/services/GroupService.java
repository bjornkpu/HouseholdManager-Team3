/*

package services;
import data.Group;
import data.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;



@Path("/groups/")
    public class GroupService {

        @GET
        @Path("/{groupid}")
        @Produces(MediaType.APPLICATION_JSON)
        public Group getGroup(@PathParam("groupid") int groupid) {
            if(kunder.get(email)!=null){
                return kunder.get(email);
            }else {
                throw new javax.ws.rs.NotFoundException();
            }

        }
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public List<User> getUserlist(int groupId) {
            return kunder.values();
        }

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public void addGroup(Group kunde) {
            kunder.put(kunde.getId(), kunde);
        }

        @DELETE
        @Path("/{email}")
        @Produces(MediaType.APPLICATION_JSON)
        public void deleteGroup(@PathParam("email") String email){
            kunder.remove(email);
        }

        @PUT
        @Path("/{email}")
        @Produces(MediaType.APPLICATION_JSON)
        public void updateGroup(Group kunde){
            if(kunder.get(kunde.getId())!=null){
                kunder.put(kunde.getId(),kunde);
            }else {
                throw new javax.ws.rs.NotFoundException();

            }
        }
    }

}
*/