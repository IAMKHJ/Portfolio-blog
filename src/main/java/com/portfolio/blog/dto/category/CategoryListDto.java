package com.portfolio.blog.dto.category;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryListDto {
    private Long id;
    private String name;
    private Long orderNumber;
}
