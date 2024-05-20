package com.expenseTracker.service;


import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.repository.AssetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    // 아이디로 찾기
    public Asset findById(Long id) throws Exception {
        return assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 로그인한 유저가 설정한 카테고리 불러오기
    public List<Asset> getAssets(Long memberId) {
        return assetRepository.findByMemberIdOrderByPosition(memberId);
    }

    // 카테고리 이름과 금액 변경
    public void updateAsset(Long id, String name, int amount) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        asset.setName(name);
        asset.setAmount(amount);
    }

    // 카테고리 순서 변경
    public void updatePosition(Long id, int position) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        asset.setPosition(position);
    }

    // 카테고리 등록
    public Asset addAsset(Asset asset) throws Exception {
        return assetRepository.save(asset);
    }

    // 카테고리 삭제
    public void deleteAsset(Long id) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        assetRepository.delete(asset);
    }

    /* (내역 추가, 변경, 삭제에 따라) 자산 금액 변경 */
    public void updateAmount(Transaction transaction, boolean input) {
        Type categoryType = transaction.getCategory().getType();
        int amount = transaction.getAmount();
        int multiplier1 = categoryType.equals(Type.EXPENSE) ? -1 : 1;
        int multiplier2 = input ? 1 : -1;

        transaction.getAsset().setAmount(transaction.getAsset().getAmount() + amount * multiplier1 * multiplier2);
    }
}
