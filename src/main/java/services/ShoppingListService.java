package services;

import data.Item;
import data.Session;
import data.ShoppingList;
import data.User;
import db.Db;
import db.ItemDao;
import db.ShoppingListDao;
import db.UserDao;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/** Service that handles reading, making, updating and deleting shopping list information.
 * @author BK
 * @author johanmsk
 * @author Martin Wangen
 * @author enoseber
 */
@Path("/groups/{groupId}/shoppingLists/")
public class ShoppingListService {
    private static final Logger log = Logger.getLogger();

	public ShoppingListService() {
	}

	@Context
    private HttpServletRequest request;

	/** Method that gets an ArrayList of shoppingLists for the given group.
	 * @param groupId The ID of the group you are trying to get the shopping lists for.
	 * @return Returns an ArrayList of shopping lists that corresponds to the groupId given.
	 * @throws ServerErrorException when failing to get shopping list.
	 */
	@GET
	@Produces("application/json")
	public ArrayList<ShoppingList> getShoppingListByGroupId(@PathParam("groupId") int groupId) {
//		Session session = (Session)request.getSession();
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);

			return shoppingListDao.getShoppingListByGroupid(groupId);
		} catch(SQLException e) {
			log.error("Failed to get shopping list array", e);
			throw new ServerErrorException("Failed to get shopping list array", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	@GET
	@Path("/user")
	@Produces("application/json")
	public ArrayList<ShoppingList> getShoppingListByUserInGroup(@PathParam("groupId") int groupId){
		Session session = (Session) request.getSession().getAttribute("session");
		log.info("Session: = " + session.getEmail());
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
			return shoppingListDao.getShoppingListByUserInGroup(groupId, session.getEmail());
		} catch (SQLException e) {
			log.error("Failed to get shopping list array", e);
			throw new ServerErrorException("Failed to get shopping list array", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Method that adds a shopping list.
	 * @param shoppingList The shopping list you are adding
	 * @throws ServerErrorException when failing to add shopping list.
	 */
    @POST
    @Path("/addShoppinglist")
    @Consumes("application/json")
    public void addShoppingList(ShoppingList shoppingList) {
        try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
            shoppingListDao.addShoppingList(shoppingList);
        } catch(SQLException e) {
            log.error("Failed to Add shopping list", e);
            throw new ServerErrorException("Failed to Add shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /** Method that updates the name and/or party_id of a shopping list. NOT the item/user-array.
     * @param shoppingList The shopping list you are updating to.
     * @throws ServerErrorException when failing to update shopping list.
     */
    @PUT
    @Consumes("application/json")
    public void updateShoppingList(ShoppingList shoppingList) {
        try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
            shoppingListDao.updateShoppingList(shoppingList);
            log.info("Updated shopping list!");
        } catch(SQLException e) {
            log.error("Failed to update shopping list", e);
            throw new ServerErrorException("Failed to update shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

	/** Method that gets a shopping list given the shopping list.
	 * @param shoppingListId The ID of the shopping list you are trying to get.
	 * @return Returns the shopping list that corresponds to the id given.
	 * @throws ServerErrorException when failing to get shopping list.
	 */
	@GET
	@Path("/{shoppingListId}")
	@Produces("application/json")
	public ShoppingList getShoppingList(@PathParam("shoppingListId") int shoppingListId) {
		Session session = (Session)request.getSession();
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
			return shoppingListDao.getShoppingList(shoppingListId,session.getEmail());
		} catch(SQLException e) {
			log.error("Failed to get shopping list", e);
			throw new ServerErrorException("Failed to get shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

    /** Method that deletes a shopping list given the shopping list id.
     * @param shoppingListId The ID of the shopping list you are trying to delete.
     * @throws ServerErrorException when failing to delete shopping list.
     */
    @DELETE
    @Path("/{shoppingListId}")
    @Consumes("application/json")
    public void deleteShoppingList(@PathParam("shoppingListId") int shoppingListId) {
        try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
            shoppingListDao.delShoppingList(shoppingListId);
            log.info("Deleted shopping list!");
        } catch(SQLException e) {
            log.error("Failed to Delete shopping list", e);
            throw new ServerErrorException("Failed to Delete shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /** Lists all items in a shopping list.
     * @param shoppingListId the shopping list you want to view all item on.
     * @return the ArrayList of items in the shopping list.
     * @throws ServerErrorException when failing to get item list.
     */
	@GET
	@Path("/{shoppingListId}/items")
	@Produces("application/json")
	public ArrayList<Item> getItemsInShoppingList(@PathParam("shoppingListId") int shoppingListId){
		try(Connection connection= Db.instance().getConnection()){
			ItemDao itemDao = new ItemDao(connection);
			return itemDao.getItemsInShoppingList(shoppingListId);
		} catch(SQLException e){
			log.error("Failed to get item list", e);
			throw new ServerErrorException("Failed to get item list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Adds an item to a shopping list
	 * @param item The item you want to add to the shopping list
	 * @throws ServerErrorException when failing to get shopping list.
	 */
	@POST
	@Path("/{shoppingListId}/items")
	@Consumes("application/json")
	public void addItemToShoppingList(@PathParam("shoppingListId")int shoppingListId, Item item){
		try(Connection connection= Db.instance().getConnection()) {
			ItemDao itemDao = new ItemDao(connection);
			itemDao.addItem(item);
			log.info("Item added to shopping list!");
		} catch (SQLException e){
			log.error("Failed to add item to shopping list", e);
			throw new ServerErrorException("Failed to add item to shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Lists an item in a shopping list given the item ID.
	 * @param shoppingListId The id to the shopping list.
	 * @param itemId the id to the item you are trying to get.
	 * @return The item with the id given.
	 * @throws ServerErrorException when failing to get the item.
	 */
	@GET
	@Path("/{shoppingListId}/items/{itemId}")
	@Produces("application/json")
	public Item getItemById(@PathParam("shoppingListId") int shoppingListId,@PathParam("itemId") int itemId){
		try(Connection connection= Db.instance().getConnection()){
			ItemDao itemDao = new ItemDao(connection);
			return itemDao.getItem(itemId);
		} catch(SQLException e){
			log.error("Failed to get item by id", e);
			throw new ServerErrorException("Failed to get item by id", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** update the item with the given id.
	 * @param items the item you are trying to update.
	 * @throws ServerErrorException when failing to get the item.
	 */
	@PUT
	@Path("/items/{status}")
	@Consumes("application/json")
	public void updateItems(@PathParam("status") int status, ArrayList<Item> items){
		try(Connection connection= Db.instance().getConnection()){
			ItemDao itemDao = new ItemDao(connection);
			if(status==2||status==1) {
				for (Item item : items) {
					item = itemDao.getItem(item.getId());
					item.setStatus(status);
					itemDao.updateItem(item);
				}

			}
		} catch(SQLException e){
			log.error("Failed to update item", e);
			throw new ServerErrorException("Failed to update item", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Delete the item with the given Id.
	 * @param items items you are trying to delete.
	 * @throws ServerErrorException when failing to get the item.
	 */
	@DELETE
	@Path("/items/")
	@Consumes("application/json")
	public void delItemById(ArrayList<Item> items){
		try(Connection connection= Db.instance().getConnection()){
			ItemDao itemDao = new ItemDao(connection);
			for(Item item : items){
				itemDao.delItem(item.getId());
			}
		} catch(SQLException e){
			log.error("Failed to delete item", e);
			throw new ServerErrorException("Failed to delete item", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Lists all users in a shopping list.
	  @param shoppingListId the shopping list you want to view all users on.
	 * @return the ArrayList of users in the shopping list.
	 * @throws ServerErrorException when failing to get user list.
	 */
	@GET
	@Path("/{shoppingListId}/users")
	@Produces("application/json")
	public ArrayList<User> getUsersInShoppingList(@PathParam("shoppingListId") int shoppingListId){
		try(Connection connection= Db.instance().getConnection()){
			UserDao userDao = new UserDao(connection);
			return userDao.getUsersInShoppingList(shoppingListId);
		} catch(SQLException e){
			log.error("Failed to get user list", e);
			throw new ServerErrorException("Failed to get user list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Adds a user to a shopping list
	 * @param user The user you want to add to the shopping list
	 * @throws ServerErrorException when failing to get shopping list.
	 */
	@POST
	@Path("/{shoppingListId}/users")
	@Consumes("application/json")
	public void addUserToShoppingList(@PathParam("shoppingListId")int shoppingListId, User user){
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
//			TODO check if works
			shoppingListDao.addUserToShoppingList(user.getEmail(), shoppingListId);
			log.info("User added to shopping list!");
		} catch (SQLException e){
			log.error("Failed to add user to shopping list", e);
			throw new ServerErrorException("Failed to add user to shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Add a specific user to a shopping list.
	 *
	 * @param shoppingListId the shopping list you want to add a user to
	 * @param userId the id of the user ou want to add to the shopping list
	 * @throws ServerErrorException when failing to add the user to the shopping list.
	 */
	@POST
	@Path("/{shoppingListId}/users/{userId}")
	public void addUserToShoppingList(@PathParam("shoppingListId") int shoppingListId,
	                                  @PathParam("userId") String userId){
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
			shoppingListDao.addUserToShoppingList(userId, shoppingListId);
		}catch (SQLException e) {
			log.error("Failed to add user to shopping list", e);
			throw new ServerErrorException("Failed to add user to shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** remove a specific user from a shopping list.
	 *
	 * @param shoppingListId the shopping list you want to remove a user from
	 * @param userId the id of the user ou want to remove from the shopping list
	 * @throws ServerErrorException when failing to remove the user from the shopping list.
	 */
	@DELETE
	@Path("/{shoppingListId}/users/{userId}")
	public void removeUserFromShoppingList(@PathParam("shoppingListId") int shoppingListId,
	                                  @PathParam("userId") String userId){
		try(Connection connection= Db.instance().getConnection()) {
			ShoppingListDao shoppingListDao = new ShoppingListDao(connection);
			shoppingListDao.removeUserFromShoppingList(userId, shoppingListId);
		}catch (SQLException e) {
			log.error("Failed to remove user from shopping list", e);
			throw new ServerErrorException("Failed to remove user from shopping list", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}
}
