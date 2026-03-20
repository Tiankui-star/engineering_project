USE cis;

-- ==========================================================
-- 1. 基础依赖表 (无外键)
-- ==========================================================

-- 1. 管理员表 (ADMIN)
CREATE TABLE `ADMIN` (
    `admin_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员唯一标识符',
    `username` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '登录用户名',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '账户是否激活'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员账户信息';

-- 2. 用户表 (USER)
CREATE TABLE `USER` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识符',
    `user_code` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '用户编码（工号或学号）',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密密码',
    `user_type` ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL COMMENT '用户类型',
    `email` VARCHAR(100) COMMENT '电子邮箱',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `last_login` DATETIME COMMENT '最后登录时间',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '账户状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统统一用户入口';

-- 3. 院系表 (DEPARTMENT)
CREATE TABLE `DEPARTMENT` (
    `department_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `department_code` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '院系代码',
    `department_name` VARCHAR(100) NOT NULL COMMENT '院系名称',
    `head_faculty` VARCHAR(50) COMMENT '系主任姓名',
    `description` TEXT COMMENT '简介'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院下属院系信息';

-- ==========================================================
-- 2. 一级外键表 (依赖于 USER, DEPARTMENT)
-- ==========================================================

-- 4. 教师表 (FACULTY)
CREATE TABLE `FACULTY` (
    `faculty_id` INT PRIMARY KEY COMMENT '关联用户表',
    `faculty_number` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '工号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `title` VARCHAR(50) COMMENT '职称',
    `research_field` VARCHAR(100) COMMENT '研究领域',
    `department_id` INT COMMENT '所属院系ID',
    `employment_date` DATE COMMENT '入职日期',
    `status` ENUM('ACTIVE', 'INACTIVE', 'RETIRED', 'ON_LEAVE') COMMENT '在职状态',
    `paper_count` INT DEFAULT 0 COMMENT '[冗余] 论文数',
    `project_count` INT DEFAULT 0 COMMENT '[冗余] 项目数',
    `total_funding` DECIMAL(15,2) DEFAULT 0.00 COMMENT '[冗余] 总资金',
    FOREIGN KEY (`faculty_id`) REFERENCES `USER`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工信息及科研统计';

-- 5. 学生表 (STUDENT)
CREATE TABLE `STUDENT` (
    `student_id` INT PRIMARY KEY COMMENT '关联用户表',
    `student_number` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '学号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `birth_date` DATE COMMENT '出生日期',
    `gender` ENUM('M', 'F', 'OTHER') COMMENT '性别',
    `id_card` VARCHAR(20) COMMENT '身份证号',
    `department_id` INT COMMENT '所属院系ID',
    `enrollment_date` DATE COMMENT '入学日期',
    `status` ENUM('ENROLLED', 'GRADUATED', 'SUSPENDED') COMMENT '学籍状态',
    `award_count` INT DEFAULT 0 COMMENT '[冗余] 获奖数',
    `paper_count` INT DEFAULT 0 COMMENT '[冗余] 参与论文数',
    FOREIGN KEY (`student_id`) REFERENCES `USER`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生基本信息及成就统计';

-- ==========================================================
-- 3. 二级外键表 (核心业务表，依赖教职工/学生表)
-- ==========================================================

-- 6. 论文表 (ACADEMIC_PAPER)
CREATE TABLE `ACADEMIC_PAPER` (
    `paper_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `paper_title` VARCHAR(255) NOT NULL COMMENT '标题',
    `abstract` TEXT COMMENT '摘要',
    `journal_name` VARCHAR(255) COMMENT '期刊名',
    `volume_issue` VARCHAR(50) COMMENT '卷期',
    `page_numbers` VARCHAR(50) COMMENT '页码',
    `publication_date` DATE COMMENT '发表日期',
    `publication_level` ENUM('CORE_A', 'CORE_B', 'CORE_C', 'SCI', 'EI', 'OTHER') COMMENT '级别',
    `doi` VARCHAR(100) COMMENT 'DOI号',
    `citation_count` INT DEFAULT 0 COMMENT '引用次数',
    `lead_author_faculty_id` INT COMMENT '第一作者教师ID',
    `department_id` INT COMMENT '归属院系ID',
    FOREIGN KEY (`lead_author_faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE SET NULL,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发表的学术论文信息';

-- 8. 科研项目表 (RESEARCH_PROJECT)
CREATE TABLE `RESEARCH_PROJECT` (
    `project_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '项目唯一标识符',
    `project_code` VARCHAR(50) NOT NULL UNIQUE KEY COMMENT '项目编号',
    `project_name` VARCHAR(255) NOT NULL COMMENT '项目名称',
    `description` TEXT COMMENT '项目描述',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `project_level` ENUM('NATIONAL', 'PROVINCIAL', 'UNIVERSITY', 'ENTERPRISE', 'OTHER') COMMENT '项目级别',
    `project_status` ENUM('PROPOSED', 'ONGOING', 'COMPLETED', 'SUSPENDED') COMMENT '项目状态',
    `lead_faculty_id` INT COMMENT '负责人教师ID',
    `department_id` INT COMMENT '所属院系ID',
    FOREIGN KEY (`lead_faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE SET NULL,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院承担的科研项目信息';

-- 11. 学生获奖表 (STUDENT_AWARD)
CREATE TABLE `STUDENT_AWARD` (
    `award_id` INT AUTO_INCREMENT PRIMARY KEY,
    `award_name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `award_date` DATE,
    `award_level` ENUM('NATIONAL', 'PROVINCIAL', 'UNIVERSITY', 'OTHER'),
    `issuing_organization` VARCHAR(255),
    `student_id` INT,
    `supervisor_faculty_id` INT COMMENT '指导老师(可为空)',
    FOREIGN KEY (`student_id`) REFERENCES `STUDENT`(`student_id`) ON DELETE CASCADE,
    FOREIGN KEY (`supervisor_faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生获得的各类奖项记录';

-- 14. 课程成绩表 (GRADE_REPORT) - 新增
CREATE TABLE `GRADE_REPORT` (
    `report_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '成绩记录唯一标识符',
    `mark` INT NOT NULL COMMENT '成绩分数',
    `student_number` VARCHAR(50) NOT NULL COMMENT '学生学号',
    `faculty_number` VARCHAR(50) NOT NULL COMMENT '教师工号',
    `course_name` VARCHAR(100) COMMENT '课程名称',
    `semester` VARCHAR(20) COMMENT '学期',
    `record_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    FOREIGN KEY (`student_number`) REFERENCES `STUDENT`(`student_number`) ON DELETE CASCADE,
    FOREIGN KEY (`faculty_number`) REFERENCES `FACULTY`(`faculty_number`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录学生课程成绩';

-- ==========================================================
-- 4. 三级外键及关联表
-- ==========================================================

-- 7. 论文作者关系表 (PAPER_AUTHOR)
CREATE TABLE `PAPER_AUTHOR` (
    `authorship_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '作者关系唯一标识符',
    `paper_id` INT NOT NULL COMMENT '论文ID',
    `faculty_id` INT COMMENT '教师作者ID',
    `student_id` INT COMMENT '学生作者ID',
    `author_order` INT COMMENT '作者排序',
    `contribution_description` VARCHAR(255) COMMENT '贡献说明',
    FOREIGN KEY (`paper_id`) REFERENCES `ACADEMIC_PAPER`(`paper_id`) ON DELETE CASCADE,
    FOREIGN KEY (`faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE CASCADE,
    FOREIGN KEY (`student_id`) REFERENCES `STUDENT`(`student_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论文与作者的多对多关系';

-- 9. 项目资金记录表 (FUNDING_RECORD)
CREATE TABLE `FUNDING_RECORD` (
    `funding_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '资金记录唯一标识符',
    `project_id` INT NOT NULL COMMENT '所属项目ID',
    `amount` DECIMAL(15,2) NOT NULL COMMENT '资金金额',
    `allocation_date` DATE COMMENT '资金到账日期',
    `funding_source` VARCHAR(255) COMMENT '资金来源',
    `funding_type` ENUM('GRANT', 'SPONSORSHIP', 'MATCHING', 'OTHER') COMMENT '资金类型',
    `status` ENUM('PENDING', 'RECEIVED', 'SPENT') COMMENT '资金状态',
    FOREIGN KEY (`project_id`) REFERENCES `RESEARCH_PROJECT`(`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目资金详细记录';

-- 10. 项目参与关系表 (RESEARCH_PARTICIPATION)
CREATE TABLE `RESEARCH_PARTICIPATION` (
    `participation_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '参与记录唯一标识符',
    `project_id` INT NOT NULL COMMENT '项目ID',
    `student_id` INT NOT NULL COMMENT '参与学生ID',
    `role_description` VARCHAR(255) COMMENT '参与角色描述',
    `join_date` DATETIME COMMENT '加入日期',
    `completion_date` DATETIME COMMENT '完成日期',
    FOREIGN KEY (`project_id`) REFERENCES `RESEARCH_PROJECT`(`project_id`) ON DELETE CASCADE,
    FOREIGN KEY (`student_id`) REFERENCES `STUDENT`(`student_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生参与科研项目记录';

-- ==========================================================
-- 5. 独立统计与日志表
-- ==========================================================

-- 12. 统计视图表 (STATISTICS_VIEW)
-- 建议：未来可以通过存储过程定时刷新此表数据
CREATE TABLE `STATISTICS_VIEW` (
    `stat_id` INT AUTO_INCREMENT PRIMARY KEY,
    `dimension_type` ENUM('GLOBAL', 'DEPARTMENT') NOT NULL COMMENT '维度类型',
    `dimension_id` INT COMMENT '分系统计存院系ID，全局存NULL',
    `metric_name` VARCHAR(50) NOT NULL COMMENT '指标名',
    `metric_value` DECIMAL(15,2) NOT NULL COMMENT '值',
    `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='为管理大屏提供预聚合数据';

-- 13. 系统日志表 (SYSTEM_LOG)
CREATE TABLE `SYSTEM_LOG` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '日志唯一标识符',
    `user_id` INT COMMENT '操作用户ID (系统级操作可能为空)',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `details` TEXT COMMENT '操作详情',
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `ip_address` VARCHAR(45) COMMENT 'IP地址 (兼容IPv6)',
    `user_agent` VARCHAR(255) COMMENT '客户端信息',
    FOREIGN KEY (`user_id`) REFERENCES `USER`(`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录系统操作日志';

-- ==========================================================
-- 触发器
-- ==========================================================

DELIMITER //

-- ==========================================================
-- 项目相关触发器
-- ==========================================================

-- 新增项目时：负责人项目数 +1
CREATE TRIGGER after_project_insert
AFTER INSERT ON RESEARCH_PROJECT
FOR EACH ROW
BEGIN
    IF NEW.lead_faculty_id IS NOT NULL THEN
        UPDATE FACULTY 
        SET project_count = project_count + 1 
        WHERE faculty_id = NEW.lead_faculty_id;
    END IF;
END //

-- 删除项目时：负责人项目数 -1
CREATE TRIGGER after_project_delete
AFTER DELETE ON RESEARCH_PROJECT
FOR EACH ROW
BEGIN
    IF OLD.lead_faculty_id IS NOT NULL THEN
        UPDATE FACULTY 
        SET project_count = GREATEST(0, project_count - 1) 
        WHERE faculty_id = OLD.lead_faculty_id;
    END IF;
END //

-- ==========================================================
-- 获奖相关触发器
-- ==========================================================

-- 新增获奖时：学生获奖数 +1
CREATE TRIGGER after_award_insert
AFTER INSERT ON STUDENT_AWARD
FOR EACH ROW
BEGIN
    IF NEW.student_id IS NOT NULL THEN
        UPDATE STUDENT 
        SET award_count = award_count + 1 
        WHERE student_id = NEW.student_id;
    END IF;
END //

-- 删除获奖时：学生获奖数 -1
CREATE TRIGGER after_award_delete
AFTER DELETE ON STUDENT_AWARD
FOR EACH ROW
BEGIN
    IF OLD.student_id IS NOT NULL THEN
        UPDATE STUDENT 
        SET award_count = GREATEST(0, award_count - 1) 
        WHERE student_id = OLD.student_id;
    END IF;
END //

-- ==========================================================
-- 资金相关触发器
-- ==========================================================

-- 资金录入时：累加教师总资金
CREATE TRIGGER after_funding_insert
AFTER INSERT ON FUNDING_RECORD
FOR EACH ROW
BEGIN
    DECLARE v_faculty_id INT;
    
    SELECT lead_faculty_id INTO v_faculty_id 
    FROM RESEARCH_PROJECT 
    WHERE project_id = NEW.project_id;
    
    IF v_faculty_id IS NOT NULL THEN
        UPDATE FACULTY 
        SET total_funding = total_funding + NEW.amount 
        WHERE faculty_id = v_faculty_id;
    END IF;
END //

-- 资金删除时：扣除对应的金额
CREATE TRIGGER after_funding_delete
AFTER DELETE ON FUNDING_RECORD
FOR EACH ROW
BEGIN
    DECLARE v_faculty_id INT;
    
    SELECT lead_faculty_id INTO v_faculty_id 
    FROM RESEARCH_PROJECT 
    WHERE project_id = OLD.project_id;
    
    IF v_faculty_id IS NOT NULL THEN
        UPDATE FACULTY 
        SET total_funding = GREATEST(0.00, total_funding - OLD.amount) 
        WHERE faculty_id = v_faculty_id;
    END IF;
END //

-- ==========================================================
-- 项目状态更新触发器
-- ==========================================================

CREATE TRIGGER after_project_status_update
AFTER UPDATE ON RESEARCH_PROJECT
FOR EACH ROW
BEGIN
    IF NEW.project_status = 'COMPLETED' AND OLD.project_status != 'COMPLETED' THEN
        UPDATE RESEARCH_PARTICIPATION 
        SET completion_date = NOW() 
        WHERE project_id = NEW.project_id 
          AND completion_date IS NULL;
    END IF;
END //

DELIMITER ;
