package com.adobe.bookstore.stock.model.entity;

import com.adobe.bookstore.order.model.entity.BookOrderStock;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Version
    @JsonIgnore
    @Column(name = "version", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long version;

    @JsonIgnore
    @OneToMany(mappedBy = "stock")
    List<BookOrderStock> orderStock;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public BookStock() {}

    public BookStock(String id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}