package com.my.batch.repository;

import com.my.batch.domain.MemberExchange;
import com.my.batch.domain.MemberExchangeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberExchangeRepository extends JpaRepository<MemberExchange, MemberExchangeId> {
    @Query("SELECT me FROM MemberExchange me JOIN FETCH me.member JOIN FETCH me.exchange WHERE me.member.email = :email")
    List<MemberExchange> findMemberExchanges(String email);

    @Modifying
    @Query("DELETE FROM MemberExchange me WHERE me.id.memberId = :memberId AND me.id.exchangeUnit =:exchangeUnit")
    void deleteByMemberIdAndExchangeUnit(Integer memberId, String exchangeUnit);
}
