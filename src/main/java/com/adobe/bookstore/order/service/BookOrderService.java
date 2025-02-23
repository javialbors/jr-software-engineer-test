package com.adobe.bookstore.order.service;

import com.adobe.bookstore.order.model.dto.BookOrderDTO;
import com.adobe.bookstore.order.model.dto.BookOrderStockDTO;
import com.adobe.bookstore.order.model.entity.BookOrder;
import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.order.repository.BookOrderRepository;
import com.adobe.bookstore.order.repository.BookOrderStockRepository;
import com.adobe.bookstore.stock.model.entity.BookStock;
import com.adobe.bookstore.stock.service.BookStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookOrderService {

    private final BookOrderRepository bookOrderRepository;
    private final BookOrderStockRepository bookOrderStockRepository;
    private final BookStockService bookStockService;

    @Autowired
    public BookOrderService(BookOrderRepository bookOrderRepository, BookOrderStockRepository bookOrderStockRepository, BookStockService bookStockService) {
        this.bookOrderRepository = bookOrderRepository;
        this.bookOrderStockRepository = bookOrderStockRepository;
        this.bookStockService = bookStockService;
    }

    public List<BookOrderDTO> getAllOrders() {
        return bookOrderRepository.findAll()
                .stream().map(order -> {
                    List<BookOrderStockDTO> items = order.getOrderStock().stream()
                            .map(orderStock -> new BookOrderStockDTO(orderStock.getStock().getId(), orderStock.getQuantity()))
                            .collect(Collectors.toList());

                    return new BookOrderDTO(order.getId(), order.getCreatedAt(), items);
                }).collect(Collectors.toList());
    }

    public String createNewOrder(List<BookRequest> bookRequests) {
        if (bookRequests.size() == 0)
            throw new RuntimeException("Can not create an empty order");

        List<String> bookRequestIds = bookRequests.stream()
                .map(bookRequest -> bookRequest.getId())
                .collect(Collectors.toList());

        List<BookStock> bookRequestStocks = bookStockService.getBookStocksById(bookRequestIds);

        // Key = ID of requested (existent) books, Value = BookStock object
        Map<String, BookStock> bookRequestStocksMap = bookRequestStocks.stream()
                .collect(Collectors.toMap(
                        bookStock -> bookStock.getId(),
                        bookStock -> bookStock
                ));

        BookOrder orderObj = new BookOrder();

        // Populates the order or throws an error if book ID does not exist in stock or stock quantity is not sufficient
        for (BookRequest bookRequest: bookRequests) {
            if (!bookRequestStocksMap.containsKey(bookRequest.getId()))
                throw new RuntimeException("ID '" + bookRequest.getId() + "' does not match any book");

            BookStock bookStock = bookRequestStocksMap.get(bookRequest.getId());

            if (bookStock.getQuantity() < bookRequest.getQuantity())
                throw new RuntimeException("Insufficient stock for book '" + bookRequest.getId() + "'");

            orderObj.addStock(bookStock, bookRequest.getQuantity());
        }

        bookStockService.updateStocksAsync(bookRequests);

        return bookOrderRepository.save(orderObj).getId();
    }
}

