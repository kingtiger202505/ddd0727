package com.quotation.domain.user.repository;

import com.quotation.domain.user.model.User;
import java.util.Optional;

/**
 * 用户仓储接口
 */
public interface UserRepository {
    
    /**
     * 保存用户
     */
    void save(User user);
    
    /**
     * 根据 ID 查找用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 更新用户信息
     */
    void update(User user);
    
    /**
     * 删除用户
     */
    void delete(Long id);
}
