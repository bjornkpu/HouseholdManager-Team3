package db;

import data.StatisticsHelp;
import data.User;
import data.WallPost;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class StatisticsTest {

    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static User user1 = new User("m@m.no", "Mats", "90807060", "password1", "123");
    private static User user2 = new User("k@k.no", "Knut", "90805050", "password2", "123");
    private static ArrayList<WallPost> list1 = new ArrayList<>();
    private static ArrayList<WallPost> list2 = new ArrayList<>();
    private StatisticsDao statisticsDao = new StatisticsDao();

    @BeforeClass
    public static void setUpClass() throws Exception {
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
        Connection connection = Db.instance().getConnection();
        System.out.println("Connection opened before");

        //Inserting test-data into database for usage in the tests

        PreparedStatement st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Mats','m@m.no','password','123','90807060')");
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Knut','k@k.no','password','321','90805050')");
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO party (id, name) VALUES (100,'123Kollektiv')");
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id) VALUES (200,'chore 1',0,?,100)");
        st.setTimestamp(1, timestamp);
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO chore_log(user_email, chore_id,done) VALUES ('m@m.no',200,?)");
        st.setTimestamp(1,timestamp);
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO chore_log(user_email, chore_id,done) VALUES ('k@k.no',200,?)");
        st.setTimestamp(1,timestamp);
        st.executeUpdate();
        st.close();
        connection.close();
        System.out.println("Connection closed before, testdata inserted");

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        try {
            Connection connection = Db.instance().getConnection();
            System.out.println("Connection opened after");

            //Deleting wallpost test-data
            PreparedStatement st = connection.prepareStatement("DELETE FROM chore_log WHERE user_email='m@m.no'");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore_log WHERE user_email='k@k.no'");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id=200");
            st.executeUpdate();

            //Deleting party test-data
            st = connection.prepareStatement("DELETE FROM party WHERE id=100");
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
        }

    }

    @Test
    public void testGetChoresPerUser() throws Exception{
        ArrayList<StatisticsHelp> map = statisticsDao.getChoresPerUser(100);
        int result =0;
        for (StatisticsHelp aMap : map) {
            if (aMap.getKey().equals("m@m.no")) {
                result = aMap.getValue();
                break;
            }
        }
        assertEquals(1,result);
    }
}
