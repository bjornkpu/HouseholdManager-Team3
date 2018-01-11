package db;

import data.RepeatedTodo;
import util.Logger;
import data.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TodoDao {

    private static final Logger log = Logger.getLogger();

    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static ArrayList<String> findCompletedBy(int todoID) throws SQLException{
        connection = Db.instance().getConnection();
        try{
            ps= connection.prepareStatement("SELECT user_id FROM chore_log WHERE chore_id=?");
            ps.setInt(1,todoID);
            ResultSet res = ps.executeQuery();
            ArrayList<String> result = new ArrayList<>();
            while(res.next()){
                result.add(rs.getString("user_id"));
            }
            rs.close();
            ps.close();
            return result;
        }
        finally {
            connection.close();
        }
    }

    public static Todo getTodo(int todoId) throws SQLException{
        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM chore WHERE id=?");
            ps.setInt(1,todoId);
            ResultSet res = ps.executeQuery();
            Todo todo = null;
            if(res.next()){
                log.info("Found Todo with id: " + todoId);
                String regularityRead = rs.getString("regularity");
                int regularity = Integer.parseInt(regularityRead);
                if(regularity>0){
                    todo=new RepeatedTodo(regularity);
                }
                else{
                    todo = new Todo();
                }
                todo = new Todo();
                todo.setDescription(rs.getString("description"));
                ArrayList<String> completedBy = findCompletedBy(todoId);
                todo.setCompletedBy(completedBy);
                todo.setAssignedTo(rs.getString("assignedTo"));
            }
            else{
                log.info("Could not find Todo");
            }
            rs.close();
            ps.close();
            return todo;
        }
        finally {
            connection.close();
        }
    }
}
