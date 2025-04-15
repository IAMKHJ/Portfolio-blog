package com.portfolio.blog.dto.member;

import com.portfolio.blog.entity.common.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSaveDto {
    private String uid;
    private String password;
    private String name;
    private String email;
    private RoleType roleType;
}
