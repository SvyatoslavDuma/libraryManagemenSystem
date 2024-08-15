package com.community.library.management.serviceTest;


import com.community.library.management.dto.MemberDTO;
import com.community.library.management.exception.MemberNotDeletableException;
import com.community.library.management.exception.MemberNotFoundException;
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

        Optional<Member> foundMember = memberService.findById(1L);

        assertTrue(foundMember.isPresent());
        assertEquals("Test Member", foundMember.get().getName());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Member> foundMember = memberService.findById(1L);

        assertFalse(foundMember.isPresent());
        verify(memberRepository, times(1)).findById(1L);
    }
    @Test
    public void testCreateMember() {
        Member member = new Member("Test Member");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member savedMember = memberService.createMember(member);

        assertEquals("Test Member", savedMember.getName());
        assertNotNull(savedMember.getMembershipDate());
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
