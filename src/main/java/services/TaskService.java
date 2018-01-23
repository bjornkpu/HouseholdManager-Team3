package services;

import data.Chore;
import db.ChoreDao;
import db.Db;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/** Service that handles reading, making, updating and deleting Task information.
 * @author BK
 */
@Path("/groups/{groupId}/task/")
public class TaskService {
	private static final Logger log = Logger.getLogger();

	private ChoreDao taskDao;
	private Connection connection;

	public TaskService() {
		try{
			connection= Db.instance().getConnection();
			taskDao = new ChoreDao(connection);
		}catch(SQLException e){
			log.error("Failed to get connection", e);
		}
	}

	@Context
	private HttpServletRequest request;

	/** Gets an {@link ArrayList} of {@link data.Chore} to the given group.
	 *
	 * @param groupId ID of the group the chores belongs to.
	 * @return The {@link Chore}.
	 */
	@GET
	@Produces("application/json")
	public ArrayList<Chore> getChores(@PathParam("groupId") int groupId) {
		try {
			return taskDao.getChores(groupId);
		} catch (SQLException e) {
			log.error("Failed to get chores", e);
			throw new ServerErrorException("Failed to get chores", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Gets a {@link data.Chore} with the given chore ID.
	 *
	 * @param choreId ID of the chore to fetch.
	 * @return The {@link Chore}.
	 */
	@GET
	@Path("/{choreId}")
	@Produces("application/json")
	public Chore getChore(@PathParam("choreId") int choreId) {
		try {
			return taskDao.getChore(choreId);
		} catch (SQLException e) {
			log.error("Failed to get chore", e);
			throw new ServerErrorException("Failed to get chore", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Gets an {@link ArrayList} of Strings with who compleated the {@link Chore}.
	 *
	 * @param choreId ID of the {@link Chore} you want to get who compleated it.
	 * @return The {@link ArrayList} of who compleated it.
	 */
	@GET
	@Path("/CompletedBy/{choreId}")
	@Produces("application/json")
	public ArrayList<String> getCompletedBy(@PathParam("choreId") int choreId) {
		try {
			return taskDao.getCompletedBy(choreId);
		} catch (SQLException e) {
			log.error("Failed to get completed by array", e);
			throw new ServerErrorException("Failed to get completed by array", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Adds a {@link Chore}.
	 * @param task The {@link Chore} you want to add.
	 */
	@POST
	@Consumes("application/json")
	public void addChore(Chore task) {
		try {
			taskDao.addChore(task);
			log.info("Added task!");
		} catch(SQLException e) {
			log.error("Failed to Add task", e);
			throw new ServerErrorException("Failed to Add task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Updates who completed the {@link Chore}.
	 *
	 * @param choreId ID of the {@link Chore}.
	 * @param users The {@link ArrayList} of {@link data.User} who completed the{@link Chore}.
	 */
	@PUT
	@Path("/CompletedBy/{choreId}")
	@Consumes("application/json")
	public void setCompletedBy(@PathParam("choreId") int choreId, ArrayList<String> users) {
		try {
			taskDao.setCompletedBy(choreId, users);
			log.info("Updated task!");
		} catch(SQLException e) {
			log.error("Failed to update task", e);
			throw new ServerErrorException("Failed to update task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Updates a {@link Chore} so the status of "taken" by a {@link data.User}.
	 *
	 * @param userEmail ID/Email of the {@link data.User} who are to complete the chore.
	 * @param choreId ID of the {@link Chore} the user are to complete.
	 */
	@PUT
	@Path("/{choreId}")
	@Consumes("application/json")
	public void assignChore(@PathParam("choreId") int choreId, String userEmail) {
		try {
			taskDao.assignChore(userEmail, choreId);
			log.info("Updated task!");
		} catch(SQLException e) {
			log.error("Failed to update task", e);
			throw new ServerErrorException("Failed to update task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}

	/** Deletes a {@link Chore}.
	 *
	 * @param choreId ID of the {@link Chore}
	 */
	@DELETE
	@Path("/{choreId}")
	@Consumes("application/json")
	public void deleteChore(@PathParam("choreId") int choreId) {
		try {
			taskDao.deleteChore(choreId);
			log.info("Deleted task!");
		} catch(SQLException e) {
			log.error("Failed to Delete task", e);
			throw new ServerErrorException("Failed to Delete task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}finally {
			Db.close(connection);
		}
	}
}




















