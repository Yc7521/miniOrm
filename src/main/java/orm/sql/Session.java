package orm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Session implements AutoCloseable {
    private final Connection connection;

    public Session(Connection connection) {
        this.connection = connection;
    }

    public int query(Statement statement) throws SQLException {
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
                return ps.getUpdateCount();
            }
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
