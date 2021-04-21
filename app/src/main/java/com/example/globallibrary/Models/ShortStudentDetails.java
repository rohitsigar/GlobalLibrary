package com.example.globallibrary.Models;

public class ShortStudentDetails {
    public String StudentName;
    public String Discreption;
    public String PhootoURL;
    public String PhoneNo;
    public String Color;
    public ShortStudentDetails() {
        // Default constructor required
    }


    public ShortStudentDetails(String StudentName, String Discreption, String PHotoURL , String PhoneNo , String Color) {
        this.StudentName = StudentName;
        this.Discreption = Discreption;
        this.PhootoURL = PHotoURL;
        this.PhoneNo = PhoneNo;
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

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getColor() {
        return Color;
    }
}
