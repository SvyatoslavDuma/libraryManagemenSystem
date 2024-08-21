package com.community.library.management.mapper;

import com.community.library.management.dto.MemberDTO;
import com.community.library.management.model.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDTO toDTO(Member member);
    Member toEntity(MemberDTO memberDTO);
}
