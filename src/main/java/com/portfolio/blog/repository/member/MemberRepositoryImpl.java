package com.portfolio.blog.repository.member;

import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.common.RoleType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.portfolio.blog.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Member> adminMemberListSearch(String roleType, String searchCnd, String keyword, Pageable pageable) {
        List<Member> members = jpaQueryFactory
                .selectFrom(member)
                .where(
                        roleTypeCondition(roleType), //전체, 관리자, 일반회원
                        searchCndCondtion(searchCnd, keyword) // 전체, 아이디, 이름
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdDate.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(
                        roleTypeCondition(roleType), //전체, 관리자, 일반회원
                        searchCndCondtion(searchCnd, keyword) //전체, 아이디, 이름
                )
                .fetchOne();

        return new PageImpl<>(members, pageable, count);
    }

    private BooleanExpression roleTypeCondition(String roleType){
        if("admin".equals(roleType)){
            return member.roleType.eq(RoleType.ADMIN);
        }else if("user".equals(roleType)){
            return member.roleType.eq(RoleType.USER);
        }else {
            return null; //all 전체 일때
        }
    }

    private BooleanExpression searchCndCondtion(String searchCnd, String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            return null; //검색어 없으면 조건 없음
        }

        String searchKeyword = "%" + keyword + "%";

        if("uid".equals(searchCnd)){
            return member.uid.like(searchKeyword);
        }else if("name".equals(searchCnd)){
            return member.name.like(searchKeyword);
        }else if("all".equals(searchCnd)){
            return member.uid.like(searchKeyword).or(member.name.like(searchKeyword));
        }else {
            return null;
        }
    }

}
