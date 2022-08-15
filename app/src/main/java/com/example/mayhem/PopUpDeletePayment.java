package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpDeletePayment extends AppCompatActivity {

    Payment_activity paymentActivity;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_delete_payment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.4));

        paymentActivity = (Payment_activity) getIntent().getSerializableExtra("payment");

        TextView name = (TextView) findViewById(R.id.payment_delete_name);

        name.setText(paymentActivity.getActivityName());
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentActivity.getPlayers() == null)
                {
                    databaseReference.child("paymentActivity").child(paymentActivity.getID()).removeValue();
                }
                else
                {
                    for (PlayerDetails playerDetails : paymentActivity.getPlayers().values())
                    {
                        databaseReference.child("playerTreasury").child(playerDetails.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                PlayerTreasury playerTreasury = snapshot.getValue(PlayerTreasury.class);
                                int paid = paymentActivity.getPlayers().get(playerTreasury.getName()).getAmountPaid();
                                int owed = paymentActivity.getPlayers().get(playerTreasury.getName()).getAmountOwed();
                                playerTreasury.setAmountOwed(playerTreasury.getAmountOwed()-owed);
                                playerTreasury.setAmountPaid(playerTreasury.getAmountPaid()-paid);
                                int i =0;
                                for (i = 0; i < playerTreasury.getPaymentsForPlayer().size(); i++)
                                    if (playerTreasury.getPaymentsForPlayer().get(i).getID().equals(paymentActivity.getID()))
                                        break;
                                playerTreasury.getPaymentsForPlayer().remove(i);
                                databaseReference.child("playerTreasury").child(playerTreasury.getID()).setValue(playerTreasury);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    databaseReference.child("paymentActivity").child(paymentActivity.getID()).removeValue();
                }
                PopUpDeletePayment.this.finish();
            }
        });


    }
}