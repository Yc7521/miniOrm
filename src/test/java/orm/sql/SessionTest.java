package orm.sql;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SessionTest {

    private Session session;

    @Before
    public void getSession() throws ClassNotFoundException {
        final SessionFactory sessionFactory = new SessionFactory();
        sessionFactory.set("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "root", "123456");
        session = sessionFactory.getSession();
        assertNotNull(session);
    }

    @Test
    public void queryTest() throws SQLException {
        assertEquals(5, session.query("SELECT * FROM course;"));
        assertEquals(0, session.query("SELECT * FROM course WHERE Cid=?;", 1000));
        assertEquals(1, session.query("SELECT * FROM course WHERE Cid=?;", 1001));
    }
}