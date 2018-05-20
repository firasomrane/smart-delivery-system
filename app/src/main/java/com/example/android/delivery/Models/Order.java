package com.example.android.delivery.Models;

import java.util.ArrayList;

/**
 * Created by ASUS on 16/05/2018.
 */

public class Order {
    private String expected_delevery_time;
    private ArrayList<PanelItem> order_elements;
    private String delivered;
    private String order_time;
    private int order_sum;

    public Order(String expected_delevery_time, ArrayList<PanelItem> order_elements, String delivered, String order_time, int order_sum) {
        this.expected_delevery_time = expected_delevery_time;
        this.order_elements = order_elements;
        this.delivered = delivered;
        this.order_time = order_time;
        this.order_sum = order_sum;
    }

    public Order() {
    }

    public String getExpected_delevery_time() {
        return expected_delevery_time;
    }

    public void setExpected_delevery_time(String expected_delevery_time) {
        this.expected_delevery_time = expected_delevery_time;
    }

    public ArrayList<PanelItem> getOrder_elements() {
        return order_elements;
    }

    public void setOrder_elements(ArrayList<PanelItem> order_elements) {
        this.order_elements = order_elements;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public int getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(int order_sum) {
        this.order_sum = order_sum;
    }

    @Override
    public String toString() {
        return "Order{" +
                "expected_delevery_time='" + expected_delevery_time + '\'' +
                ", order_elements=" + order_elements +
                ", delivered=" + delivered +
                ", order_sum=" + order_sum +
                ", order_time='" + order_time + '\'' +
                '}';
    }
}
