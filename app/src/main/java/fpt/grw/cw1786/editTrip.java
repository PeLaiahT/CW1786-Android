package fpt.grw.cw1786;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class editTrip extends AppCompatActivity {
    EditText name, date, description, destination, duration, transportation;
    Button edit, delete, addexpense;
    RadioButton yes, no;
    RadioGroup radioGroup;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        edit = findViewById(R.id.btnEdit);
        delete = findViewById(R.id.btnDelete);
        name = findViewById(R.id.txtName);
        date = findViewById(R.id.txtDate);
        date.setOnFocusChangeListener((view, b) -> {
            if(b){
                MainActivity.DatePickerFragment datePickerFragment = new MainActivity.DatePickerFragment();
                datePickerFragment.setDatepinput(date);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        description = findViewById(R.id.txtDescription);
        destination = findViewById(R.id.txtDestination);
        duration = findViewById(R.id.txtDuration);
        transportation = findViewById(R.id.txtTranspotation);
        radioGroup = findViewById(R.id.radioGroup);
        yes = findViewById(R.id.radioYes);
        no = findViewById(R.id.radioNo);
        listView = findViewById(R.id.listViewtrip);

        ImageButton home = findViewById(R.id.btnHome);
        home.setOnClickListener(view->{
            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent2);
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String nameEdit = intent.getStringExtra("name");
        String dateEdit = intent.getStringExtra("date");
        String descriptionEdit = intent.getStringExtra("description");
        String destinationEdit = intent.getStringExtra("destination");
        String durationEdit = intent.getStringExtra("duration");
        String transportationEdit = intent.getStringExtra("transportation");
        String riskassessmentEdit = intent.getStringExtra("riskassessment");
        name.setText(nameEdit);
        date.setText(dateEdit);
        description.setText(descriptionEdit);
        destination.setText(destinationEdit);
        duration.setText(durationEdit);
        transportation.setText(transportationEdit);
        if (riskassessmentEdit.equals("Yes")) {
            yes.setChecked(true);
            no.setChecked(false);
        }
        if (riskassessmentEdit.equals("No")) {
            no.setChecked(true);
            yes.setChecked(false);
        }
        delete.setOnClickListener(view -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.deleteTrip(id);
            Toast.makeText(this, "Delete sucessfull!", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(getApplicationContext(),view.class);
            startActivity(intent3);
        });
        edit.setOnClickListener(view -> {
            Trip newTrips = new Trip();
            if (!customValidation()) {
                return;
            }
            try {
                newTrips.id = id;
                newTrips.name = name.getText().toString();
                newTrips.date = date.getText().toString();
                newTrips.description = description.getText().toString();
                newTrips.destination = destination.getText().toString();
                newTrips.duration = duration.getText().toString();
                newTrips.transportation = transportation.getText().toString();
                newTrips.riskassessment = riskassessmentEdit;
                if(yes.isChecked()){
                    newTrips.riskassessment = "Yes";
                }else{
                    newTrips.riskassessment = "No";
                }
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.updateTrip(newTrips);
                Intent intent2 = new Intent(getApplicationContext(),view.class);
                startActivity(intent2);
            } catch (Exception e) {
                Toast.makeText(this, "Updating fail!", Toast.LENGTH_SHORT).show();
            }
        });

        addexpense = findViewById(R.id.btnExpense);
        addexpense.setOnClickListener(view->{
            Intent i = new Intent(this, addExpense.class);
            i.putExtra("trip_id",id);
            i.putExtra("trip_name",nameEdit);
            startActivity(i);
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Expense> expenses = dbHelper.getExpense(id);
        ArrayAdapter<Expense> arrayExpense = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, expenses);
        ListView listExpense = findViewById(R.id.listExpense);
        listExpense.setAdapter(arrayExpense);
    }

    private boolean customValidation() {
            int isSelected = radioGroup.getCheckedRadioButtonId();
            String Sname = name.getText().toString();
            String Sdate = date.getText().toString();
            String Sdestination = destination.getText().toString();
            if (Sname.isEmpty() || Sdate.isEmpty() || Sdestination.isEmpty()) {
                Toast.makeText(this, "Please input required information", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (isSelected == -1) {
                Toast.makeText(this, "You have not selected any of the risk assessment", Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(this, "Trip update sucessfull!", Toast.LENGTH_SHORT).show();
            return true;
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        public EditText getDatepinput() {
            return datepinput;
        }

        public void setDatepinput(EditText datepinput) {
            this.datepinput = datepinput;
        }

        EditText datepinput;
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(requireContext(),this, year,month,day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            datepinput.setText((String.valueOf(i2 + "/" + i1 + "/" + i)));
        }
    }
}