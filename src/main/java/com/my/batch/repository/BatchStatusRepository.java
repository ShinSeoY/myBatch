package com.my.batch.repository;

import com.my.batch.domain.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchStatusRepository extends JpaRepository<BatchStatus, Integer> {

}
