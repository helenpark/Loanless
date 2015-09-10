package com.td.innovate.loanless;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayLoansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_loans);

        LinearLayout main = (LinearLayout) findViewById(R.id.linearLayoutTexting);

        List<Debt> debtList = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

        int index = 0;
        for(Debt d : debtList) {
            LinearLayout layout = new LinearLayout(
                    PayLoansActivity.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            layout.setLayoutParams(params);
            ViewGroup.LayoutParams paramz = layout.getLayoutParams();
            paramz.height=200;

            TextView text = new TextView(PayLoansActivity.this);
            text.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setText(d.debtType + ": ($)");
            text.setTextColor(Color.BLACK);
            text.setTextSize(24);

            EditText text2 = new EditText(PayLoansActivity.this);
            text2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            text2.setText("");
            text2.setInputType(InputType.TYPE_CLASS_NUMBER);
            text2.setTextColor(Color.BLACK);
            text2.setTextSize(20);
            //text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text2.setGravity(Gravity.CENTER);
            if(index == 0) text2.setId(R.id.edit_text_debt_1);
            if(index == 1) text2.setId(R.id.edit_text_debt_2);
            if(index == 2) text2.setId(R.id.edit_text_debt_3);
            if(index == 3) text2.setId(R.id.edit_text_debt_4);
            if(index == 4) text2.setId(R.id.edit_text_debt_5);
            if(index == 5) text2.setId(R.id.edit_text_debt_6);


            layout.addView(text);
            layout.addView(text2);
            main.addView(layout);
            Log.d("[PayLoansActivity]", "Text Box: " + index + ".");
            index++;
        }


        final Button button = (Button) findViewById(R.id.btnPay);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Debt> debtList = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

                Log.d("[PayLoansActivity]", "PayingLoan: " + debtList.get(0).debtType);

                for(int i = 0; i < debtList.size(); i++) {
                    try {

                        EditText text1;
                        if(i == 0) text1 = (EditText) findViewById(R.id.edit_text_debt_1);
                        else if(i == 1) text1 = (EditText) findViewById(R.id.edit_text_debt_2);
                        else if(i == 2) text1 = (EditText) findViewById(R.id.edit_text_debt_3);
                        else if(i == 3) text1 = (EditText) findViewById(R.id.edit_text_debt_4);
                        else if(i == 4) text1 = (EditText) findViewById(R.id.edit_text_debt_5);
                        else if(i == 5) text1 = (EditText) findViewById(R.id.edit_text_debt_6);
                        else continue;

                        if(text1.getText().toString().equals(""))
                            continue;

                        Double val = Double.parseDouble(text1.getText().toString());

                        Log.d("[PayLoansActivity]", "PayingLoan: " + debtList.get(i).debtType + " + " + val);
                        debtList.get(i).addTab(val);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
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

    //adds amount to current smartPay in debt
    public void smartTab(Debt d, double funds) {
        d.smartTab = d.smartTab + funds;
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

                smartTab(debt, debt.minBalance);
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
            smartTab(priority1, excess);


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
                smartTab(debts.get(pair.key), debts.get(pair.key).minBalance);
                paid = paid+pair.value;
            }
            excess = input - paid;
            if (excess>0) {
                Debt priority1 = interestCompare(debts);
                //add excess money to highest interest rate credit, update debt
                smartTab(priority1, excess);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay_loans, menu);
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
