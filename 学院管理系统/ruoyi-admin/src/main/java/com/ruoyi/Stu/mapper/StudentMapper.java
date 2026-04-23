package com.ruoyi.Stu.mapper;

import java.util.List;
import com.ruoyi.Stu.domain.Student;
import com.ruoyi.Stu.domain.StudentAward;

/**
 * 学生基本信息及成就统计Mapper接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-07
 */
public interface StudentMapper 
{
    /**
     * 查询学生基本信息及成就统计
     * 
     * @param studentId 学生基本信息及成就统计主键
     * @return 学生基本信息及成就统计
     */
    public Student selectStudentByStudentId(Long studentId);

    /**
     * 根据学号查询学生
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    public Student selectStudentByStudentNumber(String studentNumber);

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
     * 删除学生基本信息及成就统计
     * 
     * @param studentId 学生基本信息及成就统计主键
     * @return 结果
     */
    public int deleteStudentByStudentId(Long studentId);

    /**
     * 批量删除学生基本信息及成就统计
     * 
     * @param studentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentByStudentIds(Long[] studentIds);

    /**
     * 批量删除学生获得的各类奖项记录
     * 
     * @param studentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentAwardByStudentIds(Long[] studentIds);
    
    /**
     * 批量新增学生获得的各类奖项记录
     * 
     * @param studentAwardList 学生获得的各类奖项记录列表
     * @return 结果
     */
    public int batchStudentAward(List<StudentAward> studentAwardList);
    

    /**
     * 通过学生基本信息及成就统计主键删除学生获得的各类奖项记录信息
     * 
     * @param studentId 学生基本信息及成就统计ID
     * @return 结果
     */
    public int deleteStudentAwardByStudentId(Long studentId);

    /**
     * 同步更新学生学号（由用户管理模块修改用户名时调用）
     *
     * @param studentId 学生主键（= sys_user.user_id）
     * @param newStudentNumber 新学号
     * @return 影响行数
     */
    public int updateStudentNumber(@org.apache.ibatis.annotations.Param("studentId") Long studentId,
                                   @org.apache.ibatis.annotations.Param("newStudentNumber") String newStudentNumber);
}
