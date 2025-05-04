<p align="center">
<img src="doc/images/main.png" alt="image-20230623213937364" width="500" align="center" />
</p>
<p align=center>
   SUPER速配-用户匹配系统
</p>
<p align="center">
<a target="_blank" href="https://gitee.com/ochiamaluo/super-backend">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-18.16.0-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.6.4-green" ></img>
        <img src="https://img.shields.io/badge/vue-3.2-green" ></img>
		<img src="https://img.shields.io/badge/vant-4.3.1-green" ></img>
        <img src="https://img.shields.io/badge/swagger-3.0.0-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.5.1-green" ></img>
		<img src="https://img.shields.io/badge/websocket-2.6.4-green" ></img>
		<img src="https://img.shields.io/badge/MySQL-5.7-green" ></img>
		<img src="https://img.shields.io/badge/redis-7.0.2-green" ></img>
</a></p>


## 项目介绍

​		速配(SUPER)，一个前后端分离的用户匹配系统，前端使用**Vite + Vue3 + Vant4**，后端使用**SpringBoot** + **Mybatis-plus**，使用**WebSocket**实现实时通信，并结合**阿里云SDK + 七牛云SDK**完成短信发送和图片上传。本项目使用Apache License Version 2.0开源协议。
#### 前端地址:

https://gitee.com/ochiamaluo/super-frontend

#### 后端地址:

https://gitee.com/ochiamaluo/super-backend

#### 后台管理系统及介绍请见：

https://gitee.com/ochiamaluo/super-frontend/tree/master/super-admin

## 在线体验

演示地址: http://ochiamalu.fun

账号可以自行注册，一切聊天、评论需遵守中华人民共和国有关法律规定。

仅支持中国大陆手机号，若没有符合要求的手机号或本网站短信功能无法使用，请使用以下账号直接登录：

账号：admin

密码：12345678

## 项目背景

​      许多热爱编程比赛的用户在参加比赛时常常面临没有队友的问题，这使得他们很难发挥自己的实力，也很难取得好成绩。同时，许多用户在寻找队友时也面临一些困难，比如无法找到合适的人选，或者无法与潜在的队友进行充分的交流和沟通。因此，我们希望通过该网站，为这些用户提供一个交流平台，帮助他们找到理想的队友，组建强大的编程团队，参加各种编程比赛，并最终实现自己的编程梦想。

## 核心功能

1. 用户注册和登录：用户可以通过注册账号并登录使用该网站。
2. 标签匹配：用户可以选择自己的技能和需求标签，系统会根据标签匹配合适的队友。
3. 组队功能：用户可以与其他用户组建队伍，一起参加编程比赛。
4. 帖文发布、点赞和关注：用户可以发布自己的帖文，其他用户可以对其进行点赞和关注，以便更好地了解和交流。
5. 实时聊天：队伍中的用户可以进行实时聊天，方便沟通和协作。
6. 用户管理：管理员可以对用户进行管理，包括审核用户信息和处理用户投诉等。

## 项目文档

文档地址： http://doc.ochiamalu.fun

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee 地址： https://gitee.com/ochiamaluo/super-backend
- Github地址： https://github.com/OchiaMalu/super-backend

## 技术选型

**前端**

\- Vue 3

\- Vite 脚手架

\- Vant UI 移动端组件库

\- Axios 请求库

**后端**

\- JAVA SpringBoot 框架

\- MySQL 数据库

\- Mybatis-Plus

\- Mybatis X

\- Redis缓存

\- Redisson 分布式锁

\- Quartz定时任务

\- Swagger + Knife4j 接口文档

\- Gson JSON序列化库

\- 最短编辑距离算法

\- WebSocket

## 项目亮点

1. 用户登录：使用 Redis 实现分布式 Session，解决集群间登录态同步问题；使用token储存用户信息并实现续签和超时自动退出。
2. 对于项目中复杂的集合处理（比如为队伍列表关联已加入队伍的用户），使用 Java 8 Stream API 和 Lambda 表达式来简化编码。
3. 使用 Redis 缓存首页高频访问的用户信息列表，将接口响应时长从 12520ms缩短至400ms。且通过自定义 Redis 序列化器来解决数据乱码、空间浪费的问题。
4. 为解决首次访问系统的用户主页加载过慢的问题，使用 quartz 定时任务来实现缓存预热，并通过分布式锁保证多机部署时定时任务不会重复执行。
5. 为解决同一用户重复加入队伍、入队人数超限的问题，使用 Redisson 分布式锁来实现操作互斥，保证了接口幂等性。
6. 使用编辑距离算法实现了根据标签匹配最相似用户的功能，并通过优先队列来减少 TOP N 运算过程中的内存占用。
7. 自主编写 Dockerfile，并通过第三方容器托管平台实现自动化镜像构建及容器部署，提高部署上线效率。
8. 使用 Knife4j + Swagger 自动生成后端接口文档，并通过编写 ApiOperation 等注解补充接口注释，避免了人工编写维护文档的麻烦。
9. 使用本地+云服务储存用户头像，并自定义cdn加速域名指向项目专用储存空间。
10. 使用WebSocket在单个TCP连接上进行全双工通信，创建持久性的连接，实现队伍聊天室中的实时聊天。
11. 前端使用 Vant UI 组件库，并封装了全局通用的 Layout 组件，使主页、搜索页、组队页布局一致、并减少重复代码。
12. 封装前端卡片组件，对数据做统一处理，减少重复代码。

### 匹配算法简介

1. **改进的Jaccard相似度** (`enhanced_jaccard`)：计算标签集合的交并比

2. **改进的余弦相似度** (`enhanced_cosine`)：使用TF-IDF加权计算文本相似度

3. **改进的最短距离编辑算法**(`enhanced_edit_distance`) ：考虑标签所属技术类别的匹配程度

根据标签特性调整，动态加权组合以上三种改进算法，形成一种特殊的匹配算法。

混合算法动态调整权重的核心思想是 **根据输入标签的特征自动优化各子算法的贡献比例** 。

| 特征指标                  | 适用算法            | 理论依据                    |
|-----------------------|-----------------|-------------------------|
| 唯一标签数 (`unique_tags`) | Jaccard         | 标签多样性高时，集合运算能快速筛选潜在匹配   |
| 标签组合数 (`len1*len2`)   | Cosine (TF-IDF) | 组合数多时，TF-IDF能捕捉关键标签的区分度 |
| 标签类别重叠度               | EditDistance    | 当标签来自相同技术类别时，编辑距离更有意义   |

- **平滑过渡**：使用sigmoid函数替代硬阈值，避免权重突变
- **类别感知**：根据标签类别重叠度动态调整语义权重
- **自动归一化**：确保权重总和为1

<img src="https://niu.ochiamalu.fun/myplot6.png" alt="myplot6" style="zoom:80%;" />

上图显示混合算法在比较不同情况时自动切换权重的部分示例。

<img src="https://niu.ochiamalu.fun/myplot1.png" alt="myplot1" style="zoom: 50%;margin:0 auto" />

经多样本分析，混合算法在综合情况下比起其他独立算法更加贴合最优曲线。

<img src="https://niu.ochiamalu.fun/2.png" alt="2" style="zoom: 50%;margin:0 auto" />

<img src="https://niu.ochiamalu.fun/myplot3.png" alt="myplot3" style="zoom: 50%;margin:0 auto" />

<img src="https://niu.ochiamalu.fun/myplot2.png" alt="myplot2" style="zoom:50%;margin:0 auto" />

在全随机模拟场景下，即定义了一个有层次结构的标签池，包含7个技术领域（语言、前端、后端等），每个领域下有多个具体标签，每次随机生成20个用户，每个用户从2-3个技术领域随机选择1-2个标签。

在其他算法无法判断出差异性（评分为0）时，优化后的混合算法仍然可以完成计算。

### 软件架构

![image-20230623201717125](doc/images/1.png)

### 数据库架构

![image-20230623203721932](doc/images/2.jpg)

## 后端部署

1. 执行安装后端所需依赖

   ```bash
   maven install
   ```

2. 修改application.xml，详细配置信息请见项目文档

3. 新建super数据库，并执行super.sql
4. 运行SuperApplication

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法，遇到一些 **BUG** 并修复了，欢迎小伙伴们提交 **Pull Request** 参与开源贡献！

1. **fork** 本项目到自己的 **repo**
2. 把 **fork** 过去的项目也就是你仓库中的项目 **clone** 到你的本地
3. 修改代码
4. **commit** 后 **push** 到自己的库
5. 发起**PR**（ **pull request**） 请求，提交到 **dev** 分支
6. 等待作者合并

## 联系我

<img src="doc/images/customer-service.jpg" width="200">

