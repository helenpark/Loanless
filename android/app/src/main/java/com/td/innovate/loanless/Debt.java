package com.td.innovate.loanless;



/**
 * Created by helenpark on 2015-09-09.
 */
public class Debt {

    //credit, loan, student loan
    String debtType;
    //loan: automated monthly payments
    double minPayment;
    //loan: principal amount borrowed for loan
    double principal;
    //credit: manual monthly payments
    double minBalance;
    //credit: goods purchased
    double purchasesInterest;
    //credit: cash withdrawn
    double cashInterest;
    //credit: amount spent thus far
    double creditBalance;
    //credit: amount purchases spent thus far
    double purchasesBalance;
    //credit: amount cash spent thus far
    double cashBalance;
    //credit: limit
    double creditLimit;
    //credit: amount left to use
    double creditAvailable;



    public Debt (String type, double mp, double mb, float pi, double ci, double p, double cb, double pb, double cl) {
        debtType = type;
        minPayment = mp;
        principal = p;
        minBalance = mb;
        purchasesInterest = pi;
        cashInterest = ci;
        purchasesBalance = pb;
        creditBalance = cb+pb;
        cashBalance = cb;
        creditLimit = cl;
        creditAvailable = cl-(cb+pb);
    }
}
