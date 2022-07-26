package com.example.mayhem;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Attendence_View_Holder extends RecyclerView.ViewHolder {
    TextView PlayerName;
    TextView Attended;
    TextView Missed;
    View view;

    Attendence_View_Holder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        Attended
                = (TextView)itemView
                .findViewById(R.id.attended_text);
        Missed
                = (TextView)itemView
                .findViewById(R.id.missed_text);
        view  = itemView;
    }

}