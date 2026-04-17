package com.ruoyi.Faculty.controller;

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
import com.ruoyi.Faculty.domain.Faculty;
import com.ruoyi.Faculty.service.IFacultyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教职工信息及科研统计Controller
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/Faculty/faculty")
public class FacultyController extends BaseController
{
    @Autowired
    private IFacultyService facultyService;

    /**
     * 查询教职工信息及科研统计列表
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:list')")
    @GetMapping("/list")
    public TableDataInfo list(Faculty faculty)
    {
        startPage();
        List<Faculty> list = facultyService.selectFacultyList(faculty);
        return getDataTable(list);
    }

    /**
     * 导出教职工信息及科研统计列表
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:export')")
    @Log(title = "教职工信息及科研统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Faculty faculty)
    {
        List<Faculty> list = facultyService.selectFacultyList(faculty);
        ExcelUtil<Faculty> util = new ExcelUtil<Faculty>(Faculty.class);
        util.exportExcel(response, list, "教职工信息及科研统计数据");
    }

    /**
     * 获取教职工信息及科研统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:query')")
    @GetMapping(value = "/{facultyId}")
    public AjaxResult getInfo(@PathVariable("facultyId") Long facultyId)
    {
        return success(facultyService.selectFacultyByFacultyId(facultyId));
    }

    /**
     * 新增教职工信息及科研统计
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:add')")
    @Log(title = "教职工信息及科研统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Faculty faculty)
    {
        return toAjax(facultyService.insertFaculty(faculty));
    }

    /**
     * 修改教职工信息及科研统计
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:edit')")
    @Log(title = "教职工信息及科研统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Faculty faculty)
    {
        return toAjax(facultyService.updateFaculty(faculty));
    }

    /**
     * 删除教职工信息及科研统计
     */
    @PreAuthorize("@ss.hasPermi('Faculty:faculty:remove')")
    @Log(title = "教职工信息及科研统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{facultyIds}")
    public AjaxResult remove(@PathVariable Long[] facultyIds)
    {
        return toAjax(facultyService.deleteFacultyByFacultyIds(facultyIds));
    }
}
