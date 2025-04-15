package com.portfolio.blog.controller;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.member.MemberSaveDto;
import com.portfolio.blog.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 view
    @GetMapping("/join")
    public String save(HttpSession session){
        if(session.getAttribute("USER")!=null){
            return "redirect:/";
        }
        return "member/join";
    }

    // 회원가입
    @ResponseBody
    @PostMapping("/join")
    public MessageDto<?> save(@ModelAttribute MemberSaveDto memberSaveDto){
        return memberService.save(memberSaveDto);
    }

    // admin 회원가입 view
    @GetMapping("/admin/join")
    public String adminSave(HttpSession session){
        if(session.getAttribute("USER")!=null){
            return "redirect:/";
        }
        return "member/adminJoin";
    }

}
