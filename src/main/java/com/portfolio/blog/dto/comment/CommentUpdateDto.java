package com.portfolio.blog.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDto {
    private Long id;
    private String content;
    private Long postId;
}
