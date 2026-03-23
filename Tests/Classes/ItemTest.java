package Classes;
import Classes.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Item item;
    private final String[] suppliers = {"D001", "D002"};

    @BeforeEach
    public void setUp() {
        item = new Item("I001", "LG Monitor", "LG", "Monitor", "img/I001.png",
                500.0, 10, 3, LocalDate.of(2024, 7, 20), suppliers);
    }

    @Test
    public void testGetters() {
        assertEquals("I001", item.getItemCode());
        assertEquals("LG Monitor", item.getName());
        assertEquals("LG", item.getBrand());
        assertEquals("Monitor", item.getCategory());
        assertEquals("img/I001.png", item.getImagePath());
        assertEquals(500.0, item.getPrice());
        assertEquals(10, item.getQuantity());
        assertEquals(3, item.getLowStockThreshold());
        assertEquals(LocalDate.of(2024, 7, 20), item.getPurchaseDate());
        assertArrayEquals(suppliers, item.getSuppliers());
    }

    @Test
    public void testSetters() {
        item.setName("LG OLED");
        item.setBrand("LG Pro");
        item.setCategory("Display");
        item.setImagePath("img/I002.png");
        item.setPrice(899.99);
        item.setQuantity(5);
        item.setLowStockThreshold(2);
        item.setPurchaseDate(LocalDate.of(2024, 8, 1));
        item.setSuppliers(new String[]{"D003"});

        assertEquals("LG OLED", item.getName());
        assertEquals("LG Pro", item.getBrand());
        assertEquals("Display", item.getCategory());
        assertEquals("img/I002.png", item.getImagePath());
        assertEquals(899.99, item.getPrice());
        assertEquals(5, item.getQuantity());
        assertEquals(2, item.getLowStockThreshold());
        assertEquals(LocalDate.of(2024, 8, 1), item.getPurchaseDate());
        assertArrayEquals(new String[]{"D003"}, item.getSuppliers());
    }
}
