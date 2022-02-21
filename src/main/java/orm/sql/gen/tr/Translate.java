package orm.sql.gen.tr;

import java.lang.annotation.Annotation;

public interface Translate<T> {
    default String translate(T obj, Annotation[] annotations) throws TranslateException {
        throw new TranslateException("Not implemented");
    }

    default String translate(T obj) throws TranslateException {
        return translate(obj, null);
    }
}
