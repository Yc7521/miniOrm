package orm.sql.gen.tr;

import orm.iot.DataPool;
import orm.sql.annotations.Id;
import orm.sql.annotations.Length;
import orm.sql.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeTranslate implements Translate<Type> {
    protected final Map<Type, String> mapping = new HashMap<>();

    public TypeTranslate() {
        mapping.put(String.class, "VARCHAR");
        mapping.put(Integer.class, "INTEGER");
        mapping.put(int.class, "INTEGER");
        mapping.put(Long.class, "BIGINT");
        mapping.put(long.class, "BIGINT");
        mapping.put(Short.class, "SMALLINT");
        mapping.put(short.class, "SMALLINT");
        mapping.put(Byte.class, "TINYINT");
        mapping.put(byte.class, "TINYINT");
        mapping.put(Double.class, "DOUBLE");
        mapping.put(double.class, "DOUBLE");
        mapping.put(Float.class, "FLOAT");
        mapping.put(float.class, "FLOAT");
        mapping.put(BigDecimal.class, "DECIMAL");
        mapping.put(Boolean.class, "BOOLEAN");
        mapping.put(boolean.class, "BOOLEAN");
        mapping.put(Date.class, "DATE");
        mapping.put(LocalDate.class, "DATE");
        mapping.put(LocalDateTime.class, "TIMESTAMP");
        mapping.put(byte[].class, "BLOB");
    }

    public static void register() {
        DataPool.getInstance().register(new TypeTranslate());
    }

    public static TypeTranslate getInstance() {
        final TypeTranslate typeTranslate = DataPool
          .getInstance()
          .get(TypeTranslate.class);
        assert typeTranslate != null : "TypeTranslate need register";
        return typeTranslate;
    }

    public void register(Type type, String name) {
        if (mapping.containsKey(type)) return;
        mapping.put(type, name);
    }

    @Override
    public String translate(Type obj,
                            Annotation[] annotations) throws TranslateException {
        if (mapping.containsKey(obj)) {
            final StringBuilder res = new StringBuilder(mapping.get(obj));
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Length length && obj.equals(String.class)) {
                        res.append("(").append(length.value()).append(")");
                    }
                }
                for (Annotation annotation : annotations) {
                    if (annotation instanceof NotNull notNull) {
                        res.append(notNull.value() ? " NOT NULL" : " NULL");
                    } else if (annotation instanceof Id id) {
                        res.append(" PRIMARY KEY").append(
                          (!obj.equals(String.class) && id.autoIncrement())
                            ? " AUTO_INCREMENT" : "");
                    }
                }
            }
            return res.toString();
        } else {
            throw new TranslateException("Type not support");
        }
    }
}
