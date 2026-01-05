package com.hope.service;

import com.hope.domain.dto.LoginFormDTO;
import com.hope.domain.dto.ModifyFormDTO;
import com.hope.domain.dto.UserDTO;
import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.User;
import com.hope.domain.vo.LoginVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    LoginVO login(LoginFormDTO user);

    void register(LoginFormDTO user);

    UserDTO findUserByUsername(String email);

    void forget(LoginFormDTO user);

    void sendCode(LoginFormDTO user);

    void verifyCode(String username,String email, String code);
    
    void edit(User user);

    Result refreshToken(String accessToken, HttpServletResponse response);

    Result getUserInfo();

    String updateAvatar(MultipartFile avatar, Long id) throws IOException;

    void modify(ModifyFormDTO user);
}
