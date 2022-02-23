package orm.iot;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataPool {
    private static volatile DataPool instance;
    private final HashMap<Class<?>, Object> data;

    private DataPool() {
        data = new HashMap<>();
    }

    public static DataPool getInstance() {
        if (instance == null) {
            synchronized (DataPool.class) {
                if (instance == null) {
                    instance = new DataPool();
                }
            }
        }
        return instance;
    }

    /**
     * set the direct interface(no generic) of the class and itself to map with object
     */
    protected <T> boolean register(Class<?> clazz, T obj) {
        if (obj == null || data.containsKey(clazz)) {
            return false;
        }
        final ArrayList<Class<?>> classes
          = new ArrayList<>(List.of(clazz.getInterfaces()));
        for (Type t : clazz.getGenericInterfaces()) {
            if (t instanceof ParameterizedType pt &&
                pt.getRawType() instanceof Class<?> c) {
                classes.remove(c);
            }
        }
        for (Class<?> anInterface : classes) {
            if (data.containsKey(anInterface)) continue;
            data.put(anInterface, obj);
        }
        data.put(clazz, obj);
        return true;
    }

    public <T> boolean register(T obj) {
        return register(obj.getClass(), obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) data.get(clazz);
    }
}


