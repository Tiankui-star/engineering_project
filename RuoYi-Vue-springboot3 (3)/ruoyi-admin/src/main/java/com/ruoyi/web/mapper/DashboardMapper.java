package com.ruoyi.web.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.ruoyi.web.domain.StatisticsView;

/**
 * 统计数据Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-14
 */
public interface DashboardMapper 
{
    /**
     * 查询全局统计指标
     * 
     * @param metricName 指标名称
     * @return 指标值
     */
    BigDecimal selectGlobalMetric(String metricName);

    /**
     * 查询院系统计指标
     * 
     * @param departmentId 院系ID
     * @param metricName 指标名称
     * @return 指标值
     */
    BigDecimal selectDepartmentMetric(Map<String, Object> params);

    /**
     * 查询所有全局统计指标
     * 
     * @return 统计指标列表
     */
    List<StatisticsView> selectAllGlobalMetrics();

    /**
     * 查询院系统计指标列表
     * 
     * @param departmentId 院系ID
     * @return 统计指标列表
     */
    List<StatisticsView> selectDepartmentMetrics(Integer departmentId);

    /**
     * 插入或更新统计指标
     * 
     * @param statisticsView 统计指标
     * @return 结果
     */
    int upsertStatistic(StatisticsView statisticsView);

    /**
     * 删除指定维度的统计数据
     * 
     * @param dimensionType 维度类型
     * @param dimensionId 维度ID
     * @return 结果
     */
    int deleteByDimension(Map<String, Object> params);

    /**
     * 查询教师职称分布
     * 
     * @return 职称分布数据
     */
    List<Map<String, Object>> selectFacultyTitleDistribution();

    /**
     * 查询学生学籍状态分布
     * 
     * @return 学籍状态分布数据
     */
    List<Map<String, Object>> selectStudentStatusDistribution();

    /**
     * 查询论文年度趋势
     * 
     * @return 年度趋势数据
     */
    List<Map<String, Object>> selectPaperYearlyTrend();

    /**
     * 查询项目资金年度趋势
     * 
     * @return 年度趋势数据
     */
    List<Map<String, Object>> selectFundingYearlyTrend();

    /**
     * 查询院系统计对比
     * 
     * @return 院系统计数据
     */
    List<Map<String, Object>> selectDepartmentComparison();

    /**
     * 查询项目级别分布
     * 
     * @return 项目级别分布数据
     */
    List<Map<String, Object>> selectProjectLevelDistribution();

    /**
     * 查询专利类型分布
     * 
     * @return 专利类型分布数据
     */
    List<Map<String, Object>> selectPatentTypeDistribution();

    /**
     * 统计表记录数
     * 
     * @param tableName 表名
     * @return 记录数
     */
    BigDecimal countTable(String tableName);

    /**
     * 统计某列的总和
     * 
     * @param tableName 表名
     * @param columnName 列名
     * @return 总和
     */
    BigDecimal sumColumn(Map<String, Object> params);
}
