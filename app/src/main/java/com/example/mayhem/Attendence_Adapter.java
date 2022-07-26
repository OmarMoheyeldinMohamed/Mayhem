package com.example.mayhem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Attendence_Adapter extends RecyclerView.Adapter<Attendence_View_Holder> {

    List<Attendence_Data> list
            = Collections.emptyList();

    Context context;
    ClickListener listner;

    public Attendence_Adapter(List<Attendence_Data> list,
                                 Context context, ClickListener listiner)
    {
        this.list = list;
        this.context = context;
        this.listner = listiner;
    }

    @Override
    public Attendence_View_Holder
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
                .inflate(R.layout.oneitem_players_attendance,
                        parent, false);

        Attendence_View_Holder viewHolder
                = new Attendence_View_Holder(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final Attendence_View_Holder viewHolder,
                     final int position)
    {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.PlayerName
                .setText(list.get(position).Name);
        viewHolder.Attended
                .setText(list.get(position).Attended);
        viewHolder.Missed
                .setText(list.get(position).Missed);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                listner.click(index);
            }
        });
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