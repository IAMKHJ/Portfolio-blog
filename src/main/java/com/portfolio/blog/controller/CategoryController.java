package com.portfolio.blog.controller;

import com.portfolio.blog.dto.category.CategoryDeleteDto;
import com.portfolio.blog.dto.category.CategorySaveDto;
import com.portfolio.blog.dto.category.CategorySortDto;
import com.portfolio.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 저장
    @PostMapping("/category/save")
    public String save(@ModelAttribute CategorySaveDto dto, Model model){
        categoryService.save(dto);
        model.addAttribute("categoryList", categoryService.findAll());
        return "user/category :: #cate";
    }

    // 카테고리 순서변경
    @PostMapping("/category/sort")
    public String sort(@ModelAttribute CategorySortDto dto){
        categoryService.sort(dto);
        return "user/category";
    }

    // 카테고리 삭제
    @PostMapping("/category/delete")
    public String delete(@ModelAttribute CategoryDeleteDto dto, Model model){
        categoryService.delete(dto);
        model.addAttribute("categoryList", categoryService.findAll());
        return "user/category :: #cate";
    }

}
