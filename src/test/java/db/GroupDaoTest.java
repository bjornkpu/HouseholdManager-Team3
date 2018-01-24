package db;
import data.Group;
import data.Member;
import org.junit.*;

import org.junit.runners.MethodSorters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static db.GroupDao.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * -Description of the class-
 *
 * @author
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class GroupDaoTest {
    private static Connection connection;
    private static GroupDao gr;
    private static UserDao userDao;


    private static final String name1 = "Vennegjengen";
    private static final String name2 = "Kollektiv";
    private static final String adminnavn = "tre@h.no";
    private static final String newAdmin = "to@h.no";
    private static Group newGroup = new Group();


    @BeforeClass
    public static void setUp() throws Exception {
        connection=Db.instance().getConnection();
        gr = new GroupDao(connection);
        newGroup.setName(name1);
        newGroup.setAdmin(adminnavn);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        Db.close(connection);
    }
    @Test
    public void addGroupUsingObjectTest() throws SQLException {
        int ok = 0;
        try {
            ok = gr.addGroup(newGroup);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        newGroup.setId(ok);
        assertTrue(ok!=-1&&ok!=0);
        try {
            gr.deleteGroup(ok);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void getGroupByNameTest() throws SQLException {
        String nam = "grejighruhu42hru4hho4grg";
        Group group = new Group();
        group.setName(nam);
        group.setAdmin(adminnavn);
        try{
            gr.addGroup(group);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        boolean ok = false;
        Group gr1 = new Group();
        try{
            gr1 = gr.getGroupByName(nam).get(0);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals(gr1.getName(),nam);
        try {
            int s = gr.getGroupByName(nam).get(0).getId();
            gr.deleteGroup(s);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
    }


    @Test
    public void getGroupTest() throws SQLException {
        Group group = new Group();
        try {
            group = gr.getGroup(2);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(group!=null);

    }

    @Test
    public void getAllGroupsTest() throws SQLException {
        List<Group> groupList = new ArrayList<Group>();
        try {
            groupList = gr.getAllGroups();
            List<Member> members1=groupList.get(0).getMembers();
            //System.out.println("members: "+members1.size());
            for(Member e: members1){
                System.out.println("memberemail = " + e.getEmail());
            }
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        int amountCollected = groupList.size();
        assertTrue(amountCollected > 0);
    }
    @Test
    public void updateGroupWithGroupIdAndNewName() throws SQLException {
        String nam = "fgjirjeigjreoigjiogeoir";
        String newName = "Arjeijgoireogjroehglsq";
        Group g = new Group();
        g.setName(nam);
        g.setAdmin(adminnavn);
        int s = 0;
        try{
            gr.addGroup(g);
            s = gr.getGroupByName(nam).get(0).getId();
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        boolean ok = false;
        try {
            ok = gr.updateName(s,newName);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(ok);
        try {
            gr.deleteGroup(s);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }

    }

    @Test
    public void updateGroupWithObject() throws SQLException {
        String nam = "Kgfgrei3ir";
        int s = 0;
        Group g = new Group();
        g.setName(nam);
        g.setAdmin(adminnavn);
        try{
            gr.addGroup(g);
            s = gr.getGroupByName(nam).get(0).getId();
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        Group newGroup = new Group();
        boolean ok = false;
        newGroup.setName(name2);
        newGroup.setAdmin(newAdmin);
        newGroup.setId(s);
        try {
            ok = gr.updateGroup(newGroup);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);

        }
        assertTrue(ok);
        try {
            gr.deleteGroup(s);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
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
            assertTrue(false);
        }
        assertTrue(groups.size() <= amount);
    }
    @Test
    public void deleteGroupWithObjectTest() throws SQLException {
        String nam = "KOfgeg32trigjij34";
        Group g = new Group();
        g.setName(nam);
        g.setAdmin(adminnavn);
        int s = 0;
        try{
            gr.addGroup(g);
            s = gr.getGroupByName(nam).get(0).getId();
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        boolean ok = false;
        Group group = new Group();
        group.setId(s);
        group.setName(nam);
        try{
            ok = gr.deleteGroup(group);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(ok);
    }

    @Test
    public void deletePartyWithGroupIdTest() throws SQLException {
        String nam = "Krnighu92hrhugroebgouero";
        int s = 0;
        Group g = new Group();
        g.setName(nam);
        g.setAdmin(adminnavn);
        try{
            gr.addGroup(g);
            s = gr.getGroupByName(nam).get(0).getId();
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        boolean ok = false;
        try {
            ok = gr.deleteGroup(s);
        } catch (SQLException e){
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(ok);
    }
}
