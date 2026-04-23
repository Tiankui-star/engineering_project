package com.ruoyi.Paper.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文作者关系对象 paper_author
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class PaperAuthor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作者关系ID */
    private Long authorshipId;

    /** 论文ID */
    private Long paperId;

    /** 作者类型：教师或学生 */
    private String authorType;

    /** 作者ID */
    private Long authorId;

    /** 作者姓名（非数据库字段，用于显示） */
    @Excel(name = "作者姓名")
    private String authorName;

    /** 作者排序 */
    @Excel(name = "作者排序")
    private Long authorOrder;

    /** 贡献描述 */
    @Excel(name = "贡献描述")
    private String contributionDescription;

    public void setAuthorshipId(Long authorshipId) 
    {
        this.authorshipId = authorshipId;
    }

    public Long getAuthorshipId() 
    {
        return authorshipId;
    }

    public void setPaperId(Long paperId) 
    {
        this.paperId = paperId;
    }

    public Long getPaperId() 
    {
        return paperId;
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

    public void setContributionDescription(String contributionDescription) 
    {
        this.contributionDescription = contributionDescription;
    }

    public String getContributionDescription() 
    {
        return contributionDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("authorshipId", getAuthorshipId())
            .append("paperId", getPaperId())
            .append("authorType", getAuthorType())
            .append("authorId", getAuthorId())
            .append("authorName", getAuthorName())
            .append("authorOrder", getAuthorOrder())
            .append("contributionDescription", getContributionDescription())
            .toString();
    }
}
