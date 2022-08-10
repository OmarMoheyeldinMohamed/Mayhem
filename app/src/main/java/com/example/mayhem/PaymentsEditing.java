package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaymentsEditing extends AppCompatActivity {

    Payment_activity paymentActivity;
    List<PlayerDetails> list;

    PlayerDetailsAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_editing);

        paymentActivity = (Payment_activity) getIntent().getSerializableExtra("payment");


        Map<String, PlayerDetails> oldValues =  new HashMap<>();
        for (String name : paymentActivity.getPlayers().keySet())
        {
            oldValues.put(name, new PlayerDetails(paymentActivity.getPlayers().get(name)));
        }

        TextView Name = findViewById(R.id.activityName);
        TextView Paid = findViewById(R.id.amountPaid);
        TextView Owed = findViewById(R.id.amountOwed);
        TextView Participants = findViewById(R.id.participants);
        EditText paidOutside = findViewById(R.id.paidOutside);
        Button Save = (Button) findViewById(R.id.savebtn);
        Button Saveall =(Button) findViewById(R.id.saveallbtn);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int paidOutsideInt = Integer.parseInt(paidOutside.getText().toString());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("paymentActivity").child(paymentActivity.getID())
                        .child("paidOutside").setValue(paidOutsideInt);
                Save.setEnabled(false);
            }
        });

        Saveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Map<String, Integer> changedOwed = new HashMap<>(), changedPaid = new HashMap<>();
                for (PlayerDetails player : list)
                {
                    int differenceOwed = player.getAmountOwed() - oldValues.get(player.getName()).getAmountOwed();
                    int differencePaid = player.getAmountPaid() - oldValues.get(player.getName()).getAmountPaid();
                    if (differenceOwed != 0)
                        changedOwed.put(player.getID(), differenceOwed);
                    if (differencePaid != 0)
                        changedPaid.put(player.getID(), differencePaid);
                }
                databaseReference.child("paymentActivity").child(paymentActivity.getID()).child("players").setValue(paymentActivity.getPlayers());
                for (String ID : changedOwed.keySet())
                {
                    databaseReference.child("playerTreasury").child(ID).child("amountOwed").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer oldOwed = snapshot.getValue(Integer.class);
                            int newOwed = oldOwed + changedOwed.get(ID);
                            databaseReference.child("playerTreasury").child(ID).child("amountOwed").setValue(newOwed);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                for (String ID : changedPaid.keySet())
                {
                    databaseReference.child("playerTreasury").child(ID).child("amountPaid").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer oldPaid = snapshot.getValue(Integer.class);
                            int newPaid = oldPaid + changedPaid.get(ID);
                            databaseReference.child("playerTreasury").child(ID).child("amountPaid").setValue(newPaid);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Saveall.setEnabled(false);
            }
        });
        paidOutside.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) || keyCode == KeyEvent.KEYCODE_DEL ||keyCode == KeyEvent.KEYCODE_FORWARD_DEL)
                    Save.setEnabled(true);
                return false;
            }
        });


        Name.setText(paymentActivity.getActivityName());
        Paid.setText(String.valueOf(paymentActivity.getAmountPaid()));
        Owed.setText(String.valueOf(paymentActivity.getAmountOwed()));
        Participants.setText(String.valueOf(paymentActivity.getNumberofParticipants()));
        paidOutside.setText(String.valueOf(paymentActivity.getPaidOutside()));

        list = new ArrayList<>();

        Map<String, PlayerDetails> map = paymentActivity.getPlayers();

        if (map!=null)
        {
            list.addAll(map.values());
        }


        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click2(int index)
            {
                PlayerDetails playerDetails = list.get(index);
                Intent i = new Intent(PaymentsEditing.this, PopUpDeletePlayerFromActivity.class);
                i.putExtra("payment", paymentActivity);
                i.putExtra("player", playerDetails);
                startActivity(i);
            }
            @Override
            public void click(int index) {
                //Toast.makeText(root.getContext(),  "clicked item index is "+index,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void keyPressed(int keyCode, int index, int choice, String text)
            {
                if ((keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) || keyCode == KeyEvent.KEYCODE_DEL ||keyCode == KeyEvent.KEYCODE_FORWARD_DEL)
                    Saveall.setEnabled(true);

                if (choice == 0)
                {
                    int newOwed = Integer.parseInt(text);
                    list.get(index).setAmountOwed(newOwed);
                }
                else
                {
                    list.get(index).setAmountPaid(Integer.parseInt(text));
                }
            }
        };

        adapter
                = new PlayerDetailsAdapter(
                list, this ,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));



        FloatingActionButton addPlayersBtn = (FloatingActionButton) findViewById(R.id.add_playersbtn);

        addPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentsEditing.this, PopUpAddPlayerToPayment.class);
                i.putExtra("payment", paymentActivity);
                startActivity(i);
            }
        });


    }
}