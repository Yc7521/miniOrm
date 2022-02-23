package orm.sql;

import orm.sql.gen.clauses.Delete;
import orm.sql.gen.clauses.Insert;
import orm.sql.gen.clauses.Select;
import orm.sql.gen.clauses.Update;
import orm.util.meta.Meta;
import orm.util.meta.TableMeta;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Session implements AutoCloseable {
    private final Connection connection;

    public Session(Connection connection) {
        this.connection = connection;
    }

    public <T> T insert(T object)
      throws SQLException, IllegalAccessException, NoSuchFieldException {
        final Meta<T> meta = Meta.of(object);
        final Statement statement = new Insert<>(meta).generate();
        final int i = execute(statement);
        if (i != 0) {
            meta.setField(new TableMeta<>(meta).getIdColumnName().orElseThrow(), i);
        }
        return meta.getValue();
    }

    public <T> void insertAll(T... objects)
      throws SQLException, IllegalAccessException, NoSuchFieldException {
        for (T object : objects) {
            insert(object);
        }
    }

    public <T> void remove(T object) throws SQLException, IllegalAccessException {
        final Statement statement = new Delete<>(Meta.of(object)).generate();
        execute(statement);
    }
    public <T> void removeAll(Class<T> clazz) throws SQLException, IllegalAccessException {
        final Statement statement = new Delete<>(Meta.of(clazz)).generate();
        execute(statement);
    }

    public <T> T update(T object) throws SQLException, IllegalAccessException {
        final Meta<T> meta = Meta.of(object);
        final Statement statement = new Update<>(meta).generate();
        execute(statement);
        return meta.getValue();
    }

    public <T> Select<T>.SelectBuilder select(Class<T> clazz) {
        return new Select<>(Meta.of(clazz)).builder();
    }

    public <T> boolean existsId(T value)
      throws SQLException, NoSuchFieldException, IllegalAccessException {
        final Meta<T> meta = Meta.of(value);
        final TableMeta<T> meta1 = new TableMeta<>(meta);
        final Statement statement = new Select<>(meta)
          .builder()
          .where()
          .byId(meta1.getIdColumnValue().orElseThrow())
          .end();
        try {
            AtomicBoolean exists = new AtomicBoolean(false);
            statement.load(this).result(rs -> {
                if (rs.next()) {
                    exists.set(true);
                }
            });
            return exists.get();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T save(T value)
      throws SQLException, NoSuchFieldException, IllegalAccessException {
        if (existsId(value)) {
            return update(value);
        }
        return insert(value);
    }

    public <T> void saveAll(T... values)
      throws SQLException, NoSuchFieldException, IllegalAccessException {
        for (T value : values) {
            save(value);
        }
    }

    public <T> T query(Class<T> clazz, Statement statement)
      throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        final Meta<T> meta = Meta.of(clazz).newInstance();
        try {
            statement.load(this).result(rs -> {
                if (rs.next()) {
                    mapping(rs, meta);
                } else {
                    throw new IllegalStateException("No result found");
                }
            });
        } catch (IllegalStateException e) {
            return null;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return meta.getValue();
    }

    public <T> List<T> queryAll(Class<T> clazz, Statement statement)
      throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        final Meta<T> meta = Meta.of(clazz);
        List<T> list = new java.util.ArrayList<>();
        try {
            statement.load(this).each(rs -> {
                final Meta<T> temp = meta.newInstance();
                mapping(rs, temp);
                list.add(temp.getValue());
            });
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * get value from result set by column name and put it into {@code meta}
     *
     * @param meta make sure the meta has value
     */
    private <T> void mapping(ResultSet rs, Meta<T> meta)
      throws SQLException, NoSuchFieldException, IllegalAccessException {
        assert meta != null && meta.hasValue() : "meta must have value";
        assert rs != null;
        final String[] names = meta.getFieldNames();
        for (String name : names) {
            meta.setField(name, rs.getObject(name));
        }
    }

    public int execute(Statement statement) throws SQLException {
        try {
            return statement.load(this).execute();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

}
