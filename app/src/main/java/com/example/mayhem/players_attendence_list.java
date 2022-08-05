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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class players_attendence_list extends AppCompatActivity {

    Attendence_Adapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    List<Attendence_Data> list;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<Player> playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_attendence_list);

        list =  new ArrayList<>();
        playersList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //refresh();

        recyclerView
                = (RecyclerView)findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click2(int index)
            {
                int i;
                for (i = 0; i< list.size(); i++)
                {
                    if (list.get(i).getName().equals(playersList.get(i).getName()))
                        break;
                }
                Intent intent = new Intent(players_attendence_list.this, PopUpDeletePlayer.class);
                intent.putExtra("player", playersList.get(i));
                startActivity(intent);
            }
            @Override
            public void click(int index){
                Toast.makeText(players_attendence_list.this, "clicked item index is "+index,Toast.LENGTH_SHORT).show();

            }
        };
        adapter
                = new Attendence_Adapter(
                list, this,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));




        FloatingActionButton addPlayersBtn = (FloatingActionButton) findViewById(R.id.add_playersbtn);

        addPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(players_attendence_list.this, PopUpAddPlayer.class)));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        // Make sure the request was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refresh();
            // The user picked a contact.
            // The Intent's data Uri identifies which contact was selected.

            // Do something with the contact here (bigger example below)
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        refresh();
    }


    public void refresh()
    {
//        list =  new ArrayList<>();
//        playersList = new ArrayList<>();

        databaseReference.child(values.player_training).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                list.clear();
                playersList.clear();
                for (DataSnapshot player_snapshot : snapshot.getChildren())
                {
                    Player player = player_snapshot.getValue(Player.class);
                    playersList.add(player);
                    String Name, ID;
                    List<String> attended, missed;
                    Name = player.getName();
                    attended = player.getAttendedTrainings();
                    missed = player.getMissedTrainings();
                    int numAttended, numMissed;
                    if (attended == null)
                    {
                        numAttended = 0;
                    }
                    else
                    {
                        numAttended = attended.size();
                    }
                    if (missed == null)
                    {
                        numMissed = 0;
                    }
                    else
                    {
                        numMissed = missed.size();
                    }
                    Attendence_Data data = new Attendence_Data(Name, String.valueOf(numAttended), String.valueOf(numMissed));
                    list.add(data);

                }

                list.sort(new Comparator<Attendence_Data>() {
                    @Override
                    public int compare(Attendence_Data o1, Attendence_Data o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                list.sort(new Comparator<Attendence_Data>() {
                    @Override
                    public int compare(Attendence_Data lhs, Attendence_Data rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        int lhsNum = Integer.parseInt(lhs.getAttended());
                        int rhsNum = Integer.parseInt(rhs.getAttended());
                        if (lhsNum > rhsNum)
                            return -1;
                        else if (lhsNum < rhsNum)
                            return 1;
                        return 0;
                    }
                });
                //adapter.list = list;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}