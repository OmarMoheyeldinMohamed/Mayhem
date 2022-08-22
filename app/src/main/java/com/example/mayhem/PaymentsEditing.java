package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
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
    List<Integer> paying;
    List<Boolean> enable;


    PlayerDetailsAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_editing);

        paymentActivity = (Payment_activity) getIntent().getSerializableExtra("payment");


        Map<String, PlayerDetails> oldValues =  new HashMap<>();
        if (paymentActivity.getPlayers() != null)
        {
            for (String name : paymentActivity.getPlayers().keySet())
            {
                oldValues.put(name, new PlayerDetails(paymentActivity.getPlayers().get(name)));
            }
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
                int paidOutsideInt;
                if (TextUtils.isEmpty(paidOutside.getText().toString()))
                    paidOutsideInt = 0;
                else
                {
                    paidOutsideInt = Integer.parseInt(paidOutside.getText().toString());
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("paymentActivity").child(paymentActivity.getID())
                        .child("paidOutside").setValue(paidOutsideInt);
                Save.setEnabled(false);
            }
        });

        /*
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
                SystemClock.sleep(300);
                PaymentsEditing.this.finish();
            }
        });

         */


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
        paying = new ArrayList<>();
        enable = new ArrayList<>();


        Map<String, PlayerDetails> map = paymentActivity.getPlayers();

        if (map!=null)
        {
            list.addAll(map.values());
        }
        for (int i =0; i<list.size();i++)
        {
            paying.add(0);
            enable.add(false);
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
            public void click3(int index) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Map<String, Integer> changedOwed = new HashMap<>(), changedPaid = new HashMap<>();



                    if (paying.get(index) != 0)
                    {
                        int owed = list.get(index).getAmountOwed();
                        int paid = list.get(index).getAmountPaid();
                        owed -= paying.get(index);
                        paid += paying.get(index);
                        list.get(index).setAmountOwed(owed);
                        list.get(index).setAmountPaid(paid);


                        changedOwed.put(list.get(index).getID(), owed);
                        changedPaid.put(list.get(index).getID(), paid);


                        databaseReference.child("paymentActivity").child(paymentActivity.getID()).child("players").setValue(paymentActivity.getPlayers());
                        for (String ID : changedOwed.keySet())
                        {
                            databaseReference.child("playerTreasury").child(ID).child("amountOwed").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer oldOwed = snapshot.getValue(Integer.class);
                                    int differenceOwed = list.get(index).getAmountOwed() - oldValues.get(list.get(index).getName()).getAmountOwed();
                                    int newOwed = oldOwed + differenceOwed;
                                    oldValues.get(list.get(index).getName()).setAmountOwed(changedOwed.get(list.get(index).getID()));
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
                                    int differencePaid = list.get(index).getAmountPaid() - oldValues.get(list.get(index).getName()).getAmountPaid();
                                    int newPaid = oldPaid + differencePaid;
                                    oldValues.get(list.get(index).getName()).setAmountPaid(changedPaid.get(list.get(index).getID()));
                                    databaseReference.child("playerTreasury").child(ID).child("amountPaid").setValue(newPaid);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        View v = recyclerView.getLayoutManager().findViewByPosition(index);
                        TextView vv = (TextView) v.findViewById(R.id.pay_edit);
                        vv.setText("");

                        enable.set(index, false);
                        adapter.notifyDataSetChanged();


                    }



                //Toast.makeText(root.getContext(),  "clicked item index is "+index,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void keyPressed(int keyCode, int index, int choice, String text)
            {
                if ((keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) || keyCode == KeyEvent.KEYCODE_DEL ||keyCode == KeyEvent.KEYCODE_FORWARD_DEL)

                {
                    enable.set(index, true);
                    View v = recyclerView.getLayoutManager().findViewByPosition(index);
                    v.findViewById(R.id.paybtn).setEnabled(true);

                    int pay;
                    if (TextUtils.isEmpty(text))
                        pay = 0;
                    else if (text.equals("-"))
                        return;
                    else
                        pay = Integer.parseInt(text);
                    paying.set(index, pay);

//                    if (choice == 0)
//                    {
//                        int newOwed;
//                        if (TextUtils.isEmpty(text))
//                            newOwed =0;
//                        else
//                            newOwed= Integer.parseInt(text);
//                        list.get(index).setAmountOwed(newOwed);
//                    }
//                    else
//                    {
//                        int newPaid;
//                        if (TextUtils.isEmpty(text))
//                            newPaid =0;
//                        else
//                            newPaid = Integer.parseInt(text);
//                        list.get(index).setAmountPaid(newPaid);
//                    }
                }


            }

            @Override
            public void owedPressed(int index)
            {
                PlayerDetails playerDetails = list.get(index);
                Intent i = new Intent(PaymentsEditing.this, PopUpEditOwed.class);
                i.putExtra("payment", paymentActivity);
                i.putExtra("player", playerDetails);
                startActivity(i);
            }
        };

        adapter
                = new PlayerDetailsAdapter(
                list,enable, this ,listener);
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