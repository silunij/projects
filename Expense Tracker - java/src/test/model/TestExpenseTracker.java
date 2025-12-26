package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport

public class TestExpenseTracker {
    private ExpenseTracker testExpenseTracker;
    private Expense testExpense1;
    private Expense testExpense2;
    private Expense testExpense3;
    private Expense testExpense4;

    @BeforeEach
    void runBefore() {
        testExpenseTracker = new ExpenseTracker();
        testExpense1 = new Expense(5, "food", LocalDate.of(2025, 10, 4));
        testExpense2 = new Expense(15, "clothes", LocalDate.of(2025, 10, 4));
        testExpense3 = new Expense(10, "fun", LocalDate.of(2025, 11, 5));
        testExpense4 = new Expense(900, "fun", LocalDate.of(2025, 11, 6));

    }

    @Test
    void testAddExpense() {
        testExpenseTracker.addExpense(testExpense1);
        assertEquals(1, testExpenseTracker.size());
        assertEquals(testExpense1, testExpenseTracker.get(0));
        testExpenseTracker.addExpense(testExpense2);
        assertEquals(2, testExpenseTracker.size());
        assertEquals(testExpense2, testExpenseTracker.get(1));
    }

    @Test
    void testDailyTotal() {
        testExpenseTracker.addExpense(testExpense1);
        assertEquals(5, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 10, 4)));
        testExpenseTracker.addExpense(testExpense1);
        assertEquals(10, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 10, 4)));
    }

    @Test
    void testDailyTotalMultiple() {
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense3);
        assertEquals(20, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 10, 4)));
        assertEquals(10, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 11, 5)));
    }

    @Test
    void testDailyTotalZero() {
        assertEquals(0, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 10, 4)));

    }

    @Test
    void testDailyTotalDayIsNotInList() {
        assertEquals(0, testExpenseTracker.calcDailyTotal(LocalDate.of(2025, 12, 5)));

    }

    @Test
    void testMonthlyTotalZero() {
        assertEquals(0, testExpenseTracker.calcMonthlyTotal(5));
    }

    @Test
    void testMonthlyTotalOneExpense() {
        testExpenseTracker.addExpense(testExpense1);
        assertEquals(5, testExpenseTracker.calcMonthlyTotal(10));
    }

    @Test
    void testMonthlyTotalMultiple() {
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense3);
        assertEquals(25, testExpenseTracker.calcMonthlyTotal(10));
        assertEquals(10, testExpenseTracker.calcMonthlyTotal(11));
        assertEquals(0, testExpenseTracker.calcMonthlyTotal(5));
    }

    @Test
    void testBelowDailyLimit() {
        testExpenseTracker.addExpense(testExpense1);
        assertTrue(testExpenseTracker.belowDailyLimit(LocalDate.of(2025, 10, 4)));
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense2);
        assertFalse(testExpenseTracker.belowDailyLimit(LocalDate.of(2025, 10, 4)));

    }

    @Test
    void testBelowMonthlyLimit() {
        testExpenseTracker.addExpense(testExpense1);
        assertTrue(testExpenseTracker.belowMonthlyLimit(11));
        testExpenseTracker.addExpense(testExpense4);
        assertFalse(testExpenseTracker.belowMonthlyLimit(11));
    }

    @Test
    void testGetDayWithHighestSpending() {
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense3);
        testExpenseTracker.addExpense(testExpense4);
        assertEquals(LocalDate.of(2025, 10, 4), testExpenseTracker.getDayWithHighestSpending(10));
        assertEquals(LocalDate.of(2025, 11, 6), testExpenseTracker.getDayWithHighestSpending(11));
        assertEquals(null, testExpenseTracker.getDayWithHighestSpending(1));
    }

    @Test
    void testCalcMonthlyAverage() {
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense3);
        assertEquals((5 + 15) / 2, testExpenseTracker.calcMonthlyAverage(10));
        assertEquals(0, testExpenseTracker.calcMonthlyAverage(3));
    }

    @Test
    void testGetMostSpentCategory() {
        Expense testExpense5 = new Expense(50, "travel", LocalDate.of(2025, 4, 15));
        Expense testExpense7 = new Expense(60, "travel", LocalDate.of(2025, 4, 15));
        Expense testExpense6 = new Expense(50, "food", LocalDate.of(2025, 11, 15));
        Expense testExpense8 = new Expense(50, "food", LocalDate.of(2025, 6, 15));
        Expense testExpense9 = new Expense(5, "food", LocalDate.of(2025, 6, 15));
        Expense testExpense10 = new Expense(54, "clothes", LocalDate.of(2025, 6, 15));
        testExpenseTracker.addExpense(testExpense1);
        testExpenseTracker.addExpense(testExpense2);
        testExpenseTracker.addExpense(testExpense3);
        testExpenseTracker.addExpense(testExpense4);
        testExpenseTracker.addExpense(testExpense5);
        testExpenseTracker.addExpense(testExpense6);
        testExpenseTracker.addExpense(testExpense7);
        testExpenseTracker.addExpense(testExpense8);
        testExpenseTracker.addExpense(testExpense9);
        testExpenseTracker.addExpense(testExpense10);
        assertEquals("clothes", testExpenseTracker.getMostSpentCategory(10));
        assertEquals("Other", testExpenseTracker.getMostSpentCategory(4));
        assertEquals("fun", testExpenseTracker.getMostSpentCategory(11));
        assertEquals("food", testExpenseTracker.getMostSpentCategory(6));
        assertEquals("No expenses this month", testExpenseTracker.getMostSpentCategory(3));
    }

}
