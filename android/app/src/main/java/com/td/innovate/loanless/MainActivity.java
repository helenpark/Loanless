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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Debt d = new Debt("Credit Card", 101, 102, 103, 104, 105, 106, 107, 108);
        Debt d2 = new Debt("Student Loan", 201, 202, 203, 204, 205, 206, 207, 208);
        Debt d3 = new Debt("Student Loan", 201, 202, 203, 204, 205, 206, 207, 208);
        Debt d4 = new Debt("Student Loan", 201, 202, 203, 204, 205, 206, 207, 208);
        Debt d5 = new Debt("Student Loan", 201, 202, 203, 204, 205, 206, 207, 208);

        ArrayList<Debt> listDebt = new ArrayList<Debt>();
        listDebt.add(d);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
