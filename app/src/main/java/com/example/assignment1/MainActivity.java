package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Faculty faculty;
    private EditText editTextDOJ, editTextExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faculty = new Faculty();
        editTextDOJ = findViewById(R.id.editTextDOJ);
        editTextExperience = findViewById(R.id.editTextExperience);

        setupDesignationSpinner();
    }

    public void saveData(View view) {
        EditText editTextName = findViewById(R.id.editTextName);

        faculty.setName(editTextName.getText().toString());
        faculty.setDesignation(getSelectedDesignation());
        faculty.setGender(getSelectedGender());
        faculty.setDateOfJoining(editTextDOJ.getText().toString());
        faculty.setExperience(editTextExperience.getText().toString());

        if (faculty.getName().isEmpty() || faculty.getDateOfJoining().isEmpty()) {
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, secondActivity.class);
        intent.putExtra("faculty", faculty);
        startActivity(intent);
    }

    private String getSelectedGender() {
        RadioGroup genderGroup = findViewById(R.id.rbGroupGender);
        int selectedId = genderGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedGenderButton = findViewById(selectedId);
            return selectedGenderButton.getText().toString();
        }
        return "";
    }

    private String getSelectedDesignation() {
        Spinner spinnerDesignation = findViewById(R.id.spinnerDesignation);
        return spinnerDesignation.getSelectedItem().toString();
    }

    private void setupDesignationSpinner() {
        Spinner spinnerDesignation = findViewById(R.id.spinnerDesignation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.designations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesignation.setAdapter(adapter);
    }

    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = day + "/" + (month + 1) + "/" + year;
        editTextDOJ.setText(date);

        // Calculate experience
        try {
            calculateExperience(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void calculateExperience(String dateOfJoining) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date joiningDate = sdf.parse(dateOfJoining);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        if (joiningDate != null && joiningDate.before(new Date())) {
            startCalendar.setTime(joiningDate);
            endCalendar.setTime(new Date());

            int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            int days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);

            if (days < 0) {
                // Borrow days from the previous month
                endCalendar.add(Calendar.MONTH, -1);
                days += endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                months--;
            }

            if (months < 0) {
                // Borrow months from the previous year
                months += 12;
                years--;
            }

            String experience = years + " years, " + months + " months, " + days + " days";
            editTextExperience.setText(experience);
        } else {
            Toast.makeText(this, "Invalid Date of Joining", Toast.LENGTH_SHORT).show();
            editTextExperience.setText("");
        }
    }
}