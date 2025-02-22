package com.adobe.bookstore.order.repository;

import com.adobe.bookstore.order.model.entity.BookOrderStock;
import com.adobe.bookstore.order.model.entity.BookOrderStockPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderStockRepository extends JpaRepository<BookOrderStock, BookOrderStockPK> {
}
