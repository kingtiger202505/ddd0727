package com.quotation.interfaces.config;

import cn.dev33.satoken.stp.StpInterface;
import com.quotation.domain.user.model.User;
import com.quotation.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Sa-Token 权限和角色加载接口实现
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserRepository userRepository;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 根据业务需求后续可在此查数据库 permission 表
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();
        try {
            Long userId = Long.parseLong(loginId.toString());
            Optional<User> userOpt = userRepository.findById(userId);
            userOpt.ifPresent(user -> {
                if (user.getRole() != null) {
                    roles.add(user.getRole());
                }
            });
        } catch (Exception ignored) {
        }
        return roles;
    }
}
