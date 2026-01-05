package com.hope.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginFormDTO {

    private Long id;

    private String username;

    private String email;
    
    private String code;
    
    private int role;
    
    private String password;

    private String passwordConfirm;
    
    private Integer status;
}
