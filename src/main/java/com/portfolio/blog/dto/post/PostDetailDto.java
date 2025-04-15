package com.portfolio.blog.dto.post;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDetailDto {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Member member;
    private File file;
    private String category;
}
