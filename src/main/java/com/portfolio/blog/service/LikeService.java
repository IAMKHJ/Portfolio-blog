package com.portfolio.blog.service;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.like.LikePostDto;
import com.portfolio.blog.dto.user.LoginSessionDto;
import com.portfolio.blog.entity.Likes;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.entity.common.Status;
import com.portfolio.blog.repository.like.LikeRepository;
import com.portfolio.blog.repository.member.MemberRepository;
import com.portfolio.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MessageDto<?> likePost(LikePostDto dto) {

        Optional<Member> member = memberRepository.findById(dto.getMemberId());
        if(member.isEmpty()) return new MessageDto<>("noMember");

        Optional<Post> post = postRepository.findById(dto.getPostId());
        if(post.isEmpty()) return new MessageDto<>("noPost");

        // 좋아요 누른 적 있는지 확인
        Optional<Likes> foundLikes = likeRepository.findByPostAndMember(post.get(), member.get());

        String key;
        if(foundLikes.isEmpty()){ // 해당 글에 처음 좋아요 누르면
            Likes likes = Likes.builder()
                    .post(post.get())
                    .member(member.get())
                    .status(Status.TRUE)
                    .build();
            likeRepository.save(likes);
            key = "first";
        }else{ // 좋아요 누른적 있으면
            if(foundLikes.get().getStatus().equals(Status.FALSE)){
                foundLikes.get().changeStatus(Status.TRUE);
                key = "true";
            }else{
                foundLikes.get().changeStatus(Status.FALSE);
                key = "false";
            }
        }

        // 좋아요 총갯수
        int cnt = countByPostIdAndStatus(post.get().getId());

        return new MessageDto<>(key, cnt);

    }

    public Boolean findByPostAndMember(Long postId, LoginSessionDto user) {
        Optional<Member> member = memberRepository.findById(user==null?0:user.getId());
        Optional<Post> post = postRepository.findById(postId);
        if(member.isPresent()&&post.isPresent()){
            Optional<Likes> likes = likeRepository.findByPostAndMember(post.get(), member.get());
            if(likes.isPresent()){
                return likes.get().getStatus().equals(Status.TRUE);
            }
        }
        return false;
    }

    public int countByPostIdAndStatus(Long postId){
        return likeRepository.countByPostIdAndStatus(postId, Status.TRUE);
    }
}
