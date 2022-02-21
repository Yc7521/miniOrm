package orm.sql.gen.clauses;

import org.junit.Test;
import orm.model.User;
import orm.sql.Statement;
import orm.util.meta.Meta;

import static org.junit.Assert.*;

public class SelectTest {
    @Test
    public void generateEmpty() {
        Select<User> userSelect = new Select<>(Meta.of(User.class));
        assertEquals("SELECT * FROM User", userSelect.generate().sql());
    }

    @Test
    public void genWithParam() {
        Select<User> userSelect = new Select<>(Meta.of(User.class));
        final Statement build = userSelect.where().byId("name").build();
        assertEquals("SELECT * FROM User WHERE name = ?", build.sql());
        assertEquals("name", build.params()[0]);
    }

    @Test
    public void genWithParam2() {
        Select<User> userSelect = new Select<>(Meta.of(User.class));
        final Statement build = userSelect
                .where()
                    .byId("xxx")
                .and()
                    .by("age", 18)
                .build();
        assertEquals("SELECT * FROM User WHERE name = ? AND age = ?", build.sql());
        assertEquals("xxx", build.params()[0]);
        assertEquals(18, build.params()[1]);
    }
}