package com.example.mayhem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PaymentAddPlayerAdapter extends RecyclerView.Adapter<PaymentAddPlayerViewHolder> {

    List<PlayerTreasury> list
            = Collections.emptyList();

    Context context;
    ClickListener listner;

    public PaymentAddPlayerAdapter(List<PlayerTreasury> list,
                                  Context context, ClickListener listiner) {
        this.list = list;
        this.context = context;
        this.listner = listiner;
    }

    @Override
    public PaymentAddPlayerViewHolder
    onCreateViewHolder(ViewGroup parent,
                       int viewType) {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.oneitemaddplayer,
                        parent, false);

        PaymentAddPlayerViewHolder viewHolder
                = new PaymentAddPlayerViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final PaymentAddPlayerViewHolder viewHolder,
                     final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.PlayerName
                .setText(list.get(position).getName());


        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.click(index, viewHolder.add.isChecked());
            }
        });



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