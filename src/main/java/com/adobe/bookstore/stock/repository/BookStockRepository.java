package com.adobe.bookstore.stock.repository;

import com.adobe.bookstore.stock.model.entity.BookStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStockRepository extends JpaRepository<BookStock, String> {
}
