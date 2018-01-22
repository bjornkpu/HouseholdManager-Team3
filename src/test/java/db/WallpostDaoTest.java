package db;

import data.User;
import data.WallPost;
import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static db.WallpostDao.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * -Description of the class-
 *
 * @author
 * matseda
 */
public class WallpostDaoTest {
    private static Connection connection;
    private static PreparedStatement st;
    private static WallpostDao wallpostDao;


    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static User user1 = new User("m@m.no", "Mats", "90807060", "password1", "123");
    private static User user2 = new User("k@k.no", "Knut", "90805050", "password2", "123");
    private static ArrayList<WallPost> list1 = new ArrayList<>();
    private static ArrayList<WallPost> list2 = new ArrayList<>();

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = Db.instance().getConnection();
        wallpostDao = new WallpostDao(connection);

        user1.setSalt("123");
        user2.setSalt("321");
        WallPost p1 = new WallPost(timestamp, "Melding1", "m@m.no", 100);
        WallPost p2 = new WallPost(timestamp, "Melding2", "k@k.no", 100);
        WallPost p3 = new WallPost(timestamp, "Melding3", "m@m.no", 100);
        WallPost p4 = new WallPost(timestamp, "Melding4", "m@m.no", 101);
        list1.add(p1);
        list1.add(p2);
        list1.add(p3);
        list2.add(p4);

        //Inserting test-data into database for usage in the tests
        try{
            st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Mats','m@m.no','password','123','90807060')");
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Knut','k@k.no','password','321','90805050')");
            st.executeUpdate();

            st = connection.prepareStatement("INSERT INTO party (id, name) VALUES (100,'123Kollektiv')");
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO party (id,name) VALUES (101,'Kollektiv123')");
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO party (id,name) VALUES (102,'Kollektiv123')");
            st.executeUpdate();

            st = connection.prepareStatement("INSERT INTO wallpost (id,time,message,user_email,party_id) VALUES (234,?,'Melding1','m@m.no',100)");
            st.setTimestamp(1, timestamp);
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO wallpost (id,time,message,user_email,party_id) VALUES (235,?,'Melding2','k@k.no',100)");
            st.setTimestamp(1, timestamp);
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO wallpost (id,time,message,user_email,party_id) VALUES (236,?,'Melding3','m@m.no',100)");
            st.setTimestamp(1, timestamp);
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO wallpost (id,time,message,user_email,party_id) VALUES (237,?,'Melding4','m@m.no',101)");
            st.setTimestamp(1, timestamp);
            st.executeUpdate();
            st = connection.prepareStatement("INSERT INTO wallpost (id,time,message,user_email,party_id) VALUES (238,?,'Melding5','m@m.no',102)");
            st.setTimestamp(1, timestamp);
            st.executeUpdate();
        }finally {
            st.close();
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        try {
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 234");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 235");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 236");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 237");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 238");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM wallpost WHERE id = 251");
            st.executeUpdate();

            //Deleting party test-data
            st = connection.prepareStatement("DELETE FROM party WHERE id=100");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM party WHERE id=101");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM party WHERE id=102");
            st.executeUpdate();

            //Deleting user test-data
            st = connection.prepareStatement("DELETE FROM user WHERE email = 'm@m.no';");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM user WHERE email = 'k@k.no';");
            st.executeUpdate();


            st.close();
            connection.close();
            System.out.println("Connection closed after, testdata deleted");

        } catch (Exception e) {
            System.out.print(e);
        }finally {
            Db.close(connection);
        }

    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() {

    }

    //Checks if you get the same list of wallpost as expected when using the method getWallposts()
    @Test
    public void testGetWallposts1() throws Exception{
        System.out.println("Test getWallposts1()");
        ArrayList<WallPost> resultList = wallpostDao.getWallposts(100);
        String[] result = new String[resultList.size()];
        String[] expResult = new String[list1.size()];
        if(result.length==expResult.length){
            for(int i=0;i<result.length;i++){
                result[i]=resultList.get(i).getMessage();
                expResult[i]=list1.get(i).getMessage();
            }
        }
        assertArrayEquals(result,expResult);
    }

    //Checks if you get the correct list of wallposts when using the method with user_email and party_id as parameters
    @Test
    public void testGetWallposts2() throws Exception{
        System.out.println("Test getWallposts2()");
        String result = wallpostDao.getWallposts("k@k.no",100).get(0).getMessage();
        String expResult = list1.get(1).getMessage();
        assertEquals(result,expResult);
    }

    @Test
    public void testPostWallpost() throws Exception{
        System.out.println("Test postWallpost()");
        WallPost testPost = new WallPost(timestamp,"testMelding","k@k.no",101);
        wallpostDao.postWallpost(testPost);
        ArrayList<WallPost> list = wallpostDao.getWallposts(101);
        String result = list.get(1).getPostedBy();
        String expResult = testPost.getPostedBy();
        wallpostDao.deleteWallpost(list.get(1).getId());
        System.out.print(list.get(1).getId());
        assertEquals(result,expResult);
    }

    @Test
    public void testDeleteWallpost() throws Exception{
        System.out.println("Test deleteWallpost");
        wallpostDao.deleteWallpost(238);
        ArrayList<WallPost> posts = wallpostDao.getWallposts(102);
        int result = posts.size();
        int expResult = 0;
        assertEquals(result,expResult);
    }
}
