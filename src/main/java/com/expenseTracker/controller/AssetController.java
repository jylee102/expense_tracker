package com.expenseTracker.controller;

import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import com.expenseTracker.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AssetController {

    private final MemberService memberService;
    private final AssetService assetService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @PostMapping("/asset/updateAsset")
    public @ResponseBody ResponseEntity updateName(@RequestBody Asset req, Principal principal) {
        try {
            updateDifference(req, principal);
            assetService.updateAsset(req.getId(), req.getName(), req.getAmount());
            
            return new ResponseEntity<>(new String[]{req.getName(), String.valueOf(req.getAmount())}, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트 도중 문제가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    
    // 자산 금액을 업데이트 할 때 차액을 내역에 저장
    private void updateDifference(Asset asset, Principal principal) {

        Category category;
        try {
            Asset originalAsset = assetService.findById(asset.getId());
            int difference = asset.getAmount() - originalAsset.getAmount();

            if (difference == 0) { // 금액 동일(내역 추가 불필요)
                return;
            } else if (difference < 0) { // 더 적은 금액으로 수정(지출)
                category = categoryService.findById(1L);
            } else { // 더 큰 금액으로 수정(수입)
                category = categoryService.findById(2L);
            }

            Member member = memberService.getMember(principal.getName());
            Transaction transaction = new Transaction(LocalDateTime.now(), Math.abs(difference), "잔액 수정", asset, category, member);
            transactionService.saveTransaction(TransactionFormDto.of(transaction));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @PostMapping("/asset/updatePosition")
    public @ResponseBody ResponseEntity updatePosition(@RequestBody Long[] order) {

        try {
            for (int i = 0; i < order.length; i++) {
                assetService.updatePosition(order[i], i+1);
            }

            return new ResponseEntity<>("성공적으로 변경되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/asset/addAsset")
    public @ResponseBody ResponseEntity addAsset(@RequestBody Map<String, String> req, Principal principal) {
        try {
            Member member = memberService.getMember(principal.getName());

            int position = assetService.getAssets(member.getId()).size() + 1;

            Asset asset = new Asset(req.get("name"), member, Integer.parseInt(req.get("amount")), position);
            assetService.addAsset(asset);
            return new ResponseEntity<>("성공적으로 생성되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/asset/deleteAsset/{id}")
    public @ResponseBody ResponseEntity deleteAsset(@PathVariable("id") Long id) {
        try {
            assetService.deleteAsset(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}

