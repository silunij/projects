package model;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

public class ExpenseTracker implements Writable {
    // this class maintains a list of expenses and performs calculations with those
    // expenses
    private static int DailyLimit = 30;
    private static int MonthlyLimit = 30 * 30;
    private ArrayList<Expense> expenses;

    // EFFECTS : Constructs a new ExpenseTracker
    public ExpenseTracker() {
        expenses = new ArrayList<>();
    }

    // EFFECTS : returns a list of expenses
    public ArrayList<Expense> getExpenses() {
        return this.expenses;
    }

    // modifies : this
    // EFFECTS: Adds an expense to the list of expenses.
    public void addExpense(Expense e) {
        expenses.add(e);
        EventLog.getInstance().logEvent(new Event("Expense added to expenseTracker"));
    }

    // returns the total expenses in a given day
    public int calcDailyTotal(LocalDate date) {
        int total = 0;
        for (Expense e : expenses) {

            if (date.equals(e.getDate())) {
                total += e.getAmount();

            }
        }
        return total;
    }

    // returns the total expenses in a given month.
    public int calcMonthlyTotal(int month) {
        int total = 0;

        for (Expense e : expenses) {
            LocalDate d = e.getDate();
            if (d.getMonthValue() == month) {
                total += e.getAmount();
            }
        }
        return total;
    }

    // EFFECTS: returns the day of the month with the highest spending
    public LocalDate getDayWithHighestSpending(int month) {
        LocalDate maxDate = null;
        int maxTotal = 0;
        for (Expense e : expenses) {
            LocalDate date = e.getDate();
            if (month == date.getMonthValue()) {
                if (calcDailyTotal(date) > maxTotal) {
                    maxTotal = calcDailyTotal(date);
                    maxDate = date;
                }
            }
        }
        return maxDate;
    }

    // EFFECTS: returns the category of expenses of the month with the highest
    // spending
    @SuppressWarnings("methodlength")
    public String getMostSpentCategory(int month) {
        int foodtotal = 0;
        int clothestotal = 0;
        int funtotal = 0;
        int othertotal = 0;
        for (Expense e : expenses) {
            LocalDate date = e.getDate();
            if (date.getMonthValue() == month) {
                if (e.getCategory() == "food") {
                    foodtotal += e.getAmount();
                } else if (e.getCategory() == "fun") {
                    funtotal += e.getAmount();
                } else if (e.getCategory() == "clothes") {
                    clothestotal += e.getAmount();
                } else {
                    othertotal += e.getAmount();
                }
            }
        }
        int max = Math.max(Math.max(foodtotal, funtotal), Math.max(clothestotal, othertotal));
        if (max == 0) {
            return "No expenses this month";
        } else if (max == foodtotal) {
            return "food";
        } else if (max == funtotal) {
            return "fun";
        } else if (max == clothestotal) {
            return "clothes";
        } else {
            return "Other";
        }
    }

    // returns the average expenses in a given month.
    public int calcMonthlyAverage(int month) {
        int num = 0;
        int average = 0;

        for (Expense e : expenses) {
            LocalDate d = e.getDate();
            if (d.getMonthValue() == month) {
                num += 1;
            }
        }
        if (num == 0) {
            return 0;
        }
        average = calcMonthlyTotal(month) / num;
        return average;
    }

    // EFFECTS: returns true if the amount spent on a given day is below the daily
    // limit
    public Boolean belowDailyLimit(LocalDate date) {
        return calcDailyTotal(date) < DailyLimit;
    }

    // EFFECTS: returns true if the amount spent on a given month is below the
    // monthly limit
    public Boolean belowMonthlyLimit(int month) {
        return calcMonthlyTotal(month) < MonthlyLimit;
    }

    // EFFECTS :returns the size of the expenses.
    public int size() {
        return expenses.size();
    }

    // EFFECTS : returns the expense whose index is num from list of expenses
    public Expense get(int num) {
        return expenses.get(num);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expenses", expensesToJson());
        return json;
    }

    // EFFECTS: returns things in this tracker as a JSON array
    private JSONArray expensesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Expense e : expenses) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

}
