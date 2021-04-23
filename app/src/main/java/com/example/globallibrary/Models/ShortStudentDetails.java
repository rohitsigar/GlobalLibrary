package com.example.globallibrary.Models;

public class ShortStudentDetails {
    public String StudentName;
    public String Discreption;
    public String PhootoURL;
    public String StudentId;
    public String Color;
    public ShortStudentDetails() {
        // Default constructor required
    }


    public ShortStudentDetails(String StudentName, String Discreption, String PHotoURL , String StudentId , String Color) {
        this.StudentName = StudentName;
        this.Discreption = Discreption;
        this.PhootoURL = PHotoURL;
        this.StudentId = StudentId;
        this.Color = Color;

    }

    public String getStudentName() {
        return StudentName;
    }

    public String getDiscreption() {
        return Discreption;
    }

    public String getPhootoURL() {
        return PhootoURL;
    }

    public String getStudentId() {
        return StudentId;
    }

    public String getColor() {
        return Color;
    }
}
