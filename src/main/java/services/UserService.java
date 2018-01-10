package services;

import data.User;
import db.UserDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/users/")
public class UserService {

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("userId") String userId) throws SQLException {
        if(UserDao.getUser(userId)!=null){
            return UserDao.getUser(userId);
        }else {
            throw new javax.ws.rs.NotFoundException();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<User> getUsers() {
        return users.values();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user) throws SQLException {
        UserDao.addUser(user);
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("userId") String userId){
        users.remove(userId);
    }

    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateUser(User user){
        if(users.get(user.getId())!=null){
            users.put(user.getId(),user);
        }else {
            throw new javax.ws.rs.NotFoundException();

        }
    }
}