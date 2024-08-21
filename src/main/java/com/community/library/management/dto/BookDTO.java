package com.community.library.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookDTO {

    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

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

    private Integer amount;


    public BookDTO() {
    }


    public BookDTO(String title, String author, Integer amount) {
        this.title = title;
        this.author = author;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}