package com.ruoyi.Dep.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.Dep.mapper.DepartmentMapper;
import com.ruoyi.Dep.domain.Department;
import com.ruoyi.Dep.service.IDepartmentService;

/**
 * 院系信息管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService 
{
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 查询院系信息管理
     * 
     * @param departmentId 院系信息管理主键
     * @return 院系信息管理
     */
    @Override
    public Department selectDepartmentByDepartmentId(Long departmentId)
    {
        return departmentMapper.selectDepartmentByDepartmentId(departmentId);
    }

    /**
     * 查询院系信息管理列表
     * 
     * @param department 院系信息管理
     * @return 院系信息管理
     */
    @Override
    public List<Department> selectDepartmentList(Department department)
    {
        return departmentMapper.selectDepartmentList(department);
    }

    /**
     * 新增院系信息管理
     * 
     * @param department 院系信息管理
     * @return 结果
     */
    @Override
    public int insertDepartment(Department department)
    {
        return departmentMapper.insertDepartment(department);
    }

    /**
     * 修改院系信息管理
     * 
     * @param department 院系信息管理
     * @return 结果
     */
    @Override
    public int updateDepartment(Department department)
    {
        return departmentMapper.updateDepartment(department);
    }

    /**
     * 批量删除院系信息管理
     * 
     * @param departmentIds 需要删除的院系信息管理主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentByDepartmentIds(Long[] departmentIds)
    {
        return departmentMapper.deleteDepartmentByDepartmentIds(departmentIds);
    }

    /**
     * 删除院系信息管理信息
     * 
     * @param departmentId 院系信息管理主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentByDepartmentId(Long departmentId)
    {
        return departmentMapper.deleteDepartmentByDepartmentId(departmentId);
    }

    /**
     * 根据教师ID清空系主任（教师删除时调用）
     * 
     * @param facultyId 教师ID
     * @return 结果
     */
    @Override
    public int clearHeadFacultyByFacultyId(Long facultyId)
    {
        return departmentMapper.clearHeadFacultyByFacultyId(facultyId);
    }
}
