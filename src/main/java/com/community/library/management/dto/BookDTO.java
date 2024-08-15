package com.community.library.management.dto;



public class BookDTO {

    private String title;

    private String author;

    private Integer amount;


    public BookDTO() {
    }


    public BookDTO(String title, String author, Integer amount) {
        this.title = title;
        this.author = author;
        this.amount = amount;
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