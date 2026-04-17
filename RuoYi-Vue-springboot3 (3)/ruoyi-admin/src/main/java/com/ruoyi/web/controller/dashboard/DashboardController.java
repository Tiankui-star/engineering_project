package com.ruoyi.web.controller.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.task.StatisticsRefreshTask;
import com.ruoyi.web.mapper.DashboardMapper;

/**
 * 数据大屏统计Controller
 * 
 * @author ruoyi
 * @date 2026-04-14
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
{
    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private StatisticsRefreshTask statisticsTask;

    /**
     * 获取总体概览数据
     */
    @GetMapping("/overview")
    public AjaxResult getOverview()
    {
        Map<String, Object> data = new HashMap<>();
        
        // 从 STATISTICS_VIEW 表获取预聚合数据
        data.put("totalFaculty", dashboardMapper.selectGlobalMetric("total_faculty"));
        data.put("totalStudent", dashboardMapper.selectGlobalMetric("total_student"));
        data.put("totalPaper", dashboardMapper.selectGlobalMetric("total_paper"));
        data.put("totalPatent", dashboardMapper.selectGlobalMetric("total_patent"));
        data.put("totalProject", dashboardMapper.selectGlobalMetric("total_project"));
        data.put("totalAward", dashboardMapper.selectGlobalMetric("total_award"));
        data.put("totalFunding", dashboardMapper.selectGlobalMetric("total_funding"));
        
        return AjaxResult.success(data);
    }

    /**
     * 获取教师职称分布
     */
    @GetMapping("/faculty/title-distribution")
    public AjaxResult getFacultyTitleDistribution()
    {
        List<Map<String, Object>> data = dashboardMapper.selectFacultyTitleDistribution();
        return AjaxResult.success(data);
    }

    /**
     * 获取学生学籍状态分布
     */
    @GetMapping("/student/status-distribution")
    public AjaxResult getStudentStatusDistribution()
    {
        List<Map<String, Object>> data = dashboardMapper.selectStudentStatusDistribution();
        return AjaxResult.success(data);
    }

    /**
     * 获取论文年度趋势
     */
    @GetMapping("/paper/yearly-trend")
    public AjaxResult getPaperYearlyTrend()
    {
        List<Map<String, Object>> data = dashboardMapper.selectPaperYearlyTrend();
        return AjaxResult.success(data);
    }

    /**
     * 获取项目资金年度趋势
     */
    @GetMapping("/funding/yearly-trend")
    public AjaxResult getFundingYearlyTrend()
    {
        List<Map<String, Object>> data = dashboardMapper.selectFundingYearlyTrend();
        return AjaxResult.success(data);
    }

    /**
     * 获取院系统计对比
     */
    @GetMapping("/department/comparison")
    public AjaxResult getDepartmentComparison()
    {
        List<Map<String, Object>> data = dashboardMapper.selectDepartmentComparison();
        return AjaxResult.success(data);
    }

    /**
     * 获取项目级别分布
     */
    @GetMapping("/project/level-distribution")
    public AjaxResult getProjectLevelDistribution()
    {
        List<Map<String, Object>> data = dashboardMapper.selectProjectLevelDistribution();
        return AjaxResult.success(data);
    }

    /**
     * 获取专利类型分布
     */
    @GetMapping("/patent/type-distribution")
    public AjaxResult getPatentTypeDistribution()
    {
        List<Map<String, Object>> data = dashboardMapper.selectPatentTypeDistribution();
        return AjaxResult.success(data);
    }

    /**
     * 手动刷新统计数据（管理员权限）
     */
    @PreAuthorize("@ss.hasPermi('dashboard:statistics:refresh')")
    @PostMapping("/statistics/refresh")
    public AjaxResult refreshStatistics()
    {
        statisticsTask.refreshAllStatistics();
        return AjaxResult.success("统计数据刷新成功");
    }
}
