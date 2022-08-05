package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopUpAddPlayer extends AppCompatActivity {

    EditText nameEdit;
    List<String> PlayerMissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_add_player);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.35));

        nameEdit = (EditText) findViewById(R.id.name_edit);
        PlayerMissed = new ArrayList<>();

        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = nameEdit.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(PopUpAddPlayer.this, "Name field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Player player = new Player(Name, null, null);
                PlayerTreasury playerTreasury = new PlayerTreasury(Name, "", 0, 0);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                databaseReference.child(values.player_training).orderByChild("name").equalTo(Name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Player p = snapshot.getValue(Player.class);
                        if (snapshot.getValue() != null) {
                            Toast.makeText(PopUpAddPlayer.this, "Player name already exists!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            String ID = databaseReference.child(values.player_training).push().getKey();
                            player.setID(ID);
                            playerTreasury.setID(ID);


                            databaseReference.child("playerTreasury").child(ID).setValue(playerTreasury);
                            PlayerMissed.clear();


                            databaseReference.child("practices").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                                    {
                                        Training practice = snapshot1.getValue(Training.class);
                                        List<String> missed = practice.getPlayersMissed();
                                        if (missed == null)
                                        {
                                            missed = new ArrayList<>();
                                        }
                                        missed.add(ID);
                                        databaseReference.child("practices").child(snapshot1.getKey()).child("playersMissed")
                                                .setValue(missed);
                                        PlayerMissed.add(practice.getID());

                                    }
                                    player.setMissedTrainings(PlayerMissed);
                                    databaseReference.child(values.player_training).child(ID).setValue(player);


                                    //players_attendence_list activity = (players_attendence_list) PopUpAddPlayer.this.getCallingActivity();
                                    PopUpAddPlayer.this.finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

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