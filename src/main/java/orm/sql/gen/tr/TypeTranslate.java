package orm.sql.gen.tr;

import orm.iot.DataPool;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeTranslate implements Translate<Type> {
    Map<Type, String> mapping = new HashMap<>();

    public TypeTranslate() {
        mapping.put(String.class, "VARCHAR(256)");
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
        mapping.put(BigDecimal.class, "DECIMAL()");
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
        return DataPool.getInstance().get(TypeTranslate.class);
    }

    public void register(Type type, String name) {
        if (mapping.containsKey(type)) return;
        mapping.put(type, name);
    }

    @Override
    public String translate(Type obj) throws TranslateException {
        if (mapping.containsKey(obj)) {
            return mapping.get(obj);
        } else {
            throw new TranslateException("Type not support");
        }
    }
}
