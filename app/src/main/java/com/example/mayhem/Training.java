package com.example.mayhem;

import java.io.Serializable;
import java.util.List;

public class Training implements Serializable {
    String ID, Date;
    int Day, Month, Year;
    List<String> playersAttended, playersMissed;

    public Training() {
    }

    public Training(String date, List<String> playersAttended, List<String> playersMissed) {
        Date = date;
        this.playersAttended = playersAttended;
        this.playersMissed = playersMissed;
    }

    public Training(String ID, String date, int day, int month, int year, List<String> playersAttended, List<String> playersMissed) {
        this.ID = ID;
        Date = date;
        Day = day;
        Month = month;
        Year = year;
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

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
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
