package persistence;


import model.Expense;
import model.ExpenseTracker;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            //ExpenseTracker et = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json");
        try {
            ExpenseTracker et = reader.read();
            assertEquals(0, et.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
        try {
            ExpenseTracker et = reader.read();
            List<Expense> expenses = et.getExpenses();
            assertEquals(2, expenses.size());
            checkExpense(10, "food", LocalDate.of(2025, 10, 4), expenses.get(0));
            checkExpense(20, "clothes", LocalDate.of(2025, 02, 14), expenses.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}