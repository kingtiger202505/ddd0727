package com.quotation.application.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.quotation.application.dto.user.LoginCommand;
import com.quotation.application.dto.user.RegisterCommand;
import com.quotation.application.dto.user.UserDTO;
import com.quotation.common.exception.BusinessException;
import com.quotation.common.result.Result;
import com.quotation.domain.user.model.User;
import com.quotation.domain.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 用户应用服务
 */
@Service
public class UserApplicationService {

    @Resource
    private UserRepository userRepository;

    /**
     * 用户注册
     */
    @Transactional
    public Result<Long> register(RegisterCommand command) {
        // 校验必填字段
        if (!StringUtils.hasText(command.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        if (!StringUtils.hasText(command.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findByUsername(command.getUsername());
        if (existingUser.isPresent()) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (StringUtils.hasText(command.getPhone())) {
            Optional<User> userByPhone = userRepository.findByPhone(command.getPhone());
            if (userByPhone.isPresent()) {
                throw new BusinessException("手机号已被注册");
            }
        }
        
        // 创建用户
        String role = "BUYER";
        if ("SELLER".equals(command.getRole())) {
            role = "SELLER";
        }
        
        User user;
        if ("SELLER".equals(role)) {
            user = User.createSeller(
                command.getUsername(),
                BCrypt.hashpw(command.getPassword()),
                command.getNickname(),
                command.getPhone(),
                command.getEmail()
            );
        } else {
            user = User.createBuyer(
                command.getUsername(),
                BCrypt.hashpw(command.getPassword()),
                command.getNickname(),
                command.getPhone(),
                command.getEmail()
            );
        }
        
        userRepository.save(user);
        return Result.success(user.getId());
    }

    /**
     * 用户登录
     */
    public Result<SaTokenInfo> login(LoginCommand command) {
        if (!StringUtils.hasText(command.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        if (!StringUtils.hasText(command.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        
        // 查找用户
        Optional<User> userOpt = userRepository.findByUsername(command.getUsername());
        if (userOpt.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        
        User user = userOpt.get();
        
        // 校验密码
        if (!BCrypt.checkpw(command.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 登录
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        
        return Result.success(tokenInfo);
    }

    /**
     * 用户登出
     */
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     */
    public Result<UserDTO> getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        
        User user = userOpt.get();
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setCreateTime(user.getCreatedAt());
        
        return Result.success(dto);
    }

    /**
     * 根据 ID 获取用户信息
     */
    public Result<UserDTO> getUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new BusinessException("用户不存在");
        }
        
        User user = userOpt.get();
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setCreateTime(user.getCreatedAt());
        
        return Result.success(dto);
    }
}
