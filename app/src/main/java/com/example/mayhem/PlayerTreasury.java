package com.example.mayhem;

import java.io.Serializable;
import java.util.List;

class PaymentsDetails implements Serializable {
    String Name, ID;

    public PaymentsDetails() {
    }

    public PaymentsDetails(String name, String ID) {
        Name = name;
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
public class PlayerTreasury implements Serializable{
    String Name, ID;
    List<PaymentsDetails> paymentsForPlayer;
    int amountPaid, amountOwed;


    public PlayerTreasury() {
    }

    public PlayerTreasury(String name, String ID, int amountPaid, int amountOwed) {
        Name = name;
        this.ID = ID;
        this.amountPaid = amountPaid;
        this.amountOwed = amountOwed;
    }



    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(int amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<PaymentsDetails> getPaymentsForPlayer() {
        return paymentsForPlayer;
    }

    public void setPaymentsForPlayer(List<PaymentsDetails> paymentsForPlayer) {
        this.paymentsForPlayer = paymentsForPlayer;
    }
}
