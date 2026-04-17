package com.ruoyi.Patent.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专利与作者的多对多关系对象 patent_author
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
public class PatentAuthor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long authorshipId;

    /** 专利ID */
    @Excel(name = "专利ID")
    private Long patentId;

    /** 作者类型 */
    @Excel(name = "作者类型")
    private String authorType;

    /** 作者ID（关联sys_user.user_id） */
    private Long authorId;

    /** 作者姓名（非数据库字段，用于显示） */
    private String authorName;

    /** 作者排序 */
    @Excel(name = "作者排序")
    private Long authorOrder;

    /** 贡献百分比 */
    @Excel(name = "贡献百分比")
    private BigDecimal contributionPercent;

    public void setAuthorshipId(Long authorshipId) 
    {
        this.authorshipId = authorshipId;
    }

    public Long getAuthorshipId() 
    {
        return authorshipId;
    }
    public void setPatentId(Long patentId) 
    {
        this.patentId = patentId;
    }

    public Long getPatentId() 
    {
        return patentId;
    }
    public void setAuthorType(String authorType) 
    {
        this.authorType = authorType;
    }

    public String getAuthorType() 
    {
        return authorType;
    }
    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }

    public String getAuthorName() 
    {
        return authorName;
    }

    public void setAuthorName(String authorName) 
    {
        this.authorName = authorName;
    }

    public void setAuthorOrder(Long authorOrder) 
    {
        this.authorOrder = authorOrder;
    }

    public Long getAuthorOrder() 
    {
        return authorOrder;
    }
    public void setContributionPercent(BigDecimal contributionPercent) 
    {
        this.contributionPercent = contributionPercent;
    }

    public BigDecimal getContributionPercent() 
    {
        return contributionPercent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("authorshipId", getAuthorshipId())
            .append("patentId", getPatentId())
            .append("authorType", getAuthorType())
            .append("authorId", getAuthorId())
            .append("authorName", getAuthorName())
            .append("authorOrder", getAuthorOrder())
            .append("contributionPercent", getContributionPercent())
            .toString();
    }
}
