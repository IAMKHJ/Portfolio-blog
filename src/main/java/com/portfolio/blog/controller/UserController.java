package com.portfolio.blog.controller;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.user.ChangePasswordDto;
import com.portfolio.blog.dto.user.ChangeProfileDto;
import com.portfolio.blog.dto.user.LoginDto;
import com.portfolio.blog.dto.user.LoginSessionDto;
import com.portfolio.blog.entity.Comment;
import com.portfolio.blog.entity.common.RoleType;
import com.portfolio.blog.service.CommentService;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserController {

    private final UserService userService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/login")
    public String login(HttpServletRequest request){

        if(request.getSession().getAttribute("USER")!=null){
            return "redirect:/";
        }

        request.getSession().setAttribute("prevPage", request.getHeader("Referer"));

        return "user/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public MessageDto<?> login(@ModelAttribute LoginDto loginDto, HttpSession session){

        MessageDto<?> result = userService.login(loginDto);

        if(result.getKey().equals("ok")){
            session.setAttribute("USER", result.getData());
            session.setMaxInactiveInterval(60*60); // 1시간
        }

        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/user/login";
    }

    @GetMapping("/myPage/info/{uid}")
    public String info(@PathVariable(name = "uid") String uid, Model model){
        model.addAttribute("member", memberService.findByUid(uid));
        return "user/info";
    }

    @ResponseBody
    @PostMapping("/changePw")
    public MessageDto<?> changePassword(@ModelAttribute ChangePasswordDto dto, HttpSession session) {
        MessageDto<?> result = userService.changePassword(dto);
        if(result.getKey().equals("ok")){
            session.invalidate();
        }
        return result;
    }

    @GetMapping("/myPage/profile/{uid}")
    public String profile(@PathVariable(name = "uid") String uid, Model model){
        model.addAttribute("member", memberService.findByUid(uid));
        return "user/profile";
    }

    @PostMapping("/upload/profile")
    public String profile(ChangeProfileDto dto) throws IOException {
        userService.changeProfile(dto);
        return "redirect:/myPage/profile/"+dto.getMemberUid();
    }

    @GetMapping("/myPage/myComment/{uid}")
    public String myCommentList(HttpSession session, Model model, @PathVariable(name = "uid") String uid, @PageableDefault(page = 1, size = 10, direction = Sort.Direction.DESC) Pageable pageable){

        LoginSessionDto loginSessionDto = (LoginSessionDto) session.getAttribute("USER");

        if(loginSessionDto.getRoleType()== RoleType.ADMIN){
            return "redirect:/";
        }

        Page<Comment> list = commentService.myCommentList(uid, pageable);

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

        return "user/myCommentList";
    }

}
