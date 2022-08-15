package com.example.mayhem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PlayerPaymentsAdapter extends RecyclerView.Adapter<PlayerPaymentsViewHolder> {

    List<PlayerPaymentsData> list
            = Collections.emptyList();

    Context context;
    ClickListener listner;

    public PlayerPaymentsAdapter(List<PlayerPaymentsData> list,
                                  Context context, ClickListener listiner) {
        this.list = list;
        this.context = context;
        this.listner = listiner;
    }

    @Override
    public PlayerPaymentsViewHolder
    onCreateViewHolder(ViewGroup parent,
                       int viewType) {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.oneitem_player_activity_list,
                        parent, false);

        PlayerPaymentsViewHolder viewHolder
                = new PlayerPaymentsViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final PlayerPaymentsViewHolder viewHolder,
                     final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.PaymentName
                .setText(list.get(position).activityName);



        viewHolder.AmountPaid
                .setText(String.valueOf(list.get(position).getAmountPaid()));

        viewHolder.AmountDue
                .setText(String.valueOf(list.get(position).getAmountOwed()));
        if (list.get(position).getAmountOwed() == 0) {
            viewHolder.PaidEverything.setImageResource(R.drawable.check);
        } else
            viewHolder.PaidEverything.setImageResource(R.drawable.cross);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.click(index);
            }
        });
//        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                listner.click2(index);
//                return false;
//            }
//        });
//        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                listner.click2(index);
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}