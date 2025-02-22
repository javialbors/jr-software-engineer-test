package com.adobe.bookstore.order.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookOrderDTO {

    private String id;
    private LocalDateTime createdAt;
    private List<BookOrderStockDTO> books;

    public BookOrderDTO(String id, LocalDateTime createdAt, List<BookOrderStockDTO> books) {
        this.id = id;
        this.createdAt = createdAt;
        this.books = books;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<BookOrderStockDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookOrderStockDTO> books) {
        this.books = books;
    }
}
