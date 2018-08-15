package com.kreators.mybizkios.report.model;

public class BestProductModel
{
    private String itemName;
    private int qty;
    private String price;
    private String totalPrice;
    private String totalTrans;

    public String getTotalTrans() {
        return totalTrans;
    }

    public void setTotalTrans(String totalTrans) {
        this.totalTrans = totalTrans;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
