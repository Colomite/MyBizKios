package com.kreators.mybizkios;

/**
 * Created by Ivn on 6/19/2018.
 */

public class ObjEntityItem extends ObjEntity {
    int qty;
    float price, subtotal;
    String notes, disc;

    public ObjEntityItem(int id, String code, String name, String notes, int qty, float price, String disc) {
        super(id, code, name);

        this.qty = qty;
        this.notes = notes;
        this.price = price;
        this.disc = disc;
        this.subtotal = UnitGlobal.calcSubTotal(qty, price, UnitGlobal.getDisc(this.disc, this.price, null));
    }

    public ObjEntityItem(int id, String code, String name, String notes, int qty, float price, String disc, String type) {
        super(id, code, name, type);

        this.qty = qty;
        this.notes = notes;
        this.price = price;
        this.disc = disc;
        this.subtotal = UnitGlobal.calcSubTotal(qty, price, UnitGlobal.getDisc(this.disc, this.price, null));
    }

    public String getNotes() {
        return this.notes;
    }

    public int getQty() {
        return this.qty;
    }

    public float getPrice() {
        return this.price;
    }

    public String getDisc() {
        return this.disc;
    }

    public float getSubtotal() {
        return this.subtotal;
    }

    public void update(String notes, int qty, float price, String disc) {
        //this.qty = qty;
        this.notes = notes;
        this.qty = qty;
        this.price = price;
        this.disc = disc;
        this.subtotal = UnitGlobal.calcSubTotal(qty, price, UnitGlobal.getDisc(this.disc, this.price, null));
    }

}
