package com.adobe.bookstore.stock.service;

import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.stock.model.entity.BookStock;
import com.adobe.bookstore.stock.repository.BookStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookStockServiceTest {

    @Mock
    private BookStockRepository bookStockRepository;

    @InjectMocks
    private BookStockService bookStockService;

    @Test
    public void shouldReturnStockById() {
        BookStock bookStock = new BookStock("12345-67890", "Test book", 3);

        when(bookStockRepository.findById(bookStock.getId())).thenReturn(Optional.of(bookStock));

        Optional<BookStock> result = bookStockService.getBookStockById(bookStock.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getQuantity()).isEqualTo(3);
    }

    @Test
    public void shouldReturnStockListByIds() {
        BookStock bookStock1 = new BookStock("12345-67890", "Test book 1", 3);
        BookStock bookStock2 = new BookStock("98765-43210", "Test book 2", 5);
        BookStock bookStock3 = new BookStock("12312-31231", "Test book 3", 7);

        List<BookStock> bookStockList = new ArrayList<>() {{
            add(bookStock1);
            add(bookStock2);
            add(bookStock3);
        }};

        List<String> bookStockIds = bookStockList.stream().map(b -> b.getId()).collect(Collectors.toList());

        when(bookStockRepository.findAllById(bookStockIds)).thenReturn(bookStockList);

        List<BookStock> result = bookStockService.getBookStocksById(bookStockIds);

        assertThat(result.size()).isEqualTo(3);

        Map<String, BookStock> bookStockMap = result.stream().collect(Collectors.toMap(
                b -> b.getId(),
                b -> b
        ));

        for (BookStock bookStock: bookStockList) {
            assertThat(bookStockMap.containsKey(bookStock.getId())).isEqualTo(true);
            assertThat(bookStockMap.get(bookStock.getId()).getQuantity()).isEqualTo(bookStock.getQuantity());
        }
    }

    @Test
    public void shouldReturnUpdatedStock() {
        BookStock bookStock1 = new BookStock("12345-67890", "Test book 1", 3);
        BookStock bookStock2 = new BookStock("98765-43210", "Test book 2", 5);
        BookStock bookStock3 = new BookStock("12312-31231", "Test book 3", 7);

        when(bookStockRepository.findById(bookStock1.getId())).thenReturn(Optional.of(bookStock1));
        when(bookStockRepository.findById(bookStock2.getId())).thenReturn(Optional.of(bookStock2));
        when(bookStockRepository.findById(bookStock3.getId())).thenReturn(Optional.of(bookStock3));

        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest(bookStock1.getId(), 2));
            add(new BookRequest(bookStock2.getId(), 5));
            add(new BookRequest(bookStock3.getId(), 3));
        }};

        bookStockService.updateStocksAsync(bookRequests);

        assertThat(bookStock1.getQuantity()).isEqualTo(1);
        assertThat(bookStock2.getQuantity()).isEqualTo(0);
        assertThat(bookStock3.getQuantity()).isEqualTo(4);
    }
}
