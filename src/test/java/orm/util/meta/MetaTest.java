package orm.util.meta;

import org.junit.Test;
import orm.model.User;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class MetaTest {
    final Logger global = Logger.getGlobal();

    @Test
    public void testNew()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
             IllegalAccessException, NoSuchFieldException {
        Meta<User> meta = Meta.of(User.class);
        global.info(meta.toString());
        meta = meta.newInstance();
        meta.setField("name", "new");
        meta.setField("password", "new");
        meta.setField("age", 18);
        final User value = meta.getValue();
        global.info(value.toString());
        assertEquals("new", value.getName());
        assertEquals("new", value.getPassword());
        assertEquals(18, value.getAge());
    }

    @Test
    public void testSetAndGet() throws NoSuchFieldException, IllegalAccessException {
        User user = new User("name", "password", 18);
        Meta<User> meta = Meta.of(user);
        assertEquals("name", meta.getFieldValue("name"));
        assertEquals("password", meta.getFieldValue("password"));
        assertEquals(18, meta.getFieldValue("age"));
        meta.setField("name", "test");
        meta.setField("password", "test");
        meta.setField("age", 20);

        global.info(meta.getValue().toString());
    }
}