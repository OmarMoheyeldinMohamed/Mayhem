package com.example.mayhem;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    String Name, ID;
    List<String> attendedTrainings, missedTrainings;

    public Player() {
    }

    public Player(String name, List<String> attendedTrainings, List<String> missedTrainings) {
        Name = name;
        this.attendedTrainings = attendedTrainings;
        this.missedTrainings = missedTrainings;
    }


    public Player(String name, String ID, List<String> attendedTrainings, List<String> missedTrainings) {
        Name = name;
        this.ID = ID;
        this.attendedTrainings = attendedTrainings;
        this.missedTrainings = missedTrainings;
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

    public List<String> getAttendedTrainings() {
        return attendedTrainings;
    }

    public void setAttendedTrainings(List<String> attendedTrainings) {
        this.attendedTrainings = attendedTrainings;
    }

    public List<String> getMissedTrainings() {
        return missedTrainings;
    }

    public void setMissedTrainings(List<String> missedTrainings) {
        this.missedTrainings = missedTrainings;
    }
}
