# 从单机到微服务架构演化

## 微服务和单体应用向微服务异构平台架构演变

**SOA   ESB**



面向服务开发



## SpringCloud Alibaba/Netflix生态与依赖关系



权限可以在网关层做

```
监控中心
SpringCloud Admin：整体的监控平台，能够看到所有服务的状态
```



```
服务提供方：
Actuator 实时上报自己的信息做健康检查
```

```
权限：
Spring security OAuth2.0
JWT
```

```
分布式事务：
Alibaba Seata 开源
Gts 产品
```

```
链路追踪：
SpringCloud-sleuth
zipkin
```

```
分布式锁：
Zookeeper Curator
RedLock
```

```
熔断、降级
Netflix Hystrix
```

```
消息:
Spring Cloud Stream
Spring Messaging
Spring Integrantion
Apache Canel
```

## SpringCloudAlibabaNetflix生态与依赖关系(1)





## SpringCloudAlibabaNetflix生态与依赖关系(2)

```
远程调用：
是把服务的实现类拉回来了，还是把结果拉回来了
```



Nginx建立在性能不足的情况下要配Nginx集群

**TPS：代表的是transaction是有状态的信息，例如：查数据库，或者注册、发生计算**

**QPS：拉个图片或者没有任何的计算。**



## SpringCloudAlibabaNetflix生态与依赖关系(3)

**CDN服务：异地访问加速，静态流量，负载均衡**

**HttpDns：流量可控，DNS防劫持，基于HTTP协议解析域名**



## SpringCloudAlibabaNetflix生态与依赖关系(4)

DNS服务器一定要先有，才能去解析服务器



**httpDNS只适合APP的应用**

```
Spring Reactive Web webflux 替代websocket
```

```
Netty
```



## SpringCloudAlibabaNetflix生态与依赖关系(1)

```
服务监控
Actuator
```

```
远程服务同步调用
SpringCloud OpenFeign
Netflix Feign
```

![image-20220118202423612](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220118202423612.png) 



```
客户端的负载均衡
Netflix Ribbon
SpringCloud LoadBalancer
```

```
断路器
Netflix hystrix
Alibaba Sentinel
```

```
View渲染

Enjoy
JSON
```

```
服务注册中心
Eureka		Apache Zookeeper
Alibaba	Nacos     HashiCorp consul
```

### 企业级消息总线

```
消息集成服务
Spring Cloud Stream
Spring Messaging
Spring Integration
Apache Camel
```

![image-20220118205311519](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220118205311519.png) 

```
SpringData JPA
```

```
SpringData Rest
直接提供web api crud
SpringData Rest 是基于Spring Data repositories，分析实体之间的关系，为我们生成 Hypermedia API(HATEOAS) 峰哥的Http Restful API接口
类似于微软的ODATA

Spring HATEOAS

```

```
微服务链路追踪
SpringCloud-sleuth
zipkin
SkyWalking
```

```
分布式锁
RedLock
Curator
```





## 单体应用向微服务异构平台架构演变



## 搭建NetflixEureka(注册中心)



Eureka容易造成每个节点的信息不一致的问题

坑：

如果想要相互注册的话，我们注册的节点很有可能会出现在  unavailable-replicas里，要到registered-replicas才算是注册成功



Http 调JSON  第一级别

实体对象或资源的概念 第二级别

用不同的http方法调用resultAPI 第三级别

增加Api版本控制 第四级别

Spring Rest 第五级别



## 搭建单节点Eureka

eureka节点没有主从关系，每个节点都是可写入数据的，他们是靠集群内部的通讯机制来实现数据之间的复制

**eureka集群两种方式：1、让eureka之间互相的去交换信息；2、他们都是独立的eureka客户端，每个服务都单独的存储自己的信息**



## 单独的Eureka服务存储自己的信息架构图



**Eureka会有数据一致性问题**

![image-20220119130608908](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119130608908.png) 

**eureka有多网卡绑定机制**

## 配置主机名

![image-20220119224252467](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119224252467.png) 





![image-20220119225854075](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119225854075.png) 

![image-20220119225907444](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119225907444.png) 

**上图所示，euk2和euk1虽然都注册上来了  但是他们的状态都是unavailable-replicas不可用的副本，具体原因是出在哪呢？**

**原因：所有的集群副本的都是基于他有一个相同的分组，他们需要有一个唯一标识(类似于负载均衡的道理)，他们的application的name得是一样的。**

![image-20220119231536204](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119231536204.png) 

**如下图application.name的名字得是一样的**

![image-20220119231612865](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119231612865.png)  



![image-20220119231948259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119231948259.png) 



![image-20220119231916837](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220119231916837.png)

**如果换成是两个服务器的话，hostname可以配置成ip**

appname的性质和application.name是一样的

**euk1**

```properties
#是否将自己注册到Eureka Server,默认为true，由于当前就是server，故而设置成false，表明该服务不会向eureka注册自己的信息
#eureka.client.register-with-eureka=false
#是否从eureka server获取注册信息，由于单节点，不需要同步其他节点数据，用false
#eureka.client.fetch-registry=false
#设置服务注册中心的URL，用于client和server端交流
#eureka所有的操作调用，都是基于restful协议的
eureka.client.service-url.defaultZone=http://euk2.com:7002/eureka/
#eureka节点的名称(起一个名称就行)
#hostname是用来标识主机的不一样 
#appname是用来标识分组的，应用程序的名称
eureka.instance.hostname=euk1.com
#整个服务的名称
spring.application.name=EurekaServer
#tomcat的端口
server.port=7001
```

**euk2**

```properties
#是否将自己注册到Eureka Server,默认为true，由于当前就是server，故而设置成false，表明该服务不会向eureka注册自己的信息
#eureka.client.register-with-eureka=false
#是否从eureka server获取注册信息，由于单节点，不需要同步其他节点数据，用false
#eureka.client.fetch-registry=false
#设置服务注册中心的URL，用于client和server端交流
#eureka所有的操作调用，都是基于restful协议的
eureka.client.service-url.defaultZone=http://euk1.com:7001/eureka/

#eureka节点的名称(起一个名称就行)
#hostname是用来标识主机的不一样  
# appname是用来标识分组的，应用程序的名称
eureka.instance.hostname=euk2.com
#整个服务的名称
spring.application.name=EurekaServer
#tomcat的端口
server.port=7002
```



## Eureka原理

### **Register**

**服务注册**

想要参与服务注册发现的首例需要向Eureka服务器注册信息

注册在第一次心跳发生时提交

### **Renew**

**续租，心跳**

Eureka客户需要每30秒发送一次心跳来续租

更新通知Eureka服务器实例仍然是活动的，如果服务器在90秒内没有看到更新，它将从其注册表中删除实例

心跳包是客户端向服务器发送的：client->server

### Fetch Registry

Eureka客户端从服务器获取注册表信息并将其缓存在本地。

之后，客户端使用这些信息来查找其他服务

通过获取上一个获取周期和当前获取周期之间的增量更新，可以定期(每30秒)更新此信息

节点信息在服务器中保存的时间更长(大约3分钟)，因此获取节点信息时可能会再次返回相同的实例。Eureka客户端自动处理重复的信息

在获取增量之后，Eureka客户机通过比较服务器返回的实例计数来与服务器协调信息，如果由于某种原因信息不匹配，则再次获取整个注册表信息。

### 元数据

![image-20220120211651543](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220120211651543.png) 

![image-20220120211637010](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220120211637010.png) 



### EurekaClient

 **使用discoverClient接口提供的api调用eureka方法**![image-20220121124559267](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121124559267.png) 



![image-20220121124630321](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121124630321.png) 

```json
结果：
[
  {
    "scheme": "http",
    "host": "DESKTOP-QQQ61TH",
    "port": 80,
    "metadata": {
      "dalao": "luoyixuanhahaha",
      "management.port": "80"
    },
    "secure": false,
    "serviceId": "PROVIDER",
    "instanceId": "DESKTOP-QQQ61TH:provider:80",
    "uri": "http://DESKTOP-QQQ61TH:80",
    "instanceInfo": {
      "instanceId": "DESKTOP-QQQ61TH:provider:80",
      "app": "PROVIDER",
      "ipAddr": "172.17.1.49",
      "sid": "na",
      "homePageUrl": "http://DESKTOP-QQQ61TH:80/",
      "statusPageUrl": "http://DESKTOP-QQQ61TH:80/actuator/info",
      "healthCheckUrl": "http://DESKTOP-QQQ61TH:80/actuator/health",
      "vipAddress": "provider",
      "secureVipAddress": "provider",
      "countryId": 1,
      "dataCenterInfo": {
        "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
        "name": "MyOwn"
      },
      "hostName": "DESKTOP-QQQ61TH",
      "status": "UP",
      "overriddenStatus": "UNKNOWN",
      "leaseInfo": {
        "renewalIntervalInSecs": 30,
        "durationInSecs": 90,
        "registrationTimestamp": 1642740092462,
        "lastRenewalTimestamp": 1642740374396,
        "evictionTimestamp": 0,
        "serviceUpTimestamp": 1642740074370
      },
      "isCoordinatingDiscoveryServer": false,
      "metadata": {
        "dalao": "luoyixuanhahaha",
        "management.port": "80"
      },
      "lastUpdatedTimestamp": 1642740092462,
      "lastDirtyTimestamp": 1642740074326,
      "actionType": "ADDED"
    }
  }
]
```

![image-20220121124916274](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121124916274.png) 



使用EurekaClient来调用获取InstanceInfo对象来获取eureka注册的服务信息 

![image-20220121130127358](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121130127358.png) 

![image-20220121130709482](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121130709482.png) 

![image-20220121130853597](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121130853597.png) 

```java
package com.mashibing.eurekaserver.eurekaprovider;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author LYX
 * @description
 * @date 2022/1/20 13:17
 */
@RestController
public class MainController {

    @Autowired
    //抽象
    DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClient eurekaClient;


    @GetMapping("/getHi")
    public String getHi() {
        return "Hi";
    }

    @GetMapping("/client")
    public String client() {
        List<String> services = discoveryClient.getServices();
        System.out.println(ToStringBuilder.reflectionToString(services));
        return "Hi";
    }
    @GetMapping("/client2")
    public Object client2() {
        return discoveryClient.getInstances("provider");
    }
    @GetMapping("/client3")
    public Object client3() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("provider");
        for (ServiceInstance provider : serviceInstanceList) {
            System.out.println(ToStringBuilder.reflectionToString(provider));
        }
        return serviceInstanceList;
    }


    @GetMapping("/client4")
    public Object client4() {
        //具体服务
        //List<InstanceInfo> instanceInfos = eurekaClient.getInstancesById("DESKTOP-QQQ61TH:provider:80");
        //使用服务名找instance列表
        List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress("provider", false);
        for (InstanceInfo instanceInfo : instanceInfos) {
            System.out.println(ToStringBuilder.reflectionToString(instanceInfo));
        }
        if (instanceInfos.size() > 0) {
            //服务
            InstanceInfo instanceInfo = instanceInfos.get(0);
            if (instanceInfo.getStatus().equals(InstanceInfo.InstanceStatus.UP)) {
                String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + "/getHi";
                System.out.println(url);

                //获取url后使用restTemplate调用服务
                RestTemplate restTemplate = new RestTemplate();
                String restStr = restTemplate.getForObject(url, String.class);
                System.out.println(restStr);
            }
        }
        return instanceInfos;
    }
}
```

![image-20220121132106374](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121132106374.png) 

```java
@GetMapping("/ribbon")
    public Object ribbon() {

        //使用ribbon 完成客户端的负载均衡 获取服务列表 ribbon 会把状态为down状态的服务给我们过滤掉
        ServiceInstance instance = loadBalancerClient.choose("provider");

        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/getHi";
        System.out.println(url);

        //获取url后使用restTemplate调用服务
        RestTemplate restTemplate = new RestTemplate();
        String restStr = restTemplate.getForObject(url, String.class);
        System.out.println(restStr);

        return instance;
    }
```

eureka如果网络不达或者是服务不可用的时候可以向eureka把该台机器修改为down

![image-20220121133406610](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220121133406610.png) 

### 作业

- 使用ribbon完成负载均衡的调用
- 高可用集群搭起来

所有服务都想eureka提交自己的信息



##  自我保护机制

**宁可保留一些健康和不健康的，也不盲目的注销任何健康的服务**



自我保护触发

客户端每分钟续约数量小于客户端总数的85%时会出发保护机制



**自我保护机制的触发条件：**
（当每分钟心跳次数( renewsLastMin ) 小于 numberOfRenewsPerMinThreshold 时，并且开启自动保护模式开关( eureka.server.enable-self-preservation = true ) 时，触发自我保护机制，不再自动过期租约。）
numberOfRenewsPerMinThreshold = expectedNumberOfRenewsPerMin * 续租百分比( eureka.server.renewalPercentThreshold, 默认0.85 )
expectedNumberOfRenewsPerMin = 当前注册的应用实例数 x 2
为什么乘以 2：
默认情况下，注册的应用实例每半分钟续租一次，那么一分钟心跳两次，因此 x 2 。

服务实例数：10个，期望每分钟续约数：10 * 2=20，期望阈值：20*0.85=17，自我保护少于17时 触发。

剔除：

```java
    AbstractInstanceRegistry
    
    public void evict(long additionalLeaseMs) {
        logger.debug("Running the evict task");

        if (!isLeaseExpirationEnabled()) {
            logger.debug("DS: lease expiration is currently disabled.");
            return;
    }
    此代码意思：if中判断为true，不走此逻辑，走下面的剔除。如果if为false。走此逻辑，不剔除。
```

```java
PeerAwareInstanceRegistryImpl

    @Override
    public boolean isLeaseExpirationEnabled() {
        if (!isSelfPreservationModeEnabled()) {
        //如果打开自我保护，不进入此逻辑。
            // The self preservation mode is disabled, hence allowing the instances to expire.
            return true;
        }
        return numberOfRenewsPerMinThreshold > 0 && getNumOfRenewsInLastMin() > numberOfRenewsPerMinThreshold;
    }
```

**关闭**

```properties
eureka.server.enable-self-preservation=false
```



如何控制服务上下线？



## 使用SpringBoot2xActuator监控应用

**向我们每一个端点，来对外暴露本端点的一些信息的**

**eureka服务端的依赖中本身就带有actuator，我们只需要在客户端去引入就行**

**Client端配置Actuator**

```xml
<!-- 用来上报节点信息的 -->
<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
#开启actuator所有端点
management.endpoints.web.exposure.include=*
#开启shutdown端点(可以远程关闭这个微服务的节点)
management.endpoint.shutdown.enabled=true
```

![image-20220123152115728](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123152115728.png) 

**只支持post请求，不支持get请求**

![image-20220123152240848](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123152240848.png) 

![image-20220123152256398](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123152256398.png) 



### 开启手动控制

在client端配置：将自己真正的健康状态传播到server。

```yaml
#上报服务的正式状态
eureka:
  client:
    healthcheck:
      enabled: true
```

### Client端配置Actuator

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



### 改变健康状态的Service

```java
@Service
public class HealthStatusService implements HealthIndicator{

	private Boolean status = true;

	public void setStatus(Boolean status) {
		this.status  = status;
	}

	@Override
	public Health health() {
		// TODO Auto-generated method stub
		if(status)
		return new Health.Builder().up().build();
		return new Health.Builder().down().build();
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return this.status.toString();
	}
```

```java
/**测试接口**/
   @GetMapping("/health")
    public String health(@RequestParam("status") Boolean status) {

        healthStatusService.setStatus(status);
        return healthStatusService.getStatus();
    }
```

上线

![image-20220123153954997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123153954997.png) 

下线：

![image-20220123154044353](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123154044353.png) 

![image-20220123154108113](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123154108113.png)

## Eureka安全配置

### 开启Eureka安全连接

```
spring.security.user.name=yiming
spring.security.user.password=123
```

 现在是单节点了，所以要把这两个配置要解开，不向别人注册 也不拉去别人的信息

![image-20220123161403127](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123161403127.png) 



![image-20220123161623676](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123161623676.png) 



加完之后服务就没有连接权限了

![image-20220123161741964](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123161741964.png)

在连接地址中加自己的账号密码就能连接了

![image-20220123161909642](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123161909642.png)



### 如果服务注册报错

```
Root name 'timestamp' does not match expected ('instance') for type [simple
```

是默认开启了防止跨域攻击

#### 手动关闭

在**服务端**增加配置类

```
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable();
		super.configure(http);
	}

}
```

**security提供的跨域攻击保护**



## Ribbon

### 两种负载均衡

**基于客户端的负载均衡**

![image-20220123164029723](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123164029723.png)

**基于服务器的负载均衡** 

![image-20220123164316825](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123164316825.png) 



![image-20220123170640646](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123170640646.png) 



### 切换负载均衡策略

**注解方式**

![image-20220123212904122](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123212904122.png) 

**手动写负载均衡策略**

```java
/**
     * 手动负载均衡
     * @return
     */
    @GetMapping("/loadBalancing")
    public Object loadBalancing() {
        List<ServiceInstance> instanceList = discoveryClient.getInstances("provider");

        System.out.println("instance" + instanceList);


        //随机
        int i = new Random().nextInt(instanceList.size());
        AtomicInteger atomicInteger = new AtomicInteger();
        //自定义轮循算法
        int andIncrement = atomicInteger.getAndIncrement();
        System.out.println("-----" + andIncrement % instanceList.size());

        ServiceInstance instance1 = instanceList.get(andIncrement % instanceList.size());

        for (ServiceInstance instance : instanceList) {
            instance.getMetadata(); //权重 1~9

        }
        
        System.out.println(instance1);
        return null;
    }
```

#### 配置文件

针对服务定ribbon策略：

```sh
provider.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

给所有服务定ribbon策略：

```sh
ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

属性配置方式优先级高于Java代码。

### Ribbon脱离Eureka

```sh
ribbon.eureka.enabled=false
ribbon.listOfServers=localhost:80,localhost:81
```

为service-sms设置 请求的网络地址列表。

Ribbon可以和服务注册中心Eureka一起工作，从服务注册中心获取服务端的地址信息，也可以在配置文件中使用listOfServers字段来设置服务端地址。

 

添加LoadBalanced注解，这样ribbon就会帮我们转化地址

![image-20220123220800132](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123220800132.png) 

### 依赖注入

```java
@Bean
// 开启负载均衡
@LoadBalanced
RestTemplate restTemplate() {
	return new RestTemplate();
}
```

接下来便可以使用资源地址调用服务

```java
String url ="http://provider/getHi";
String respStr = restTemplate.getForObject(url, String.class);
```



### 负载均衡替换成基于服务端的

服务的健康状态需要每个节点去自己上报，只有两种状态：UP/DOWN





## SpringCloud 全栈快速上手（四）*****



### Restful风格的API

Rest服了风格的api接口规范

**要先将资源定义出来**

![image-20220123230712671](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123230712671.png) 

**针对单表  不再重复 crud  SpringData Rest**



**为什么要用SpringCloud？**

SpringCloud调起服务资源的时候是硬编码的**不容易扩展**

SpringCloud基于http协议的，可以做异构平台，跨语言

1、他的生态非常的完整

2、异构平台(可以调不同语言的服务，跨平台的重要性)

![image-20220123232008534](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123232008534.png) 

3、可插拔式的服务(类似于长连接请求之间的依赖性太高了，而http请求每次请求的服务地址还不一定，无状态服务)，解决了网络的不可靠

![image-20220123232356793](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123232356793.png) 

![image-20220123232422089](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220123232422089.png) 

4、几乎没有配置文件，基本上都是靠约定

**无限的提高了可用性，稍微的降低了一些数据一致性**



**请求方**

```java
package com.mashibing.eurekaserver.eurekaprovider;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author LYX
 * @description
 * @date 2022/1/24 12:26
 */
@RestController
public class RestTempleteController {

    @Autowired
    //抽象
    DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/client10")
    public Object client10() {

        String url ="http://provider/getHi";
        //获取url后使用restTemplate调用服务
        String restStr = restTemplate.getForObject(url, String.class);

        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);

        System.out.println("entity:" + entity);


        System.out.println(restStr);

        return restStr;
    }



    @GetMapping("/client11")
    public Object client11() {

        String url ="http://provider/getMap";
        //获取url后使用restTemplate调用服务
        Map<String, String> restStr = restTemplate.getForObject(url, Map.class);

        //ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

        System.out.println("entity:" + restStr.get("hello"));

        System.out.println(restStr);

        return restStr;
    }


    @GetMapping("/client12")
    public Object client12() {

        String url ="http://provider/getObject";
        //获取url后使用restTemplate调用服务
        Person restStr = restTemplate.getForObject(url, Person.class);

        //ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);

        System.out.println("entity:" + restStr.toString());

        System.out.println(restStr);

        return restStr;
    }

    @GetMapping("/client13")
    public Object client13() {
        //{1}其实只是一个占位符，在发起请求的时候他会用uriVariables的值替换
        String url ="http://provider/getObject1?name{1}";
        //获取url后使用restTemplate调用服务
        Person restStr = restTemplate.getForObject(url, Person.class, "罗老六123");
        System.out.println("entity:" + restStr.toString());
        System.out.println(restStr);
        return restStr;
    }
    
    @GetMapping("/client14")
    public Object client14() {
        //占位符需要替换，要跟map里的key对上
        String url = "http://provider/getObject1?name={name}";
        //获取url后使用restTemplate调用服务
        Map<String, String> map = Collections.singletonMap("name", "xiao666");
        Person restStr = restTemplate.getForObject(url, Person.class, map);

        System.out.println("entity:" + restStr.toString());
        System.out.println(restStr);
        return restStr;
    }
    
    @GetMapping("/client15")
    public Object client15() {
        //占位符需要替换，要跟map里的key对上
        String url = "http://provider/postObj";
        //获取url后使用restTemplate调用服务
        Map<String, String> map = Collections.singletonMap("name", "xiao612366");
        //请求发过去返回一个location，做资源的重定向
        Person object = restTemplate.postForObject(url, map, Person.class);

        System.out.println("object:" + object.toString());
        System.out.println(object);
        return object;
    }
}
```

**服务端**

```java
@GetMapping("/getHi")
    public String getHi() {
        return "Hi 老子的端口是"+ port;
    }

    @GetMapping("/health")
    public String health(@RequestParam("status") Boolean status) {

        healthStatusService.setStatus(status);
        return healthStatusService.getStatus();
    }

    @GetMapping("/getMap")
    public Map<String, String> getMap() {
        return Collections.singletonMap("hello", "baby");
    }

    @GetMapping("/getObject")
    public Person getObject() {
        Person person = new Person();
        person.setId(123123123);
        person.setName("老六");
        return person;
    }
    @GetMapping("/getObject1")
    public Person getObject1(@RequestParam String name) {
        Person person = new Person(100, name);
        return person;
    }
```

#### PostForLocation

使用postForLocation的时候需要加respon.addHeader("Location, uri.toString");

**调用方**

```
		String url ="http://provider/postParam";
		   
		Map<String, String> map = Collections.singletonMap("name", " memeda");
		URI location = restTemplate.postForLocation(url, map, Person.class);
		
		System.out.println(location);
```

**生产方**

需要设置头信息，不然返回的是null

```
	public URI postParam(@RequestBody Person person,HttpServletResponse response) throws Exception {

	URI uri = new URI("https://www.baidu.com/s?wd="+person.getName());
	response.addHeader("Location", uri.toString());
```

### 

### 拦截器

需要实现`ClientHttpRequestInterceptor`接口

```
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		System.out.println("拦截啦！！！");
		System.out.println(request.getURI());

		ClientHttpResponse response = execution.execute(request, body);

		System.out.println(response.getHeaders());
		return response;
	}
```

添加到resttemplate中

```
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
		return restTemplate;
	}
```

### 远程服务调用RestTemplate





### 声明式服务调用Feign

请求到了之后先到controller里，api中检测到FeignClient注解，拦截，请求



Feign

好处就是减少代码耦合

方便异构



Feign传参的时候需要添加@RequestParam或者RequestBody注解，不然值传不过去

**不是在我们自己的controller控制层加，而是在Feign上面加注解**

因为我们的controller只是接一次我们自己的web请求的，是到我们当前的Consumer的MVC框架里的，

Feign这里 getMapping 是给Feign看的` get请求 user-provider/getMap?id={1} ，@RequestParam("id") 也是给Feign看的





### Get和POST

Feign默认所有带参数的请求都是Post，想要使用指定的提交方式需引入依赖

```
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-httpclient</artifactId>
        </dependency>
```



并指明提交方式

```
@RequestMapping(value = "/alived", method = RequestMethod.POST)
@GetMapping("/findById")
```



![image-20220125125629976](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220125125629976.png) 



## Ribbon的重试策略与服务恢复

**1、建立连接超时**

**2、业务线程超时**

所以我们在设计超时操作的时候也要分为两部分来设计，第一部分是建立连接的超时，第二部分是业务线程的超时



这些都是在客户端使用的

**ribbon提供了负载均衡和重试机制	**

### 超时

Feign默认支持Ribbon；Ribbon的重试机制和Feign的重试机制有冲突，所以源码中默认关闭Feign的重试机制,使用Ribbon的重试机制

```
#连接超时时间(ms)
ribbon.ConnectTimeout=1000
#业务逻辑超时时间(ms)
ribbon.ReadTimeout=6000
```

### 重试

```
#同一台实例最大重试次数,不包括首次调用
ribbon.MaxAutoRetries=1
#重试负载均衡其他的实例最大重试次数,不包括首次调用
ribbon.MaxAutoRetriesNextServer=1
#是否所有操作都重试
ribbon.OkToRetryOnAllOperations=false
```

使用ribbon重试机制，请求失败后，每个6秒会重新尝试



## 手动实现Hystrix原理,降级熔断和隔离

降级
 try{
        1.发起向服务方的请求
          1.1 判断连接超时
                -> 这次请求   记录到服务里
          1.2 尝试向其他服务器发起请求
         2.还是没成功

   } catch(Exception e) {

​     1.避免返回不友好的错误信息
​         提供降级方案
​         ->给一个友好的信息/页面
​         ->重试按钮
​         ->写到MQ队列中
​         ->联系电话或者其他......
​     2.
​     return  "请稍后重试"
   }

限流/隔离(隔离线程)
     我们每次发的都是http请求，线程消耗，所以每次请求都会开启一个线程(Tomcat线程池是处理总的业务线程数)
     为了避免线程开启过多，造成线程堆积
     map(uri, 线程数) 相同的uri存到一组中限定线程的线程数
     try{

​      if (当前线程满了) {
​          throw exception......
​      }

​     } catch(E) {

​     }
熔断
​     当失败次数(每次失败都会计数)达到某个阈值的时候，立即切断对于当前这台服务器所发起的所有请求
​     count ++;
​     if (count == 10) {
​         throw exception......
​     }

 问题:熔断之后就让他这样一直断吗?
请求的时候分为两种状态 请求/不请求
    /半请求
   开 代表请求   关 代表不请求   半开 代表半请求count ++;
     if (count == 10) {
          在抛异常之前抽出几个请求出来 尝试着请求
          new random == 1; -->发请求
          或者按时间来发
          如果成功之后将count 归零
         throw exception......
     }

