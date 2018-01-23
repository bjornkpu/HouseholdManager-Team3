package util;

import data.Member;

import java.util.ArrayList;

/**
 * -Description of the class-
 *
 * @author
 * Martin Wangen
 */

public class DebtCalculator {
    public static boolean makePayment(Member payer, Member recipient, double amount){

        payer.setBalance(payer.getBalance()+amount);
        recipient.setBalance(recipient.getBalance()-amount);
        return true;
    }

    //@sulfax: Burde vært void siden du aldri returnerer noe annet en true
    public static boolean calculateReceipt(Member payer, ArrayList<Member> participants, double total){
        double subtotal = total/participants.size();
        payer.setBalance(payer.getBalance()+total);
        for(Member memb : participants){
           memb.setBalance(memb.getBalance()-subtotal);
        }
        return true;
    }
}
