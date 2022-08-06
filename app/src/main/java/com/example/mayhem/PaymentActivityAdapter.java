package com.example.mayhem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PaymentActivityAdapter extends RecyclerView.Adapter<PaymentActivityViewHolder> {

    List<Payment_activity> list
            = Collections.emptyList();

    Context context;
    ClickListener listner;

    public PaymentActivityAdapter(List<Payment_activity> list,
                                 Context context, ClickListener listiner) {
        this.list = list;
        this.context = context;
        this.listner = listiner;
    }

    @Override
    public PaymentActivityViewHolder
    onCreateViewHolder(ViewGroup parent,
                       int viewType) {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.oneitem_payment_activity,
                        parent, false);

        PaymentActivityViewHolder viewHolder
                = new PaymentActivityViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final PaymentActivityViewHolder viewHolder,
                     final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.PaymentName
                .setText(list.get(position).activityName);

        int participants = 0;
        if (list.get(position).getPlayers() != null)
            participants = list.get(position).getPlayers().size();
        viewHolder.Participants.setText(String.valueOf(participants));
        int x = 0;


        if (list.get(position).players != null)
            for (PlayerDetails p : list.get(position).players.values())
                x +=p.amountPaid;
        viewHolder.AmountPaid
                .setText(String.valueOf(x+list.get(position).paidOutside));

        x = 0;
        if (list.get(position).players != null)
            for (PlayerDetails p : list.get(position).players.values())
                x +=p.amountOwed;

        viewHolder.AmountDue
                .setText(String.valueOf(x));
        if (x == 0) {
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