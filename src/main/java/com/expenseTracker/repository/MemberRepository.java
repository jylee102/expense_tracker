package com.expenseTracker.repository;

import com.expenseTracker.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // select * from member where email = ?
    Member findByEmail(String email); // 회원가입시 중복된 회원이 있는지 이메일로 확인
}
