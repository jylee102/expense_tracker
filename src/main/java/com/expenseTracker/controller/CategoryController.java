package com.expenseTracker.controller;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final MemberService memberService;
    private final CategoryService categoryService;

    @PostMapping("/category/updateColor")
    public @ResponseBody ResponseEntity updateColor(@RequestBody Category req) {

        try {
            categoryService.updateColor(req.getId(), req.getColor());
            return new ResponseEntity<>(req.getColor(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/category/updateName")
    public @ResponseBody ResponseEntity updateName(@RequestBody Category req) {

        try {
            categoryService.updateName(req.getId(), req.getName());
            return new ResponseEntity<>(req.getName(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/category/updatePosition")
    public @ResponseBody ResponseEntity updatePosition(@RequestBody Long[] order) {

        try {
            for (int i = 0; i < order.length; i++) {
                categoryService.updatePosition(order[i], i+1);
            }
            
            return new ResponseEntity<>("성공적으로 변경되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/category/addCategory")
    public @ResponseBody ResponseEntity addCategory(@RequestBody Map<String, String> req, Principal principal) {
        try {
            Member member = memberService.getMember(principal.getName());
            Type type;

            if (req.get("type").equals("income")) {
                type = Type.INCOME;
            } else type = Type.EXPENSE;

            int position = categoryService.getCategories(member.getId(), type).size() + 1;

            Category category = new Category(req.get("name"), type, member, req.get("color"), position);
            categoryService.addCategory(category);
            return new ResponseEntity<>("성공적으로 생성되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/category/deleteCategory/{id}")
    public @ResponseBody ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
