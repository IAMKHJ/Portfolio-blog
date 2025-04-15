package com.portfolio.blog.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostSaveDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private MultipartFile file;
    private String category;
}
