package db;

import data.Chore;
import data.RepeatedChore;
import data.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static db.ChoreDao.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * -Description of the class-
 *
 * @author matseda
 * @author bk
 */

public class ChoreDaoTest {

    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static User user1 = new User("m@m.no", "Mats", "90807060", "password1","123");
    private static User user2 = new User("k@k.no", "Knut", "90805050", "password2","123");
    private static ArrayList<Chore> list1 = new ArrayList<>();
    private static ArrayList<Chore> list2 = new ArrayList<>();
    static private java.sql.Date currDate;
    static Chore c5=new Chore("desc4", currDate,101);
	static Chore c6=new Chore(204,"desc5");
	static ArrayList<String> compledetBy = new ArrayList<>();

    @BeforeClass
    public static void setUpClass() throws Exception {
        user1.setSalt("123");
        user2.setSalt("321");
        java.util.Date myDate = new java.util.Date(System.currentTimeMillis());
        currDate = new java.sql.Date(myDate.getTime());


	    compledetBy.add("Mats");
        Chore c1=new Chore(200,"desc1",compledetBy,"m@m.no", currDate, 100);
        Chore c2=new Chore(201,"desc2",compledetBy,"k@k.no", currDate,100);
        Chore c3=new Chore(202,"desc3",compledetBy,"m@m.no", currDate,100);
        Chore c4=new Chore(203,"desc4",compledetBy,"m@m.no", currDate,101);

        list1.add(c1);
        list1.add(c2);
        list1.add(c3);
        list2.add(c4);
        Connection connection = Db.instance().getConnection();
        System.out.println("Connection opened before");
        //Deleting wallpost test-data

        //Inserting test-data into database for usage in the tests

        PreparedStatement st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Mats','m@m.no','password','123','90807060')");
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO user (name,email,password,salt,phone) VALUES ('Knut','k@k.no','password','321','90805050')");
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO party (id, name) VALUES (100,'123Kollektiv')");
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO party (id,name) VALUES (101,'Kollektiv123')");
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO party (id,name) VALUES (102,'Kollektiv123')");
        st.executeUpdate();

        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id, user_email) VALUES (200,'desc1',0,?,100,'m@m.no')");
        st.setDate(1, currDate);
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id, user_email) VALUES (201,'desc2',0,?,100,'k@k.no')");
        st.setDate(1, currDate);
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id, user_email) VALUES (202,'desc3',7,?,100,'m@m.no')");
        st.setDate(1, currDate);
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id, user_email) VALUES (203,'desc4',0,?,101,'m@m.no')");
        st.setDate(1, currDate);
        st.executeUpdate();
        st = connection.prepareStatement("INSERT INTO chore (id,name,regularity,deadline,party_id, user_email) VALUES (204,'desc5',0,?,101,'m@m.no')");
        st.setDate(1, currDate);
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
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 200");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 201");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 202");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 203");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 204");
            st.executeUpdate();
            st = connection.prepareStatement("DELETE FROM chore WHERE id = 205");
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
        }

    }

    //Sjekker om vi får ut chore med forventet innhold ved metoden getChore
    @Test
    public void testGetChore() throws Exception{
        System.out.println("Testing getChore() nr 1");
        Chore chore = getChore(200);
        String result = chore.getDescription();
        String expResult = list1.get(0).getDescription();
        assertEquals(result,expResult);
    }

    //Denne testen sjekker om objektene blir opprettet i riktig klasse, Chore eller RepeatedChore
    @Test
    public void testGetChore2() throws Exception{
        System.out.println("Tester getChore() nr 2");
        Chore chore1 = getChore(202);
        Chore chore2 = getChore(200);
        boolean resultat1 = chore1 instanceof RepeatedChore;
        assertTrue(resultat1);
        boolean resultat2 = chore2 instanceof RepeatedChore;
        assertFalse(resultat2);
    }


    //Checks if you get list of chores
    @Test
    public void testGetChores() throws Exception{
        System.out.println("Tester getChores()");
        ArrayList<Chore> chores = getChores(100);
        int resultat = chores.size();
        assertTrue(resultat>0);
    }

    //First adds a new chore, then deletes it
    @Test
    public void testAddAndDeleteChore() throws Exception{
        System.out.println("Tester addChore() and deleteChore()");
        addChore(c5);
        ArrayList<Chore> chores = getChores(101);
        int sizeAfterAdd= chores.size();
        Chore last = chores.get(sizeAfterAdd-1);
        String result = last.getDescription();
        String expResult = c5.getDescription();
        System.out.println(last.getChoreId());
        deleteChore(last.getChoreId());
        assertEquals(result,expResult);

        chores = getChores(101);
        int sizeAfterDelete = chores.size();
        assertEquals(sizeAfterAdd-1,sizeAfterDelete);

    }

    //Checking if you can update which user is assigned to a chore
    @Test
    public void testAssignChore() throws Exception{
        System.out.println("Testing assignChore()");
        String email1 = getChore(200).getAssignedTo();
        assignChore("k@k.no",200);
        String email2 = getChore(200).getAssignedTo();
        assertNotEquals(email1,email2);
    }

    @Test
    public void testSetCompletedBy() throws  Exception{
        System.out.println("Testing setCompletedBy");
        ArrayList<String> brukere = getCompletedBy(200);
        ArrayList<String> brukere1 = new ArrayList<>();
        brukere1.add("m@m.no");
        brukere1.add("k@k.no");
        setCompletedBy(200,brukere1);
        ArrayList<String> brukere2 = getCompletedBy(200);
        setCompletedBy(200,brukere); //Resets completed by.
        assertEquals(brukere1.size(),brukere2.size());
        assertTrue(brukere.size()<brukere2.size());

    }
}
