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


    public Debt (String type, double mp, double mb, double pi, double ci, double p, double cb, double pb, double cl) {
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

    public Debt (JSONObject innerJObject) {

        try
        {
            this.debtType = innerJObject.getString("debtType");
            this.minPayment = innerJObject.getDouble("minPayment");
            this.minBalance = innerJObject.getDouble("minBalance");
            this.purchasesInterest = innerJObject.getDouble("purchasesInterest");
            this.cashInterest = innerJObject.getDouble("cashInterest");
            this.principal = innerJObject.getDouble("principal");
            this.creditBalance = innerJObject.getDouble("creditBalance");
            this.purchasesBalance = innerJObject.getDouble("purchasesBalance");
            this.cashBalance = innerJObject.getDouble("cashBalance");
            this.purchasesInterest = innerJObject.getDouble("purchasesInterest");
            this.creditLimit = innerJObject.getDouble("creditLimit");
            this.creditAvailable = innerJObject.getDouble("creditAvailable");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("debtType", debtType);
            json.put("minPayment", minPayment);
            json.put("minBalance", minBalance);
            json.put("purchasesInterest", purchasesInterest);
            json.put("cashInterest", cashInterest);
            json.put("principal", principal);
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
