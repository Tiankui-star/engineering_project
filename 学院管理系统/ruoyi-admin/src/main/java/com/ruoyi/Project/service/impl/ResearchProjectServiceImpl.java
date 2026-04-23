package com.ruoyi.Project.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.Project.domain.ProjectOutput;
import com.ruoyi.Project.domain.ProjectMember;
import com.ruoyi.Project.domain.FundingRecord;
import com.ruoyi.Project.mapper.ResearchProjectMapper;
import com.ruoyi.Project.domain.ResearchProject;
import com.ruoyi.Project.service.IResearchProjectService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 学院承担的科研项目信息Service业务层处理
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
@Service
public class ResearchProjectServiceImpl implements IResearchProjectService 
{
    @Autowired
    private ResearchProjectMapper researchProjectMapper;

    /**
     * 查询学院承担的科研项目信息
     * 
     * @param projectId 学院承担的科研项目信息主键
     * @return 学院承担的科研项目信息
     */
    @Override
    public ResearchProject selectResearchProjectByProjectId(Long projectId)
    {
        return researchProjectMapper.selectResearchProjectByProjectId(projectId);
    }

    /**
     * 查询学院承担的科研项目信息列表
     * 教职工和学生角色只能查看自己参与的项目
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 学院承担的科研项目信息
     */
    @Override
    public List<ResearchProject> selectResearchProjectList(ResearchProject researchProject)
    {
        // 教职工和学生角色：只查看自己是成员的项目
        if (hasRole("college_teacher") || hasRole("college_student")) {
            researchProject.setMemberUserId(SecurityUtils.getUserId());
        }
        return researchProjectMapper.selectResearchProjectList(researchProject);
    }

    /**
     * 新增学院承担的科研项目信息
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertResearchProject(ResearchProject researchProject)
    {
        int rows = researchProjectMapper.insertResearchProject(researchProject);
        insertProjectOutput(researchProject);
        insertProjectMember(researchProject);
        insertFundingRecord(researchProject);
        return rows;
    }

    /**
     * 修改学院承担的科研项目信息
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateResearchProject(ResearchProject researchProject)
    {
        // 先更新项目基本信息
        int rows = researchProjectMapper.updateResearchProject(researchProject);
        // 删除并重新插入成果
        researchProjectMapper.deleteProjectOutputByProjectId(researchProject.getProjectId());
        insertProjectOutput(researchProject);
        // 删除并重新插入成员（注意：负责人由触发器处理，这里只处理非负责人成员）
        researchProjectMapper.deleteProjectMemberByProjectId(researchProject.getProjectId());
        insertProjectMember(researchProject);
        // 删除并重新插入资金记录
        researchProjectMapper.deleteFundingRecordByProjectId(researchProject.getProjectId());
        insertFundingRecord(researchProject);
        return rows;
    }

    /**
     * 批量删除学院承担的科研项目信息
     * 
     * @param projectIds 需要删除的学院承担的科研项目信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteResearchProjectByProjectIds(Long[] projectIds)
    {
        researchProjectMapper.deleteProjectOutputByProjectIds(projectIds);
        researchProjectMapper.deleteProjectMemberByProjectIds(projectIds);
        researchProjectMapper.deleteFundingRecordByProjectIds(projectIds);
        return researchProjectMapper.deleteResearchProjectByProjectIds(projectIds);
    }

    /**
     * 删除学院承担的科研项目信息信息
     * 
     * @param projectId 学院承担的科研项目信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteResearchProjectByProjectId(Long projectId)
    {
        researchProjectMapper.deleteProjectOutputByProjectId(projectId);
        researchProjectMapper.deleteProjectMemberByProjectId(projectId);
        researchProjectMapper.deleteFundingRecordByProjectId(projectId);
        return researchProjectMapper.deleteResearchProjectByProjectId(projectId);
    }

    /**
     * 新增项目成果关联（论文和专利）信息
     * 
     * @param researchProject 学院承担的科研项目信息对象
     */
    public void insertProjectOutput(ResearchProject researchProject)
    {
        List<ProjectOutput> projectOutputList = researchProject.getProjectOutputList();
        Long projectId = researchProject.getProjectId();
        if (StringUtils.isNotNull(projectOutputList))
        {
            List<ProjectOutput> list = new ArrayList<ProjectOutput>();
            for (ProjectOutput projectOutput : projectOutputList)
            {
                projectOutput.setProjectId(projectId);
                list.add(projectOutput);
            }
            if (list.size() > 0)
            {
                researchProjectMapper.batchProjectOutput(list);
            }
        }
    }

    /**
     * 新增项目成员信息
     * 
     * @param researchProject 学院承担的科研项目信息对象
     */
    public void insertProjectMember(ResearchProject researchProject)
    {
        List<ProjectMember> projectMemberList = researchProject.getProjectMemberList();
        Long projectId = researchProject.getProjectId();
        String leaderType = researchProject.getLeaderType();
        Long leaderId = researchProject.getLeaderId();
        if (StringUtils.isNotNull(projectMemberList))
        {
            List<ProjectMember> list = new ArrayList<ProjectMember>();
            for (ProjectMember projectMember : projectMemberList)
            {
                // 排除负责人（负责人已由触发器自动添加）
                if ("LEADER".equals(projectMember.getRoleType()) 
                    && leaderType.equals(projectMember.getMemberType()) 
                    && leaderId.equals(projectMember.getUserId())) {
                    continue;
                }
                projectMember.setProjectId(projectId);
                list.add(projectMember);
            }
            if (list.size() > 0)
            {
                researchProjectMapper.batchProjectMember(list);
            }
        }
    }

    /**
     * 新增项目资金记录信息
     * 
     * @param researchProject 学院承担的科研项目信息对象
     */
    public void insertFundingRecord(ResearchProject researchProject)
    {
        List<FundingRecord> fundingRecordList = researchProject.getFundingRecordList();
        Long projectId = researchProject.getProjectId();
        if (StringUtils.isNotNull(fundingRecordList))
        {
            List<FundingRecord> list = new ArrayList<FundingRecord>();
            for (FundingRecord fundingRecord : fundingRecordList)
            {
                fundingRecord.setProjectId(projectId);
                list.add(fundingRecord);
            }
            if (list.size() > 0)
            {
                researchProjectMapper.batchFundingRecord(list);
            }
        }
    }

    // ===================== 私有辅助方法 =====================

    /**
     * 判断当前登录用户是否拥有指定角色
     *
     * @param roleKey 角色标识（如 college_teacher）
     * @return true 表示拥有该角色
     */
    private boolean hasRole(String roleKey) {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            return loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> roleKey.equals(r.getRoleKey()));
        } catch (Exception e) {
            return false;
        }
    }
}
