package macaroni.utils;

import macaroni.model.character.Plumber;
import macaroni.model.character.Saboteur;
import macaroni.model.element.*;
import macaroni.model.misc.WaterCollector;

import java.util.*;

/**
 * Static class for creating instances of model objects.
 * Part of the Graphics phase.
 */
public final class ModelObjectFactory {

    /**
     * The map that stores the objects created by ModelObjectFactory.
     */
    private static final Map<String, Object> objectMap = new HashMap<>();

    /**
     * The water collector that should be used as ground when
     * a new pipe is created by cisterns. ({@link Cistern#spawnPipe()})
     */
    private static WaterCollector cisternCreatePipeGround = null;

    /**
     * The name that should be used when cisterns create a new pipe. ({@link Cistern#spawnPipe()})
     */
    private static String cisternCreatePipeName = null;

    /**
     * The name that should be used when cisterns create a new pump. ({@link Cistern#acquirePump()})
     */
    private static String cisternCreatePumpName = null;

    /**
     * The name that should be used when pipes create a new pipe. ({@link Pipe#split()})
     */
    private static String pipeCreatePipeName = null;

    private ModelObjectFactory() {
    }

    /**
     * Gets a previously created object by name.
     *
     * @param name the name of the object
     * @return the object, or null if no object was created yet with this name
     */
    public static Object getObject(String name) {
        return objectMap.get(name);
    }

    /**
     * Gets the name of an existing object.
     *
     * @param object the object
     * @return the name of the object, or empty string if the object is not in the objectMap
     */
    public static String getNameOfObject(Object object) {
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if (entry.getValue().equals(object)) {
                return entry.getKey();
            }
        }
        return "";
    }

    /**
     * Returns the list of existing objects.
     *
     * @return the collection of objects
     */
    public static Collection<Object> getObjectList() {
        return objectMap.values();
    }

    /**
     * Returns the names of the existing objects.
     *
     * @return the set of names
     */
    public static Set<String> getObjectNameList() {
        return objectMap.keySet();
    }

    /**
     * Returns all existing objects that are of the specified type.
     *
     * @param type the type of objects to return
     * @return the collection of matching objects
     */
    public static <T extends Element> List<T> getObjectList(Class<T> type) {
        return new ArrayList<>(objectMap.values().stream()
                .filter(type::isInstance).map(type::cast).toList());
    }

    /**
     * Returns all existing objects that are of the specified type,
     * in randomized order.
     *
     * @param type the type of objects to return
     * @return the collection of matching objects
     */
    public static <T extends Element> List<T> getRandomizedObjectList(Class<T> type) {
        var elements = getObjectList(type);
        Collections.shuffle(elements);
        return elements;
    }

    /**
     * Clears the list of previously created objects, and resets any
     * variables that might have been set previously.
     */
    public static void reset() {
        objectMap.clear();
        cisternCreatePipeGround = null;
        cisternCreatePipeName = null;
        cisternCreatePumpName = null;
    }


    /**
     * Creates a new Pipe element.
     *
     * @param name     the name of the pipe object
     * @param ground   the water collector that should be set as the pipe's ground
     * @param capacity the capacity of the pipe
     * @return the created pipe
     */
    public static Pipe createPipe(String name, WaterCollector ground, int capacity) {
        Pipe p = new Pipe(ground, capacity);
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Pipe element.
     *
     * @param name   the name of the pipe object
     * @param ground the water collector that should be set as the pipe's ground
     * @return the created pipe
     */
    public static Pipe createPipe(String name, WaterCollector ground) {
        Pipe p = new Pipe(ground);
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Pump element.
     *
     * @param name         the name of the pump object
     * @param maxPortCount the number of ports on the pump
     * @param tankCapacity the capacity of the tank of the pump
     * @return the created pump
     */
    public static Pump createPump(String name, int maxPortCount, int tankCapacity) {
        Pump p = new Pump(maxPortCount, tankCapacity);
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Pump element.
     *
     * @param name         the name of the pump object
     * @param maxPortCount the number of ports on the pump
     * @return the created pump
     */
    public static Pump createPump(String name, int maxPortCount) {
        Pump p = new Pump(maxPortCount);
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Pump element.
     *
     * @param name the name of the pump object
     * @return the created pump
     */
    public static Pump createPump(String name) {
        Pump p = new Pump();
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Spring element.
     *
     * @param name                  the name of the spring object
     * @param waterDispensedPerTick amount of water dispensed per tick
     * @return the created spring
     */
    public static Spring createSpring(String name, int waterDispensedPerTick) {
        Spring s = new Spring(waterDispensedPerTick);
        objectMap.put(name, s);
        return s;
    }

    /**
     * Creates a new Spring element.
     *
     * @param name the name of the spring object
     * @return the created spring
     */
    public static Spring createSpring(String name) {
        Spring s = new Spring();
        objectMap.put(name, s);
        return s;
    }

    /**
     * Creates a new Cistern element.
     *
     * @param name             the name of the cistern object
     * @param cisternCollector the water collector that should store the cistern's collected water
     * @return the created cistern
     */
    public static Cistern createCistern(String name, WaterCollector cisternCollector) {
        Cistern c = new Cistern(cisternCollector);
        objectMap.put(name, c);
        return c;
    }

    /**
     * Creates a new Plumber.
     *
     * @param name          the name of the plumber object
     * @param startLocation the initial location of the plumber
     * @return the created plumber
     */
    public static Plumber createPlumber(String name, Element startLocation) {
        Plumber p = new Plumber(startLocation);
        objectMap.put(name, p);
        return p;
    }

    /**
     * Creates a new Saboteur.
     *
     * @param name          the name of the saboteur object
     * @param startLocation the initial location of the saboteur
     * @return the created saboteur
     */
    public static Saboteur createSaboteur(String name, Element startLocation) {
        Saboteur s = new Saboteur(startLocation);
        objectMap.put(name, s);
        return s;
    }

    /**
     * Creates a new Water Collector.
     *
     * @param name the name of the water collector object
     * @return the created water collector
     */
    public static WaterCollector createWaterCollector(String name) {
        WaterCollector wc = new WaterCollector();
        objectMap.put(name, wc);
        return wc;
    }


    /**
     * Sets the water collector that will be used as ground for pipes
     * created by {@link Cistern#spawnPipe()}.
     *
     * @param wc the water collector to set
     */
    public static void setCisternCreatePipeGround(WaterCollector wc) {
        cisternCreatePipeGround = wc;
    }

    /**
     * Sets the name that should be used when cisterns create a new pipe.
     *
     * @param name the name to set
     */
    public static void setCisternCreatePipeName(String name) {
        cisternCreatePipeName = name;
    }

    /**
     * Creates a new Pipe element, with a water collector that was set beforehand
     * using {@link #setCisternCreatePipeGround(WaterCollector)}, and with a name
     * that has to be set using {@link #setCisternCreatePipeName(String)} before
     * every time this method is called.
     * <p></p>
     * This should only be used by {@link Cistern#spawnPipe()}.
     *
     * @return the created pipe
     */
    public static Pipe cisternCreatePipe() {
        if (cisternCreatePipeName == null) {
            throw new IllegalStateException("ERROR: cisternCreatePipeName has to be set before calling cisternCreatePipe!");
        }
        String pipeName = cisternCreatePipeName;
        cisternCreatePipeName = null;
        return createPipe(pipeName, cisternCreatePipeGround);
    }


    /**
     * Sets the name that should be used when cisterns create a new pump.
     *
     * @param name the name to set
     */
    public static void setCisternCreatePumpName(String name) {
        cisternCreatePumpName = name;
    }

    /**
     * Creates a new Pump element, with a default name that can be changed
     * using {@link #setCisternCreatePumpName(String)}.
     * <p></p>
     * This should only be used by {@link Cistern#acquirePump()}.
     *
     * @return the created pump
     */
    public static Pump cisternCreatePump() {
        if (cisternCreatePumpName == null) {
            throw new IllegalStateException("ERROR: cisternCreatePumpName has to be set before calling cisternCreatePump!");
        }
        String pumpName = cisternCreatePumpName;
        cisternCreatePumpName = null;
        return createPump(pumpName);
    }


    /**
     * Sets the name that should be used when pipes create a new pipe.
     *
     * @param name the name to set
     */
    public static void setPipeCreatePipeName(String name) {
        pipeCreatePipeName = name;
    }

    /**
     * Creates a new Pipe element, with a default name that can be changed
     * using {@link #setPipeCreatePipeName(String)}.
     * <p></p>
     * This should only be used by {@link Pipe#split()}.
     *
     * @param ground the water collector that should be set as the pipe's ground
     * @return the created pipe
     */
    public static Pipe pipeCreatePipe(WaterCollector ground) {
        if (pipeCreatePipeName == null) {
            throw new IllegalStateException("ERROR: pipeCreatePipeName has to be set before calling pipeCreatePipe!");
        }
        String pipeName = pipeCreatePipeName;
        pipeCreatePipeName = null;
        return createPipe(pipeName, ground);
    }
}
