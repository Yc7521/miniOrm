package orm.iot;

import java.util.HashMap;

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

    protected <T> boolean register(Class<?> clazz, T obj) {
        if (obj == null || data.containsKey(clazz)) {
            return false;
        }
        data.put(clazz, obj);
        return true;
    }

    public <T> boolean register(T obj) {
        return register(obj.getClass(), obj);
    }

    public <T> T get(Class<T> clazz) {
        return (T) data.get(clazz);
    }
}


