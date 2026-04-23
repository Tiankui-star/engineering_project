package com.ruoyi.Dep.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 院系信息管理对象 department
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public class Department extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long departmentId;

    /** 院系代码 */
    @Excel(name = "院系代码")
    private String departmentCode;

    /** 院系名称 */
    @Excel(name = "院系名称")
    private String departmentName;

    /** 系主任ID */
    private Long headFacultyId;

    /** 系主任姓名（非数据库字段，用于展示） */
    private String headFacultyName;

    /** 简介 */
    @Excel(name = "简介")
    private String description;

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public void setDepartmentCode(String departmentCode) 
    {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentCode() 
    {
        return departmentCode;
    }

    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }

    public void setHeadFacultyId(Long headFacultyId) 
    {
        this.headFacultyId = headFacultyId;
    }

    public Long getHeadFacultyId() 
    {
        return headFacultyId;
    }

    public void setHeadFacultyName(String headFacultyName) 
    {
        this.headFacultyName = headFacultyName;
    }

    public String getHeadFacultyName() 
    {
        return headFacultyName;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("departmentId", getDepartmentId())
            .append("departmentCode", getDepartmentCode())
            .append("departmentName", getDepartmentName())
            .append("headFacultyId", getHeadFacultyId())
            .append("headFacultyName", getHeadFacultyName())
            .append("description", getDescription())
            .toString();
    }
}
