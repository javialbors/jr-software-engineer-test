package com.adobe.bookstore.order.controller;

import com.adobe.bookstore.order.model.dto.BookOrderDTO;
import com.adobe.bookstore.order.model.request.BookRequest;
import com.adobe.bookstore.order.service.BookOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookOrderResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'Test book', 3)")
    public void shouldReturnNewOrderId() {
        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("12345-67890", 1));
        }};

        var result = restTemplate.postForObject("http://localhost:" + port + "/orders/new", bookRequests, String.class);

        assertThat(UUID.fromString(result)).isNotNull();
    }

    @Test
    @DirtiesContext
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'Test book', 3)")
    public void shouldReturnInsufficientStockError() {
        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("12345-67890", 4));
        }};

        var result = restTemplate.postForObject("http://localhost:" + port + "/orders/new", bookRequests, String.class);

        assertThat(result).startsWith("Insufficient");
    }

    @Test
    @DirtiesContext
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'Test book', 3)")
    public void shouldReturnInvalidBookError() {
        List<BookRequest> bookRequests = new ArrayList<>(){{
            add(new BookRequest("54321-67890", 1));
        }};

        var result = restTemplate.postForObject("http://localhost:" + port + "/orders/new", bookRequests, String.class);

        assertThat(result).contains("does not match");
    }

    @Test
    @DirtiesContext
    @Sql(statements = {
            "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'Test book 1', 3)",
            "INSERT INTO book_stock (id, name, quantity) VALUES ('11111-22222', 'Test book 2', 15)",
            "INSERT INTO book_order (id, created_at) VALUES ('67890-12345', '2025-02-23T13:55:40.312958')",
            "INSERT INTO book_order (id, created_at) VALUES ('99999-12345', '2025-02-21T13:55:40.312958')",
            "INSERT INTO book_order_stock (order_id, stock_id, quantity) VALUES ('67890-12345', '12345-67890', 2)",
            "INSERT INTO book_order_stock (order_id, stock_id, quantity) VALUES ('67890-12345', '11111-22222', 3)",
            "INSERT INTO book_order_stock (order_id, stock_id, quantity) VALUES ('99999-12345', '12345-67890', 1)"
    })
    public void shouldReturnAllOrders() {
        var result = restTemplate.getForObject("http://localhost:" + port + "/orders/", BookOrderDTO[].class);

        assertThat(result.length).isEqualTo(2);

        for (BookOrderDTO order: result) {
            if (order.getId().equals("67890-12345"))
                assertThat(order.getBooks().size() == 2);

            if (order.getId().equals("99999-12345"))
                assertThat(order.getBooks().size() == 1);
        }
    }
}
