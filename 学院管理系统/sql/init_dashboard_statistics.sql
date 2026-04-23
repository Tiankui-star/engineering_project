-- ==========================================================
-- 数据大屏统计初始化脚本
-- 执行此脚本将填充 STATISTICS_VIEW 表的初始数据
-- ==========================================================

-- 清空现有统计数据
DELETE FROM STATISTICS_VIEW WHERE dimension_type = 'GLOBAL';

-- 插入全局统计数据
INSERT INTO STATISTICS_VIEW (dimension_type, dimension_id, metric_name, metric_value, update_date)
VALUES 
    ('GLOBAL', NULL, 'total_faculty', (SELECT COUNT(*) FROM FACULTY), NOW()),
    ('GLOBAL', NULL, 'total_student', (SELECT COUNT(*) FROM STUDENT), NOW()),
    ('GLOBAL', NULL, 'total_paper', (SELECT COUNT(*) FROM ACADEMIC_PAPER), NOW()),
    ('GLOBAL', NULL, 'total_patent', (SELECT COUNT(*) FROM PATENT), NOW()),
    ('GLOBAL', NULL, 'total_project', (SELECT COUNT(*) FROM RESEARCH_PROJECT), NOW()),
    ('GLOBAL', NULL, 'total_award', (SELECT COUNT(*) FROM STUDENT_AWARD), NOW()),
    ('GLOBAL', NULL, 'total_funding', (SELECT COALESCE(SUM(amount), 0) FROM FUNDING_RECORD), NOW());

-- 验证插入结果
SELECT * FROM STATISTICS_VIEW WHERE dimension_type = 'GLOBAL' ORDER BY stat_id;

-- 说明：
-- 1. total_faculty: 教师总数
-- 2. total_student: 学生总数
-- 3. total_paper: 论文总数
-- 4. total_patent: 专利总数
-- 5. total_project: 项目总数
-- 6. total_award: 获奖总数
-- 7. total_funding: 资金总额
