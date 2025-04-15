package com.portfolio.blog.service;

import com.portfolio.blog.dto.category.CategoryDeleteDto;
import com.portfolio.blog.dto.category.CategoryListDto;
import com.portfolio.blog.dto.category.CategorySaveDto;
import com.portfolio.blog.dto.category.CategorySortDto;
import com.portfolio.blog.entity.Category;
import com.portfolio.blog.repository.category.CategoryRepository;
import com.portfolio.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional
    public void save(CategorySaveDto dto){
        Optional<Category> lastCategory = categoryRepository.findTopByOrderByOrderNumberDesc();
        Category category = Category.builder()
                .name(dto.getName())
                .orderNumber(lastCategory.isPresent() ? lastCategory.get().getOrderNumber()+1 : 1)
                .build();
        categoryRepository.save(category);
    }

    @Transactional
    public void sort(CategorySortDto dto) {
        categoryRepository.sort(dto);
        categoryRepository.updateOrderNumber(dto);
    }

    @Transactional(readOnly = true)
    public List<CategoryListDto> findAll() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "orderNumber"));
        List<CategoryListDto> list = new ArrayList<>();

        for (Category category : categories){
            CategoryListDto dto = CategoryListDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .orderNumber(category.getOrderNumber())
                    .build();
            list.add(dto);
        }

        return list;

    }

    @Transactional
    public void delete(CategoryDeleteDto dto) {
        categoryRepository.deleteById(dto.getId());
        postRepository.deleteByCategory(dto.getName());
    }

}
