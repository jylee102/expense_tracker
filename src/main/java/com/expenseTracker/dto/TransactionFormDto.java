package com.expenseTracker.dto;

import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.entity.Transaction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class TransactionFormDto {

    private Long id;

    @NotNull
    private Member member;

    @NotNull(message = "날짜는 필수 입력입니다.")
    private LocalDate date;

    private String description;

    @NotNull(message = "금액은 필수 입력입니다.")
    private int amount;

    @NotNull(message = "자산 분류를 선택해주세요.")
    private Asset asset;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    // modelMapper를 사용
    private static ModelMapper modelMapper = new ModelMapper();

    // dto -> entity
    public Transaction createTransaction() {
        Transaction transaction = modelMapper.map(this, Transaction.class);
        transaction.setDate(LocalDateTime.of(this.date, LocalTime.now()));
        return transaction;
    }

    // entity -> dto
    public static TransactionFormDto of(Transaction transaction) {
        TransactionFormDto transactionFormDto = modelMapper.map(transaction, TransactionFormDto.class);
        transactionFormDto.setDate(transaction.getDate().toLocalDate());
        return transactionFormDto;
    }
}

