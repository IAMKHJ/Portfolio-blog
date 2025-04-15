package com.portfolio.blog.dto.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResultDto {
    private Long id;
    private String content;
    private Long parentId;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
