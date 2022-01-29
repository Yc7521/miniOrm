package orm.sql.gen.clauses;

import orm.sql.gen.tr.Translate;
import orm.sql.gen.tr.TranslateException;
import orm.sql.gen.tr.TypeTranslate;
import orm.util.meta.Meta;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateTable<T> {
    private final Meta<T> entityMeta;

    public CreateTable(Meta<T> meta) {
        this.entityMeta = meta;
    }

    public String generate() {
        final Translate<Type> typeTranslate = TypeTranslate.getInstance();
        return "CREATE TABLE `%s` (%s)".formatted(entityMeta.getSimpleName(),
                Arrays.stream(entityMeta.getFields()).map(field -> {
                    final String name = field.getName();
                    try {
                        if (name.equals("id")) {
                            return "`id` %s PRIMARY KEY".formatted(
                                    typeTranslate.translate(field.getType(),
                                            field.getDeclaredAnnotations()));
                        }
                        return "`%s` %s".formatted(name,
                                typeTranslate.translate(field.getType(),
                                        field.getDeclaredAnnotations()));
                    } catch (TranslateException ignored) {
                        return name;
                    }
                }).collect(Collectors.joining(", ")));
    }
}
