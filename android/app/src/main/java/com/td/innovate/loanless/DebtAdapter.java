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

import java.util.ArrayList;

/**
 * Created by ImSuperGreg on 2015-09-09.
 */
public class DebtAdapter extends CustomArrayAdapter<Debt> {

    public DebtAdapter(Context _context, ArrayList<Debt> debtList) {
        super(_context, R.layout.list_view_loans, debtList);
    }

    public void fillViewHolder(Object viewHolder, Debt data) {
        Context context = getContext();

        ViewHolder vh = (ViewHolder) viewHolder;
        vh.name.setText(String.valueOf(data.debtType));
        vh.total.setText(String.format(context.getResources().getString(R.string.listViewItem_Total),
                String.valueOf(data.principal)));

        vh.balance.setText(String.format(context.getResources().getString(R.string.listViewItem_Balance),
                String.valueOf(data.creditBalance)));

        vh.dueDate.setText(String.format(context.getResources().getString(R.string.listViewItem_DueDate),
                String.valueOf("09/08/2015")));

        vh.picture_activity.setImageResource(R.drawable.placeholder_car_icon);

        // set the progress bar
        int progress;
        if (data.debtType.equals("Credit Card")) {
            progress = (int) ((data.creditBalance / data.creditLimit) * 100);
            vh.progress.setProgress(progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.progress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                // change color to pink
                vh.progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F26390")));
                vh.background_circle.setColorFilter(Color.parseColor("#F26390"), PorterDuff.Mode.SRC_ATOP);

            }
        } else if (data.debtType.equals("Student Loan")) {
            progress = (int) ((data.creditBalance / data.principal) * 100);
            vh.progress.setProgress(progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.progress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                // change color to yellow
                vh.progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFCA8E")));
                vh.background_circle.setColorFilter(Color.parseColor("#FFCA8E"), PorterDuff.Mode.SRC_ATOP);
            }
        } else if (data.debtType.equals("Car Loan")) {
            progress = (int) ((data.creditBalance / data.creditLimit) * 100);
            vh.progress.setProgress(progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.progress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                // change color to blue
                vh.progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#7DDFE7")));
                vh.background_circle.setColorFilter(Color.parseColor("#7DDFE7"), PorterDuff.Mode.SRC_ATOP);
            }
        } else {
            //for any other kind of loan
            progress = (int) ((data.creditBalance / data.creditLimit) * 100);
            vh.progress.setProgress(progress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh.progress.setProgressBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                // change color to green
                vh.progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#A5D6A7")));
                vh.background_circle.setColorFilter(Color.parseColor("#A5D6A7"), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public Object getViewHolder(View rowView) {

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.name = (TextView) rowView.findViewById(R.id.item_name);
        viewHolder.total = (TextView)rowView.findViewById(R.id.item_total);
        viewHolder.dueDate = (TextView)rowView.findViewById(R.id.item_due_date);
        viewHolder.balance = (TextView)rowView.findViewById(R.id.item_balance);
        viewHolder.status = (TextView)rowView.findViewById(R.id.status_indicator);
        viewHolder.picture_activity = (ImageView)rowView.findViewById(R.id.picture_activity);
        viewHolder.progress = (ProgressBar)rowView.findViewById(R.id.progressBar);
        viewHolder.background_circle = (ImageView)rowView.findViewById(R.id.background_circle);

        return viewHolder;
    }

    private final class ViewHolder{
        TextView name;
        TextView total;
        TextView balance;
        TextView dueDate;
        TextView status;
        ImageView picture_activity;
        ImageView background_circle;
        ProgressBar progress;

    }
}

/*
this allows you to add the different fields that your row element xml file has. so ours has 5 textviews and an imageview

        in the `getViewHolder`, you need to create an instance of the viewholder as follows
        `ViewHolder viewHolder = new ViewHolder();`
        this is where you will look for the different elements in the row view it would be something like this
        ` viewHolder.name = (TextView) rowView.findViewById(R.id.item_name);`

        and then, in the `fillViewHolder`, you take the object and make it a viewholder

        like this `ViewHolder vh = (ViewHolder)viewHolder;`

        and then you set the textviews and imageviews from the passed in data object
*/