package com.quotation.application.service;

import com.quotation.application.convert.QuotationConvert;
import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.application.dto.QuotationDTO;
import com.quotation.application.dto.UpdateQuotationCommand;
import com.quotation.common.result.Result;
import com.quotation.domain.quotation.model.Quotation;
import com.quotation.domain.quotation.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报价应用服务
 */
@Service
public class QuotationApplicationService {

    @Resource
    private QuotationRepository quotationRepository;

    @Resource
    private QuotationConvert quotationConvert;

    /**
     * 创建报价单
     */
    @Transactional
    public Result<Long> createQuotation(CreateQuotationCommand command) {
        try {
            Quotation quotation = quotationConvert.toEntity(command);
            quotation.setSellerId(command.getSellerId());
            quotation.setBuyerId(command.getBuyerId());
            quotation.setPrice(command.getPrice());
            quotation.setStatus("PENDING");
            quotation.setVersion(1);
            quotation.setCreatedAt(LocalDateTime.now());
            quotation.setUpdatedAt(LocalDateTime.now());
            
            quotationRepository.save(quotation);
            return Result.success(quotation.getId());
        } catch (Exception e) {
            return Result.error("创建报价单失败：" + e.getMessage());
        }
    }

    /**
     * 更新报价单
     */
    @Transactional
    public Result<Boolean> updateQuotation(UpdateQuotationCommand command) {
        try {
            Quotation quotation = quotationRepository.findById(command.getId());
            if (quotation == null) {
                return Result.error("报价单不存在");
            }
            
            quotation.setProductName(command.getProductName());
            quotation.setQuantity(command.getQuantity());
            quotation.setPrice(command.getUnitPrice());
            quotation.setUpdatedAt(LocalDateTime.now());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("更新报价单失败：" + e.getMessage());
        }
    }

    /**
     * 查询报价单详情
     */
    public Result<QuotationDTO> getQuotationDetail(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.error("报价单不存在");
            }
            return Result.success(quotationConvert.toDTO(quotation));
        } catch (Exception e) {
            return Result.error("查询报价单详情失败：" + e.getMessage());
        }
    }

    /**
     * 查询报价单列表（从登录上下文获取用户 ID）
     */
    public Result<List<QuotationDTO>> listQuotations(String status) {
        try {
            Long currentUserId = 1L; // 临时使用固定值
            List<Quotation> quotations = quotationRepository.findByUserIdAndStatus(currentUserId, status);
            return Result.success(quotationConvert.toDTOList(quotations));
        } catch (Exception e) {
            return Result.error("查询报价单列表失败：" + e.getMessage());
        }
    }

    /**
     * 批准报价
     */
    @Transactional
    public Result<Boolean> approveQuotation(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.error("报价单不存在");
            }
            
            quotation.setStatus("APPROVED");
            quotation.setUpdatedAt(LocalDateTime.now());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("批准报价失败：" + e.getMessage());
        }
    }

    /**
     * 拒绝报价
     */
    @Transactional
    public Result<Boolean> rejectQuotation(Long id) {
        try {
            Quotation quotation = quotationRepository.findById(id);
            if (quotation == null) {
                return Result.error("报价单不存在");
            }
            
            quotation.setStatus("REJECTED");
            quotation.setUpdatedAt(LocalDateTime.now());
            
            quotationRepository.updateWithOptimisticLock(quotation);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("拒绝报价失败：" + e.getMessage());
        }
    }
}
