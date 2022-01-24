package orm.iot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataPoolTest {
    DataPool pool;

    @Before
    public void getInstance() {
        pool = DataPool.getInstance();
        assertNotNull(pool);
    }

    public void register() {
        assertTrue(pool.register(new Device("test")));
    }

    @Test
    public void registerAndGet() {
        register();
        Device device;
        device = pool.get(Device.class);
        assertEquals("test", device.name);
    }

    static class Device {
        String name;

        public Device(String name) {
            this.name = name;
        }
    }
}
