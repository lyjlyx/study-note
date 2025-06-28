# **k8s的squid（附docker镜像拉取问题）**

在Kubernetes (k8s) 的上下文中，Squid 通常指的是 Squid 缓存代理服务器的容器化版本。Squid 是一个广泛使用的开源缓存代理和Web服务器加速器，它可以缓存HTTP、HTTPS和FTP等协议的内容，减少带宽使用，提高内容加载速度，并且可以作为代理服务器来提高安全性和隐私性。

在 Kubernetes 环境中，Squid 可以部署为一个 Pod，或者作为一组 Pods 运行，以便提供高可用性和负载均衡。使用 Kubernetes 部署 Squid 可以带来以下好处：

1.**自动化部署和管理**：通过 Kubernetes 的声明式配置和自动化部署机制，可以轻松地将 Squid 集成到现有的DevOps流程中。

2.**可伸缩性**：可以根据流量和负载需求，自动或手动地扩展 Squid 实例的数量。

3.**高可用性**：Kubernetes 可以监控 Squid Pods 的健康状况，并在必要时自动替换故障实例。

4.**网络策略和安全**：可以利用 Kubernetes 的网络策略来控制哪些服务可以访问 Squid Pod，并确保通信安全。

要在 Kubernetes 上部署 Squid，你通常需要编写一个 Deployment 配置文件来定义 Squid Pod 的规格，包括镜像、资源限制、环境变量等。此外，你可能还需要配置 Service 资源以提供网络访问，以及可能的 ConfigMap 或 Secret 资源来管理配置文件和敏感数据。

例如，一个基本的 Squid **Deployment** 配置可能如下所示：

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

![image-20250628155420448](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155420448.png)

因为使用的helm所以我们要使用helm相关命令删除

1.首先，找出您想要删除的 release 的名称。您可以列出所有当前的 Helm releases 来找到它：

```
helm list --all-namespaces
```

2.然后，使用以下命令删除 release

```
helm uninstall <release-name> -n <namespace>
```

去helm官网

https://helm.sh/

![image-20250628155536275](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155536275.png)

搜索要安装的squid

![image-20250628155728682](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155728682.png)

点击install

![image-20250628155741708](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155741708.png)

执行repository环境安装脚本

![image-20250628155756261](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155756261.png) 

install chart要结合自己的kubeConfig文件来安装

这里我们已经有安装squid的了，则使用upgrade，初始环境的话使用install

helm upgrade squid-scrm-qywx lib42/squid --kubeconfig /data/cicd/kubeconfig/kube-config-test -n lspace-testKUBECONFIG=/data/cicd/kubeconfig/kube-config-test helm upgrade -i squid-scrm-qywx lib42/squid -n lspace-test

这个时候squid已经建好了，我们使用k8s容器命令可以看到有对应命名的squid启动

![image-20250628155813807](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155813807.png) 

## **Kubesphere**

![image-20250628155853370](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155853370.png)

![image-20250628155901340](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628155901340.png)

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_441085_TJG9XaBEruFUuM0m_1717820461)

## **腾讯云**

这个时候在tke集群上也能搜到相关服务

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_687693_cEOwiVyRyl3jWgFm_1717812961)

我们需要给指定服务器打上squid的标签

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_674243_XDLUxNfUoQwA8vgX_1717812961) 

找台外网服务器将相关标签打上

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_720097_jHbTgjsF9Mlr5zMW_1717812961)

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_738095_2MZZ8nNxRHzX9D09_1717812961)

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



## **记开发环境squid异常问题**

因为项目中的message容器没跑起来，查看报错原因是因为squid连接不上的问题（省略message报错）

我们通过kpod -o wide |grep squid发现squi对应的容器一直处于Pending状态

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_124366_phP02EReIwamPd4s_1717841107)

查看squid相关详情``kubectl describe pod squid-7f655ccccc-xrzgn --namespace lspace-dev``

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_174545_eV5i1qu_UKEn3UiE_1717841107) 

当时错误判断以为是squid的helm问题，所以对其helm进行了相关的重装，**具体步骤看上面的操作**

但是重装完成之后发现新的squid的一直是ImagePullBackOff状态

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_134868_oYcZ8ohGCqFyiRxr_1717841107) 

查看describe

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_103851_8B_DCVXZ7lCYpC5F_1717841107)

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_141899_ICd0byP1NEfOvq5M_1717841107)

发现是镜像拉取失败，以为是镜像源的问题（**其实就是公司网络太慢镜像源问题**）

重新设置了docker的镜像源

参考地址

https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_164758_Eh2yrgF0NxOWu1WC_1717841107) 

但是并没有什么用，该拉取下来还是拉取不下来，刚刚好公司有自己的腾讯云的私有仓库，我们先把地址换成腾讯云的镜像仓库地址看看能不能Pull下来。

 ``docker pull mirror.ccs.tencentyun.com/honestica/squid:4.69``

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_85290_PhtjkodmRLQ6DnSO_1717841107)

发现ping不通mirror.ccs.tencentyun.com（**应该是个人版问题**），所以用我们自己的私有镜像仓库地址看看，可以ping通

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_121183_U7UnF33GnHQ2CjRf_1717841107) 

那就直接先进行登录，生成登录链接

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_112939_emN-3_gJQQKCklGW_1717841107) 

登录成功，但是还是拉不下来

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_122595_kwUg30MrminGhBqP_1717841107)

在私有仓中查询了一下没有发现对应的squid相关的镜像（但是我们之前用的个人版是有的）

**所以具体思路就是先从个人版拉取相关镜像，先在测试服务器（测试服务器是腾讯云的，所以不会有网络的困扰，很快）**

查询对应的squid镜像名字

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_155366_g8U6H0z2IEueskc6_1717841107)

 将对应的镜像名称打上tag标签，然后push到私有仓中

``docker tag honestica/squid:4.69 docker-bj.tencentcloudcr.com/lspace/squid:4.69``

``docker push docker-bj.tencentcloudcr.com/lspace/squid:4.69``

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_185921_x07x95cNZgcrTkX2_1717841107)

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_135226_RAOa-wTIPWKJTMUL_1717841107)

镜像仓库中有了

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_135228_eKstSEdJKCE1COQ9_1717841107)

随后我们回到开发服务器

根据相关的仓库地址把相关的tag拉下来

docker pull docker-bj.tencentcloudcr.com/lspace/squid:4.69打上标签，就行了

docker tag docker-bj.tencentcloudcr.com/lspace/squid:4.69 honestica/squid:4.69

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_177531_kwcA3ZQv5wp09pQQ_1717841107)

查看详情，发现拉取成功了

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTMxMDI3MDA1Njk5ODMwMjc_150465_1GnMwMuWWzqfji1g_1717841107) 

## **Ingress容器拉取问题**

近日发现开发环境的后端请求路径访问不通，显示请求超时，所以首先我们要先看看容器的health是否是通的

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTY4ODg1NzA5NDM3MjgzNA_374287_83l1T4R9UmjNVQg8_1723018342) 

发现连接被重置了，所以需要去确认一下证书是否是正确的（其实首先应该看看Ingress的配置文件是否被修改过，可以先去检查）

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/MTY4ODg1NzA5NDM3MjgzNA_138166_TBIFTfORvpXkRSvv_1723018071) 





## 如果替换squid中的ConfigMap配置

这里以 隐藏原始IP 这个字段

可以先去百度查询一下需要加什么参数，这里查到的参数有两个

```yaml
     forwarded_for delete
     via off
```

首先的话可以先将squid之前的配置文件备份一下（如果需要的话）

```bash
   kubectl -n lspace-test get configmap squid-conf -o yaml > squid-conf-backup.yaml
```

编辑 ConfigMap

```bash
   kubectl -n lspace-test edit configmap squid-conf
```

找到 squid.conf 字段，添加如下内容（建议加在文件顶部或 http_access 相关配置前）：

红框中的都是squid.conf配置

![image-20250628162703567](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628162703567.png)

**注意：上述中的内容并没有转义，所以都是用\n 或者 多个\n 去分割的，直接在相关的后面加上就行，这里加的位置为**

![image-20250628162818308](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250628162818308.png)

后面:wq保存就行

**让 Pod 加载新配置**

由于 ConfigMap 挂载到 Pod 后，Pod 不会自动 reload 配置，必须重启 Pod。

```bash
   kubectl -n lspace-test delete pod -l app=squid
```

K8s 会自动拉起新的 Pod，新的配置会生效。























































