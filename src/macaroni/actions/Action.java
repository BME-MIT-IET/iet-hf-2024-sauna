package macaroni.actions;

import macaroni.model.element.Element;
import macaroni.model.element.Pipe;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Felhasználói interakciók kezelésére használt osztály.
 */
public abstract class Action {

    // Logger inicializálása
    private static final Logger LOGGER = Logger.getLogger(Action.class.getName());

    /**
     * Minden esetben hamissal tér vissza.
     *
     * @param element az elem, amin action-t szeretnénk végezni.
     * @return Minden esetben hamissal tér vissza.
     */
    public boolean doAction(Element element) {
        log("doAction(Element) metódus meghívása");
        return false;
    }

    /**
     * Meghívja önmagán a doAction(Element)-et, és annak a visszatérési értékével tér vissza.
     *
     * @param pipe a cső, amin action-t szeretnénk végezni.
     */
    public boolean doAction(Pipe pipe) {
        log("doAction(Pipe) metódus meghívása");
        return doAction((Element) pipe);
    }

    /**
     * Logolás készítése.
     *
     * @param message a logolandó üzenet
     */
    private void log(String message) {
        LOGGER.log(Level.INFO, message);
    }
}
