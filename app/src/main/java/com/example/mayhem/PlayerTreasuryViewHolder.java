package com.example.mayhem;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerTreasuryViewHolder extends RecyclerView.ViewHolder {
    TextView PlayerName, AmountDue, AmountPaid;
    ImageView PaidEverything;
    View view;

    PlayerTreasuryViewHolder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        AmountDue
                = (TextView) itemView
                .findViewById(R.id.owed_text);
        AmountPaid = (TextView) itemView.findViewById(R.id.paid_text);
        PaidEverything = (ImageView) itemView.findViewById(R.id.paid_everything);
        view  = itemView;
    }

}