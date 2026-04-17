package com.ruoyi.Stu.service;

import java.util.List;
import com.ruoyi.Stu.domain.Student;

/**
 * 学生基本信息及成就统计Service接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public interface IStudentService 
{
    /**
     * 查询学生基本信息及成就统计
     * 
     * @param studentId 学生基本信息及成就统计主键
     * @return 学生基本信息及成就统计
     */
    public Student selectStudentByStudentId(Long studentId);

    /**
     * 查询学生基本信息及成就统计列表
     * 
     * @param student 学生基本信息及成就统计
     * @return 学生基本信息及成就统计集合
     */
    public List<Student> selectStudentList(Student student);

    /**
     * 新增学生基本信息及成就统计
     * 
     * @param student 学生基本信息及成就统计
     * @return 结果
     */
    public int insertStudent(Student student);

    /**
     * 修改学生基本信息及成就统计
     * 
     * @param student 学生基本信息及成就统计
     * @return 结果
     */
    public int updateStudent(Student student);

    /**
     * 批量删除学生基本信息及成就统计
     * 
     * @param studentIds 需要删除的学生基本信息及成就统计主键集合
     * @return 结果
     */
    public int deleteStudentByStudentIds(Long[] studentIds);

    /**
     * 删除学生基本信息及成就统计信息
     * 
     * @param studentId 学生基本信息及成就统计主键
     * @return 结果
     */
    public int deleteStudentByStudentId(Long studentId);
}
