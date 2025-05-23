package com.portfolio.blog.dto.post;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.entity.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminPostListDto {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private String memberName;
    private File file;
    private LocalDateTime createdDate;
    private Status status;
    private String category;

    public AdminPostListDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getConvertContent();
        this.hit = entity.getHit();
        this.memberName = entity.getMember().getName();
        this.file = entity.getFiles().isEmpty() ? null : entity.getFiles().get(0);
        this.createdDate = entity.getCreatedDate();
        this.status = entity.getStatus();
        this.category = entity.getCategory();
    }

}
