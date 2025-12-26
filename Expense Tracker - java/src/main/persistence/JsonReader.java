package persistence;


import model.Event;
import model.EventLog;
import model.Expense;
import model.ExpenseTracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads tracker from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads tracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ExpenseTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(new Event("Loaded expenses from file"));
        return parseExpenseTracker(jsonObject);
        
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses tracker from JSON object and returns it
    private ExpenseTracker parseExpenseTracker(JSONObject jsonObject) {
        ExpenseTracker et = new ExpenseTracker();
        addExpenses(et, jsonObject);
        return et;
    }

    // MODIFIES: tracker
    // EFFECTS: parses expenses from JSON object and adds them to tracker
    private void addExpenses(ExpenseTracker et, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("expenses");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(et, nextExpense);
        }
    }

    // MODIFIES: expenseTracker
    // EFFECTS: parses expense from JSON object and adds it to expenseTracker
    private void addExpense(ExpenseTracker et, JSONObject jsonObject) {
        int amount = jsonObject.getInt("amount");
        String category = jsonObject.getString("category");
        String dateString = jsonObject.getString("date");
        LocalDate date = LocalDate.parse(dateString);
        Expense expense = new Expense(amount, category, date);
        et.addExpense(expense);
    }
}
