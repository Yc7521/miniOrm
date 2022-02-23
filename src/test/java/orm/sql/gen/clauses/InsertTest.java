package orm.sql.gen.clauses;

import org.junit.Test;
import orm.model.User;
import orm.sql.Statement;
import orm.util.meta.Meta;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class InsertTest {
    @Test
    public void generate() throws IllegalAccessException {
        User user = new User("test", "pass", 18);
        Insert<User> insert = new Insert<>(Meta.of(user));
        final Statement statement = insert.generate();
        assertEquals(
          "INSERT INTO `user` (`name`, `password`, `age`) VALUES (?, ?, ?)",
          statement.sql()
        );
        assertArrayEquals(new Object[]{"test", "pass", 18}, statement.params());
    }
}