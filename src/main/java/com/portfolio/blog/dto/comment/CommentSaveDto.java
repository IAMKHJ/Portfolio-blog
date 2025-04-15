package com.portfolio.blog.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSaveDto {
    private String content;
    private Long parentId;
    private Long memberId;
    private Long postId;
}
