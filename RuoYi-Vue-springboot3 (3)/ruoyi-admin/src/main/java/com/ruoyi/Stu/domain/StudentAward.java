package com.ruoyi.Stu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生获得的各类奖项记录对象 student_award
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public class StudentAward extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long awardId;

    /**  */
    @Excel(name = "")
    private String awardName;

    /**  */
    @Excel(name = "")
    private String description;

    /**  */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "", width = 30, dateFormat = "yyyy-MM-dd")
    private Date awardDate;

    /**  */
    @Excel(name = "")
    private String awardLevel;

    /**  */
    @Excel(name = "")
    private String issuingOrganization;

    /**  */
    @Excel(name = "")
    private Long studentId;

    /** 指导老师(可为空) */
    @Excel(name = "指导老师(可为空)")
    private Long supervisorFacultyId;

    public void setAwardId(Long awardId) 
    {
        this.awardId = awardId;
    }

    public Long getAwardId() 
    {
        return awardId;
    }
    public void setAwardName(String awardName) 
    {
        this.awardName = awardName;
    }

    public String getAwardName() 
    {
        return awardName;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setAwardDate(Date awardDate) 
    {
        this.awardDate = awardDate;
    }

    public Date getAwardDate() 
    {
        return awardDate;
    }
    public void setAwardLevel(String awardLevel) 
    {
        this.awardLevel = awardLevel;
    }

    public String getAwardLevel() 
    {
        return awardLevel;
    }
    public void setIssuingOrganization(String issuingOrganization) 
    {
        this.issuingOrganization = issuingOrganization;
    }

    public String getIssuingOrganization() 
    {
        return issuingOrganization;
    }
    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }
    public void setSupervisorFacultyId(Long supervisorFacultyId) 
    {
        this.supervisorFacultyId = supervisorFacultyId;
    }

    public Long getSupervisorFacultyId() 
    {
        return supervisorFacultyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("awardId", getAwardId())
            .append("awardName", getAwardName())
            .append("description", getDescription())
            .append("awardDate", getAwardDate())
            .append("awardLevel", getAwardLevel())
            .append("issuingOrganization", getIssuingOrganization())
            .append("studentId", getStudentId())
            .append("supervisorFacultyId", getSupervisorFacultyId())
            .toString();
    }
}
