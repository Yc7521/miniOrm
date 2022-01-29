package orm.sql.gen.clauses;

import org.junit.Test;
import orm.model.User;
import orm.sql.gen.tr.TypeTranslate;
import orm.util.meta.Meta;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class CreateTableTest {

    @Test
    public void generate() {
        TypeTranslate.register();
        CreateTable<User> table = new CreateTable<>(Meta.of(User.class));
        Logger.getGlobal().info(table.generate());
    }

}