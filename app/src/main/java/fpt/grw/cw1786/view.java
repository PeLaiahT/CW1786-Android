package fpt.grw.cw1786;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class view extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private ListView listView;
    private ArrayList<Trip> trips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        searchTrip();
       Button deleteAll = findViewById(R.id.btnDeleteAll);
       ImageButton home = findViewById(R.id.btnHome);
       home.setOnClickListener(view->{
           Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
           startActivity(intent2);
       });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Trip> trips = dbHelper.getTrip();
        ArrayAdapter<Trip> arrayAdapter = new ArrayAdapter<Trip>(this, android.R.layout.simple_list_item_1, trips);
        ListView listView = findViewById(R.id.listViewtrip);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                try{
                    Trip selectedTrip = trips.get(i);
                    openDetail(selectedTrip);
                }catch (Exception e){
                    e.printStackTrace();
                }
        });
        deleteAll.setOnClickListener(view -> {
            long result = dbHelper.deleteAlltrip();
            if(result>0){
                Toast.makeText(this, "All trips has been deleted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),view.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Deleting Fail!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDetail(Trip selectedTrip) {
        Intent intent = new Intent(this, editTrip.class);
        intent.putExtra("id",selectedTrip.getId());
        intent.putExtra("name",selectedTrip.getName());
        intent.putExtra("date",selectedTrip.getDate());
        intent.putExtra("description",selectedTrip.getDescription());
        intent.putExtra("destination",selectedTrip.getDestination());
        intent.putExtra("duration",selectedTrip.getDuration());
        intent.putExtra("transportation",selectedTrip.getTransportation());
        intent.putExtra("riskassessment",selectedTrip.getRiskassessment());
        startActivity(intent);
    }
    private void searchTrip() {
        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                dbHelper = new DatabaseHelper(view.this);
                trips = dbHelper.getTrip();

                ArrayList<Trip> filterTrip = new ArrayList<>();

                for(Trip trip: trips){
                    if(trip.getName().toLowerCase().contains(s.toLowerCase())){
                        filterTrip.add(trip);
                    }

                }
                listView = findViewById(R.id.listViewtrip);
                ArrayAdapter<Trip> adapter = new ArrayAdapter<Trip>(view.this, android.R.layout.simple_list_item_1, filterTrip);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    try{
                        Trip selectedTrip = filterTrip.get(i);
                        openDetail(selectedTrip);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

                return false;
            }
        });
    }
}