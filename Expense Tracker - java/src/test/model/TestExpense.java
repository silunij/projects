package model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport

public class TestExpense {

    private Expense testExpense1;
    private Expense testExpense2;
    private Expense testExpense3;
    private Expense testExpense4;

    @BeforeEach
    void runBefore() {
        testExpense1 = new Expense(100, "food", LocalDate.of(2025, 10, 4));
        testExpense2 = new Expense(104, "clothes", LocalDate.of(2025, 10, 4));
        testExpense3 = new Expense(20, "fun", LocalDate.of(2025, 10, 4));
        testExpense4 = new Expense(10, "travel", LocalDate.of(2025, 10, 4));
    }

   
    @Test
    void testCalcPercentageBudget() {
        assertEquals((100.0 / 100) * 100, testExpense1.calcPercentageBudget(), 0.01);
        assertEquals((104.0 / 50) * 100, testExpense2.calcPercentageBudget(), 0.01);
        assertEquals(0, testExpense4.calcPercentageBudget(), 0.01);
        assertEquals((20.0 / 80 * 100), testExpense3.calcPercentageBudget(), 0.01);
    }

}
