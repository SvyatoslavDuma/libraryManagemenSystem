package com.community.library.management.controller;

import com.community.library.management.dto.MemberDTO;
import com.community.library.management.model.Member;
import com.community.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findById(id);
        return member.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        Member savedMember = memberService.createMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Member> updateUserName(
            @PathVariable Long id,
            @RequestBody MemberDTO memberDTO) {

        Member updatedMember = memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(updatedMember);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
