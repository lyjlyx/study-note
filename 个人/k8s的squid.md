# k8s的squid

在Kubernetes (k8s) 的上下文中，Squid 通常指的是 Squid 缓存代理服务器的容器化版本。Squid 是一个广泛使用的开源缓存代理和Web服务器加速器，它可以缓存HTTP、HTTPS和FTP等协议的内容，减少带宽使用，提高内容加载速度，并且可以作为代理服务器来提高安全性和隐私性。

在 Kubernetes 环境中，Squid 可以部署为一个 Pod，或者作为一组 Pods 运行，以便提供高可用性和负载均衡。使用 Kubernetes 部署 Squid 可以带来以下好处：

1. **自动化部署和管理**：通过 Kubernetes 的声明式配置和自动化部署机制，可以轻松地将 Squid 集成到现有的DevOps流程中。

2. **可伸缩性**：可以根据流量和负载需求，自动或手动地扩展 Squid 实例的数量。

3. **高可用性**：Kubernetes 可以监控 Squid Pods 的健康状况，并在必要时自动替换故障实例。

4. **网络策略和安全**：可以利用 Kubernetes 的网络策略来控制哪些服务可以访问 Squid Pod，并确保通信安全。

要在 Kubernetes 上部署 Squid，你通常需要编写一个 Deployment 配置文件来定义 Squid Pod 的规格，包括镜像、资源限制、环境变量等。此外，你可能还需要配置 Service 资源以提供网络访问，以及可能的 ConfigMap 或 Secret 资源来管理配置文件和敏感数据。

例如，一个基本的 Squid Deployment 配置可能如下所示：

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: squid-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: squid
  template:
    metadata:
      labels:
        app: squid
    spec:
      containers:
      - name: squid
        image: some-registry/squid:latest
        ports:
        - containerPort: 3128
```

请注意，这只是一个非常基本的示例。实际部署时，你需要根据实际需求调整配置，包括设置正确的镜像仓库地址、版本标签、资源限制、持久化存储卷（如果需要缓存持久化）以及其他相关配置。



如果squid出现异常情况，没有在任何容器上我们可以将其删除重新安装

![img](data:image/png;base64,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)

因为使用的helm所以我们要使用helm相关命令删除

1. 首先，找出您想要删除的 release 的名称。您可以列出所有当前的 Helm releases 来找到它：

```
helm list --all-namespaces
```

2. 然后，使用以下命令删除 release：

```
helm uninstall <release-name> -n <namespace>
```



去helm官网

```
https://helm.sh/
```

![image-20240302160731441](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240302160731441.png) 

搜索要安装的squid

![image-20240302160758620](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240302160758620.png)



点击install

![image-20240302160835141](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240302160835141.png)

执行repository环境安装脚本

![image-20240302160857522](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240302160857522.png)



install chart要结合自己的kubeConfig文件来安装

这里我们已经有安装squid的了，则使用upgrade，初始环境的话使用install

```
helm upgrade squid-scrm-qywx lib42/squid --kubeconfig /data/cicd/kubeconfig/kube-config-test -n lspace-test
```

```
KUBECONFIG=/data/cicd/kubeconfig/kube-config-test helm upgrade -i squid-scrm-qywx lib42/squid -n lspace-test
```



这个时候squid已经建好了，我们使用k8s容器命令可以看到有对应命名的squid启动

![image-20240323173136359](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240323173136359.png) 





### Kubesphere

![image-20240608121930287](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608121930287.png)

![image-20240608121957930](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608121957930.png)

![image-20240608122007614](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608122007614.png)

### 腾讯云

这个时候在tke集群上也能搜到相关服务

![image-20240323173209486](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240323173209486.png)



我们需要给指定服务器打上squid的标签

![image-20240323173236622](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240323173236622.png) 

找台外网服务器将相关标签打上

![image-20240323173404815](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240323173404815.png)

![image-20240323173418581](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240323173418581.png)



这样就完成了相关的squid配置，相关应用使用，这个对象调用请求就可以从绑定的服务器IP出去了

```java
package vip.lspace.diga.qywx;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
public class RestTemplateQywxConfig {

    @Value("${http_proxy_qywx:}")
    private String proxyHost;

    @Bean
    public RestTemplate restQywxTemplate() {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
        return new RestTemplate(requestFactory);
    }

    /**
     * Apache HttpClient
     *
     * @return HttpClient
     */
    private HttpClient httpClient() {
        // 支持HTTP、HTTPS
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", SSLConnectionSocketFactory.getSocketFactory())
            .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setValidateAfterInactivity(2000);
        HttpHost httpHost = new HttpHost(proxyHost, 80);
        RequestConfig requestConfig = RequestConfig.custom()
            // 服务器返回数据(response)的时间，超时抛出read timeout
            .setSocketTimeout(65000)
            // 连接上服务器(握手成功)的时间，超时抛出connect timeout
            .setConnectTimeout(15000)
            // 从连接池中获取连接的超时时间，超时抛出ConnectionPoolTimeoutException
            .setConnectionRequestTimeout(15000)
            .setProxy(httpHost)
            .build();

        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        //新建RetryPolicy  也就是判断是否重新执行的机制
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        //配置的重试次数   //一直重新执行 3次
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        //新建BackOffPolicy机制  也就是重新执行时的相关设置
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        //执行操作的时间间隔
        exponentialBackOffPolicy.setInitialInterval(2 * 1000);
        //分别设置指数增长的倍数  则四次分别为 2，8，512，……
        exponentialBackOffPolicy.setMultiplier(2);
        //最大的间隔时间  为40秒
        exponentialBackOffPolicy.setMaxInterval(40 * 1000);
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
        return retryTemplate;
    }

}
```

![image-20240323173615100](C:/Users/97151/AppData/Roaming/Typora/typora-user-images/image-20240323173615100.png)









## 记开发环境squid异常问题

因为项目中的message容器没跑起来，查看报错原因是因为squid连接不上的问题（省略message报错）

我们通过`` kpod -o wide |grep squid``发现squi对应的容器一直处于Pending状态

![image-20240608172936630](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608172936630.png) 

查看squid相关详情``kubectl describe pod squid-7f655ccccc-xrzgn --namespace lspace-dev``

![image-20240608173049144](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608173049144.png) 

 

当时错误判断以为是squid的helm问题，所以对其helm进行了相关的重装，**具体步骤看上面的操作**

但是重装完成之后发现新的squid的一直是ImagePullBackOff状态

![image-20240608173407956](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608173407956.png) 

查看describe

![image-20240608173451931](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608173451931.png)

![image-20240608173710440](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608173710440.png)

发现是镜像拉取失败，以为是镜像源的问题（**其实就是公司网络太慢镜像源问题**）

重新设置了docker的镜像源

参考地址

```
https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
```

![image-20240608174106151](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608174106151.png) 

但是并没有什么用，该拉取下来还是拉取不下来，刚刚好公司有自己的腾讯云的私有仓库，我们先把地址换成腾讯云的镜像仓库地址看看能不能Pull下来。

```
 docker pull mirror.ccs.tencentyun.com/honestica/squid:4.69
```

![image-20240608175404427](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175404427.png) 

发现ping不通mirror.ccs.tencentyun.com（**应该是个人版问题**），所以用我们自己的私有镜像仓库地址看看，可以ping通

![image-20240608175511006](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175511006.png) 

那就直接先进行登录，生成登录链接

![image-20240608175546703](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175546703.png) 

登录成功，但是还是拉不下来

![image-20240608175621327](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175621327.png)

在私有仓中查询了一下没有发现对应的squid相关的镜像（但是我们之前用的个人版是有的）

**所以具体思路就是先从个人版拉取相关镜像，先在测试服务器（测试服务器是腾讯云的，所以不会有网络的困扰，很快）**

查询对应的squid镜像名字

![image-20240608175839641](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175839641.png) 

 将对应的镜像名称打上tag标签，然后push到私有仓中

```
docker tag honestica/squid:4.69 docker-bj.tencentcloudcr.com/lspace/squid:4.69
docker push docker-bj.tencentcloudcr.com/lspace/squid:4.6
```

![image-20240608180000131](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608180000131.png) 

![image-20240608175924019](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608175924019.png) 

镜像仓库中有了

![image-20240608180057136](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608180057136.png)

随后我们回到开发服务器

根据相关的仓库地址把相关的tag拉下来

```
docker pull docker-bj.tencentcloudcr.com/lspace/squid:4.69
```

打上标签，就行了

```
docker tag docker-bj.tencentcloudcr.com/lspace/squid:4.69 honestica/squid:4.69
```

![image-20240608180254101](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608180254101.png) 

查看详情，发现拉取成功了

![image-20240608180407072](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240608180407072.png) 















