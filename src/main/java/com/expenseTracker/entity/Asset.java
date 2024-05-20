package com.expenseTracker.entity;

import com.expenseTracker.repository.AssetRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "assets")
@Getter
@Setter
@ToString
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int amount;

    private int position;

    public Asset() {}

    public Asset(String name, Member member, int amount, int position) {
        this.name = name;
        this.member = member;
        this.amount = amount;
        this.position = position;
    }
}
