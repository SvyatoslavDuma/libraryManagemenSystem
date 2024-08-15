package com.community.library.management.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Size(min = 3, message = "Title must have at least 3 characters")
    @Pattern(regexp = "^[A-Z].*", message = "Title should start with a capital letter")
    @Schema(description = "Title of the book", example = "Iron Flame")
    private String title;

    @NotEmpty
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Author should be in 'First Last' format")
    @Schema(description = "Author of the book", example = "Rebecca Yarros")
    private String author;


    @Min(value = 0, message = "Amount must be >=0 ")
    private int amount;

    public Book() {
    }

    public Book(String title, String author, int amount) {
        this.title = title;
        this.author = author;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
