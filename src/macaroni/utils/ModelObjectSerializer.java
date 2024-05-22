package macaroni.utils;

import macaroni.model.effects.BananaEffect;
import macaroni.model.effects.Effect;
import macaroni.model.effects.NoEffect;
import macaroni.model.effects.TechnokolEffect;
import macaroni.model.element.Pipe;
import macaroni.views.Position;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static class for serializing and serializing model objects.
 * Part of the Graphics phase.
 */
public class ModelObjectSerializer {

    /**
     * The listener to notify when a model object is loaded.
     */
    private static ModelObjectLoadedListener modelObjectLoadedListener;

    private ModelObjectSerializer() {

    }

    /**
     * Sets the listener to notify when a model object is loaded.
     *
     * @param listener the listener to set.
     */
    public static void setModelObjectLoadedListener(ModelObjectLoadedListener listener) {
        modelObjectLoadedListener = listener;
    }

    /**
     * Serializes the given object's given attribute into a string.
     *
     * @param object        the object to serialize
     * @param attributeName the object's attribute to serialize. Serializes all
     *                      attributes of the object if null.
     * @return the string representation of the attribute, or null if the
     * given object does not have an attribute with the given name
     */
    public static String serializeToString(Object object, String attributeName) {
        StringBuilder sb = new StringBuilder();
        // print objectName: objectType
        sb.append(ModelObjectFactory.getNameOfObject(object)).append(": ").append(object.getClass().getSimpleName());

        // go through class and all of its superclasses
        for (Class<?> reflection = object.getClass(); reflection != null; reflection = reflection.getSuperclass()) {
            // go through all fields of class
            for (Field field : reflection.getDeclaredFields()) {
                // print fieldName = fieldValue
                if (attributeName == null || field.getName().equals(attributeName)) {
                    sb.append("\n\t").append(field.getName()).append(" = ");
                    field.setAccessible(true); // NOSONAR
                    appendField(object, sb, field);
                    if (attributeName != null) return sb.toString();
                }
            }
        }
        return attributeName != null ? null : sb.toString();
    }

    private static void appendField(Object object, StringBuilder sb, Field field) {
        try {
            sb.append(serializeAttribute(field.get(object)));
        } catch (IllegalAccessException e) {
            sb.append("$IllegalAccessException");
        }
    }

    /**
     * Serializes the given object into a string.
     *
     * @param object the object to serialize
     * @return the string representation of the object
     */
    public static String serializeToString(Object object) {
        return serializeToString(object, null);
    }

    /**
     * Serializes the given attribute into a string.
     *
     * @param obj the attribute to serialize
     * @return the serialized attribute
     */
    private static Object serializeAttribute(Object obj) {
        // if obj is null, return "null"
        if (obj == null) return "null";

        // if obj is a model object, represent it as its name
        String objName = ModelObjectFactory.getNameOfObject(obj);
        if (!objName.isBlank()) {
            return objName;
        }

        // if obj is an Effect, write its name
        if (obj instanceof NoEffect) {
            return "None";
        } else if (obj instanceof BananaEffect) {
            return "Banana";
        } else if (obj instanceof TechnokolEffect) {
            return "Technokol";
        }

        // if obj is a list, go through all elements and print them,
        // and if they are model objects, print their names instead
        if (obj instanceof List list) {
            StringBuilder sb = new StringBuilder("[");
            for (Object o : list) {
                sb.append(serializeAttribute(o)).append(", ");
            }
            if (sb.length() > 1) {
                // delete last 2 chars ", "
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            return sb.toString();
        }

        return obj;
    }

    /**
     * Serializes all currently existing model objects from the ModelObjectFactory
     * into the specified file.
     *
     * @param file the file to serialize all objects to
     * @throws IOException if an I/O error occurs
     */
    public static void serializeToFile(File file) throws IOException {
        Collection<String> objectNames = ModelObjectFactory.getObjectNameList().stream().sorted(Comparator.naturalOrder()).toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // write object data
            for (String name : objectNames) {
                writer.write(serializeToString(ModelObjectFactory.getObject(name)) + "\n");
            }
            writer.flush();
        }
    }

    /**
     * Deserializes all model objects from the specified file and registers them
     * into the ModelObjectFactory.
     *
     * @param file the file to serialize all objects to
     * @throws IOException if an I/O error occurs
     */
    public static void deserializeFromFile(File file) throws IOException {
        String[] lines = Files.readString(file.toPath()).split("\n");

        createObjects(lines);

        // set fields for objects
        String currentObjectName = null;
        for (String line : lines) {
            if (!line.startsWith("#") && line.contains("=")) {
                // if line starts with tab, then it is an attribute name-value pair,
                // set field of the current object
                String[] attribData = line.split("=");
                setObjectField(ModelObjectFactory.getObject(currentObjectName), attribData[0].strip(), attribData.length < 2 ? "" : attribData[1].strip());
            } else {
                currentObjectName = line.split(":")[0].strip();
            }
        }
    }

    private static void createObjects(String[] lines) {
        Pattern posRegex = Pattern.compile("x\\s*=\\s*(\\d+)\\s*,\\s*y\\s*=\\s*(\\d+)");
        Position pos = null;

        // create objects
        for (String line : lines) {
            if (line.startsWith("#")) {
                Matcher matcher = posRegex.matcher(line);
                if (matcher.find()) {
                    pos = new Position(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
                }
            }
            if (line.contains(":")) {
                String[] objInfo = line.split(":");
                createObject(objInfo[1].strip(), objInfo[0].strip(), pos);
                pos = null;
            }
        }
    }

    /**
     * Creates and registers a model object into the ModelObjectFactory.
     *
     * @param type the type of model object to create
     * @param name the name of the model object
     * @return the created object
     */
    private static Object createObject(String type, String name, Position pos) {
        Object created = switch (type) {
            case "Pipe" -> ModelObjectFactory.createPipe(name, null);
            case "Pump" -> ModelObjectFactory.createPump(name);
            case "Spring" -> ModelObjectFactory.createSpring(name);
            case "Cistern" -> ModelObjectFactory.createCistern(name, null);
            case "Plumber" -> ModelObjectFactory.createPlumber(name, null);
            case "Saboteur" -> ModelObjectFactory.createSaboteur(name, null);
            case "WaterCollector" -> ModelObjectFactory.createWaterCollector(name);
            default -> throw new IllegalArgumentException("Invalid object type found in map file: " + type);
        };
        if (modelObjectLoadedListener != null)
            modelObjectLoadedListener.modelObjectLoaded(created, name, pos);
        return created;
    }

    /**
     * Sets the value of the field of the given object.
     *
     * @param object     the object to set the field of
     * @param fieldName  the name of the field to set
     * @param fieldValue the value to set the field to
     */
    private static void setObjectField(Object object, String fieldName, String fieldValue) {
        // go through class and all of its superclasses
        for (Class<?> reflection = object.getClass(); reflection != null; reflection = reflection.getSuperclass()) {
            // search for field
            for (Field field : reflection.getDeclaredFields()) {
                if (!field.getName().equals(fieldName)) continue;
                field.setAccessible(true); //NOSONAR
                try {
                    field.set(object, deserializeAttribute(object, field, field.getType(), fieldValue)); //NOSONAR
                    return;
                } catch (IllegalAccessException e) {
                    throw new UnsupportedOperationException(e);
                }
            }
        }

        throw new IllegalStateException("Invalid attribute found in map file: " + fieldName);
    }

    /**
     * Deserializes an attribute of the given object.
     *
     * @param object         the object to deserialize an attribute of
     * @param field          the field of the object this serialized attribute corresponds to
     * @param type           the type of the object this serialized attribute corresponds to
     * @param attributeValue the serialized value of the attribute
     * @return the deserialized object
     */
    private static Object deserializeAttribute(Object object, Field field, Class<?> type, String attributeValue) {
        // if attribute value is blank, return null
        if (attributeValue.equals("null")) return null;

        // if type is int, return a parsed int
        if (type == int.class) {
            return Integer.parseInt(attributeValue);
        }

        // if type is boolean, return a parsed boolean
        if (type == boolean.class) {
            return Boolean.parseBoolean(attributeValue);
        }

        // if type is list, construct a list, and fill it with the
        // deserialized object of all the serialized elements
        if (type == List.class) {
            Class<?> listType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            List<Object> list = new ArrayList<>();

            // add all values to the list using the same castAttribute
            attributeValue = attributeValue.substring(1, attributeValue.length() - 1);
            String[] listValues = attributeValue.split(",");
            if (listValues.length == 1 && listValues[0].isBlank()) return list;
            for (String value : listValues) {
                list.add(deserializeAttribute(object, null, listType, value.strip()));
            }
            return list;
        }

        // if type is effect, create the corresponding effect
        if (type == Effect.class) {
            return switch (attributeValue) {
                case "None" -> new NoEffect((Pipe) object);
                case "Banana" -> new BananaEffect((Pipe) object);
                case "Technokol" -> new TechnokolEffect((Pipe) object);
                default -> throw new IllegalArgumentException("Unknown effect type: " + attributeValue);
            };
        }

        // if attribute value is found in ModelObjectFactory, return that
        Object obj = ModelObjectFactory.getObject(attributeValue);
        if (obj != null) {
            return obj;
        }

        throw new IllegalArgumentException("Unsupported attribute type: " + type.getSimpleName() + ", value: " + attributeValue);
    }
}
