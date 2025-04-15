package com.portfolio.blog.repository.post;

import com.portfolio.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    void updateHits(Long id);
    Page<Post> postListSearch(String category, String keyword, Pageable pageable);
}
