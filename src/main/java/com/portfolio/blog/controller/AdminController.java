package com.portfolio.blog.controller;

import com.portfolio.blog.dto.member.AdminMemberListDto;
import com.portfolio.blog.dto.member.MemberStatusUpdateDto;
import com.portfolio.blog.dto.post.AdminPostListDto;
import com.portfolio.blog.dto.post.PostStatusUpdateDto;
import com.portfolio.blog.dto.user.LoginSessionDto;
import com.portfolio.blog.entity.common.RoleType;
import com.portfolio.blog.service.CategoryService;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.PostService;
import jakarta.servlet.http.HttpSession;
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
public class AdminController {

    private final CategoryService categoryService;
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/myPage/category")
    public String category(Model model, HttpSession session){

        LoginSessionDto loginSessionDto = (LoginSessionDto) session.getAttribute("USER");

        if(loginSessionDto.getRoleType()==RoleType.USER){
            return "redirect:/";
        }

        model.addAttribute("categoryList", categoryService.findAll());
        return "admin/category";
    }

    @GetMapping("/myPage/member")
    public String adminMemberList(HttpSession session, Model model, @RequestParam(name = "roleType", defaultValue = "") String roleType, @RequestParam(name = "searchCnd", defaultValue = "") String searchCnd, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1, size = 10, direction = Sort.Direction.DESC) Pageable pageable){

        LoginSessionDto loginSessionDto = (LoginSessionDto) session.getAttribute("USER");

        if(loginSessionDto.getRoleType()==RoleType.USER){
            return "redirect:/";
        }

        Page<AdminMemberListDto> list = memberService.adminMemberListSearch(roleType, searchCnd, keyword, pageable);

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
        model.addAttribute("roleType", roleType);
        model.addAttribute("searchCnd", searchCnd);
        model.addAttribute("keyword", keyword);

        return "admin/memberList";
    }

    @ResponseBody
    @PostMapping("/memberStatusUpdate")
    public String memberStatusUpdate(@ModelAttribute MemberStatusUpdateDto dto) throws IOException {
        memberService.memberStatusUpdate(dto);
        return "redirect:/myPage/member";
    }

    @GetMapping("/myPage/post")
    public String adminPostList(HttpSession session, Model model, @RequestParam(name = "category", defaultValue = "") String category, @RequestParam(name = "searchCnd", defaultValue = "") String searchCnd, @RequestParam(name = "keyword", defaultValue = "") String keyword, @PageableDefault(page = 1, size = 10, direction = Sort.Direction.DESC) Pageable pageable){

        LoginSessionDto loginSessionDto = (LoginSessionDto) session.getAttribute("USER");

        if(loginSessionDto.getRoleType()==RoleType.USER){
            return "redirect:/";
        }

        Page<AdminPostListDto> list = postService.adminPostListSearch(category, searchCnd, keyword, pageable);

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
        model.addAttribute("searchCnd", searchCnd);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryKeyword", category);
        model.addAttribute("categoryList", categoryService.findAll());

        return "admin/postList";
    }

    @ResponseBody
    @PostMapping("/postStatusUpdate")
    public String postStatusUpdate(@ModelAttribute PostStatusUpdateDto dto) throws IOException {
        postService.postStatusUpdate(dto);
        return "redirect:/myPage/post";
    }

}
