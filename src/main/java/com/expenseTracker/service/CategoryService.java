package com.expenseTracker.service;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import com.expenseTracker.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 아이디로 카테고리 찾기
    public Category findById(Long id) throws Exception {
        return categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 전체 카테고리의 개수
    public Long count() {
        return categoryRepository.count();
    }

    // 로그인한 유저가 설정한 카테고리 불러오기
    public List<Category> getCategories(Long memberId, Type type) throws Exception {
        return categoryRepository.findByMemberIdAndTypeOrderByPosition(memberId, type);
    }

    // 카테고리 색깔 변경
    public void updateColor(Long id, String color) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        category.setColor(color);
    }

    // 카테고리 이름 변경
    public void updateName(Long id, String name) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        category.setName(name);
    }

    // 카테고리 순서 변경
    public void updatePosition(Long id, int position) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        category.setPosition(position);
    }

    // 카테고리 등록
    public Category addCategory(Category category) throws Exception {
        return categoryRepository.save(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long id) throws Exception {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new);
        categoryRepository.delete(category);
    }
}
