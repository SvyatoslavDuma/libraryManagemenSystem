package com.community.library.management.service;


import com.community.library.management.dto.MemberDTO;
import com.community.library.management.exception.MemberNotDeletableException;
import com.community.library.management.exception.MemberNotFoundException;
import com.community.library.management.mapper.MemberMapper;
import com.community.library.management.model.Member;
import com.community.library.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member createMember(MemberDTO memberDTO) {
        memberDTO.setMembershipDate(LocalDate.now());
        return memberRepository.save(memberMapper.toEntity(memberDTO));
    }

    public Member updateMember(Long memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.setName(memberDTO.getName());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        if (!member.getBorrowedBooks().isEmpty()) {
            throw new MemberNotDeletableException();
        }
        memberRepository.deleteById(id);
    }

}