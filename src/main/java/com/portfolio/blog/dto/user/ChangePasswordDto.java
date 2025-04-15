package com.portfolio.blog.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private Long id;
    private String password;
    private String changePassword;
}
