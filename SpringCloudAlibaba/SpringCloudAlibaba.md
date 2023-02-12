# SpringCloud Alibaba(一)



## Sentinel限流器

**限流器为了保证用户大量并发不会把服务给击溃**



**观察者模式(基于事件监听)：**服务器注册到nacos的事件池里，nacos会关注该服务的数据的变化，一旦有些数据变化了，他会通知事件池，事件池就会调起一个新的线程的方法，把新的数据就推给了注册的这个客户端了，



**要想使用SpringCloudAlibaba的Sentinel的话 SpringBoot的版本要大于2.2.0小于2.3.0**





**实现方法**

```java
/**
接口调用
**/
@RestController
public class MainController {

    @Autowired
    private PersonService personService;

    @GetMapping("/get")
    public String get() {
        return personService.getBody();
    }

}


/**
 * @author LYX
 * @description
 * @date 2022/2/17 23:05
 */
@Service
public class PersonService {

    //这个注解中的value要和我们初始化规则方法里面所设置进去的resource要一样，例：HelloWorld
    @SentinelResource(value = "HelloWorld", blockHandler = "back")
    public String getBody() {
        //执行真正的业务逻辑，或者可以叫做被保护的资源
        System.out.println("123123");
        return "给你咯";
    }

    /**
     * 降级方法
     * @return 需要添加blockException
     */
    public String back(BlockException blockException) {
        System.out.println("back降级");
        return "我降级啦";
    }
}
```







![image-2022021723092122 9](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220217230921229.png) 

启动之后所有的日志都会写到这个路径下

![ ](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220217230855072.png) 



**如果在项目中配置好了sentinel的规则，再去整合dashboard的话非常容易出问题，容易引发冲突，dashboard也能配置这些规则，所以需要整合过去，而且sentinel配了dashboard的配置会不生效**



## Sentinel Dashboard

Sentinel 提供一个轻量级的开源控制台，它提供机器发现以及健康情况管理、监控（单机和集群），规则管理和推送的功能。另外，鉴权在生产环境中也必不可少。这里，我们将会详细讲述如何通过[简单的步骤](https://github.com/alibaba/Sentinel/wiki/控制台#2-启动控制台)就可以使用这些功能。

接下来，我们将会逐一介绍如何整合 Sentinel 核心库和 Dashboard，让它发挥最大的作用。同时我们也在阿里云上提供企业级的控制台：[AHAS Sentinel 控制台](https://github.com/alibaba/Sentinel/wiki/AHAS-Sentinel-控制台)，您只需要几个简单的步骤，就能最直观地看到控制台如何实现这些功能。

Sentinel 控制台包含如下功能:

- [**查看机器列表以及健康情况**](https://github.com/alibaba/Sentinel/wiki/控制台#4-查看机器列表以及健康情况)：收集 Sentinel 客户端发送的心跳包，用于判断机器是否在线。
- [**监控 (单机和集群聚合)**](https://github.com/alibaba/Sentinel/wiki/控制台#5-监控)：通过 Sentinel 客户端暴露的监控 API，定期拉取并且聚合应用监控信息，最终可以实现秒级的实时监控。
- [**规则管理和推送**](https://github.com/alibaba/Sentinel/wiki/控制台#6-规则管理及推送)：统一管理推送规则。
- [**鉴权**](https://github.com/alibaba/Sentinel/wiki/控制台#鉴权)：生产环境中鉴权非常重要。这里每个开发者需要根据自己的实际情况进行定制。

> 注意：Sentinel 控制台目前仅支持单机部署。

#### 下载

https://github.com/alibaba/Sentinel/releases

https://github.com/alibaba/Sentinel/wiki/Dashboard

下载sentinel dashboard

https://github.com/alibaba/Sentinel/releases/tag/1.7.2

#### 启动

> **注意**：启动 Sentinel 控制台需要 JDK 版本为 1.8 及以上版本。

使用如下命令启动控制台：

```
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.2.jar
```

其中 `-Dserver.port=8080` 用于指定 Sentinel 控制台端口为 `8080`。

从 Sentinel 1.6.0 起，Sentinel 控制台引入基本的**登录**功能，默认用户名和密码都是 `sentinel`。可以参考 [鉴权模块文档](https://github.com/alibaba/Sentinel/wiki/控制台#鉴权) 配置用户名和密码。

**配置流量监控**

![image-2022 0218124954845](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218124954845.png) 

**配置文件**

```
spring.application.name=stn1

server.port=81

#是不是上来就提交信息 服务启动之后，是否直接去注册dashboard
spring.cloud.sentinel.eager=true
#http端口
spring.cloud.sentinel.transport.dashboard=localhost:8080
```

**注册上来了**

![image-20220218 125510784](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218125510784.png) 

![image-20220218125407224](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218125407224.png) 



**如果启动了一个服务因为某些原因挂了（虽然他挂了 但是他不影响他注册到dashboard上），但是你调整之后重新启动之后，他会在dashboard注册两个服务，一个是你前面失败的，一个是正常的，这样会影响调用**



## Nacos下发文件

**如何使用nacos来下发配置文件到sentinel的dashboard上获取配置**

**nacos依赖要添加**

```xml
<dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
```



![image-20220218 131527012](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218131527012.png) 

![image-20220  218131538019](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218131538019.png) 

### springboot配置

```
#整合nacos下发配置
spring.cloud.sentinel.datasource.ds.nacos.server-addr=localhost:8848
#数据id
spring.cloud.sentinel.datasource.ds.nacos.dataId=stn
spring.cloud.sentinel.datasource.ds.nacos.groupId=DEFAULT_GROUP
#必须要填,根据qps限流
spring.cloud.sentinel.datasource.ds.nacos.ruleType=flow
```

### Nacos添加规则

```json
[
    {
        "resource": "HelloWorld",
        "limitApp": "default",
        //1代表qps限流，2代表线程限流
        "grade": 1,
        "count": 2,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]

```

nacos自动下发的限流规则

![image-202202181 31605368](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218131605368.png)





**nacos配置阈值为3**

![image-20220218131715 862](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218131715862.png) 

dashboard实时拉取

![image-2 0220218131730801](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220218131730801.png) 



### Sentinel 与 Hystrix 的对比

|                | Sentinel                                       | Hystrix                       |
| :------------- | :--------------------------------------------- | :---------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离         |
| 熔断降级策略   | 基于响应时间或失败比率                         | 基于失败比率                  |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于 RxJava）       |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                |
| 扩展性         | 多个 SPI 扩展点                                | 插件的形式                    |
| 基于注解的支持 | 支持                                           | 支持                          |
| 限流           | 基于 QPS，支持基于调用关系的限流               | 有限的支持                    |
| 流量整形       | 支持慢启动、匀速器模式                         | 不支持                        |
| 系统负载保护   | 支持                                           | 不支持                        |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                        |
| 常见框架的适配 | Servlet、Spring Cloud、Dubbo、gRPC 等          | Servlet、Spring Cloud Netflix |

###  



## Sentinel平滑时间窗口



以一个桶的形式来实现一个时间窗口

bucket = 小的时间窗口 500ms

bucket数组默认120个限制



**先拿到当前写入的时间戳 去掉毫秒  % 数组总长度 = 具体的数组里**

![image-20220 219000036301](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219000036301.png) 

**平滑**

**在记录时间戳的时候直接在数组的最后一个位置去记录，检查他是否写进来就拿数组的第一个，两边对减之后得到了一个时间窗口范围，计算时间窗口范围是否大于阈值，如果大于阈值就不往里写了。**



sentinel以一秒为一个单位，把他切割成两个部分，每一部分都是以一个小的时间窗口来统计计数的，两两一组，组成一秒

**让每两个小的时间窗口加起来看看有没有超过最大接收值。**

**例如：每秒最多只能接收100个请求，把每秒分为两个部分，第一部分接收了50个请求，第二部分来了10个请求 第一部分和第二部分加起来没有超过最大值不需要拒绝，能够接收，第三部分来了80，第二部分接收了10  也没超过，第四部分来了80，与第三部分相加超过了最大值，可以拒绝掉一部分的请求，或者平滑的迁移到下一部分来处理**



## QPS隔离与线程隔离

![image-202202 19101608205](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219101608205.png) 



## 三种限流

sentinel设置的限流规则可以关联请求



 ![image-20220 219104116089](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219104116089.png) 



Warm Up预热



RT(Response Time)



## 降级

![ ](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219111504159.png) 

![image-202 20219112842174](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219112842174.png) 



一个进程会包含一个或多个线程

### Load

线程对CPU所产生的结果

### BBR

对于网络资源的优化，Google提出的对于网络规则比较调优的算法



![image-20220219113 839505](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219113839505.png) 





## 异常比例与异常数

![image-20220219 114425146](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219114425146.png) 

## 热点限流



**为了避免热点数据把我们做定向流量分发或者没做定向流量分发的时候的一些后端的集群给打垮**



## 集群限流



## 系统自适应限流

Sentinel 系统自适应限流从整体维度对应用入口流量进行控制，结合应用的 Load、CPU 使用率、总体平均 RT、入口 QPS 和并发线程数等几个维度的监控指标，通过自适应的流控策略，让系统的入口流量和系统的负载达到一个平衡，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。

- **Load 自适应**（仅对 Linux/Unix-like 机器生效）：系统的 load1 作为启发指标，进行自适应系统保护。当系统 load1 超过设定的启发值，且系统当前的并发线程数超过估算的系统容量时才会触发系统保护（BBR 阶段）。系统容量由系统的 `maxQps * minRt` 估算得出。设定参考值一般是 `CPU cores * 2.5`。

- **CPU usage**（1.5.0+ 版本）：当系统 CPU 使用率超过阈值即触发系统保护（取值范围 0.0-1.0），比较灵敏。
- **平均 RT**：当单台机器上所有入口流量的平均 RT 达到阈值即触发系统保护，单位是毫秒。
- **并发线程数**：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
- **入口 QPS**：当单台机器上所有入口流量的 QPS 达到阈值即触发系统保护。

## 集群流控

为什么要使用集群流控呢？假设我们希望给某个用户限制调用某个 API 的总 QPS 为 50，但机器数可能很多（比如有 100 台）。这时候我们很自然地就想到，找一个 server 来专门来统计总的调用量，其它的实例都与这台 server 通信来判断是否可以调用。这就是最基础的集群流控的方式。

另外集群流控还可以解决流量不均匀导致总体限流效果不佳的问题。假设集群中有 10 台机器，我们给每台机器设置单机限流阈值为 10 QPS，理想情况下整个集群的限流阈值就为 100 QPS。不过实际情况下流量到每台机器可能会不均匀，会导致总量没有到的情况下某些机器就开始限流。因此仅靠单机维度去限制的话会无法精确地限制总体流量。而集群流控可以精确地控制整个集群的调用总量，结合单机限流兜底，可以更好地发挥流量控制的效果。

集群流控中共有两种身份：

- Token Client：集群流控客户端，用于向所属 Token Server 通信请求 token。集群限流服务端会返回给客户端结果，决定是否限流。
- Token Server：即集群流控服务端，处理来自 Token Client 的请求，根据配置的集群规则判断是否应该发放 token（是否允许通过）。

![ima ge](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/65305357-8f39bc80-dbb5-11e9-96d6-d1111fc365a9.png) 



1. 集群总体模式

   

2. 单击均摊模式

![ ](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219193025950.png) 



硬编码、通过nacos下发配置、写到文件系统里



## Nacos基本详解

官方网站

https://nacos.io/zh-cn/

Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您实现动态服务发现、服务配置管理、服务及流量管理。

Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构(例如微服务范式、云原生范式)的服务基础设施。

![nacos_lan dscape.png](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/29102804_XygY.png)



- 特性大图：要从功能特性，非功能特性，全面介绍我们要解的问题域的特性诉求
- 架构大图：通过清晰架构，让您快速进入 Nacos 世界
- 业务大图：利用当前特性可以支持的业务场景，及其最佳实践
- 生态大图：系统梳理 Nacos 和主流技术生态的关系
- 优势大图：展示 Nacos 核心竞争力
- 战略大图：要从战略到战术层面讲 Nacos 的宏观优势

![img ](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/nacosMap.jpg)

**配置中心与注册中心压测报告**

https://nacos.io/zh-cn/docs/nacos-config-benchmark.html



## 动态配置服务

动态配置服务让您能够以中心化、外部化和动态化的方式管理所有环境的配置。动态配置消除了配置变更时重新部署应用和服务的需要。配置中心化管理让实现无状态服务更简单，也让按需弹性扩展服务更容易。



### 服务提供方

1. `bootstrap.yaml` 中配置 Nacos server 的地址和应用名

```
spring:
  cloud:
    nacos:
      config:
        server-addr: 192.168.29.1:8848
        file-extension: yaml
   
  application:
    name: nacos-p
```

之所以需要配置 `spring.application.name` ，是因为它是构成 Nacos 配置管理 `dataId`字段的一部分。

在 Nacos Spring Cloud 中，`dataId` 的完整格式如下：

```plain
${prefix}-${spring.profiles.active}.${file-extension}
```

- `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置。
- `spring.profiles.active` 即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)。 **注意：当 `spring.profiles.active` 为空时，对应的连接符 `-` 也将不存在，dataId 的拼接格式变成 `${prefix}.${file-extension}`**
- `file-exetension` 为配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension` 来配置。目前只支持 `properties` 和 `yaml` 类型。

2. Controller

```
@RestController
@RefreshScope
public class MainController {

    @Value("${xxoo}")
    private String userName;

    @RequestMapping("/hello")
    public String hello() {
        return "hello+:" + userName;
    }
}
```



@RefreshScope注解能帮助我们做局部的参数刷新，但侵入性较强，需要开发阶段提前预知可能的刷新点，并且该注解底层是依赖于cglib进行代理

## 服务发现及管理

动态服务发现对以服务为中心的（例如微服务和云原生）应用架构方式非常关键。Nacos支持DNS-Based和RPC-Based（Dubbo、gRPC）模式的服务发现。Nacos也提供实时健康检查，以防止将请求发往不健康的主机或服务实例。借助Nacos，您可以更容易地为您的服务实现断路器。



### eureka

![ ](C:%5CProgram%20Files%5CWorkSpace%5Cstudy%5Cstudy-note%5CSpringCloudAlibaba%5Cimage%5Caaca0b10-36c0-11ea-83e2-610758492683)

网上很多人说 Eureka 闭源，其实没有，只是 Eurkea 2.x 分支不再维护，官方依然在积极地维护 Eureka 1.x，Spring Cloud 还是使用的 1.x 版本的 Eureka

### Nacos注册中心

类似 Eureka、ZooKeeper、Consul 等组件，既可以支持 HTTP、https 的服务注册和发现，也可以支持 RPC 的服务注册和发现，比如 Dubbo，也是出自于阿里，完全可以替代 Eureka、ZooKeeper、Consul。



### 命名空间



##  动态DNS服务

通过支持权重路由，动态DNS服务能让您轻松实现中间层负载均衡、更灵活的路由策略、流量控制以及简单数据中心内网的简单DNS解析服务。动态DNS服务还能让您更容易地实现以DNS协议为基础的服务发现，以消除耦合到厂商私有服务发现API上的风险。

## 集群

![img](image/5c1018e0-3a8d-11ea-afb7-7bb0fd976cea)

单节点的nacos使用standalone 模式

### 修改为mysql存储配置

#### 修改conf/application.properties

核心配置

```properties
server.contextPath=/nacos
server.servlet.contextPath=/nacos
server.port=8841

#数据源是什么平台的
spring.datasource.platform=mysql

db.num=1
#可以配置两台做主从
db.url.0=jdbc:mysql://localhost:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=840416
```

#### 导入sql文件

nacos-mysql.sql

### 集群配置

Nacos支持三种部署模式

- 单机模式 - 用于测试和单机试用。
- 集群模式 - 用于生产环境，确保高可用。
- 多集群模式 - 用于多数据中心场景。

window下默认启动脚本为单机版

修改startup.cmd

```
set MODE="cluster"
```





修改conf/cluster.conf

```
192.168.29.1:8841
192.168.29.1:8842
192.168.29.1:8843
```

#启动nacos集群模式命令

```
startup.cmd -m cluster
```



#### SpringCloud客户端访问

##### 使用,分割

```
spring:
  cloud:
    nacos:
      config:
        server-addr: 192.168.29.1:8842,192.168.29.1:8840,192.168.29.1:8841
        file-extension: yaml
```

##### 使用Nginx负载



## NacosSync 迁移其他注册中心的数据

官方文档

https://nacos.io/zh-cn/docs/nacos-sync-use.html







CMDB 

Nacos官网提供了结合Prometheus和Grafana实现Metrics监控的示例。



## CAP 定律

推荐 DNS 解析 Nacos 地址，或者使用 Nginx 进行 TCP 层代理，这样配置的 Nacos 地址信息就不会跟随 Nacos 的 IP 变化而变化。

Nacos 的注册中心的持久化是存储在文件中的，MySQL 存储的是配置；

## Raft 协议

### Raft 实现原理

1. leader 选举：集群中必须存在一个 leader 节点。
2. 日志复制：leader 节点接受来自客户端的请求将这些序列化日志同步到其它的节点。
3. 安全性：如果某个节点已经将一条提交过的数据输入 Raft 状态机，其他节点不能将相同索引的另一条数据输入到 Raft 状态机中执行。

动画

http://thesecretlivesofdata.com/raft/

## MSE

阿里云上面已经有微服务引擎

## 创建用户与修改密码

连接密码

Nginx



![ ](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219205833760.png) 





## 数据库配置

修改nacos配置

配置文件库

![image-202 20219223459138](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220219223459138.png) 

















































































































