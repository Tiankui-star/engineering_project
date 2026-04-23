package com.ruoyi.Faculty.service;

import java.util.List;
import com.ruoyi.Faculty.domain.Faculty;

/**
 * 教职工信息及科研统计Service接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public interface IFacultyService 
{
    /**
     * 查询教职工信息及科研统计
     * 
     * @param facultyId 教职工信息及科研统计主键
     * @return 教职工信息及科研统计
     */
    public Faculty selectFacultyByFacultyId(Long facultyId);

    /**
     * 查询教职工信息及科研统计列表
     * 
     * @param faculty 教职工信息及科研统计
     * @return 教职工信息及科研统计集合
     */
    public List<Faculty> selectFacultyList(Faculty faculty);

    /**
     * 新增教职工信息及科研统计
     * 
     * @param faculty 教职工信息及科研统计
     * @return 结果
     */
    public int insertFaculty(Faculty faculty);

    /**
     * 修改教职工信息及科研统计
     * 
     * @param faculty 教职工信息及科研统计
     * @return 结果
     */
    public int updateFaculty(Faculty faculty);

    /**
     * 批量删除教职工信息及科研统计
     * 
     * @param facultyIds 需要删除的教职工信息及科研统计主键集合
     * @return 结果
     */
    public int deleteFacultyByFacultyIds(Long[] facultyIds);

    /**
     * 删除教职工信息及科研统计信息
     * 
     * @param facultyId 教职工信息及科研统计主键
     * @return 结果
     */
    public int deleteFacultyByFacultyId(Long facultyId);

    /**
     * 同步更新工号（由用户管理模块修改用户名时调用）
     * 若该 userId 对应的教师不存在，则不做任何操作。
     *
     * @param facultyId 教师主键（= sys_user.user_id）
     * @param newFacultyNumber 新工号
     */
    public void syncFacultyNumber(Long facultyId, String newFacultyNumber);
}
