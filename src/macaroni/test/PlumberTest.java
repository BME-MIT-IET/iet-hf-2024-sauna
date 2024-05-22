package macaroni.test;

import macaroni.model.character.Plumber;
import macaroni.model.element.Cistern;
import macaroni.model.element.Pipe;
import macaroni.model.misc.WaterCollector;
import macaroni.utils.ModelObjectFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlumberTest {
    @Test
    void repair(){
        Pipe p1 = new Pipe(new WaterCollector());
        p1.pierce();
        assertTrue(p1.isPierced());
        Plumber plu = new Plumber(p1);
        plu.repair(p1);
        assertFalse(p1.isPierced());
    }

    @Test
    void pickUpPump(){
        ModelObjectFactory.setCisternCreatePumpName("Pump");
        Cistern c1 = new Cistern(new WaterCollector());
        Plumber plu = new Plumber(c1);
        plu.pickUpPump(c1);
        assertEquals(plu.getHeldPumpCount(), 1);
        for(int i=0; i<3; i++){
            ModelObjectFactory.setCisternCreatePumpName("Pump"+i);
            plu.pickUpPump(c1);
        }
        assertEquals(plu.getHeldPumpCount(), 4);
    }

    @Test
    void placePump(){
        ModelObjectFactory.setCisternCreatePumpName("Pump");
        Cistern c1 = new Cistern(new WaterCollector());
        Plumber plu = new Plumber(c1);
        plu.pickUpPump(c1);
        assertEquals(plu.getHeldPumpCount(), 1);
        Pipe p1 = new Pipe(new WaterCollector());
        Cistern c2 = new Cistern(new WaterCollector());
        p1.addEndpoint(c1);
        p1.addEndpoint(c2);
        plu.moveTo(p1);
        ModelObjectFactory.setPipeCreatePipeName("Pipe");
        plu.placePump(p1);
        assertEquals(plu.getHeldPumpCount(), 0);
    }
}