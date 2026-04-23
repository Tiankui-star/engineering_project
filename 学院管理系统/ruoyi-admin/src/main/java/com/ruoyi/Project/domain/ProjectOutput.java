package com.ruoyi.Project.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目成果关联（论文和专利）对象 project_output
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public class ProjectOutput extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long projectOutputId;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 成果类型：论文/专利 */
    @Excel(name = "成果类型：论文/专利")
    private String outputType;

    /** 成果ID */
    @Excel(name = "成果ID")
    private Long outputId;

    /** 产出日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "产出日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outputDate;

    /** 贡献描述 */
    @Excel(name = "贡献描述")
    private String contributionDescription;

    /** 论文标题（非数据库字段） */
    private String paperTitle;

    /** 专利名称（非数据库字段） */
    private String patentName;

    public void setProjectOutputId(Long projectOutputId) 
    {
        this.projectOutputId = projectOutputId;
    }

    public Long getProjectOutputId() 
    {
        return projectOutputId;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setOutputType(String outputType) 
    {
        this.outputType = outputType;
    }

    public String getOutputType() 
    {
        return outputType;
    }
    public void setOutputId(Long outputId) 
    {
        this.outputId = outputId;
    }

    public Long getOutputId() 
    {
        return outputId;
    }
    public void setOutputDate(Date outputDate) 
    {
        this.outputDate = outputDate;
    }

    public Date getOutputDate() 
    {
        return outputDate;
    }
    public void setContributionDescription(String contributionDescription) 
    {
        this.contributionDescription = contributionDescription;
    }

    public String getContributionDescription() 
    {
        return contributionDescription;
    }

    public void setPaperTitle(String paperTitle)
    {
        this.paperTitle = paperTitle;
    }

    public String getPaperTitle()
    {
        return paperTitle;
    }

    public void setPatentName(String patentName)
    {
        this.patentName = patentName;
    }

    public String getPatentName()
    {
        return patentName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectOutputId", getProjectOutputId())
            .append("projectId", getProjectId())
            .append("outputType", getOutputType())
            .append("outputId", getOutputId())
            .append("outputDate", getOutputDate())
            .append("contributionDescription", getContributionDescription())
            .append("paperTitle", getPaperTitle())
            .append("patentName", getPatentName())
            .toString();
    }
}
