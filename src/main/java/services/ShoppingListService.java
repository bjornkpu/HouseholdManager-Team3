package services;
import data.ShoppingList;
import db.ShoppingListDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
/**
 * Service that handles reading, making, updating and deleting shopping list information.
 *
 * @author BK
 */
@Path("shoppingList")
public class ShoppingListService {
    private static final Logger log = Logger.getLogger();

    private ShoppingListDao shoppingListDao = new ShoppingListDao();

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public ShoppingList get(@PathParam("id") int shoppingListId) {
        try {
            return ShoppingListDao.getShoppingList(shoppingListId);
        } catch(SQLException e) {
            log.error("Failed to get shopping list", e);
            throw new ServerErrorException("Failed to get shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @POST
    @Path("/{email}")
    @Consumes("application/json")
    public void add(ShoppingList user) {
        try {
            ShoppingListDao.addShoppingList(user);
            log.info("Added shopping list!");
        } catch(SQLException e) {
            log.error("Failed to Add shopping list", e);
            throw new ServerErrorException("Failed to Add shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PUT
    @Path("/{email}")
    @Consumes("application/json")
    public void update(ShoppingList user) {
        try {
            ShoppingListDao.updateShoppingList(user);
            log.info("Updated shopping list!");
        } catch(SQLException e) {
            log.error("Failed to update shopping list", e);
            throw new ServerErrorException("Failed to update shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PUT
    @Path("/{email}")
    @Consumes("application/json")
    public void delete(int shoppingListId) {
        try {
            ShoppingListDao.delShoppingList(shoppingListId);
            log.info("Deleted shopping list!");
        } catch(SQLException e) {
            log.error("Failed to Delete shopping list", e);
            throw new ServerErrorException("Failed to Delete shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
