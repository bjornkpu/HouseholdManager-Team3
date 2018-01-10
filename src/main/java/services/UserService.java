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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user) throws SQLException {
        UserDao.addUser(user);
    }

    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateUser(User user) throws SQLException {
        if((user.getEmail())!=null){
            UserDao.updateUser(user);
                    }else {
            throw new javax.ws.rs.NotFoundException();

        }
    }
}