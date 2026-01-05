package com.hope.controller;

import com.hope.domain.dto.LoginFormDTO;
import com.hope.domain.dto.ModifyFormDTO;
import com.hope.domain.dto.RefreshDTO;
import com.hope.domain.dto.UserDTO;
import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.User;
import com.hope.domain.vo.LoginVO;
import com.hope.service.UserService;
import com.hope.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 3402351070
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录 @RequestBody User user
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginFormDTO user) {
        LoginVO login = userService.login(user);
        return Result.ok(login);
    }
    
    /**
     * 验证码（验证）
     * @param user
     * @return
     */
    @PostMapping("/send-code")
    public Result sendCode(@Valid @RequestBody LoginFormDTO user) {
        // 只要这行没报错，就说明成功了
        userService.sendCode(user);
        return Result.ok("邮箱验证码发送成功");
    }


    /**
     * 注册 @RequestBody User user
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody LoginFormDTO user){
        userService.register(user);
        return Result.ok("注册成功");
    }


    /**
     * 忘记密码 id email code password
     *
     * @param user
     * @return
     */
    @PostMapping("/forget")
    public Result forget(@RequestBody LoginFormDTO user){
        userService.forget(user);
        return Result.ok("重置密码成功");
    }
    /**
     * 忘记密码 id email code password
     *
     * @param user
     * @return
     */
    @PostMapping("/update-password")
    public Result modify(@RequestBody ModifyFormDTO user){
        userService.modify(user);
        return Result.ok("重置密码成功");
    }

    /**
     * 这是给用来展示单独的信息的比如 头像 之类的
     * @param username
     * @return
     */
    @GetMapping("/find-by-username/{username}")
    public Result findUserByUsername(@PathVariable String username) {
        // 只要这行不抛出异常，返回的就是正常的 UserDTO
        UserDTO userByUsername = userService.findUserByUsername(username);
        return Result.ok(userByUsername);
    }

    /**
     * 这是给用来修改单独的信息的比如 年龄,性别,个人所在地,邮箱 之类的
     * @param user
     * @return
     */
    @PostMapping("/edit")//upload
    public Result edit(@RequestBody User user){
        userService.edit(user);
        return Result.ok("数据库更新成功");
        
    }

    /**
     * 查询 id 的部分用户
     * @return
     */
    @GetMapping("/userinfo")
    public Result getUserInfo(){
        return userService.getUserInfo();
    }
    /**
     * 更新 双token
     * @param response
     * @param
     * @return
     */
    @PostMapping("refreshToken")
    public Result refreshToken(@RequestBody RefreshDTO refreshDTO, HttpServletResponse response){
        String refreshToken=refreshDTO.getRefreshToken();
        System.out.println("refreshToken="+refreshToken);
        return userService.refreshToken(refreshToken,response);
    }

  
    /**
     * 更换头像
     * @param avatar
     * @return
     */
    @PostMapping("/avatar")
    public Result updateAvatar(MultipartFile avatar) throws IOException {
        if (avatar == null || avatar.isEmpty()) {
            return Result.fail("头像文件不能为空");
        }
        if (avatar.getSize() > 2 * 1024 * 1024) {   // 2 MB
            return Result.fail("头像大小不能超过 2MB");
        }
        Long id=Long.parseLong(ThreadLocalUtil.get());
        String url = userService.updateAvatar(avatar, id);
        return Result.ok(url);
    }
//    @GetMapping("/")
//    public String index() {
//        return "<h1>欢迎！请访问 <a href='/user'>/user</a> 查看你的 GitHub 信息</h1>";
//    }

    /**
     * @AuthenticationPrincipal 这是一个非常方便的注解
     * 它可以直接把登录成功的 GitHub 用户信息注入进来
     */
//    @GetMapping("/user")
//    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
//        // 返回用户的所有信息（JSON格式），包含用户名、头像、Email等
//        return principal.getAttributes();
//    }
}
