package com.adobe.bookstore.stock.service;

import com.adobe.bookstore.stock.model.BookStock;
import com.adobe.bookstore.stock.repository.BookStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStockService {

    private final BookStockRepository bookStockRepository;

    @Autowired
    public BookStockService(BookStockRepository bookStockRepository) {
        this.bookStockRepository = bookStockRepository;
    }

    public Optional<BookStock> getBookStockById(String bookId) {
        return bookStockRepository.findById(bookId);
    }

    public List<BookStock> getBookStocksById(List<String> ids) {
        return bookStockRepository.findAllById(ids);
    }
}
