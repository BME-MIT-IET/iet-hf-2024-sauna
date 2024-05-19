package macaroni.actions;

import macaroni.app.gameView.ViewRepository;
import macaroni.model.character.Character;
import macaroni.model.character.Plumber;
import macaroni.model.element.Element;
import macaroni.views.CharacterView;
import macaroni.views.PipeView;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Mozgási interakciót kezelő osztály.
 */
public class MoveAction extends Action {
    private static final Logger logger = Logger.getLogger(MoveAction.class.getName());
    /**
     * A karakter, ami a mozgást végzi.
     */
    private final Character actor;

    /**
     * Létrehoz egy MoveAction példányt.
     *
     * @param actor A karakter, ami a mozgást végzi.
     */
    public MoveAction(Character actor) {
        this.actor = actor;
    }

    /**
     * Ha tudja, elvégzi a játékos paraméterként kapott elemre való mozgatását.
     *
     * @param element az elem, amin action-t szeretnénk végezni.
     * @return Igazzal tér vissza ha sikeres.
     */
    @Override
    public boolean doAction(Element element) {
        var locationBeforeMove = actor.getLocation();
        var success = actor.moveTo(element);
        logger.info("Move success: " + success);
        if (success) {
            var characterView = (CharacterView) ViewRepository.getViewOfObject(actor);
            characterView.setPosition(ViewRepository.getViewOfObject(element).getPosition());
            if (actor instanceof Plumber plumber) {
                if (plumber.getHeldPipe() != null) {
                    var heldPipeView = (PipeView) ViewRepository.getViewOfObject(plumber.getHeldPipe());
                    heldPipeView.replaceEndpointPos(
                            ViewRepository.getViewOfObject(locationBeforeMove).getPosition(),
                            ViewRepository.getViewOfObject(actor.getLocation()).getPosition()
                    );
                }
            }
        }
        return success;
    }
}
