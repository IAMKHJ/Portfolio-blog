package com.portfolio.blog.dto.user;

import com.portfolio.blog.entity.File;
import com.portfolio.blog.entity.common.RoleType;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class LoginDto {
    private String uid;
    private String password;
}
