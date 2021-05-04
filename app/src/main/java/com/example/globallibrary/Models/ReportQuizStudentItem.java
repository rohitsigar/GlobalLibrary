package com.example.globallibrary.Models;

public class ReportQuizStudentItem {
    public String Performance;
    public String Day;
    public String Date;
    public String Time;
    public String Difficulty;
    public String Catigory;



    public ReportQuizStudentItem() {
        // Default constructor required
    }


    public ReportQuizStudentItem(String Performance, String Catigory, String Difficulty, String Day,String Date , String Time) {

        this.Performance = Performance;
        this.Catigory = Catigory;
        this.Difficulty = Difficulty;
        this.Day  =Day;
        this.Date = Date;
        this.Time = Time;

    }


    public String getPerformance() {
        return Performance;
    }

    public String getDay() {
        return Day;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }


    public String getDifficulty() {
        return Difficulty;
    }

    public String getCatigory() {
        return Catigory;
    }
}
