package Controllers;

import Classes.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

public class DealerController {
    @FXML
    protected TableView<Dealer> dealerTable;

    @FXML
    protected TableView<Item> dealerItemsTable;

    private List<Dealer> selectedDealers = new ArrayList<>();
    private MyHashMap<Item> itemMap = Repository.getInstance().getItemMap();
    private MyHashMap<Dealer> dealerMap = Repository.getInstance().getDealerMap();

    @FXML
    protected TableColumn<Dealer, String> dealerIdCol, dealerNameCol, dealerLocationCol, dealerEmailCol, dealerPhoneCol;

    @FXML
    protected TableColumn<Item, String> itemCodeCol, itemNameCol, itemCategoryCol;
    @FXML
    protected TableColumn<Item, Integer> itemQuantityCol;

    @FXML
    public void initialize() {
        //  Dealer object
        dealerIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDealerId()));
        dealerNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dealerLocationCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        dealerEmailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        dealerPhoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        //  Item object - there is some error in the logic here
        itemCodeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItemCode()));
        itemNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        itemCategoryCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        itemQuantityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
    }

    //  Display 4 dealers - random
    @FXML
    public void onSelectRandomDealers() {
        //  Selecting 4 dealers - random
        List<Dealer> all = dealerMap.getAll();
        Collections.shuffle(all);
        selectedDealers.clear();
        for (int i = 0; i < 4 && i < all.size(); i++) {
            selectedDealers.add(all.get(i));
        }

        // Custom sort by location using insertion sort
        for (int i = 1; i < selectedDealers.size(); i++) {
            Dealer key = selectedDealers.get(i);
            int j = i - 1;
            while (j >= 0 && selectedDealers.get(j).getLocation().compareTo(key.getLocation()) > 0) {
                selectedDealers.set(j + 1, selectedDealers.get(j));
                j--;
            }
            selectedDealers.set(j + 1, key);
        }

        //  Displaying all the selected dealers - by replacing the previous
        dealerTable.getItems().setAll(selectedDealers);
    }

    //
    @FXML
    public void onShowDealerItems() {
        Dealer selected = dealerTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dealerItemsTable.getItems().clear();
            for (String code : selected.getItemsSupplied()) {
                if (code != null) {
                    // System.out.println(code);
                    Item i = itemMap.get(code.trim()); //  itemMap needs to be created on initializing from .txt
                    if (i != null) {
                        dealerItemsTable.getItems().add(i);
                    }
                }
            }
        }
    }

    // Optional setter if itemMap is to be injected
    public void setItemMap(MyHashMap<Item> itemMap) {
        this.itemMap = itemMap;
    }
}
