package macaroni.utils;

import macaroni.views.Position;

/**
 * Listener that can be used in {@link ModelObjectSerializer} to listen to
 * each loaded model object.
 */
public interface ModelObjectLoadedListener {

    /**
     * Called when a model object is loaded from a map file.
     * @param object the object that was loaded
     * @param name the name of the object
     * @param pos the positional data of the object, or null if this object had no positional data
     */
    void modelObjectLoaded(Object object, String name, Position pos);
}
