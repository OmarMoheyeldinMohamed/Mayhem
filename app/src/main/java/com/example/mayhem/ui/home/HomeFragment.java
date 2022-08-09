package com.example.mayhem.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayhem.Attendence_Adapter;
import com.example.mayhem.ClickListener;
import com.example.mayhem.PlayerTreasury;
import com.example.mayhem.PlayerTreasuryAdapter;
import com.example.mayhem.PopUpAddPlayer;
import com.example.mayhem.PopUpDeletePlayer;
import com.example.mayhem.R;
import com.example.mayhem.databinding.FragmentHomeBinding;
import com.example.mayhem.player_payments_list;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //private FragmentHomeBinding binding;

    List<PlayerTreasury> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PlayerTreasuryAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.players_treasury, container, false);

        list =  new ArrayList<>();

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

                Intent intent = new Intent(root.getContext(), PopUpDeletePlayer.class);
                intent.putExtra("player", list.get(index));
                startActivity(intent);
            }
            @Override
            public void click(int index){
                Intent intent = new Intent(root.getContext(), player_payments_list.class);
                intent.putExtra("player", list.get(index));
                startActivity(intent);
                //Toast.makeText(root.getContext(),  "clicked item index is "+index,Toast.LENGTH_SHORT).show();

            }
        };



        adapter
                = new PlayerTreasuryAdapter(
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
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    @Override
    public void onResume() {

        super.onResume();
        refresh();
    }

    public void refresh()
    {
        databaseReference.child("playerTreasury").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.getValue() == null)
                    return;
                for (DataSnapshot player : snapshot.getChildren())
                {
                    PlayerTreasury p = player.getValue(PlayerTreasury.class);
                    list.add(p);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}