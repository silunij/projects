package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import persistence.JsonReader;
import persistence.JsonWriter;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.EventLog;
import model.Expense;
import model.ExpenseTracker;
import model.Event;


@ExcludeFromJacocoGeneratedReport
// this is the graphical user interface
public class GUI extends JFrame implements ActionListener {
    private JsonReader jsonReader;

    private JButton addButton;
    private JButton viewDailyTotButton;
    private JButton viewMonthlyTotButton;
    private JButton monthlySummaryButton;
    private JButton printListButton;
    private ExpenseTracker tracker;
    private JButton quitButton;
    private JsonWriter jsonWriter;

    private JInternalFrame controlPanel;

    private JDesktopPane desktop;
    private static final String JSON_STORE = "./data/tracker.json";

    // constructor
    public GUI() {

        super("Expense Tracker");
        initializeData(); 

        desktop = new JDesktopPane();
        controlPanel = new JInternalFrame("Control Panel", false, false, false, false);
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setSize(400, 300);
        controlPanel.setVisible(true); 

        initializeButtons();
        buttonPanel();
        addMenu();

        desktop.add(controlPanel);
        add(desktop);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void initializeData() {
        tracker = new ExpenseTracker();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    //MODIFIES: this
    // EFFECTS: initializes the buttons. adds the buttons to the window.
    private void initializeButtons() {
        addButton = new JButton("Add Expense");
        viewDailyTotButton = new JButton("View daily total");
        viewMonthlyTotButton = new JButton("View monthly total");
        monthlySummaryButton = new JButton("view Monthly Summary");
        printListButton = new JButton("Print list of Data");
        quitButton = new JButton("Quit");

        JButton[] buttons = {addButton, viewDailyTotButton, viewMonthlyTotButton,
                monthlySummaryButton, printListButton, quitButton};

        for (JButton b : buttons) {
            b.addActionListener(this);
        }
    }

    //MODIFIES: this
    // EFFECTS: adds a menu for save and load on the window
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> save());
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> load());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

    }

    // EFFECTS: creates a button panel
    public void buttonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        buttonPanel.setLayout(new GridLayout(4,2));
        buttonPanel.add(addButton);
        buttonPanel.add(viewDailyTotButton);
        buttonPanel.add(viewMonthlyTotButton);
        buttonPanel.add(monthlySummaryButton);
        buttonPanel.add(printListButton);
        buttonPanel.add(quitButton);

        controlPanel.add(buttonPanel, BorderLayout.WEST);

    }

    // EFFECTS : calls the method which provides functionality to each button
    @SuppressWarnings("methodlength")
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == addButton) {
            add();
        }
        if (source == viewDailyTotButton) {
            printDailyTot();
        }
        if (source == viewMonthlyTotButton) {
            printmonthlyTot();
        }
        if (source == monthlySummaryButton) {
            printSummary();
        }
        if (source == printListButton) {
            printList();
        }
        if (source == quitButton) {
            quit();
        }
    }

    // EFFECTS: takes user input 
    public void add() {
        String amountString = JOptionPane.showInputDialog(this, "Enter amount:");
        if (amountString == null) {
            JOptionPane.showMessageDialog(this, "Unsuccessfull");
        }
        int amount = Integer.parseInt(amountString);
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null) {
            JOptionPane.showMessageDialog(this, "Unsuccessfull");
        }

        String dateString = JOptionPane.showInputDialog(this, "Enter date (yyyy-MM-dd):");
        if (dateString == null) {
            JOptionPane.showMessageDialog(this, "Unsuccessfull");
        }

        LocalDate date = LocalDate.parse(dateString);

        Expense expense = new Expense(amount, category, date);
        tracker.addExpense(expense);

        JOptionPane.showMessageDialog(this, "Expense added!");
        showBudgetProgress(expense);
        
    }

    private void showBudgetProgress(Expense expense) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int percent = expense.calcPercentageBudget();
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setStringPainted(true);
        bar.setBackground(Color.LIGHT_GRAY);


        JLabel label = new JLabel("The amount you entered ($" + expense.getAmount()
                + ") is " + percent + "% of the budget for " + expense.getCategory());

        panel.add(label);
        panel.add(bar);

        JOptionPane.showMessageDialog(this, panel, "Budget Progress", JOptionPane.INFORMATION_MESSAGE);
    }


    // EFFECTS: prints daily total when user enters the date
    public void printDailyTot() {
        String dateInput = JOptionPane.showInputDialog(this, "Enter date (YYYY-MM-DD):");

        if (dateInput != null) {
            try {
                LocalDate date = LocalDate.parse(dateInput);
                int total = tracker.calcDailyTotal(date);
                JOptionPane.showMessageDialog(this,
                        "Total for " + date + ": $" + total);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    //EFFECTS: prints monthly total when the user enters the month 
    public void printmonthlyTot() {
        String month = JOptionPane.showInputDialog(this, "Enter Month (1-12)");

        if (month != null) {
            try {
                Integer mnth = Integer.parseInt(month);
                int total = tracker.calcMonthlyTotal(mnth);
                JOptionPane.showMessageDialog(this,
                        "Total for " + month + ": $" + total);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid month");
            }
        }
    }

    // EFFECTS: prints the expense summary for the month the user enters.
    public void printSummary() {
        String month = JOptionPane.showInputDialog(this, "Enter Month (1-12)");
        if (month != null) {
            try {
                Integer mnth = Integer.parseInt(month);
                int total = tracker.calcMonthlyTotal(mnth);
                float  average = tracker.calcMonthlyAverage(mnth);
                LocalDate day = tracker.getDayWithHighestSpending(mnth);
                String category = tracker.getMostSpentCategory(mnth);
                JOptionPane.showMessageDialog(this,
                        " Monthly Summary" + " Total Spent this month is " + total + ":\n"
                                + "your daily average for month" + mnth + "is " + average + ":\n"
                                + "The day of this month with the highest spending is " + day + ":\n"
                                + "and the most spent category is " + category);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid month");
            }
        }
    }

    // EFFECTS: prints the list of expenses added in a new panel
    public void printList() {
        ArrayList<Expense> expenseList = tracker.getExpenses();
        if (expenseList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No expenses recorded yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are all your recorded expenses:\n\n");
            for (Expense ex : expenseList) {
                sb.append("Amount: $").append(ex.getAmount())
                        .append(", Category: ").append(ex.getCategory())
                        .append(", Date: ").append(ex.getDate())
                        .append("\n");

            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "All Expenses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: saves the data
    public void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(tracker);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Saved to " + JSON_STORE);
        } catch (FileNotFoundException exception) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads the data
    public void load() {
        try {
            tracker = jsonReader.read();
            JOptionPane.showMessageDialog(this, "Loaded from " + JSON_STORE);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS quits the window.
    public void quit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (Event e : EventLog.getInstance()) {
                System.out.println(e.toString());
            }
            System.exit(0);
            
        }
    }
}
