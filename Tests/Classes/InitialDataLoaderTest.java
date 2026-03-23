package Classes;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class InitialDataLoaderTest {

    @Test
    public void testLoadItemMap() {
        MyHashMap<Item> itemMap = InitialDataLoader.loadItemMap();
        assertNotNull(itemMap);
        assertTrue(itemMap.getSize() > 0, "Item map should not be empty");

        Item item = itemMap.get("I002");
        assertNotNull(item, "Item I002 should exist");
        assertEquals("Apple Juice (1L)", item.getName());
        assertEquals("Beverages", item.getCategory());
        assertEquals(1237.42, item.getPrice(), 0.01);
    }

    @Test
    public void testLoadDealerMap() {
        MyHashMap<Dealer> dealerMap = InitialDataLoader.loadDealerMap();
        assertNotNull(dealerMap);
        assertTrue(dealerMap.getSize() > 0, "Dealer map should not be empty");

        Dealer dealer = dealerMap.get("D001");
        assertNotNull(dealer, "Dealer D001 should exist");
        assertEquals("Tech World", dealer.getName());
        assertTrue(Arrays.asList(dealer.getItemsSupplied()).contains("I001"));
    }
}
