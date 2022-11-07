package fpt.grw.cw1786;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class addExpense extends AppCompatActivity {
    Button save,cancel;
    EditText type,amount,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        type = findViewById(R.id.txtType);
        amount = findViewById(R.id.txtAmount);
        day = findViewById(R.id.txtDay);
        day.setOnFocusChangeListener((view, b) -> {
            if(b){
                MainActivity.DatePickerFragment datePickerFragment = new MainActivity.DatePickerFragment();
                datePickerFragment.setDatepinput(day);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        Intent intent = getIntent();
        int trip_id = intent.getIntExtra("trip_id", 0);

        save = findViewById(R.id.btnSaveExpense);
        save.setOnClickListener(view -> {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            if(!customValidation()){
                return;
            }else{
                long result = dbHelper.insertExpense(trip_id,type.getText().toString(),amount.getText().toString(),day.getText().toString());
                if(result>0){
                    Intent intent2 = new Intent(getApplicationContext(),view.class);
                    startActivity(intent2);
                    Toast.makeText(this, "Adding successfull", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Adding fail!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel = findViewById(R.id.btnCancel);
        cancel.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(),view.class);
            startActivity(intent1);
        });

    }
    public boolean customValidation() {
        String Stype = type.getText().toString();
        String Samount = amount.getText().toString();
        String Sday = day.getText().toString();
        if(Stype.isEmpty() || Samount.isEmpty() || Sday.isEmpty()){
            Toast.makeText(this,"Please input required information",Toast.LENGTH_SHORT).show();
            return false;
        }
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