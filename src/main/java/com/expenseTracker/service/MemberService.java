package com.expenseTracker.service;

import com.expenseTracker.config.MemberContext;
import com.expenseTracker.constant.Type;
import com.expenseTracker.dto.MemberFormDto;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.repository.AssetRepository;
import com.expenseTracker.repository.CategoryRepository;
import com.expenseTracker.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional // 하나의 메소드가 트랜잭션으로 묶인다.(DB Exception 혹은 다른 Exception 발생시 롤백)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;

    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        saveDefaults(member);

        return member;
    }

    private void saveDefaults(Member member) {
        String[] incomeCategories = {"급여", "용돈", "금융소득"};
        String[] expenseCategories = {"식비", "교통/차량", "패션/미용", "문화생활", "경조사비"};
        String[] assetNames = {"현금", "은행", "카드", "저축"};

        // 수입 카테고리 생성 및 저장
        for (int i = 0; i < incomeCategories.length; i++) {
            Category category = new Category(incomeCategories[i], Type.INCOME, member, "cloud", i + 1);
            categoryRepository.save(category);
        }

        // 지출 카테고리 생성 및 저장
        for (int i = 0; i < expenseCategories.length; i++) {
            Category category = new Category(expenseCategories[i], Type.EXPENSE, member, "cloud", i + 1);
            categoryRepository.save(category);
        }

        // 자산 생성 및 저장
        for (int i = 0; i < assetNames.length; i++) {
            Asset asset = new Asset(assetNames[i], member, 0, i + 1);
            assetRepository.save(asset);
        }
    }

    // 회원 중복체크
    private void validateDuplicateMember(Member member) {
        Member findMember = getMember(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 이메일로 회원 찾기
    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }

    // 회원정보 업데이트
    public Long updateMember(MemberFormDto memberFormDto) {
        Member member = getMember(memberFormDto.getEmail());
        member.update(memberFormDto);

        return member.getId();
    }

    // 비밀번호 확인(현재 비밀번호와 같은지)
    public Boolean checkPassword(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    // 비밀번호 변경
    public void updatePassword(Member member, String rawPassword) {
        String password = passwordEncoder.encode(rawPassword);
        member.setPassword(password);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //해당 email 계정을 가진 사용자가 있는지 확인
        Member member = getMember(email);

        if (member == null) {//사용자가 없다면
            throw new UsernameNotFoundException(email);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new MemberContext(member, authorities);
    }
}
