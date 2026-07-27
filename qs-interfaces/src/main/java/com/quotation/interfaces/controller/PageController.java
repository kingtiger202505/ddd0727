package com.quotation.interfaces.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.quotation.application.dto.user.UserDTO;
import com.quotation.application.service.UserApplicationService;
import com.quotation.common.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 页面控制器 - 负责返回前端页面
 */
@Controller
public class PageController {

    @Resource
    private UserApplicationService userApplicationService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Model model) {
        // 检查是否登录
        if (StpUtil.isLogin()) {
            Result<UserDTO> result = userApplicationService.getCurrentUser();
            if (result.isSuccess() && result.getData() != null) {
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("username", result.getData().getUsername());
            } else {
                model.addAttribute("isLoggedIn", false);
            }
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "index";
    }
}
