package orm.util.meta;

import orm.sql.annotations.Id;
import orm.sql.annotations.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class TableMeta<T> {
    private final Meta<T> entityMeta;

    public TableMeta(Meta<T> meta) {
        this.entityMeta = meta;
    }

    public Meta<T> getEntityMeta() {
        return entityMeta;
    }

    public T getValue() {
        return getEntityMeta().getValue();
    }

    public boolean hasValue() {
        return getEntityMeta().hasValue();
    }

    public String getTableName() {
        final Meta<T> meta = getEntityMeta();
        final Optional<Annotation> name = Arrays
          .stream(meta.getDeclaredAnnotations())
          .filter(annotation -> annotation instanceof Table)
          .findFirst();
        return name.map(annotation -> ((Table) annotation).name()).orElse(meta.getName());
    }

    public Optional<Field> getIdColumn() {
        final Stream<Field> id = Arrays.stream(getFields()).filter(field ->
          field.isAnnotationPresent(Id.class) || field.getName().equals("id"));
        return id.findFirst();
    }

    public Optional<String> getIdColumnName() {
        return getIdColumn().map(Field::getName);
    }

    public Field[] getFields() {
        return getEntityMeta().getFields();
    }

    public Object[] getFieldValues() throws IllegalAccessException {
        return getEntityMeta().getFieldValues();
    }
}
