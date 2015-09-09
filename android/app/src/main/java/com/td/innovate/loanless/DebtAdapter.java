package com.td.innovate.loanless;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ImSuperGreg on 2015-09-09.
 */
public class DebtAdapter extends CustomArrayAdapter<Debt> {

    public DebtAdapter(Context _context, ArrayList<Debt> debtList) {
        super(_context, R.layout.list_view_loans, debtList);
    }

    public void fillViewHolder(Object viewHolder, Debt data) {
        Context context = getContext();

        ViewHolder vh = (ViewHolder)viewHolder;
        vh.name.setText(String.valueOf(data.debtType));
        vh.total.setText(String.format(context.getResources().getString(R.string.listViewItem_Total),
                String.valueOf(data.principal)));

        vh.balance.setText(String.format(context.getResources().getString(R.string.listViewItem_Balance),
                String.valueOf(data.creditBalance)));

        vh.dueDate.setText(String.format(context.getResources().getString(R.string.listViewItem_DueDate),
                String.valueOf("09/08/2015")));

        vh.picture_activity.setImageResource(R.drawable.placeholder_car_icon);
    }

    public Object getViewHolder(View rowView) {

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.name = (TextView) rowView.findViewById(R.id.item_name);
        viewHolder.total = (TextView)rowView.findViewById(R.id.item_total);
        viewHolder.dueDate = (TextView)rowView.findViewById(R.id.item_due_date);
        viewHolder.balance = (TextView)rowView.findViewById(R.id.item_balance);
        viewHolder.picture_activity = (ImageView)rowView.findViewById(R.id.picture_activity);

        return viewHolder;
    }

    private final class ViewHolder{
        TextView name;
        TextView total;
        TextView balance;
        TextView dueDate;
        ImageView picture_activity;

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