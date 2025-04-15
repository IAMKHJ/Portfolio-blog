package com.portfolio.blog.controller;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.user.ChangePasswordDto;
import com.portfolio.blog.dto.user.ChangeProfileDto;
import com.portfolio.blog.dto.user.LoginDto;
import com.portfolio.blog.service.CategoryService;
import com.portfolio.blog.service.MemberService;
import com.portfolio.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){

        if(request.getSession().getAttribute("USER")!=null){
            return "redirect:/";
        }

        request.getSession().setAttribute("prevPage", request.getHeader("Referer"));

        return "user/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public MessageDto<?> login(@ModelAttribute LoginDto loginDto, HttpServletRequest request){

        MessageDto<?> result = userService.login(loginDto);

        if(result.getKey().equals("ok")){
            request.getSession().setAttribute("USER", result.getData());
            request.getSession().setMaxInactiveInterval(60*60); // 1시간
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

    @GetMapping("/myPage/category/{uid}")
    public String category(@PathVariable(name = "uid") String uid, Model model){
        model.addAttribute("member", memberService.findByUid(uid));
        model.addAttribute("categoryList", categoryService.findAll());
        return "user/category";
    }

}
