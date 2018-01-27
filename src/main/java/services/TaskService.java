package services;

import data.Chore;
import data.Session;
import data.User;
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


	public TaskService() {
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
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}
		try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);

			return choreDao.getChores(groupId);
		} catch (SQLException e) {
			log.error("Failed to get chores", e);
			throw new ServerErrorException("Failed to get chores", Response.Status.INTERNAL_SERVER_ERROR, e);
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
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			return choreDao.getChore(choreId);
		} catch (SQLException e) {
			log.error("Failed to get chore", e);
			throw new ServerErrorException("Failed to get chore", Response.Status.INTERNAL_SERVER_ERROR, e);
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
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			return choreDao.getCompletedBy(choreId);
		} catch (SQLException e) {
			log.error("Failed to get completed by array", e);
			throw new ServerErrorException("Failed to get completed by array", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Adds a {@link Chore}.
	 * @param task The {@link Chore} you want to add.
	 */
	@POST
	@Consumes("application/json")
	public Response addChore(Chore task) {
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			boolean s = choreDao.addChore(task);
			log.info("Added task!");
			if (s){
				return Response.status(200).entity(s).build();
			}
			return Response.status(404).entity(s).build();
		} catch(SQLException e) {
			log.error("Failed to Add task", e);
			throw new ServerErrorException("Failed to Add task", Response.Status.INTERNAL_SERVER_ERROR, e);
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
	public Response setCompletedBy(@PathParam("choreId") int choreId, ArrayList<String> users) {
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			boolean s= choreDao.setCompletedBy(choreId, users);
			log.info("Updated task!");
			if (s){
				return Response.status(200).entity(s).build();
			}
			return Response.status(404).entity(s).build();
		} catch(SQLException e) {
			log.error("Failed to update task", e);
			throw new ServerErrorException("Failed to set completed by", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Updates a {@link Chore} so the status of "taken" by a {@link data.User}.
	 *
	 * @param choreId ID of the {@link Chore} the user are to complete.
	 * @param user    The {@link data.User} who are to complete the chore.
	 */
	@PUT
	@Path("/{choreId}")
	@Consumes("application/json")
	public Response assignChore(@PathParam("choreId") int choreId, User user) {
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			boolean s = choreDao.assignChore(user, choreId);
			log.info("Assigned task!");
			if (s){
				return Response.status(200).entity(s).build();
			}
			return Response.status(404).entity(s).build();
		} catch(SQLException e) {
			log.error("Failed to assign task", e);
			throw new ServerErrorException("Failed to assign task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}

	/** Deletes a {@link Chore}.
	 *
	 * @param choreId ID of the {@link Chore}
	 */
	@DELETE
	@Path("/{choreId}")
	@Consumes("application/json")
	public Response deleteChore(@PathParam("choreId") int choreId) {
		Session session = (Session)request.getSession().getAttribute("session");
		//Check if there is a session
		if(session == null) {
			log.info("Session not found");
			throw new NotAuthorizedException("No session found",Response.Status.UNAUTHORIZED);
		}try(Connection connection = Db.instance().getConnection()) {
			ChoreDao choreDao = new ChoreDao(connection);
			choreDao.setCompletedBy(choreId,null);
			choreDao.assignChore(new User(null),choreId);
			boolean s = choreDao.deleteChore(choreId);
			log.info("Deleted task!");
			if (s){
				return Response.status(200).entity(s).build();
			}
			return Response.status(404).entity(s).build();
		} catch(SQLException e) {
			log.error("Failed to Delete task", e);
			throw new ServerErrorException("Failed to Delete task", Response.Status.INTERNAL_SERVER_ERROR, e);
		}
	}
}




















