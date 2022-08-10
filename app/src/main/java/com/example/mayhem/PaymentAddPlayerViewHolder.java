package com.example.mayhem;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PaymentAddPlayerViewHolder extends RecyclerView.ViewHolder {
    TextView PlayerName;
    CheckBox add;
    View view;

    PaymentAddPlayerViewHolder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        add
                = (CheckBox) itemView
                .findViewById(R.id.checkBox3);
        view  = itemView;
    }

}