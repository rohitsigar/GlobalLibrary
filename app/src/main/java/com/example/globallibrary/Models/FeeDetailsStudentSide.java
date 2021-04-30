package com.example.globallibrary.Models;

public class FeeDetailsStudentSide {


    public String StudentId;
    public double Amount;
    public String DueDate;
    public String BranchId;
    public String UniquePaymentId;
    public boolean Status ;
    public FeeDetailsStudentSide(String StudentId, double Amount, String DueDate , String BranchId ,String UniquePaymentId  , boolean Status) {
        this.StudentId = StudentId;
        this.Amount = Amount;
        this.DueDate = DueDate;
        this.BranchId = BranchId;
        this.UniquePaymentId = UniquePaymentId;
        this.Status = Status;



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
}

