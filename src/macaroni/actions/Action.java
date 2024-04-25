package macaroni.actions;

import macaroni.model.element.Element;
import macaroni.model.element.Pipe;

/**
 * Felhasználói interakciók kezelésére használt osztály.
 */
public abstract class Action {

    /**
     * Minden esetben hamissal tér vissza.
     *
     * @param element az elem, amin action-t szeretnénk végezni.
     * @return Minden esetben hamissal tér vissza.
     */
    public boolean doAction(Element element) {
        return false;
    }

    /**
     * Meghívja önmagán a doAction(Element)-et, és annak a visszatérési értékével tér vissza.
     *
     * @param pipe a cső, amin action-t szeretnénk végezni.
     */
    public boolean doAction(Pipe pipe) {
        return doAction((Element) pipe);
    }
}
