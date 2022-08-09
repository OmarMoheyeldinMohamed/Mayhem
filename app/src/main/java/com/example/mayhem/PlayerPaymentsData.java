package com.example.mayhem;

import java.io.Serializable;
import java.util.Map;

public class PlayerPaymentsData implements Serializable {
    String activityName, ID;
    int amountPaid, amountOwed;

    public PlayerPaymentsData() {
    }

    public PlayerPaymentsData(String activityName, String ID, int amountPaid, int amountOwed) {
        this.activityName = activityName;
        this.ID = ID;
        this.amountPaid = amountPaid;
        this.amountOwed = amountOwed;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
}
