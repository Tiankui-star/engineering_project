package com.ruoyi.Stu.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.Stu.domain.StudentAward;
import com.ruoyi.Stu.mapper.StudentMapper;
import com.ruoyi.Stu.domain.Student;
import com.ruoyi.Stu.service.IStudentService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.exception.ServiceException;

/**
 * 学生基本信息及成就统计Service业务层处理
 *
 * @author 软件工程施工队
 * @date 2026-04-07
 */
@Service
public class StudentServiceImpl implements IStudentService
{
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ISysUserService sysUserService;   // 注入若依用户服务

    /**
     * 查询学生基本信息及成就统计
     *
     * @param studentId 学生基本信息及成就统计主键
     * @return 学生基本信息及成就统计
     */
    @Override
    public Student selectStudentByStudentId(Long studentId)
    {
        return studentMapper.selectStudentByStudentId(studentId);
    }

    /**
     * 查询学生基本信息及成就统计列表
     *
     * @param student 学生基本信息及成就统计
     * @return 学生基本信息及成就统计
     */
    @Override
    public List<Student> selectStudentList(Student student)
    {
        return studentMapper.selectStudentList(student);
    }

    /**
     * 新增学生基本信息及成就统计
     *
     * @param student 学生基本信息及成就统计
     * @return 结果
     */
    @Transactional
    @Override
    public int insertStudent(Student student)
    {
        // 1. 获取或创建系统用户，得到 userId
        Long userId = getOrCreateSysUser(student);
        // 2. 设置 student 主键（student_id）
        student.setStudentId(userId);
        // 3. 插入 student 表
        int rows = studentMapper.insertStudent(student);
        // 4. 插入子表获奖记录
        insertStudentAward(student);
        return rows;
    }

    /**
     * 修改学生基本信息及成就统计
     *
     * @param student 学生基本信息及成就统计
     * @return 结果
     */
    @Transactional
    @Override
    public int updateStudent(Student student)
    {
        studentMapper.deleteStudentAwardByStudentId(student.getStudentId());
        insertStudentAward(student);
        return studentMapper.updateStudent(student);
    }

    /**
     * 批量删除学生基本信息及成就统计
     *
     * @param studentIds 需要删除的学生基本信息及成就统计主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteStudentByStudentIds(Long[] studentIds)
    {
        studentMapper.deleteStudentAwardByStudentIds(studentIds);
        int rows = studentMapper.deleteStudentByStudentIds(studentIds);
        // 同步删除对应的系统用户（学生ID = 用户ID）
        sysUserService.deleteUserByIds(studentIds);
        return rows;
    }

    /**
     * 删除学生基本信息及成就统计信息
     *
     * @param studentId 学生基本信息及成就统计主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteStudentByStudentId(Long studentId)
    {
        studentMapper.deleteStudentAwardByStudentId(studentId);
        int rows = studentMapper.deleteStudentByStudentId(studentId);
        // 同步删除对应的系统用户（学生ID = 用户ID）
        sysUserService.deleteUserById(studentId);
        return rows;
    }

    /**
     * 新增学生获得的各类奖项记录信息
     *
     * @param student 学生基本信息及成就统计对象
     */
    public void insertStudentAward(Student student)
    {
        List<StudentAward> studentAwardList = student.getStudentAwardList();
        Long studentId = student.getStudentId();
        if (StringUtils.isNotNull(studentAwardList))
        {
            List<StudentAward> list = new ArrayList<StudentAward>();
            for (StudentAward studentAward : studentAwardList)
            {
                studentAward.setStudentId(studentId);
                list.add(studentAward);
            }
            if (list.size() > 0)
            {
                studentMapper.batchStudentAward(list);
            }
        }
    }

    // ===================== 私有辅助方法 =====================

    /**
     * 根据学号查找或创建系统用户
     * @param student 学生信息（必须包含 studentNumber 和 name）
     * @return sys_user.user_id
     */
    private Long getOrCreateSysUser(Student student) {
        String username = student.getStudentNumber();  // 用学号作为登录名
        // 1. 先查询是否已存在
        SysUser existUser = sysUserService.selectUserByUserName(username);
        if (existUser != null) {
            // 用户已存在，抛出异常
            throw new ServiceException("学号已存在，请使用其他学号");
        }
    
        // 2. 检查学号是否已在学生表中存在
        Student existStudent = studentMapper.selectStudentByStudentNumber(username);
        if (existStudent != null) {
            throw new ServiceException("学号已存在，请使用其他学号");
        }
    
        // 3. 不存在则创建新用户
        SysUser newUser = new SysUser();
        newUser.setUserName(username);
        newUser.setNickName(student.getName());
        // 初始密码，建议从配置文件读取，这里写死示例
        newUser.setPassword(SecurityUtils.encryptPassword("123456"));
        newUser.setStatus("0");      // 正常状态
        // 设置默认部门（请根据你的实际部门树修改，例如 100 为根部门）
        newUser.setDeptId(100L);
        // 设置默认角色（必须，否则用户无法登录后台，请根据你的数据库实际角色ID修改）
        Long[] roleIds = {102L};       // 假设2号角色是"学生"或"普通用户"，请根据你的实际修改
        newUser.setRoleIds(roleIds);
    
        // 调用若依的插入方法
        sysUserService.insertUser(newUser);
    
        // 插入后重新查询获取 userId（因为 insertUser 方法不返回主键）
        SysUser inserted = sysUserService.selectUserByUserName(username);
        if (inserted == null) {
            throw new ServiceException("创建系统用户失败，用户名：" + username);
        }
        return inserted.getUserId();
    }
}