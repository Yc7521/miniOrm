package orm.sql;

import java.io.Serial;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Statement implements Serializable {
    @Serial
    private static final long serialVersionUID = 2022L;
    private final String sql;
    private final Object[] params;

    private Statement(String sql, Object[] params) {
        assert sql != null;
        assert params != null;
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
            System.arraycopy(statement.params,
              0,
              objects,
              offset,
              statement.params.length
            );
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
        System.arraycopy(statement.params,
          0,
          objects,
          this.params.length,
          statement.params.length
        );
        return new Statement(this.sql + statement.sql, objects);
    }

    public String sql() {
        return sql;
    }

    public Object[] params() {
        return params;
    }

    public boolean hasParams() {
        assert params != null;
        return params.length > 0;
    }

    Result load(Session session) {
        return new Result(this, session);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Statement) obj;
        return Objects.equals(this.sql, that.sql) && Arrays.equals(this.params,
          that.params
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql, Arrays.hashCode(params));
    }

    @Override
    public String toString() {
        return "{sql=%s, params=%s}".formatted(sql, Arrays.toString(params));
    }

    public interface ConsumerEx<T> {
        void accept(T t) throws Exception;
    }

    public interface SupplierEx<T> {
        T get() throws Exception;
    }

    public interface FunctionEx<T, R> {
        R apply(T t) throws Exception;
    }

    protected static class Result {
        Statement statement;
        Session session;

        public Result(Statement statement, Session session) {
            this.statement = statement;
            this.session = session;
        }

        /**
         * @param select call if the first result is a ResultSet object;
         */
        private int execute(FunctionEx<ResultSet, Integer> select) throws Exception {
            return execute(select, ps -> {
                // ps.getUpdateCount();
                try (final ResultSet resultSet = ps.getGeneratedKeys()) {
                    return resultSet.getInt(1);
                } catch (SQLException e) {
                    return 0;
                }
            });
        }

        /**
         * @param select call if the first result is a ResultSet object;
         * @param update call if the first result is an update count or there is no result
         */
        private int execute(FunctionEx<ResultSet, Integer> select,
                            FunctionEx<PreparedStatement, Integer> update) throws Exception {
            assert select != null;
            final Object[] params = statement.params();
            try (final PreparedStatement ps = session.getConnection().prepareStatement(
              statement.sql())) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                if (ps.execute()) {
                    try (final ResultSet resultSet = ps.getResultSet()) {
                        return select.apply(resultSet);
                    }
                } else {
                    return update.apply(ps);
                }
            }
        }

        public int execute() throws Exception {
            return execute(resultSet -> {
            });
        }

        public int execute(ConsumerEx<ResultSet> line) throws Exception {
            assert line != null;
            return execute(resultSet -> {
                int count = 0;
                while (resultSet.next()) {
                    line.accept(resultSet);
                    count++;
                }
                return count;
            });
        }

        public void result(ConsumerEx<ResultSet> all) throws Exception {
            assert all != null;
            execute(resultSet -> {
                all.accept(resultSet);
                return 0;
            });
        }

        public void each(ConsumerEx<ResultSet> line) throws Exception {
            assert line != null;
            result(resultSet -> {
                while (resultSet.next()) {
                    line.accept(resultSet);
                }
            });
        }
    }

}
