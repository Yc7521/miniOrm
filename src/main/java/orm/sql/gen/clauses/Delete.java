package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;

import java.util.ArrayList;

public class Delete<T> extends Clause<T> {
    private final ArrayList<Statement> statements;

    public Delete(Meta<T> meta) {
        super(meta);
        statements = new ArrayList<>();
        statements.add(Statement.of("DELETE FROM " + getTableName()));
    }

    public Statement generate() {
        return Statement.of(statements);
    }

    public Where<T>.WhereBuilder where() {
        return new Where<>(getEntityMeta()).new WhereBuilder(this.statements);
    }
}
