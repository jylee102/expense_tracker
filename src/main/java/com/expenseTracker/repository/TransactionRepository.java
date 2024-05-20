package com.expenseTracker.repository;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE YEAR(t.date) = :year AND MONTH(t.date) = :month " +
            "AND t.member.id = :memberId " +
            "AND (LOWER(t.category.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "order by t.date desc")
    Page<Transaction> findByMemberIdAndDateMonth(@Param("memberId") Long memberId,
                                                 @Param("year") int year, @Param("month") int month,
                                                 @Param("searchValue") String searchValue,
                                                 Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.member.id = :memberId AND YEAR(t.date) = :year AND MONTH(t.date) = " +
            ":month")
    List<Transaction> getList(@Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.category.id = :categoryId AND YEAR(t.date) =" +
            " :year AND MONTH(t.date) = :month")
    int calcTotalAmountForCategory(@Param("categoryId") Long categoryId, @Param("year") int year,
                                   @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.category.type = :categoryType " +
            "AND YEAR(t.date) = :year AND MONTH(t.date) = :month " +
            "AND t.member.id = :memberId")
    double getTotalAmountOf(@Param("categoryType") Type categoryType, @Param("year") int year,
                            @Param("month") int month, @Param("memberId") Long memberId);
}
