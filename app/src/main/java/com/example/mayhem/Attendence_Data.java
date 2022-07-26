package com.example.mayhem;


public class Attendence_Data {
    String Name;
    String Attended;
    String Missed;
    int Day, Month, Year;


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
}
