package com.my.batch.repository;

import com.my.batch.domain.Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, String> {
    Page<Exchange> findAllByNameContaining(String keyword, Pageable pageable);

}
