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
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            ExpenseTracker et = new ExpenseTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(et);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            et = reader.read();
            assertEquals(0, et.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            ExpenseTracker et = new ExpenseTracker();
            et.addExpense(new Expense(10, "food", LocalDate.of(2025, 10, 4)));
            et.addExpense(new Expense(20, "clothes", LocalDate.of(2025, 02, 14)));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(et);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            et = reader.read();
            List<Expense> expenses = et.getExpenses();
            assertEquals(2, expenses.size());
            checkExpense(10, "food", LocalDate.of(2025, 10, 4), expenses.get(0));
            checkExpense(20, "clothes", LocalDate.of(2025, 02, 14), expenses.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
