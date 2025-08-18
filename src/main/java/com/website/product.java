package com.website;

import java.util.*;

public class product {
    private String type;
    private int ID;
    private ArrayList<String> name;
    private int quantity;
    private float price;
    private ArrayList<String> imagePath;   

    public product(String type, int ID, String name, int quantity, int price, ArrayList<String> imagePath) {
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

    public ArrayList<String> getName() {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        if (imagePath == null) {
            this.imagePath.clear();
        } else {
            this.imagePath = imagePath;
        }
    }

    public void addImagePath(String imagePath) {
        if (this.imagePath != null) {
            this.imagePath.add(imagePath);
        }
    }

}
