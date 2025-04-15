package com.portfolio.blog.repository.category;

import com.portfolio.blog.dto.category.CategorySortDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.portfolio.blog.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void sort(CategorySortDto dto) {
        if(dto.getStartNum()>dto.getEndNum()){//밑에서 위로 이동
            jpaQueryFactory
                    .update(category)
                    .set(category.orderNumber, category.orderNumber.add(1))
                    .where(category.orderNumber.goe(dto.getEndNum()))
                    .execute();
        }else if(dto.getStartNum()<dto.getEndNum()){//위에서 밑으로 이동
            jpaQueryFactory
                    .update(category)
                    .set(category.orderNumber, category.orderNumber.add(-1))
                    .where(category.orderNumber.loe(dto.getEndNum()))
                    .execute();
        }
    }

    @Override
    public void updateOrderNumber(CategorySortDto dto) {
        jpaQueryFactory
                .update(category)
                .set(category.orderNumber, dto.getEndNum())
                .where(category.id.eq(dto.getStartId()))
                .execute();
    }
}
