package com.ruoyi.Faculty.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教职工信息及科研统计对象 faculty
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public class Faculty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联系统用户ID (sys_user.user_id) */
    private Long facultyId;

    /** 工号 */
    @Excel(name = "工号")
    private String facultyNumber;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 职称 */
    @Excel(name = "职称")
    private String title;

    /** 研究领域 */
    @Excel(name = "研究领域")
    private String researchField;

    /** 所属院系ID */
    @Excel(name = "所属院系ID")
    private Long departmentId;

    /** 所属院系名称（非数据库字段） */
    private String departmentName;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date employmentDate;

    /** 在职状态 */
    @Excel(name = "在职状态")
    private String status;

    /** [冗余] 论文数 */
    @Excel(name = "[冗余] 论文数")
    private Long paperCount;

    /** [冗余] 项目数 */
    @Excel(name = "[冗余] 项目数")
    private Long projectCount;

    /** [冗余] 总资金 */
    @Excel(name = "[冗余] 总资金")
    private BigDecimal totalFunding;

    public void setFacultyId(Long facultyId) 
    {
        this.facultyId = facultyId;
    }

    public Long getFacultyId() 
    {
        return facultyId;
    }

    public void setFacultyNumber(String facultyNumber) 
    {
        this.facultyNumber = facultyNumber;
    }

    public String getFacultyNumber() 
    {
        return facultyNumber;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setResearchField(String researchField) 
    {
        this.researchField = researchField;
    }

    public String getResearchField() 
    {
        return researchField;
    }

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }

    public void setEmploymentDate(Date employmentDate) 
    {
        this.employmentDate = employmentDate;
    }

    public Date getEmploymentDate() 
    {
        return employmentDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPaperCount(Long paperCount) 
    {
        this.paperCount = paperCount;
    }

    public Long getPaperCount() 
    {
        return paperCount;
    }

    public void setProjectCount(Long projectCount) 
    {
        this.projectCount = projectCount;
    }

    public Long getProjectCount() 
    {
        return projectCount;
    }

    public void setTotalFunding(BigDecimal totalFunding) 
    {
        this.totalFunding = totalFunding;
    }

    public BigDecimal getTotalFunding() 
    {
        return totalFunding;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("facultyId", getFacultyId())
            .append("facultyNumber", getFacultyNumber())
            .append("name", getName())
            .append("title", getTitle())
            .append("researchField", getResearchField())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("employmentDate", getEmploymentDate())
            .append("status", getStatus())
            .append("paperCount", getPaperCount())
            .append("projectCount", getProjectCount())
            .append("totalFunding", getTotalFunding())
            .toString();
    }
}
