package com.hope.domain.dto;

import lombok.Data;

@Data
public class ModifyFormDTO {

    private Long id;

    private String username;

    private String email;
    
    private String oldPassword;

    private String password;

    private String passwordConfirm;
}