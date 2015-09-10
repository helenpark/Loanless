package com.td.innovate.loanless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //TODO: remove all log.d statements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debt d1 = new Debt("cc1","Good","credit",0.0,0.0,0.0,0.0,10.0,0.1,0.2,50.0,50.0,1000.0);
        Debt d2 = new Debt("cc2","Good","credit",0.0,0.0,0.0,0.0,11.0,0.1,0.2,60.0,60.0,1000.0);
        Debt d3 = new Debt("cc3","Good","credit",0.0,0.0,0.0,0.0,12.0,0.1,0.2,70.0,70.0,1000.0);
        Debt d4 = new Debt("cc4","Good","credit",0.0,0.0,0.0,0.0,13.0,0.1,0.2,80.0,80.0,1000.0);

        ArrayList<Debt> test = new ArrayList<Debt>();
        test.add(d1);
        test.add(d2);
        test.add(d3);
        test.add(d4);
        smartPay(25, test);


    }

    //SmartPay Algorithm determines best payment strategy

    //calculates total min monthly balance for credit debts
    public double basicPayment (double... payments) {
        double total = 0;
        for (double payment:payments) {
            total = total + payment;
        }
        return total;
    }

    //calculates interest on remaining credit balance
    public double interestCalculator (Debt debt) {
        if (debt.debtType.equals("loan")) {
            return debt.loanInterest*debt.loanBalance;
        } else {
            double purchaseDebt = debt.purchasesBalance * debt.purchasesInterest;
            double cashDebt = debt.cashBalance * debt.cashInterest;
            return purchaseDebt + cashDebt;
        }
    }

    //determines credit debt with highest non-pay cost
    public Debt interestCompare (ArrayList<Debt> debts) {
        double highestDebt = interestCalculator(debts.get(0));
        Debt best = debts.get(0);
        for (Debt debt: debts) {
            double currDept = interestCalculator(debt);
            if (currDept>highestDebt) {
                best = debt;
                highestDebt = currDept;
            }
        }
        return best;
    }

    //adds the specified funds to credit debt
    //debt must be a credit
    public void addFunds(Debt d, double funds, boolean excessPay) {
        d.creditBalance = d.creditBalance-funds;
        d.creditAvailable = d.creditAvailable+funds;
        if (excessPay) {
            d.cashBalance = d.cashBalance - funds;
        } else {
            d.status = "Paid Min";
            d.purchasesBalance = d.purchasesBalance-(d.minBalance*(d.purchasesBalance/d.creditBalance));
            d.cashBalance = d.cashBalance-(d.minBalance*(d.cashBalance/d.creditBalance));
            d.creditBalance = d.cashBalance+d.purchasesBalance;
            d.creditAvailable = d.creditLimit-d.creditBalance;
            d.minBalance = 0.0;
        }
    }

    //builds a ArrayList of debts, sorted from lowest min balance to highest
    public ArrayList<Pair> build (ArrayList<Debt> debts) {
        ArrayList<Pair> fullList = new ArrayList<Pair>();
        int i = 0;
        Pair temp;
        for (Debt debt : debts) {
            temp = new Pair(i, debt.minBalance);
            fullList.add(temp);
            i++;
        }
        Collections.sort(fullList);
        return fullList;
    }

    //collects the most debt payments that are less than input
    // returns an arrayList of pairs that hold all arrays that must be paid
    public ArrayList<Pair> collectPay (ArrayList<Pair> fullList, double input) {
        double total = 0;
        ArrayList<Pair> toPay = new ArrayList<Pair>();
        for (Pair pair : fullList) {
            double check = total + pair.value;
            if (check < input) {
                total = check;
                toPay.add(pair);
            }
        }
        return toPay;
    }

    //SmartPay Algorithm determines best payment strategy - just dont question it.
    public void smartPay(double input, ArrayList<Debt> debts) {
        Log.d("[TEST]", String.valueOf(debts.get(0).creditBalance));
        Log.d("[TEST]", String.valueOf(debts.get(0).creditAvailable));

        double minPayments[] = new double[debts.size()];
        int i =0;
        for (Debt debt : debts) {
            minPayments[i] = debt.minBalance;
            i++;
        }
        double totalMin = basicPayment(minPayments);
        Log.d("[SmartPay-basicPayment]", String.valueOf(totalMin));

        double excess = input-totalMin;
        if (excess>0) {
            for (Debt debt : debts) {
                addFunds(debt, debt.minBalance, false);
                Log.d("[addFunds1]", String.valueOf(debt.minBalance));
                Log.d("[addFunds2]", String.valueOf(debt.purchasesBalance));
                Log.d("[addFunds3]", String.valueOf(debt.cashBalance));
                Log.d("[addFunds4]", String.valueOf(debt.creditBalance));
                Log.d("[addFunds5]", String.valueOf(debt.creditAvailable));
                //pay off the minimum balance for all credit debt
            }
            Debt priority1 = interestCompare(debts);
            Log.d("[interestComp]", String.valueOf(priority1.debtType));
            //add excess money to highest interest rate credit, update debt
            addFunds(priority1, excess, true);


            Log.d("[addFunds6]", String.valueOf(priority1.cashBalance));
            Log.d("[addFunds7]", String.valueOf(priority1.purchasesBalance));
            Log.d("[addFunds8]", String.valueOf(priority1.creditAvailable));
            Log.d("[addFunds9]", String.valueOf(priority1.creditBalance));

        } else {
            ArrayList<Pair> fullList = build(debts);
            Log.d("[build-toPay]", String.valueOf(debts.get(fullList.get(0).key).debtType));
            Log.d("[toPay]", String.valueOf(debts.get(fullList.get(1).key).debtType));
            Log.d("[toPay]", String.valueOf(debts.get(fullList.get(2).key).debtType));
            Log.d("[toPay]", String.valueOf(debts.get(fullList.get(3).key).debtType));

            ArrayList<Pair> debtsToPay = collectPay(fullList, input);

            double paid = 0;
            for (Pair pair : debtsToPay) {
                addFunds(debts.get(pair.key), debts.get(pair.key).minBalance, false);
                paid = paid+pair.value;
            }
            excess = input - paid;
            if (excess>0) {
                Debt priority1 = interestCompare(debts);
                //add excess money to highest interest rate credit, update debt
                addFunds(priority1, excess, true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
