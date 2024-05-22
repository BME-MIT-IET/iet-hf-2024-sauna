package macaroni.actions;

import macaroni.app.game_view.ViewRepository;
import macaroni.model.character.Character;
import macaroni.model.element.Pipe;
import macaroni.model.element.Pump;
import macaroni.views.PumpView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pumpa bemeneti csőállítási interakciót kezelő osztály.
 */
public class SetPumpInputAction extends Action {

    /**
     * Logger inicializálása
     */
    private static final Logger LOGGER = Logger.getLogger(SetPumpInputAction.class.getName());

    /**
     * A karakter, aki az akciót végzi
     */
    private final Character actor;
    /**
     * A pumpa, amin az akciót végzi
     */
    private final Pump pump;

    /**
     * Létrehoz egy SetPumpInputAction példányt.
     *
     * @param actor A karakter, aki az akciót végzi
     * @param pump  A pumpa, amin az akciót végzi
     */
    public SetPumpInputAction(Character actor, Pump pump) {
        this.actor = actor;
        this.pump = pump;
    }

    /**
     * Ha tudja, elvégzi a paraméterként kapott cső bemeneti csőnek való beállítását.
     *
     * @param pipe a cső, amin action-t szeretnénk végezni.
     * @return Igazzal tér vissza ha sikeres.
     */
    @Override
    public boolean doAction(Pipe pipe) {
        var success = actor.setInputPipe(pump, pipe);
        if (success) {
            var pumpView = (PumpView) ViewRepository.getViewOfObject(pump);
            pumpView.setInputPipePos(ViewRepository.getViewOfObject(pipe).getPosition());
            LOGGER.log(Level.INFO, "Pump input pipe set successfully");
        }
        return success;
    }
}
