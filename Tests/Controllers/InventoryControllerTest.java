package Controllers;

import Classes.Item;
import Classes.MyHashMap;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView.TableViewSelectionModel;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryControllerTest {

    private InventoryController controller;
    private MyHashMap<Item> testItemMap;

    @BeforeAll
    public static void initToolkit() {
        new JFXPanel(); // Initialize JavaFX
    }

    @BeforeEach
    public void setup() {
        controller = new InventoryController();

        controller.inventoryTable = new TableView<>();
        controller.itemCodeField = new TextField();
        controller.nameField = new TextField();
        controller.brandField = new TextField();
        controller.categoryField = new TextField();
        controller.priceField = new TextField();
        controller.quantityField = new TextField();
        controller.lowStockField = new TextField();
        controller.supplierIdField = new TextField();
        controller.imagePathField = new TextField();
        controller.totalItemCountLabel = new Label();
        controller.totalValueLabel = new Label();
        controller.successMessageLabel = new Label();

        testItemMap = new MyHashMap<>();
        setPrivateField(controller, "itemMap", testItemMap);

        controller.inventoryTable.setSelectionModel(new StubSelectionModel<>());
    }

    @Test
    public void testAddItemSuccessfully() {
        fillFields("I001", "Apple", "FreshFarms", "Fruit", "50", "10", "3", "D001", "img/path.jpg");

        Platform.runLater(() -> {
            controller.onAddItem();
            assertEquals(1, testItemMap.getSize());
            assertEquals("Added successfully", controller.successMessageLabel.getText());
            assertTrue(controller.successMessageLabel.isVisible());
        });
        waitForFxEvents();
    }

    @Test
    public void testUpdateItemSuccessfully() {
        Item it = new Item("I002", "Banana", "FarmY", "Fruit", "img", 20.0, 5, 2, LocalDate.now(), new String[]{"D002"});
        testItemMap.put("I002", it);

        fillFields("I002", "UpdatedBanana", "BrandNew", "Fruit", "25", "8", "3", "D002", "img/new.png");

        Platform.runLater(() -> {
            controller.onUpdateItem();
            Item updated = testItemMap.get("I002");
            assertEquals("UpdatedBanana", updated.getName());
            assertEquals(25, updated.getPrice());
        });
        waitForFxEvents();
    }

    @Test
    public void testDeleteItem() {
        Item it = new Item("I003", "Carrot", "FreshFarm", "Vegetable", "img", 15.0, 10, 4, LocalDate.now(), new String[]{"D003"});
        testItemMap.put("I003", it);

        Platform.runLater(() -> {
            controller.inventoryTable.getItems().add(it);
            controller.inventoryTable.getSelectionModel().select(0);
            controller.onDeleteItem();
            assertNull(testItemMap.get("I003"));
        });
        waitForFxEvents();
    }

    @Test
    public void testFieldValidationError() {
        Platform.runLater(() -> {
            controller.itemCodeField.setText("");
            controller.nameField.setText("");
            controller.priceField.setText("-50");
            controller.quantityField.setText("abc");
            controller.lowStockField.setText("-3");
            controller.supplierIdField.setText("");

            List<String> errors = invokeValidateFields();
            assertFalse(errors.isEmpty());
        });
        waitForFxEvents();
    }

    // === Utility Methods ===

    private void fillFields(String code, String name, String brand, String cat,
                            String price, String qty, String threshold,
                            String suppliers, String imgPath) {
        controller.itemCodeField.setText(code);
        controller.nameField.setText(name);
        controller.brandField.setText(brand);
        controller.categoryField.setText(cat);
        controller.priceField.setText(price);
        controller.quantityField.setText(qty);
        controller.lowStockField.setText(threshold);
        controller.supplierIdField.setText(suppliers);
        controller.imagePathField.setText(imgPath);
    }

    private List<String> invokeValidateFields() {
        try {
            var method = InventoryController.class.getDeclaredMethod("validateFields");
            method.setAccessible(true);
            return (List<String>) method.invoke(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForFxEvents() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(latch::countDown);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout waiting for JavaFX event");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // === Minimal StubSelectionModel to prevent JavaFX errors ===
    private static class StubSelectionModel<S> extends TableViewSelectionModel<S> {
        private int selectedIndex = -1;
        private final ObservableList<S> dummy = FXCollections.observableArrayList();

        protected StubSelectionModel() {
            super(new TableView<>());
        }

        @Override public void select(int index) { selectedIndex = index; }
        @Override public void select(S obj) {}
        @Override public void clearAndSelect(int index) { selectedIndex = index; }
        @Override public void selectPrevious() {}
        @Override public void selectNext() {}
        @Override public void selectFirst() {}
        @Override public void selectLast() {}
        @Override public void clearSelection(int index) {}
        @Override public void clearSelection() {}
        @Override public boolean isSelected(int index) { return index == selectedIndex; }
        @Override public boolean isEmpty() { return selectedIndex == -1; }
        @Override public void selectAll() {}
        @Override public ObservableList<Integer> getSelectedIndices() {
            return selectedIndex == -1 ? FXCollections.observableArrayList() : FXCollections.observableArrayList(selectedIndex);
        }
        @Override public ObservableList<S> getSelectedItems() {
            return FXCollections.observableArrayList();
        }
        @Override public ObservableList<TablePosition> getSelectedCells() {
            return FXCollections.observableArrayList();
        }
        @Override public void clearSelection(int row, TableColumn<S, ?> column) {}
        @Override public void clearAndSelect(int row, TableColumn<S, ?> column) {}
        @Override public boolean isSelected(int row, TableColumn<S, ?> column) { return false; }
        @Override public void select(int row, TableColumn<S, ?> column) {}
        @Override public void selectLeftCell() {}
        @Override public void selectRightCell() {}
        @Override public void selectAboveCell() {}
        @Override public void selectBelowCell() {}
    }
}