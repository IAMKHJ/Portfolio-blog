package com.portfolio.blog.repository.post;

import com.portfolio.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    void updateHits(Long id, int amount);
    Page<Post> postListSearch(String category, String keyword, Pageable pageable);
    Page<Post> adminPostListSearch(String category, String searchCnd, String keyword, Pageable pageable);
}
