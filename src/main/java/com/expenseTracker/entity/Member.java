package com.expenseTracker.entity;

import com.expenseTracker.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk값의 타입은 참조타입 Long으로 지정

    private String name; // String 사이즈를 지정하지 않으면 -> varchar(255)

    @Column(unique = true) // 유니크 제약조건(값이 중복되면 안 되는 컬럼)
    private String email;

    private String password;

    private String phone;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        // 패스워드 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());

        Member member = new Member();

        // 사용자가 입력한 회원가입 정보를 member 엔티티로 변환
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPhone(memberFormDto.getPhone());
        member.setPassword(password);

        return member;
    }

    public void update(MemberFormDto memberFormDto) {
        this.name = memberFormDto.getName();
        this.phone = memberFormDto.getPhone();
    }
}
