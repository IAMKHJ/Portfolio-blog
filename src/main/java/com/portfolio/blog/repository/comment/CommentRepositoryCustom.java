package com.portfolio.blog.repository.comment;

import com.portfolio.blog.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByPost(Long id);
}
