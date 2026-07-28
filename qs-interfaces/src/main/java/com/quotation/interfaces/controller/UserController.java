package com.quotation.interfaces.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.quotation.application.dto.user.LoginCommand;
import com.quotation.application.dto.user.RegisterCommand;
import com.quotation.application.dto.user.UserDTO;
import com.quotation.application.service.UserApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器 - RESTful API (支持小程序和管理端)
 */
@RestController
@RequestMapping({"/api/users", "/api/user"})
public class UserController {

    @Resource
    private UserApplicationService userApplicationService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody RegisterCommand command) {
        return userApplicationService.register(command);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<SaTokenInfo> login(@RequestBody LoginCommand command) {
        return userApplicationService.login(command);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return userApplicationService.logout();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping({"/current", "/info"})
    public Result<UserDTO> getCurrentUser() {
        return userApplicationService.getCurrentUser();
    }

    /**
     * 根据 ID 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        return userApplicationService.getUserById(id);
    }
}
