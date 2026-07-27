package com.quotation.domain.quotation.repository;

import com.quotation.domain.quotation.model.Quotation;
import java.util.List;

/**
 * 报价单仓储接口
 */
public interface QuotationRepository {
    
    /**
     * 保存报价单
     */
    void save(Quotation quotation);
    
    /**
     * 根据 ID 查找报价单
     */
    Quotation findById(Long id);
    
    /**
     * 根据用户 ID 和状态查询列表
     */
    List<Quotation> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * 更新报价单（乐观锁）
     */
    boolean updateWithOptimisticLock(Quotation quotation);
    
    /**
     * 删除报价单
     */
    void delete(Long id);
}
