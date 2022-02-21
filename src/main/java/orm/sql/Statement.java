package orm.sql;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Statement implements Serializable {
    @Serial
    private static final long serialVersionUID = 2022L;
    private final String sql;
    private final Object[] params;

    private Statement(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    private Statement(String sql) {
        this(sql, new Object[]{});
    }

    public static Statement of(String sql) {
        return new Statement(sql);
    }

    public static Statement of(String sql, Object... params) {
        return new Statement(sql, params);
    }

    public static Statement of(List<Statement> statements) {
        if (statements.size() == 0) {
            return new Statement("");
        }
        StringBuilder sql = new StringBuilder();
        final Object[] objects = new Object[statements
                .stream()
                .mapToInt(value -> value.params.length)
                .sum()];
        int offset = 0;
        for (Statement statement : statements) {
            sql.append(statement.sql);
            System.arraycopy(statement.params, 0, objects, offset,
                    statement.params.length);
            offset += statement.params.length;
        }
        return new Statement(sql.toString(), objects);
    }

    public static Statement of(Statement... statements) {
        return of(Arrays.asList(statements));
    }

    public Statement append(Statement statement) {
        final Object[] objects = new Object[this.params.length + statement.params.length];
        System.arraycopy(this.params, 0, objects, 0, this.params.length);
        System.arraycopy(statement.params, 0, objects, this.params.length,
                statement.params.length);
        return new Statement(this.sql + statement.sql, objects);
    }

    public String sql() {
        return sql;
    }

    public Object[] params() {
        return params;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Statement) obj;
        return Objects.equals(this.sql, that.sql) &&
               Objects.equals(this.params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql, params);
    }

    @Override
    public String toString() {
        return "Statement[" +
               "sql=" + sql + ", " +
               "params=" + params + ']';
    }

}
