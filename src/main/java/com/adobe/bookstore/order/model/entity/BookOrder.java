package com.adobe.bookstore.order.model.entity;

import com.adobe.bookstore.stock.model.BookStock;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_order")
@JsonSerialize
public class BookOrder {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<BookOrderStock> orderStock = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public BookOrder() {}

    public BookOrder(String id) {
        this.id = id;
    }

    public void addStock(BookStock book, Integer quantity) {
        BookOrderStockPK pk = new BookOrderStockPK();

        pk.setOrderId(this.id);
        pk.setStockId(book.getId());

        BookOrderStock bookOrderStock = new BookOrderStock(pk, this, book, quantity);

        orderStock.add(bookOrderStock);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
