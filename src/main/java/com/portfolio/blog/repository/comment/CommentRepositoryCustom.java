package com.portfolio.blog.repository.comment;

import com.portfolio.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByPost(Long id);
    Page<Comment> adminCommentListSearch(String searchCnd, String keyword, Pageable pageable);
}
