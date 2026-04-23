package com.ruoyi.web.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 统计数据对象 statistics_view
 * 
 * @author ruoyi
 * @date 2026-04-14
 */
public class StatisticsView extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    private Integer statId;

    /** 维度类型：GLOBAL-全局 / DEPARTMENT-院系 */
    @Excel(name = "维度类型")
    private String dimensionType;

    /** 维度ID（院系ID） */
    @Excel(name = "维度ID")
    private Integer dimensionId;

    /** 指标名称 */
    @Excel(name = "指标名称")
    private String metricName;

    /** 指标值 */
    @Excel(name = "指标值")
    private BigDecimal metricValue;

    /** 更新时间 */
    private Date updateDate;

    public void setStatId(Integer statId) 
    {
        this.statId = statId;
    }

    public Integer getStatId() 
    {
        return statId;
    }

    public void setDimensionType(String dimensionType) 
    {
        this.dimensionType = dimensionType;
    }

    public String getDimensionType() 
    {
        return dimensionType;
    }

    public void setDimensionId(Integer dimensionId) 
    {
        this.dimensionId = dimensionId;
    }

    public Integer getDimensionId() 
    {
        return dimensionId;
    }

    public void setMetricName(String metricName) 
    {
        this.metricName = metricName;
    }

    public String getMetricName() 
    {
        return metricName;
    }

    public void setMetricValue(BigDecimal metricValue) 
    {
        this.metricValue = metricValue;
    }

    public BigDecimal getMetricValue() 
    {
        return metricValue;
    }

    public void setUpdateDate(Date updateDate) 
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() 
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return "StatisticsView{" +
                "statId=" + statId +
                ", dimensionType='" + dimensionType + '\'' +
                ", dimensionId=" + dimensionId +
                ", metricName='" + metricName + '\'' +
                ", metricValue=" + metricValue +
                ", updateDate=" + updateDate +
                '}';
    }
}
