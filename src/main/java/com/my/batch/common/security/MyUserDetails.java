package com.my.batch.common.security;

import com.my.batch.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MyUserDetails extends User {

    private Member member;

    public MyUserDetails(Member member) {
        super(member.getEmail(), member.getPhone(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
