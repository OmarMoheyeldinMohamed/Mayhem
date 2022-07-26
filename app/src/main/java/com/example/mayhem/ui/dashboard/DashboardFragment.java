package com.example.mayhem.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayhem.ClickListener;
import com.example.mayhem.PaymentActivityAdapter;
import com.example.mayhem.Payment_activity;
import com.example.mayhem.PaymentsEditing;
import com.example.mayhem.PlayerTreasury;
import com.example.mayhem.PlayerTreasuryAdapter;
import com.example.mayhem.PopUpAddPaymentActivity;
import com.example.mayhem.PopUpAddPractice;
import com.example.mayhem.PopUpDeletePayment;
import com.example.mayhem.R;
import com.example.mayhem.databinding.FragmentDashboardBinding;
import com.example.mayhem.player_payments_list;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    //private FragmentDashboardBinding binding;

    List<Payment_activity> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PaymentActivityAdapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.payable_event_treasury, container, false);

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
                Payment_activity paymentActivity = list.get(index);
                Intent i = new Intent(root.getContext(), PopUpDeletePayment.class);
                i.putExtra("payment", paymentActivity);
                startActivity(i);
            }
            @Override
            public void click(int index){
                Intent intent = new Intent(root.getContext(), PaymentsEditing.class);
                intent.putExtra("payment", list.get(index));
                startActivity(intent);
            }
        };


        adapter
                = new PaymentActivityAdapter(
                list, root.getContext() ,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(root.getContext()));


        FloatingActionButton addPlayersBtn = (FloatingActionButton) root.findViewById(R.id.add_playersbtn);

        addPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(root.getContext(), PopUpAddPaymentActivity.class)));
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
        list.clear();
        databaseReference.child("paymentActivity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    return;
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Payment_activity paymentActivity = snapshot1.getValue(Payment_activity.class);
                    list.add(paymentActivity);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}