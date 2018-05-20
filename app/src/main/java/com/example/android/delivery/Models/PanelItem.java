package com.example.android.delivery.Models;

/**
 * Created by ASUS on 10/05/2018.
 */

public class PanelItem {
    private String product_image_path;
    private String product_name;
    private String product_id;
    private String product_amount;
    private String product_price;

    public PanelItem(String product_image_path, String product_name, String product_id, String product_amount, String product_price) {
        this.product_image_path = product_image_path;
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_amount = product_amount;
        this.product_price = product_price;
    }

    public PanelItem() {
    }

    public String getProduct_image_path() {
        return product_image_path;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_image_path(String product_image_path) {
        this.product_image_path = product_image_path;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    @Override
    public String toString() {
        return "PanelItem{" +
                "product_image_path='" + product_image_path + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_amount='" + product_amount + '\'' +
                ", product_price='" + product_price + '\'' +
                '}';
    }
}
