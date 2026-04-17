package com.ruoyi.Paper.mapper;

import java.util.List;
import com.ruoyi.Paper.domain.PaperAuthor;

/**
 * 论文作者关系Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface PaperAuthorMapper 
{
    /**
     * 查询论文作者关系
     * 
     * @param authorshipId 论文作者关系主键
     * @return 论文作者关系
     */
    public PaperAuthor selectPaperAuthorByAuthorshipId(Long authorshipId);

    /**
     * 查询论文作者关系列表
     * 
     * @param paperAuthor 论文作者关系
     * @return 论文作者关系集合
     */
    public List<PaperAuthor> selectPaperAuthorList(PaperAuthor paperAuthor);

    /**
     * 根据论文ID查询作者列表
     * 
     * @param paperId 论文ID
     * @return 作者列表
     */
    public List<PaperAuthor> selectPaperAuthorsByPaperId(Long paperId);

    /**
     * 新增论文作者关系
     * 
     * @param paperAuthor 论文作者关系
     * @return 结果
     */
    public int insertPaperAuthor(PaperAuthor paperAuthor);

    /**
     * 批量新增论文作者关系
     * 
     * @param paperAuthorList 论文作者关系列表
     * @return 结果
     */
    public int batchInsertPaperAuthor(List<PaperAuthor> paperAuthorList);

    /**
     * 修改论文作者关系
     * 
     * @param paperAuthor 论文作者关系
     * @return 结果
     */
    public int updatePaperAuthor(PaperAuthor paperAuthor);

    /**
     * 删除论文作者关系
     * 
     * @param authorshipId 论文作者关系主键
     * @return 结果
     */
    public int deletePaperAuthorByAuthorshipId(Long authorshipId);

    /**
     * 根据论文ID删除作者关系
     * 
     * @param paperId 论文ID
     * @return 结果
     */
    public int deletePaperAuthorByPaperId(Long paperId);

    /**
     * 批量删除论文作者关系
     * 
     * @param authorshipIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaperAuthorByAuthorshipIds(Long[] authorshipIds);
}
