package com.ruoyi.Stu.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生基本信息及成就统计对象 student
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public class Student extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联系统用户ID (sys_user.user_id) */
    private Long studentId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNumber;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 所属院系ID */
    @Excel(name = "所属院系ID")
    private Long departmentId;

    /** 入学日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入学日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date enrollmentDate;

    /** 学籍状态 */
    @Excel(name = "学籍状态")
    private String status;

    /** 获奖数 */
    @Excel(name = "获奖数")
    private Long awardCount;

    /** 参与论文数 */
    @Excel(name = "参与论文数")
    private Long paperCount;

    /** 参与项目数 */
    @Excel(name = "参与项目数")
    private Long projectCount;

    /** 学生获得的各类奖项记录信息 */
    private List<StudentAward> studentAwardList;

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }

    public void setStudentNumber(String studentNumber) 
    {
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() 
    {
        return studentNumber;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public void setEnrollmentDate(Date enrollmentDate) 
    {
        this.enrollmentDate = enrollmentDate;
    }

    public Date getEnrollmentDate() 
    {
        return enrollmentDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setAwardCount(Long awardCount) 
    {
        this.awardCount = awardCount;
    }

    public Long getAwardCount() 
    {
        return awardCount;
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

    public List<StudentAward> getStudentAwardList()
    {
        return studentAwardList;
    }

    public void setStudentAwardList(List<StudentAward> studentAwardList)
    {
        this.studentAwardList = studentAwardList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("studentId", getStudentId())
            .append("studentNumber", getStudentNumber())
            .append("name", getName())
            .append("birthDate", getBirthDate())
            .append("gender", getGender())
            .append("idCard", getIdCard())
            .append("departmentId", getDepartmentId())
            .append("enrollmentDate", getEnrollmentDate())
            .append("status", getStatus())
            .append("awardCount", getAwardCount())
            .append("paperCount", getPaperCount())
            .append("projectCount", getProjectCount())
            .append("studentAwardList", getStudentAwardList())
            .toString();
    }
}
