package util;
import data.Member;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class DebtCalculatorTest {

    @Test
    public void testMakePayment() throws Exception {
        Member payer = new Member();
        Member recipient = new Member();
        double sum = 100;
        payer.setBalance(-100);
        recipient.setBalance(100);

        DebtCalculator.makePayment(payer,recipient,sum);
        assertTrue(recipient.getBalance()==0);
    }

    @Test
    public void testCalculateReceipt() throws Exception{
        ArrayList<Member> list = new ArrayList<>();
        for(int i = 0; i<5;i++){
            list.add(new Member());
            list.get(i).setBalance(100);
        }
        DebtCalculator.calculateReceipt(list.get(0),list, 500);
        assertTrue(list.get(0).getBalance() == 500 && list.get(1).getBalance() ==0 );

    }
}