package uk.ac.cam.echo2016.multinarrative.gui;

import uk.ac.cam.echo2016.multinarrative.gui.Strings;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringsTest {

    @Test public void testPopulateString(){
        assertEquals("A B C",Strings.populateString("%1 %2 %3","A","B","C","D"));
    }

}
