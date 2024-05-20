package com.expenseTracker.repository;

import com.expenseTracker.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    // select * from assets where member_id = ? order by position
    List<Asset> findByMemberIdOrderByPosition(Long memberId);
}
