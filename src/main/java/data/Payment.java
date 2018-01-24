package data;

public class Payment {
    private int id;
    private String payer;
    private String payerName="Jan";
    private String receiver;
    private double amount;
    private int party;
    private boolean paid=false;

    public Payment(int id, String payer, String receiver, double amount, int party, String payerName){
        this.id=id;
        this.payer=payer;
        this.receiver=receiver;
        this.amount=amount;
        this.party=party;
        this.payerName=payerName;
    }

    public Payment(String payer, String receiver, double amount, int party){
        this.payer=payer;
        this.receiver=receiver;
        this.amount=amount;
        this.party=party;
    }

    public double getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public int getParty() {
        return party;
    }

    public String getPayer() {
        return payer;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean getPaid(){
        return paid;
    }

    public void setPaid(){
        paid=true;
    }

    public String getPayerName() {
        return payerName;
    }
}

