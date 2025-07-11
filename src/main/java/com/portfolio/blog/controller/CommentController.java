package com.portfolio.blog.controller;

import com.portfolio.blog.dto.comment.CommentDeleteDto;
import com.portfolio.blog.dto.comment.CommentSaveDto;
import com.portfolio.blog.dto.comment.CommentUpdateDto;
import com.portfolio.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/comment/save")
    public String save(@ModelAttribute CommentSaveDto dto, Model model){
        commentService.save(dto);
        model.addAttribute("commentList", commentService.findAllByPost(dto.getPostId()));
        return "post/detail :: #comment_info_wrap";
    }

    // 댓글 수정
    @PostMapping("/comment/update")
    public String update(@ModelAttribute CommentUpdateDto dto, Model model){
        commentService.update(dto);
        model.addAttribute("commentList", commentService.findAllByPost(dto.getPostId()));
        return "post/detail :: #comment_info_wrap";
    }

    // 댓글 삭제
    @PostMapping("/comment/delete")
    public String delete(@ModelAttribute CommentDeleteDto dto, Model model){
        commentService.delete(dto);
        model.addAttribute("commentList", commentService.findAllByPost(dto.getPostId()));
        return "post/detail :: #comment_info_wrap";
    }

}
