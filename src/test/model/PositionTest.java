package model;

import model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {
    @Test
    public void testEncodeAndDecode() {
        assertEquals("E3", new Position("E3").toString());
        assertEquals("C8", new Position(2, 7).toString());
    }
}
