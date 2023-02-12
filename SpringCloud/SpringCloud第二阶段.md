# SpringCloud第二阶段



## Hystrix整合Feign

hystrix有一个隔离的功能，可以隔离每个请求，针对于具体的应用服务开启不同的线程池，在不同的线程池中发起不用的请求。

### 整合Resttemplate

#### Service

```
	@HystrixCommand(fallbackMethod = "back")
	public String alive() {
		// 自动处理URL
		
		RestTemplate restTemplate = new RestTemplate();
		
		String url ="http://user-provider/User/alive";
		String object = restTemplate.getForObject(url, String.class);
		
		return object;
		
	}
	
	
	public String back() {
		
		return "请求失败~bbb...";
	}
```

#### 启动类

```
@EnableCircuitBreaker
```

### 



### **整合Feign**

#### 配置

```
feign.hystrix.enabled=true
```

#### 接口

```
#通过fallback配置熔断策略     fallback需要给spring托管
@FeignClient(name = "user-provider",fallback = AliveBack.class)
public interface ConsumerApi {

	@RequestMapping(value = "/User/alive",method = RequestMethod.GET)
	public String alive();
	
	@RequestMapping(value = "/User/getById",method = RequestMethod.GET)
	public String getById(Integer id);
}
```

**降级的话就相当于服务还能凑合用而不是不能用了**





## Hystrix整合Resttemplate 

使用Feign自带restTemplate，如果要用Resttemplate的话就要加上启动注解

#### 启动类

```java
@EnableCircuitBreaker
```

#### Service

```java
	@HystrixCommand(fallbackMethod = "back")
	public String alive() {
		// 自动处理URL
		
		RestTemplate restTemplate = new RestTemplate();
		
		String url ="http://user-provider/User/alive";
		String object = restTemplate.getForObject(url, String.class);
		
		return object;
		
	}
	
	
	public String back() {
		
		return "请求失败~bbb...";
	}
```



## Hystrix线程隔离和信号量隔离



**信号量隔离和线程隔离之间的区别**

基于线程池的隔离

![image-20220127163717635](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220127163717635.png) 



![image-20220129135812448](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220129135812448.png) 





![image-20220129140347090](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220129140347090.png) 





**线程池的失败策略**

1. 异步网络请求，解放worker的线程阻塞
2. 线程池内部，异常隔离



### Servlet3.1

单节点应用不推荐使用这种的模式，最好是要有业务阻塞的这种业务处理最好用这种模式处理。

![image-20220129143638300](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220129143638300.png) 



**http://localhost:92/actuator/hystrix.stream** 404问题

解决：https://www.jianshu.com/p/4b515d81fee7

默认情况下hystrix使用线程池控制请求隔离

线程池隔离技术，是用 Hystrix 自己的线程去执行调用；而信号量隔离技术，是直接让 tomcat 线程去调用依赖服务。信号量隔离，只是一道关卡，信号量有多少，就允许多少个 tomcat 线程通过它，然后去执行。



信号量隔离主要维护的是Tomcat的线程，不需要内部线程池，更加轻量级。

配置

```
hystrix.command.default.execution.isolation.strategy 隔离策略，默认是Thread, 可选Thread｜Semaphore
thread 通过线程数量来限制并发请求数，可以提供额外的保护，但有一定的延迟。一般用于网络调用
semaphore 通过semaphore count来限制并发请求数，适用于无网络的高并发请求
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds 命令执行超时时间，默认1000ms
hystrix.command.default.execution.timeout.enabled 执行是否启用超时，默认启用true
hystrix.command.default.execution.isolation.thread.interruptOnTimeout 发生超时是是否中断，默认true
hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests 最大并发请求数，默认10，该参数当使用ExecutionIsolationStrategy.SEMAPHORE策略时才有效。如果达到最大并发请求数，请求会被拒绝。理论上选择semaphore size的原则和选择thread size一致，但选用semaphore时每次执行的单元要比较小且执行速度快（ms级别），否则的话应该用thread。
semaphore应该占整个容器（tomcat）的线程池的一小部分。
```



#### Feign下配置

```
hystrix.command.default.execution.isolation.strategy=SEMAPHORE
```



### 开启dashboard

启动类

```
@EnableHystrixDashboard
```



引入依赖

```
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>
				spring-cloud-starter-netflix-hystrix-dashboard
			</artifactId>
		</dependency>
		
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

健康上报

http://localhost:90/actuator/hystrix.stream

图形化

http://localhost:90/hystrix

**问题**

线程隔离怎么能和调一个宕机得服务走failback联系在一起



## 网关

### 概念和功能

![image-20220204162759685](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220204162759685.png) 



![image-20220204162810796](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220204162810796.png)  



**隧道模式**

好处：灵活处理

![image-20220204163521253](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220204163521253.png) 



Zuul是Netflix开源的微服务网关，核心是一系列过滤器。这些过滤器可以完成以下功能。

1. 是所有微服务入口，进行分发。
2. 身份认证与安全。识别合法的请求，拦截不合法的请求。
3. 监控。在入口处监控，更全面。
4. 动态路由。动态将请求分发到不同的后端集群。
5. 压力测试。可以逐渐增加对后端服务的流量，进行测试。
6. 负载均衡。也是用ribbon。
7. 限流（望京超市）。比如我每秒只要1000次，10001次就不让访问了。
8. 服务熔断



### 业务网关隧道模式

**网关中所有的拒绝策略都要尽可能的前置，这样可以更大的降低我们系统中很多不必要的开销**

新建项目引入依赖

```
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
		</dependency>
```



配置文件

```
eureka.client.service-url.defaultZone=http://euk1.com:7001/eureka/
spring.application.name=zuulserver
server.port=80
```

启动类

```
@EnableZuulProxy
```

测试访问

网关会将服务名转换成具体服务的ip和端口，实际进行访问

```
http://localhost/consumer/alive
```

### 负载均衡

启动两个Consumer

轮询访问上面地址，会看到返回结果中，端口一直轮询在变。说明负载均衡生效了，默认是轮询

```
consumer.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

### 路由端点

调试的时候，看网关请求的地址，以及 映射是否正确。网关请求有误时，可以通过此处排查错误。

配置

```
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
management.endpoint.health.enabled=true
management.endpoint.routes.enabled=true
```

### 配置指定微服务的访问路径

1. 通过服务名配置（虚拟主机名）

```sh
zuul.routes.consumer=/xxoo/**
```

配置前先访问，然后做对比。

2.自定义映射

```
#访问xx下的所有地址，都会转发到下面的url中
zuul.routes.xx.path=/xx/**
zuul.routes.xx.url=http://loethin.com
```

![image-20220208134440173](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208134440173.png)







 

3. 自定义下的负载均衡

```
zuul.routes.xx.path=/xx/**
zuul.routes.xx.service-id=cuid

cuid.ribbon.listOfServers=localhost:82,localhost:83
ribbon.eureka.enabled=false
```





## 链路追踪

### Sleuth简介

Sleuth是Spring cloud的分布式跟踪解决方案。

1. span(跨度)，基本工作单元。一次链路调用，创建一个span，

   span用一个64位id唯一标识。包括：id，描述，时间戳事件，spanId,span父id。

   span被启动和停止时，记录了时间信息，初始化span叫：root span，它的span id和trace id相等。

2. trace(跟踪)，一组共享“root span”的span组成的树状结构 称为 trace，trace也有一个64位ID，trace中所有span共享一个trace id。类似于一颗 span 树。

3. annotation（标签），annotation用来记录事件的存在，其中，核心annotation用来定义请求的开始和结束。

   - CS(Client Send客户端发起请求)。客户端发起请求描述了span开始。
   - SR(Server Received服务端接到请求)。服务端获得请求并准备处理它。SR-CS=网络延迟。
   - SS（Server Send服务器端处理完成，并将结果发送给客户端）。表示服务器完成请求处理，响应客户端时。SS-SR=服务器处理请求的时间。
   - CR（Client Received 客户端接受服务端信息）。span结束的标识。客户端接收到服务器的响应。CR-CS=客户端发出请求到服务器响应的总时间。



#### Sleuth单独

1. pom

   每个需要监控的系统

```sh
<!-- 引入sleuth依赖 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-sleuth</artifactId>
		</dependency>
```

测试点：

1. 启动eureka 7900，service-sms 8002，api-driver 9002.
2. 访问一次。看日志结果。

```sh
 [api-driver,1a409c98e7a3cdbf,1a409c98e7a3cdbf,true] 
 
 [服务名称，traceId（一条请求调用链中 唯一ID），spanID（基本的工作单元，获取数据等），是否让zipkin收集和展示此信息]

看下游
[service-sms,1a409c98e7a3cdbf,b3d93470b5cf8434,true]

traceId， 是一样的。

服务名必须得写。
```

#### zipkin

上面排错看日志，很原始。刀耕火种，加入利器 zipkin。

zipkin是twitter开源的分布式跟踪系统。

原理收集系统的时序数据，从而追踪微服务架构中系统延时等问题。还有一个友好的界面。



由4个部分组成：

Collector、Storage、Restful API、Web UI组成

采集器，存储器，接口，UI。



原理：

sleuth收集跟踪信息通过http请求发送给zipkin server，zipkin将跟踪信息存储，以及提供RESTful API接口，zipkin ui通过调用api进行数据展示。

默认内存存储，可以用mysql，ES等存储。



操作步骤：

1. 每个需要监听的服务的pom中添加。

```sh
<!-- zipkin -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zipkin</artifactId>
		</dependency>
```

2. 每个需要监听的服务yml中

```sh
spring:
  #zipkin
  zipkin:
    base-url: http://localhost:9411/
    #采样比例1
  sleuth:
    sampler:
      rate: 1  
```

3. 启动zipkin。

```sh
jar包下载：curl -sSL https://zipkin.io/quickstart.sh | bash -s
我放到了 目录：C:\github\online-taxi-demo  下面。


java -jar zipkin.jar

或者docker：
docker run -d -p 9411:9411 openzipkin/zipkin

```



## SpringCloud Admin健康检查

### Admin服务器端

### 引入依赖

```xml
server端：
<!-- Admin 服务 -->
		<dependency>
			<groupId>de.codecentric</groupId>
			<artifactId>spring-boot-admin-starter-server</artifactId>
		</dependency>
		<!-- Admin 界面 -->
		<dependency>
			<groupId>de.codecentric</groupId>
			<artifactId>spring-boot-admin-server-ui</artifactId>
		</dependency>


```

启动类

```java
package com.mashibing.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}

```

### 微服务端(客户端)

```xml
微服务端：
		<!-- Admin 服务 -->
      <dependency>
        <groupId>de.codecentric</groupId>
        <artifactId>spring-boot-admin-starter-client</artifactId>
        <version>2.2.1</version>
      </dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```

配置

```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
#springcloud admin 的地址
spring.boot.admin.client.url=http://localhost:8080
```

### 邮件通知

1. pom

   ```sh
   <dependency>
   			<groupId>org.springframework.boot</groupId>
   			<artifactId>spring-boot-starter-mail</artifactId>
   		</dependency>
   ```

2. yml

   ```sh
   #邮件设置
   spring.mail.host=smtp.qq.com
   spring.mail.username=971512214
   spring.mail.password=
   spring.mail.properties.mail.smpt.auth=true
   spring.mail.properties.mail.smpt.starttls.enable=true
   spring.mail.properties.mail.smpt.starttls.required=true
   
   #收件邮箱
   spring.boot.admin.notify.mail.to: 971512214@qq.com
   
   # 发件邮箱 该地址要和发邮件的账号一模一样(spring.mail.username)
   spring.boot.admin.notify.mail.from: 971512214@qq.com
   ```

**password**

![image-20220208154246891](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208154246891.png) 

![image-20220208154609259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208154609259.png) 



**当停止服务的时候 admin会通过邮件发送报告**

![image-20220208155054151](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208155054151.png) 





### 钉钉群通知

#### 启动类

```java
package com.mashibing.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;

@SpringBootApplication
@EnableAdminServer
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	   @Bean
	    public DingDingNotifier dingDingNotifier(InstanceRepository repository) {
	        return new DingDingNotifier(repository);
	    }
}

```

#### 通知类

```
package com.mashibing.admin;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import reactor.core.publisher.Mono;

public class DingDingNotifier extends AbstractStatusChangeNotifier  {
	public DingDingNotifier(InstanceRepository repository) {
        super(repository);
    }
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        String serviceName = instance.getRegistration().getName();
        String serviceUrl = instance.getRegistration().getServiceUrl();
        String status = instance.getStatusInfo().getStatus();
        Map<String, Object> details = instance.getStatusInfo().getDetails();
        StringBuilder str = new StringBuilder();
        str.append("服务预警 : 【" + serviceName + "】");
        str.append("【服务地址】" + serviceUrl);
        str.append("【状态】" + status);
        str.append("【详情】" + JSONObject.toJSONString(details));
        return Mono.fromRunnable(() -> {
            DingDingMessageUtil.sendTextMessage(str.toString());
        });
    }
}

```

#### 发送工具类

```
package com.mashibing.admin;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class DingDingMessageUtil {
	public static String access_token = "Token";
    public static void sendTextMessage(String msg) {
        try {
            Message message = new Message();
            message.setMsgtype("text");
            message.setText(new MessageInfo(msg));
            URL url = new URL("https://oapi.dingtalk.com/robot/send?access_token=" + access_token);
            // 建立 http 连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            conn.connect();
            OutputStream out = conn.getOutputStream();
            String textMessage = JSONObject.toJSONString(message);
            byte[] data = textMessage.getBytes();
            out.write(data);
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            byte[] data1 = new byte[in.available()];
            in.read(data1);
            System.out.println(new String(data1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

#### 消息类

```
package com.mashibing.admin;

public class Message {
	private String msgtype;
    private MessageInfo text;
    public String getMsgtype() {
        return msgtype;
    }
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
    public MessageInfo getText() {
        return text;
    }
    public void setText(MessageInfo text) {
        this.text = text;
    }
}





package com.mashibing.admin;

public class MessageInfo {
    private String content;
    public MessageInfo(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}



```

在钉钉群中创建一个只能机器人，并设置关键词

![image-20220208203538490](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208203538490.png)



**会生成webhook地址，将地址中的token放到URL中**

![image-20220208203802532](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208203802532.png) 

**停止服务后钉钉消息没有发送成功，后台抛异常**

![image-20220208204137490](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208204137490.png) 

**是因为我们设置的关键字没有在内容中**

![image-20220208204751965](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208204751965.png) 

![image-20220208204803618](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208204803618.png) 





### 微信通知

服务号 模板消息



### 为什么需要配置中心

单体应用，配置写在配置文件中，没有什么大问题。如果要切换环境 可以切换不同的profile（2种方式），但在微服务中。

1. 微服务比较多。成百上千，配置很多，需要集中管理。

2. 管理不同环境的配置。

3. 需要动态调整配置参数，更改配置不停服。

   

### 配置中心介绍

分布式配置中心包括3个部分：

1. 存放配置的地方：git ，本地文件 等。
2. config  server。从 1 读取配置。
3. config client。是 config server 的客户端 消费配置。

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/222.png)



## 什么是配置中心及应用场景

1. 重启
2. 重新加载
3. 单服务重新加载
4. 通过配置中心一次完成所有微服务的配置重载



![image-20220208211937450](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208211937450.png) 

## SpringCloudConfig和Github单机搭建和配置命名规则



**创建一个新的git分支**

![image-20220208214648757](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220208214648757.png) 

### 匹配规则

```
获取配置规则：根据前缀匹配
/{name}-{profiles}.properties
/{name}-{profiles}.yml
/{name}-{profiles}.json
#label分支名，可加可不加
/{label}/{name}-{profiles}.yml

name 服务名称
profile 环境名称，开发、测试、生产：dev qa prd
lable 仓库分支、默认master分支

匹配原则：从前缀开始。
```

### 分支读取

## 客户端配置

### 配置文件

修改 application.properties为bootstrap.properties

```properties
#直接URL方式查找配置中心
spring.cloud.config.uri=http://localhost:9999/
#通过注册中心查找
#spring.cloud.config.discovery.enabled=true
#spring.cloud.config.discovery.service-id=a-config
spring.cloud.config.profile=dev
spring.cloud.config.label=dev 
```

### 引入依赖

```
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-client</artifactId>
		</dependency>
```

### 使用远程配置

```
	
	@Value("${config.info}")
	String info;
```

服务名       配置名

consumer-dev.properties

```
config.info="config-dev,v1"
```

**直接使用@Value去远程读取配置文件的时候会报该注入失败，原因是配置文件的约定名需要叫bootstrap.properties，这样才能在我们注入之前去远程加载配置文件**

![image-20220209125117371](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209125117371.png) 

![image-20220209125123838](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209125123838.png) 

**每次修改配置文件之后客户端都得重新启动，我们可以使用@RefreshScope注解到Controller上**

![image-20220209132859456](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209132859456.png) 

**再使用actuator**

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**配置文件中在把端点打开**

```properties
management.endpoints.web.exposure.include=*
```

可以在不重启的情况下，修改远程配置文件的内容



**actuator配置之后我们就会有一个refresh端点的，我们需要去请求这个端点地址(要用post请求)，他就会给我们重新加载配置**

![image-20220209133440522](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209133440522.png) 



![image-20220209133627049](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209133627049.png) 

![image-20220209133638405](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209133638405.png) 

**但是他只更改单节点的配置，比如consumer有94和93 ，我们使用http://localhost:93/actuator/refresh这个地址去刷新的话就只有93更新了**



**所以我们有一个叫消息服务总线BUS**

**就是把我们的所有服务全部都接入到bus里面，底层实现就是MQ(要支持AMQP统一化的接口标准)**

RabbitMQ、Kafka

### 自动刷新

#### erlang安装

http://www.erlang.org/downloads

#### RabbitMQ安装

http://www.rabbitmq.com/install-windows.html

#### 环境变量

path中添加 `%ERLANG_HOME%\bin`



**到sbin目录下执行下面的命令**

D:\LOETHINFILES\rabbitMQ\rabbitmq_server-3.9.13\sbin

```bash
# 开启RabbitMQ节点
rabbitmqctl start_app
# 开启RabbitMQ管理模块的插件，并配置到RabbitMQ节点上
rabbitmq-plugins enable rabbitmq_management
```

#### 管理界面

http://localhost:15672

用户名密码均为guest

#### 服务配置

配置文件

```
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

依赖

```pom
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-bus-amqp</artifactId>
		</dependency>
```

#### 测试

启动两个微服务

修改配置文件后向其中一个端点发送post请求

http://localhost:91/actuator/bus-refresh

观察另一个服务是否也跟着刷新了



![image-20220209164707125](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209164707125.png) 



我们使用Bus让RabbitMQ将更新消息暂存到队列中，这样用actuator对某一个服务refresh的时候其他服务都会通过队列去拉取并且更新(**注意这里使用的refresh用的是**http://localhost:93/actuator/bus-refresh)



![image-20220209164953632](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209164953632.png) 

![image-20220209165227180](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209165227180.png) 



**还可以在总线配置上actuator和bus(就是Config-Center,记得将端点都打开)**

![image-20220209170031611](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209170031611.png) 

```json
{"_links":{"self":{"href":"http://localhost:70/actuator","templated":false},"archaius":{"href":"http://localhost:70/actuator/archaius","templated":false},"beans":{"href":"http://localhost:70/actuator/beans","templated":false},"caches-cache":{"href":"http://localhost:70/actuator/caches/{cache}","templated":true},"caches":{"href":"http://localhost:70/actuator/caches","templated":false},"health":{"href":"http://localhost:70/actuator/health","templated":false},"health-path":{"href":"http://localhost:70/actuator/health/{*path}","templated":true},"info":{"href":"http://localhost:70/actuator/info","templated":false},"conditions":{"href":"http://localhost:70/actuator/conditions","templated":false},"configprops":{"href":"http://localhost:70/actuator/configprops","templated":false},"bus-env":{"href":"http://localhost:70/actuator/bus-env","templated":false},"bus-env-destination":{"href":"http://localhost:70/actuator/bus-env/{destination}","templated":true},"env":{"href":"http://localhost:70/actuator/env","templated":false},"env-toMatch":{"href":"http://localhost:70/actuator/env/{toMatch}","templated":true},"integrationgraph":{"href":"http://localhost:70/actuator/integrationgraph","templated":false},"loggers":{"href":"http://localhost:70/actuator/loggers","templated":false},"loggers-name":{"href":"http://localhost:70/actuator/loggers/{name}","templated":true},"heapdump":{"href":"http://localhost:70/actuator/heapdump","templated":false},"threaddump":{"href":"http://localhost:70/actuator/threaddump","templated":false},"metrics":{"href":"http://localhost:70/actuator/metrics","templated":false},"metrics-requiredMetricName":{"href":"http://localhost:70/actuator/metrics/{requiredMetricName}","templated":true},"scheduledtasks":{"href":"http://localhost:70/actuator/scheduledtasks","templated":false},"mappings":{"href":"http://localhost:70/actuator/mappings","templated":false},"refresh":{"href":"http://localhost:70/actuator/refresh","templated":false},"bus-refresh":{"href":"http://localhost:70/actuator/bus-refresh","templated":false},"bus-refresh-destination":{"href":"http://localhost:70/actuator/bus-refresh/{destination}","templated":true},"features":{"href":"http://localhost:70/actuator/features","templated":false},"service-registry":{"href":"http://localhost:70/actuator/service-registry","templated":false},"bindings-name":{"href":"http://localhost:70/actuator/bindings/{name}","templated":true},"bindings":{"href":"http://localhost:70/actuator/bindings","templated":false},"channels":{"href":"http://localhost:70/actuator/channels","templated":false}}}
```

**直接请求总线来完成远端服务配置文件的更新**

![image-20220209170156519](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220209170156519.png) 





























































































































































 
