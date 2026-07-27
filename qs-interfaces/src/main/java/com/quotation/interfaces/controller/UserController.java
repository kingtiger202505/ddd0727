package com.quotation.interfaces.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.quotation.application.dto.user.LoginCommand;
import com.quotation.application.dto.user.RegisterCommand;
import com.quotation.application.dto.user.UserDTO;
import com.quotation.application.service.UserApplicationService;
import com.quotation.interfaces.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserApplicationService userApplicationService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<Long> register(@RequestBody RegisterCommand command) {
        return userApplicationService.register(command);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<SaTokenInfo> login(@RequestBody LoginCommand command) {
        return userApplicationService.login(command);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        return userApplicationService.logout();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public R<UserDTO> getCurrentUser() {
        return userApplicationService.getCurrentUser();
    }

    /**
     * 根据 ID 获取用户信息
     */
    @GetMapping("/{id}")
    public R<UserDTO> getUserById(@PathVariable Long id) {
        return userApplicationService.getUserById(id);
    }
}
