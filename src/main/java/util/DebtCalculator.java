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


    /**
     * Metod to recalculate two members balances after a payment.
     * @param payer
     * @param recipient
     * @param amount
     */
    public static void makePayment(Member payer, Member recipient, double amount){

        payer.setBalance(payer.getBalance()+amount);
        recipient.setBalance(recipient.getBalance()-amount);
    }

    /**
     * Method to recalculate balances after a disbursement is done.
     * @param payer The one who payd
     * @param participants the ones sharing the expence.
     * @param total total amount. Not defined currency.
     */
    public static void calculateReceipt(Member payer, ArrayList<Member> participants, double total){
        double subtotal = total/participants.size();
        log.info(payer.getEmail()+" balance from: "+payer.getBalance()+" to: "+(payer.getBalance()+total));
        payer.setBalance(payer.getBalance()+total);
        for(Member memb : participants){
            log.info(memb.getEmail()+" balance from: "+memb.getBalance()+" to: "+(memb.getBalance()-subtotal));
            memb.setBalance(memb.getBalance()-subtotal);
        }
    }
}
