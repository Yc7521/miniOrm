package orm.sql;

import org.junit.Before;
import org.junit.Test;
import orm.model.User;
import orm.sql.gen.clauses.CreateTable;
import orm.sql.gen.tr.TypeTranslate;
import orm.util.meta.Meta;

import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SessionTest {
    private Session session;
    private Logger logger;
    private User user;

    @Before
    public void getSession() {
        final SessionFactory sessionFactory = new SessionFactory();
        sessionFactory.set(
          "com.mysql.cj.jdbc.Driver",
          "jdbc:mysql://localhost:3306/test",
          "root",
          "123456"
        );
        session = sessionFactory.getSession();
        logger = Logger.getGlobal();
        user = new User("test_1", "test_password", 18);
        TypeTranslate.register();
        assertNotNull(session);
    }

    @Test
    public void createTable() throws SQLException {
        CreateTable<User> createTable = new CreateTable<>(Meta.of(User.class));
        final Statement generate = createTable.generate();
        session.execute(createTable.drop());
        logger.info(String.valueOf(session.execute(generate)));
    }

    @Test
    public void delete() throws SQLException, IllegalAccessException {
        session.remove(user);
    }

    @Test
    public void insert() throws SQLException, IllegalAccessException, NoSuchFieldException {
        delete();
        logger.info(String.valueOf(session.insert(user)));
    }

    @Test
    public void select() throws SQLException, NoSuchFieldException, IllegalAccessException {
        insert();
        assertEquals(
          1,
          session.query(session.select(User.class).where().by("name", "test_1").build())
        );
    }

    @Test
    public void query() throws SQLException {
//        Select<course> select;
//        {
//            select = new Select<>(Meta.of(course.class));
//            final int line = session.query(select.generate());
//            assertEquals(5, line);
//        }
//        {
//            select = new Select<>(Meta.of(course.class));
//            final int line = session.query(select.where().by("Cid", 1000).build());
//            assertEquals(0, line);
//        }
//        {
//            select = new Select<>(Meta.of(course.class));
//            final int line = session.query(select.where().by("Cid", 1001).build());
//            assertEquals(1, line);
//        }
    }
}
