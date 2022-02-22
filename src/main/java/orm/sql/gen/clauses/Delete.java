package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Delete<T> extends Clause<T> {
    private final ArrayList<Statement> statements;

    public Delete(Meta<T> meta) throws IllegalAccessException {
        super(meta);
        statements = new ArrayList<>();
        statements.add(Statement.of("DELETE FROM `%s`".formatted(getMeta().getTableName())));
        AtomicBoolean error = new AtomicBoolean(false);
        if (meta.hasValue()) {
            final Statement build = where().byId(getMeta().getIdColumn().map(field -> {
                field.setAccessible(true);
                try {
                    return field.get(meta.getValue());
                } catch (IllegalAccessException e) {
                    error.set(true);
                }
                return null;
            }).orElseThrow()).build();
            if (error.get()) {
                throw new IllegalAccessException("Cannot delete entity");
            }
            statements.clear();
            statements.add(build);
        }
    }

    public Statement generate() {
        return Statement.of(statements);
    }

    public Where<T>.WhereBuilder where() {
        return new Where<>(getMeta()).new WhereBuilder(this.statements);
    }
}
