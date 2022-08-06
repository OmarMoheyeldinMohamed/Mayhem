package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopUpAddPaymentActivity extends AppCompatActivity {

    EditText nameEdit, priceEdit;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_add_payment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.6));


        nameEdit = (EditText) findViewById(R.id.name_edit);
        priceEdit = (EditText) findViewById(R.id.price_edit);
        aSwitch = (Switch) findViewById(R.id.all_players_switch);

        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = nameEdit.getText().toString();
                String Price = priceEdit.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(PopUpAddPaymentActivity.this, "Name field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Price)) {
                    Toast.makeText(PopUpAddPaymentActivity.this, "Price field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int priceInt = Integer.parseInt(Price);

                boolean allPlayers = aSwitch.isChecked();

                Payment_activity paymentActivity = new Payment_activity(Name,0, priceInt);


                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                databaseReference.child("paymentActivity").orderByChild("activityName").equalTo(Name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Payment_activity p = snapshot.getValue(Payment_activity.class);
                        if (p != null) {
                            Toast.makeText(PopUpAddPaymentActivity.this, "Payment name already exists!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                        {
                            String ID = databaseReference.child("paymentActivity").push().getKey();
                            paymentActivity.setID(ID);

                            if (allPlayers)
                            {
                                databaseReference.child("playerTreasury").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue() == null)
                                        {
                                            databaseReference.child("paymentActivity").child(ID).setValue(paymentActivity);
                                        }
                                        else
                                        {
                                            List<PlayerDetails> list  = new ArrayList<>();
                                            for (DataSnapshot snapshot1 :snapshot.getChildren())
                                            {
                                                PlayerTreasury playerTreasury = snapshot1.getValue(PlayerTreasury.class);
                                                PlayerDetails playerDetails = new PlayerDetails(playerTreasury.getID(), playerTreasury.getName(),
                                                        0, priceInt);
                                                list.add(playerDetails);
                                                playerTreasury.setAmountOwed(playerTreasury.getAmountOwed()+priceInt);
                                                PaymentsDetails paymentsDetails = new PaymentsDetails(Name, ID);
                                                List<PaymentsDetails> paymentsDetailsList = playerTreasury.getPaymentsForPlayer();
                                                if (paymentsDetailsList == null)
                                                    paymentsDetailsList = new ArrayList<>();
                                                paymentsDetailsList.add(paymentsDetails);
                                                playerTreasury.setPaymentsForPlayer(paymentsDetailsList);
                                                databaseReference.child("playerTreasury").child(playerTreasury.getID()).setValue(playerTreasury);
                                            }
                                            Map<String, PlayerDetails>  map = new HashMap<>();
                                            for (PlayerDetails player : list)
                                            {
                                                map.put(player.name, player);

                                            }
                                            paymentActivity.setPlayers(map);
                                            databaseReference.child("paymentActivity").child(ID).setValue(paymentActivity);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else
                            {
                                databaseReference.child("paymentActivity").child(ID).setValue(paymentActivity);
                            }

                            PopUpAddPaymentActivity.this.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}