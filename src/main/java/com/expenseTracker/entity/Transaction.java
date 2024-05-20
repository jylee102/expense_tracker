package com.expenseTracker.entity;

import com.expenseTracker.dto.TransactionFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private int amount;
    private String description;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Transaction() {
    }

    public Transaction(LocalDateTime date, int amount, String description, Asset asset, Category category, Member member) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.asset = asset;
        this.category = category;
        this.member = member;
    }

    public void update(TransactionFormDto transactionFormDto) {
        this.date = transactionFormDto.getDate().atTime(LocalTime.now());
        this.description = transactionFormDto.getDescription();
        this.amount = transactionFormDto.getAmount();
        this.asset = transactionFormDto.getAsset();
        this.category = transactionFormDto.getCategory();
    }
}
