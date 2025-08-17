package com.website;

import java.util.*;

public class product {
    private String type;
    private int ID;
    private List<String> name;
    private int quantity;
    private int price;
    private List<String> imagePath;   

    public product(String type, int ID, String name, int quantity, int price, List<String> imagePath) {
        this.type = type;
        this.ID = ID;
        this.name = new ArrayList<>();
        for (String n : name.split(" ")) {
            this.name.add(n.trim());
        }
        this.quantity = quantity;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }   

    public int getID() {
        return ID;
    }   

    public void setID(int ID) {
        this.ID = ID;
    }   

    public List<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.clear();
        for (String n : name.split(" ")) {
            this.name.add(n.trim());
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(List<String> imagePath) {
        this.imagePath = imagePath;
    }

}
