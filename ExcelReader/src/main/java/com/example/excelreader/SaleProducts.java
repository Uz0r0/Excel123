package com.example.excelreader;

import javafx.scene.chart.XYChart;

import java.awt.datatransfer.Clipboard;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SaleProducts {

    private int id;
    private String name;
    private int price;
    private int quantity;
    private int finalPrice;
    private LocalDate date;

    public SaleProducts(int id, String name, int price, int quantity, int finalPrice, LocalDate date){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.date = date;
    }

    @Override
    public String toString() {
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", finalPrice=" + finalPrice + ", date=" + date.format(format1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
