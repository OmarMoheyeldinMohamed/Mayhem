package com.example.mayhem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayhem.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    Attendence_Adapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    List<Attendence_Data> list;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<Player> playersList;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list =  new ArrayList<>();
        playersList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //refresh();

        recyclerView
                = (RecyclerView)root.findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click2(int index)
            {
                int i;
                for (i = 0; i< list.size(); i++)
                {
                    if (list.get(index).getName().equals(playersList.get(i).getName()))
                        break;
                }
                Intent intent = new Intent(root.getContext(), PopUpDeletePlayer.class);
                intent.putExtra("player", playersList.get(i));
                startActivity(intent);
            }
            @Override
            public void click(int index){
                //Toast.makeText(root.getContext(),  "clicked item index is "+index,Toast.LENGTH_SHORT).show();

            }
        };
        adapter
                = new Attendence_Adapter(
                list, root.getContext() ,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(root.getContext()));




        FloatingActionButton addPlayersBtn = (FloatingActionButton) root.findViewById(R.id.add_playersbtn);

        addPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(root.getContext(), PopUpAddPlayer.class)));
            }
        });

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();
        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refresh()
    {
//        list =  new ArrayList<>();
//        playersList = new ArrayList<>();

        databaseReference.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
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