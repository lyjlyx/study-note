# Prometheus+Grafana详解

原始文章地址：

```
https://blog.csdn.net/weixin_52198548/article/details/135347794
```

## Prometheus(普罗米修斯）监控系统

### 1、Prometheus概述

#### 1.1 任务背景

某公司由于业务快速发展，公司要求对现有机器进行业务监控，责成运维部门来实施这个任务。任务要求如下：

- 部署监控服务器，实现7x24实时监控


- 针对公司的业务及研发部门设计监控系统，对监控项和触发器拿出合理意见


- 做好问题预警机制，对可能出现的问题要及时告警并形成严格的处理机制


- 做好监控告警系统，要求可以实现告警分级

  - 一级报警 电话通知

  - 二级报警 微信通知

  - 三级报警 邮件通知

- 处理好公司服务器异地集中监控问题

为什么要监控？

```
实时收集数据，通过报警及时发现问题，及时处理。数据为优化也可以提供依据。
```

监控四要素：

- 监控对象 [主机状态 服务 资源 页面，url]

- 用什么监控

- 什么时间监控 [7x24 5x8]

- 报警给谁

  **监控技术选型：**

- mrtg (MRTG - Tobi Oetiker’s MRTG - The Multi Router Traffic Grapher)通过snmp协议得到设备的流量信息，并以包含PNG格式的图形的HTML文档方式显示给用户。

- cacti (仙人掌) 用php语言实现的一个软件，它的主要功能是用snmp服务获取数据，然后用rrdtool储存和更新数据。官网地址:Cactus | Description, Distribution, Family, & Facts | Britannica
- ntop 官网地址: https://www.ntop.org/ 。
- nagios 能够跨平台,插件多,报警功能强大。官网地址: https://www.nagios.org/
- centreon 底层使用的就是nagios。是一个nagios整合版软件。官网地址:https://www.centreon.com/
- ganglia 设计用于测量数以千计的节点,资源消耗非常小。官网地址:http://ganglia.info/
- open-falcon 小米发布的运维监控软件，高效率，高可用。时间较短，用户基数小。官网地址: http://open-falcon.org/
- zabbix 跨平台，画图，多条件告警，多种API接口。使用基数特别大。官网地址: https://www.zabbix.com/
- prometheus 基于时间序列的数值数据的容器监控解决方案。官网地址: https://prometheus.io/
- 综合分析：Prometheus比较适合公司的监控需求

#### 1.2 Prometheus特点

Prometheus 受启发于 Google 的 Brogmon 监控系统（相似的 Kubernetes 是从 Google的 Brog 系统演变而来），从 2012 年开始由前 Google 工程师在 Soundcloud 以开源软件的形式进行研发，并且于 2015 年早期对外发布早期版本。2016 年 5 月继 Kubernetes 之后成为第二个正式加入 CNCF 基金会的项目，同年 6 月正式发布 1.0 版本。2017 年底发布了基于全新存储层的 2.0 版本，能更好地与容器平台、云平台配合。

Prometheus 作为新一代的云原生监控系统，目前已经有超过 650+位贡献者参与到Prometheus 的研发工作上，并且超过 120+项的第三方集成。

Prometheus 是一个开源的完整监控解决方案，其对传统监控系统的测试和告警模型进行了彻底的颠覆，形成了基于中央化的规则计算、统一分析和告警的新模型。 相比于传统监控系统，Prometheus 具有以下优点：

1 易于管理
Prometheus优秀的设计使得其本身非常易于管理，不会因为Prometheus增加管理成本。

Prometheus 核心部分只有一个单独的二进制文件，不存在任何的第三方依赖(数据库，缓存等等)。唯一需要的就是本地磁盘，因此不会有潜在级联故障的风险。
Prometheus 基于 Pull 模型的架构方式，可以在任何地方（本地电脑，开发环境，测试环境）搭建我们的监控系统。也可以通过中间网关支持push模型
对于一些复杂的情况，还可以使用 Prometheus 服务发现(Service Discovery)的能力动态管理监控目标。
2 可监控服务的内部运行状态
Pometheus 鼓励用户监控服务的内部状态，基于 Prometheus 丰富的 Client 库，用户可以轻松的在应用程序中添加对 Prometheus 的支持，从而让用户可以获取服务和应用内部真正的运行状态。