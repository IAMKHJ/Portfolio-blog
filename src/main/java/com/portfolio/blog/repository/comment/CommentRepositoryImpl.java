package com.portfolio.blog.repository.comment;

import com.portfolio.blog.entity.Comment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.portfolio.blog.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findAllByPost(Long id) { // 글에대한 댓글 전체 가져오기
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(id))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                )
                .fetch();
    }

    @Override
    public Page<Comment> adminCommentListSearch(String searchCnd, String keyword, Pageable pageable) {
        List<Comment> comments = jpaQueryFactory
                .selectFrom(comment)
                .where(
                        searchCndCondition(searchCnd, keyword) // 전체, 작성자, 내용
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        searchCndCondition(searchCnd, keyword) // 전체, 작성자, 내용
                )
                .fetchOne();

        return new PageImpl<>(comments, pageable, count);
    }

    private BooleanExpression searchCndCondition(String searchCnd, String keyword){
        if (keyword == null || keyword.trim().isEmpty()) {
            return null; //검색어 없으면 조건 없음
        }

        String searchKeyword = "%" + keyword + "%";

        if("name".equals(searchCnd)){
            return comment.member.name.like(searchKeyword);
        }else if("content".equals(searchCnd)){
            return comment.content.like(searchKeyword);
        }else if("all".equals(searchCnd)){
            return comment.member.name.like(searchKeyword).or(comment.content.like(searchKeyword));
        }else {
            return null;
        }
    }

}
