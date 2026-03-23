package Classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class DealerTest {

    private Dealer dealer;

    @BeforeEach
    public void setup() {
        dealer = new Dealer("D001", "John Doe", "Colombo", "john@example.com", "0771234567");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("D001", dealer.getDealerId());
        assertEquals("John Doe", dealer.getName());
        assertEquals("Colombo", dealer.getLocation());
        assertEquals("john@example.com", dealer.getEmail());
        assertEquals("0771234567", dealer.getPhoneNumber());
    }

    @Test
    public void testSetters() {
        dealer.setName("Jane");
        dealer.setLocation("Kandy");
        dealer.setEmail("jane@example.com");
        dealer.setPhoneNumber("0712345678");

        assertEquals("Jane", dealer.getName());
        assertEquals("Kandy", dealer.getLocation());
        assertEquals("jane@example.com", dealer.getEmail());
        assertEquals("0712345678", dealer.getPhoneNumber());
    }

    @Test
    public void testAddItemAndResize() {
        for (int i = 0; i < 15; i++) {
            dealer.addItem("Item" + i);
        }

        String[] items = dealer.getItemsSupplied();
        assertTrue(items.length >= 15);  // Resize must have occurred
        assertEquals("Item0", items[0]);
        assertEquals("Item14", items[14]);
    }

    @Test
    public void testSetAndGetItemsSuppliedDirectly() {
        String[] suppliedItems = {"I001", "I002", "I003"};
        dealer.setItemsSupplied(suppliedItems);
        assertArrayEquals(suppliedItems, dealer.getItemsSupplied());
    }
}
