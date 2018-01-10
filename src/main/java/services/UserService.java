package services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/users/")
public class UserService {
    private static Map<String,Kunde> users = new HashMap<String,Kunde>();
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Kunde getUser(@PathParam("userId") String userId) {
        if(users.get(userId)!=null){
            return users.get(userId);
        }else {
            throw new javax.ws.rs.NotFoundException();
        }

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Kunde> getUsers() {
        return users.values();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(Kunde kunde) {
        users.put(kunde.getId(), kunde);
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteKunde(@PathParam("userId") String userId){
        users.remove(userId);
    }

    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateKunde(Kunde kunde){
        if(users.get(kunde.getId())!=null){
            users.put(kunde.getId(),kunde);
        }else {
            throw new javax.ws.rs.NotFoundException();

        }
    }
}