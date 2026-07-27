package com.quotation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quotation.domain.quotation.model.Quotation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 报价单 Mapper 接口
 */
@Mapper
public interface QuotationMapper extends BaseMapper<Quotation> {

    /**
     * 乐观锁更新
     */
    @Update("UPDATE quotations SET price = #{quotation.price}, status = #{quotation.status}, " +
            "version = version + 1, updated_at = #{quotation.updatedAt} " +
            "WHERE id = #{quotation.id} AND version = #{oldVersion}")
    int updateWithOptimisticLock(@Param("quotation") Quotation quotation, @Param("oldVersion") Integer oldVersion);
}
