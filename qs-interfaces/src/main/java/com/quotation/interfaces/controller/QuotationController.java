package com.quotation.interfaces.controller;

import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.application.dto.QuotationDTO;
import com.quotation.application.dto.UpdateQuotationCommand;
import com.quotation.application.service.QuotationApplicationService;
import com.quotation.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报价单控制器
 */
@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Resource
    private QuotationApplicationService quotationService;

    /**
     * 创建报价单
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody CreateQuotationCommand command) {
        return quotationService.createQuotation(command);
    }

    /**
     * 更新报价单
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody UpdateQuotationCommand command) {
        return quotationService.updateQuotation(command);
    }

    /**
     * 查询详情
     */
    @GetMapping("/detail/{id}")
    public Result<QuotationDTO> detail(@PathVariable Long id) {
        return quotationService.getQuotationDetail(id);
    }

    /**
     * 列表查询（不需要 userId 参数，从登录上下文获取）
     */
    @GetMapping("/list")
    public Result<List<QuotationDTO>> list(
            @RequestParam(required = false) String status) {
        return quotationService.listQuotations(status);
    }

    /**
     * 批准报价
     */
    @PostMapping("/approve/{id}")
    public Result<Boolean> approve(@PathVariable Long id) {
        return quotationService.approveQuotation(id);
    }

    /**
     * 拒绝报价
     */
    @PostMapping("/reject/{id}")
    public Result<Boolean> reject(@PathVariable Long id) {
        return quotationService.rejectQuotation(id);
    }
}
