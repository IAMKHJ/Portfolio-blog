package com.portfolio.blog.dto.member;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.common.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailDto {
    private Long id;
    private String uid;
    private String name;
    private String email;
    private RoleType roleType;
    private File file;
}
