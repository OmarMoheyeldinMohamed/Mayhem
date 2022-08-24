package com.example.mayhem.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.mayhem.player_payments_list;
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

    //private FragmentHomeBinding binding;

    List<PlayerTreasury> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PlayerTreasuryAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.players_treasury, container, false);

        list =  new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressBar = root.findViewById(R.id.progressBar);

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
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child("playerTreasury").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.getValue() == null)
                {
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                for (DataSnapshot player : snapshot.getChildren())
                {
                    PlayerTreasury p = player.getValue(PlayerTreasury.class);
                    list.add(p);
                }
                list.sort(new Comparator<PlayerTreasury>() {
                    @Override
                    public int compare(PlayerTreasury o1, PlayerTreasury o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                list.sort(new Comparator<PlayerTreasury>() {
                    @Override
                    public int compare(PlayerTreasury o1, PlayerTreasury o2) {
                        Integer o1size = (o1.getPaymentsForPlayer() == null)? 0 :o1.getPaymentsForPlayer().size();
                        Integer o2size = (o2.getPaymentsForPlayer() == null)? 0 : o2.getPaymentsForPlayer().size();
                        return o2size.compareTo(o1size);
                    }
                });
                list.sort(new Comparator<PlayerTreasury>() {
                    @Override
                    public int compare(PlayerTreasury o1, PlayerTreasury o2) {
                        Integer i1 =o1.getAmountPaid();
                        Integer i2 = o2.getAmountPaid();
                        return i2.compareTo(i1);
                    }
                });
                list.sort(new Comparator<PlayerTreasury>() {
                    @Override
                    public int compare(PlayerTreasury o1, PlayerTreasury o2) {
                        Integer i1 =o1.getAmountOwed();
                        Integer i2 = o2.getAmountOwed();
                        return i1.compareTo(i2);
                    }
                });
                list.sort(new Comparator<PlayerTreasury>() {
                    @Override
                    public int compare(PlayerTreasury o1, PlayerTreasury o2) {
                        Boolean i1 =o1.getAmountOwed() <= 0;
                        Boolean i2 = o2.getAmountOwed() <= 0;
                        return i1.compareTo(i2);
                    }
                });
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}