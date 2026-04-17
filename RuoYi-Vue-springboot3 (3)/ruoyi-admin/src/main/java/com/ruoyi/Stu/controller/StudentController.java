package com.ruoyi.Stu.controller;

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
import com.ruoyi.Stu.domain.Student;
import com.ruoyi.Stu.service.IStudentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学生基本信息及成就统计Controller
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/Stu/student")
public class StudentController extends BaseController
{
    @Autowired
    private IStudentService studentService;

    /**
     * 查询学生基本信息及成就统计列表
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(Student student)
    {
        startPage();
        List<Student> list = studentService.selectStudentList(student);
        return getDataTable(list);
    }

    /**
     * 导出学生基本信息及成就统计列表
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:export')")
    @Log(title = "学生基本信息及成就统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Student student)
    {
        List<Student> list = studentService.selectStudentList(student);
        ExcelUtil<Student> util = new ExcelUtil<Student>(Student.class);
        util.exportExcel(response, list, "学生基本信息及成就统计数据");
    }

    /**
     * 获取学生基本信息及成就统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:query')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable("studentId") Long studentId)
    {
        return success(studentService.selectStudentByStudentId(studentId));
    }

    /**
     * 新增学生基本信息及成就统计
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:add')")
    @Log(title = "学生基本信息及成就统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Student student)
    {
        return toAjax(studentService.insertStudent(student));
    }

    /**
     * 修改学生基本信息及成就统计
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:edit')")
    @Log(title = "学生基本信息及成就统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Student student)
    {
        return toAjax(studentService.updateStudent(student));
    }

    /**
     * 删除学生基本信息及成就统计
     */
    @PreAuthorize("@ss.hasPermi('Stu:student:remove')")
    @Log(title = "学生基本信息及成就统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable Long[] studentIds)
    {
        return toAjax(studentService.deleteStudentByStudentIds(studentIds));
    }
}
