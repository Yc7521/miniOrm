package orm.sql;

import orm.sql.gen.clauses.Delete;
import orm.sql.gen.clauses.Insert;
import orm.sql.gen.clauses.Select;
import orm.sql.gen.clauses.Update;
import orm.util.meta.Meta;
import orm.util.meta.TableMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Session implements AutoCloseable {
    private final Connection connection;

    public Session(Connection connection) {
        this.connection = connection;
    }

    public <T> T insert(T object) throws SQLException, IllegalAccessException, NoSuchFieldException {
        final Meta<T> meta = Meta.of(object);
        final Statement statement = new Insert<>(meta).generate();
        final int i = execute(statement);
        if (i != 0) {
            meta.setField(new TableMeta<>(meta).getIdColumnName().orElseThrow(), i);
        }
        return meta.getValue();
    }

    public <T> void remove(T object) throws SQLException, IllegalAccessException {
        final Statement statement = new Delete<>(Meta.of(object)).generate();
        execute(statement);
    }

    public <T> T update(T object) throws SQLException, IllegalAccessException {
        final Meta<T> meta = Meta.of(object);
        final Statement statement = new Update<>(meta).generate();
        execute(statement);
        return meta.getValue();
    }

    public <T> Select<T> select(Class<T> clazz) {
        return new Select<>(Meta.of(clazz));
    }

    public <T> T save(T object) throws SQLException, IllegalAccessException, NoSuchFieldException {
        // TODO: insert if not exists or update if exists
        return insert(object);
    }

    public int query(Statement statement) throws SQLException {
        // TODO: make this method return a object
        return execute(statement);
    }

    public int execute(Statement statement) throws SQLException {
        final Object[] params = statement.params();
        try (final PreparedStatement ps = connection.prepareStatement(statement.sql())) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            if (ps.execute()) {
                try (final ResultSet resultSet = ps.getResultSet()) {
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    return count;
                }
            } else {
                // ps.getUpdateCount();
                try {
                    final ResultSet resultSet = ps.getGeneratedKeys();
                    return resultSet.getInt(1);
                } catch (SQLException e) {
                    return 0;
                }
            }
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

}
