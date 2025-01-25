package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class secondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Faculty faculty = (Faculty) getIntent().getSerializableExtra("faculty");

        TextView textViewDetails = findViewById(R.id.textViewDetails);
        textViewDetails.setText("Name: " + faculty.getName() + "\n" +
                "Designation: " + faculty.getDesignation() + "\n" +
                "Gender: " + faculty.getGender() + "\n" +
                "Date of Joining: " + faculty.getDateOfJoining() + "\n" +
                "Experience: " + faculty.getExperience());
    }
}