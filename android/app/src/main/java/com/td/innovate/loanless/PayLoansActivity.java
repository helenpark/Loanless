package com.td.innovate.loanless;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

            TextView text = new TextView(PayLoansActivity.this);
            text.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setText("PLEASE SHOW ME SOME TEXT!");
            text.setTextColor(Color.RED);

            EditText text2 = new EditText(PayLoansActivity.this);
            text2.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            text2.setText("PLEASE SHOW ME SOME MORE TEXT!");
            text2.setTextColor(Color.GREEN);

            layout.addView(text);
            layout.addView(text2);
            main.addView(layout);
            Log.d("[PayLoansActivity]", "Text Box: " + index + ".");
            index++;
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
