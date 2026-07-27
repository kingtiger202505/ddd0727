package com.quotation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quotation.domain.quotation.model.Quotation;
import com.quotation.domain.quotation.repository.QuotationRepository;
import com.quotation.infrastructure.persistence.mapper.QuotationMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报价单仓储实现
 */
@Repository
public class QuotationRepositoryImpl implements QuotationRepository {

    @Resource
    private QuotationMapper quotationMapper;

    @Override
    public void save(Quotation quotation) {
        quotationMapper.insert(quotation);
    }

    @Override
    public Quotation findById(Long id) {
        return quotationMapper.selectById(id);
    }

    @Override
    public List<Quotation> findByUserIdAndStatus(Long userId, String status) {
        LambdaQueryWrapper<Quotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Quotation::getBuyerId, userId)
               .or().eq(Quotation::getSellerId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Quotation::getStatus, status);
        }
        wrapper.orderByDesc(Quotation::getCreatedAt);
        return quotationMapper.selectList(wrapper);
    }

    @Override
    public boolean updateWithOptimisticLock(Quotation quotation) {
        Integer oldVersion = quotation.getVersion();
        int rows = quotationMapper.updateWithOptimisticLock(quotation, oldVersion);
        return rows > 0;
    }

    @Override
    public void delete(Long id) {
        quotationMapper.deleteById(id);
    }
}
