package com.ruoyi.Paper.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.Faculty.domain.Faculty;
import com.ruoyi.Faculty.mapper.FacultyMapper;
import com.ruoyi.Stu.domain.Student;
import com.ruoyi.Stu.mapper.StudentMapper;
import com.ruoyi.Paper.mapper.AcademicPaperMapper;
import com.ruoyi.Paper.mapper.PaperAuthorMapper;
import com.ruoyi.Paper.domain.AcademicPaper;
import com.ruoyi.Paper.domain.PaperAuthor;
import com.ruoyi.Paper.service.IAcademicPaperService;

/**
 * 学术论文信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class AcademicPaperServiceImpl implements IAcademicPaperService 
{
    @Autowired
    private AcademicPaperMapper academicPaperMapper;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PaperAuthorMapper paperAuthorMapper;

    /**
     * 查询学术论文信息
     * 
     * @param paperId 学术论文信息主键
     * @return 学术论文信息
     */
    @Override
    public AcademicPaper selectAcademicPaperByPaperId(Long paperId)
    {
        AcademicPaper paper = academicPaperMapper.selectAcademicPaperByPaperId(paperId);
        if (paper != null) {
            // 查询作者列表
            List<PaperAuthor> authorList = paperAuthorMapper.selectPaperAuthorsByPaperId(paperId);
            paper.setPaperAuthorList(authorList);
        }
        return paper;
    }

    /**
     * 查询学术论文信息列表
     * 
     * @param academicPaper 学术论文信息
     * @return 学术论文信息
     */
    @Override
    public List<AcademicPaper> selectAcademicPaperList(AcademicPaper academicPaper)
    {
        List<AcademicPaper> paperList = academicPaperMapper.selectAcademicPaperList(academicPaper);
        // 为每篇论文加载作者列表
        for (AcademicPaper paper : paperList) {
            List<PaperAuthor> authorList = paperAuthorMapper.selectPaperAuthorsByPaperId(paper.getPaperId());
            paper.setPaperAuthorList(authorList);
        }
        return paperList;
    }

    /**
     * 新增学术论文信息
     * 
     * @param academicPaper 学术论文信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAcademicPaper(AcademicPaper academicPaper)
    {
        // 根据一作类型自动设置院系ID
        if (academicPaper.getFirstAuthorId() != null) {
            if ("FACULTY".equals(academicPaper.getFirstAuthorType())) {
                // 一作类型为教师，设置院系ID为教师所属院系
                Faculty faculty = facultyMapper.selectFacultyByFacultyId(academicPaper.getFirstAuthorId());
                if (faculty != null && faculty.getDepartmentId() != null) {
                    academicPaper.setDepartmentId(faculty.getDepartmentId());
                }
            } else if ("STUDENT".equals(academicPaper.getFirstAuthorType())) {
                // 一作类型为学生，设置院系ID为学生所属院系
                Student student = studentMapper.selectStudentByStudentId(academicPaper.getFirstAuthorId());
                if (student != null && student.getDepartmentId() != null) {
                    academicPaper.setDepartmentId(student.getDepartmentId());
                }
            }
        }
        
        // 插入论文信息
        int result = academicPaperMapper.insertAcademicPaper(academicPaper);
        
        // 插入作者列表
        List<PaperAuthor> authorList = academicPaper.getPaperAuthorList();
        if (result > 0 && authorList != null && !authorList.isEmpty()) {
            // 设置论文ID
            for (PaperAuthor author : authorList) {
                author.setPaperId(academicPaper.getPaperId());
            }
            paperAuthorMapper.batchInsertPaperAuthor(authorList);
        }
        
        return result;
    }

    /**
     * 修改学术论文信息
     * 
     * @param academicPaper 学术论文信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAcademicPaper(AcademicPaper academicPaper)
    {
        // 根据一作类型自动设置院系ID
        if (academicPaper.getFirstAuthorId() != null) {
            if ("FACULTY".equals(academicPaper.getFirstAuthorType())) {
                // 一作类型为教师，设置院系ID为教师所属院系
                Faculty faculty = facultyMapper.selectFacultyByFacultyId(academicPaper.getFirstAuthorId());
                if (faculty != null && faculty.getDepartmentId() != null) {
                    academicPaper.setDepartmentId(faculty.getDepartmentId());
                }
            } else if ("STUDENT".equals(academicPaper.getFirstAuthorType())) {
                // 一作类型为学生，设置院系ID为学生所属院系
                Student student = studentMapper.selectStudentByStudentId(academicPaper.getFirstAuthorId());
                if (student != null && student.getDepartmentId() != null) {
                    academicPaper.setDepartmentId(student.getDepartmentId());
                }
            }
        }
        
        // 更新论文信息
        int result = academicPaperMapper.updateAcademicPaper(academicPaper);
        
        // 删除原有作者关系
        paperAuthorMapper.deletePaperAuthorByPaperId(academicPaper.getPaperId());
        
        // 插入新的作者列表
        List<PaperAuthor> authorList = academicPaper.getPaperAuthorList();
        if (authorList != null && !authorList.isEmpty()) {
            for (PaperAuthor author : authorList) {
                author.setPaperId(academicPaper.getPaperId());
            }
            paperAuthorMapper.batchInsertPaperAuthor(authorList);
        }
        
        return result;
    }

    /**
     * 批量删除学术论文信息
     * 
     * @param paperIds 需要删除的学术论文信息主键
     * @return 结果
     */
    @Override
    public int deleteAcademicPaperByPaperIds(Long[] paperIds)
    {
        return academicPaperMapper.deleteAcademicPaperByPaperIds(paperIds);
    }

    /**
     * 删除学术论文信息信息
     * 
     * @param paperId 学术论文信息主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAcademicPaperByPaperId(Long paperId)
    {
        // 先删除作者关系
        paperAuthorMapper.deletePaperAuthorByPaperId(paperId);
        // 再删除论文
        return academicPaperMapper.deleteAcademicPaperByPaperId(paperId);
    }
}
