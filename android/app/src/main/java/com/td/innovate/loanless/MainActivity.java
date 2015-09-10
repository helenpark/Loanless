package com.td.innovate.loanless;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //TODO: remove all log.d statements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: create debt objects that make sense in real life
        Debt d1 = new Debt("Everyday Credit Card", "Good","credit", 0.0, 0.0, 0.0, 101, 102, 103, 104, 105, 106, 107, 500);
        Debt d2 = new Debt("Student Loan","Good","loan",0.0, 0.0, 585, 201, 202, 203, 2000, 2, 206, 207, 208);
        Debt d3 = new Debt("Car Loan", "Not Enough Funds","loan",0.0, 0.0, 4900, 201, 202, 203, 7000, 7000, 206, 207, 208);
        Debt d4 = new Debt("Credit Card # 2", "Paid Min","credit",0.0, 0.0, 0.0, 101, 102, 103, 104, 105, 106, 107, 500);
        Debt d5 = new Debt("Credit Card # 3", "Late","credit",0.0, 0.0, 0.0, 101, 102, 103, 104, 105, 106, 107, 500);
        Debt d6 = new Debt("Mortgage","Not Enough Funds","loan",0.0, 0.0, 10000, 201, 202, 203, 200000, 205, 206, 207, 208);


        ArrayList<Debt> listDebt = new ArrayList<Debt>();
        listDebt.add(d1);
        listDebt.add(d2);
        listDebt.add(d3);
        listDebt.add(d4);
        listDebt.add(d5);
        listDebt.add(d6);

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
