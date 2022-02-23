package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;

import java.util.ArrayList;

public class Select<T> extends Clause<T> {
    private final ArrayList<Statement> statements;

    public Select(Meta<T> meta) {
        super(meta);
        statements = new ArrayList<>();
        statements.add(Statement.of("SELECT * FROM `%s`".formatted(getMeta().getTableName())));
    }

    @Override
    public Statement generate() {
        return Statement.of(statements);
    }

    public SelectBuilder builder() {
        return new SelectBuilder();
    }

    public class SelectBuilder {
        public Where<T>.WhereBuilder where() {
            return new Where<>(getMeta()).new WhereBuilder(statements);
        }

        public Statement end() {
            return Statement.of(statements);
        }
    }
}
