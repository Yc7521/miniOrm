package orm.util.converter;

public interface Converter<T, U> {
    /**
     * @param value an object needed be converted
     */
    T parse(U value);

    /**
     * adapt to sql
     *
     * @return an object can be set into sql
     */
    U format(T value);
}
