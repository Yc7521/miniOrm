package orm.sql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        session.removeAll(User.class);
    }

    @Test
    public void insert()
      throws SQLException, IllegalAccessException, NoSuchFieldException {
        delete();
        session.insert(user);
        var list = new User[]{new User("test_2", "none2", 18), new User(
          "test_3",
          "none3",
          18
        ), new User("test_4", "none4", 18),};
        session.saveAll(list);
    }

    @Test
    public void select() throws Exception {
        // init the data
        insert();
        // create the select statement
        final Statement statement = session.select(User.class).where().by(
          "name",
          "test_1"
        ).end();
        // Test: execute the statement
        assertEquals(1, session.execute(statement));
        // Test: get the result
        final User data = session.query(User.class, statement);
        assertEquals(user, data);
        // Log: get all the data
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info(gson.toJson(session.queryAll(
          User.class,
          session.select(User.class).end()
        )));
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
