package com.adobe.bookstore.stock.model;

import com.adobe.bookstore.order.model.entity.BookOrderStock;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "book_stock")
@JsonSerialize
public class BookStock {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToMany(mappedBy = "stock")
    List<BookOrderStock> orderStock;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BookOrderStock> getOrderStock() {
        return orderStock;
    }

    public void setOrderStock(List<BookOrderStock> orderStock) {
        this.orderStock = orderStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}