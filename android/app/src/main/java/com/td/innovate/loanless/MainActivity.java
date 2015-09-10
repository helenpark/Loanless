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
/*
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
*/
        //TODO: create debt objects that make sense in real life
        Debt d1 = new Debt("Everyday Credit", "Good","credit", 0.0, 0.0, 0.0, 101, 0.19, 0.22, 0, 0, 488, 450, 1500);
        Debt d2 = new Debt("Student Loan","Good","loan",0.0, 220, 6200, 0.0, 0.0, 0.0, 23000, 0.08, 0.0, 0.0, 0.0);
        Debt d3 = new Debt("Car Loan", "Not Enough Funds","loan",0.0, 380, 5500, 0.0, 0.0, 0.0, 7000, 0.12, 0.0, 0.0, 0.0);
        Debt d4 = new Debt("VISA Credit", "Paid Min","credit",0.0, 0.0, 0.0, 23, 0.17, 0.30, 0, 0, 106, 307, 1000);
        Debt d5 = new Debt("Extra Credit", "Late","credit",0.0, 0.0, 0.0, 10, 0.1, 0.13, 0, 0, 0, 35, 300);


        ArrayList<Debt> listDebt = new ArrayList<Debt>();
        listDebt.add(d1);
        listDebt.add(d2);
        listDebt.add(d3);
        listDebt.add(d4);
        listDebt.add(d5);

        DebtStorage.storeDebtToSharedPrefs(getApplicationContext(), listDebt);

        ArrayList<Debt> listDebtJsonified = DebtStorage.getDebtFromSharedPrefs(getApplicationContext());

        for(Debt dz : listDebtJsonified) {
            Log.d("[Main Activity]", "Object was pulled from memory: " + dz.debtType + " " + dz.creditLimit);
        }

        Context context = getApplicationContext();

        DebtAdapter adapt = new DebtAdapter(context, listDebtJsonified);

        ListView view = (ListView)findViewById(R.id.listView);
        view.setAdapter(adapt);


        final Button button = (Button) findViewById(R.id.btnMakePayment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, PayLoansActivity.class);
                myIntent.putExtra("isSmartPay", false); //Optional parameters
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
}
