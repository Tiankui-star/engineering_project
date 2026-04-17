package com.ruoyi.Patent.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专利信息对象 patent
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
public class Patent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long patentId;

    /** 专利号 */
    @Excel(name = "专利号")
    private String patentCode;

    /** 专利名称 */
    @Excel(name = "专利名称")
    private String patentName;

    /** 专利摘要 */
    @Excel(name = "专利摘要")
    private String patentAbstract;

    /** 专利类型：发明/实用新型/外观设计 */
    @Excel(name = "专利类型：发明/实用新型/外观设计")
    private String patentType;

    /** 专利级别 */
    @Excel(name = "专利级别")
    private String patentLevel;

    /** 申请日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applicationDate;

    /** 授权日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "授权日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date grantDate;

    /** 到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 专利状态 */
    @Excel(name = "专利状态")
    private String patentStatus;

    /** 第一作者类型 */
    @Excel(name = "第一作者类型")
    private String firstAuthorType;

    /** 第一作者ID（关联sys_user.user_id） */
    private Long firstAuthorId;

    /** 第一作者姓名（非数据库字段，用于显示） */
    @Excel(name = "第一作者姓名")
    private String firstAuthorName;

    /** 所属院系ID */
    private Long departmentId;

    /** 所属院系名称（非数据库字段，用于显示） */
    @Excel(name = "所属院系")
    private String departmentName;

    /** 附件链接 */
    @Excel(name = "附件链接")
    private String attachmentUrl;

    /** 专利与作者的多对多关系信息 */
    private List<PatentAuthor> patentAuthorList;

    public void setPatentId(Long patentId) 
    {
        this.patentId = patentId;
    }

    public Long getPatentId() 
    {
        return patentId;
    }

    public void setPatentCode(String patentCode) 
    {
        this.patentCode = patentCode;
    }

    public String getPatentCode() 
    {
        return patentCode;
    }

    public void setPatentName(String patentName) 
    {
        this.patentName = patentName;
    }

    public String getPatentName() 
    {
        return patentName;
    }

    public void setPatentAbstract(String patentAbstract) 
    {
        this.patentAbstract = patentAbstract;
    }

    public String getPatentAbstract() 
    {
        return patentAbstract;
    }

    public void setPatentType(String patentType) 
    {
        this.patentType = patentType;
    }

    public String getPatentType() 
    {
        return patentType;
    }

    public void setPatentLevel(String patentLevel) 
    {
        this.patentLevel = patentLevel;
    }

    public String getPatentLevel() 
    {
        return patentLevel;
    }

    public void setApplicationDate(Date applicationDate) 
    {
        this.applicationDate = applicationDate;
    }

    public Date getApplicationDate() 
    {
        return applicationDate;
    }

    public void setGrantDate(Date grantDate) 
    {
        this.grantDate = grantDate;
    }

    public Date getGrantDate() 
    {
        return grantDate;
    }

    public void setExpiryDate(Date expiryDate) 
    {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() 
    {
        return expiryDate;
    }

    public void setPatentStatus(String patentStatus) 
    {
        this.patentStatus = patentStatus;
    }

    public String getPatentStatus() 
    {
        return patentStatus;
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

    public void setAttachmentUrl(String attachmentUrl) 
    {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentUrl() 
    {
        return attachmentUrl;
    }

    public List<PatentAuthor> getPatentAuthorList()
    {
        return patentAuthorList;
    }

    public void setPatentAuthorList(List<PatentAuthor> patentAuthorList)
    {
        this.patentAuthorList = patentAuthorList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("patentId", getPatentId())
            .append("patentCode", getPatentCode())
            .append("patentName", getPatentName())
            .append("patentAbstract", getPatentAbstract())
            .append("patentType", getPatentType())
            .append("patentLevel", getPatentLevel())
            .append("applicationDate", getApplicationDate())
            .append("grantDate", getGrantDate())
            .append("expiryDate", getExpiryDate())
            .append("patentStatus", getPatentStatus())
            .append("firstAuthorType", getFirstAuthorType())
            .append("firstAuthorId", getFirstAuthorId())
            .append("firstAuthorName", getFirstAuthorName())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("attachmentUrl", getAttachmentUrl())
            .append("patentAuthorList", getPatentAuthorList())
            .toString();
    }
}
