package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;

import java.io.Serializable;
import java.util.ArrayList;

public class Select<T> extends Clause<T> {
    private final ArrayList<Statement> statements;

    public Select(Meta<T> meta) {
        super(meta);
        statements = new ArrayList<>();
        statements.add(Statement.of("SELECT * FROM " + getTableName()));
    }

    public Statement generate() {
        return Statement.of(statements);
    }

    public Where<T>.WhereBuilder where() {
        return new Where<>(getEntityMeta()).new WhereBuilder(this.statements);
    }

}
