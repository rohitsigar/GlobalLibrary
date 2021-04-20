package com.example.globallibrary.Models;

public class ShortStudentDetails {
    public String StudentName;
    public String Discreption;
    public String PhootoURL;
    public ShortStudentDetails() {
        // Default constructor required
    }


    public ShortStudentDetails(String StudentName, String Discreption, String PHotoURL) {
        this.StudentName = StudentName;
        this.Discreption = Discreption;
        this.PhootoURL = PHotoURL;

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
}
