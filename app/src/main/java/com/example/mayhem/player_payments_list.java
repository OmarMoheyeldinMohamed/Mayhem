package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class player_payments_list extends AppCompatActivity {

    List<Payment_activity> paymentsList;
    List<PlayerPaymentsData> list;

    PlayerPaymentsAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;
    PlayerTreasury player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_payments_list);


        player = (PlayerTreasury) getIntent().getSerializableExtra("player");

        TextView Name = (TextView) findViewById(R.id.playerName);
        TextView Paid = (TextView) findViewById(R.id.amountPaid);
        TextView Owed = (TextView) findViewById(R.id.amountOwed);

        Name.setText(player.getName());
        Paid.setText(String.valueOf(player.getAmountPaid()));
        Owed.setText(String.valueOf(player.getAmountOwed()));


        list = new ArrayList<>();
        paymentsList =  new ArrayList<>();

        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click2(int index)
            {


            }
            @Override
            public void click(int index) {
                //Toast.makeText(root.getContext(),  "clicked item index is "+index,Toast.LENGTH_SHORT).show();
            }
        };

        adapter
                = new PlayerPaymentsAdapter(
                list, this ,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));



        List<PaymentsDetails> paymentsDetails = player.getPaymentsForPlayer();
        if (paymentsDetails == null)
            paymentsDetails = new ArrayList<>();

        for (PaymentsDetails payment : paymentsDetails)
        {
            String ID = payment.getID();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("paymentActivity").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Payment_activity paymentActivity = snapshot.getValue(Payment_activity.class);
                    paymentsList.add(paymentActivity);
                    list.add(new PlayerPaymentsData(paymentActivity.getActivityName(), paymentActivity.getID(),
                            paymentActivity.getPlayers().get(player.getName()).getAmountPaid(), paymentActivity.getPlayers().get(player.getName()).getAmountOwed() ));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }
}