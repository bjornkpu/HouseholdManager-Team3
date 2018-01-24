package data;

public class Payment {
    private int id;
    private String payer;
    private String payerName="Jan";
    private String receiver;
    private double amount;
    private int party;
    private boolean paid=false;

    public Payment(){}
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

    public void setId(int id) {
        this.id = id;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setParty(int party) {
        this.party = party;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
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

