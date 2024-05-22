package macaroni.model.element;

import macaroni.model.misc.WaterCollector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpringTest {
    @Test
    void tick(){
        Spring s1 = new Spring();
        Pump secondEndPoint = new Pump();
        Pipe p1 = new Pipe(new WaterCollector(), 10);
        s1.addPipe(p1);
        secondEndPoint.addPipe(p1);
        s1.tick();
        assertEquals(p1.getStoredWater(), 4);

        Spring s2 = new Spring(2);
        Pipe p2 = new Pipe(new WaterCollector(), 10);
        s2.addPipe(p2);
        secondEndPoint.addPipe(p2);
        s2.tick();
        assertEquals(p2.getStoredWater(), 2);
    }

}