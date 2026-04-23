package com.ruoyi.Dep.service;

import java.util.List;
import com.ruoyi.Dep.domain.Department;

/**
 * 院系信息管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface IDepartmentService 
{
    /**
     * 查询院系信息管理
     * 
     * @param departmentId 院系信息管理主键
     * @return 院系信息管理
     */
    public Department selectDepartmentByDepartmentId(Long departmentId);

    /**
     * 查询院系信息管理列表
     * 
     * @param department 院系信息管理
     * @return 院系信息管理集合
     */
    public List<Department> selectDepartmentList(Department department);

    /**
     * 新增院系信息管理
     * 
     * @param department 院系信息管理
     * @return 结果
     */
    public int insertDepartment(Department department);

    /**
     * 修改院系信息管理
     * 
     * @param department 院系信息管理
     * @return 结果
     */
    public int updateDepartment(Department department);

    /**
     * 批量删除院系信息管理
     * 
     * @param departmentIds 需要删除的院系信息管理主键集合
     * @return 结果
     */
    public int deleteDepartmentByDepartmentIds(Long[] departmentIds);

    /**
     * 删除院系信息管理信息
     * 
     * @param departmentId 院系信息管理主键
     * @return 结果
     */
    public int deleteDepartmentByDepartmentId(Long departmentId);

    /**
     * 根据教师ID清空系主任（教师删除时调用）
     *
     * @param facultyId 教师ID
     * @return 结果
     */
    public int clearHeadFacultyByFacultyId(Long facultyId);

    /**
     * 校验院系代码是否唯一
     *
     * @param department 院系信息
     * @return 结果（true=唯一，false=重复）
     */
    public boolean checkDepartmentCodeUnique(Department department);

    /**
     * 校验院系名称是否唯一
     *
     * @param department 院系信息
     * @return 结果（true=唯一，false=重复）
     */
    public boolean checkDepartmentNameUnique(Department department);

    /**
     * 校验系主任是否已担任其他院系系主任
     *
     * @param department 院系信息
     * @return 结果（true=可用，false=已被占用）
     */
    public boolean checkHeadFacultyIdUnique(Department department);
}
