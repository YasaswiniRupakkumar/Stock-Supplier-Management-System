package Controllers;

import Classes.Item;
import Classes.MyHashMap;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest {

    private HelloController controller;

    @BeforeEach
    public void setUp() {
        // Initialize JavaFX runtime
        new JFXPanel();

        controller = new HelloController();

        // Inject dummy UI elements
        controller.lowStockTable = new TableView<>();
        controller.lowStockStatusLabel = new Label();

        // Add test data to the itemMap
        MyHashMap<Item> mockMap = controller.getItemMap();
        mockMap.put("I019", new Item("I001", "TestItem1", "BrandA", "CategoryX", "img/I001.png", 100.0, 2, 5, LocalDate.now(), new String[]{"D001"}));
        mockMap.put("I020", new Item("I002", "TestItem2", "BrandB", "CategoryY", "img/I002.png", 200.0, 10, 4, LocalDate.now(), new String[]{"D002"}));
    }

    @Test
    public void testLoadLowStockItems() {
        Platform.runLater(() -> {
            controller.loadLowStockItems();
            assertEquals(7, controller.lowStockTable.getItems().size());
            assertTrue(controller.lowStockStatusLabel.getText().contains("7 low-stock"));
        });
    }

    @Test
    public void testItemMapInjection() {
        assertNotNull(controller.getItemMap());
        assertEquals(17, controller.getItemMap().getSize());
    }
}
