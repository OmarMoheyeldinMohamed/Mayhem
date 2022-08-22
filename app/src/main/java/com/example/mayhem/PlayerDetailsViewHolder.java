package com.example.mayhem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerDetailsViewHolder extends RecyclerView.ViewHolder {
    TextView PlayerName;
    TextView AmountDue, AmountPaid;
    EditText payAmount;
    Button payBtn;
    ImageView PaidEverything;
    View view;

    PlayerDetailsViewHolder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        AmountDue
                = (TextView) itemView
                .findViewById(R.id.owed_text);
        AmountPaid = (TextView) itemView.findViewById(R.id.paid_text);
        payBtn = (Button) itemView.findViewById(R.id.paybtn);
        payAmount = (EditText) itemView.findViewById(R.id.pay_edit);

        PaidEverything = (ImageView) itemView.findViewById(R.id.paid_everything);
        view  = itemView;
    }

}