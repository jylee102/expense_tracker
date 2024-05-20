package com.expenseTracker.entity;

import com.expenseTracker.constant.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String color;

    private int position; // 순서

    public Category() {}

    public Category(String name, Type type) {
        this.name = name;
        this.type = type;
        this.color = "cloud";
    }

    public Category(String name, Type type, Member member, String color, int position) {
        this.name = name;
        this.type = type;
        this.member = member;
        this.color = color;
        this.position = position;
    }

}
