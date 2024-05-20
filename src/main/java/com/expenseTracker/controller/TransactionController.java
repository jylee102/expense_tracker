package com.expenseTracker.controller;

import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AssetService assetService;

    // 내역 등록(insert)
    @PostMapping(value = "/transaction/insert")
    public @ResponseBody ResponseEntity insert(@Valid TransactionFormDto transactionFormDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            // 유효성 체크 후 에러 결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러 메시지를 가지고 온다.

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            assetService.updateAmount(transactionService.saveTransaction(transactionFormDto), true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // 내역 확인
    @GetMapping(value = "/transaction/view/{transactionId}")
    public ResponseEntity view(@PathVariable("transactionId") Long transactionId) {
        try {
            Transaction transaction = transactionService.getDtl(transactionId);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("내역을 불러오는 것에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 내역 수정(edit)
    @PostMapping(value = "/transaction/edit")
    public @ResponseBody ResponseEntity edit(@Valid TransactionFormDto transactionFormDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            // 유효성 체크 후 에러 결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러 메시지를 가지고 온다.

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            Transaction from = transactionService.findById(transactionFormDto.getId());
            assetService.updateAmount(from, false);

            Transaction to = transactionService.updateTransaction(transactionFormDto);
            assetService.updateAmount(to, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("수정에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // 내역 삭제
    @PostMapping(value = "/transaction/delete")
    public String delete(@RequestParam("id") Long id, Model model) {

        try {
            assetService.updateAmount(transactionService.findById(id), false);
            transactionService.deleteTransaction(id);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "삭제 실패");
            return "redirect:/";
        }

        return "redirect:/";
    }
}
