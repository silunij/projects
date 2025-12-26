package model;

import java.time.LocalDate;

import org.json.JSONObject;
import persistence.Writable;

public class Expense implements Writable {
    // this class represents a single expense record entered by the user

    private int amount;
    private String category;
    private LocalDate date;

    public Expense(int amount, String category, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // EFFECTS: returns the amount of the expense
    public int getAmount() {
        return amount;
    }



    // EFFECTS: returns the category
    public String getCategory() {
        return category;
    }



    // EFFECTS: returns the date
    public LocalDate getDate() {
        return date;
    }

    // EFFECTS: returns the percentage of budget of an expense for its category.
    public Integer calcPercentageBudget() {
        if (category.equals("food")) {
            return (int) Math.round((amount / 100.0) * 100);
        }
        if (category.equals("clothes")) {
            return  (int) Math.round((amount / 50.0) * 100);
        }
        if (category.equals("fun")) {
            return  (int) Math.round((amount / 80.0) * 100);
        }   
        return 0;
    }
    

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("category", category);
        json.put("date", date.toString());
        return json;
    }
}
   