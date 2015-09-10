package com.td.innovate.loanless;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.Iterator;


/**
 * Created by helenpark on 2015-09-09.
 */
public class Debt {

    //debt Identifier
    public String title = "";
    //debt status: Good, Late, Paid Min, Not Enough Funds
    public String status = "";
    //credit, loan, student loan
    public String debtType = "";
    //loan: automated monthly payments
    public double minPayment;
    //loan: amount left to pay
    public double loanBalance;
    //loan: principal amount borrowed for loan
    public double principal;
    //loan: interest on loan
    public double loanInterest;
    //credit: manual monthly payments
    public double minBalance;
    //credit: goods purchased
    public double purchasesInterest;
    //credit: cash withdrawn
    public double cashInterest;
    //credit: amount spent thus far, not paid back
    public double creditBalance;
    //credit: amount purchases spent thus far
    public double purchasesBalance;
    //credit: amount cash spent thus far
    public double cashBalance;
    //credit: limit
    public double creditLimit;
    //credit: amount left to use
    public double creditAvailable;


    public Debt (String t, String s, String type, double mp, double lb, double mb, double pi, double ci, double p, double li, double cb, double pb, double cl) {
        title = t;
        status = s;
        debtType = type;
        minPayment = mp;
        loanBalance = lb;
        principal = p;
        loanInterest = li;
        minBalance = mb;
        purchasesInterest = pi;
        cashInterest = ci;
        purchasesBalance = pb;
        creditBalance = cb+pb;
        cashBalance = cb;
        creditLimit = cl;
        creditAvailable = cl-creditBalance;
    }



    public Debt (JSONObject innerJObject) {

        try {
            this.title = innerJObject.getString("title");
            this.status = innerJObject.getString("status");
            this.debtType = innerJObject.getString("debtType");
            this.minPayment = innerJObject.getDouble("minPayment");
            this.loanBalance = innerJObject.getDouble("loanBalance");
            this.minBalance = innerJObject.getDouble("minBalance");
            this.purchasesInterest = innerJObject.getDouble("purchasesInterest");
            this.cashInterest = innerJObject.getDouble("cashInterest");
            this.principal = innerJObject.getDouble("principal");
            this.loanInterest = innerJObject.getDouble("loanInterest");
            this.creditBalance = innerJObject.getDouble("creditBalance");
            this.purchasesBalance = innerJObject.getDouble("purchasesBalance");
            this.cashBalance = innerJObject.getDouble("cashBalance");
            this.creditLimit = innerJObject.getDouble("creditLimit");
            this.creditAvailable = innerJObject.getDouble("creditAvailable");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("title", title);
            json.put("status", status);
            json.put("debtType", debtType);
            json.put("minPayment", minPayment);
            json.put("loanBalance", loanBalance);
            json.put("minBalance", minBalance);
            json.put("purchasesInterest", purchasesInterest);
            json.put("cashInterest", cashInterest);
            json.put("principal", principal);
            json.put("loanInterest", loanInterest);
            json.put("creditBalance", creditBalance);
            json.put("purchasesBalance", purchasesBalance);
            json.put("cashBalance", cashBalance);
            json.put("purchasesInterest", purchasesInterest);
            json.put("creditLimit", creditLimit);
            json.put("creditAvailable", creditAvailable);
            return json.toString();
        }
        catch(JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
