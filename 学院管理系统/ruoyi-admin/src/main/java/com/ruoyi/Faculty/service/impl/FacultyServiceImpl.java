package com.ruoyi.Faculty.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.Faculty.mapper.FacultyMapper;
import com.ruoyi.Faculty.domain.Faculty;
import com.ruoyi.Faculty.service.IFacultyService;
import com.ruoyi.Dep.service.IDepartmentService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.exception.ServiceException;

/**
 * 教职工信息及科研统计Service业务层处理
 *
 * @author 软件工程施工队
 * @date 2026-04-07
 */
@Service
public class FacultyServiceImpl implements IFacultyService
{
    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private ISysUserService sysUserService;   // 注入若依用户服务

    @Autowired
    private IDepartmentService departmentService;  // 注入院系服务

    /**
     * 查询教职工信息及科研统计
     *
     * @param facultyId 教职工信息及科研统计主键
     * @return 教职工信息及科研统计
     */
    @Override
    public Faculty selectFacultyByFacultyId(Long facultyId)
    {
        return facultyMapper.selectFacultyByFacultyId(facultyId);
    }

    /**
     * 查询教职工信息及科研统计列表
     * 教职工角色只能查看自己的信息
     *
     * @param faculty 教职工信息及科研统计
     * @return 教职工信息及科研统计
     */
    @Override
    public List<Faculty> selectFacultyList(Faculty faculty)
    {
        // 教职工角色：只能查看自己的记录
        if (hasRole("college_teacher")) {
            faculty.setFacultyId(SecurityUtils.getUserId());
        }
        return facultyMapper.selectFacultyList(faculty);
    }

    /**
     * 新增教职工信息及科研统计
     *
     * @param faculty 教职工信息及科研统计
     * @return 结果
     */
    @Override
    @Transactional
    public int insertFaculty(Faculty faculty)
    {
        // 1. 获取或创建系统用户，得到 userId
        Long userId = getOrCreateSysUser(faculty);
        // 2. 设置 faculty 主键（faculty_id）
        faculty.setFacultyId(userId);
        // 3. 插入 faculty 表
        return facultyMapper.insertFaculty(faculty);
    }

    /**
     * 修改教职工信息及科研统计
     * 若工号或姓名发生变更，同步更新 sys_user 中的 user_name / nick_name
     *
     * @param faculty 教职工信息及科研统计
     * @return 结果
     */
    @Override
    @Transactional
    public int updateFaculty(Faculty faculty)
    {
        // 读取修改前的数据，判断工号和姓名是否变更
        Faculty existing = facultyMapper.selectFacultyByFacultyId(faculty.getFacultyId());
        if (existing != null) {
            boolean numberChanged = faculty.getFacultyNumber() != null
                    && !faculty.getFacultyNumber().equals(existing.getFacultyNumber());
            boolean nameChanged = faculty.getName() != null
                    && !faculty.getName().equals(existing.getName());

            if (numberChanged || nameChanged) {
                if (numberChanged) {
                    // 校验新工号唯一性
                    SysUser conflict = sysUserService.selectUserByUserName(faculty.getFacultyNumber());
                    if (conflict != null && !conflict.getUserId().equals(faculty.getFacultyId())) {
                        throw new ServiceException("工号 " + faculty.getFacultyNumber() + " 已被其他账号占用");
                    }
                }
                // 同步更新 sys_user
                SysUser sysUser = new SysUser();
                sysUser.setUserId(faculty.getFacultyId());
                if (numberChanged) sysUser.setUserName(faculty.getFacultyNumber());
                if (nameChanged)   sysUser.setNickName(faculty.getName());
                sysUserService.updateUserProfile(sysUser);
            }
        }
        return facultyMapper.updateFaculty(faculty);
    }

    /**
     * 批量删除教职工信息及科研统计
     *
     * @param facultyIds 需要删除的教职工信息及科研统计主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteFacultyByFacultyIds(Long[] facultyIds)
    {
        // 先清空这些教师担任的系主任职务
        for (Long facultyId : facultyIds) {
            departmentService.clearHeadFacultyByFacultyId(facultyId);
        }
        
        int rows = facultyMapper.deleteFacultyByFacultyIds(facultyIds);
        // 同步删除对应的系统用户（教职工ID = 用户ID）
        sysUserService.deleteUserByIds(facultyIds);
        return rows;
    }

    /**
     * 删除教职工信息及科研统计信息
     *
     * @param facultyId 教职工信息及科研统计主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteFacultyByFacultyId(Long facultyId)
    {
        // 先清空该教师担任的系主任职务
        departmentService.clearHeadFacultyByFacultyId(facultyId);
        
        int rows = facultyMapper.deleteFacultyByFacultyId(facultyId);
        // 同步删除对应的系统用户（教职工ID = 用户ID）
        sysUserService.deleteUserById(facultyId);
        return rows;
    }

    // ===================== 公共辅助方法 =====================

    @Override
    public void syncFacultyNumber(Long facultyId, String newFacultyNumber) {
        Faculty existing = facultyMapper.selectFacultyByFacultyId(facultyId);
        if (existing != null) {
            facultyMapper.updateFacultyNumber(facultyId, newFacultyNumber);
        }
    }

    // ===================== 私有辅助方法 =====================

    /**
     * 根据工号查找或创建系统用户
     * @param faculty 教师信息
     * @return sys_user.user_id
     */
    private Long getOrCreateSysUser(Faculty faculty) {
        String username = faculty.getFacultyNumber();  // 用工号作为登录名
        // 1. 先查询是否已存在
        SysUser existUser = sysUserService.selectUserByUserName(username);
        if (existUser != null) {
            // 用户已存在，抛出异常
            throw new ServiceException("工号已存在，请使用其他工号");
        }

        // 2. 检查工号是否已在教师表中存在
        Faculty existFaculty = facultyMapper.selectFacultyByFacultyNumber(username);
        if (existFaculty != null) {
            throw new ServiceException("工号已存在，请使用其他工号");
        }

        // 3. 不存在则创建新用户
        SysUser newUser = new SysUser();
        newUser.setUserName(username);
        newUser.setNickName(faculty.getName());
        newUser.setPassword(SecurityUtils.encryptPassword("123456"));
        newUser.setStatus("0");
        // 请根据实际修改部门ID和角色ID
        newUser.setDeptId(100L);        // 默认部门ID，需改为你系统中的有效部门ID
        newUser.setRoleIds(new Long[]{5L}); // 默认角色ID，需改为你系统中的有效角色ID（如教师角色）

        sysUserService.insertUser(newUser);

        // 插入后重新查询获取 userId
        SysUser inserted = sysUserService.selectUserByUserName(username);
        if (inserted == null) {
            throw new ServiceException("创建系统用户失败，用户名：" + username);
        }
        return inserted.getUserId();
    }

    // ===================== 私有辅助方法 =====================

    /**
     * 判断当前登录用户是否拥有指定角色
     *
     * @param roleKey 角色标识（如 college_teacher）
     * @return true 表示拥有该角色
     */
    private boolean hasRole(String roleKey) {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            return loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> roleKey.equals(r.getRoleKey()));
        } catch (Exception e) {
            return false;
        }
    }
}