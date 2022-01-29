package orm.sql.gen.tr;

import java.lang.annotation.Annotation;

public interface Translate<T> {
    String translate(T obj, Annotation[] annotations) throws TranslateException;

    default String translate(T obj) throws TranslateException {
        return translate(obj, null);
    }
}
