package com.portfolio.blog.dto.user;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.common.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginSessionDto {
    private Long id;
    private String uid;
    private String name;
    private String email;
    private RoleType roleType;
    private List<File> files;
}
