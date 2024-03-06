package com.my.batch.repository;

import com.my.batch.domain.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

}
