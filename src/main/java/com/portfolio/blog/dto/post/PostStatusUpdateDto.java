package com.portfolio.blog.dto.post;

import com.portfolio.blog.entity.common.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostStatusUpdateDto {
    private Long id;
    private Status status;
}
