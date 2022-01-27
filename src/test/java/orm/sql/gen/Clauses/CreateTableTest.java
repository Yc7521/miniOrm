package orm.sql.gen.Clauses;

import org.junit.Test;
import orm.iot.DataPool;
import orm.model.User;
import orm.sql.gen.tr.TypeTranslate;
import orm.util.meta.Meta;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CreateTableTest {

    @Test
    public void generate() {
        TypeTranslate.register();
        CreateTable<User> table = new CreateTable<>(Meta.of(User.class));
        Logger.getGlobal().info(table.generate());
    }

}