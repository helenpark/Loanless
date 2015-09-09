package com.td.innovate.loanless;
import java.math.BigDecimal;



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
}
