package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PopUpAddPlayerToPayment extends AppCompatActivity {
    List<PlayerTreasury> list, finalList;
    List<Boolean> checkedList;

    Payment_activity paymentActivity;

    PaymentAddPlayerAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_add_player_to_payment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.9));

        Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalList = new ArrayList<>();
                for (int i = 0; i< checkedList.size(); i++)
                {
                    if (!checkedList.get(i))
                        continue;
                    finalList.add(list.get(i));
                }
                Intent gotoScreenVar = new Intent(PopUpAddPlayerToPayment.this, PaymentsEditing.class);

                gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Map<String, PlayerDetails> map =  paymentActivity.getPlayers();
                if (map == null)
                    map = new HashMap<>();
                for (PlayerTreasury player : finalList)
                {
                    player.setAmountOwed(player.getAmountOwed() + paymentActivity.getDefaultPrice());
                    PaymentsDetails paymentsDetails = new PaymentsDetails(paymentActivity.getActivityName(), paymentActivity.getID());
                    List<PaymentsDetails> paymentsDetailsList = player.getPaymentsForPlayer();
                    if (paymentsDetailsList == null)
                        paymentsDetailsList = new ArrayList<>();
                    paymentsDetailsList.add(paymentsDetails);
                    player.setPaymentsForPlayer(paymentsDetailsList);

                    databaseReference.child("playerTreasury").child(player.getID()).setValue(player);


                    PlayerDetails p = new PlayerDetails(player.getID(), player.getName(),0, paymentActivity.getDefaultPrice());

                   map.put(player.getName(), p);
                }
                paymentActivity.setPlayers(map);
                databaseReference.child("paymentActivity").child(paymentActivity.getID()).setValue(paymentActivity);
                gotoScreenVar.putExtra("payment", paymentActivity);
                startActivity(gotoScreenVar);
            }
        });

        paymentActivity = (Payment_activity) getIntent().getSerializableExtra("payment");
        list = new ArrayList<>();
        checkedList = new ArrayList<>();

        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click(int index, boolean choice)
            {
                checkedList.set(index, choice);
            }
        };

        adapter
                = new PaymentAddPlayerAdapter(
                list,checkedList, this ,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("playerTreasury").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() ==null)
                        return;
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        PlayerTreasury playerTreasury = snapshot1.getValue(PlayerTreasury.class);
                        if (paymentActivity.getPlayers() != null && paymentActivity.getPlayers().containsKey(playerTreasury.getName()))
                        {
                            continue;
                        }
                        list.add(playerTreasury);
                        checkedList.add(false);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




    }
}