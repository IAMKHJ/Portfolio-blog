package com.portfolio.blog.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ChangeProfileDto {
    private String memberUid;
    private MultipartFile file;
}
