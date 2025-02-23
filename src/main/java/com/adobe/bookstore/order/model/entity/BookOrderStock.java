package com.adobe.bookstore.order.model.entity;

import com.adobe.bookstore.stock.model.entity.BookStock;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "book_order_stock")
public class BookOrderStock {

    @EmbeddedId
    private BookOrderStockPK id;

    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private BookOrder order;

    @MapsId("stockId")
    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    @JsonIgnore
    private BookStock stock;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public BookOrderStock() {}

    public BookOrderStock(BookOrderStockPK id, BookOrder order, BookStock stock, Integer quantity) {
        this.id = id;
        this.order = order;
        this.stock = stock;
        this.quantity = quantity;
    }

    public BookOrderStockPK getId() {
        return id;
    }

    public void setId(BookOrderStockPK id) {
        this.id = id;
    }

    public BookOrder getOrder() {
        return order;
    }

    public void setOrder(BookOrder order) {
        this.order = order;
    }

    public BookStock getStock() {
        return stock;
    }

    public void setStock(BookStock stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
