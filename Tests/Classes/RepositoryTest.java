package Classes;

import Classes.Dealer;
import Classes.Item;
import Classes.MyHashMap;
import Classes.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    @Test
    public void testSingletonInstanceNotNull() {
        Repository repo = Repository.getInstance();
        assertNotNull(repo);
    }

    @Test
    public void testSingletonOnlyOneInstance() {
        Repository repo1 = Repository.getInstance();
        Repository repo2 = Repository.getInstance();
        assertSame(repo1, repo2, "Both instances should be the same (singleton).");
    }

    @Test
    public void testItemMapLoaded() {
        MyHashMap<Item> itemMap = Repository.getInstance().getItemMap();
        assertNotNull(itemMap);
        assertTrue(itemMap.getSize() > 0, "Item map should contain at least one item.");
    }

    @Test
    public void testDealerMapLoaded() {
        MyHashMap<Dealer> dealerMap = Repository.getInstance().getDealerMap();
        assertNotNull(dealerMap);
        assertTrue(dealerMap.getSize() > 0, "Dealer map should contain at least one dealer.");
    }
}
