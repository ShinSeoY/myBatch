package com.my.batch.security;

import com.my.batch.domain.Member;
import com.my.batch.exception.error.NotFoundUserException;
import com.my.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> found = memberRepository.findByEmail(email);
        Member member = found.orElseThrow(() -> new NotFoundUserException());
        return new MyUserDetails(member);
    }
}
