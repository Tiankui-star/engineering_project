-- ============================================================
-- 学院管理平台 - 完整初始化脚本 (init_data.sql)
--
-- 执行顺序（严格按序执行）：
--   1. ry_20260320.sql                  (若依框架基础表 + 内置数据)
--   2. mycreatetables_compatible.sql    (业务表 + 触发器)
--   3. 本脚本 init_data.sql             (菜单 / 角色权限 / 测试业务数据)
--
-- 测试账号（密码统一：123456）：
--   超级管理员 : admin        / admin123（若依默认）
--   学院管理员 : 请在系统中手动创建并分配 college_admin 角色
--   教职工账号 : 2021001001  / 123456  (张威，工号即登录账号)
--              : 2021001002  / 123456  (李芳)
--              : 2021001003  / 123456  (王军)
--   学生账号   : 2021010101  / 123456  (张明，学号即登录账号)
--              : 2021010102  / 123456  (李红)
--              : 2022010101  / 123456  (王强)
--
-- 脚本特性：全程幂等，可反复执行
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ============================================================
-- PART 1：业务菜单配置
-- 幂等处理：先清除 ID 2100~2799 范围内的旧数据，再重新插入
-- ============================================================

-- 1.1 清理旧业务菜单的角色权限关联
DELETE FROM sys_role_menu WHERE menu_id BETWEEN 2100 AND 2799;

-- 1.2 清理旧业务菜单
DELETE FROM sys_menu WHERE menu_id BETWEEN 2100 AND 2799;

-- 1.3 插入业务菜单 -----------------------------------------------

-- 【院系管理】一级菜单 (ID: 2100-2105)
INSERT INTO sys_menu VALUES('2100', '院系管理',   '0',    '5',  'department', 'Dep/department/index',    '', '', 1, 0, 'C', '0', '0', 'Dep:department:list',   'tree',    'admin', sysdate(), '', null, '院系管理菜单');
INSERT INTO sys_menu VALUES('2101', '院系查询',   '2100', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2102', '院系新增',   '2100', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2103', '院系修改',   '2100', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2104', '院系删除',   '2100', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2105', '院系导出',   '2100', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:export', '#', 'admin', sysdate(), '', null, '');

-- 【教职工管理】一级菜单 (ID: 2200-2205)
INSERT INTO sys_menu VALUES('2200', '教职工管理', '0',    '6',  'faculty',    'Faculty/faculty/index',   '', '', 1, 0, 'C', '0', '0', 'Faculty:faculty:list',   'peoples', 'admin', sysdate(), '', null, '教职工管理菜单');
INSERT INTO sys_menu VALUES('2201', '教职工查询', '2200', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2202', '教职工新增', '2200', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2203', '教职工修改', '2200', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2204', '教职工删除', '2200', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2205', '教职工导出', '2200', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:export', '#', 'admin', sysdate(), '', null, '');

-- 【学生管理】一级菜单 (ID: 2300-2305)
INSERT INTO sys_menu VALUES('2300', '学生管理',   '0',    '7',  'student',    'Stu/student/index',       '', '', 1, 0, 'C', '0', '0', 'Stu:student:list',   'student', 'admin', sysdate(), '', null, '学生管理菜单');
INSERT INTO sys_menu VALUES('2301', '学生查询',   '2300', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2302', '学生新增',   '2300', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2303', '学生修改',   '2300', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2304', '学生删除',   '2300', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2305', '学生导出',   '2300', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:export', '#', 'admin', sysdate(), '', null, '');

-- 【论文管理】一级菜单 (ID: 2400-2405)
INSERT INTO sys_menu VALUES('2400', '论文管理',   '0',    '8',  'paper',      'Paper/Paper/index',       '', '', 1, 0, 'C', '0', '0', 'Paper:Paper:list',   'document', 'admin', sysdate(), '', null, '论文管理菜单');
INSERT INTO sys_menu VALUES('2401', '论文查询',   '2400', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2402', '论文新增',   '2400', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2403', '论文修改',   '2400', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2404', '论文删除',   '2400', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2405', '论文导出',   '2400', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:export', '#', 'admin', sysdate(), '', null, '');

-- 【专利管理】一级菜单 (ID: 2500-2505)
INSERT INTO sys_menu VALUES('2500', '专利管理',   '0',    '9',  'patent',     'Patent/Patent/index',     '', '', 1, 0, 'C', '0', '0', 'Patent:Patent:list',   'skill',  'admin', sysdate(), '', null, '专利管理菜单');
INSERT INTO sys_menu VALUES('2501', '专利查询',   '2500', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2502', '专利新增',   '2500', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2503', '专利修改',   '2500', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2504', '专利删除',   '2500', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2505', '专利导出',   '2500', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:export', '#', 'admin', sysdate(), '', null, '');

-- 【项目管理】一级菜单 (ID: 2600-2605)
INSERT INTO sys_menu VALUES('2600', '项目管理',   '0',    '10', 'project',    'Project/project/index',   '', '', 1, 0, 'C', '0', '0', 'Project:project:list',   'job',  'admin', sysdate(), '', null, '项目管理菜单');
INSERT INTO sys_menu VALUES('2601', '项目查询',   '2600', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:query',  '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2602', '项目新增',   '2600', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:add',    '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2603', '项目修改',   '2600', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:edit',   '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2604', '项目删除',   '2600', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES('2605', '项目导出',   '2600', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:export', '#', 'admin', sysdate(), '', null, '');

-- 【数据大屏】一级菜单 (ID: 2700-2701)
INSERT INTO sys_menu VALUES('2700', '数据大屏',   '0',    '11', 'dataScreen', 'dashboard/DataScreen',    '', '', 1, 0, 'C', '0', '0', 'dashboard:view',               'chart', 'admin', sysdate(), '', null, '数据大屏菜单');
INSERT INTO sys_menu VALUES('2701', '刷新统计',   '2700', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'dashboard:statistics:refresh', '#',   'admin', sysdate(), '', null, '');

-- 1.4 超级管理员 (role_id=1) 业务菜单授权
INSERT INTO sys_role_menu VALUES
  ('1','2100'),('1','2101'),('1','2102'),('1','2103'),('1','2104'),('1','2105'),
  ('1','2200'),('1','2201'),('1','2202'),('1','2203'),('1','2204'),('1','2205'),
  ('1','2300'),('1','2301'),('1','2302'),('1','2303'),('1','2304'),('1','2305'),
  ('1','2400'),('1','2401'),('1','2402'),('1','2403'),('1','2404'),('1','2405'),
  ('1','2500'),('1','2501'),('1','2502'),('1','2503'),('1','2504'),('1','2505'),
  ('1','2600'),('1','2601'),('1','2602'),('1','2603'),('1','2604'),('1','2605'),
  ('1','2700'),('1','2701');

-- 1.5 普通角色 (role_id=2) 业务菜单授权
INSERT INTO sys_role_menu VALUES
  ('2','2100'),('2','2101'),('2','2102'),('2','2103'),('2','2104'),('2','2105'),
  ('2','2200'),('2','2201'),('2','2202'),('2','2203'),('2','2204'),('2','2205'),
  ('2','2300'),('2','2301'),('2','2302'),('2','2303'),('2','2304'),('2','2305'),
  ('2','2400'),('2','2401'),('2','2402'),('2','2403'),('2','2404'),('2','2405'),
  ('2','2500'),('2','2501'),('2','2502'),('2','2503'),('2','2504'),('2','2505'),
  ('2','2600'),('2','2601'),('2','2602'),('2','2603'),('2','2604'),('2','2605'),
  ('2','2700'),('2','2701');


-- ============================================================
-- PART 2：自定义角色与权限配置
-- 角色说明：
--   role_id=3  学院管理员(college_admin)  - 系统管理核心 + 业务全权 + 数据大屏
--   role_id=4  学院领导(college_leader)   - 所有模块只读 + 导出 + 数据大屏
--   role_id=5  教职工(college_teacher)    - 论文/专利/项目增删改查 + 师生只读
--   role_id=6  学生(college_student)      - 学生档案查改 + 论文/专利/项目增改查
-- ============================================================

-- 2.1 幂等清理（按外键依赖顺序）
DELETE FROM sys_role_menu WHERE role_id IN (3, 4, 5, 6);
DELETE FROM sys_user_role   WHERE role_id IN (3, 4, 5, 6);
DELETE FROM sys_role        WHERE role_id IN (3, 4, 5, 6);

-- 2.2 创建四个业务角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
VALUES
  (3, '学院管理员', 'college_admin',   3, '1', 1, 1, '0', '0', 'admin', sysdate(), '学院管理员：管理全平台业务数据及用户账号'),
  (4, '学院领导',   'college_leader',  4, '1', 1, 1, '0', '0', 'admin', sysdate(), '学院领导：只读查看全部数据，可导出，核心是数据大屏'),
  (5, '教职工',     'college_teacher', 5, '1', 1, 1, '0', '0', 'admin', sysdate(), '教职工：管理自己参与的论文、专利、项目'),
  (6, '学生',       'college_student', 6, '1', 1, 1, '0', '0', 'admin', sysdate(), '学生：查看并维护自己参与的论文、专利、项目及学生档案');

-- 2.3 学院管理员(3)：系统管理核心权限（若依原生固定ID）
INSERT INTO sys_role_menu VALUES
  (3,1),(3,100),(3,1000),(3,1001),(3,1002),(3,1003),(3,1004),(3,1005),(3,1006),
  (3,101),(3,1007),(3,1008),(3,1009),(3,1010),(3,1011),
  (3,105),(3,1025),(3,1026),(3,1027),(3,1028),(3,1029),
  (3,108),(3,500),(3,1039),(3,1040),(3,1041),(3,501),(3,1042),(3,1043),(3,1044),(3,1045);

-- 学院管理员(3)：业务全权 + 数据大屏（动态按 perms 查找，不依赖固定 menu_id）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE perms IN (
  'Dep:department:list','Dep:department:query','Dep:department:add','Dep:department:edit','Dep:department:remove','Dep:department:export',
  'Faculty:faculty:list','Faculty:faculty:query','Faculty:faculty:add','Faculty:faculty:edit','Faculty:faculty:remove','Faculty:faculty:export',
  'Stu:student:list','Stu:student:query','Stu:student:add','Stu:student:edit','Stu:student:remove','Stu:student:export',
  'Paper:Paper:list','Paper:Paper:query','Paper:Paper:add','Paper:Paper:edit','Paper:Paper:remove','Paper:Paper:export',
  'Patent:Patent:list','Patent:Patent:query','Patent:Patent:add','Patent:Patent:edit','Patent:Patent:remove','Patent:Patent:export',
  'Project:project:list','Project:project:query','Project:project:add','Project:project:edit','Project:project:remove','Project:project:export',
  'dashboard:view','dashboard:statistics:refresh'
);

-- 2.4 学院领导(4)：所有模块只读+导出 + 数据大屏
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 4, menu_id FROM sys_menu WHERE perms IN (
  'Dep:department:list','Dep:department:query','Dep:department:export',
  'Faculty:faculty:list','Faculty:faculty:query','Faculty:faculty:export',
  'Stu:student:list','Stu:student:query','Stu:student:export',
  'Paper:Paper:list','Paper:Paper:query','Paper:Paper:export',
  'Patent:Patent:list','Patent:Patent:query','Patent:Patent:export',
  'Project:project:list','Project:project:query','Project:project:export',
  'dashboard:view','dashboard:statistics:refresh'
);

-- 2.5 教职工(5)：论文/专利/项目增删改查 + 教职工修改自己 + 学生只读
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 5, menu_id FROM sys_menu WHERE perms IN (
  'Faculty:faculty:list','Faculty:faculty:query','Faculty:faculty:edit',
  'Stu:student:list','Stu:student:query',
  'Paper:Paper:list','Paper:Paper:query','Paper:Paper:add','Paper:Paper:edit','Paper:Paper:remove','Paper:Paper:export',
  'Patent:Patent:list','Patent:Patent:query','Patent:Patent:add','Patent:Patent:edit','Patent:Patent:remove','Patent:Patent:export',
  'Project:project:list','Project:project:query','Project:project:add','Project:project:edit','Project:project:remove','Project:project:export'
);

-- 2.6 学生(6)：学生档案查改 + 论文/专利/项目增改查（不含删除/导出）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 6, menu_id FROM sys_menu WHERE perms IN (
  'Stu:student:list','Stu:student:query','Stu:student:edit',
  'Paper:Paper:list','Paper:Paper:query','Paper:Paper:add','Paper:Paper:edit',
  'Patent:Patent:list','Patent:Patent:query','Patent:Patent:add','Patent:Patent:edit',
  'Project:project:list','Project:project:query','Project:project:add','Project:project:edit'
);


-- ============================================================
-- PART 3：测试系统用户账号
-- 3 名教职工账号 (user_id: 200-202) + 3 名学生账号 (user_id: 210-212)
-- 密码统一：admin123
-- BCrypt Hash: $2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2
-- ============================================================

-- 3.1 幂等清理
DELETE FROM sys_user_role WHERE user_id IN (200, 201, 202, 210, 211, 212);
DELETE FROM sys_user      WHERE user_id IN (200, 201, 202, 210, 211, 212);

-- 3.2 插入教职工系统用户
-- 字段顺序: user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex,
--           avatar, password, status, del_flag, login_ip, login_date, pwd_update_date,
--           create_by, create_time, update_by, update_time, remark
-- 密码 BCrypt Hash（123456）: $2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG
INSERT INTO sys_user VALUES
  (200, 103, '2021001001', '张威', '00', 'zhangwei@college.edu.cn',  '13800000101', '0', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试教职工-张威'),
  (201, 103, '2021001002', '李芳', '00', 'lifang@college.edu.cn',    '13800000102', '1', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试教职工-李芳'),
  (202, 103, '2021001003', '王军', '00', 'wangjun@college.edu.cn',   '13800000103', '0', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试教职工-王军');

-- 3.3 插入学生系统用户
INSERT INTO sys_user VALUES
  (210, 105, '2021010101', '张明', '00', 'zhangming@student.edu.cn',  '13900000101', '0', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试学生-张明'),
  (211, 105, '2021010102', '李红', '00', 'lihong@student.edu.cn',     '13900000102', '1', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试学生-李红'),
  (212, 105, '2022010101', '王强', '00', 'wangqiang@student.edu.cn',  '13900000103', '0', '',
   '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAuG',
   '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试学生-王强');

-- 3.4 分配角色：教职工 → college_teacher(5)，学生 → college_student(6)
INSERT INTO sys_user_role (user_id, role_id) VALUES
  (200, 5), (201, 5), (202, 5),
  (210, 6), (211, 6), (212, 6);


-- ============================================================
-- PART 4：业务测试数据
--
-- 触发器注意事项（mycreatetables_compatible.sql 中已定义）：
--   ① INSERT RESEARCH_PROJECT  → 自动向 PROJECT_MEMBER 插入 LEADER 并 project_count+1
--   ② INSERT PROJECT_MEMBER    → role_type != LEADER 时 project_count+1
--   ③ INSERT PAPER_AUTHOR      → 对应 FACULTY/STUDENT.paper_count+1
--   ④ INSERT PATENT_AUTHOR     → 对应 FACULTY/STUDENT.patent_count+1
--   ⑤ INSERT STUDENT_AWARD     → 对应 STUDENT.award_count+1
--   ⑥ INSERT FUNDING_RECORD    → 对应项目负责人(FACULTY).total_funding+金额
-- 因此所有冗余计数字段初始设为 0，由触发器自动维护，无需手动赋值。
-- ============================================================

-- 4.0 幂等清理（按外键依赖逆序删除）
DELETE FROM PROJECT_OUTPUT;
DELETE FROM FUNDING_RECORD;
DELETE FROM PROJECT_MEMBER;
DELETE FROM RESEARCH_PROJECT;
DELETE FROM STUDENT_AWARD;
DELETE FROM PATENT_AUTHOR;
DELETE FROM PATENT;
DELETE FROM PAPER_AUTHOR;
DELETE FROM ACADEMIC_PAPER;
DELETE FROM STUDENT;
DELETE FROM FACULTY;
DELETE FROM DEPARTMENT;


-- ============================================================
-- 4.1 院系数据（DEPARTMENT）
-- head_faculty_id 先置 NULL，FACULTY 插入完毕后再 UPDATE
-- ============================================================
INSERT INTO DEPARTMENT (department_id, department_code, department_name, head_faculty_id, description) VALUES
  (1, 'CS01', '计算机科学与技术学院', NULL,
   '承担计算机科学与技术、软件工程、人工智能等专业的教学与科研工作，设有人工智能、大数据与云计算等重点实验室。'),
  (2, 'EE01', '电子工程学院', NULL,
   '承担电子信息工程、通信工程等专业的教学与科研工作，设有嵌入式系统与物联网、无线通信技术等研究方向。'),
  (3, 'MA01', '数学与统计学院', NULL,
   '承担数学、统计学等基础学科的教学科研工作，为全院理工科专业提供公共数学课程教学支撑。');


-- ============================================================
-- 4.2 教职工数据（FACULTY）
-- 冗余计数字段全部初始化为 0，由触发器维护
-- ============================================================
INSERT INTO FACULTY (faculty_id, faculty_number, name, title, research_field,
                     department_id, employment_date, status,
                     paper_count, patent_count, project_count, total_funding)
VALUES
  (200, '2021001001', '张威', '教授',   '人工智能与机器学习', 1, '2018-09-01', 'ACTIVE', 0, 0, 0, 0.00),
  (201, '2021001002', '李芳', '副教授', '网络安全与密码学',   1, '2019-03-01', 'ACTIVE', 0, 0, 0, 0.00),
  (202, '2021001003', '王军', '讲师',   '嵌入式系统与物联网', 2, '2020-07-01', 'ACTIVE', 0, 0, 0, 0.00);


-- ============================================================
-- 4.3 更新院系负责人
-- ============================================================
UPDATE DEPARTMENT SET head_faculty_id = 200 WHERE department_id = 1;
UPDATE DEPARTMENT SET head_faculty_id = 202 WHERE department_id = 2;


-- ============================================================
-- 4.4 学生数据（STUDENT）
-- ============================================================
INSERT INTO STUDENT (student_id, student_number, name, birth_date, gender, id_card,
                     department_id, enrollment_date, status,
                     award_count, paper_count, patent_count, project_count)
VALUES
  (210, '2021010101', '张明', '2002-05-15', 'M', '110101200205150011', 1, '2021-09-01', 'ENROLLED', 0, 0, 0, 0),
  (211, '2021010102', '李红', '2002-11-22', 'F', '110101200211220026', 1, '2021-09-01', 'ENROLLED', 0, 0, 0, 0),
  (212, '2022010101', '王强', '2003-03-08', 'M', '110101200303080033', 2, '2022-09-01', 'ENROLLED', 0, 0, 0, 0);


-- ============================================================
-- 4.5 论文数据（ACADEMIC_PAPER）
-- ============================================================
INSERT INTO ACADEMIC_PAPER (paper_id, paper_title, abstract, journal_name, volume_issue, page_numbers,
                             publication_date, publication_level, doi, citation_count,
                             first_author_type, first_author_id, department_id)
VALUES
  (1,
   '基于深度学习的图像识别方法研究',
   '本文提出一种改进的卷积神经网络结构，用于提升复杂场景下图像分类任务的精度与效率。通过在公开标准数据集上的对比实验，所提方法在 Top-1 准确率方面较基线模型提升了 3.2 个百分点，推理延迟降低 18%。',
   '计算机学报', '2022(45)', '1023-1034', '2022-06-15', 'SCI',
   '10.13328/j.cnki.jos.2022.001', 12, 'FACULTY', 200, 1),
  (2,
   '区块链技术在教育数据安全共享中的应用研究',
   '针对教育数据共享场景中隐私泄露与数据篡改风险，本文设计了基于联盟链的教育数据安全共享框架，通过智能合约实现细粒度访问控制，实验表明该框架能有效保障数据完整性并满足合规要求。',
   '软件学报', '2023(34)', '456-469', '2023-02-20', 'EI',
   '10.13328/j.cnki.jos.2023.002', 7, 'FACULTY', 201, 1),
  (3,
   '基于 FPGA 的低功耗图像边缘检测算法设计与实现',
   '本文基于 FPGA 平台设计了一种低功耗图像边缘检测加速算法，采用并行流水线结构将处理吞吐量提升 40%，与同等软件实现相比功耗降低约 25%，适用于边缘计算与嵌入式视觉场景。',
   '电子学报', '2023(51)', '789-798', '2023-07-10', 'CORE_B',
   '10.3969/j.issn.0372-2112.2023.003', 3, 'STUDENT', 210, 1);


-- ============================================================
-- 4.6 论文作者关系（PAPER_AUTHOR）
-- 触发器 after_paper_author_insert 将自动更新 FACULTY/STUDENT.paper_count
-- ============================================================
INSERT INTO PAPER_AUTHOR (paper_id, author_type, author_id, author_order, contribution_description)
VALUES
  -- 论文1：张威(1st) / 李芳(2nd) / 张明-学生(3rd)
  (1, 'FACULTY', 200, 1, '提出研究方法，完成主体实验设计与论文撰写'),
  (1, 'FACULTY', 201, 2, '参与实验设计，负责数据分析与结果验证'),
  (1, 'STUDENT', 210, 3, '协助实验执行与实验数据整理'),
  -- 论文2：李芳(1st) / 张威(2nd)
  (2, 'FACULTY', 201, 1, '提出框架设计，完成主体研究与系统实现'),
  (2, 'FACULTY', 200, 2, '参与系统优化与安全性分析'),
  -- 论文3：张明-学生(1st) / 张威(2nd-指导)
  (3, 'STUDENT', 210, 1, '完成算法设计与 FPGA 全流程实现'),
  (3, 'FACULTY', 200, 2, '提供研究方向指导与论文修改意见');


-- ============================================================
-- 4.7 专利数据（PATENT）
-- ============================================================
INSERT INTO PATENT (patent_id, patent_code, patent_name, abstract,
                    patent_type, patent_level,
                    application_date, grant_date, expiry_date, patent_status,
                    first_author_type, first_author_id, department_id, attachment_url)
VALUES
  (1,
   'ZL202110001001.1',
   '一种基于卷积神经网络的图像识别与分类装置',
   '本发明涉及一种基于卷积神经网络的图像识别与分类装置，通过改进的多尺度特征提取模块与轻量化网络结构，显著提升了复杂背景下图像识别的速度与精度，适用于工业质检、安防监控等领域。',
   'INVENTION', 'NATIONAL', '2021-03-15', '2023-01-20', '2041-03-15', 'GRANTED',
   'FACULTY', 200, 1, NULL),
  (2,
   'ZL202120001001.2',
   '一种低功耗物联网无线通信模块',
   '本实用新型提供了一种低功耗物联网无线通信模块，采用自适应帧聚合与动态功率控制技术，在保证通信可靠性的前提下将整体功耗降低约 60%，适用于智能楼宇、工业自动化等场景。',
   'UTILITY_MODEL', 'NATIONAL', '2021-06-01', '2022-08-10', '2031-06-01', 'GRANTED',
   'FACULTY', 202, 2, NULL),
  (3,
   'ZL202210001001.3',
   '一种基于微服务架构的智能校园信息管理系统',
   '本实用新型提出了一种基于微服务架构的智能校园信息管理系统，实现学生档案、教学管理、科研成果等核心业务的统一管理与数据互通，具备高可用、水平扩展的特点。',
   'UTILITY_MODEL', 'PROVINCIAL', '2022-04-20', NULL, NULL, 'EXAMINING',
   'STUDENT', 210, 1, NULL);


-- ============================================================
-- 4.8 专利作者关系（PATENT_AUTHOR）
-- 触发器 after_patent_author_insert 将自动更新 FACULTY/STUDENT.patent_count
-- ============================================================
INSERT INTO PATENT_AUTHOR (patent_id, author_type, author_id, author_order, contribution_percent)
VALUES
  -- 专利1：张威(1st 60%) / 李芳(2nd 40%)
  (1, 'FACULTY', 200, 1, 60.00),
  (1, 'FACULTY', 201, 2, 40.00),
  -- 专利2：王军(1st 70%) / 王强-学生(2nd 30%)
  (2, 'FACULTY', 202, 1, 70.00),
  (2, 'STUDENT', 212, 2, 30.00),
  -- 专利3：张明-学生(1st 70%) / 张威(2nd-指导 30%)
  (3, 'STUDENT', 210, 1, 70.00),
  (3, 'FACULTY', 200, 2, 30.00);


-- ============================================================
-- 4.9 科研项目数据（RESEARCH_PROJECT）
-- 触发器 after_project_insert 将自动：
--   ① 向 PROJECT_MEMBER 插入负责人（role_type='LEADER'）
--   ② 负责人 project_count+1
-- ============================================================
INSERT INTO RESEARCH_PROJECT (project_id, project_code, project_name, description,
                               start_date, end_date, project_level, project_status,
                               leader_type, leader_id, department_id)
VALUES
  (1,
   'NSFC2021001', '深度学习图像识别关键技术研究',
   '本项目面向复杂场景下的高精度图像识别需求，研究轻量化深度学习模型设计方法、迁移学习与多模态特征融合技术，在目标检测与语义分割等核心任务上取得突破，成果已发表 SCI 论文并获得发明专利授权。',
   '2021-01-01', '2023-12-31', 'NATIONAL', 'COMPLETED', 'FACULTY', 200, 1),
  (2,
   'PROV2022001', '面向零信任架构的网络安全主动防御系统研究',
   '本项目研究基于零信任安全模型的动态访问控制策略，构建具备威胁感知与自适应响应能力的主动防御体系，提升关键信息基础设施的安全防护水平，成果已发表 EI 论文1篇。',
   '2022-03-01', '2024-02-28', 'PROVINCIAL', 'ONGOING', 'FACULTY', 201, 1),
  (3,
   'CORP2022001', '智能工厂物联网平台建设与应用',
   '本项目与深圳智能科技有限公司合作，面向智能制造场景构建基于边缘计算的物联网数据采集、传输与分析平台，实现生产设备实时监控、异常预警及能耗优化，已完成交付并投入生产使用。',
   '2022-06-01', '2023-06-30', 'ENTERPRISE', 'COMPLETED', 'FACULTY', 202, 2);


-- ============================================================
-- 4.10 项目成员追加（PROJECT_MEMBER）
-- 负责人已由上方触发器自动插入，此处仅追加非负责人成员
-- 触发器 after_project_member_insert 将自动更新非负责人成员的 project_count
-- ============================================================
INSERT INTO PROJECT_MEMBER (project_id, member_type, user_id, role_type, role_description, join_date, contribution_percent)
VALUES
  -- 项目1 追加成员
  (1, 'FACULTY', 201, 'PARTICIPANT', '负责系统实现与算法优化',    '2021-01-01', 25.00),
  (1, 'STUDENT', 210, 'PARTICIPANT', '负责实验数据采集与处理',    '2021-03-01', 15.00),
  (1, 'STUDENT', 211, 'PARTICIPANT', '负责论文撰写与文献调研',    '2021-03-01', 10.00),
  -- 项目2 追加成员
  (2, 'FACULTY', 200, 'ADVISOR',     '提供人工智能技术顾问支持',  '2022-03-01', 15.00),
  (2, 'STUDENT', 211, 'PARTICIPANT', '协助系统测试与安全评估',    '2022-04-01', 15.00),
  -- 项目3 追加成员
  (3, 'FACULTY', 201, 'PARTICIPANT', '负责数据分析模块开发',      '2022-06-01', 20.00),
  (3, 'STUDENT', 212, 'PARTICIPANT', '负责前端可视化界面开发',    '2022-06-01', 15.00);


-- ============================================================
-- 4.11 资金记录（FUNDING_RECORD）
-- 触发器 after_funding_insert 将自动更新项目负责人(FACULTY).total_funding
--   项目1负责人张威(200)：500000 + 100000 = 600000
--   项目2负责人李芳(201)：200000 +  50000 = 250000
--   项目3负责人王军(202)：1200000
-- ============================================================
INSERT INTO FUNDING_RECORD (project_id, amount, allocation_date, funding_source, funding_type, status)
VALUES
  -- 项目1 资金
  (1,  500000.00, '2021-02-01', '国家自然科学基金委员会', 'GRANT',      'RECEIVED'),
  (1,  100000.00, '2021-02-15', '学校配套科研经费',       'MATCHING',   'RECEIVED'),
  -- 项目2 资金
  (2,  200000.00, '2022-04-01', '省科学技术厅',           'GRANT',      'RECEIVED'),
  (2,   50000.00, '2022-04-15', '学校配套科研经费',       'MATCHING',   'RECEIVED'),
  -- 项目3 资金
  (3, 1200000.00, '2022-07-01', '深圳智能科技有限公司',   'SPONSORSHIP','RECEIVED');


-- ============================================================
-- 4.12 学生获奖数据（STUDENT_AWARD）
-- 触发器 after_award_insert 将自动更新 STUDENT.award_count
--   张明(210)：2 项; 李红(211)：1 项; 王强(212)：1 项
-- ============================================================
INSERT INTO STUDENT_AWARD (award_name, description, award_date, award_level,
                            issuing_organization, student_id, supervisor_faculty_id)
VALUES
  ('全国大学生计算机设计大赛一等奖',
   '参赛作品：基于 FPGA 的智能图像识别系统，荣获 2022 年全国大学生计算机设计大赛一等奖。',
   '2022-08-20', 'NATIONAL', '教育部高等学校计算机类专业教学指导委员会', 210, 200),
  ('广东省"挑战杯"大学生创新创业大赛优秀奖',
   '参赛项目：面向中小企业的轻量化网络安全解决方案，荣获省级优秀奖。',
   '2023-05-15', 'PROVINCIAL', '共青团广东省委员会', 211, 201),
  ('全国大学生数学建模竞赛二等奖',
   '参赛题目：物联网设备能耗优化模型建立与求解，荣获全国二等奖。',
   '2023-09-25', 'NATIONAL', '中国工业与应用数学学会', 212, 202),
  ('校级优秀毕业设计（论文）',
   '毕业设计题目：基于深度学习的实时目标检测系统设计，被评为校级优秀毕业设计。',
   '2024-06-10', 'UNIVERSITY', '学院教务处', 210, 200);


-- ============================================================
-- 4.13 项目成果关联（PROJECT_OUTPUT）
-- ============================================================
INSERT INTO PROJECT_OUTPUT (project_id, output_type, output_id, output_date, contribution_description)
VALUES
  (1, 'PAPER',  1, '2022-06-15', '项目一期成果，发表 SCI 论文 1 篇'),
  (1, 'PATENT', 1, '2023-01-20', '项目二期核心成果，获得发明专利授权'),
  (2, 'PAPER',  2, '2023-02-20', '项目研究成果，发表 EI 论文 1 篇'),
  (3, 'PATENT', 2, '2022-08-10', '项目核心技术成果，获得实用新型专利授权');


-- ============================================================
-- PART 5：数据大屏统计表初始化（STATISTICS_VIEW）
-- 在所有业务数据就绪后统一重算，确保数值准确
-- ============================================================
DELETE FROM STATISTICS_VIEW WHERE dimension_type = 'GLOBAL';

INSERT INTO STATISTICS_VIEW (dimension_type, dimension_id, metric_name, metric_value, update_date)
VALUES
  ('GLOBAL', NULL, 'total_faculty',  (SELECT COUNT(*)                FROM FACULTY),         NOW()),
  ('GLOBAL', NULL, 'total_student',  (SELECT COUNT(*)                FROM STUDENT),          NOW()),
  ('GLOBAL', NULL, 'total_paper',    (SELECT COUNT(*)                FROM ACADEMIC_PAPER),   NOW()),
  ('GLOBAL', NULL, 'total_patent',   (SELECT COUNT(*)                FROM PATENT),           NOW()),
  ('GLOBAL', NULL, 'total_project',  (SELECT COUNT(*)                FROM RESEARCH_PROJECT), NOW()),
  ('GLOBAL', NULL, 'total_award',    (SELECT COUNT(*)                FROM STUDENT_AWARD),    NOW()),
  ('GLOBAL', NULL, 'total_funding',  (SELECT COALESCE(SUM(amount),0) FROM FUNDING_RECORD),   NOW());


-- ============================================================
-- 收尾：恢复外键检查，重置自增起点
-- 将所有自增主键设为 100+，避免与测试数据冲突
-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE DEPARTMENT       AUTO_INCREMENT = 100;
ALTER TABLE ACADEMIC_PAPER   AUTO_INCREMENT = 100;
ALTER TABLE RESEARCH_PROJECT AUTO_INCREMENT = 100;
ALTER TABLE PATENT           AUTO_INCREMENT = 100;
ALTER TABLE STUDENT_AWARD    AUTO_INCREMENT = 100;
ALTER TABLE PAPER_AUTHOR     AUTO_INCREMENT = 100;
ALTER TABLE PATENT_AUTHOR    AUTO_INCREMENT = 100;
ALTER TABLE PROJECT_MEMBER   AUTO_INCREMENT = 100;
ALTER TABLE FUNDING_RECORD   AUTO_INCREMENT = 100;
ALTER TABLE PROJECT_OUTPUT   AUTO_INCREMENT = 100;
ALTER TABLE sys_user         AUTO_INCREMENT = 1000;
ALTER TABLE sys_role         AUTO_INCREMENT = 100;


-- ============================================================
-- 可选验证查询（取消注释后执行）
-- ============================================================
-- SELECT tbl, cnt FROM (
--   SELECT 'DEPARTMENT'       tbl, COUNT(*) cnt FROM DEPARTMENT       UNION ALL
--   SELECT 'FACULTY',               COUNT(*) cnt FROM FACULTY               UNION ALL
--   SELECT 'STUDENT',               COUNT(*) cnt FROM STUDENT               UNION ALL
--   SELECT 'ACADEMIC_PAPER',        COUNT(*) cnt FROM ACADEMIC_PAPER        UNION ALL
--   SELECT 'PATENT',                COUNT(*) cnt FROM PATENT                UNION ALL
--   SELECT 'RESEARCH_PROJECT',      COUNT(*) cnt FROM RESEARCH_PROJECT      UNION ALL
--   SELECT 'PROJECT_MEMBER',        COUNT(*) cnt FROM PROJECT_MEMBER        UNION ALL
--   SELECT 'FUNDING_RECORD',        COUNT(*) cnt FROM FUNDING_RECORD        UNION ALL
--   SELECT 'STUDENT_AWARD',         COUNT(*) cnt FROM STUDENT_AWARD         UNION ALL
--   SELECT 'PROJECT_OUTPUT',        COUNT(*) cnt FROM PROJECT_OUTPUT        UNION ALL
--   SELECT 'STATISTICS_VIEW',       COUNT(*) cnt FROM STATISTICS_VIEW
-- ) t;
-- 
-- -- 验证冗余计数是否由触发器正确维护
-- SELECT name, paper_count, patent_count, project_count, total_funding FROM FACULTY;
-- SELECT name, award_count, paper_count, patent_count, project_count  FROM STUDENT;
-- 
-- -- 验证数据大屏统计
-- SELECT metric_name, metric_value FROM STATISTICS_VIEW WHERE dimension_type='GLOBAL' ORDER BY metric_name;
-- 
-- -- 验证角色菜单数量
-- SELECT r.role_name, COUNT(rm.menu_id) menu_count
-- FROM sys_role r LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
-- WHERE r.role_id IN (3,4,5,6) GROUP BY r.role_id, r.role_name;
