package com.ruoyi.Patent.mapper;

import java.util.List;
import com.ruoyi.Patent.domain.Patent;
import com.ruoyi.Patent.domain.PatentAuthor;

/**
 * 专利信息Mapper接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
public interface PatentMapper 
{
    /**
     * 查询专利信息
     * 
     * @param patentId 专利信息主键
     * @return 专利信息
     */
    public Patent selectPatentByPatentId(Long patentId);

    /**
     * 查询专利信息列表
     * 
     * @param patent 专利信息
     * @return 专利信息集合
     */
    public List<Patent> selectPatentList(Patent patent);

    /**
     * 新增专利信息
     * 
     * @param patent 专利信息
     * @return 结果
     */
    public int insertPatent(Patent patent);

    /**
     * 修改专利信息
     * 
     * @param patent 专利信息
     * @return 结果
     */
    public int updatePatent(Patent patent);

    /**
     * 删除专利信息
     * 
     * @param patentId 专利信息主键
     * @return 结果
     */
    public int deletePatentByPatentId(Long patentId);

    /**
     * 批量删除专利信息
     * 
     * @param patentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatentByPatentIds(Long[] patentIds);

    /**
     * 批量删除专利与作者的多对多关系
     * 
     * @param patentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatentAuthorByPatentIds(Long[] patentIds);
    
    /**
     * 批量新增专利与作者的多对多关系
     * 
     * @param patentAuthorList 专利与作者的多对多关系列表
     * @return 结果
     */
    public int batchPatentAuthor(List<PatentAuthor> patentAuthorList);
    

    /**
     * 通过专利信息主键删除专利与作者的多对多关系信息
     * 
     * @param patentId 专利信息ID
     * @return 结果
     */
    public int deletePatentAuthorByPatentId(Long patentId);
}
