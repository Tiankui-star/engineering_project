package com.ruoyi.Project.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目资金记录对象 funding_record
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public class FundingRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资金ID */
    private Long fundingId;

    /** 项目ID */
    private Long projectId;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 拨款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拨款日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date allocationDate;

    /** 资金来源 */
    @Excel(name = "资金来源")
    private String fundingSource;

    /** 资金类型 */
    @Excel(name = "资金类型")
    private String fundingType;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setFundingId(Long fundingId) 
    {
        this.fundingId = fundingId;
    }

    public Long getFundingId() 
    {
        return fundingId;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setAllocationDate(Date allocationDate) 
    {
        this.allocationDate = allocationDate;
    }

    public Date getAllocationDate() 
    {
        return allocationDate;
    }

    public void setFundingSource(String fundingSource) 
    {
        this.fundingSource = fundingSource;
    }

    public String getFundingSource() 
    {
        return fundingSource;
    }

    public void setFundingType(String fundingType) 
    {
        this.fundingType = fundingType;
    }

    public String getFundingType() 
    {
        return fundingType;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fundingId", getFundingId())
            .append("projectId", getProjectId())
            .append("amount", getAmount())
            .append("allocationDate", getAllocationDate())
            .append("fundingSource", getFundingSource())
            .append("fundingType", getFundingType())
            .append("status", getStatus())
            .toString();
    }
}
