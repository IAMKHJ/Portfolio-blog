package com.portfolio.blog.dto.member;

import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.common.RoleType;
import com.portfolio.blog.entity.common.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminMemberListDto {
    private Long id;
    private String uid;
    private String name;
    private String email;
    private RoleType roleType;
    private Status status;
    private LocalDateTime createdDate;

    public AdminMemberListDto(Member entity) {
        this.id = entity.getId();
        this.uid = entity.getUid();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.roleType = entity.getRoleType();
        this.status = entity.getStatus();
        this.createdDate = entity.getCreatedDate();
    }
}
