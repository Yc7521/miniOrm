package orm.sql.gen.clauses;

import com.mysql.cj.xdevapi.PreparableStatement;
import orm.sql.Statement;
import orm.util.meta.Meta;

import java.util.ArrayList;

public class Where<T> extends Clause<T> {
    public Where(Meta<T> meta) {
        super(meta);
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
            statements.add(Statement.of(getIdColumnName().orElseThrow() + " = ?", id));
            return new LogicBuilder();
        }

        public LogicBuilder by(String columnName, Object value) {
            statements.add(Statement.of(columnName + " = ?", value));
            return new LogicBuilder();
        }

        public LogicBuilder by(String columnName, Operator operator, Object value) {
            statements.add(
                    Statement.of("%s %s ?".formatted(columnName, operator.getOperator()),
                            value));
            return new LogicBuilder();
        }

        public LogicBuilder like(String columnName, Object value) {
            statements.add(Statement.of("%s LIKE ?".formatted(columnName), value));
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
