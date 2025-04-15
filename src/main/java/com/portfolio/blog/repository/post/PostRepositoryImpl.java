package com.portfolio.blog.repository.post;

import com.portfolio.blog.entity.Post;
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
    public void updateHits(Long id) {
        jpaQueryFactory
                .update(post)
                .set(post.hit, post.hit.add(1))
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
                )
                .fetchOne();

        return new PageImpl<>(posts, pageable, count);
    }
}
