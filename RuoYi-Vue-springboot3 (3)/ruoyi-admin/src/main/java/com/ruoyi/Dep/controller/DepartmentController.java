package com.ruoyi.Dep.controller;

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
import com.ruoyi.Dep.domain.Department;
import com.ruoyi.Dep.service.IDepartmentService;
import com.ruoyi.Faculty.domain.Faculty;
import com.ruoyi.Faculty.service.IFacultyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 院系信息管理Controller
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@RestController
@RequestMapping("/Dep/department")
public class DepartmentController extends BaseController
{
    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IFacultyService facultyService;

    /**
     * 查询院系信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:list')")
    @GetMapping("/list")
    public TableDataInfo list(Department department)
    {
        startPage();
        List<Department> list = departmentService.selectDepartmentList(department);
        return getDataTable(list);
    }

    /**
     * 导出院系信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:export')")
    @Log(title = "院系信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Department department)
    {
        List<Department> list = departmentService.selectDepartmentList(department);
        ExcelUtil<Department> util = new ExcelUtil<Department>(Department.class);
        util.exportExcel(response, list, "院系信息管理数据");
    }

    /**
     * 获取院系信息管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:query')")
    @GetMapping(value = "/{departmentId}")
    public AjaxResult getInfo(@PathVariable("departmentId") Long departmentId)
    {
        return success(departmentService.selectDepartmentByDepartmentId(departmentId));
    }

    /**
     * 新增院系信息管理
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:add')")
    @Log(title = "院系信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Department department)
    {
        return toAjax(departmentService.insertDepartment(department));
    }

    /**
     * 修改院系信息管理
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:edit')")
    @Log(title = "院系信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Department department)
    {
        return toAjax(departmentService.updateDepartment(department));
    }

    /**
     * 删除院系信息管理
     */
    @PreAuthorize("@ss.hasPermi('Dep:department:remove')")
    @Log(title = "院系信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{departmentIds}")
    public AjaxResult remove(@PathVariable Long[] departmentIds)
    {
        return toAjax(departmentService.deleteDepartmentByDepartmentIds(departmentIds));
    }

    /**
     * 模糊查询教师列表（用于系主任选择）
     */
    @GetMapping("/searchFaculty")
    public TableDataInfo searchFaculty(String name)
    {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        List<Faculty> list = facultyService.selectFacultyList(faculty);
        return getDataTable(list);
    }
}
