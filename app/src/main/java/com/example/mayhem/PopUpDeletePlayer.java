package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class PopUpDeletePlayer extends AppCompatActivity {

    Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_delete_player);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.4));

        player = (Player) getIntent().getSerializableExtra("player");

        TextView textView = (TextView) findViewById(R.id.player_delete_name);
        textView.setText(player.getName() + "?");


        Button confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("players").child(player.getID()).removeValue();
                List<String> attended, missed;
                attended = player.getAttendedTrainings();
                missed = player.getMissedTrainings();
                if (attended == null)
                {
                    attended = new ArrayList<>();
                }
                if (missed == null)
                {
                    missed = new ArrayList<>();
                }


                for (String x : attended)
                {
                    databaseReference.child("practices").child(x).child("playersAttended").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            List<String> practiceAttendedList = snapshot.getValue(t);
                            practiceAttendedList.remove(player.getID());
                            databaseReference.child("practices").child(x).child("playersAttended").setValue(practiceAttendedList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
//                int missed_size = missed.size();
//                int i = 0;
//                if (missed_size == 0)
//                {
//
//                }
                for (String x : missed)
                {
//                    i++;
//                    int finalI = i;
                    databaseReference.child("practices").child(x).child("playersMissed").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            List<String> practiceMissedList = snapshot.getValue(t);
                            practiceMissedList.remove(player.getID());
                            databaseReference.child("practices").child(x).child("playersMissed").setValue(practiceMissedList);
//                            if (finalI == missed_size)
//                            {
//                                PopUpDeletePlayer.this.finish();
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                PopUpDeletePlayer.this.finish();

            }
        });
    }
}