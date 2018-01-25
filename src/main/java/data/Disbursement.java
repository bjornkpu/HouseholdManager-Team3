package data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Disbursement {
    private int id;
    private double disbursement;
    private String name;
    private Timestamp date;
    private int accepted;
    private User payer;
    private ArrayList<User> participants;
    private ArrayList<Item> items;

    public Disbursement(){}

    public Disbursement(double disbursement, String name, Timestamp date, User payer) {
        this.disbursement = disbursement;
        this.name = name;
        this.date = date;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public double getDisbursement() {
        return disbursement;
    }

    public void setDisbursement(double disbursement) {
        this.disbursement = disbursement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**Returns a copy, with the same participants and items arraylist reference
     *
     * @return
     */
    public Disbursement copy(){
        Disbursement d = new Disbursement();
        d.setId(this.id);
        d.setDisbursement(this.disbursement);
        d.setName(this.name);
        d.setDate(this.date);
        d.setAccepted(this.accepted);
        d.setPayer(this.payer);
        d.setParticipants(this.participants);
        d.setItems(this.items);
        return d;
    }
}
