package com.example.mayhem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

class PlayerDetails implements Serializable
{
    String ID, name;
    int amountPaid, amountOwed;

    public PlayerDetails() {
    }

    public PlayerDetails(PlayerDetails player)
    {
        this.ID = player.getID();
        this.name = player.getName();
        this.amountOwed = player.getAmountOwed();
        this.amountPaid = player.getAmountPaid();
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

public class Payment_activity implements Serializable {
    String activityName, ID;
    Map<String, PlayerDetails> players;
    int paidOutside;
    int defaultPrice;

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

    public int getNumberofParticipants()
    {
        int participants = 0;
        if (players != null)
            participants = players.size();
        return participants;
    }

    public int getAmountPaid()
    {
        int x = 0;
        if (players == null)
            return x+paidOutside;

        for (PlayerDetails p : players.values())
        {
            x += p.getAmountPaid();
        }
        x += paidOutside;
        return x;
    }


    public int getAmountOwed()
    {
        int x = 0;
        if (players == null)
            return x;

        for (PlayerDetails p : players.values())
        {
            x += p.getAmountOwed();
        }
        return x;
    }

}
