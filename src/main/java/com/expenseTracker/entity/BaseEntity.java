package com.expenseTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class}) // audit 기능을 사용하기 위해 작성
@MappedSuperclass // 다른 엔티티에서 부모클래스로 사용하기 위해
@Getter
@Setter
public abstract class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createBy;
}
