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











































