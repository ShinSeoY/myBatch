package com.my.batch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberExchangeId implements Serializable {

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "exchange_id")
    private Integer exchangeId;

}
