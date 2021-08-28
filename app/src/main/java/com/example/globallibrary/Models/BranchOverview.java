package com.example.globallibrary.Models;

public class BranchOverview {
    public String BranchName;
    public String Discreption;
    public String Address;
    public String EmailAddress;
    public String ContactNumber;
    public String PhotoURL;
    public  String Fees;


    public BranchOverview() {
        // Default constructor required
    }


    public BranchOverview(String BranchName, String Discreption, String Address, String ContactNumber, String PhotoURL,String EmailAddress , String Fees) {
        this.BranchName = BranchName;
        this.Discreption = Discreption;
        this.Address = Address;
        this.ContactNumber = ContactNumber;
        this.PhotoURL  =PhotoURL;
        this.EmailAddress = EmailAddress;
        this.Fees = Fees;
    }

    public String getBranchName() {
        return this.BranchName;
    }

    public String getDiscreption() {
        return this.Discreption;
    }

    public String getAddress() {
        return this.Address;
    }

    public String getEmailAddress() {
        return this.EmailAddress;
    }

    public String getContactNumber() {
        return this.ContactNumber;
    }

    public String getPhotoURL() {
        return this.PhotoURL;
    }

    public String getFees() {
        return Fees;
    }

    public void setFees(String fees) {
        Fees = fees;
    }
}
