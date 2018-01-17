package util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ForgottenPasswordTest {

    @Test
    public void geneateNewPasswordTest(){
        assertTrue(ForgottenPassword.generateNewPassword("sulfax@hotmail.com")!=null);
    }

}
