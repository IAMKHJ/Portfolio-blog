package com.portfolio.blog.dto.post;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostUpdateDto {
    private String title;
    private String content;
    private MultipartFile file;
    private String category;
}
