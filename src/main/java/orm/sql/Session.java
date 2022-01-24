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

    public int query(String sql, Object... args) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            if (statement.execute()) {
                try (final ResultSet resultSet = statement.getResultSet()) {
                    int count = 0;
                    while (resultSet.next()) {
                        count++;
                    }
                    return count;
                }
            } else {
                return statement.getUpdateCount();
            }
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
