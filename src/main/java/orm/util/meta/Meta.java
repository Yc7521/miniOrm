package orm.util.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Meta<T> {
    private final T value;
    private final Class<?> clazz;

    /**
     * set value is the given value and class is this value's class
     *
     * @param value a value(must not null)
     */
    private Meta(T value) {
        assert value != null;
        this.value = value;
        this.clazz = value.getClass();
    }

    /**
     * set value is null and class is the given class
     */
    private Meta(Class<?> clazz) {
        this.value = null;
        this.clazz = clazz;
    }

    /**
     * set each is given
     */
    private Meta(T value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    /**
     * return a {@link Meta} whom value is given value and class is value's class
     */
    public static <T> Meta<T> of(T value) {
        assert value != null;
        return new Meta<>(value);
    }

    /**
     * return a {@link Meta} whom value is null and class is given class
     */
    public static <T> Meta<T> of(Class<T> clazz) {
        assert clazz != null;
        return new Meta<>(clazz);
    }

    /**
     * return a {@link Meta} which contains the given values
     */
    public static <T> Meta<T> of(T value, Class<?> clazz) {
        return new Meta<>(value, clazz);
    }

    public T getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public String getName() {
        return clazz.getName();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public Annotation[] getDeclaredAnnotations() {
        return clazz.getDeclaredAnnotations();
    }

    public Field[] getFields() {
        return clazz.getDeclaredFields();
    }

    public String[] getFieldNames() {
        return Arrays
          .stream(clazz.getDeclaredFields())
          .map(Field::getName)
          .toArray(String[]::new);
    }


    public Field getField(String name) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    public Object[] getFieldValues() throws IllegalAccessException {
        assert hasValue();
        AtomicReference<IllegalAccessException> err = new AtomicReference<>(null);
        final Object[] res = Arrays.stream(clazz.getDeclaredFields()).map(field -> {
            field.setAccessible(true);
            try {
                return field.get(value);
            } catch (IllegalAccessException e) {
                err.set(e);
                return null;
            }
        }).toArray();
        if (err.get() != null) {
            throw err.get();
        }
        return res;
    }

    public Object getFieldValue(String name)
      throws NoSuchFieldException, IllegalAccessException {
        assert hasValue();
        final Field field = getField(name);
        field.setAccessible(true);
        return field.get(value);
    }

    public void setField(String name, Object obj)
      throws NoSuchFieldException, IllegalAccessException {
        assert hasValue();
        final Field field = getField(name);
        field.setAccessible(true);
        field.set(value, obj);
    }

    /**
     * new an instance by class and put into a {@link Meta} to return
     *
     * @return a {@link Meta} contains a new instance of class
     * @throws NoSuchMethodException if not has a none parameter constructor
     */
    @SuppressWarnings("unchecked")
    public Meta<T> newInstance()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
             IllegalAccessException {
        return new Meta<>((T) clazz.getDeclaredConstructor().newInstance(), clazz);
    }

    public String toValueString() {
        assert hasValue();
        final Field[] fields = getFields();
        return "Meta<%s>{%s}".formatted(
          clazz.getSimpleName(),
          Arrays.stream(fields).map(field -> {
              field.setAccessible(true);
              try {
                  return field.getName() + "=" + field.get(value);
              } catch (IllegalAccessException ignored) {
                  return field.getName() + "=error";
              }
          }).collect(Collectors.joining(", "))
        );
    }

    @Override
    public String toString() {
        final Field[] fields = getFields();
        return "Meta<%s>{%s}".formatted(
          clazz.getSimpleName(),
          Arrays
            .stream(fields)
            .map(field -> field.getName() + "=" + field.getType().getTypeName())
            .collect(Collectors.joining(", "))
        );
    }
}
