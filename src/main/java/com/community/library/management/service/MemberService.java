package com.community.library.management.service;


import com.community.library.management.dto.MemberDTO;
import com.community.library.management.exception.MemberNotDeletableException;
import com.community.library.management.exception.MemberNotFoundException;
import com.community.library.management.model.Member;
import com.community.library.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member createMember(Member member) {
        member.setMembershipDate(LocalDate.now());
        return memberRepository.save(member);
    }

    public Member updateMember (Long memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.setName(memberDTO.getName());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        if (!member.getBorrowedBooks().isEmpty()) {
            throw new MemberNotDeletableException("Member cannot be deleted because they have borrowed books");
        }
        memberRepository.deleteById(id);
    }

}