package com.td.innovate.loanless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Debt d = new Debt("CreditCard", 101, 102, 103, 104, 105, 106, 107, 108);
        Debt d2 = new Debt("StudentLoan", 201, 202, 203, 204, 205, 206, 207, 208);

        List<Debt> listDebt = new ArrayList<Debt>();
        listDebt.add(d);
        listDebt.add(d2);

        DebtStorage storage = new DebtStorage();
        storage.storeDebtToSharedPrefs(getApplicationContext(), listDebt);

        List<Debt> listDebtJsonified = storage.getDebtFromSharedPrefs(getApplicationContext());

        for(Debt dz : listDebtJsonified) {
            Log.d("[Main Activity]", "Object was pulled from memory: " + dz.debtType + " " + dz.creditLimit);
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
