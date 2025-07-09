package com.portfolio.blog.repository.post;

import com.portfolio.blog.entity.Post;
import com.portfolio.blog.entity.common.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.portfolio.blog.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void updateHits(Long id, int amount) {
        jpaQueryFactory
                .update(post)
                .set(post.hit, post.hit.add(amount))
                .where(post.id.eq(id))
                .execute();
    }

    @Override
    public Page<Post> postListSearch(String category, String keyword, Pageable pageable) {
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .where(
                        (post.category.eq(category).and(post.title.contains(keyword)))
                                .or(post.category.eq(category).and(post.convertContent.contains(keyword)))
                                .and(post.status.eq(Status.TRUE))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(
                        (post.category.eq(category).and(post.title.contains(keyword)))
                                .or(post.category.eq(category).and(post.convertContent.contains(keyword)))
                                .and(post.status.eq(Status.TRUE))
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, count);
    }

    @Override
    public Page<Post> adminPostListSearch(String category, String searchCnd, String keyword, Pageable pageable) {
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .where(
                        categoryCondition(category), //카테고리
                        searchCndCondition(searchCnd, keyword) // 전체, 제목, 내용
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(
                        categoryCondition(category), //카테고리
                        searchCndCondition(searchCnd, keyword) // 전체, 제목, 내용
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, count);
    }

    private BooleanExpression categoryCondition(String category) {
        if (category == null || category.equals("all") || category.trim().isEmpty()) {
            return null; // 전체 선택 시 조건 없이
        }

        return post.category.eq(category);
    }

    private BooleanExpression searchCndCondition(String searchCnd, String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            return null; //검색어 없으면 조건 없음
        }

        String searchKeyword = "%" + keyword + "%";

        if("title".equals(searchCnd)){
            return post.title.like(searchKeyword);
        }else if("content".equals(searchCnd)){
            return post.convertContent.like(searchKeyword);
        }else if("all".equals(searchCnd)){
            return post.title.like(searchKeyword).or(post.convertContent.like(searchKeyword));
        }else {
            return null;
        }
    }

}
