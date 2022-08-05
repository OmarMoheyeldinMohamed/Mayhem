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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PopUpDeleteTraining extends AppCompatActivity {

    Training training;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_delete_training);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.35));

        training = (Training) getIntent().getSerializableExtra("training");

        TextView textView = (TextView) findViewById(R.id.delete_confirmation_text);
        textView.setText("Are you sure you want to delete training on " + training.getDate() + "?");


        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("practices").child(training.getID()).removeValue();

                List<String> playersAttended, playersMissed;
                playersAttended = training.getPlayersAttended();
                playersMissed = training.getPlayersMissed();

                if (playersAttended == null)
                    playersAttended = new ArrayList<>();
                if (playersMissed == null)
                    playersMissed = new ArrayList<>();

                for (String x : playersAttended)
                {
                    databaseReference.child(values.player_training).child(x).child("attendedTrainings").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            List<String> playerAttendedList = snapshot.getValue(t);
                            playerAttendedList.remove(training.getID());
                            databaseReference.child(values.player_training).child(x).child("attendedTrainings").setValue(playerAttendedList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                for (String x : playersMissed)
                {
                    databaseReference.child(values.player_training).child(x).child("missedTrainings").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            List<String> playerMissedList = snapshot.getValue(t);
                            playerMissedList.remove(training.getID());
                            databaseReference.child(values.player_training).child(x).child("missedTrainings").setValue(playerMissedList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                PopUpDeleteTraining.this.finish();


            }
        });
    }
}