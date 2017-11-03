# jDeploy数据模型

## 数据结构

### ~~用户表 user~~
因为打包发布这种事只能单用户操作，所以用户表貌似就没什么用了

| 字段名 | 说明 | 类型 | 备注 |
| --- | --- | --- | --- |
| id | id | INT | 自增量，主键 |
| username | 帐号 | VARCHAR(16) |  |
| password | 密码 | VARCHAR(128) | 非明文，md5加密 |
| last_login_at | 最后登录时间 | DATETIME | 字符串 |
| role | 角色 | INT | 0: 非管理员; 1: 管理员 |
| create_at | 创建时间 | DATETIME |  |

```sql
CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL COMMENT '帐号',
  `password` VARCHAR(128) NOT NULL COMMENT '密码 非明文，md5加密',
  `last_login_at` DATETIME COMMENT '最后登录时间',
  `role` INT(11) NOT NULL DEFAULT '角色 0: 非管理员; 1: 管理员',
  `create_at` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_username` (`username`) USING BTREE
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_general_ci;
```

### 项目表 project
| 字段名 | 说明 | 类型 | 备注 |
| --- | --- | --- | --- |
| id | id | VARCHAR(64) | UUID |
| name | 名称 | VARCHAR(16) | 使用git仓库中的项目名 |
| description | 描述 | VARCHAR(64) |  |
| git_url | 远程git仓库地址 | VARCHAR(64) |  |
| branch | 分支名 | VARCHAR(16) |  |
| project_source_path | 项目源码路径 | VARCHAR(32) |  |
| project_deploy_path | 项目发布路径 | VARCHAR(32) |  |
| package_name | 包名 | VARCHAR(16) | 完整包名，如：demo.jar |
| deploy_sub_module | 发布子模块名称 | VARCHAR(16) | 发布的子模块文件夹名 |
| deploy_way | 发布方法 | INT | 1: 直接运行; 2: 拷贝到容器 |
| custom_start | 自定义启动命令 | VARCHAR(16) |  |
| web_container_id | 容器id | VARCHAR(16) |  |
| create_at | 创建时间 | DATETIME |  |

```sql
CREATE TABLE `project` (
  `id` VARCHAR(64) NOT NULL,
  `name` VARCHAR(16) NOT NULL COMMENT '名称',
  `description` VARCHAR(64) COMMENT '描述',
  `git_url` VARCHAR(64) NOT NULL CPMMENT '远程git仓库地址',
  `branch` VARCHAR(16) CPMMENT '分支名',
  `project_source_path` VARCHAR(32) NOT NULL CPMMENT '项目源码路径',
  `project_deploy_path` VARCHAR(32) NOT NULL CPMMENT '项目发布路径',
  `package_name` VARCHAR(16) NOT NULL CPMMENT '包名 完整包名，如：demo.jar',
  `deploy_sub_module` VARCHAR(16) CPMMENT '发布子模块名称 发布的子模块文件夹名',
  `deploy_way` INT CPMMENT '发布方法 1: 直接运行; 2: 拷贝到容器',
  `custom_start` VARCHAR(16) CPMMENT '自定义启动命令',
  `web_container_id` VARCHAR(16) CPMMENT '容器id',
  `create_at` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_general_ci;
```

### web容器 web_container
| 字段名 | 说明 | 类型 | 备注 |
| --- | --- | --- | --- |
| id | id | VARCHAR(64) | UUID |
| name | 名称 | VARCHAR(16) | 使用git仓库中的项目名 |
| description | 描述 | VARCHAR(16) |  |
| path | 路径 | VARCHAR(16) |  |
| type | 类型 | INT | 1: tomcat; 2: jetty |
| create_at | 创建时间 | DATETIME |  |
