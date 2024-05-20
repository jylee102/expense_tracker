package com.expenseTracker.service;

import com.expenseTracker.constant.Type;
import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // id로 찾기
    public Transaction findById(Long id) throws Exception {
        return transactionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 등록(insert)
    public Transaction saveTransaction(TransactionFormDto transactionFormDto) throws Exception {
        Transaction transaction = transactionFormDto.createTransaction(); // dto -> entity

        LocalDateTime dateTime = transactionFormDto.getDate().atTime(LocalTime.now());
        transaction.setDate(dateTime);

        transactionRepository.save(transaction); // insert

        return transaction;
    }

    // 리스트 페이지
    @Transactional(readOnly = true)
    public Page<Transaction> getPage(Long memberId, int year, int month, String searchValue, Pageable pageable) throws Exception {
        return transactionRepository.findByMemberIdAndDateMonth(memberId, year, month, searchValue, pageable);
    }

    // 리스트(달력)
    @Transactional(readOnly = true)
    public List<Transaction> getList(Long memberId, int year, int month) throws Exception {
        return transactionRepository.getList(memberId, year, month);
    }

    // 내역 정보 가져오기
    @Transactional(readOnly = true)
    public Transaction getDtl(Long itemId) throws Exception {

        return transactionRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 수정(edit)
    public Transaction updateTransaction(TransactionFormDto transactionFormDto) throws Exception {

        Transaction transaction = transactionRepository.findById(transactionFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        transaction.update(transactionFormDto);

        return transaction;
    }

    // 삭제(delete)
    public void deleteTransaction(Long transactionId) throws Exception {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(EntityNotFoundException::new);

        transactionRepository.delete(transaction);
    }

    // 카테고리별 수입/지출을 계산
    public int calcTotalAmountForCategory(Category category, int year, int month) throws Exception {
        return transactionRepository.calcTotalAmountForCategory(category.getId(), year, month);
    }

    // 수입/지출의 총액
    public double getTotalAmountOf(Type type, int year, int month, Long memberId) throws Exception {
        return transactionRepository.getTotalAmountOf(type, year, month, memberId);
    }
}
