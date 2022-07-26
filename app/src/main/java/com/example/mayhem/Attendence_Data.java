package com.example.mayhem;


public class Attendence_Data {
    String Name;
    String Attended;
    String Missed;


    public Attendence_Data(String name, String attended, String missed) {
        this.Name = name;
        Attended = attended;
        Missed = missed;
    }

    public String getName() {
        return Name;
    }

    public String getAttended() {
        return Attended;
    }

    public String getMissed() {
        return Missed;
    }
}
