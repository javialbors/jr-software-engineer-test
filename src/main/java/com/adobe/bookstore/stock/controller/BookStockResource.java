package com.adobe.bookstore.stock.controller;

import com.adobe.bookstore.stock.service.BookStockService;
import com.adobe.bookstore.stock.model.entity.BookStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books_stock/")
public class BookStockResource {

    private final BookStockService bookStockService;

    @Autowired
    public BookStockResource(BookStockService bookStockService) {
        this.bookStockService = bookStockService;
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookStock> getStockById(@PathVariable String bookId) {
        return bookStockService.getBookStockById(bookId)
                .map(bookStock -> ResponseEntity.ok(bookStock))
                .orElse(ResponseEntity.notFound().build());
    }
}
