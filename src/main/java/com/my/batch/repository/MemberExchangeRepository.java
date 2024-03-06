package com.my.batch.repository;

import com.my.batch.domain.MemberExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberExchangeRepository extends JpaRepository<MemberExchange, Integer> {
    @Query("SELECT me FROM MemberExchange me JOIN FETCH me.member JOIN FETCH me.exchange WHERE me.member.email = :email")
    List<MemberExchange> findMemberExchanges(String email);
}
