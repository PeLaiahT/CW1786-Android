package fpt.grw.cw1786;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText name,date,description,destination,duration,transportation;
    Button save,view;
    RadioButton yes,no;
    RadioGroup radioGroup;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save = findViewById(R.id.btnSave);
        view = findViewById(R.id.btnView);
        name = findViewById(R.id.txtName);
        date = findViewById(R.id.txtDate);
        date.setOnFocusChangeListener((view, b) -> {
            if(b){
                DatePickerFragment datePickerFragment = new DatePickerFragment();
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



        save.setOnClickListener(view -> {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            if(!customValidation()){
                return;
            }
            try{
                    if(yes.isChecked()){
                        dbHelper.insertTrip(name.getText().toString(),date.getText().toString(),description.getText().toString(),destination.getText().toString(),duration.getText().toString(),
                                transportation.getText().toString(),"Yes"
                        );
                    } else {
                        dbHelper.insertTrip(name.getText().toString(),date.getText().toString(),description.getText().toString(),destination.getText().toString(),duration.getText().toString(),
                                transportation.getText().toString(),"No"
                        );
                }
            }catch(Exception e){
                Toast.makeText(this, "Adding fail!", Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),view.class);
            startActivity(intent);
        });
    }

    public boolean customValidation() {
        int isSelected = radioGroup.getCheckedRadioButtonId();
        String Sname = name.getText().toString();
        String Sdate = date.getText().toString();
        String Sdestination = destination.getText().toString();
        if(Sname.isEmpty() || Sdate.isEmpty() || Sdestination.isEmpty()){
            Toast.makeText(MainActivity.this,"Please input required information",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isSelected == -1){
            Toast.makeText(MainActivity.this,"You have not selected any of the risk assessment",Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(this, "Trip added sucessfull!", Toast.LENGTH_SHORT).show();
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