package com.expenseTracker.config;


import com.expenseTracker.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberContext extends User {
    // authentication 객체에 저장하고 싶은 값을 필드로 지정
    private final String phone;
    private final String name;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities); // User 생성자 실행
        this.phone = member.getPhone();
        this.name = member.getName();
    }
}
