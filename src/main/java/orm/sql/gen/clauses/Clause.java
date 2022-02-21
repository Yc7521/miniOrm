package orm.sql.gen.clauses;

import orm.sql.annotations.Id;
import orm.util.meta.Meta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class Clause<T> {
    private final Meta<T> entityMeta;

    public Clause(Meta<T> meta) {
        this.entityMeta = meta;
    }

    public Meta<T> getEntityMeta() {
        return entityMeta;
    }

    protected String getTableName() {
        return getEntityMeta().getSimpleName();
    }

    protected Optional<Field> getIdColumn() {
        final Stream<Field> id = Arrays
                .stream(getFields())
                .filter(field -> field.isAnnotationPresent(Id.class) ||
                                 field.getName().equals("id"));
        return id.findFirst();
    }

    protected Optional<String> getIdColumnName() {
        return getIdColumn().map(Field::getName);
    }

    protected Field[] getFields() {
        return getEntityMeta().getFields();
    }
}
