package com.example.mayhem;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlayerDetailsViewHolder extends RecyclerView.ViewHolder {
    TextView PlayerName;
    EditText AmountDue, AmountPaid;
    ImageView PaidEverything;
    View view;

    PlayerDetailsViewHolder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        AmountDue
                = (EditText) itemView
                .findViewById(R.id.owed_text);
        AmountPaid = (EditText) itemView.findViewById(R.id.paid_text);
        PaidEverything = (ImageView) itemView.findViewById(R.id.paid_everything);
        view  = itemView;
    }

}