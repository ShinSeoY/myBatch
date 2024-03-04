//package com.my.batch.security;
//
//import com.szs.ShinSeoyoung.domain.Member;
//import lombok.Getter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.List;
//
//@Getter
//public class MyUserDetails extends User {
//
//    private Member member;
//
//    public MyUserDetails(Member member) {
//        super(member.getUserId(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        this.member = member;
//    }
//}
