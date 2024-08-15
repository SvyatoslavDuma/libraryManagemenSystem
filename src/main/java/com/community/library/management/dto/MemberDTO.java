package com.community.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class MemberDTO {

    @Schema(description = "New name of the user", example = "John Doe")
    private String name;


    public MemberDTO() {
    }


    public MemberDTO(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
