package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Expense;
import model.ExpenseTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

// this class contains the user interface (console) for the expense tracker.
@ExcludeFromJacocoGeneratedReport
public class ExpenseApp {

    private ExpenseTracker expenses;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/tracker.json";

    // EFFECTS: initializes expenses and a new scanner.
    public ExpenseApp() {
        expenses = new ExpenseTracker();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        run();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void run() {
        boolean keepGoing = true;
        String command = null;

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");

    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nChoose an option");
        System.out.println("\ta -> Add Expense");
        System.out.println("\td -> View Daily Total");
        System.out.println("\tmt -> View Monthly Total");
        System.out.println("\tms -> View Monthly Summary Report");
        System.out.println("\tv -> View list of Expenses added");
        System.out.println("\ts -> Save List of Expenses");
        System.out.println("\tl -> Load List of Expenses");
        System.out.println("\tq -> quit");
    }

    // MODIFIES :this
    // EFFECTS : processes user commands.
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddExpense();
        } else if (command.equals("d")) {
            doCalcDailyTotal();
        } else if (command.equals("mt")) {
            doCalcMonthlyTotal();
        } else if (command.equals("ms")) {
            doMonthlySummary();
        } else if (command.equals("v")) {
            doGetListOfExpenses();
        } else if (command.equals("s")) {
            doSaveToTracker();
        } else if (command.equals("l")) {
            loadFromTracker();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    public void doSaveToTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenses);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    public void loadFromTracker() {
        try {
            expenses = jsonReader.read();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    public void doGetListOfExpenses() {
        ArrayList<Expense> expenseList = expenses.getExpenses();
        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            System.out.println("Here are all your recorded expenses:");
            for (Expense e : expenseList) {
                System.out.println("Amount: $" + e.getAmount()
                        + ", Category: " + e.getCategory()
                        + ", Date: " + e.getDate());
            }
        }
    }

    // EFFECTS: returns a summary of the month including the day the most money is
    // spent, what category, what day of the week

    public void doMonthlySummary() {
        System.out.println("Enter the month number (1–12):");
        int month = input.nextInt();

        System.out.println("Monthly Summary");
        System.out.println("Total spent this month: $" + expenses.calcMonthlyTotal(month));
        System.out.println("Average daily spending: $" + expenses.calcMonthlyAverage(month));
        System.out.println("Day with highest spending: " + expenses.getDayWithHighestSpending(month));
        System.out.println("Most spent category: " + expenses.getMostSpentCategory(month));

    }

    // MODIFIES: this
    // EFFECTS: adds an Expense
    public void doAddExpense() {

        System.out.print("Enter amount to add");
        int amount = input.nextInt();
        input.nextLine();

        System.out.print("Enter category of expense");
        String category = input.nextLine();

        System.out.print("Enter today's date (yyyy-mm-dd)'");
        LocalDate date = LocalDate.parse(input.nextLine());
        handleAmountPercentage(amount, category, date);

        Expense expense = new Expense(amount, category, date);

        expenses.addExpense(expense);
        System.out.println("Expense added successfully!");

    }

    // EFFECTS; generates a message about the percentage of budget for this category
    // the amount is.
    public void handleAmountPercentage(int amount, String category, LocalDate date) {
        Expense e = new Expense(amount, category, date); // pass recurring=false
        Integer percentage = e.calcPercentageBudget();
        System.out.println("This amount is " + percentage + "% of the budget for " + category);
    }

    // MODIFIES: this
    // EFFECTS: Calculates Daily Total
    public void doCalcDailyTotal() {
        input.nextLine();
        System.out.println("Enter the date (yyyy-mm-dd):");
        String userInput = input.nextLine();
        LocalDate date = LocalDate.parse(userInput);
        int total = expenses.calcDailyTotal(date);
        if (!expenses.belowDailyLimit(date)) {
            System.out.println("Warning: You’ve exceeded your daily limit!");
        } else {
            System.out.println("You’re within your daily limit!");
        }
        System.out.println("Total for " + date + ": " + total);
    }

    // MODIFIES: this
    // EFFECTS: Calculates Monthly limit
    @SuppressWarnings("methodlength") 
    public void doCalcMonthlyTotal() {
        System.out.print("Enter the month (Integer Number of the month, 1-january... 2-feb etc)");
        int month = input.nextInt();
        int total = expenses.calcMonthlyTotal(month);
        if (!expenses.belowMonthlyLimit(month)) {
            System.out.println("Warning: You’ve exceeded your daily limit!");
        } else {
            System.out.println("You’re within your daily limit!");
        }
        String monthName = "";
        if (month == 1) {
            monthName = "january";
        }
        if (month == 2) {
            monthName = "february";
        }
        if (month == 3) {
            monthName = "march";
        }
        if (month == 4) {
            monthName = "april";
        }
        if (month == 5) {
            monthName = "may";
        }
        if (month == 6) {
            monthName = "June";
        }
        if (month == 7) {
            monthName = "july";
        }
        if (month == 8) {
            monthName = "august";
        }
        if (month == 9) {
            monthName = "september";
        }
        if (month == 10) {
            monthName = "october";
        }
        if (month == 11) {
            monthName = "november";
        }
        if (month == 12) {
            monthName = "december";
        }
        System.out.println("Total for " + monthName + ": " + total);
    }

}
