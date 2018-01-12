package db;
import data.Group;
import data.User;
import org.junit.*;

import db.GroupDao;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * -Description of the class-
 *
 * @author
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class GroupDaoTest {
    private static GroupDao gr;
    private static UserDao userDao;
    private static String name1 = "Vennegjengen";
    private static String name2 = "Kollektiv";
    private static String adminnavn = "tre@h.no";
    private static String newAdmin = "to@h.no";
    private static int id1 = 1;
    private static int id2 = 2;

    @BeforeClass
    public static void setUp() throws Exception {
        gr = new GroupDao();
    }
    @Test
    public void addGroupUsingObjectTest() throws SQLException {
        Group newGroup = new Group();
        String n = "HUs!";
        boolean ok = false;
        newGroup.setAdmin(adminnavn);
        newGroup.setName(n);
        try {
            ok = gr.addParty(newGroup);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);

        try {
            int s = gr.getGroupByName(n).getId();
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Test
    public void addGroupUsingNameAndAdminIdTest() throws SQLException {
        String d = "Husveien";
        boolean ok = false;
        try {
            ok = gr.addParty(d,adminnavn);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
        try {
            int s = gr.getGroupByName(d).getId();
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Test
    public void getGroupByNameTest() throws SQLException {
        String nam = "BilJohan";
        try{
            gr.addParty(nam,adminnavn);
        } catch (SQLException e){
            e.printStackTrace();
        }
        boolean ok = false;
        Group gr1 = new Group();
        try{
            gr1 = gr.getGroupByName(nam);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(gr1.getName(),nam);
        assertEquals(gr1.getAdmin(),adminnavn);
        try {
            int s = gr.getGroupByName(nam).getId();
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Test
    public void getGroupTest() throws SQLException {
        Group group = new Group();
        try {
            group = gr.getGroup(1);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(group!=null);

    }

    @Test
    public void getAllGroupsTest() throws SQLException {
        List<Group> groupList = new ArrayList<Group>();
        try {
            groupList = gr.getAllGroups();
        } catch (SQLException e){
            e.printStackTrace();
        }
        int amountCollected = groupList.size();
        assertTrue(amountCollected > 0);
    }
    @Test
    public void updateGroupWithGroupIdAndNewName() throws SQLException {
        String nam = "BilJohan";
        String newName = "ArnT";
        int s = 0;
        try{
            gr.addParty(nam,adminnavn);
            s = gr.getGroupByName(nam).getId();
        } catch (SQLException e){
            e.printStackTrace();
        }
        boolean ok = false;
        try {
            ok = gr.updateName(s,newName);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
        try {
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    @Test
    public void updateGroupWithGroupIdAndNewAdmin() throws SQLException {
        String nam = "BilJohan";
        int s = 0;
        try{
            gr.addParty(nam,adminnavn);
            s = gr.getGroupByName(nam).getId();
        } catch (SQLException e){
            e.printStackTrace();
        }

        boolean ok = false;
        try {
            ok = gr.updateAdmin(s,newAdmin);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
        try {
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    @Test
    public void updateGroupWithObject() throws SQLException {
        String nam = "KOllek";
        int s = 0;
        try{
            gr.addParty(nam,adminnavn);
            s = gr.getGroupByName(nam).getId();
        } catch (SQLException e){
            e.printStackTrace();
        }
        Group newGroup = new Group();
        boolean ok = false;
        newGroup.setAdmin(newAdmin);
        newGroup.setName(name2);
        newGroup.setId(s);
        try {
            ok = gr.updateParty(newGroup);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
        try {
            gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Test
    public void getAnAmountOfGroupsTest() throws SQLException {
        int amount = 2;
        List<Group> groups = new ArrayList<>();
        try{
            groups = gr.getGroups(amount);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertEquals(groups.size(),amount);
    }
    @Test
    public void deleteGroupWithObjectTest() throws SQLException {
        String nam = "KOllek10v";
        int s = 0;
        try{
            gr.addParty(nam,adminnavn);
            s = gr.getGroupByName(nam).getId();
        } catch (SQLException e){
            e.printStackTrace();
        }
        boolean ok = false;
        Group group = new Group();
        group.setId(s);
        group.setName(nam);
        group.setAdmin(adminnavn);
        try{
            ok = gr.deleteParty(group);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
    }

    @Test
    public void deletePartyWithGroupIdTest() throws SQLException {
        String nam = "KOllek101v";
        int s = 0;
        try{
            gr.addParty(nam,adminnavn);
            s = gr.getGroupByName(nam).getId();
        } catch (SQLException e){
            e.printStackTrace();
        }
        boolean ok = false;
        try {
            ok = gr.deleteParty(s);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(ok);
    }
}
