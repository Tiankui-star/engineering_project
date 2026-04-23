package com.ruoyi.Patent.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.Patent.domain.PatentAuthor;
import com.ruoyi.Patent.mapper.PatentMapper;
import com.ruoyi.Patent.domain.Patent;
import com.ruoyi.Patent.service.IPatentService;
import com.ruoyi.Faculty.mapper.FacultyMapper;
import com.ruoyi.Faculty.domain.Faculty;
import com.ruoyi.Stu.mapper.StudentMapper;
import com.ruoyi.Stu.domain.Student;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 专利信息Service业务层处理
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
@Service
public class PatentServiceImpl implements IPatentService 
{
    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 查询专利信息
     * 
     * @param patentId 专利信息主键
     * @return 专利信息
     */
    @Override
    public Patent selectPatentByPatentId(Long patentId)
    {
        return patentMapper.selectPatentByPatentId(patentId);
    }

    /**
     * 查询专利信息列表
     * 教职工角色只能查看自己参与的专利
     * 
     * @param patent 专利信息
     * @return 专利信息
     */
    @Override
    public List<Patent> selectPatentList(Patent patent)
    {
        // 教职工角色：只查看自己是作者的专利
        if (hasRole("college_teacher") || hasRole("college_student")) {
            patent.setAuthorUserId(SecurityUtils.getUserId());
        }
        return patentMapper.selectPatentList(patent);
    }

    /**
     * 新增专利信息
     * 
     * @param patent 专利信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertPatent(Patent patent)
    {
        // 根据第一作者类型自动设置院系ID
        if (patent.getFirstAuthorId() != null) {
            if ("FACULTY".equals(patent.getFirstAuthorType())) {
                // 第一作者类型为教师，设置院系ID为教师所属院系
                Faculty faculty = facultyMapper.selectFacultyByFacultyId(patent.getFirstAuthorId());
                if (faculty != null && faculty.getDepartmentId() != null) {
                    patent.setDepartmentId(faculty.getDepartmentId());
                }
            } else if ("STUDENT".equals(patent.getFirstAuthorType())) {
                // 第一作者类型为学生，设置院系ID为学生所属院系
                Student student = studentMapper.selectStudentByStudentId(patent.getFirstAuthorId());
                if (student != null && student.getDepartmentId() != null) {
                    patent.setDepartmentId(student.getDepartmentId());
                }
            }
        }
        
        int rows = patentMapper.insertPatent(patent);
        insertPatentAuthor(patent);
        return rows;
    }

    /**
     * 修改专利信息
     * 
     * @param patent 专利信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePatent(Patent patent)
    {
        // 根据第一作者类型自动设置院系ID
        if (patent.getFirstAuthorId() != null) {
            if ("FACULTY".equals(patent.getFirstAuthorType())) {
                // 第一作者类型为教师，设置院系ID为教师所属院系
                Faculty faculty = facultyMapper.selectFacultyByFacultyId(patent.getFirstAuthorId());
                if (faculty != null && faculty.getDepartmentId() != null) {
                    patent.setDepartmentId(faculty.getDepartmentId());
                }
            } else if ("STUDENT".equals(patent.getFirstAuthorType())) {
                // 第一作者类型为学生，设置院系ID为学生所属院系
                Student student = studentMapper.selectStudentByStudentId(patent.getFirstAuthorId());
                if (student != null && student.getDepartmentId() != null) {
                    patent.setDepartmentId(student.getDepartmentId());
                }
            }
        }
        
        // 先更新专利基本信息
        int rows = patentMapper.updatePatent(patent);
        // 删除并重新插入作者列表
        patentMapper.deletePatentAuthorByPatentId(patent.getPatentId());
        insertPatentAuthor(patent);
        return rows;
    }

    /**
     * 批量删除专利信息
     * 
     * @param patentIds 需要删除的专利信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePatentByPatentIds(Long[] patentIds)
    {
        patentMapper.deletePatentAuthorByPatentIds(patentIds);
        return patentMapper.deletePatentByPatentIds(patentIds);
    }

    /**
     * 删除专利信息信息
     * 
     * @param patentId 专利信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePatentByPatentId(Long patentId)
    {
        patentMapper.deletePatentAuthorByPatentId(patentId);
        return patentMapper.deletePatentByPatentId(patentId);
    }

    /**
     * 新增专利与作者的多对多关系信息
     * 
     * @param patent 专利信息对象
     */
    public void insertPatentAuthor(Patent patent)
    {
        List<PatentAuthor> patentAuthorList = patent.getPatentAuthorList();
        Long patentId = patent.getPatentId();
        if (StringUtils.isNotNull(patentAuthorList))
        {
            List<PatentAuthor> list = new ArrayList<PatentAuthor>();
            for (PatentAuthor patentAuthor : patentAuthorList)
            {
                patentAuthor.setPatentId(patentId);
                list.add(patentAuthor);
            }
            if (list.size() > 0)
            {
                patentMapper.batchPatentAuthor(list);
            }
        }
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
