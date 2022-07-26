package com.example.mayhem;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Attendence_List_View_Holder extends RecyclerView.ViewHolder {
    TextView PlayerName;
    CheckBox Attended;
    View view;

    Attendence_List_View_Holder(View itemView)
    {
        super(itemView);
        PlayerName
                = (TextView)itemView
                .findViewById(R.id.player_name);
        Attended
                = (CheckBox) itemView
                .findViewById(R.id.checkBox);
        view  = itemView;
    }

}