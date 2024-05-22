package macaroni.model.element;

import macaroni.model.misc.WaterCollector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CisternTest {
    @Test
    void tick(){
        Pipe p = new Pipe(new WaterCollector(),3);
        Cistern c1 = new Cistern(new WaterCollector());
        assertFalse(c1.removePipe(p));
        c1.addPipe(p);
        p.addWater(5);
        c1.tick();
        assertEquals(p.getStoredWater(),0);
        p.pierce();
        p.addWater(5);

        assertEquals(p.getStoredWater(), 0);
        assertTrue(c1.removePipe(p));

    }
}