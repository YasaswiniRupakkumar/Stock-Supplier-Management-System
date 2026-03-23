package Classes;

public class Dealer {
    String dealerId, name, location, email, phoneNumber;
    String[] itemsSupplied;
    int topI, capacityI;

    public Dealer(String di, String n, String l, String e, String pn, String[] is) {
        this.dealerId = di;
        this.name = n;
        this.location = l;
        this.email = e;
        this.phoneNumber = pn;
        this.itemsSupplied = is;
        this.capacityI = itemsSupplied.length;
        topI = -1;
    }

    public Dealer(String di, String n, String l, String e, String pn) {
        this.dealerId = di;
        this.name = n;
        this.location = l;
        this.email = e;
        this.phoneNumber = pn;
        this.topI = -1;
        this.capacityI = 10;
        this.itemsSupplied = new String[capacityI];
    }

    public void setDealerId(String di) {
        this.dealerId = di;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String l) {
        this.location = l;
    }

    public String getLocation() {
        return location;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getEmail() {
        return email;
    }

    public void setPhoneNumber(String pn) {
        this.phoneNumber = pn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setItemsSupplied(String[] is) {
        this.itemsSupplied = is;
    }

    public void addItem(String i) {
        if(topI + 1 == capacityI) {
            resizeI();
        }
        itemsSupplied[++topI] = i;
    }

    public String[] getItemsSupplied() {
        return itemsSupplied;
    }

    public void resizeI() {
        capacityI = capacityI * 2;
        String[] newArr = new String [capacityI];

        System.arraycopy(itemsSupplied, 0, newArr, 0, itemsSupplied.length);

        itemsSupplied = newArr;
    }
}
