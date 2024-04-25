package macaroni.model.character;

import macaroni.model.effects.BananaEffect;
import macaroni.model.element.Element;
import macaroni.model.element.Pipe;

/**
 * Saboteurs can drop bananas.
 */
public class Saboteur extends Character {

    /**
     * Creates a new Saboteur.
     *
     * @param location the initial location of the saboteur
     */
    public Saboteur(Element location) {
        super(location);
    }

    /**
     * Drops a banana effect on the pipe, that will in turn make it slippery.
     * <p></p>
     * This action will only be successful if the saboteur is standing on the given pipe.
     *
     * @param pipe the pipe about to be made slippery
     */
    public boolean dropBanana(Pipe pipe) {
        if (location == pipe) {
            pipe.setEffect(new BananaEffect(pipe));
            return true;
        } else {
            return false;
        }
    }
}
