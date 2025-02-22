package com.adobe.bookstore.order.model.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookOrderStockPK implements Serializable {

    private String stockId;
    private String orderId;

    public BookOrderStockPK() {}

    public BookOrderStockPK(String stockId, String orderId) {
        this.stockId = stockId;
        this.orderId = orderId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookOrderStockPK that = (BookOrderStockPK) o;
        return Objects.equals(stockId, that.stockId) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId, orderId);
    }
}
