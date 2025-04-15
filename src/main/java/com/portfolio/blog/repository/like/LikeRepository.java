package com.portfolio.blog.repository.like;

import com.portfolio.blog.entity.Likes;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.entity.common.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostAndMember(Post post, Member member);

    int countByPostIdAndStatus(Long postId, Status status);
}
