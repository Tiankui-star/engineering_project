package com.ruoyi.Project.mapper;

import java.util.List;
import com.ruoyi.Project.domain.ResearchProject;
import com.ruoyi.Project.domain.ProjectOutput;
import com.ruoyi.Project.domain.ProjectMember;
import com.ruoyi.Project.domain.FundingRecord;

/**
 * 学院承担的科研项目信息Mapper接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public interface ResearchProjectMapper 
{
    /**
     * 查询学院承担的科研项目信息
     * 
     * @param projectId 学院承担的科研项目信息主键
     * @return 学院承担的科研项目信息
     */
    public ResearchProject selectResearchProjectByProjectId(Long projectId);

    /**
     * 查询学院承担的科研项目信息列表
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 学院承担的科研项目信息集合
     */
    public List<ResearchProject> selectResearchProjectList(ResearchProject researchProject);

    /**
     * 新增学院承担的科研项目信息
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 结果
     */
    public int insertResearchProject(ResearchProject researchProject);

    /**
     * 修改学院承担的科研项目信息
     * 
     * @param researchProject 学院承担的科研项目信息
     * @return 结果
     */
    public int updateResearchProject(ResearchProject researchProject);

    /**
     * 删除学院承担的科研项目信息
     * 
     * @param projectId 学院承担的科研项目信息主键
     * @return 结果
     */
    public int deleteResearchProjectByProjectId(Long projectId);

    /**
     * 批量删除学院承担的科研项目信息
     * 
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchProjectByProjectIds(Long[] projectIds);

    /**
     * 批量删除项目成果关联（论文和专利）
     * 
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectOutputByProjectIds(Long[] projectIds);
    
    /**
     * 批量新增项目成果关联（论文和专利）
     * 
     * @param projectOutputList 项目成果关联（论文和专利）列表
     * @return 结果
     */
    public int batchProjectOutput(List<ProjectOutput> projectOutputList);
    

    /**
     * 通过学院承担的科研项目信息主键删除项目成果关联（论文和专利）信息
     * 
     * @param projectId 学院承担的科研项目信息ID
     * @return 结果
     */
    public int deleteProjectOutputByProjectId(Long projectId);

    /**
     * 批量新增项目成员
     * 
     * @param projectMemberList 项目成员列表
     * @return 结果
     */
    public int batchProjectMember(List<ProjectMember> projectMemberList);

    /**
     * 通过学院承担的科研项目信息主键删除项目成员信息
     * 
     * @param projectId 学院承担的科研项目信息ID
     * @return 结果
     */
    public int deleteProjectMemberByProjectId(Long projectId);

    /**
     * 批量删除项目成员
     * 
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectMemberByProjectIds(Long[] projectIds);

    /**
     * 批量新增项目资金记录
     * 
     * @param fundingRecordList 项目资金记录列表
     * @return 结果
     */
    public int batchFundingRecord(List<FundingRecord> fundingRecordList);

    /**
     * 通过学院承担的科研项目信息主键删除项目资金记录信息
     * 
     * @param projectId 学院承担的科研项目信息ID
     * @return 结果
     */
    public int deleteFundingRecordByProjectId(Long projectId);

    /**
     * 批量删除项目资金记录
     * 
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFundingRecordByProjectIds(Long[] projectIds);
}
