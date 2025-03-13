package org.example;

public class Item {
    String itemCode;
    double price;
    String weightOrSize;
    String manufactureDate;
    String expiryDate;
    String manufacturer;

    public Item(String itemCode, double price, String weightOrSize, String manufactureDate, String expiryDate, String manufacturer) {
        this.itemCode = itemCode;
        this.price = price;
        this.weightOrSize = weightOrSize;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.manufacturer = manufacturer;
    }

    public double getNetPrice(int quantity) {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "Item Code: " + itemCode + ", Price: $" + price + ", Weight/Size: " + weightOrSize +
                ", Manufacture Date: " + manufactureDate + ", Expiry Date: " + expiryDate +
                ", Manufacturer: " + manufacturer;
    }
}