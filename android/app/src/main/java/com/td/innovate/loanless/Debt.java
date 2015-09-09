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
    public String debtType;
    //loan: automated monthly payments
    public double minPayment;
    //loan: principal amount borrowed for loan
    public double principal;
    //credit: manual monthly payments
    public double minBalance;
    //credit: yes, no, partial
    public String balancePaid;
    //credit: goods purchased
    public double purchasesInterest;
    //credit: cash withdrawn
    public double cashInterest;
    //credit: amount spent thus far
    public double creditBalance;
    //credit: amount purchases spent thus far
    public double purchasesBalance;
    //credit: amount cash spent thus far
    public double cashBalance;
    //credit: limit
    public double creditLimit;
    //credit: amount left to use
    public double creditAvailable;




    public Debt (String type, double mp, double mb, String bp, double pi, double ci, double p, double cb, double pb, double cl) {
        debtType = type;
        minPayment = mp;
        principal = p;
        minBalance = mb;
        balancePaid = bp;
        purchasesInterest = pi;
        cashInterest = ci;
        purchasesBalance = pb;
        creditBalance = cb+pb;
        cashBalance = cb;
        creditLimit = cl;
        creditAvailable = cl-(cb+pb);
    }

    public Debt (JSONObject jData) {

        try
        {
            Iterator<String> keys = jData.keys();
            while( keys.hasNext() )
            {
                String key = keys.next();

                JSONObject innerJObject = jData.getJSONObject(key);
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
