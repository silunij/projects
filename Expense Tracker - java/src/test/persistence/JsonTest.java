package persistence;

import model.Expense;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class JsonTest {
    protected void checkExpense(int amount, String category, LocalDate date, Expense expense) {
        assertEquals(amount, expense.getAmount());
        assertEquals(category, expense.getCategory());
        assertEquals(date, expense.getDate());
    }
}