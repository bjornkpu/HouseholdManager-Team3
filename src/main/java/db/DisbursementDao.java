package db;

import data.Disbursement;
import data.Item;
import data.User;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * -Description of the class-
 *
 * @author johanmsk
 */
public class DisbursementDao {
    private static final Logger log = Logger.getLogger();

    private Connection connection;
    private PreparedStatement ps;
    private Statement statement;
    private ResultSet rs;
    private ResultSet res;

    public DisbursementDao(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<User> getParticipants(int disbursementID) throws SQLException{
//        connection = Db.instance().getConnection();
        try{
            ps= connection.prepareStatement("SELECT * FROM user_disbursement ud JOIN user u ON ud.user_email = u.email WHERE disp_id=?");
            ps.setInt(1,disbursementID);
            res = ps.executeQuery();
            ArrayList<User> result = new ArrayList<>();
            while(res.next()){
                User user = new User();
                user.setEmail(res.getString("user_email"));
                user.setName(res.getString("name"));
                result.add(user);
            }
            return result;
        }
        finally {
            Db.close(res);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    private int getBuyer(ArrayList<User> users, String email){
        for(int i=0; i<users.size();i++){
            if(users.get(i).getEmail().equals(email)){
                return i;
            }
        }
        return -1;
    }


    /**Fetches a list of disbursements in a group, where the user is a participant of the disbursement
     *
     * @param groupId The id of the group to get disbursements from.
     * @param email The email of the user.
     * @return An {@link ArrayList} of {@link Disbursement}s, without participants or items.
     * @throws SQLException
     */
    public ArrayList<Disbursement> getDisbursementsInGroup(int groupId, String email) throws SQLException{
//        connection = Db.instance().getConnection();
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud ON d.id = ud.disp_id WHERE d.party_id=? AND ud.user_email=?");
            ps.setInt(1,groupId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            ArrayList<Disbursement> disbursements= null;
            Disbursement disbursement = null;
            if(rs.first()){
                disbursements = new ArrayList<Disbursement>();
                do{
                    log.info("Found disbursement " + groupId + " in group: " +groupId+ " with user: "+email);
                    disbursement = new Disbursement();
                    disbursement.setId(rs.getInt("id"));
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    ArrayList<User> users = getParticipants(rs.getInt("id"));
                    disbursement.setParticipants(users);
                    int index = getBuyer(users,rs.getString("payer_id"));
                    if(index>=0) disbursement.setPayer(users.get(index));
                    disbursements.add(disbursement);
                }while(rs.next());
            } else {
                log.info("Could not find any disbursement in group: "+groupId+" user: " + email);
            }
            return disbursements;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);g_tdat2003_t3@mysql.stud.iie.ntnu.no
        }
    }
    public void testmethod(){
        return;
    }

    /**Fetches the participants and items in a given disbursement, if the user
     * is one of the participants.
     *
     * @param disbursementId The id of the disbursement to fetch.
     * @param email The id of the user.
     * @return The {@link Disbursement}, with complete data including items and participants.
     * @throws SQLException
     */
    public  Disbursement getDisbursementDetails(int disbursementId, String email)throws SQLException{
//        connection = Db.instance().getConnection();
        UserDao userDao= new UserDao(connection);
        ItemDao itemDao= new ItemDao(connection);
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud ON d.id = ud.disp_id WHERE d.id=? AND ud.user_email=?");
            ps.setInt(1,disbursementId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            ArrayList<Item> items = new ArrayList<Item>();
            ArrayList<User> users = new ArrayList<User>();
            Disbursement disbursement = null;
            if(rs.first()){
                do{
                    log.info("Found disbursement " + disbursementId);
                    disbursement = new Disbursement();
                    disbursement.setId(rs.getInt("id"));
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    disbursement.setParticipants(userDao.getUsersInDisbursement(disbursementId));
                    disbursement.setItems(itemDao.getItemsInDisbursement(disbursementId));
                }while(rs.next());
            } else {
                log.info("Could not find any disbursement with id: "+disbursementId);
            }
            return disbursement;
        } finally {
            Db.close(rs);
            Db.close(ps);
//            Db.close(connection);
        }
    }

    /**Adds a {@link data.Disbursement} to the given group. Does not commit
     * anything to the database unless all parts of the disbursement are succesfully
     * inserted.
     *
     * @param disbursement {@link Disbursement} to add.
     * @param groupid Id of the group the disbursement belongs.
     * @return {@code true} if successful, @{code false} if not successful.
     * @throws SQLException in case of error in statements or connection.
     */
    public boolean addDisbursement(Disbursement disbursement, int groupid)throws SQLException{
//        connection = Db.instance().getConnection();
        connection.setAutoCommit(false);
        try {
            if(makeDisbursement(disbursement,groupid)){
                disbursement.setId(lastId());
                if(addParticipantsToDisbursement(disbursement)){
                    if(addItemsToDisbursement(disbursement)){
                        connection.commit();
                        return true;
                    }
                }
            }

        } finally {
            if(connection!=null){
                connection.rollback();
                connection.setAutoCommit(true);
//                connection.close();
            }
        }
        return false;
    }

    private int lastId()throws SQLException{
        try{
            ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
            rs = ps.executeQuery();
            if(!rs.next()){
                log.info("Cant get last insert ID");
            }return rs.getInt(1);
        }finally {
            Db.close(rs);
            Db.close(ps);
        }
    }

    private boolean addParticipantsToDisbursement(Disbursement disbursement) throws SQLException {
        try{
            ps = connection.prepareStatement("INSERT INTO user_disbursement " +
                    "(user_email,disp_id)" +
                    "VALUES (?,?)");
            int i=0;
            for (User u : disbursement.getParticipants()) {
                ps.setString(1,u.getEmail());
                ps.setInt(2,disbursement.getId());
                ps.addBatch();
                i++;
                if(i % 1000 == 0 || i == disbursement.getParticipants().size()){
                    int[] result =ps.executeBatch();
                    for(int r : result){
                        if(r==Statement.EXECUTE_FAILED){
                            return false;
                        }
                    }
                }
            }
            return true;
        }finally {
            Db.close(ps);
        }
    }
    private boolean addItemsToDisbursement(Disbursement disbursement) throws SQLException {
        try{
            statement = connection.createStatement();
            String sql= "UPDATE item SET shoppinglist_id=NULL ,disbursement_id="+disbursement.getId()+" WHERE ";
            int i=0;
            for (Item item : disbursement.getItems()) {
                if(i==0){
                    sql+="id="+ item.getId();
                }
                sql+=" OR id="+item.getId();
                i++;
            }
            sql+=";";
            int result=-1;
            if(i<0){
                result=statement.executeUpdate(sql);
            }
            return result==disbursement.getItems().size();
        }finally {
            Db.close(statement);
        }
    }

    private boolean makeDisbursement(Disbursement disbursement, int groupid) throws SQLException {
        try{
            ps = connection.prepareStatement("INSERT INTO disbursement " +
                    "(price, name, date, payer_id, party_id) " +
                    "VALUES (?,?,?,?,?)");
            ps.setDouble(1,disbursement.getDisbursement());
            ps.setString(2,disbursement.getName());
            ps.setTimestamp(3,new Timestamp(disbursement.getDate().getTime()));
            ps.setString(4,disbursement.getPayer().getEmail());
            ps.setInt(5,groupid);
            int result = ps.executeUpdate();
            log.info("Add user " + (result == 1?"ok":"failed"));
            return result==1;
        }finally {
            Db.close(ps);
        }
    }
}
