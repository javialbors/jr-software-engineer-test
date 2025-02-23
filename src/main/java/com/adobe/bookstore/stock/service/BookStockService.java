package com.adobe.bookstore.stock.service;

import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.stock.model.entity.BookStock;
import com.adobe.bookstore.stock.repository.BookStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    @Async
    @Transactional
    public void updateStocksAsync(List<BookRequest> bookRequests) {
        for (BookRequest bookRequest: bookRequests) {
            try {
                Optional<BookStock> bookStockOpt = getBookStockById(bookRequest.getId());

                if (bookStockOpt.isPresent()) {
                    BookStock bookStock = bookStockOpt.get();
                    bookStock.setQuantity(bookStock.getQuantity() - bookRequest.getQuantity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
