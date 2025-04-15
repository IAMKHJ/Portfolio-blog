package com.portfolio.blog.service;

import com.portfolio.blog.dto.comment.CommentDeleteDto;
import com.portfolio.blog.dto.comment.CommentListDto;
import com.portfolio.blog.dto.comment.CommentSaveDto;
import com.portfolio.blog.dto.comment.CommentUpdateDto;
import com.portfolio.blog.entity.Comment;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.repository.comment.CommentRepository;
import com.portfolio.blog.repository.member.MemberRepository;
import com.portfolio.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void save(CommentSaveDto dto) {

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("글 작성자를 찾을 수 없습니다."));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));

        Comment newComment;
        if(dto.getParentId()!=null){ // 답글
            Comment comment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

            newComment = Comment.builder()
                    .content(dto.getContent())
                    .member(member)
                    .parent(comment)
                    .post(post)
                    .build();
        }else{ // 댓글
            newComment = Comment.builder()
                    .content(dto.getContent())
                    .member(member)
                    .post(post)
                    .build();
        }

        commentRepository.save(newComment);
    }

    @Transactional
    public void update(CommentUpdateDto dto) {

        postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.update(dto);
    }

    @Transactional(readOnly = true)
    public List<CommentListDto> findAllByPost(Long id) {

         List<Comment> comments = commentRepository.findAllByPost(id);
         List<CommentListDto> list = new ArrayList<>();

        for (Comment comment : comments){
            Member member = comment.getMember();
            Post post = comment.getPost();
            CommentListDto dto = CommentListDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .parentId(comment.getParent() == null ? null : comment.getParent().getId())
                    .memberId(member.getId())
                    .postId(post.getId())
                    .postMemberName(post.getMember().getName())
                    .memberName(member.getName())
                    .file(member.getFiles().isEmpty()?null:member.getFiles().get(0))
                    .createdDate(comment.getCreatedDate())
                    .lastModifiedDate(comment.getLastModifiedDate())
                    .build();
            list.add(dto);
        }

        return list;
    }

    @Transactional
    public void delete(CommentDeleteDto dto) {

        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        commentRepository.delete(comment);
    }
}
