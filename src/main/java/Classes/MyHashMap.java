package Classes;

import java.util.*;

public class MyHashMap <T>{
    int capacity, size;
    List<List<Pair<String, T>>> hashMap;

    public MyHashMap() {
        size = 0;
        capacity = 10;
        hashMap = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++) {
            hashMap.add(new ArrayList<>());
        }
    }

    private int hashFunction(String key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private double calLoadFactor() {
        return (double) size / capacity;
    }

    private void resize() {
        capacity = capacity * 2;
        List<List<Pair<String, T>>> tempHashMap = new ArrayList<>(capacity);
        for(int i = 0; i < capacity; i++) {
            tempHashMap.add(new ArrayList<>());
        }

        for(List<Pair<String, T>> bucket : hashMap) {
            for(Pair<String, T> pair : bucket) {
                tempHashMap = putCall(pair.key, pair.value, tempHashMap, false);
            }
        }

        this.hashMap = tempHashMap;
    }

    public List<List<Pair<String, T>>> put(String key, T data) {
        return putCall(key, data, this.hashMap, true);
    }

    // Main - add
    private List<List<Pair<String, T>>> putCall(String key, T data, List<List<Pair<String, T>>> targetArray, boolean addSize) {
        if (addSize) {
            if (calLoadFactor() > 0.75) {
                resize();
                targetArray = this.hashMap;
            }
        }
        int index = hashFunction(key);
        List<Pair<String, T>> bucket = targetArray.get(index);

        for(Pair<String, T> pair: bucket){
            if(pair.key.equals(key)) {
                return null;
            }
        }

        bucket.add(new Pair<>(key, data));
        if (addSize) {
            size++;
        }
        return targetArray;
    }

    // Main - View, update
    public T get(String key) {
        int index = hashFunction(key);
        for(Pair<String, T> pair : hashMap.get(index)) {
            if(pair.key.equals(key)) {
                return pair.value;
            }
        }
        return null;
    }

    // Main - delete
    public boolean delete(String key) {
        int index = hashFunction(key);

        Iterator<Pair<String, T>> it = hashMap.get(index).iterator();

        while(it.hasNext()) {
            Pair<String, T> pair = it.next();
            if (pair.key.equals(key)) {
                it.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    // Main - update
    public boolean update(String key, T value) {
        int index = hashFunction(key);

        List<Pair<String, T>> bucket = hashMap.get(index);

        for(Pair<String, T> pair : bucket) {
            if (pair.key.equals(key)) {
                pair.value = value;
                return true;
            }
        }
        return false;
    }

    public List<T> getAll() {
        List<T> all = new ArrayList<>();
        for (List<Pair<String, T>> bucket : hashMap) {
            for(Pair<String, T> pair : bucket) {
                all.add(pair.value); // Adding each T Object
            }
        }
        return all;
    }

}
