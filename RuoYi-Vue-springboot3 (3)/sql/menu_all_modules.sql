-- ----------------------------
-- 本项目所有模块菜单SQL
-- 说明：包含系统管理、科研管理（院系、教职工、学生、论文、专利、项目）、数据大屏等所有模块的菜单配置
-- 执行前请确保已执行基础表结构初始化（ry_20260320.sql）
-- ----------------------------

-- ----------------------------
-- 一、科研管理一级菜单（ID: 2000）
-- ----------------------------
insert into sys_menu values('2000', '科研管理', '0', '5', 'research', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '科研管理目录');

-- ----------------------------
-- 二、院系管理模块（ID: 2100-2199）
-- ----------------------------
-- 院系管理菜单
insert into sys_menu values('2100', '院系管理', '2000', '1', 'department', 'Dep/department/index', '', '', 1, 0, 'C', '0', '0', 'Dep:department:list', 'tree', 'admin', sysdate(), '', null, '院系管理菜单');

-- 院系管理按钮
insert into sys_menu values('2101', '院系查询', '2100', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2102', '院系新增', '2100', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2103', '院系修改', '2100', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2104', '院系删除', '2100', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2105', '院系导出', '2100', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Dep:department:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 三、教职工管理模块（ID: 2200-2299）
-- ----------------------------
-- 教职工管理菜单
insert into sys_menu values('2200', '教职工管理', '2000', '2', 'faculty', 'Faculty/faculty/index', '', '', 1, 0, 'C', '0', '0', 'Faculty:faculty:list', 'peoples', 'admin', sysdate(), '', null, '教职工管理菜单');

-- 教职工管理按钮
insert into sys_menu values('2201', '教职工查询', '2200', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2202', '教职工新增', '2200', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2203', '教职工修改', '2200', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2204', '教职工删除', '2200', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2205', '教职工导出', '2200', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Faculty:faculty:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 四、学生管理模块（ID: 2300-2399）
-- ----------------------------
-- 学生管理菜单
insert into sys_menu values('2300', '学生管理', '2000', '3', 'student', 'Stu/student/index', '', '', 1, 0, 'C', '0', '0', 'Stu:student:list', 'student', 'admin', sysdate(), '', null, '学生管理菜单');

-- 学生管理按钮
insert into sys_menu values('2301', '学生查询', '2300', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2302', '学生新增', '2300', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2303', '学生修改', '2300', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2304', '学生删除', '2300', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2305', '学生导出', '2300', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Stu:student:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 五、论文管理模块（ID: 2400-2499）
-- ----------------------------
-- 论文管理菜单
insert into sys_menu values('2400', '论文管理', '2000', '4', 'paper', 'Paper/Paper/index', '', '', 1, 0, 'C', '0', '0', 'Paper:Paper:list', 'document', 'admin', sysdate(), '', null, '论文管理菜单');

-- 论文管理按钮
insert into sys_menu values('2401', '论文查询', '2400', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2402', '论文新增', '2400', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2403', '论文修改', '2400', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2404', '论文删除', '2400', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2405', '论文导出', '2400', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Paper:Paper:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 六、专利管理模块（ID: 2500-2599）
-- ----------------------------
-- 专利管理菜单
insert into sys_menu values('2500', '专利管理', '2000', '5', 'patent', 'Patent/Patent/index', '', '', 1, 0, 'C', '0', '0', 'Patent:Patent:list', 'skill', 'admin', sysdate(), '', null, '专利管理菜单');

-- 专利管理按钮
insert into sys_menu values('2501', '专利查询', '2500', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2502', '专利新增', '2500', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2503', '专利修改', '2500', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2504', '专利删除', '2500', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2505', '专利导出', '2500', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Patent:Patent:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 七、项目管理模块（ID: 2600-2699）
-- ----------------------------
-- 项目管理菜单
insert into sys_menu values('2600', '项目管理', '2000', '6', 'project', 'Project/project/index', '', '', 1, 0, 'C', '0', '0', 'Project:project:list', 'job', 'admin', sysdate(), '', null, '项目管理菜单');

-- 项目管理按钮
insert into sys_menu values('2601', '项目查询', '2600', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2602', '项目新增', '2600', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2603', '项目修改', '2600', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2604', '项目删除', '2600', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2605', '项目导出', '2600', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'Project:project:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 八、数据大屏模块（ID: 2700-2799）
-- ----------------------------
-- 数据大屏菜单
insert into sys_menu values('2700', '数据大屏', '0', '6', 'dataScreen', 'dashboard/DataScreen', '', '', 1, 0, 'C', '0', '0', 'dashboard:view', 'chart', 'admin', sysdate(), '', null, '数据大屏菜单');

-- 数据大屏按钮
insert into sys_menu values('2701', '刷新统计', '2700', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'dashboard:statistics:refresh', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 九、更新超级管理员角色（角色ID=1）的菜单权限
-- 说明：将新增的所有科研管理和数据大屏菜单授权给超级管理员
-- ----------------------------
-- 科研管理一级菜单
insert into sys_role_menu values ('1', '2000');

-- 院系管理
insert into sys_role_menu values ('1', '2100');
insert into sys_role_menu values ('1', '2101');
insert into sys_role_menu values ('1', '2102');
insert into sys_role_menu values ('1', '2103');
insert into sys_role_menu values ('1', '2104');
insert into sys_role_menu values ('1', '2105');

-- 教职工管理
insert into sys_role_menu values ('1', '2200');
insert into sys_role_menu values ('1', '2201');
insert into sys_role_menu values ('1', '2202');
insert into sys_role_menu values ('1', '2203');
insert into sys_role_menu values ('1', '2204');
insert into sys_role_menu values ('1', '2205');

-- 学生管理
insert into sys_role_menu values ('1', '2300');
insert into sys_role_menu values ('1', '2301');
insert into sys_role_menu values ('1', '2302');
insert into sys_role_menu values ('1', '2303');
insert into sys_role_menu values ('1', '2304');
insert into sys_role_menu values ('1', '2305');

-- 论文管理
insert into sys_role_menu values ('1', '2400');
insert into sys_role_menu values ('1', '2401');
insert into sys_role_menu values ('1', '2402');
insert into sys_role_menu values ('1', '2403');
insert into sys_role_menu values ('1', '2404');
insert into sys_role_menu values ('1', '2405');

-- 专利管理
insert into sys_role_menu values ('1', '2500');
insert into sys_role_menu values ('1', '2501');
insert into sys_role_menu values ('1', '2502');
insert into sys_role_menu values ('1', '2503');
insert into sys_role_menu values ('1', '2504');
insert into sys_role_menu values ('1', '2505');

-- 项目管理
insert into sys_role_menu values ('1', '2600');
insert into sys_role_menu values ('1', '2601');
insert into sys_role_menu values ('1', '2602');
insert into sys_role_menu values ('1', '2603');
insert into sys_role_menu values ('1', '2604');
insert into sys_role_menu values ('1', '2605');

-- 数据大屏
insert into sys_role_menu values ('1', '2700');
insert into sys_role_menu values ('1', '2701');

-- ----------------------------
-- 十、更新普通角色（角色ID=2）的菜单权限
-- 说明：将新增的所有科研管理和数据大屏菜单授权给普通角色
-- ----------------------------
-- 科研管理一级菜单
insert into sys_role_menu values ('2', '2000');

-- 院系管理
insert into sys_role_menu values ('2', '2100');
insert into sys_role_menu values ('2', '2101');
insert into sys_role_menu values ('2', '2102');
insert into sys_role_menu values ('2', '2103');
insert into sys_role_menu values ('2', '2104');
insert into sys_role_menu values ('2', '2105');

-- 教职工管理
insert into sys_role_menu values ('2', '2200');
insert into sys_role_menu values ('2', '2201');
insert into sys_role_menu values ('2', '2202');
insert into sys_role_menu values ('2', '2203');
insert into sys_role_menu values ('2', '2204');
insert into sys_role_menu values ('2', '2205');

-- 学生管理
insert into sys_role_menu values ('2', '2300');
insert into sys_role_menu values ('2', '2301');
insert into sys_role_menu values ('2', '2302');
insert into sys_role_menu values ('2', '2303');
insert into sys_role_menu values ('2', '2304');
insert into sys_role_menu values ('2', '2305');

-- 论文管理
insert into sys_role_menu values ('2', '2400');
insert into sys_role_menu values ('2', '2401');
insert into sys_role_menu values ('2', '2402');
insert into sys_role_menu values ('2', '2403');
insert into sys_role_menu values ('2', '2404');
insert into sys_role_menu values ('2', '2405');

-- 专利管理
insert into sys_role_menu values ('2', '2500');
insert into sys_role_menu values ('2', '2501');
insert into sys_role_menu values ('2', '2502');
insert into sys_role_menu values ('2', '2503');
insert into sys_role_menu values ('2', '2504');
insert into sys_role_menu values ('2', '2505');

-- 项目管理
insert into sys_role_menu values ('2', '2600');
insert into sys_role_menu values ('2', '2601');
insert into sys_role_menu values ('2', '2602');
insert into sys_role_menu values ('2', '2603');
insert into sys_role_menu values ('2', '2604');
insert into sys_role_menu values ('2', '2605');

-- 数据大屏
insert into sys_role_menu values ('2', '2700');
insert into sys_role_menu values ('2', '2701');

-- ----------------------------
-- 执行完成提示
-- ----------------------------
-- 菜单SQL已执行完成！
-- 请刷新浏览器或重新登录系统以查看新菜单。
-- 如需调整菜单权限，可在系统管理->角色管理中进行配置。
