package com.portfolio.blog.dto.comment;

import com.portfolio.blog.entity.File;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentListDto {
    private Long id;
    private String content;
    private Long parentId;
    private Long memberId;
    private Long postId;
    private String postMemberName;
    private String memberName;
    private File file;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
