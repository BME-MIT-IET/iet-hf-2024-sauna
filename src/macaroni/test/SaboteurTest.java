package macaroni.test;

import macaroni.model.character.Saboteur;
import macaroni.model.effects.BananaEffect;
import macaroni.model.effects.TechnokolEffect;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;
import macaroni.model.misc.WaterCollector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaboteurTest {
    @Test
    void dropBanana(){
        Pipe p1 = new Pipe(new WaterCollector());
        Saboteur s1 = new Saboteur(p1);
        s1.dropBanana(p1);
        assertEquals(p1.getEffect().getClass(), BananaEffect.class);
    }

    @Test
    void applyTechnokol(){
        Pipe p1 = new Pipe(new WaterCollector());
        Saboteur s1 = new Saboteur(p1);
        s1.applyTechnokol(p1);
        assertEquals(p1.getEffect().getClass(), TechnokolEffect.class);
    }

    @Test
    void getLocation(){
        Pipe p1 = new Pipe(new WaterCollector());
        Saboteur s1 = new Saboteur(p1);
        assertEquals(s1.getLocation(), p1);
    }

    @Test
    void setPipeIO(){
        Pump pump = new Pump();
        Saboteur s1 = new Saboteur(pump);
        Pipe p1 = new Pipe(new WaterCollector());
        pump.addPipe(p1);
        s1.setInputPipe(pump, p1);
        assertEquals(pump.getInputPipe(), p1);
        Pipe p2 = new Pipe(new WaterCollector());
        pump.addPipe(p2);
        s1.setOutputPipe(pump, p2);
        assertEquals(pump.getOutputPipe(), p2);
    }
}