package com.portfolio.blog.dto.member;

import com.portfolio.blog.entity.common.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberStatusUpdateDto {
    private Long id;
    private Status status;
}
