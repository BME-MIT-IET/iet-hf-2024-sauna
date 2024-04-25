package macaroni.app.gameView;

import macaroni.views.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ViewRepository {

    private final static Map<Object, View> views = new HashMap<>();

    private ViewRepository() {

    }

    public static void add(Object obj, View view) {
        views.put(obj, view);
    }

    public static Collection<View> getAll() {
        return views.values();
    }

    public static Collection<View> getFiltered(Predicate<? super View> filter) {
        return views.values().stream().filter(filter).toList();
    }

    public static View getViewOfObject(Object o) {
        return views.get(o);
    }

    public static void forEach(Predicate<? super View> filter, Consumer<? super View> action) {
        views.values().stream().filter(filter).forEach(action);
    }
}
