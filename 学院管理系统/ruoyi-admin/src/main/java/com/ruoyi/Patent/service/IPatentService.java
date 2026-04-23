package com.ruoyi.Patent.service;

import java.util.List;
import com.ruoyi.Patent.domain.Patent;

/**
 * 专利信息Service接口
 * 
 * @author 软件工程施工队
 * @date 2026-04-14
 */
public interface IPatentService 
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
     * 批量删除专利信息
     * 
     * @param patentIds 需要删除的专利信息主键集合
     * @return 结果
     */
    public int deletePatentByPatentIds(Long[] patentIds);

    /**
     * 删除专利信息信息
     * 
     * @param patentId 专利信息主键
     * @return 结果
     */
    public int deletePatentByPatentId(Long patentId);
}
