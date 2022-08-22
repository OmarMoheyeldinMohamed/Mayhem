package com.example.mayhem;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PlayerDetailsAdapter extends RecyclerView.Adapter<PlayerDetailsViewHolder> {

    private final List<Boolean> enable;
    List<PlayerDetails> list
            = Collections.emptyList();

    Context context;
    ClickListener listner;

    public PlayerDetailsAdapter(List<PlayerDetails> list,List<Boolean> enable,
                                 Context context, ClickListener listiner)
    {
        this.list = list;
        this.context = context;
        this.listner = listiner;
        this.enable = enable;
    }

    @Override
    public PlayerDetailsViewHolder
    onCreateViewHolder(ViewGroup parent,
                       int viewType)
    {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.oneitemplayerdetails,
                        parent, false);

        PlayerDetailsViewHolder viewHolder
                = new PlayerDetailsViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final PlayerDetailsViewHolder viewHolder,
                     final int position)
    {
        final int index = viewHolder.getAdapterPosition();

        viewHolder.payBtn.setEnabled(this.enable.get(position));
        viewHolder.PlayerName
                .setText(list.get(position).name);

        viewHolder.AmountDue
                .setText(String.valueOf(list.get(position).getAmountOwed()));
        viewHolder.AmountDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.owedPressed(position);
            }
        });
        viewHolder.AmountPaid
                .setText(String.valueOf(list.get(position).getAmountPaid()));
        int d = 0;
        if (list.get(position).getAmountOwed()<=0)
        {
            viewHolder.PaidEverything.setImageResource(R.drawable.check);
        }
        else
            viewHolder.PaidEverything.setImageResource(R.drawable.cross);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listner.click(index);
            }
        });
        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listner.click2(index);
                return false;
            }
        });


//        viewHolder.AmountDue.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                listner.keyPressed(keyCode, index,0, viewHolder.AmountDue.getText().toString());
//                return false;
//            }
//        });
//        viewHolder.AmountPaid.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                listner.keyPressed(keyCode, index, 1, viewHolder.AmountPaid.getText().toString());
//                return false;
//            }
//        });

        viewHolder.payAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                listner.keyPressed(keyCode, index,0, viewHolder.payAmount.getText().toString());
                return false;
            }
        });

        viewHolder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.click3(index);
            }
        });
//        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                listner.click2(index);
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}