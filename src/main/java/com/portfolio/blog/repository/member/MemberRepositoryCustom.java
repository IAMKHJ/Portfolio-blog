package com.portfolio.blog.repository.member;

import com.portfolio.blog.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> adminMemberListSearch(String roleType, String searchCnd, String keyword, Pageable pageable);
}
