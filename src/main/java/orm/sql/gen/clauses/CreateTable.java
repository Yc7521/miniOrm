package orm.sql.gen.clauses;

import orm.sql.gen.tr.Translate;
import orm.sql.gen.tr.TranslateException;
import orm.sql.gen.tr.TypeTranslate;
import orm.util.meta.Meta;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateTable<T> extends Clause<T> {
    public CreateTable(Meta<T> meta) {
        super(meta);
    }

    public String generate() {
        final Translate<Type> typeTranslate = TypeTranslate.getInstance();
        return "CREATE TABLE `%s` (%s)".formatted(getTableName(),
                Arrays.stream(getFields()).map(field -> {
                    final String name = field.getName();
                    try {
                        String temp = "`%s` %s".formatted(name,
                                typeTranslate.translate(field.getType(),
                                        field.getDeclaredAnnotations()));
                        if (!name.equals("id")) return temp;
                        return temp + " PRIMARY KEY";
                    } catch (TranslateException ignored) {
                        return name;
                    }
                }).collect(Collectors.joining(", ")));
    }
}
