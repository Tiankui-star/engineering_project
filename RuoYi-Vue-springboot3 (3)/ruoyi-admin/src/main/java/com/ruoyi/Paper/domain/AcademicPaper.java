package com.ruoyi.Paper.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学术论文信息对象 academic_paper
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class AcademicPaper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long paperId;

    /** 论文标题 */
    @Excel(name = "论文标题")
    private String paperTitle;

    /** 摘要 */
    @Excel(name = "摘要")
    private String paperAbstract;

    /** 期刊名称 */
    @Excel(name = "期刊名称")
    private String journalName;

    /** 卷期 */
    @Excel(name = "卷期")
    private String volumeIssue;

    /** 页码 */
    @Excel(name = "页码")
    private String pageNumbers;

    /** 发表日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发表日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publicationDate;

    /** 发表级别 */
    @Excel(name = "发表级别")
    private String publicationLevel;

    /** DOI号 */
    @Excel(name = "DOI号")
    private String doi;

    /** 被引次数 */
    private Long citationCount;

    /** 作者类型：教师或学生 */
    @Excel(name = "作者类型：教师或学生")
    private String firstAuthorType;

    /** 一作ID（教师ID或学生ID） */
    private Long firstAuthorId;

    /** 一作姓名（非数据库字段，用于显示） */
    @Excel(name = "一作姓名")
    private String firstAuthorName;

    /** 所属院系ID */
    private Long departmentId;

    /** 所属院系名称（非数据库字段，用于显示） */
    @Excel(name = "所属院系")
    private String departmentName;

    /** 论文作者列表（非数据库字段） */
    private List<PaperAuthor> paperAuthorList;

    public void setPaperId(Long paperId) 
    {
        this.paperId = paperId;
    }

    public Long getPaperId() 
    {
        return paperId;
    }

    public void setPaperTitle(String paperTitle) 
    {
        this.paperTitle = paperTitle;
    }

    public String getPaperTitle() 
    {
        return paperTitle;
    }

    public void setPaperAbstract(String paperAbstract) 
    {
        this.paperAbstract = paperAbstract;
    }

    public String getPaperAbstract() 
    {
        return paperAbstract;
    }

    public void setJournalName(String journalName) 
    {
        this.journalName = journalName;
    }

    public String getJournalName() 
    {
        return journalName;
    }

    public void setVolumeIssue(String volumeIssue) 
    {
        this.volumeIssue = volumeIssue;
    }

    public String getVolumeIssue() 
    {
        return volumeIssue;
    }

    public void setPageNumbers(String pageNumbers) 
    {
        this.pageNumbers = pageNumbers;
    }

    public String getPageNumbers() 
    {
        return pageNumbers;
    }

    public void setPublicationDate(Date publicationDate) 
    {
        this.publicationDate = publicationDate;
    }

    public Date getPublicationDate() 
    {
        return publicationDate;
    }

    public void setPublicationLevel(String publicationLevel) 
    {
        this.publicationLevel = publicationLevel;
    }

    public String getPublicationLevel() 
    {
        return publicationLevel;
    }

    public void setDoi(String doi) 
    {
        this.doi = doi;
    }

    public String getDoi() 
    {
        return doi;
    }

    public void setCitationCount(Long citationCount) 
    {
        this.citationCount = citationCount;
    }

    public Long getCitationCount() 
    {
        return citationCount;
    }

    public void setFirstAuthorType(String firstAuthorType) 
    {
        this.firstAuthorType = firstAuthorType;
    }

    public String getFirstAuthorType() 
    {
        return firstAuthorType;
    }

    public void setFirstAuthorId(Long firstAuthorId) 
    {
        this.firstAuthorId = firstAuthorId;
    }

    public Long getFirstAuthorId() 
    {
        return firstAuthorId;
    }

    public String getFirstAuthorName() 
    {
        return firstAuthorName;
    }

    public void setFirstAuthorName(String firstAuthorName) 
    {
        this.firstAuthorName = firstAuthorName;
    }

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public List<PaperAuthor> getPaperAuthorList() 
    {
        return paperAuthorList;
    }

    public void setPaperAuthorList(List<PaperAuthor> paperAuthorList) 
    {
        this.paperAuthorList = paperAuthorList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paperId", getPaperId())
            .append("paperTitle", getPaperTitle())
            .append("paperAbstract", getPaperAbstract())
            .append("journalName", getJournalName())
            .append("volumeIssue", getVolumeIssue())
            .append("pageNumbers", getPageNumbers())
            .append("publicationDate", getPublicationDate())
            .append("publicationLevel", getPublicationLevel())
            .append("doi", getDoi())
            .append("citationCount", getCitationCount())
            .append("firstAuthorType", getFirstAuthorType())
            .append("firstAuthorId", getFirstAuthorId())
            .append("firstAuthorName", getFirstAuthorName())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("paperAuthorList", getPaperAuthorList())
            .toString();
    }
}
