package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class PositionTest extends TestCase {
    public void testEncodeAndDecode() {
        assertTrue(new Position("E3").toString().equals("E3"));
        assertTrue(new Position(2, 7).toString().equals("C8"));
    }
}
