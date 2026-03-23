package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class InitialDataLoader {
    //  Load Item hashmap
    public static MyHashMap<Item> loadItemMap() {
        MyHashMap<Item> itemMap = new MyHashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader("U:\\AIDS\\SEM 2\\Programming Fundamentals 1601\\Assignment\\DemoTry\\src\\main\\resources\\textFiles\\Items.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length >= 10) {
                    String[] suppliers = Arrays.stream(parts[9].split(";")).map(String::trim).toArray(String[]::new);
                    itemMap.put(parts[0].trim(), new Item(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), Double.parseDouble(parts[5].trim()), Integer.parseInt(parts[6].trim()), Integer.parseInt(parts[7].trim()), LocalDate.parse(parts[8].trim()), suppliers));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return itemMap;
    }

    //  Load Dealer hashmap
    public static MyHashMap<Dealer> loadDealerMap() {
        MyHashMap<Dealer> dealerMap = new MyHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("U:\\AIDS\\SEM 2\\Programming Fundamentals 1601\\Assignment\\DemoTry\\src\\main\\resources\\textFiles\\Dealers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String[] items = Arrays.stream(parts[5].split(";")).map(String::trim).toArray(String[]::new);
                    dealerMap.put(parts[0].trim(), new Dealer(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), items));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dealerMap;
    }
}
