package com.expenseTracker.repository;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // select * from categories where member_id = ? and category_type = ? order by sequence
    List<Category> findByMemberIdAndTypeOrderByPosition(Long memberId, Type type);
}
