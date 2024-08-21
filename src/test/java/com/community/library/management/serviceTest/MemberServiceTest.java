package com.community.library.management.serviceTest;


import com.community.library.management.dto.MemberDTO;
import com.community.library.management.exception.MemberNotDeletableException;
import com.community.library.management.exception.MemberNotFoundException;
import com.community.library.management.mapper.MemberMapper;
import com.community.library.management.model.BorrowedBook;
import com.community.library.management.model.Member;
import com.community.library.management.repository.MemberRepository;
import com.community.library.management.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberMapper memberMapper;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void testFindAll() {
        memberService.findAll();
        verify(memberRepository, times(1)).findAll();
    }
    @Test
    public void testFindByIdSuccess() {
        Member member = new Member("Test Member");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById(1L);

        assertEquals(member,foundMember);
        assertEquals("Test Member", foundMember.getName());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.findById(1L);
        });

        verify(memberRepository, times(1)).findById(1L);
    }
    @Test
    public void testCreateMember() {
        Member member = new Member("Test Member");

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO memberDTO = new MemberDTO("Test Member");

        when(memberMapper.toEntity(any(MemberDTO.class))).thenReturn(member);

        Member savedMemberDTO = memberService.createMember(memberDTO);

        assertEquals("Test Member", savedMemberDTO.getName());
        assertNotNull(savedMemberDTO.getMembershipDate());

        verify(memberRepository, times(1)).save(member);
    }
    @Test
    public void testUpdateMemberSuccess() {
        Member member = new Member("Test Member");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("Updated Member");

        when(memberRepository.save(member)).thenReturn(member);
        assertNotNull(member.getName(), "Member's name should not be null before save");

        Member updatedMember = memberService.updateMember(1L, memberDTO);

        assertEquals("Updated Member", updatedMember.getName());
        verify(memberRepository, times(1)).save(member);
    }
    @Test
    public void testUpdateMemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("Updated Member");

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.updateMember(1L, memberDTO);
        });

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void testDeleteMemberSuccess() {
        Member member = new Member("Test Member");
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteMemberHasBorrowedBooks() {
        Member member = new Member("Test Member");
        member.getBorrowedBooks().add(new BorrowedBook());
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberNotDeletableException exception = assertThrows(MemberNotDeletableException.class, () -> {
            memberService.deleteMember(1L);
        });

        assertEquals("Member cannot be deleted because they have borrowed books", exception.getMessage());
        verify(memberRepository, never()).deleteById(1L);
    }
    @Test
    public void testDeleteMemberNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.deleteMember(1L);
        });

        verify(memberRepository, never()).deleteById(1L);
    }
}
