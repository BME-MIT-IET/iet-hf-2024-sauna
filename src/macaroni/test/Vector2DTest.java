package macaroni.test;

import macaroni.math.Vector2D;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    @Test
    void length() {
        Vector2D v1 = new Vector2D(1.0, 2.0);
        Vector2D v2 = new Vector2D(1.0, 2.0);
        assertEquals(v2.length(), v1.length());

    }
}