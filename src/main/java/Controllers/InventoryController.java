package Controllers;

import Classes.Item;
import Classes.MyHashMap;
import Classes.Repository;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryController {

    @FXML protected TableView<Item> inventoryTable;
    @FXML protected TableColumn<Item, String> itemCodeCol, nameCol, brandCol, categoryCol, purchaseDateCol;
    @FXML protected TableColumn<Item, Number> priceCol, quantityCol;
    @FXML protected TableColumn<Item, ImageView> imageCol;

    @FXML protected Label totalItemCountLabel, totalValueLabel, successMessageLabel;

    @FXML protected TextField itemCodeField, nameField, brandField, categoryField;
    @FXML protected TextField priceField, quantityField, lowStockField, supplierIdField, imagePathField;

    private MyHashMap<Item> itemMap = Repository.getInstance().getItemMap();
    private HelloController helloController;

    public void setHelloController(HelloController controller) {
        this.helloController = controller;
    }

    @FXML
    public void initialize() {
        itemCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getItemCode()));
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        brandCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBrand()));
        categoryCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategory()));
        priceCol.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getPrice()));
        quantityCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getQuantity()));
        purchaseDateCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPurchaseDate().toString()));

        imageCol.setCellValueFactory(d -> {
            ImageView iv = new ImageView(new Image("file:" + d.getValue().getImagePath(), 40, 40, true, true));
            return new SimpleObjectProperty<>(iv);
        });

        inventoryTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> populateFields(newVal));

        setupDragDrop();

        successMessageLabel.setVisible(false);
        onRefresh();
    }

    private void populateFields(Item it) {
        if (it == null) return;
        itemCodeField.setText(it.getItemCode());
        nameField.setText(it.getName());
        brandField.setText(it.getBrand());
        categoryField.setText(it.getCategory());
        priceField.setText(String.valueOf(it.getPrice()));
        quantityField.setText(String.valueOf(it.getQuantity()));
        lowStockField.setText(String.valueOf(it.getLowStockThreshold()));
        supplierIdField.setText(String.join(";", it.getSuppliers()));
        imagePathField.setText(it.getImagePath());
    }

    private void setupDragDrop() {
        imagePathField.setOnDragOver(e -> { if(e.getDragboard().hasFiles()) e.acceptTransferModes(); });
        imagePathField.setOnDragDropped(e -> {
            var files = e.getDragboard().getFiles();
            if (!files.isEmpty()) imagePathField.setText(files.get(0).getAbsolutePath());
            e.setDropCompleted(true);
        });
    }

    @FXML public void onBrowseImage() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("img", "*.jpg", "*.png", "*.jpeg"));
        var f = fc.showOpenDialog(inventoryTable.getScene().getWindow());
        if (f != null) imagePathField.setText(f.getAbsolutePath());
    }

    @FXML public void onAddItem() { processAddUpdate(true); }

    @FXML public void onUpdateItem() { processAddUpdate(false); }

    private void processAddUpdate(boolean isNew) {
        List<String> err = validateFields();
        if (!err.isEmpty()) { showAlert(String.join("\n", err)); return; }

        String code = itemCodeField.getText().trim();
        String[] sup = supplierIdField.getText().trim().split(";");
        String img = imagePathField.getText().trim();
        Item it = new Item(code,
                nameField.getText().trim(),
                brandField.getText().trim(),
                categoryField.getText().trim(),
                img,
                Double.parseDouble(priceField.getText().trim()),
                Integer.parseInt(quantityField.getText().trim()),
                Integer.parseInt(lowStockField.getText().trim()),
                LocalDate.now(),
                sup);

        if (isNew) {
            if (itemMap.put(code, it) == null) { showAlert("Item code exists"); return; }
            showSuccess("Added successfully");
        } else {
            itemMap.update(code, it);
            showSuccess("Updated successfully");
        }

        onRefresh();
        if (helloController != null) helloController.loadLowStockItems();
    }

    @FXML public void onDeleteItem() {
        Item sel = inventoryTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Select to delete"); return; }
        itemMap.delete(sel.getItemCode());
        showSuccess("Deleted successfully");
        onRefresh();
        if (helloController != null) helloController.loadLowStockItems();
    }

    @FXML public void onRefresh() {
        inventoryTable.getItems().setAll(getSortedItems());
        clearFields();
        updateSummary();
    }

    private List<Item> getSortedItems() {
        var list = new ArrayList<>(itemMap.getAll());
        list.sort(Comparator.comparing(Item::getCategory));
        return list;
    }

    private void updateSummary() {
        int c = itemMap.getSize();
        double v = itemMap.getAll().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        totalItemCountLabel.setText("Total Items: " + c);
        totalValueLabel.setText(String.format("Total Inventory Value: Rs. %.2f", v));
    }

    private List<String> validateFields() {
        var err = new ArrayList<String>();

        if (itemCodeField.getText().isBlank()) err.add("Item Code is required.");
        else if (itemCodeField.getText().contains(",")) err.add("Item Code cannot contain commas.");

        if (nameField.getText().isBlank()) err.add("Name is required.");
        else if (nameField.getText().contains(",")) err.add("Name cannot contain commas.");

        if (brandField.getText().isBlank()) err.add("Brand is required.");
        else if (brandField.getText().contains(",")) err.add("Brand cannot contain commas.");

        if (categoryField.getText().isBlank()) err.add("Category is required.");
        else if (categoryField.getText().contains(",")) err.add("Category cannot contain commas.");

        if (priceField.getText().isBlank()) err.add("Price is required.");
        else {
            try { if (Double.parseDouble(priceField.getText()) < 0) err.add("Price ≥ 0"); }
            catch(Exception e) { err.add("Invalid price format."); }
        }

        if (quantityField.getText().isBlank()) err.add("Quantity is required.");
        else {
            try { if (Integer.parseInt(quantityField.getText()) < 0) err.add("Quantity ≥ 0"); }
            catch(Exception e) { err.add("Invalid quantity format."); }
        }

        if (lowStockField.getText().isBlank()) err.add("Low Stock Threshold is required.");
        else {
            try { if (Integer.parseInt(lowStockField.getText()) < 0) err.add("Threshold ≥ 0"); }
            catch(Exception e) { err.add("Invalid threshold format."); }
        }

        if (supplierIdField.getText().isBlank()) err.add("Supplier ID is required.");
        else if (supplierIdField.getText().contains(",")) err.add("Supplier ID cannot contain commas. Use `;` to separate multiple suppliers.");

        if (!imagePathField.getText().isBlank() && imagePathField.getText().contains(",")) {
            err.add("Image path cannot contain commas.");
        }

        return err;
    }


    private void clearFields() {
        itemCodeField.clear(); nameField.clear(); brandField.clear();
        categoryField.clear(); priceField.clear(); quantityField.clear();
        lowStockField.clear(); supplierIdField.clear(); imagePathField.clear();
        inventoryTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        var a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }

    private void showSuccess(String msg) {
        successMessageLabel.setText(msg);
        successMessageLabel.setStyle("-fx-border-color: green; -fx-text-fill: green;");
        successMessageLabel.setVisible(true);
        PauseTransition p = new PauseTransition(Duration.seconds(30));
        p.setOnFinished(e -> successMessageLabel.setVisible(false));
        p.play();
    }
}
