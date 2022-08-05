package com.example.mayhem;

import java.util.List;
import java.util.Map;

class PlayerDetails
{
    String ID, name;
    int amountPaid, amountOwed;

    public PlayerDetails() {
    }

    public PlayerDetails(String ID, String name, int amountPaid, int amountOwed) {
        this.ID = ID;
        this.name = name;
        this.amountPaid = amountPaid;
        this.amountOwed = amountOwed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
public class Payment_activity {
    String activityName, ID;
    Map<String, PlayerDetails> players;
    int paidOutside;
    int defaultPrice;
    int participants;

    public Payment_activity() {
    }

    public Payment_activity(String activityName, int paidOutside, int defaultPrice) {
        this.activityName = activityName;
        this.paidOutside = paidOutside;
        this.defaultPrice = defaultPrice;
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

    public Map<String, PlayerDetails> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, PlayerDetails> players) {
        this.players = players;
    }

    public int getPaidOutside() {
        return paidOutside;
    }

    public void setPaidOutside(int paidOutside) {
        this.paidOutside = paidOutside;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
