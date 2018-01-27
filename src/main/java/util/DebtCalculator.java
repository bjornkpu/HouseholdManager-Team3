package util;

import data.Member;

import java.util.ArrayList;

/**
 * -Used to calculate members debt-
 *
 * @author
 * Martin Wangen
 */

public class DebtCalculator {
    private static Logger log = Logger.getLogger();

    public static boolean makePayment(Member payer, Member recipient, double amount){

        payer.setBalance(payer.getBalance()+amount);
        recipient.setBalance(recipient.getBalance()-amount);
        return true;
    }

    //@sulfax: Burde vært void siden du aldri returnerer noe annet en true
    public static boolean calculateReceipt(Member payer, ArrayList<Member> participants, double total){
        double subtotal = total/participants.size();
        log.info(payer.getEmail()+" balance from: "+payer.getBalance()+" to: "+(payer.getBalance()+total));
        payer.setBalance(payer.getBalance()+total);
        for(Member memb : participants){
            log.info(memb.getEmail()+" balance from: "+memb.getBalance()+" to: "+(memb.getBalance()-subtotal));
            memb.setBalance(memb.getBalance()-subtotal);
        }
        return true;
    }
}
