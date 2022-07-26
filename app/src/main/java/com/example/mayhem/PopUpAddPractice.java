package com.example.mayhem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PopUpAddPractice extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    String DateString;

    int Year, Month, Day;

    List<String> playersList;

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        return makeDateString(day, month, year, dayWeek);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_add_practice);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int) (height*0.35));

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        playersList = new ArrayList<>();

        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("practices").orderByChild("date").equalTo(DateString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null)
                        {
                            Toast.makeText(PopUpAddPractice.this, "There is already practice on that date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String ID = databaseReference.child("practices").push().getKey();


                        databaseReference.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                playersList.clear();
                                for (DataSnapshot players : snapshot.getChildren())
                                {
                                    Player player = players.getValue(Player.class);
                                    String playerID = player.getID();
                                    playersList.add(playerID);
                                    List<String> missed = player.getMissedTrainings();
                                    if (missed == null)
                                    {
                                        missed = new ArrayList<>();
                                    }
                                    missed.add(ID);
                                    player.setMissedTrainings(missed);
                                    databaseReference.child("players").child(players.getKey()).setValue(player);

                                }
                                Training training = new Training(ID,DateString, Day, Month, Year, null, playersList);
                                databaseReference.child("practices").child(ID).setValue(training);
                                PopUpAddPractice.this.finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                Year = year;
                Month = month;
                Day = day;
                Calendar cal = new GregorianCalendar(year, month-1, day);
                int DayWeek = cal.get(Calendar.DAY_OF_WEEK);
                String date = makeDateString(day, month, year, DayWeek);
                DateString = date;
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH)+1;
        Day = cal.get(Calendar.DAY_OF_MONTH);
        int DayWeek = cal.get(Calendar.DAY_OF_WEEK);

        DateString = makeDateString(Day, Month, Year, DayWeek);
        datePickerDialog = new DatePickerDialog(this, dateSetListener, Year, Month-1, Day);
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

    }

    private String getDayofWeek(int day)
    {
        switch (day)
        {
            case 1:return "Sunday";
            case 2: return  "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5 : return "Thursday";
            case 6: return "Friday";
            case 7 : return "Saturday";
            default: return "Error";

        }
    }
    private String makeDateString(int day, int month, int year, int dayWeek)
    {
        String DayWeekString = getDayofWeek(dayWeek);
        return DayWeekString + ", " +getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }


}