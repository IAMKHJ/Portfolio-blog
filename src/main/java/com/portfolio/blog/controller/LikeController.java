package com.portfolio.blog.controller;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.like.LikePostDto;
import com.portfolio.blog.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @ResponseBody
    @PostMapping("/like/post")
    public MessageDto<?> likePost(@ModelAttribute LikePostDto dto){
        return likeService.likePost(dto);
    }

}
