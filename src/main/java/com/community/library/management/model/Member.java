package com.community.library.management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate membershipDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    public Member() {
    }

    public Member(String name) {
        this.name = name;
        this.membershipDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
