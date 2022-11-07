package fpt.grw.cw1786;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trips";
    private static final String TABLE_TRIP = "trips";
    public static final String TRIP_ID = "trip_id";
    public static final String NAME_COLUMN = "name";
    public static final String DATE_COLUMN = "date";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DESTINATION_COLUMN = "destination";
    public static final String DURATION_COLUMN = "duration";
    public static final String TRANSPORTATION_COLUMN = "transportation";
    public static final String RISKASSESSMENT_COLUMN = "riskassessment";

    private static final String TABLE_EXPENSE = "expenses";
    public static final String EXPENSE_ID = "expense_id";
    public static final String TYPE_COLUMN = "type";
    public static final String AMOUNT_COLUMN = "amount";
    public static final String DAY_COLUMN = "day";

    private SQLiteDatabase database;

    private static final String TRIP_TABLE_CREATE = String.format(
              "CREATE TABLE %s (" +
              "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
              "   %s TEXT, " +
              "   %s TEXT, " +
              "   %s TEXT, " +
              "   %s TEXT, " +
              "   %s TEXT, " +
                      "   %s TEXT, " +
                      "   %s TEXT)",
            TABLE_TRIP, TRIP_ID, NAME_COLUMN, DATE_COLUMN, DESCRIPTION_COLUMN,DESTINATION_COLUMN,DURATION_COLUMN,TRANSPORTATION_COLUMN,RISKASSESSMENT_COLUMN);
    private static final String EXPENSE_TABLE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s INTEGER, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            TABLE_EXPENSE,EXPENSE_ID,TRIP_ID,TYPE_COLUMN, AMOUNT_COLUMN, DAY_COLUMN);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRIP_TABLE_CREATE);
        db.execSQL(EXPENSE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);

        Log.v(this.getClass().getName(), TABLE_TRIP + " database upgrade to version " +
                newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertTrip(String name, String date, String description,String destination,String duration, String transportation, String riskassessment) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(NAME_COLUMN, name);
        rowValues.put(DATE_COLUMN, date);
        rowValues.put(DESCRIPTION_COLUMN, description);
        rowValues.put(DESTINATION_COLUMN, destination);
        rowValues.put(DURATION_COLUMN, duration);
        rowValues.put(TRANSPORTATION_COLUMN, transportation);
        rowValues.put(RISKASSESSMENT_COLUMN, riskassessment);
        return database.insertOrThrow(TABLE_TRIP, null, rowValues);
    }
    public long insertExpense(int tripId, String type,String amount,String day){
        ContentValues rowValues = new ContentValues();
        rowValues.put(TRIP_ID, tripId);
        rowValues.put(TYPE_COLUMN, type);
        rowValues.put(AMOUNT_COLUMN, amount);
        rowValues.put(DAY_COLUMN, day);
        return database.insertOrThrow(TABLE_EXPENSE,null, rowValues);
    }
    public long updateTrip(Trip trip) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(NAME_COLUMN, trip.getName());
        rowValues.put(DATE_COLUMN, trip.getDate());
        rowValues.put(DESCRIPTION_COLUMN, trip.getDescription());
        rowValues.put(DESTINATION_COLUMN, trip.getDestination());
        rowValues.put(DURATION_COLUMN, trip.getDuration());
        rowValues.put(TRANSPORTATION_COLUMN, trip.getTransportation());
        rowValues.put(RISKASSESSMENT_COLUMN, trip.getRiskassessment());
        return database.update(TABLE_TRIP, rowValues, "TRIP_ID=?",new String[]{trip.getId() + ""});
    }
    public long deleteTrip(int tripId){
        return database.delete(TABLE_TRIP,"TRIP_ID=?", new String[]{tripId +""});
    }
    public long deleteAlltrip(){
        return database.delete(TABLE_TRIP,null,null);
    }
    public ArrayList<Trip> getTrip() {
        Cursor cursor = database.query(TABLE_TRIP, new String[]{TRIP_ID, NAME_COLUMN, DATE_COLUMN, DESCRIPTION_COLUMN, DESTINATION_COLUMN,DURATION_COLUMN,TRANSPORTATION_COLUMN,RISKASSESSMENT_COLUMN},
                null, null, null, null, TRIP_ID);

        ArrayList<Trip> results = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String description = cursor.getString(3);
            String destination = cursor.getString(4);
            String duration = cursor.getString(5);
            String transportation = cursor.getString(6);
            String riskassessment = cursor.getString(7);
            Trip trip = new Trip();
            trip.setName(name);
            trip.setDate(date);
            trip.setId(id);
            trip.setDescription(description);
            trip.setDestination(destination);
            trip.setDuration(duration);
            trip.setTransportation(transportation);
            trip.setRiskassessment(riskassessment);

            results.add(trip);
            cursor.moveToNext();
        }
        return results;
    }
    public ArrayList<Expense> getExpense(int id){
        String MY_QUERY = "SELECT b.expense_id, b.trip_id, a.name,b.type,b.amount,b.day FROM "+ TABLE_TRIP+ " a INNER JOIN "+
                TABLE_EXPENSE + " b ON a.trip_id=b.trip_id WHERE a.trip_id=?";
        Cursor cursor = database.rawQuery(MY_QUERY,new String[]{String.valueOf(id)});

        ArrayList<Expense> results = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int expense_id = cursor.getInt(0);
            int trip_id = cursor.getInt(1);
            String name = cursor.getString(2);
            String type = cursor.getString(3);
            String amount = cursor.getString(4);
            String day = cursor.getString(5);
            Expense expense = new Expense();
            expense.setExpense_Id(expense_id);
            expense.setTrip_Id(trip_id);
            expense.setTrip_name(name);
            expense.setType(type);
            expense.setAmount(amount);
            expense.setDay(day);

            results.add(expense);
            cursor.moveToNext();
        }
        return results;
    }
}
