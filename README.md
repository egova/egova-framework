# egova-framework

egova framework是基于java后端技术的基础性架构，旨在为公司提供一套标准的基础类库设计以及工具类封装。

## 开发环境

- Jdk版本:  1.8
- 构建工具:  maven
- 源码管理:  git

## 工程组织

以maven父子工程方式组织，结构如下：

```
┌── egova-framework  
├── docs                            	# 工程文档
├── modules                         	# 模块文件夹
│    ├── egova-framework-base       	# 基础类定义
│    ├── egova-framework-cache       	# 二级缓存实现
│    ├── egova-framework-cloud-api      # 微服务api
│    ├── egova-framework-cloud-adapter  # 微服务适配器
│    ├── egova-framework-dependencies   # 依赖管理
│    ├── egova-framework-oauth       	# oauth2认证
│    ├── egova-framework-redis       	# redis自动配置
│    └── egova-framework-minio       	# minio对象存储
│
└── pom.xml                         	# pom

```

## 约定

### 包名约定

* 所有包必须以`com.egova`开头

### 模块名称

* 规则:`egova-framework-{模块名称}`
  
## 版本管理

* 仓库地址：https://oss.sonatype.org/

* 发布`SNAPSHOT`版本
  
    ```bash
    $ mvn clean deploy
    ```

* 发布`Staging`版本

  ```bash
  $ mvn release:clean
  $ mvn release:prepare
  $ mvn release:perform
  ```