package Controllers;

import Classes.Dealer;
import Classes.Item;
import Classes.MyHashMap;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DealerControllerTest {

    private DealerController controller;
    private MyHashMap<Dealer> mockDealerMap;
    private MyHashMap<Item> mockItemMap;

    private TableView<Dealer> dealerTableMock;
    private TableView<Item> dealerItemsTableMock;

    @BeforeEach
    public void setup() {
        controller = new DealerController();

        // Initialize JavaFX environment
        new JFXPanel();

        // Create TableViews
        dealerTableMock = new TableView<>();
        dealerItemsTableMock = new TableView<>();
        controller.dealerTable = dealerTableMock;
        controller.dealerItemsTable = dealerItemsTableMock;

        // 🟡 Initialize ALL TableColumns used in the controller
        controller.dealerIdCol = new TableColumn<>();
        controller.dealerNameCol = new TableColumn<>();
        controller.dealerLocationCol = new TableColumn<>();
        controller.dealerEmailCol = new TableColumn<>();
        controller.dealerPhoneCol = new TableColumn<>();

        controller.itemCodeCol = new TableColumn<>();
        controller.itemNameCol = new TableColumn<>();
        controller.itemCategoryCol = new TableColumn<>();
        controller.itemQuantityCol = new TableColumn<>();

        // Setup mock data
        mockDealerMap = new MyHashMap<>();
        mockItemMap = new MyHashMap<>();

        Dealer d1 = new Dealer("D001", "Alice", "Colombo", "alice@mail.com", "0771234567", new String[]{"I001", "I002"});
        Dealer d2 = new Dealer("D002", "Bob", "Kandy", "bob@mail.com", "0777654321", new String[]{"I003"});
        Dealer d3 = new Dealer("D003", "Charlie", "Galle", "charlie@mail.com", "0779999999", new String[]{"I004"});
        Dealer d4 = new Dealer("D004", "David", "Jaffna", "david@mail.com", "0778888888", new String[]{"I005"});

        mockDealerMap.put("D001", d1);
        mockDealerMap.put("D002", d2);
        mockDealerMap.put("D003", d3);
        mockDealerMap.put("D004", d4);

        Item i1 = new Item("I001", "Item A", "BrandX", "CategoryX", "img/I001.png", 100.0, 10, 5, LocalDate.now(), new String[]{"D001"});
        Item i2 = new Item("I002", "Item B", "BrandY", "CategoryY", "img/I002.png", 200.0, 3, 2, LocalDate.now(), new String[]{"D001"});
        Item i3 = new Item("I003", "Item C", "BrandZ", "CategoryZ", "img/I003.png", 300.0, 7, 4, LocalDate.now(), new String[]{"D002"});

        mockItemMap.put("I001", i1);
        mockItemMap.put("I002", i2);
        mockItemMap.put("I003", i3);

        controller.setItemMap(mockItemMap);
        setPrivateField(controller, "dealerMap", mockDealerMap);

        // 👇 Now safe to call initialize
        controller.initialize();
    }

    @Test
    public void testRandomDealerSelectionAndSorting() {
        controller.onSelectRandomDealers();
        ObservableList<Dealer> selected = controller.dealerTable.getItems();
        assertEquals(4, selected.size());

        for (int i = 0; i < selected.size() - 1; i++) {
            String loc1 = selected.get(i).getLocation();
            String loc2 = selected.get(i + 1).getLocation();
            assertTrue(loc1.compareTo(loc2) <= 0, "Dealers not sorted by location");
        }
    }

    @Test
    public void testShowDealerItems() {
        controller.onSelectRandomDealers();

        Dealer knownDealer = mockDealerMap.get("D001");
        dealerTableMock.getItems().setAll(Arrays.asList(knownDealer));
        dealerTableMock.getSelectionModel().select(knownDealer);

        controller.onShowDealerItems();

        ObservableList<Item> items = controller.dealerItemsTable.getItems();
        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> i.getItemCode().equals("I001")));
        assertTrue(items.stream().anyMatch(i -> i.getItemCode().equals("I002")));
    }

    // Helper method to inject private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Reflection failed", e);
        }
    }
}
