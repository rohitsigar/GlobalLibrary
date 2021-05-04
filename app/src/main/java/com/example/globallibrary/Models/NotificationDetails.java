package com.example.globallibrary.Models;

public class NotificationDetails  {
    public String Title;
    public String Discreption;
    public String Date;
    public String Time;
    public String Day;
    public java.util.Date SortIt;



    public NotificationDetails() {
        // Default constructor required
    }


    public NotificationDetails(String Title, String Discreption, String Time, String Date, String Day , java.util.Date SortIt) {
        this.Title = Title;
        this.Discreption = Discreption;
        this.Time = Time;
        this.Date = Date;
        this.Day  =Day;
        this.SortIt = SortIt;
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

    public java.util.Date getSortIt() {
        return SortIt;
    }

//    @Override
//    public int compareTo(NotificationDetails o) {
//
//        int a =  this.SortIt.compareTo(o.SortIt);
//        if(a > 0)
//        {
//            return  0;
//        }
//        return  1;
//
//    }
}


