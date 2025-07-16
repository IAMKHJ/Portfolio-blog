package com.portfolio.blog.repository.comment;

import com.portfolio.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Page<Comment> findByMemberId(Long id, Pageable pageable);
    Page<Comment> adminCommentListSearch(String searchCnd, String keyword, Pageable pageable);
}
