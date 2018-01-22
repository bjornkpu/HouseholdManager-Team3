package data;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class UserMemberTest {
    private static String email; //id
    private static String name;
    private static String phone;
    private static String password;
    private static String salt;
    private static double balance;
    private static int status;
    private static User user;
    private static Member member;

    @BeforeClass
    public static void setUp(){
        email = "email";
        name = "name";
        phone = "90309030";
        password = "pw";
        salt = "131";
        balance = 1.1;
        status = 1;
        user=new User();
        member = new Member();
    }

    @Test
    public void settersAndGetters(){
        user.setEmail(email);
        member.setEmail(email);
        user.setName(name);
        member.setName(name);
        user.setPassword(password);
        member.setPassword(password);
        user.setPhone(phone);
        member.setPhone(phone);
        user.setSalt(salt);
        member.setSalt(salt);
        member.setBalance(balance);
        member.setStatus(status);

        assertEquals(user.getEmail(),member.getEmail());
        assertEquals(user.getName(),member.getName());
        assertEquals(user.getPassword(),member.getPassword());
        assertEquals(user.getPhone(),member.getPhone());
        assertEquals(user.getSalt(),member.getSalt());
        assertTrue(balance-member.getBalance()==0);
        assertEquals(status,member.getStatus());
        assertEquals(member.toString(),user.toString());
    }
    @Test
    public void construktorTest(){
        user = new User(email,name,phone,password,salt);
        member = new Member(email,name,phone,password,salt,balance,status);

        assertEquals(user.getEmail(),member.getEmail());
        assertEquals(user.getName(),member.getName());
        assertEquals(user.getPassword(),member.getPassword());
        assertEquals(user.getPhone(),member.getPhone());
        assertEquals(user.getSalt(),member.getSalt());
        assertTrue(balance-member.getBalance()==0);
        assertEquals(status,member.getStatus());
        assertEquals(member.toString(),user.toString());
    }

}
