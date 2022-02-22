package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;
import orm.util.meta.TableMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Insert<T> extends Clause<T> {

    public Insert(Meta<T> meta) {
        super(meta);
        assert meta.hasValue();
    }

    @Override
    public Statement generate() throws IllegalAccessException {
        final TableMeta<T> meta = getMeta();
        return Statement.of("INSERT INTO `%s` (%s) VALUES (%s)".formatted(
          meta.getTableName(),
          Arrays
            .stream(meta.getFields())
            .map(field -> "`%s`".formatted(field.getName()))
            .collect(Collectors.joining(", ")),
          Arrays
            .stream(meta.getFields())
            .map(field -> "?")
            .collect(Collectors.joining(", "))
        ), meta.getFieldValues());
    }
}
