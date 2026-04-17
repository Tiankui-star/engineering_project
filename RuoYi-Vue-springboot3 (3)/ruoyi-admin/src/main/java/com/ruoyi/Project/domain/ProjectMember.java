package com.ruoyi.Project.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目成员对象 project_member
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public class ProjectMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成员记录ID */
    private Long memberId;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 成员类型：教师/学生 */
    @Excel(name = "成员类型：教师/学生")
    private String memberType;

    /** 成员ID */
    @Excel(name = "成员ID")
    private Long userId;

    /** 角色类型 */
    @Excel(name = "角色类型")
    private String roleType;

    /** 角色描述 */
    @Excel(name = "角色描述")
    private String roleDescription;

    /** 加入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinDate;

    /** 离开日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveDate;

    /** 贡献百分比 */
    @Excel(name = "贡献百分比")
    private Double contributionPercent;

    /** 成员名称（非数据库字段） */
    private String memberName;

    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setMemberType(String memberType) 
    {
        this.memberType = memberType;
    }

    public String getMemberType() 
    {
        return memberType;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setRoleType(String roleType) 
    {
        this.roleType = roleType;
    }

    public String getRoleType() 
    {
        return roleType;
    }

    public void setRoleDescription(String roleDescription) 
    {
        this.roleDescription = roleDescription;
    }

    public String getRoleDescription() 
    {
        return roleDescription;
    }

    public void setJoinDate(Date joinDate) 
    {
        this.joinDate = joinDate;
    }

    public Date getJoinDate() 
    {
        return joinDate;
    }

    public void setLeaveDate(Date leaveDate) 
    {
        this.leaveDate = leaveDate;
    }

    public Date getLeaveDate() 
    {
        return leaveDate;
    }

    public void setContributionPercent(Double contributionPercent) 
    {
        this.contributionPercent = contributionPercent;
    }

    public Double getContributionPercent() 
    {
        return contributionPercent;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getMemberName()
    {
        return memberName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("projectId", getProjectId())
            .append("memberType", getMemberType())
            .append("userId", getUserId())
            .append("roleType", getRoleType())
            .append("roleDescription", getRoleDescription())
            .append("joinDate", getJoinDate())
            .append("leaveDate", getLeaveDate())
            .append("contributionPercent", getContributionPercent())
            .append("memberName", getMemberName())
            .toString();
    }
}
