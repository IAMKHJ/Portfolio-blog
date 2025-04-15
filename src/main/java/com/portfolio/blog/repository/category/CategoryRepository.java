package com.portfolio.blog.repository.category;

import com.portfolio.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    Optional<Category> findTopByOrderByOrderNumberDesc();
}
