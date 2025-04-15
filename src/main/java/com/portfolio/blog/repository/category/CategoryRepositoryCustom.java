package com.portfolio.blog.repository.category;

import com.portfolio.blog.dto.category.CategorySortDto;

public interface CategoryRepositoryCustom {
    void sort(CategorySortDto dto);

    void updateOrderNumber(CategorySortDto dto);
}
