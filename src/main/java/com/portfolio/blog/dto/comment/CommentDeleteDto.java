package com.portfolio.blog.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteDto {
    private Long id;
    private Long postId;
}
