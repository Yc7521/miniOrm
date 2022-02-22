package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;
import orm.util.meta.TableMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Update<T> extends Clause<T> {
    public Update(Meta<T> meta) {
        super(meta);
        assert meta.hasValue();
    }

    @Override
    public Statement generate() throws IllegalAccessException {
        final TableMeta<T> meta = getMeta();
        final Stream<Field> fields = Arrays.stream(meta.getFields());
        final ArrayList<Statement> statements = new ArrayList<>();
        statements.add(Statement.of("UPDATE `%s` %s".formatted(
          meta.getTableName(),
          fields
            .map(field -> "SET `%s` = ?".formatted(field.getName()))
            .collect(Collectors.joining(", "))
        ), meta.getFieldValues()));
        final Field id = meta.getIdColumn().orElseThrow();
        id.setAccessible(true);
        final Where<T>.WhereBuilder builder = new Where<>(meta).new WhereBuilder(
          statements);
        return builder.byId(id.get(meta.getValue())).build();
    }
}
