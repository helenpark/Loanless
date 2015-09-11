package com.td.innovate.loanless;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayLoansActivity extends AppCompatActivity {
    boolean isSmartPay = false;
    double payAmount=0;
    int posn;
    double totalAmount;
    DecimalFormat df = new DecimalFormat("#0.00");

    ListView listView;
    PayLoanAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_loans);

        isSmartPay = getIntent().getExtras().getBoolean("isSmartPay");

        if(!isSmartPay) {
            LinearLayout smartLayout = (LinearLayout) findViewById(R.id.linearLayoutSmartPay);
            smartLayout.setVisibility(View.GONE);
            Log.d("[PayLoansActivity]", "Hiding smart payment text selection.");

        }


        listView = (ListView) findViewById(R.id.listViewPayLoans);
        ArrayList<Debt> debtList = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

        final Context context = getApplicationContext();

        adapt = new PayLoanAdapter(this, debtList);
        listView.setAdapter(adapt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                posn = position;
                final Context cont = getApplicationContext();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            final EditText input = new EditText(cont);
                            input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                            input.setTextColor(Color.BLACK);
                            new AlertDialog.Builder(PayLoansActivity.this)
                                    .setTitle("Payment Amount")
                                    .setMessage("Amount to pay:")
                                    .setView(input)
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            payAmount = Double.parseDouble(input.getText().toString());
                                            View x = listView.getChildAt(posn);
                                            TextView et = (TextView) x.findViewById(R.id.textSmartPayValue);
                                            et.setText(String.valueOf(payAmount));
                                            adapt.getItem(posn).smartTab = payAmount;
                                            adapt.notifyDataSetChanged();
                                            totalAmount = totalAmount + payAmount;
                                            final Button button = (Button) findViewById(R.id.btnPay);
                                            button.setText("PAY NOW: $" + df.format(totalAmount));
                                        }
                                    }).create().show();
                        }
                    }
                });
            }
        });


        final Button button = (Button) findViewById(R.id.btnPay);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Debt> debtList = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

                for(int i = 0; i < debtList.size(); i++) {

                    try {
                        debtList.get(i).addTab(adapt.getItem(i).smartTab);

                        Log.d("[PayLoansActivity]", "Paying Loan: (" + debtList.get(i).debtType + "): " + adapt.getItem(i).smartTab);

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                //TODO: apply changes to persistent data
                DebtStorage.storeDebtToSharedPrefs(getApplicationContext(), debtList);

                Toast.makeText(getApplicationContext(), "Payments made successfully.",
                        Toast.LENGTH_LONG).show();
                finish();
                adapt.notifyDataSetChanged();

            }
        });

        final Button buttonAlloc = (Button) findViewById(R.id.btnSmartPayAllocate);
        buttonAlloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Debt> debtList = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

                Log.d("[PayLoansActivity]", "Allocating Smart Pay Money.");

                EditText et = (EditText) findViewById(R.id.editTextSmartPay);
                Double val = Double.parseDouble(et.getText().toString());

                smartPay(val, debtList);

                ListView listView = (ListView) findViewById(R.id.listViewPayLoans);

                // Reset data adapter
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                // Hide keyboard
                et.setInputType(0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

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
