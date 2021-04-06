package com.example.globallibrary.Models;

public class NotificationDetails {
    public String Title;
    public String Discreption;
    public String Date;
    public String Time;
    public String Day;



    public NotificationDetails() {
        // Default constructor required
    }


    public NotificationDetails(String Title, String Discreption, String Time, String Date, String Day) {
        this.Title = Title;
        this.Discreption = Discreption;
        this.Time = Time;
        this.Date = Date;
        this.Day  =Day;
    }

    public String getTitle() {
        return Title;
    }

    public String getDiscreption() {
        return Discreption;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getDay() {
        return Day;
    }
}


