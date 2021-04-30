package com.example.globallibrary.Models;

public class ReportQuizBranchItem {
    public String StudentName;
    public String Performance;
    public String Day;
    public String Date;
    public String Time;
    public String PhotoURL;
    public String Difficulty;
    public String Catigory;



    public ReportQuizBranchItem() {
        // Default constructor required
    }


    public ReportQuizBranchItem(String StudentName, String Performance, String Catigory, String Difficulty, String Day,String Date , String Time , String PhotoURl) {
        this.StudentName = StudentName;
        this.Performance = Performance;
        this.Catigory = Catigory;
        this.Difficulty = Difficulty;
        this.Day  =Day;
        this.Date = Date;
        this.Time = Time;
        this.PhotoURL = PhotoURl;
    }

    public String getStudentName() {
        return StudentName;
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

    public String getPhotoURL() {
        return PhotoURL;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public String getCatigory() {
        return Catigory;
    }
}
