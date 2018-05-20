package com.example.android.delivery.Models;

/**
 * Created by ASUS on 08/05/2018.
 */

public class Product {
    private String product_image_path;
    private String product_name;
    private String id;
    private String product_description;
    private String product_price;

    public Product(String product_image_path, String product_name, String product_id, String product_description, String product_price) {
        this.product_image_path = product_image_path;
        this.product_name = product_name;
        this.id = product_id;
        this.product_description = product_description;
        this.product_price = product_price;
    }

    public Product() {
    }

    public String getProduct_image_path() {
        return product_image_path;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getId() {
        return id;
    }

    public String getProduct_description() {
        return product_description;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_image_path='" + product_image_path + '\'' +
                ", product_name='" + product_name + '\'' +
                ", id='" + id + '\'' +
                ", product_description='" + product_description + '\'' +
                ", product_price='" + product_price + '\'' +
                '}';
    }
}
