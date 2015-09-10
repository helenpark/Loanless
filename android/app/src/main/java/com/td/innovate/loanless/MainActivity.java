package com.td.innovate.loanless;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        //TODO: create debt objects that make sense in real life
        Debt d1 = new Debt("cc1", "","Credit Card", 0.0, 0.0, 101, 102, 103, 104, 105, 106, 107, 500);
        Debt d2 = new Debt("l1","","Student Loan", 0.0, 0.0, 201, 202, 203, 204, 2000, 206, 207, 208);
        Debt d3 = new Debt("l2",  "","Car Loan", 0.0, 0.0, 201, 202, 203, 204, 7000, 206, 207, 208);
        Debt d4 = new Debt("o1", "","Mortgage", 0.0, 0.0,  101, 102, 103, 104, 105, 250, 107, 500);

        ArrayList<Debt> listDebt = new ArrayList<Debt>();
        listDebt.add(d1);
        listDebt.add(d2);
        listDebt.add(d3);
        listDebt.add(d4);

        DebtStorage.storeDebtToSharedPrefs(getApplicationContext(), listDebt);

        ArrayList<Debt> listDebtJsonified = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

        /*for(Debt dz : listDebtJsonified) {
            Log.d("[Main Activity]", "Object was pulled from memory: " + dz.debtType + " " + dz.creditLimit);
        }*/

        Context context = getApplicationContext();

        DebtAdapter adapt = new DebtAdapter(context, listDebtJsonified);

        ListView view = (ListView)findViewById(R.id.listView);
        view.setAdapter(adapt);


        final Button button = (Button) findViewById(R.id.btnMakePayment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, PayLoansActivity.class);
                myIntent.putExtra("isSmartPay", false);
                MainActivity.this.startActivity(myIntent);
            }
        });
        final Button button2 = (Button) findViewById(R.id.btnSmartPay);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, PayLoansActivity.class);
                myIntent.putExtra("isSmartPay", true);
                MainActivity.this.startActivity(myIntent);
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Context context = getApplicationContext();
        ArrayList<Debt> listDebt = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

        Log.d("[MainActivity]", "Resuming MainActivity: Updating data.");

        DebtAdapter adapt = new DebtAdapter(context, listDebt);

        ListView view = (ListView)findViewById(R.id.listView);
        view.setAdapter(adapt);
    }
}
