package orm.sql;

import org.junit.Before;
import org.junit.Test;
import orm.sql.gen.clauses.Select;
import orm.util.meta.Meta;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SessionTest {

    private Session session;

    @Before
    public void getSession() throws ClassNotFoundException {
        final SessionFactory sessionFactory = new SessionFactory();
        sessionFactory.set("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/test",
                "root", "123456");
        session = sessionFactory.getSession();
        assertNotNull(session);
    }

    @Test
    public void queryTest() throws SQLException {
        Select<course> select;
        {
            select = new Select<>(Meta.of(course.class));
            final int line = session.query(select.generate());
            assertEquals(5, line);
        }
        {
            select = new Select<>(Meta.of(course.class));
            final int line = session.query(select.where().by("Cid", 1000).build());
            assertEquals(0, line);
        }
        {
            select = new Select<>(Meta.of(course.class));
            final int line = session.query(select.where().by("Cid", 1001).build());
            assertEquals(1, line);
        }
    }
}

class course {
}