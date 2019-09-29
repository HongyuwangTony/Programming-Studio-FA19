package view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ViewInitializationTest {
    @Test
    public void testViewInitialization() {
        assertDoesNotThrow(() -> Main.main(null));
    }
}
