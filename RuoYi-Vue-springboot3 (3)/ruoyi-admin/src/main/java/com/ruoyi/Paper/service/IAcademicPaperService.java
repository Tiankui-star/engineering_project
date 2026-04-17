package com.ruoyi.Paper.service;

import java.util.List;
import com.ruoyi.Paper.domain.AcademicPaper;

/**
 * 学术论文信息Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface IAcademicPaperService 
{
    /**
     * 查询学术论文信息
     * 
     * @param paperId 学术论文信息主键
     * @return 学术论文信息
     */
    public AcademicPaper selectAcademicPaperByPaperId(Long paperId);

    /**
     * 查询学术论文信息列表
     * 
     * @param academicPaper 学术论文信息
     * @return 学术论文信息集合
     */
    public List<AcademicPaper> selectAcademicPaperList(AcademicPaper academicPaper);

    /**
     * 新增学术论文信息
     * 
     * @param academicPaper 学术论文信息
     * @return 结果
     */
    public int insertAcademicPaper(AcademicPaper academicPaper);

    /**
     * 修改学术论文信息
     * 
     * @param academicPaper 学术论文信息
     * @return 结果
     */
    public int updateAcademicPaper(AcademicPaper academicPaper);

    /**
     * 批量删除学术论文信息
     * 
     * @param paperIds 需要删除的学术论文信息主键集合
     * @return 结果
     */
    public int deleteAcademicPaperByPaperIds(Long[] paperIds);

    /**
     * 删除学术论文信息信息
     * 
     * @param paperId 学术论文信息主键
     * @return 结果
     */
    public int deleteAcademicPaperByPaperId(Long paperId);
}
