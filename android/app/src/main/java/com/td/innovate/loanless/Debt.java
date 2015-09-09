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

    String debtType;
    BigDecimal minPayment;
    BigDecimal minBalance;
    float purchasesInterest;
    float cashInterest;
    BigDecimal principal;
    BigDecimal creditBalance;
    BigDecimal purchasesBalance;
    BigDecimal cashBalance;
    BigDecimal creditLimit;
    BigDecimal creditAvailable;



    public Debt (String type, BigDecimal mp, BigDecimal mb, float pi, float ci, BigDecimal p, BigDecimal cb, BigDecimal pb, BigDecimal cl) {
        debtType = type;
        minPayment = mp;
        minBalance = mb;
        purchasesInterest = pi;
        cashInterest = ci;
        principal = p;
        purchasesBalance = pb;
        creditBalance = cb.add(pb);
        cashBalance = cb;
        creditLimit = cl;
        creditAvailable = cl.subtract(cb.add(pb));
    }

    public Debt (JSONObject jData) {

        try
        {
            Iterator<String> keys = jData.keys();
            while( keys.hasNext() )
            {
                String key = keys.next();

                JSONObject innerJObject = jData.getJSONObject(key);
                this.debtType = innerJObject.getDouble("debtType");
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

        return json;
    }
}
