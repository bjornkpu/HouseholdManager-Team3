package util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EmailSenderTest {

    @Test
    public void geneateNewPasswordTest(){
        assertTrue(EmailSender.generateNewPassword("sulfax@hotmail.com")!=null);
    }

}
