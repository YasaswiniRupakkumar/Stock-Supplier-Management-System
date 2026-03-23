package Classes;
import java.time.LocalDate;

public class Item {
    String itemCode,  name, brand, category, imagePath;
    String[] suppliers;
    double price;
    int quantity, lowStockThreshold;
    LocalDate purchaseDate;

    public Item(String ic, String n, String b, String c, String ip, Double p, int q, int lst, LocalDate pd, String[] s) {
        this.itemCode = ic;
        this.name = n;
        this.brand = b;
        this.category = c;
        this.imagePath = ip;
        this.suppliers = s;
        this.price = p;
        this.quantity = q;
        this.lowStockThreshold = lst;
        this.purchaseDate = pd;
    }

    public Item(String ic, String n, String b, String c, Double p, int q, int lst, LocalDate pd, String[] s) {
        this.itemCode = ic;
        this.name = n;
        this.brand = b;
        this.category = c;
        this.suppliers = s;
        this.price = p;
        this.quantity = q;
        this.lowStockThreshold = lst;
        this.purchaseDate = pd;
    }

    public void setItemCode(String ic) {
        this.itemCode = ic;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setBrand(String b) {
        this.brand = b;
    }

    public String getBrand() {
        return brand;
    }

    public void setCategory(String c) {
        this.category = c;
    }

    public String getCategory() {
        return category;
    }

    public void setSuppliers(String[] s) {
        this.suppliers = s;
    }

    public String[] getSuppliers() {
        return suppliers;
    }

    public void setPrice(double p) {
        this.price = p;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setLowStockThreshold(int lst) {
        this.lowStockThreshold = lst;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setPurchaseDate(LocalDate pd) {
        this.purchaseDate = pd;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setImagePath(String ip) {
        this.imagePath = ip;
    }

    public String getImagePath() {
        return imagePath;
    }
}
