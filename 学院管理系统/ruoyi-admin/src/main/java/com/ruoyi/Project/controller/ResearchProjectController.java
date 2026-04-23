package com.ruoyi.Project.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.Project.domain.ResearchProject;
import com.ruoyi.Project.domain.ProjectMember;
import com.ruoyi.Project.service.IResearchProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学院承担的科研项目信息Controller
 * 
 * @author 软件工程施工队
 * @date 2026-04-09
 */
@RestController
@RequestMapping("/Project/project")
public class ResearchProjectController extends BaseController
{
    @Autowired
    private IResearchProjectService researchProjectService;

    /**
     * 查询学院承担的科研项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('Project:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchProject researchProject)
    {
        startPage();
        List<ResearchProject> list = researchProjectService.selectResearchProjectList(researchProject);
        return getDataTable(list);
    }

    /**
     * 导出学院承担的科研项目信息列表
     */
    @PreAuthorize("@ss.hasPermi('Project:project:export')")
    @Log(title = "学院承担的科研项目信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchProject researchProject)
    {
        List<ResearchProject> list = researchProjectService.selectResearchProjectList(researchProject);
        ExcelUtil<ResearchProject> util = new ExcelUtil<ResearchProject>(ResearchProject.class);
        util.exportExcel(response, list, "学院承担的科研项目信息数据");
    }

    /**
     * 获取学院承担的科研项目信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('Project:project:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId)
    {
        return success(researchProjectService.selectResearchProjectByProjectId(projectId));
    }

    /**
     * 新增学院承担的科研项目信息
     */
    @PreAuthorize("@ss.hasPermi('Project:project:add')")
    @Log(title = "学院承担的科研项目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchProject researchProject)
    {
        if (researchProject.getStartDate() != null && researchProject.getEndDate() != null
                && researchProject.getStartDate().after(researchProject.getEndDate()))
        {
            return error("新增项目失败，项目开始日期不能晚于结束日期");
        }
        if (researchProject.getProjectMemberList() != null)
        {
            for (ProjectMember member : researchProject.getProjectMemberList())
            {
                if (member.getJoinDate() != null && member.getLeaveDate() != null
                        && member.getJoinDate().after(member.getLeaveDate()))
                {
                    return error("新增项目失败，项目成员『加入日期』不能晚于『离开日期』");
                }
            }
        }
        return toAjax(researchProjectService.insertResearchProject(researchProject));
    }

    /**
     * 修改学院承担的科研项目信息
     */
    @PreAuthorize("@ss.hasPermi('Project:project:edit')")
    @Log(title = "学院承担的科研项目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchProject researchProject)
    {
        if (researchProject.getStartDate() != null && researchProject.getEndDate() != null
                && researchProject.getStartDate().after(researchProject.getEndDate()))
        {
            return error("修改项目失败，项目开始日期不能晚于结束日期");
        }
        if (researchProject.getProjectMemberList() != null)
        {
            for (ProjectMember member : researchProject.getProjectMemberList())
            {
                if (member.getJoinDate() != null && member.getLeaveDate() != null
                        && member.getJoinDate().after(member.getLeaveDate()))
                {
                    return error("修改项目失败，项目成员『加入日期』不能晚于『离开日期』");
                }
            }
        }
        return toAjax(researchProjectService.updateResearchProject(researchProject));
    }

    /**
     * 删除学院承担的科研项目信息
     */
    @PreAuthorize("@ss.hasPermi('Project:project:remove')")
    @Log(title = "学院承担的科研项目信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds)
    {
        return toAjax(researchProjectService.deleteResearchProjectByProjectIds(projectIds));
    }
}
