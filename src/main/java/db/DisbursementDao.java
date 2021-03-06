package db;

import data.Disbursement;
import data.Item;
import data.Member;
import data.User;
import util.DebtCalculator;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data access object for Disbursements
 *
 * @author johanmsk
 * @author Martin Wangen
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

    /**
     * Helpmethod to find an index.
     * @param users Arraylist of users
     * @param email of the one we're looking for
     * @return index
     */
    private int getBuyerIndex(ArrayList<User> users, String email){
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
        UserDao userDao = new UserDao(connection);
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud " +
                    "ON d.id = ud.disp_id WHERE d.party_id=? AND ud.user_email=?");
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
                    disbursement.setAccepted(rs.getInt("accepted"));
                    log.info("Accepted int:"+disbursement.getAccepted());
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    disbursement.setPayer(new User(rs.getString("payer_id")));
                    disbursement.setParticipants(userDao.getUsersInDisbursement(disbursement.getId()));
                    int buyerIndex = getBuyerIndex(disbursement.getParticipants(),disbursement.getPayer().getEmail());
                    if(buyerIndex>=0){
                        disbursement.setPayer(disbursement.getParticipants().get(buyerIndex));
                    }
                    disbursements.add(disbursement);
                }while(rs.next());
            } else {
                log.info("Could not find any disbursement in group: "+groupId+" user: " + email);
            }
            return disbursements;
        } finally {
            Db.close(rs);
            Db.close(ps);
        }
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
        UserDao userDao= new UserDao(connection);
        ItemDao itemDao= new ItemDao(connection);
        try {
            ps = connection.prepareStatement("SELECT * FROM disbursement d JOIN user_disbursement ud " +
                    "ON d.id = ud.disp_id WHERE d.id=? AND ud.user_email=?");
            ps.setInt(1,disbursementId);
            ps.setString(2,email);
            rs = ps.executeQuery();

            Disbursement disbursement = null;
            if(rs.first()){
                do{
                    log.info("Found disbursement " + disbursementId);
                    disbursement = new Disbursement();
                    disbursement.setId(rs.getInt("id"));
                    disbursement.setDisbursement(rs.getDouble("price"));
                    disbursement.setName(rs.getString("name"));
                    disbursement.setDate(rs.getTimestamp("date"));
                    disbursement.setPayer(new User(rs.getString("payer_id")));
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
        }
    }

    /**Deletes a {@link Disbursement}
     *
     * @param disbursement the Disbursement to delete
     * @return {@code true} if deletion is successful, {@code false} if unsuccessful
     * @throws SQLException with a description of where it happened and origin exception.
     */
    public boolean deleteDisbursement(Disbursement disbursement) throws SQLException{
        boolean oldCommit=true;
        if(connection.getAutoCommit()){
            connection.setAutoCommit(false);
        }else{
            oldCommit=false;
        }
        int rs;
        try{
            //Set foreign key in items to null
            try{
                ps=connection.prepareStatement("UPDATE item SET disbursement_id=NULL, status=2 WHERE disbursement_id=?");
                ps.setInt(1,disbursement.getId());
                rs=ps.executeUpdate();
                if(rs!=disbursement.getItems().size()){
                    log.info("Could not find all the items in the disbursement");
                    return false;
                }else {
                    log.info("Found '"+rs+"' items in disbursement: "+disbursement.getId());
                }
            }catch(SQLException e){
                throw new SQLException("Error on deleteDisbursement, remove item.disbursement_id",e);
            }
            finally {
                Db.close(ps);
            }
            //Delete disbursement
            try{
                ps = connection.prepareStatement("DELETE FROM disbursement WHERE id=?");
                ps.setInt(1,disbursement.getId());
                rs=ps.executeUpdate();
                if(rs==1){
                    if(oldCommit){
                        log.info("Comitting disbursement deletion");
                        connection.commit();
                    }else{
                        log.info("Old autocommit was false, leaving commit up to parent");
                    }return true;
                }else{
                    log.info("Deleted '"+rs+"' disbursement with id: "+disbursement.getId());
                }

            }catch (SQLException e) {
                throw new SQLException("Error on deleteDisbursement, delete disbursement",e);
            }finally {
                Db.close(ps);
            }
        }finally {
            if(connection!=null){
                if(oldCommit){
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            }
        }
        log.info("Should not reach, deleteDisbursement last return");
        return false;
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
        boolean oldCommit=true;
        if(connection.getAutoCommit()){
            connection.setAutoCommit(false);
        }else{
            oldCommit=false;
        }
        try {
            if(makeDisbursement(disbursement,groupid)){
                disbursement.setId(lastId());
                if(addParticipantsToDisbursement(disbursement)){
                    if(addItemsToDisbursement(disbursement)){
                        if(oldCommit){
                            log.info("Comitting disbursement");
                            connection.commit();
                        }else{
                            log.info("Old autocommit was false, leaving commit up to parent");
                        }
                        return true;
                    }
                }
            }

        } finally {
            if(connection!=null){
                if(oldCommit){
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            }
        }
        return false;
    }

    /**
     * Returns the last ID autoincremented in the DB during this connection.
     * @return the ID
     * @throws SQLException
     */
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

    /**
     * Method to update the balances in the DB
     * @param disbursement Contains the information about who participated and the sum
     * @param groupid which group to update in
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    public boolean updateBalances(Disbursement disbursement, int groupid) throws SQLException {
        boolean returnResult = true;
        ArrayList<Member> members = new ArrayList<>();
        Member payer = null;
        try {
            //get the participants old balance and lock the table rows.

            //Make the sql for selecting all participants
            String sql = "SELECT user_email, balance FROM user_party WHERE (user_email=?";
            for (int i = 1; i < disbursement.getParticipants().size(); i++) {
                sql += " OR user_email=?";
            }
            sql += ") AND party_id=? FOR UPDATE";
//            log.info("SQL for getting old balances:\n"+sql);
            ps = connection.prepareStatement(sql);

            //Input the emails into the preparedstatement
            int i;
            for (i = 1; i <= disbursement.getParticipants().size(); i++) {
                User u = disbursement.getParticipants().get(i - 1);
                ps.setString(i, u.getEmail());
            }
            ps.setInt(i, groupid);

            //Execute and add the participants into an arraylist
            rs = ps.executeQuery();
            if (rs.next()) {
                do {
                    Member m = new Member();
                    m.setEmail(rs.getString("user_email"));
                    m.setBalance(rs.getDouble("balance"));
                    members.add(m);
                    if (disbursement.getPayer().getEmail().equals(m.getEmail())) {
                        log.info("payer: " + m.getEmail());
                        payer = m;
                    }
                } while (rs.next());
            }
            Db.close(rs);
            Db.close(ps);

            //Make sure we have the payer
            if (payer == null) {
                sql = "SELECT user_email, balance FROM user_party WHERE user_email=? AND party_id=? FOR UPDATE";
                ps = connection.prepareStatement(sql);
                ps.setString(1, disbursement.getPayer().getEmail());
                ps.setInt(2, groupid);
                rs = ps.executeQuery();
                if (rs.next()) {
                    payer = new Member();
                    payer.setEmail(rs.getString("user_email"));
                    payer.setBalance(rs.getDouble("balance"));
                }
                Db.close(rs);
                Db.close(ps);
            }

            //Give them new balances
            if (payer != null) {
                DebtCalculator.calculateReceipt(payer, members, disbursement.getDisbursement());
            } else {
                log.error("Payer is null, should not reach this", new NullPointerException());
            }

            //Add payer to members. We're done with updating balances, no longer need to keep track of who is paying.
            if (!members.contains(payer)) {
                members.add(payer);
            }

            ps = connection.prepareStatement("UPDATE user_party SET balance = ? " +
                    "WHERE user_email=? AND party_id = ?");
            i = 0;
            for (Member m : members) {
                ps.setDouble(1, m.getBalance());
                ps.setString(2, m.getEmail());
                ps.setInt(3, groupid);
                ps.addBatch();
                if (i % 1000 == 0 || i == members.size()) {
                    int[] result = ps.executeBatch();
                    for (int r : result) {
                        if (r == Statement.EXECUTE_FAILED) {
                            returnResult = false;
                        }
                    }
                }
            }
            log.info("Updated balance " + (returnResult ? "ok" : "failed"));
            return returnResult;
        }catch(SQLException e){
            throw new SQLException("Error in updatebalances",e);
        }finally {
            Db.close(rs);
            Db.close(ps);

        }
    }

    /**
     * Adds participants to a disbursement in the DB
     * @param disbursement contains the participants
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    private boolean addParticipantsToDisbursement(Disbursement disbursement) throws SQLException {
        boolean returnResult = true;
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
                            returnResult= false;
                        }
                    }
                }
            }
            log.info("Add users to disbursement " + (returnResult?"ok":"failed"));
            return returnResult;
        }catch(SQLException e){
            throw new SQLException("Error in addParticipantsToDisbursement",e);
        }finally {
            Db.close(ps);
        }
    }

    /**
     * Adds participants to a disbursement in the DB
     * @param disbursement contains the items
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    private boolean addItemsToDisbursement(Disbursement disbursement) throws SQLException {
        try{
            statement = connection.createStatement();
            String sql= "UPDATE item SET status="+Item.PURCHASED+" ,disbursement_id="+disbursement.getId()+" WHERE ";
            int i=0;
            System.out.println("items.len: "+disbursement.getItems().size());
            for (Item item : disbursement.getItems()) {
                if(i==0){
                    sql+="id="+ item.getId();
                }else{
                    sql+=" OR id="+item.getId();
                }
                i++;
            }
            sql+=";";
            log.info("additemsSQL: "+sql);
            int result=-1;
            if(i>0){
                result=statement.executeUpdate(sql);
            }
            log.info("Add item to disbursement " +
                    (result == disbursement.getItems().size()?"ok":"failed"));
            return result==disbursement.getItems().size();
        }catch(SQLException e){
            throw new SQLException("Error in addItemsToDisbursement",e);
        }finally {
            Db.close(statement);
        }
    }

    /**
     * Help method to addDisbursement
     * @param disbursement the disbursement
     * @param groupid to add the disbursement to
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    private boolean makeDisbursement(Disbursement disbursement, int groupid) throws SQLException {
        try{
            ps = connection.prepareStatement("INSERT INTO disbursement " +
                    "(price, name, date, payer_id, party_id) " +
                    "VALUES (?,?,?,?,?)");
            ps.setDouble(1,disbursement.getDisbursement());
            ps.setString(2,disbursement.getName());
            try{
                ps.setTimestamp(3,new Timestamp(disbursement.getDate().getTime()));
            }catch (Exception e){
                ps.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            }
            ps.setString(4,disbursement.getPayer().getEmail());
            ps.setInt(5,groupid);
            int result = ps.executeUpdate();
            log.info("Add disbursement " + (result == 1?"ok":"failed"));
            return result==1;
        }catch(SQLException e){
            throw new SQLException("Error in makeDisbursement",e);
        }finally {
            Db.close(ps);
        }
    }

    /**
     * Method that registres the response of the participants
     * @param disbursement contains info of the disbursement
     * @param groupId wich group it belongs to
     * @param email the responder
     * @param response the response. 1 is accepted and 2 is declined
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    public boolean respondToDisbursement(Disbursement disbursement,int groupId ,String email, int response) throws SQLException{
        try{
            disbursement = getDisbursementDetails(disbursement.getId(),email);
            if(response==2){
                deleteDisbursement(disbursement);
                return true;
            }
            ps = connection.prepareStatement("UPDATE user_disbursement SET accepted=? WHERE user_email=? AND disp_id = ?");
            ps.setInt(1,response);
            ps.setString(2,email);
            ps.setInt(3,disbursement.getId());
            int result = ps.executeUpdate();
            if(result==1&&checkResponses(disbursement,groupId)){
                updateBalances(disbursement,groupId);
            }
            return result==1;
        }finally {
            Db.close(rs);
            Db.close(ps);
//            connection.close();
        }
    }

    /**
     * Heling method to check if all have accepted the disbursement.
     * @param disbursement wich disbursement
     * @param groupId what group
     * @return boolean to tell if the transaction was successful.
     * @throws SQLException
     */
    public boolean checkResponses(Disbursement disbursement, int groupId) throws SQLException {
        try {
            ps = connection.prepareStatement("SELECT accepted FROM user_disbursement WHERE disp_id=?");
            ps.setInt(1, disbursement.getId());
            res = ps.executeQuery();
            while (res.next()) {
                int response = res.getInt("accepted");
                if (response == 0) return false;
            }
            return true;
        } finally {
            Db.close(res);
            Db.close(ps);
//            Db.close(connection);
        }
    }
}
