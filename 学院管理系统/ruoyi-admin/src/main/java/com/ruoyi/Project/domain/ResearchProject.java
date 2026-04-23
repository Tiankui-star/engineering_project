package com.ruoyi.Project.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学院承担的科研项目信息对象 research_project
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public class ResearchProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long projectId;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String projectCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目描述 */
    @Excel(name = "项目描述")
    private String description;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 项目级别：国家级/省级/校级/企业/其他 */
    @Excel(name = "项目级别：国家级/省级/校级/企业/其他")
    private String projectLevel;

    /** 项目状态：申报中/进行中/已完成/暂停 */
    @Excel(name = "项目状态：申报中/进行中/已完成/暂停")
    private String projectStatus;

    /** 负责人类型：教师或学生 */
    @Excel(name = "负责人类型：教师或学生")
    private String leaderType;

    /** 负责人ID（关联sys_user.user_id） */
    @Excel(name = "负责人ID", readConverterExp = "关=联sys_user.user_id")
    private Long leaderId;

    /** 所属院系ID */
    @Excel(name = "所属院系ID")
    private Long departmentId;

    /** 负责人名称（非数据库字段） */
    private String leaderName;

    /** 所属院系名称（非数据库字段） */
    private String departmentName;

    /** 项目成果关联（论文和专利）信息 */
    private List<ProjectOutput> projectOutputList;

    /** 项目成员信息 */
    private List<ProjectMember> projectMemberList;

    /** 项目资金记录信息 */
    private List<FundingRecord> fundingRecordList;

    /** 角色数据过滤：当前登录用户ID（非数据库字段，用于按成员过滤） */
    private Long memberUserId;

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setProjectCode(String projectCode) 
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode() 
    {
        return projectCode;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setProjectLevel(String projectLevel) 
    {
        this.projectLevel = projectLevel;
    }

    public String getProjectLevel() 
    {
        return projectLevel;
    }

    public void setProjectStatus(String projectStatus) 
    {
        this.projectStatus = projectStatus;
    }

    public String getProjectStatus() 
    {
        return projectStatus;
    }

    public void setLeaderType(String leaderType) 
    {
        this.leaderType = leaderType;
    }

    public String getLeaderType() 
    {
        return leaderType;
    }

    public void setLeaderId(Long leaderId) 
    {
        this.leaderId = leaderId;
    }

    public Long getLeaderId() 
    {
        return leaderId;
    }

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public List<ProjectOutput> getProjectOutputList()
    {
        return projectOutputList;
    }

    public void setProjectOutputList(List<ProjectOutput> projectOutputList)
    {
        this.projectOutputList = projectOutputList;
    }

    public List<ProjectMember> getProjectMemberList()
    {
        return projectMemberList;
    }

    public void setProjectMemberList(List<ProjectMember> projectMemberList)
    {
        this.projectMemberList = projectMemberList;
    }

    public List<FundingRecord> getFundingRecordList()
    {
        return fundingRecordList;
    }

    public void setFundingRecordList(List<FundingRecord> fundingRecordList)
    {
        this.fundingRecordList = fundingRecordList;
    }

    public Long getMemberUserId() 
    {
        return memberUserId;
    }

    public void setMemberUserId(Long memberUserId) 
    {
        this.memberUserId = memberUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectCode", getProjectCode())
            .append("projectName", getProjectName())
            .append("description", getDescription())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("projectLevel", getProjectLevel())
            .append("projectStatus", getProjectStatus())
            .append("leaderType", getLeaderType())
            .append("leaderId", getLeaderId())
            .append("leaderName", getLeaderName())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("projectOutputList", getProjectOutputList())
            .append("projectMemberList", getProjectMemberList())
            .append("fundingRecordList", getFundingRecordList())
            .toString();
    }
}
