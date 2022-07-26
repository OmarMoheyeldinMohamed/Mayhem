package com.example.mayhem;

import java.io.Serializable;
import java.util.List;

public class Practice implements Serializable {
    String ID, Date;
    List<String> playersAttended, playersMissed;

    public Practice() {
    }

    public Practice(String ID, String date, List<String> playersAttended, List<String> playersMissed) {
        this.ID = ID;
        Date = date;
        this.playersAttended = playersAttended;
        this.playersMissed = playersMissed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public List<String> getPlayersAttended() {
        return playersAttended;
    }

    public void setPlayersAttended(List<String> playersAttended) {
        this.playersAttended = playersAttended;
    }

    public List<String> getPlayersMissed() {
        return playersMissed;
    }

    public void setPlayersMissed(List<String> playersMissed) {
        this.playersMissed = playersMissed;
    }
}
