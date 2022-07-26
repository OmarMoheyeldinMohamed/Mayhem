package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class attendence_list_per_training extends AppCompatActivity {

    Training practice;
    List<Player> playersList, changedPlayers;

    Attendence_List_Adapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    List<Attendence_list> list;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_list_per_training);

        practice = (Training) getIntent().getSerializableExtra("practice");

        playersList = new ArrayList<>();
        list = new ArrayList<>();
        changedPlayers =  new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Button save = (Button) findViewById(R.id.savebtn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("practices").child(practice.getID()).setValue(practice);
                for (Player x : changedPlayers)
                {
                    databaseReference.child("players").child(x.getID()).setValue(x);
                }
                attendence_list_per_training.this.finish();
            }
        });
        refresh();
        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click(int index, boolean choice){
                if (choice == true)
                {
                    List<String> attended, missed;
                    attended = playersList.get(index).getAttendedTrainings();
                    if (attended == null)
                        attended = new ArrayList<>();
                    attended.add(practice.getID());
                    missed = playersList.get(index).getMissedTrainings();
                    if (missed == null)
                        missed = new ArrayList<>();
                    missed.remove(missed.indexOf(practice.getID()));
                    playersList.get(index).setAttendedTrainings(attended);
                    playersList.get(index).setMissedTrainings(missed);
                    List<String> playersAttended = practice.getPlayersAttended();
                    if (playersAttended == null)
                        playersAttended = new ArrayList<>();
                    playersAttended.add(playersList.get(index).getID());
                    List<String> playersMissed = practice.getPlayersMissed();
                    if (playersMissed == null)
                        playersMissed = new ArrayList<>();
                    playersMissed.remove(playersList.get(index).getID());

                    practice.setPlayersAttended(playersAttended);
                    practice.setPlayersMissed(playersMissed);
                }
                else
                {
                    List<String> attended, missed;
                    attended = playersList.get(index).getAttendedTrainings();
                    attended.remove(practice.getID());
                    if (attended == null)
                        attended = new ArrayList<>();
                    missed = playersList.get(index).getMissedTrainings();
                    if (missed == null)
                        missed = new ArrayList<>();
                    missed.add(practice.getID());
                    playersList.get(index).setAttendedTrainings(attended);
                    playersList.get(index).setMissedTrainings(missed);
                    List<String> playersAttended = practice.getPlayersAttended();
                    if (playersAttended == null)
                        playersAttended = new ArrayList<>();
                    playersAttended.remove(playersList.get(index).getID());

                    List<String> playersMissed = practice.getPlayersMissed();
                    if (playersMissed == null)
                        playersMissed = new ArrayList<>();
                    playersMissed.add(playersList.get(index).getID());

                    practice.setPlayersAttended(playersAttended);
                    practice.setPlayersMissed(playersMissed);
                }
                if (!changedPlayers.contains(playersList.get(index)))
                {
                    changedPlayers.add(playersList.get(index));
                }


            }
        };
        adapter
                = new Attendence_List_Adapter(
                list, this, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

    }

    public void refresh()
    {
        playersList.clear();
        list.clear();
        databaseReference.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot p : snapshot.getChildren())
                {
                    Player player = p.getValue(Player.class);
                    playersList.add(player);
                }

                playersList.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (Player x : playersList)
                {
                    boolean attended = true;
                    if (x.getAttendedTrainings() == null || x.getAttendedTrainings().indexOf(practice.getID()) == -1)
                        attended = false;
                    list.add(new Attendence_list(x.getName(), attended));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}