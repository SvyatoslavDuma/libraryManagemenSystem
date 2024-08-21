package com.community.library.management.controller;


import com.community.library.management.dto.MemberDTO;
import com.community.library.management.exception.MemberNotFoundException;
import com.community.library.management.model.Member;
import com.community.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberHtmlController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/{id}")
    public String getMemberById(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);
        return "member-detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("member", new MemberDTO());
        return "create-member";
    }

    @PostMapping
    public String createMember(@ModelAttribute MemberDTO memberDTO) {
        memberService.createMember(memberDTO);
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        return "edit-member";
    }

    @PostMapping("/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute MemberDTO memberDTO) {
        memberService.updateMember(id, memberDTO);
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}