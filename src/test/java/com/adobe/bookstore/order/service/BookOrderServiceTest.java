package com.adobe.bookstore.order.service;

import com.adobe.bookstore.order.model.dto.BookOrderDTO;
import com.adobe.bookstore.order.model.entity.BookOrder;
import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.order.repository.BookOrderRepository;
import com.adobe.bookstore.stock.model.entity.BookStock;
import com.adobe.bookstore.stock.service.BookStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookOrderServiceTest {

    @Mock
    private BookOrderRepository bookOrderRepository;

    @Mock
    private BookStockService bookStockService;

    @InjectMocks
    private BookOrderService bookOrderService;

    @Test
    public void shouldReturnAllOrders() {
        BookStock bookStock1 = new BookStock("11111-22222", "Test book 1", 5);
        BookStock bookStock2 = new BookStock("33333-44444", "Test book 2", 3);

        BookOrder order1 = new BookOrder("55555-66666");
        order1.addStock(bookStock1, 2);

        BookOrder order2 = new BookOrder("77777-88888");
        order2.addStock(bookStock1, 3);
        order2.addStock(bookStock2, 1);

        List<BookOrder> bookOrders = new ArrayList<>(){{
            add(order1);
            add(order2);
        }};

        when(bookOrderRepository.findAll()).thenReturn(bookOrders);

        List<BookOrderDTO> result = bookOrderService.getAllOrders();

        assertThat(result.size()).isEqualTo(2);

        for (BookOrderDTO order: result) {
            if (order.getId().equals(order1.getId())) {
                assertThat(order.getBooks().size()).isEqualTo(1);
                assertThat(order.getBooks().get(0).getQuantity()).isEqualTo(2);
            } else if (order.getId().equals(order2.getId()))
                assertThat(order.getBooks().size()).isEqualTo(2);
            else fail("Order ID not expected");
        }
    }

    @Test
    public void shouldCreateNewOrder() {
        BookStock bookStock1 = new BookStock("12345-67890", "Test book 1", 10);
        BookStock bookStock2 = new BookStock("54321-67890", "Test book 2", 7);

        List<BookStock> bookRequestList = new ArrayList<>() {{
            add(bookStock1);
            add(bookStock2);
        }};

        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("12345-67890", 1));
            add(new BookRequest("54321-67890", 7));
        }};

        List<String> bookRequestIds = bookRequestList.stream().map(b -> b.getId()).collect(Collectors.toList());

        BookOrder fakeOrder = new BookOrder(UUID.randomUUID().toString());

        when(bookStockService.getBookStocksById(bookRequestIds)).thenReturn(bookRequestList);
        when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(fakeOrder);

        String result = bookOrderService.createNewOrder(bookRequests);

        assertThat(result.equals(fakeOrder.getId()));
    }

    @Test
    public void shouldNotCreateEmptyOrder() {
        List<BookRequest> bookRequests = new ArrayList<>();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           bookOrderService.createNewOrder(bookRequests);
        });

        assertThat(exception.getMessage()).isEqualTo("Can not create an empty order");
    }

    @Test
    public void shouldThrowInsufficientStockError() {
        BookStock bookStock1 = new BookStock("12345-67890", "Test book 1", 10);
        BookStock bookStock2 = new BookStock("54321-67890", "Test book 2", 7);

        List<BookStock> bookRequestList = new ArrayList<>() {{
            add(bookStock1);
            add(bookStock2);
        }};

        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("12345-67890", 5));
            add(new BookRequest("54321-67890", 8)); // Current test stock for this book is 7
        }};

        List<String> bookRequestIds = bookRequestList.stream().map(b -> b.getId()).collect(Collectors.toList());

        when(bookStockService.getBookStocksById(bookRequestIds)).thenReturn(bookRequestList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookOrderService.createNewOrder(bookRequests);
        });

        assertThat(exception.getMessage().startsWith("Insufficient stock"));
    }

    @Test
    public void shouldThrowInvalidBookError() {
        BookStock bookStock1 = new BookStock("12345-67890", "Test book 1", 10);
        BookStock bookStock2 = new BookStock("54321-67890", "Test book 2", 7);

        List<BookStock> bookRequestList = new ArrayList<>() {{
            add(bookStock1);
            add(bookStock2);
        }};

        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("xxxxx-xxxxx", 5)); // Unexistent book ID in stock
            add(new BookRequest("54321-67890", 7));
        }};

        List<String> bookRequestIds = bookRequestList.stream().map(b -> b.getId()).collect(Collectors.toList());

        when(bookStockService.getBookStocksById(bookRequestIds)).thenReturn(bookRequestList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookOrderService.createNewOrder(bookRequests);
        });

        assertThat(exception.getMessage().contains("does not match any book"));
    }
}
