package Classes;

import Classes.MyHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {

    private MyHashMap<String> map;

    @BeforeEach
    public void setUp() {
        map = new MyHashMap<>();
    }

    @Test
    public void testPutAndGet() {
        map.put("A001", "Apple");
        map.put("B002", "Banana");
        assertEquals("Apple", map.get("A001"));
        assertEquals("Banana", map.get("B002"));
    }

    @Test
    public void testDuplicateKeyReturnsNull() {
        map.put("A001", "Apple");
        assertNull(map.put("A001", "Another Apple"));
    }

    @Test
    public void testGetInvalidKeyReturnsNull() {
        assertNull(map.get("InvalidKey"));
    }

    @Test
    public void testDelete() {
        map.put("A001", "Apple");
        assertTrue(map.delete("A001"));
        assertNull(map.get("A001"));
        assertFalse(map.delete("A001")); // already deleted
    }

    @Test
    public void testUpdate() {
        map.put("A001", "Apple");
        assertTrue(map.update("A001", "Avocado"));
        assertEquals("Avocado", map.get("A001"));

        assertFalse(map.update("X999", "DoesNotExist")); // key not present
    }

    @Test
    public void testSizeAndGetAll() {
        map.put("A001", "Apple");
        map.put("B002", "Banana");
        assertEquals(2, map.getSize());

        List<String> all = map.getAll();
        assertTrue(all.contains("Apple"));
        assertTrue(all.contains("Banana"));
    }

    @Test
    public void testResize() {
        // Add more than 10 * 0.75 = 7.5 items to trigger resize
        for (int i = 0; i < 20; i++) {
            assertNotNull(map.put("K" + i, "Value" + i));
        }
        assertEquals(20, map.getSize());
        assertEquals("Value5", map.get("K5"));
    }
}
