package com.adobe.bookstore.order.repository;

import com.adobe.bookstore.order.model.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, String> {
}
