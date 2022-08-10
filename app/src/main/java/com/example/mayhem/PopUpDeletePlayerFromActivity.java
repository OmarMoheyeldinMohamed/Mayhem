package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class PopUpDeletePlayerFromActivity extends AppCompatActivity {

    PlayerDetails playerDetails;
    PlayerTreasury playerTreasury;
    DatabaseReference databaseReference;
    Payment_activity paymentActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_delete_player_from);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.4));


        playerDetails = (PlayerDetails) getIntent().getSerializableExtra("player");
        paymentActivity = (Payment_activity) getIntent().getSerializableExtra("payment");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("playerTreasury").child(playerDetails.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                playerTreasury = snapshot.getValue(PlayerTreasury.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        TextView textView = (TextView) findViewById(R.id.player_delete_name);
        textView.setText(playerDetails.getName() + "?");

        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int paid = paymentActivity.getPlayers().get(playerTreasury.getName()).getAmountPaid();
                int owed = paymentActivity.getPlayers().get(playerTreasury.getName()).getAmountOwed();
                playerTreasury.setAmountOwed(playerDetails.getAmountOwed()-owed);
                playerTreasury.setAmountPaid(playerDetails.getAmountPaid()-paid);
                paymentActivity.getPlayers().remove(playerTreasury.getName());
                int i =0;
                for (i = 0; i < playerTreasury.getPaymentsForPlayer().size(); i++)
                    if (playerTreasury.getPaymentsForPlayer().get(i).getID().equals(paymentActivity.getID()))
                        break;
                playerTreasury.getPaymentsForPlayer().remove(i);
                databaseReference.child("playerTreasury").child(playerTreasury.getID()).setValue(playerTreasury);
                databaseReference.child("paymentActivity").child(paymentActivity.getID()).child("players")
                        .child(playerTreasury.getName()).removeValue();

                Intent gotoScreenVar = new Intent(PopUpDeletePlayerFromActivity.this, PaymentsEditing.class);

                gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                gotoScreenVar.putExtra("payment", paymentActivity);
                startActivity(gotoScreenVar);
            }
        });

    }
}