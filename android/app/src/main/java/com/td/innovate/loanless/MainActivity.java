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
