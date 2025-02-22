package com.adobe.bookstore.order.controller;

import com.adobe.bookstore.order.model.dto.BookOrderDTO;
import com.adobe.bookstore.order.model.entity.BookOrder;
import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.order.service.BookOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/")
public class BookOrderResource {

    private final BookOrderService bookOrderService;

    @Autowired
    public BookOrderResource(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @GetMapping
    public ResponseEntity<List<BookOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(bookOrderService.getAllOrders());
    }

    @PostMapping("new")
    public ResponseEntity<String> newOrder(@RequestBody List<BookRequest> bookRequests) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bookOrderService.createNewOrder(bookRequests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
