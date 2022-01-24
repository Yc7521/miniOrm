package orm.util.meta;

public class Reflect {
    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

}
