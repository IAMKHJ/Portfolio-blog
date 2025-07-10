package com.portfolio.blog.dto.post;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private int hit;
    private String memberName;
    private File file;
    private LocalDateTime createdDate;
    private String category;

    public PostListDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getConvertContent();
        this.hit = entity.getHit();
        this.memberName = entity.getMember().getName();
        this.file = entity.getFiles().isEmpty() ? null : entity.getFiles().get(0);
        this.createdDate = entity.getCreatedDate();
        this.category = entity.getCategory();
    }
}
