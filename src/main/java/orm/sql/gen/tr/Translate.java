package orm.sql.gen.tr;

public interface Translate<T> {
    String translate(T obj) throws TranslateException;
}
