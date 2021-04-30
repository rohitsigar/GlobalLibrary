package com.example.globallibrary.Models;

public class FeeDetailsBranchSide {


    public String StudentId;
    public double Amount;
    public String DueDate;
    public String BranchId;
    public String UniquePaymentId;
    public boolean Status ;
    public String StudentName;
    public String URL;
    public FeeDetailsBranchSide(String StudentId, double Amount, String DueDate , String BranchId ,String UniquePaymentId  , boolean Status , String StudentName , String URL) {
        this.StudentId = StudentId;
        this.Amount = Amount;
        this.DueDate = DueDate;
        this.BranchId = BranchId;
        this.UniquePaymentId = UniquePaymentId;
        this.Status = Status;
        this.StudentName = StudentName;
        this.URL = URL;



    }

    public String getStudentId() {
        return StudentId;
    }

    public double getAmount() {
        return Amount;
    }

    public String getDueDate() {
        return DueDate;
    }

    public String getBranchId() {
        return BranchId;
    }

    public String getUniquePaymentId() {
        return UniquePaymentId;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getStudentName() {
        return StudentName;
    }

    public String getURL() {
        return URL;
    }
}

