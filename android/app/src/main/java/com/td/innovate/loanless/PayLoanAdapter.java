package com.td.innovate.loanless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ImSuperGreg on 2015-09-09.
 */
public class PayLoanAdapter extends CustomArrayAdapter<Debt> {

    public PayLoanAdapter(Context _context, ArrayList<Debt> debtList) {
        super(_context, R.layout.list_view_pay_loans, debtList);
    }

    public void fillViewHolder(Object viewHolder, Debt data) {
        Context context = getContext();

        ViewHolder vh = (ViewHolder) viewHolder;
        vh.name.setText(String.valueOf(data.title));

        DecimalFormat df = new DecimalFormat("#.00");


        vh.balance.setText(String.format(context.getResources().getString(R.string.listViewItem_Balance),
                String.valueOf(df.format(data.creditBalance))));

        // set the progress bar
        int progress;
        if (data.debtType.equals("credit")) {
            vh.picture_activity.setImageResource(R.drawable.placeholder_card_icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.background_circle.setColorFilter(Color.parseColor("#F26390"), PorterDuff.Mode.SRC_ATOP);
            }
        } else if(data.debtType.equals("loan")){
            if (data.title.equals("Student Loan")) {
                vh.picture_activity.setImageResource(R.drawable.placeholder_student_icon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    vh.background_circle.setColorFilter(Color.parseColor("#FFCA8E"), PorterDuff.Mode.SRC_ATOP);
                }
            } else if (data.title.equals("Car Loan")) {
                vh.picture_activity.setImageResource(R.drawable.placeholder_car_icon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    vh.background_circle.setColorFilter(Color.parseColor("#7DDFE7"), PorterDuff.Mode.SRC_ATOP);
                }
            }else{
                vh.picture_activity.setImageResource(R.drawable.placeholder_home_icon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    vh.background_circle.setColorFilter(Color.parseColor("#A5D6A7"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        } else {
            vh.picture_activity.setImageResource(R.drawable.placeholder_home_icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.background_circle.setColorFilter(Color.parseColor("#B39DDB"), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public Object getViewHolder(View rowView) {

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.name = (TextView) rowView.findViewById(R.id.loan_item_name);
        viewHolder.balance = (TextView)rowView.findViewById(R.id.loan_item_balance);
        viewHolder.picture_activity = (ImageView)rowView.findViewById(R.id.loan_picture_activity);
        viewHolder.background_circle = (ImageView)rowView.findViewById(R.id.loan_background_circle);

        return viewHolder;
    }

    private final class ViewHolder{
        TextView name;
        TextView balance;
        ImageView picture_activity;
        ImageView background_circle;
    }
}
