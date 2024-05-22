package macaroni.math;

import macaroni.views.Position;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    @Test
    void add(){
        Vector2D v1 = new Vector2D(1.0, 2.0);
        Vector2D v2 = new Vector2D(1.0, 2.0);
        v1 = v1.add(v2);
        assertEquals(v1.getX(), 2.0);
        assertEquals(v1.getY(), 4.0);
    }

    @Test
    void dot(){
        Vector2D v1 = new Vector2D(1.0, 2.0);
        Vector2D v2 = new Vector2D(1.0, 2.0);
        double dotProduct = v1.dot(v2);
        assertEquals(dotProduct, 5.0);
    }

    @Test
    void length() {
        Vector2D v1 = new Vector2D(1.0, 2.0);
        Vector2D v2 = new Vector2D(1.0, 2.0);
        assertEquals(v2.length(), v1.length());
        Vector2D v3 = new Vector2D(3.0, 4.0);
        assertEquals(v3.length(), 5.0);
    }

    @Test
    void scale(){
        Vector2D v1 = new Vector2D(1.0, 2.0);
        Vector2D v2 = v1.scale(3.0);
        assertEquals(v2.getX(), 3.0);
        assertEquals(v2.getY(), 6.0);
    }

    @Test
    void normalize(){
        Vector2D v3 = new Vector2D(3.0, 4.0);
        v3 = v3.normalize();
        assertEquals(v3.getX(), 3.0/5.0, 0.0001);
        assertEquals(v3.getY(), 4.0/5.0, 0.0001);
    }

    @Test
    void toPosition(){
        Vector2D v3 = new Vector2D(3.6, 4.2);
        Position p1 = v3.toPosition();
        assertEquals(p1.x(), 3);
        assertEquals(p1.y(), 4);
    }
}