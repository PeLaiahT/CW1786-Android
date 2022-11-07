package fpt.grw.cw1786;

public class Expense {
    private int trip_Id;

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    private String trip_name;
    private int expense_Id;
    private String type;

    public int getTrip_Id() {
        return trip_Id;
    }

    public void setTrip_Id(int trip_Id) {
        this.trip_Id = trip_Id;
    }

    public int getExpense_Id() {
        return expense_Id;
    }

    public void setExpense_Id(int expense_Id) {
        this.expense_Id = expense_Id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String amount;
    private String day;

    @Override
    public String toString() {
        return "Expense{" +
                "name=" + type +
                ", amount='" + amount + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
