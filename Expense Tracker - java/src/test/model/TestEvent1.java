package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestEvent1 {
    private Event e1;
    private Date d1;
    
    @BeforeEach
	public void runBefore() {
        e1 = new Event("Sensor open at door");   // (1)
        d1 = Calendar.getInstance().getTime();   // (2)
    }
        
    @Test
	public void testEvent() {
        assertEquals("Sensor open at door", e1.getDescription());
        assertEquals(d1, e1.getDate());
    }
    
    @Test
	public void testToString() {
        assertEquals(d1.toString() + "\n" + "Sensor open at door", e1.toString());
    }
}
