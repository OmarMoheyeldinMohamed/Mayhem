package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpEditOwed extends AppCompatActivity {

    EditText owedEdit;

    PlayerDetails playerDetails;
    PlayerTreasury playerTreasury;
    DatabaseReference databaseReference;
    Payment_activity paymentActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_edit_owed);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.4));

        owedEdit = findViewById(R.id.owed_edit);

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




        Button save = findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int owed = Integer.parseInt(owedEdit.getText().toString());
                int oldowed = paymentActivity.getPlayers().get(playerTreasury.getName()).getAmountOwed();
                int difference = owed - oldowed;
                paymentActivity.getPlayers().get(playerTreasury.getName()).setAmountOwed(owed);
                int newowed = playerTreasury.getAmountOwed() + difference;
                databaseReference.child("paymentActivity").child(paymentActivity.getID()).child("players").child(playerDetails.getName())
                        .setValue(paymentActivity.getPlayers().get(playerTreasury.getName()));
                databaseReference.child("playerTreasury").child(playerTreasury.getID()).child("amountOwed").setValue(newowed);


                Intent gotoScreenVar = new Intent(PopUpEditOwed.this, PaymentsEditing.class);

                gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                gotoScreenVar.putExtra("payment", paymentActivity);
                startActivity(gotoScreenVar);

            }
        });
    }
}