package Controllers;

import Classes.Item;
import Classes.MyHashMap;
import Classes.Repository;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloController {

    @FXML private AnchorPane inventoryPane;
    @FXML protected TableView<Item> lowStockTable;
    @FXML protected Label lowStockStatusLabel;

    @FXML private TableColumn<Item, String> itemCodeCol, nameCol, brandCol, categoryCol, purchaseDateCol;
    @FXML private TableColumn<Item, Number> priceCol, quantityCol, thresholdCol;
    @FXML private TableColumn<Item, Void> imageCol;

    private InventoryController inventoryController;
    private final MyHashMap<Item> itemMap = Repository.getInstance().getItemMap();

    @FXML
    public void initialize() {
        // Load and embed InventoryTab.fxml into the inventoryPane
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryTab.fxml"));
            Parent pane = loader.load();
            inventoryPane.getChildren().setAll(pane);

            inventoryController = loader.getController();
            inventoryController.setHelloController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupLowStockTableColumns();
        loadLowStockItems();
    }

    private void setupLowStockTableColumns() {
        itemCodeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemCode()));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        brandCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand()));
        categoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));
        quantityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));
        thresholdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getLowStockThreshold()));
        purchaseDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPurchaseDate().toString()));

        setupImageColumn();
    }

    private void setupImageColumn() {
        imageCol.setCellFactory((Callback<TableColumn<Item, Void>, TableCell<Item, Void>>) col -> new TableCell<>() {
            private final ImageView view = new ImageView();

            {
                view.setFitWidth(60);
                view.setFitHeight(60);
                view.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    String path = getTableRow().getItem().getImagePath();
                    try {
                        Image img = new Image(new FileInputStream(path));
                        view.setImage(img);
                        setGraphic(view);
                    } catch (IOException e) {
                        setGraphic(null); // Fallback for missing images
                    }
                }
            }
        });
    }

    @FXML
    public void loadLowStockItems() {
        lowStockTable.getItems().clear();
        int count = 0;
        for (Item item : itemMap.getAll()) {
            if (item.getQuantity() < item.getLowStockThreshold()) {
                lowStockTable.getItems().add(item);
                count++;
            }
        }
        lowStockStatusLabel.setText(count + " low-stock item(s)");
    }

    @FXML
    public void onSave() {
        String path = "U:\\AIDS\\SEM 2\\Programming Fundamentals 1601\\Assignment\\DemoTry\\src\\main\\resources\\textFiles\\Items.txt";
        try (PrintWriter pw = new PrintWriter(path)) {
            for (Item it : itemMap.getAll()) {
                pw.println(it.getItemCode() + "," +
                        it.getName() + "," +
                        it.getBrand() + "," +
                        it.getCategory() + "," +
                        it.getImagePath() + "," +
                        it.getPrice() + "," +
                        it.getQuantity() + "," +
                        it.getLowStockThreshold() + "," +
                        it.getPurchaseDate() + "," +
                        String.join(";", it.getSuppliers()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onExit() {
        Platform.exit();
    }

    public MyHashMap<Item> getItemMap() {
        return itemMap;
    }
}
