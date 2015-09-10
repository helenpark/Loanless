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
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        });
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
