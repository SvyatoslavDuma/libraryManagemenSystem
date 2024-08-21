package com.community.library.management.mapper;

import com.community.library.management.dto.BookDTO;
import com.community.library.management.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
}