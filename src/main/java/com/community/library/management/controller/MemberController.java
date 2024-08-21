package com.community.library.management.controller;

import com.community.library.management.dto.MemberDTO;
import com.community.library.management.mapper.MemberMapper;
import com.community.library.management.model.Member;
import com.community.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
public class MemberController {


    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<Member> members = memberService.findAll();
        List<MemberDTO> memberDTOs = members.stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(memberDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return new  ResponseEntity<>(memberMapper.toDTO(member), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        Member savedMember =  memberService.createMember(memberDTO);
        return new ResponseEntity<>(memberMapper.toDTO(savedMember), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<MemberDTO> updateUserName(
            @PathVariable Long id,
            @RequestBody @Valid MemberDTO memberDTO) {

         memberService.updateMember(id, memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
            memberService.deleteMember(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
