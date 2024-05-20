package com.expenseTracker.config;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import com.expenseTracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInit implements ApplicationRunner {

    private final CategoryService categoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 카테고리가 없는 경우에만 초기화 작업 실행
        if (categoryService.count() == 0) {
            Category expenseCategory = new Category("차액", Type.EXPENSE);
            categoryService.addCategory(expenseCategory);

            Category incomeCategory  = new Category("차액", Type.INCOME);
            categoryService.addCategory(incomeCategory );
        }
    }
}