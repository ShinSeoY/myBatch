package com.my.batch.repository;

import com.my.batch.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);

//    @Modifying
//    @Query("UPDATE Member m SET m.emailEnabled = :isEnabledEmail, m.smsEnabled = :isEnabledSms WHERE m.id = :id")
//    void updateNotification(Integer id, boolean isEnabledEmail, boolean isEnabledSms);

}
