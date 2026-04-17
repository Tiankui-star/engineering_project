package com.ruoyi.web.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.web.domain.StatisticsView;
import com.ruoyi.web.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计数据定时刷新任务
 * 
 * @author ruoyi
 * @date 2026-04-14
 */
@Component("statisticsTask")
public class StatisticsRefreshTask
{
    private static final Logger log = LoggerFactory.getLogger(StatisticsRefreshTask.class);

    @Autowired
    private DashboardMapper dashboardMapper;

    /**
     * 刷新全局统计数据
     * 定时任务配置建议：0 0 2 * * ? （每天凌晨2点执行）
     */
    public void refreshGlobalStatistics()
    {
        log.info("开始刷新全局统计数据...");
        
        try
        {
            // 删除旧的全局统计数据
            Map<String, Object> params = new HashMap<>();
            params.put("dimensionType", "GLOBAL");
            dashboardMapper.deleteByDimension(params);
            
            // 统计教师总数
            insertGlobalStat("total_faculty", getCount("FACULTY"));
            
            // 统计学生总数
            insertGlobalStat("total_student", getCount("STUDENT"));
            
            // 统计论文总数
            insertGlobalStat("total_paper", getCount("ACADEMIC_PAPER"));
            
            // 统计专利总数
            insertGlobalStat("total_patent", getCount("PATENT"));
            
            // 统计项目总数
            insertGlobalStat("total_project", getCount("RESEARCH_PROJECT"));
            
            // 统计获奖总数
            insertGlobalStat("total_award", getCount("STUDENT_AWARD"));
            
            // 统计资金总额
            insertGlobalStat("total_funding", getSum("FUNDING_RECORD", "amount"));
            
            log.info("全局统计数据刷新完成！");
        }
        catch (Exception e)
        {
            log.error("刷新全局统计数据失败", e);
        }
    }

    /**
     * 刷新院系统计数据
     */
    public void refreshDepartmentStatistics()
    {
        log.info("开始刷新院系统计数据...");
        
        try
        {
            // 删除旧的院系统计数据
            Map<String, Object> params = new HashMap<>();
            params.put("dimensionType", "DEPARTMENT");
            dashboardMapper.deleteByDimension(params);
            
            // 这里可以按院系循环统计
            // 暂时留空，等需要时补充
            
            log.info("院系统计数据刷新完成！");
        }
        catch (Exception e)
        {
            log.error("刷新院系统计数据失败", e);
        }
    }

    /**
     * 刷新所有统计数据
     */
    public void refreshAllStatistics()
    {
        log.info("开始刷新所有统计数据...");
        refreshGlobalStatistics();
        refreshDepartmentStatistics();
        log.info("所有统计数据刷新完成！");
    }

    /**
     * 插入全局统计指标
     */
    private void insertGlobalStat(String metricName, BigDecimal value)
    {
        StatisticsView stat = new StatisticsView();
        stat.setDimensionType("GLOBAL");
        stat.setDimensionId(null);
        stat.setMetricName(metricName);
        stat.setMetricValue(value);
        dashboardMapper.upsertStatistic(stat);
    }

    /**
     * 获取表记录数
     */
    private BigDecimal getCount(String tableName)
    {
        return dashboardMapper.countTable(tableName);
    }

    /**
     * 获取表某列的总和
     */
    private BigDecimal getSum(String tableName, String columnName)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("columnName", columnName);
        return dashboardMapper.sumColumn(params);
    }
}
