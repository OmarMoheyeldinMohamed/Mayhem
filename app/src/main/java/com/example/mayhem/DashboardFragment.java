package com.example.mayhem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayhem.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    Attendence_Adapter adapter;
    RecyclerView recyclerView;
    ClickListener listener;

    List<Attendence_Data> list;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    ProgressBar progressBar;


    List<Training> practiceList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = new ArrayList<>();
        practiceList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressBar = root.findViewById(R.id.progressBar);

        recyclerView
                = (RecyclerView) root.findViewById(
                R.id.recyclerView);
        listener = new ClickListener() {
            @Override
            public void click2(int index)
            {
                int i;
                for (i = 0; i<list.size(); i++)
                {
                    if (list.get(index).getName().equals(practiceList.get(i).getDate()))
                        break;
                }
                Intent intent = new Intent(root.getContext(), PopUpDeleteTraining.class);

                intent.putExtra("training", practiceList.get(i));
                startActivity(intent);
            }
            @Override
            public void click(int index){
                String PracticeDate = list.get(index).Name;
                int i;
                for (i =0; i< practiceList.size(); i++)
                {
                    if (practiceList.get(i).getDate().equals(PracticeDate))
                        break;
                }
                Intent intent = new Intent(root.getContext(), attendence_list_per_training.class);
                intent.putExtra("practice", practiceList.get(i));
                startActivity(intent);

            }
        };
        adapter
                = new Attendence_Adapter(
                list, root.getContext(), listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(root.getContext()));




        FloatingActionButton addPlayersBtn = (FloatingActionButton) root.findViewById(R.id.add_playersbtn);

        addPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(root.getContext(), PopUpAddPractice.class)));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {


        super.onResume();
        refresh();
    }

    public void refresh()
    {
//        list =  new ArrayList<>();
//        playersList = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);


        databaseReference.child("practices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                list.clear();
                practiceList.clear();
                for (DataSnapshot training_snapshot : snapshot.getChildren())
                {
                    Training training = training_snapshot.getValue(Training.class);
                    practiceList.add(training);
                    String Date, ID;
                    List<String> attended, missed;
                    Date = training.getDate();
                    attended = training.getPlayersAttended();
                    missed = training.getPlayersMissed();
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
                    Attendence_Data data = new Attendence_Data(Date, String.valueOf(numAttended), String.valueOf(numMissed));
                    int Day, Month, Year;
                    Day = training.getDay();
                    Month = training.getMonth();
                    Year = training.getYear();
                    data.setDay(Day);
                    data.setMonth(Month);
                    data.setYear(Year);
                    list.add(data);

                }
                list.sort(new Comparator<Attendence_Data>() {
                    @Override
                    public int compare(Attendence_Data lhs, Attendence_Data rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        Calendar lhsCal = new GregorianCalendar(lhs.getYear(), lhs.getMonth(), lhs.getDay());
                        Calendar rhsCal = new GregorianCalendar(rhs.getYear(), rhs.getMonth(), rhs.getDay());
                        return lhsCal.compareTo(rhsCal);
                    }
                });
                //adapter.list = list;
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}