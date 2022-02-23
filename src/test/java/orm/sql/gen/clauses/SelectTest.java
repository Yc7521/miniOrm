package orm.sql.gen.clauses;

import org.junit.Test;
import orm.model.User;
import orm.sql.Statement;
import orm.util.meta.Meta;

import static org.junit.Assert.assertEquals;

public class SelectTest {
    @Test
    public void generateEmpty() {
        Select<User> userSelect = new Select<>(Meta.of(User.class));
        assertEquals("SELECT * FROM `user`", userSelect.generate().sql());
    }

    @Test
    public void genWithParam() {
        Select<User>.SelectBuilder userSelect
          = new Select<>(Meta.of(User.class)).builder();
        final Statement build = userSelect.where().byId("name").end();
        assertEquals("SELECT * FROM `user` WHERE `name` = ?", build.sql());
        assertEquals("name", build.params()[0]);
    }

    @Test
    public void genWithParam2() {
        Select<User>.SelectBuilder userSelect
          = new Select<>(Meta.of(User.class)).builder();
        final Statement build = userSelect.where().byId("xxx").and().by("age", 18).end();
        assertEquals("SELECT * FROM `user` WHERE `name` = ? AND `age` = ?", build.sql());
        assertEquals("xxx", build.params()[0]);
        assertEquals(18, build.params()[1]);
    }
}