-- ==========================================================
-- 科研管理系统数据库表结构（兼容版）
-- 适用环境：MySQL 5.7+ / MySQL 8.0+
-- 执行顺序：
--   1. 先执行若依基础SQL：ry_20260320.sql
--   2. 再执行本SQL文件
-- ==========================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==========================================================
-- 1. 删除所有已存在的表（按依赖顺序反向删除）
-- ==========================================================
DROP TABLE IF EXISTS `SYSTEM_LOG`;
DROP TABLE IF EXISTS `STATISTICS_VIEW`;
DROP TABLE IF EXISTS `PROJECT_OUTPUT`;
DROP TABLE IF EXISTS `PATENT_AUTHOR`;
DROP TABLE IF EXISTS `PATENT`;
DROP TABLE IF EXISTS `PROJECT_MEMBER`;
DROP TABLE IF EXISTS `FUNDING_RECORD`;
DROP TABLE IF EXISTS `PAPER_AUTHOR`;
DROP TABLE IF EXISTS `STUDENT_AWARD`;
DROP TABLE IF EXISTS `RESEARCH_PROJECT`;
DROP TABLE IF EXISTS `ACADEMIC_PAPER`;
DROP TABLE IF EXISTS `STUDENT`;
DROP TABLE IF EXISTS `FACULTY`;
DROP TABLE IF EXISTS `DEPARTMENT`;

-- ==========================================================
-- 2. 创建基础表（无外键依赖）
-- ==========================================================

-- 院系表（先不创建外键，等FACULTY表创建后再添加）
CREATE TABLE `DEPARTMENT` (
    `department_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `department_code` VARCHAR(50) NOT NULL UNIQUE COMMENT '院系代码',
    `department_name` VARCHAR(100) NOT NULL COMMENT '院系名称',
    `head_faculty_id` BIGINT DEFAULT NULL COMMENT '系主任ID（关联faculty.faculty_id）',
    `description` TEXT COMMENT '简介'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院下属院系信息';

-- ==========================================================
-- 3. 教师表和学生表（依赖DEPARTMENT表）
-- ==========================================================

-- 教师表
CREATE TABLE `FACULTY` (
    `faculty_id` BIGINT NOT NULL PRIMARY KEY COMMENT '关联系统用户ID (sys_user.user_id)',
    `faculty_number` VARCHAR(50) NOT NULL UNIQUE COMMENT '工号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` ENUM('M', 'F', 'OTHER') DEFAULT NULL COMMENT '性别',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `research_field` VARCHAR(100) DEFAULT NULL COMMENT '研究领域',
    `department_id` INT DEFAULT NULL COMMENT '所属院系ID',
    `employment_date` DATE DEFAULT NULL COMMENT '入职日期',
    `status` ENUM('ACTIVE', 'INACTIVE', 'RETIRED', 'ON_LEAVE') DEFAULT NULL COMMENT '在职状态',
    `paper_count` INT DEFAULT 0 COMMENT '[冗余] 一作论文数',
    `patent_count` INT DEFAULT 0 COMMENT '[冗余] 专利数',
    `project_count` INT DEFAULT 0 COMMENT '[冗余] 项目数',
    `total_funding` DECIMAL(15,2) DEFAULT 0.00 COMMENT '[冗余] 总资金',
    FOREIGN KEY (`faculty_id`) REFERENCES `sys_user`(`user_id`) ON DELETE RESTRICT,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工信息及科研统计';

-- 学生表
CREATE TABLE `STUDENT` (
    `student_id` BIGINT NOT NULL PRIMARY KEY COMMENT '关联系统用户ID (sys_user.user_id)',
    `student_number` VARCHAR(50) NOT NULL UNIQUE COMMENT '学号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
    `gender` ENUM('M', 'F', 'OTHER') DEFAULT NULL COMMENT '性别',
    `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
    `department_id` INT DEFAULT NULL COMMENT '所属院系ID',
    `enrollment_date` DATE DEFAULT NULL COMMENT '入学日期',
    `status` ENUM('ENROLLED', 'GRADUATED', 'SUSPENDED') DEFAULT NULL COMMENT '学籍状态',
    `award_count` INT DEFAULT 0 COMMENT '[冗余] 获奖数',
    `paper_count` INT DEFAULT 0 COMMENT '[冗余] 一作论文数',
    `patent_count` INT DEFAULT 0 COMMENT '[冗余] 专利数',
    `project_count` INT DEFAULT 0 COMMENT '[冗余] 参与项目数',
    FOREIGN KEY (`student_id`) REFERENCES `sys_user`(`user_id`) ON DELETE RESTRICT,
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生基本信息及成就统计';

-- ==========================================================
-- 4. 添加DEPARTMENT表的外键（此时FACULTY表已存在）
-- ==========================================================
ALTER TABLE `DEPARTMENT`
    ADD CONSTRAINT `fk_department_head_faculty`
    FOREIGN KEY (`head_faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE SET NULL;

-- ==========================================================
-- 5. 核心业务表（依赖教师和学生表）
-- ==========================================================

-- 论文表
CREATE TABLE `ACADEMIC_PAPER` (
    `paper_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '论文ID',
    `paper_title` VARCHAR(255) NOT NULL COMMENT '论文标题',
    `abstract` TEXT COMMENT '摘要',
    `journal_name` VARCHAR(255) COMMENT '期刊名称',
    `volume_issue` VARCHAR(50) COMMENT '卷期',
    `page_numbers` VARCHAR(50) COMMENT '页码',
    `publication_date` DATE COMMENT '发表日期',
    `publication_level` ENUM('CORE_A', 'CORE_B', 'CORE_C', 'SCI', 'EI', 'OTHER') COMMENT '发表级别',
    `doi` VARCHAR(100) COMMENT 'DOI号',
    `citation_count` INT DEFAULT 0 COMMENT '被引次数',
    `first_author_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '一作类型：教师或学生',
    `first_author_id` BIGINT NOT NULL COMMENT '一作ID（关联sys_user.user_id）',
    `department_id` INT COMMENT '所属院系ID',
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发表的学术论文信息';

-- 科研项目表
CREATE TABLE `RESEARCH_PROJECT` (
    `project_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '项目ID',
    `project_code` VARCHAR(50) NOT NULL UNIQUE COMMENT '项目编号',
    `project_name` VARCHAR(255) NOT NULL COMMENT '项目名称',
    `description` TEXT COMMENT '项目描述',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `project_level` ENUM('NATIONAL', 'PROVINCIAL', 'UNIVERSITY', 'ENTERPRISE', 'OTHER') COMMENT '项目级别',
    `project_status` ENUM('PROPOSED', 'ONGOING', 'COMPLETED', 'SUSPENDED') COMMENT '项目状态',
    `leader_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '负责人类型：教师或学生',
    `leader_id` BIGINT NOT NULL COMMENT '负责人ID（关联sys_user.user_id）',
    `department_id` INT COMMENT '所属院系ID',
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院承担的科研项目信息';

-- 学生获奖表
CREATE TABLE `STUDENT_AWARD` (
    `award_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '获奖ID',
    `award_name` VARCHAR(255) NOT NULL COMMENT '获奖名称',
    `description` TEXT COMMENT '获奖描述',
    `award_date` DATE COMMENT '获奖日期',
    `award_level` ENUM('NATIONAL', 'PROVINCIAL', 'UNIVERSITY', 'OTHER') COMMENT '获奖级别',
    `issuing_organization` VARCHAR(255) COMMENT '颁发机构',
    `student_id` BIGINT DEFAULT NULL COMMENT '学生ID，允许NULL以保留历史',
    `supervisor_faculty_id` BIGINT COMMENT '指导老师ID',
    FOREIGN KEY (`student_id`) REFERENCES `STUDENT`(`student_id`) ON DELETE SET NULL,
    FOREIGN KEY (`supervisor_faculty_id`) REFERENCES `FACULTY`(`faculty_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生获得的各类奖项记录';

-- 论文作者关系表
CREATE TABLE `PAPER_AUTHOR` (
    `authorship_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '作者关系ID',
    `paper_id` INT NOT NULL COMMENT '论文ID',
    `author_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '作者类型：教师或学生',
    `author_id` BIGINT NOT NULL COMMENT '作者ID（关联sys_user.user_id）',
    `author_order` INT COMMENT '作者排序',
    `contribution_description` VARCHAR(255) COMMENT '贡献描述',
    FOREIGN KEY (`paper_id`) REFERENCES `ACADEMIC_PAPER`(`paper_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论文与作者的多对多关系';

-- 项目资金记录表
CREATE TABLE `FUNDING_RECORD` (
    `funding_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '资金记录ID',
    `project_id` INT NOT NULL COMMENT '项目ID',
    `amount` DECIMAL(15,2) NOT NULL COMMENT '金额',
    `allocation_date` DATE COMMENT '分配日期',
    `funding_source` VARCHAR(255) COMMENT '资金来源',
    `funding_type` ENUM('GRANT', 'SPONSORSHIP', 'MATCHING', 'OTHER') COMMENT '资金类型',
    `status` ENUM('PENDING', 'RECEIVED', 'SPENT') COMMENT '资金状态',
    FOREIGN KEY (`project_id`) REFERENCES `RESEARCH_PROJECT`(`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目资金详细记录';

-- 项目成员表
CREATE TABLE `PROJECT_MEMBER` (
    `member_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '成员ID',
    `project_id` INT NOT NULL COMMENT '关联项目ID',
    `member_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '成员类型',
    `user_id` BIGINT NOT NULL COMMENT '成员ID（关联sys_user.user_id）',
    `role_type` ENUM('LEADER', 'PARTICIPANT', 'ADVISOR', 'OTHER') DEFAULT 'PARTICIPANT' COMMENT '角色类型',
    `role_description` VARCHAR(255) COMMENT '角色描述',
    `join_date` DATE COMMENT '加入日期',
    `leave_date` DATE COMMENT '离开日期',
    `contribution_percent` DECIMAL(5,2) COMMENT '贡献百分比',
    FOREIGN KEY (`project_id`) REFERENCES `RESEARCH_PROJECT`(`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成员（教师和学生）';

-- 专利表
CREATE TABLE `PATENT` (
    `patent_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '专利ID',
    `patent_code` VARCHAR(100) NOT NULL UNIQUE COMMENT '专利号',
    `patent_name` VARCHAR(255) NOT NULL COMMENT '专利名称',
    `abstract` TEXT COMMENT '专利摘要',
    `patent_type` ENUM('INVENTION', 'UTILITY_MODEL', 'DESIGN') NOT NULL COMMENT '专利类型：发明/实用新型/外观设计',
    `patent_level` ENUM('NATIONAL', 'PROVINCIAL', 'CITY') COMMENT '专利级别',
    `application_date` DATE COMMENT '申请日期',
    `grant_date` DATE COMMENT '授权日期',
    `expiry_date` DATE COMMENT '到期日期',
    `patent_status` ENUM('APPLYING', 'EXAMINING', 'GRANTED', 'REJECTED', 'EXPIRED') DEFAULT 'APPLYING' COMMENT '专利状态',
    `first_author_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '第一作者类型',
    `first_author_id` BIGINT NOT NULL COMMENT '第一作者ID（关联sys_user.user_id）',
    `department_id` INT COMMENT '所属院系',
    `attachment_url` VARCHAR(500) COMMENT '附件链接',
    FOREIGN KEY (`department_id`) REFERENCES `DEPARTMENT`(`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专利信息';

-- 专利作者关系表
CREATE TABLE `PATENT_AUTHOR` (
    `authorship_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '作者关系ID',
    `patent_id` INT NOT NULL COMMENT '专利ID',
    `author_type` ENUM('FACULTY', 'STUDENT') NOT NULL COMMENT '作者类型',
    `author_id` BIGINT NOT NULL COMMENT '作者ID（关联sys_user.user_id）',
    `author_order` INT DEFAULT 0 COMMENT '作者排序',
    `contribution_percent` DECIMAL(5,2) COMMENT '贡献百分比',
    FOREIGN KEY (`patent_id`) REFERENCES `PATENT`(`patent_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专利与作者的多对多关系';

-- 项目成果关联表
CREATE TABLE `PROJECT_OUTPUT` (
    `project_output_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '项目成果ID',
    `project_id` INT NOT NULL COMMENT '关联项目ID',
    `output_type` ENUM('PAPER', 'PATENT') NOT NULL COMMENT '成果类型：论文/专利',
    `output_id` INT NOT NULL COMMENT '成果ID',
    `output_date` DATE COMMENT '产出日期',
    `contribution_description` VARCHAR(255) COMMENT '贡献描述',
    UNIQUE KEY `uk_project_output` (`project_id`, `output_type`, `output_id`),
    FOREIGN KEY (`project_id`) REFERENCES `RESEARCH_PROJECT`(`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成果关联（论文和专利）';

-- 统计视图表
CREATE TABLE `STATISTICS_VIEW` (
    `stat_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    `dimension_type` ENUM('GLOBAL', 'DEPARTMENT') NOT NULL COMMENT '维度类型',
    `dimension_id` INT COMMENT '维度ID',
    `metric_name` VARCHAR(50) NOT NULL COMMENT '指标名称',
    `metric_value` DECIMAL(15,2) NOT NULL COMMENT '指标值',
    `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_dimension_metric` (`dimension_type`, `dimension_id`, `metric_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='为管理大屏提供预聚合数据';

-- 系统日志表
CREATE TABLE `SYSTEM_LOG` (
    `log_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `user_id` BIGINT COMMENT '操作用户ID (sys_user.user_id)',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `details` TEXT COMMENT '操作详情',
    `timestamp` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
    `ip_address` VARCHAR(45) COMMENT 'IP地址',
    `user_agent` VARCHAR(255) COMMENT '用户代理',
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录系统操作日志';

-- ==========================================================
-- 6. 创建触发器
-- ==========================================================

-- 项目新增触发器
CREATE TRIGGER `after_project_insert`
AFTER INSERT ON `RESEARCH_PROJECT`
FOR EACH ROW
BEGIN
    -- 添加项目负责人到 PROJECT_MEMBER 表
    INSERT INTO PROJECT_MEMBER (project_id, member_type, user_id, role_type, role_description, join_date)
    VALUES (NEW.project_id, NEW.leader_type, NEW.leader_id, 'LEADER', '项目负责人', NEW.start_date);
    
    -- 更新负责人的项目数统计
    IF NEW.leader_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET project_count = project_count + 1 
        WHERE faculty_id = NEW.leader_id;
    ELSEIF NEW.leader_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET project_count = project_count + 1 
        WHERE student_id = NEW.leader_id;
    END IF;
END;

-- 项目删除触发器
CREATE TRIGGER `after_project_delete`
AFTER DELETE ON `RESEARCH_PROJECT`
FOR EACH ROW
BEGIN
    -- 删除项目时级联删除 PROJECT_MEMBER 记录
    DELETE FROM PROJECT_MEMBER WHERE project_id = OLD.project_id;
    
    -- 更新负责人的项目数统计
    IF OLD.leader_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET project_count = GREATEST(0, project_count - 1) 
        WHERE faculty_id = OLD.leader_id;
    ELSEIF OLD.leader_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET project_count = GREATEST(0, project_count - 1) 
        WHERE student_id = OLD.leader_id;
    END IF;
END;

-- 获奖新增触发器
CREATE TRIGGER `after_award_insert`
AFTER INSERT ON `STUDENT_AWARD`
FOR EACH ROW
BEGIN
    IF NEW.student_id IS NOT NULL THEN
        UPDATE STUDENT 
        SET award_count = award_count + 1 
        WHERE student_id = NEW.student_id;
    END IF;
END;

-- 获奖删除触发器
CREATE TRIGGER `after_award_delete`
AFTER DELETE ON `STUDENT_AWARD`
FOR EACH ROW
BEGIN
    IF OLD.student_id IS NOT NULL THEN
        UPDATE STUDENT 
        SET award_count = GREATEST(0, award_count - 1) 
        WHERE student_id = OLD.student_id;
    END IF;
END;

-- 资金新增触发器
CREATE TRIGGER `after_funding_insert`
AFTER INSERT ON `FUNDING_RECORD`
FOR EACH ROW
BEGIN
    DECLARE v_leader_type ENUM('FACULTY', 'STUDENT');
    DECLARE v_leader_id BIGINT;
    
    SELECT leader_type, leader_id INTO v_leader_type, v_leader_id 
    FROM RESEARCH_PROJECT 
    WHERE project_id = NEW.project_id;
    
    IF v_leader_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET total_funding = total_funding + NEW.amount 
        WHERE faculty_id = v_leader_id;
    END IF;
END;

-- 资金删除触发器
CREATE TRIGGER `after_funding_delete`
AFTER DELETE ON `FUNDING_RECORD`
FOR EACH ROW
BEGIN
    DECLARE v_leader_type ENUM('FACULTY', 'STUDENT');
    DECLARE v_leader_id BIGINT;
    
    SELECT leader_type, leader_id INTO v_leader_type, v_leader_id 
    FROM RESEARCH_PROJECT 
    WHERE project_id = OLD.project_id;
    
    IF v_leader_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET total_funding = GREATEST(0.00, total_funding - OLD.amount) 
        WHERE faculty_id = v_leader_id;
    END IF;
END;

-- 项目状态更新触发器
CREATE TRIGGER `after_project_status_update`
AFTER UPDATE ON `RESEARCH_PROJECT`
FOR EACH ROW
BEGIN
    IF NEW.project_status = 'COMPLETED' AND OLD.project_status != 'COMPLETED' THEN
        UPDATE PROJECT_MEMBER 
        SET leave_date = NOW() 
        WHERE project_id = NEW.project_id 
          AND leave_date IS NULL;
    END IF;
END;

-- 论文作者新增触发器
CREATE TRIGGER `after_paper_author_insert`
AFTER INSERT ON `PAPER_AUTHOR`
FOR EACH ROW
BEGIN
    IF NEW.author_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET paper_count = paper_count + 1 
        WHERE faculty_id = NEW.author_id;
    ELSEIF NEW.author_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET paper_count = paper_count + 1 
        WHERE student_id = NEW.author_id;
    END IF;
END;

-- 论文作者删除触发器
CREATE TRIGGER `after_paper_author_delete`
AFTER DELETE ON `PAPER_AUTHOR`
FOR EACH ROW
BEGIN
    IF OLD.author_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET paper_count = GREATEST(0, paper_count - 1) 
        WHERE faculty_id = OLD.author_id;
    ELSEIF OLD.author_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET paper_count = GREATEST(0, paper_count - 1) 
        WHERE student_id = OLD.author_id;
    END IF;
END;

-- 论文作者更新触发器
CREATE TRIGGER `after_paper_author_update`
AFTER UPDATE ON `PAPER_AUTHOR`
FOR EACH ROW
BEGIN
    IF OLD.author_type != NEW.author_type OR OLD.author_id != NEW.author_id THEN
        -- 原作者论文数-1
        IF OLD.author_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET paper_count = GREATEST(0, paper_count - 1) 
            WHERE faculty_id = OLD.author_id;
        ELSEIF OLD.author_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET paper_count = GREATEST(0, paper_count - 1) 
            WHERE student_id = OLD.author_id;
        END IF;
        
        -- 新作者论文数+1
        IF NEW.author_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET paper_count = paper_count + 1 
            WHERE faculty_id = NEW.author_id;
        ELSEIF NEW.author_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET paper_count = paper_count + 1 
            WHERE student_id = NEW.author_id;
        END IF;
    END IF;
END;

-- 专利作者新增触发器
CREATE TRIGGER `after_patent_author_insert`
AFTER INSERT ON `PATENT_AUTHOR`
FOR EACH ROW
BEGIN
    IF NEW.author_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET patent_count = patent_count + 1 
        WHERE faculty_id = NEW.author_id;
    ELSEIF NEW.author_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET patent_count = patent_count + 1 
        WHERE student_id = NEW.author_id;
    END IF;
END;

-- 专利作者删除触发器
CREATE TRIGGER `after_patent_author_delete`
AFTER DELETE ON `PATENT_AUTHOR`
FOR EACH ROW
BEGIN
    IF OLD.author_type = 'FACULTY' THEN
        UPDATE FACULTY 
        SET patent_count = GREATEST(0, patent_count - 1) 
        WHERE faculty_id = OLD.author_id;
    ELSEIF OLD.author_type = 'STUDENT' THEN
        UPDATE STUDENT 
        SET patent_count = GREATEST(0, patent_count - 1) 
        WHERE student_id = OLD.author_id;
    END IF;
END;

-- 专利作者更新触发器
CREATE TRIGGER `after_patent_author_update`
AFTER UPDATE ON `PATENT_AUTHOR`
FOR EACH ROW
BEGIN
    IF OLD.author_type != NEW.author_type OR OLD.author_id != NEW.author_id THEN
        -- 原作者专利数-1
        IF OLD.author_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET patent_count = GREATEST(0, patent_count - 1) 
            WHERE faculty_id = OLD.author_id;
        ELSEIF OLD.author_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET patent_count = GREATEST(0, patent_count - 1) 
            WHERE student_id = OLD.author_id;
        END IF;
        
        -- 新作者专利数+1
        IF NEW.author_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET patent_count = patent_count + 1 
            WHERE faculty_id = NEW.author_id;
        ELSEIF NEW.author_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET patent_count = patent_count + 1 
            WHERE student_id = NEW.author_id;
        END IF;
    END IF;
END;

-- 项目成员新增触发器
CREATE TRIGGER `after_project_member_insert`
AFTER INSERT ON `PROJECT_MEMBER`
FOR EACH ROW
BEGIN
    IF NEW.role_type != 'LEADER' THEN
        IF NEW.member_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET project_count = project_count + 1 
            WHERE faculty_id = NEW.user_id;
        ELSEIF NEW.member_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET project_count = project_count + 1 
            WHERE student_id = NEW.user_id;
        END IF;
    END IF;
END;

-- 项目成员删除触发器
CREATE TRIGGER `after_project_member_delete`
AFTER DELETE ON `PROJECT_MEMBER`
FOR EACH ROW
BEGIN
    IF OLD.role_type != 'LEADER' THEN
        IF OLD.member_type = 'FACULTY' THEN
            UPDATE FACULTY 
            SET project_count = GREATEST(0, project_count - 1) 
            WHERE faculty_id = OLD.user_id;
        ELSEIF OLD.member_type = 'STUDENT' THEN
            UPDATE STUDENT 
            SET project_count = GREATEST(0, project_count - 1) 
            WHERE student_id = OLD.user_id;
        END IF;
    END IF;
END;

-- ==========================================================
-- 7. 数据大屏实时同步触发器
-- ==========================================================

-- 教师新增触发器
CREATE TRIGGER `after_faculty_insert_dashboard`
AFTER INSERT ON `FACULTY`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM FACULTY),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_faculty';
END;

-- 教师删除触发器
CREATE TRIGGER `after_faculty_delete_dashboard`
AFTER DELETE ON `FACULTY`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM FACULTY),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_faculty';
END;

-- 学生新增触发器
CREATE TRIGGER `after_student_insert_dashboard`
AFTER INSERT ON `STUDENT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM STUDENT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_student';
END;

-- 学生删除触发器
CREATE TRIGGER `after_student_delete_dashboard`
AFTER DELETE ON `STUDENT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM STUDENT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_student';
END;

-- 论文新增触发器
CREATE TRIGGER `after_paper_insert_dashboard`
AFTER INSERT ON `ACADEMIC_PAPER`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM ACADEMIC_PAPER),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_paper';
END;

-- 论文删除触发器
CREATE TRIGGER `after_paper_delete_dashboard`
AFTER DELETE ON `ACADEMIC_PAPER`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM ACADEMIC_PAPER),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_paper';
END;

-- 专利新增触发器
CREATE TRIGGER `after_patent_insert_dashboard`
AFTER INSERT ON `PATENT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM PATENT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_patent';
END;

-- 专利删除触发器
CREATE TRIGGER `after_patent_delete_dashboard`
AFTER DELETE ON `PATENT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM PATENT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_patent';
END;

-- 项目新增触发器
CREATE TRIGGER `after_project_insert_dashboard`
AFTER INSERT ON `RESEARCH_PROJECT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM RESEARCH_PROJECT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_project';
END;

-- 项目删除触发器
CREATE TRIGGER `after_project_delete_dashboard`
AFTER DELETE ON `RESEARCH_PROJECT`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM RESEARCH_PROJECT),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_project';
END;

-- 获奖新增触发器
CREATE TRIGGER `after_award_insert_dashboard`
AFTER INSERT ON `STUDENT_AWARD`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM STUDENT_AWARD),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_award';
END;

-- 获奖删除触发器
CREATE TRIGGER `after_award_delete_dashboard`
AFTER DELETE ON `STUDENT_AWARD`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COUNT(*) FROM STUDENT_AWARD),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_award';
END;

-- 资金新增触发器
CREATE TRIGGER `after_funding_insert_dashboard`
AFTER INSERT ON `FUNDING_RECORD`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COALESCE(SUM(amount), 0) FROM FUNDING_RECORD),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_funding';
END;

-- 资金删除触发器
CREATE TRIGGER `after_funding_delete_dashboard`
AFTER DELETE ON `FUNDING_RECORD`
FOR EACH ROW
BEGIN
    UPDATE STATISTICS_VIEW 
    SET metric_value = (SELECT COALESCE(SUM(amount), 0) FROM FUNDING_RECORD),
        update_date = NOW()
    WHERE dimension_type = 'GLOBAL' 
      AND metric_name = 'total_funding';
END;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 执行完成提示
SELECT '数据库表结构创建完成！' AS message;
