package com.portfolio.blog.repository.post;

import com.portfolio.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    void deleteByCategory(String categoryName);
}
