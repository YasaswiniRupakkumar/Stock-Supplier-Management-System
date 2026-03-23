package Classes;

public class Repository {
    private static Repository instance;         // 1. Static instance
    private final MyHashMap<Item> itemMap;        // 2. The shared map
    private final MyHashMap<Dealer> dealerMap;

    private Repository() {                      // 3. Private constructor
        itemMap = InitialDataLoader.loadItemMap();
        dealerMap = InitialDataLoader.loadDealerMap();
    }

    public static synchronized Repository getInstance() {    // 4. Public access method
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public MyHashMap<Item> getItemMap() {         // 5. Getter for the map
        return itemMap;
    }

    public MyHashMap<Dealer> getDealerMap() {         // 5. Getter for the map
        return dealerMap;
    }
}
