package data;

import java.util.ArrayList;
import java.util.Date;

public class Disbursement {
    private int id;
    private double disbursement;
    private String name;
    private Date date;
    private User payer;


    private ArrayList<User> participants;
    private ArrayList<Item> items;

    public Disbursement() {

    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
