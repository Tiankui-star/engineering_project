# 学院科研管理平台

基于 **Spring Boot 3 + Vue 2 + Element UI** 的前后端分离学院管理系统，集成院系信息、教职工档案、学生管理、论文/专利/科研项目全流程管理及数据大屏可视化。底层基于 [若依（RuoYi-Vue）](https://gitee.com/y_project/RuoYi-Vue) v3.9.2 Spring Boot 3 分支定制开发。

---

## 目录

- [技术栈](#技术栈)
- [功能模块](#功能模块)
- [环境要求](#环境要求)
- [部署步骤](#部署步骤)
  - [一、数据库初始化](#一数据库初始化)
  - [二、后端启动](#二后端启动)
  - [三、前端启动](#三前端启动)
- [SQL 脚本说明](#sql-脚本说明)
- [默认账号](#默认账号)
- [常用配置说明](#常用配置说明)

---

## 技术栈

| 层级 | 技术选型 |
| --- | --- |
| 后端框架 | Spring Boot 3.x |
| 安全认证 | Spring Security + JWT |
| 持久层 | MyBatis + Druid 连接池 |
| 缓存 | Redis |
| 数据库 | MySQL 5.7+ / 8.0+ |
| 前端框架 | Vue 2 + Element UI |
| 构建工具 | Maven（后端）/ Vue CLI（前端）|
| 定时任务 | Quartz |

---

## 功能模块

| 模块 | 说明 |
| --- | --- |
| 院系管理 | 院系基本信息维护，院系代码/名称/系主任唯一性校验 |
| 教职工管理 | 教职工档案管理，与系统用户双向绑定 |
| 学生管理 | 学生档案及获奖信息管理 |
| 论文管理 | 学术论文信息及作者关系维护 |
| 专利管理 | 专利信息及发明人关系维护 |
| 科研项目管理 | 项目信息、成员、资金、成果全生命周期管理 |
| 数据大屏 | 核心指标汇总、院系对比柱状图可视化 |
| 系统管理 | 用户/角色/菜单/字典/日志等若依内置功能 |

---

## 环境要求

| 组件 | 版本要求 |
| --- | --- |
| JDK | 17+ |
| MySQL | 5.7+ 或 8.0+ |
| Redis | 6.x+ |
| Node.js | 14.x+ |
| npm | 6.x+ |
| Maven | 3.8+ |

---

## 部署步骤

### 一、数据库初始化

#### 1. 创建数据库

```sql
CREATE DATABASE `cis` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cis;
```

#### 2. 按顺序执行 SQL 脚本

> 脚本文件均位于项目根目录 `sql/` 文件夹下。
> **严格按照以下顺序执行，跳过或乱序会导致外键约束报错。**

| 顺序 | 文件名 | 说明 |
| :---: | --- | --- |
| 1 | `ry_20260320.sql` | 若依框架基础表（sys_user、sys_role、sys_menu 等）及内置数据，**必须最先执行** |
| 2 | `quartz.sql` | Quartz 定时任务框架所需的 QRTZ_* 系列表，依赖步骤 1 的数据库环境 |
| 3 | `mycreatetables_compatible.sql` | 本项目业务表（DEPARTMENT、FACULTY、STUDENT、ACADEMIC_PAPER、RESEARCH_PROJECT、PATENT 等），兼容 MySQL 5.7 / 8.0，**推荐使用此文件** |
| 4 | `init_data.sql` | 全量初始化脚本：业务菜单配置 + 四个自定义角色权限 + 测试账号 + 测试业务数据，**脚本可幂等重复执行** |
| 5 | `init_dashboard_statistics.sql` | 数据大屏统计初始值（可选，需在步骤 4 完成后执行） |

**执行示例（MySQL 命令行）：**

```bash
mysql -u root -p cis < sql/ry_20260320.sql
mysql -u root -p cis < sql/quartz.sql
mysql -u root -p cis < sql/mycreatetables_compatible.sql
mysql -u root -p cis < sql/init_data.sql
mysql -u root -p cis < sql/init_dashboard_statistics.sql
```

**执行示例（图形化工具，如 Navicat / DBeaver）：**

1. 新建连接并选择数据库 `cis`
2. 依次打开上述 5 个文件，**按顺序**运行全部语句
3. 每个文件执行完毕后确认无报错，再执行下一个

---

### 二、后端启动

#### 1. 修改数据库连接

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`，填写本机 MySQL 连接信息：

```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/cis?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
        username: root
        password: 你的MySQL密码
```

#### 2. 修改 Redis 连接（如有密码）

编辑 `ruoyi-admin/src/main/resources/application.yml`：

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:        # 无密码留空，有密码填写
      database: 0
```

#### 3. 编译并启动

```bash
# 在项目根目录执行
mvn clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

或在 IDE（IntelliJ IDEA）中直接运行 `RuoYiApplication.java`。

后端默认监听端口：**8080**

---

### 三、前端启动

```bash
cd ruoyi-ui
npm install
npm run dev
```

前端默认访问地址：**http://localhost:80**

**生产环境打包：**

```bash
cd ruoyi-ui
npm run build:prod
```

打包产物在 `ruoyi-ui/dist/` 目录，部署到 Nginx 或 Apache 即可。

---

## SQL 脚本说明

### 脚本文件一览

| 文件 | 用途 | 是否需要手动执行 |
| --- | --- | :---: |
| `ry_20260320.sql` | 若依框架基础表 + 内置菜单/用户/角色数据 | 是（步骤 1）|
| `quartz.sql` | Quartz 定时任务框架表 | 是（步骤 2）|
| `mycreatetables_compatible.sql` | 业务表结构（**推荐**，兼容 5.7/8.0）| 是（步骤 3）|
| `init_data.sql` | 菜单 + 角色权限 + 测试账号 + 业务测试数据（**综合脚本**）| 是（步骤 4）|
| `init_dashboard_statistics.sql` | 数据大屏统计初始值 | 可选（步骤 5）|
| `menu_all_modules.sql` | 仅含业务菜单 INSERT，已整合到 `init_data.sql` Part 1 | **无需单独执行** |
| `init_roles_permissions.sql` | 仅含四角色权限配置，已整合到 `init_data.sql` Part 2 | **无需单独执行** |
| `mycreatetables.sql` | 旧版建表（存在循环外键依赖顺序问题）| **不推荐，请使用 compatible 版本** |

### init_data.sql 内容结构

该文件是**全量一次性部署脚本**，内部分为三个部分，按顺序执行：

```
PART 1 — 业务菜单配置
  院系/教职工/学生/论文/专利/项目/数据大屏 菜单 INSERT
  超级管理员(role_id=1) 和 普通角色(role_id=2) 菜单授权

PART 2 — 自定义角色与权限配置
  role_id=3  学院管理员(college_admin)  系统管理核心 + 业务全权
  role_id=4  学院领导(college_leader)   所有模块只读 + 导出 + 数据大屏
  role_id=5  教职工(college_teacher)    论文/专利/项目增删改查 + 师生只读
  role_id=6  学生(college_student)      学生档案查改 + 论文/专利/项目增改查

PART 3 — 测试账号
  3 名教职工系统账号 (user_id: 200~202)
  3 名学生系统账号   (user_id: 210~212)
  以及对应的 FACULTY / STUDENT 业务记录
```

> `init_data.sql` 使用 `DELETE ... WHERE` + 重新插入的幂等设计，可**反复执行**以重置测试数据，不会重复插入。

### 外键依赖关系（建表顺序）

```
sys_user（若依原生）
    └── DEPARTMENT（无外键先建）
          ├── FACULTY（依赖 sys_user + DEPARTMENT）
          │     └── DEPARTMENT.head_faculty_id（ALTER ADD FK，事后补）
          ├── STUDENT（依赖 sys_user + DEPARTMENT）
          ├── ACADEMIC_PAPER（依赖 DEPARTMENT）
          │     └── PAPER_AUTHOR（依赖 ACADEMIC_PAPER）
          ├── RESEARCH_PROJECT（依赖 DEPARTMENT）
          │     ├── FUNDING_RECORD（依赖 RESEARCH_PROJECT）
          │     ├── PROJECT_MEMBER（依赖 RESEARCH_PROJECT）
          │     └── PROJECT_OUTPUT（依赖 RESEARCH_PROJECT）
          ├── PATENT（依赖 DEPARTMENT）
          │     └── PATENT_AUTHOR（依赖 PATENT）
          ├── STUDENT_AWARD（依赖 STUDENT + FACULTY）
          └── STATISTICS_VIEW（独立统计表）
```

---

## 默认账号

### 若依内置账号（密码：admin123）

| 账号 | 密码 | 说明 |
| --- | --- | --- |
| `admin` | `admin123` | 超级管理员，拥有所有权限 |
| `ry` | `admin123` | 若依内置测试员 |

### 测试业务账号（由 init_data.sql 初始化，密码：admin123）

| 账号 | 密码 | 角色 | 说明 |
| --- | --- | --- | --- |
| `2021001001` | `admin123` | 教职工 | 张威，计算机科学与技术系 |
| `2021001002` | `admin123` | 教职工 | 李芳，软件工程系 |
| `2021001003` | `admin123` | 教职工 | 王军，信息安全系 |
| `2021010101` | `admin123` | 学生 | 张明，计算机科学与技术系 |
| `2021010102` | `admin123` | 学生 | 李红，软件工程系 |
| `2022010101` | `admin123` | 学生 | 王强，信息安全系 |

> 教职工账号即**工号**，学生账号即**学号**。
> 学院管理员账号需在系统中手动创建用户并分配 `college_admin` 角色。

---

## 常用配置说明

### 后端端口

`ruoyi-admin/src/main/resources/application.yml`

```yaml
server:
  port: 8080   # 修改此处更换后端端口
```

### 文件上传路径

```yaml
ruoyi:
  profile: D:/ruoyi/uploadPath   # Windows 示例
  # profile: /home/ruoyi/uploadPath   # Linux 示例
```

### 前端代理目标

`ruoyi-ui/.env.development`

```
VUE_APP_BASE_API = 'http://localhost:8080'   # 与后端端口保持一致
```

### Druid 监控控制台

访问地址：`http://localhost:8080/druid`
默认账号：`ruoyi` / `123456`
