package com.example.mayhem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PaymentActivityViewHolder extends RecyclerView.ViewHolder {
    TextView PaymentName, AmountDue, AmountPaid, Participants;
    ImageView PaidEverything;
    View view;

    PaymentActivityViewHolder(View itemView)
    {
        super(itemView);
        PaymentName
                = (TextView)itemView
                .findViewById(R.id.activity_name);
        AmountDue
                = (TextView) itemView
                .findViewById(R.id.owed_text);
        AmountPaid = (TextView) itemView.findViewById(R.id.paid_text);
        Participants = (TextView) itemView.findViewById(R.id.participants_text);
        PaidEverything = (ImageView) itemView.findViewById(R.id.paid_everything);
        view  = itemView;
    }

}