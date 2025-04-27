package com.portfolio.blog.repository.member;

import com.portfolio.blog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByUid(String uid);
    Optional<Member> findByEmail(String email);
}
