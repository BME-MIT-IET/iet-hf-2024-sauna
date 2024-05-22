package macaroni.actions;

import macaroni.app.game_view.ViewRepository;
import macaroni.model.character.Plumber;
import macaroni.model.element.ActiveElement;
import macaroni.model.element.Pipe;
import macaroni.views.PipeView;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Cső aktív elemről való lekötési interakciót kezelő osztály.
 */
public class DetachPipeAction extends Action {
    private static final Logger logger = Logger.getLogger(DetachPipeAction.class.getName());
    /**
     * A szerelő, aki az akciót végzi
     */
    private final Plumber actor;
    /**
     * Az aktív elem, amiről lecsatlakoztatja a csövet
     */
    private final ActiveElement activeElement;

    /**
     * Létrehoz egy DetachPipeAction példányt.
     *
     * @param actor A szerelő, aki az akciót végzi
     * @param activeElement Az aktív elem, amiről lecsatlakoztatja a csövet
     */
    public DetachPipeAction(Plumber actor, ActiveElement activeElement) {
        this.actor = actor;
        this.activeElement = activeElement;
    }

    /**
     * Ha tudja, elvégzi a paraméterként kapott cső lecsatlakoztatását.
     *
     * @param pipe a cső, amin action-t szeretnénk végezni.
     * @return Igazzal tér vissza ha sikeres.
     */
    @Override
    public boolean doAction(Pipe pipe) {
        var success = actor.detachPipe(activeElement, pipe);
        if (success) {
            logger.info("Detach pipe success");
            var detachedPipeView = (PipeView) ViewRepository.getViewOfObject(pipe);
            detachedPipeView.replaceEndpointPos(
                    ViewRepository.getViewOfObject(activeElement).getPosition(),
                    ViewRepository.getViewOfObject(actor).getPosition()
            );
        }
        return success;
    }
}
