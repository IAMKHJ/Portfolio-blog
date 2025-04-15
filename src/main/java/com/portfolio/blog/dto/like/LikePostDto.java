package com.portfolio.blog.dto.like;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikePostDto {
    private Long memberId;
    private Long postId;
}
