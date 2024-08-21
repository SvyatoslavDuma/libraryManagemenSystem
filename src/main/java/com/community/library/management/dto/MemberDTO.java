package com.community.library.management.dto;

import com.community.library.management.model.BorrowedBook;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberDTO {

    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @NotEmpty(message = "Name is required")
    @Schema(description = "New name of the user", example = "John Doe")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate membershipDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    public MemberDTO() {
    }
    public MemberDTO(String name) {
        this.name= name;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
