//package com.my.batch.security;
//
//import com.szs.ShinSeoyoung.domain.Member;
//import com.szs.ShinSeoyoung.exception.error.NotFoundUserException;
//import com.szs.ShinSeoyoung.repository.member.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Member> found = memberRepository.findByUserId(username);
//        Member member = found.orElseThrow(() -> new NotFoundUserException());
//        return new MyUserDetails(member);
//    }
//}
