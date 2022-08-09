package com.example.mayhem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerPaymentsViewHolder extends RecyclerView.ViewHolder {
    TextView PaymentName, AmountDue, AmountPaid;
    ImageView PaidEverything;
    View view;

    PlayerPaymentsViewHolder(View itemView)
    {
        super(itemView);
        PaymentName
                = (TextView)itemView
                .findViewById(R.id.activity_name);
        AmountDue
                = (TextView) itemView
                .findViewById(R.id.owed_text);
        AmountPaid = (TextView) itemView.findViewById(R.id.paid_text);
        PaidEverything = (ImageView) itemView.findViewById(R.id.paid_everything);
        view  = itemView;
    }

}