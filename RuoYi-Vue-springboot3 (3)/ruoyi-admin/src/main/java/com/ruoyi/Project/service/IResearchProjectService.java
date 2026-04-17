package com.ruoyi.Project.service;

import java.util.List;
import com.ruoyi.Project.domain.ResearchProject;

/**
 * 学院承担的科研项目信息Service接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
public interface IResearchProjectService 
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
     * 批量删除学院承担的科研项目信息
     * 
     * @param projectIds 需要删除的学院承担的科研项目信息主键集合
     * @return 结果
     */
    public int deleteResearchProjectByProjectIds(Long[] projectIds);

    /**
     * 删除学院承担的科研项目信息信息
     * 
     * @param projectId 学院承担的科研项目信息主键
     * @return 结果
     */
    public int deleteResearchProjectByProjectId(Long projectId);
}
