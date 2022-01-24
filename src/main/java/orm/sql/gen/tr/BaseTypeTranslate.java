package orm.sql.gen.tr;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class BaseTypeTranslate implements Translate<Type> {
    @Override
    public String translate(Type obj) throws TranslateException {
        if (obj.equals(Integer.class)) {
            return "INTEGER";
        } else if (obj.equals(Short.class)) {
            return "SMALLINT";
        } else if (obj.equals(Byte.class)) {
            return "TINYINT";
        } else if (obj.equals(Float.class)) {
            return "FLOAT";
        } else if (obj.equals(Double.class)) {
            return "DOUBLE";
        } else if (obj.equals(String.class)) {
            return "VARCHAR";
        } else if (obj.equals(Date.class)) {
            return "DATE";
        } else if (obj.equals(LocalDate.class)) {
            return "DATE";
        } else if (obj.equals(LocalDateTime.class)) {
            return "DATETIME";
        } else {
            throw new TranslateException("Type not support");
        }
    }
}
