package com.portfolio.blog.controller;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.post.PostListDto;
import com.portfolio.blog.dto.post.PostSaveDto;
import com.portfolio.blog.dto.post.PostUpdateDto;
import com.portfolio.blog.dto.user.LoginSessionDto;
import com.portfolio.blog.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final ChatService chatService;
    private final CategoryService categoryService;

    //글 쓰기 화면
    @GetMapping("/post/write")
    public String write(Model model, @RequestParam(name = "category", defaultValue = "") String category, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1) Pageable pageable){
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("categoryKeyword", category);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("keyword", keyword);
        return "post/write";
    }

    //글 쓰기
    @ResponseBody
    @PostMapping("/post/save")
    public MessageDto<?> save(@ModelAttribute PostSaveDto dto) throws IOException {
        return postService.save(dto);
    }

    //글 수정 화면
    @GetMapping("/post/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model, @RequestParam(name = "category", defaultValue = "") String category, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1) Pageable pageable){
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryKeyword", category);
        model.addAttribute("categoryList", categoryService.findAll());
        return "post/update";
    }

    //글 수정
    @ResponseBody
    @PatchMapping("/post/update/{id}")
    public MessageDto<?> update(@PathVariable(name = "id") Long id, @ModelAttribute PostUpdateDto dto) throws IOException {
        return postService.update(id, dto);
    }

    //글 목록
    @GetMapping("/post/list")
    public String list(@RequestParam(name = "category", defaultValue = "") String category, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1, size = 10, direction = Sort.Direction.DESC) Pageable pageable, Model model){

        Page<PostListDto> list = postService.search(category, keyword, pageable);

        /*
         * blockLimit : page 개수 설정
         * 현재 사용자가 선택한 페이지 앞 뒤로 3페이지씩만 보여준다.
         * ex : 현재 사용자가 4페이지라면 2, 3, (4), 5, 6
         */
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) -1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryKeyword", category);
        model.addAttribute("categoryList", categoryService.findAll());

        return "post/list";
    }

    //글 단건 조회
    @GetMapping("/post/detail/{id}")
    public String findById(@PathVariable(name = "id") Long id, Model model, @RequestParam(name = "category", defaultValue = "") String category, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1) Pageable pageable, HttpServletRequest request){
        postService.updateHits(id);
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("commentList", commentService.findAllByPost(id));
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("like", likeService.findByPostAndMember(id, (LoginSessionDto) request.getSession().getAttribute("USER")));
        model.addAttribute("likeCnt", likeService.countByPostIdAndStatus(id));
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryKeyword", category);
        model.addAttribute("chat", chatService.findById(id));

        return "post/detail";
    }

    //글 삭제
    @ResponseBody
    @DeleteMapping("/post/delete/{id}")
    public MessageDto<?> delete(@PathVariable(name = "id") Long id){
        return postService.delete(id);
    }

}
