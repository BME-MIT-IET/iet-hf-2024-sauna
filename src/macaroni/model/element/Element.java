package macaroni.model.element;

import macaroni.model.character.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Element of the pipe system, the characters can move on these.
 */
public abstract class Element {

    /**
     * List containing the neighbours of the element.
     */
    protected List<Element> neighbours = new ArrayList<>();

    /**
     * Lets the character enter the element.
     *
     * @param character the character that wants to enter the element
     * @param from      the location where the character is coming from
     */
    public boolean enter(Character character, Element from) {
        if (neighbours.contains(from)) {
            return character.leave(this);
        }
        return false;
    }

    /**
     * Notifies the element that someone wants to step off from it.
     *
     * @return true on success
     */
    public boolean leave() {
        return true;
    }

    /**
     * Updates the element.
     */
    public abstract void tick();
}
