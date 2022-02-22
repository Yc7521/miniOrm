package orm.sql.gen.clauses;

import orm.sql.Statement;
import orm.util.meta.Meta;
import orm.util.meta.TableMeta;

import java.util.ArrayList;

public class Where<T> extends Clause<T> {
    public Where(TableMeta<T> meta) {
        super(meta);
    }

    public Where(Meta<T> meta) {
        super(meta);
    }

    @Override
    public Statement generate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public WhereBuilder builder() {
        return new WhereBuilder();
    }

    public class WhereBuilder {
        private final ArrayList<Statement> statements;

        public WhereBuilder(ArrayList<Statement> statements) {
            this.statements = statements;
            statements.add(Statement.of(" WHERE "));
        }

        public WhereBuilder() {
            statements = new ArrayList<>();
            statements.add(Statement.of("WHERE "));
        }

        public LogicBuilder byId(Object id) {
            statements.add(Statement.of("`%s` = ?".formatted(getMeta()
              .getIdColumnName()
              .orElseThrow()), id));
            return new LogicBuilder();
        }

        public LogicBuilder by(String columnName, Object value) {
            statements.add(Statement.of("`%s` = ?".formatted(columnName), value));
            return new LogicBuilder();
        }

        public LogicBuilder by(String columnName, Operator operator, Object value) {
            statements.add(Statement.of("`%s` %s ?".formatted(columnName,
              operator.getOperator()
            ), value));
            return new LogicBuilder();
        }

        public LogicBuilder like(String columnName, Object value) {
            statements.add(Statement.of("`%s` LIKE ?".formatted(columnName), value));
            return new LogicBuilder();
        }

        public Statement build() {
            return Statement.of(statements);
        }

        public class LogicBuilder {
            public WhereBuilder and() {
                statements.add(Statement.of(" AND "));
                return WhereBuilder.this;
            }

            public WhereBuilder or() {
                statements.add(Statement.of(" OR "));
                return WhereBuilder.this;
            }

            public Statement build() {
                return Statement.of(statements);
            }
        }
    }
}
