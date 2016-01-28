package test;

import main.java.Placeholder;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlaceholderTest {
    @Test public void testGetMessage() {
        Placeholder pH = new Placeholder();

        String test = pH.getMessage();

        assertEquals("This is a placeholder file", test);
    }
}
