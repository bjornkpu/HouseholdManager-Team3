package services;
import data.ShoppingList;
import db.ShoppingListDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
/** Service that handles reading, making, updating and deleting shopping list information.
 * @author BK
 */
@Path("group/shoppingList")
public class ShoppingListService {
    private static final Logger log = Logger.getLogger();

    private ShoppingListDao shoppingListDao = new ShoppingListDao();

    @Context
    private HttpServletRequest request;

	/** Method that gets a shopping list given the shopping list id.
	 * @param shoppingListId The ID of the shopping list you are trying to get.
	 * @return Returns the shopping list that corresponds to the id given.
	 * @throws ServerErrorException when failing to get shopping list.
	 */
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public ShoppingList get(@PathParam("id") int shoppingListId) {
        try {
            return shoppingListDao.getShoppingList(shoppingListId);
        } catch(SQLException e) {
            log.error("Failed to get shopping list", e);
            throw new ServerErrorException("Failed to get shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

	/** Method that adds a shopping list.
	 * @param shoppingList The shopping list you are adding
	 * @throws ServerErrorException when failing to add shopping list.
	 */
    @POST
    @Path("/{email}")
    @Consumes("application/json")
    public void add(ShoppingList shoppingList) {
        try {
            shoppingListDao.addShoppingList(shoppingList);
            log.info("Added shopping list!");
        } catch(SQLException e) {
            log.error("Failed to Add shopping list", e);
            throw new ServerErrorException("Failed to Add shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /** Method that updates a shopping list.
     * @param shoppingList The shopping list you are updating to.
     * @throws ServerErrorException when failing to update shopping list.
     */
    @PUT
    @Path("/{email}")
    @Consumes("application/json")
    public void update(ShoppingList shoppingList) {
        try {
            shoppingListDao.updateShoppingList(shoppingList);
            log.info("Updated shopping list!");
        } catch(SQLException e) {
            log.error("Failed to update shopping list", e);
            throw new ServerErrorException("Failed to update shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /** Method that deletes a shopping list given the shopping list id.
     * @param shoppingListId The ID of the shopping list you are trying to delete.
     * @throws ServerErrorException when failing to delete shopping list.
     */
    @DELETE
    @Path("/{email}")
    @Consumes("application/json")
    public void delete(int shoppingListId) {
        try {
            shoppingListDao.delShoppingList(shoppingListId);
            log.info("Deleted shopping list!");
        } catch(SQLException e) {
            log.error("Failed to Delete shopping list", e);
            throw new ServerErrorException("Failed to Delete shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
