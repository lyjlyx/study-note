# Kubesphere学习



## Kubesphere安装

安装内容都是根据官方文档来进行操作

```
https://v3-0.docs.kubesphere.io/zh/docs/installing-on-linux/introduction/kubekey/
```

首先运行以下命令，以确保您从正确的区域下载 KubeKey。

```
export KKZONE=cn
```

运行以下命令来下载 KubeKey：

```
curl -sfL https://get-kk.kubesphere.io | VERSION=v1.0.1 sh -
```

为 `kk` 添加可执行权限：

```
chmod +x kk
```



执行安装创建命令

```
./kk create cluster --with-kubernetes v1.17.9 --with-kubesphere v3.0.0
```

![image-20240606200809505](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240606200809505.png)





在 Kubernetes 中，Service 的内部 DNS 名称是基于 Service 名称和命名空间自动生成的。你可以通过以下方式确定 RabbitMQ 服务的内部 DNS 名称：

### 1. 获取 Service 名称和命名空间

首先，确保你知道 RabbitMQ 服务的名称和所在的命名空间。你可以使用以下命令来查看：

```bash
kubectl get svc -n <namespace>
```

例如，如果你的 RabbitMQ 服务在 `default` 命名空间下，你可以运行：

```bash
kubectl get svc -n default
```

输出示例：

```
NAME        TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)         AGE
rabbitmq    ClusterIP   10.96.0.1     <none>        5672/TCP        1h
```

在这个示例中，服务名称是 `rabbitmq`，命名空间是 `default`。

### 2. 确定内部 DNS 名称

Kubernetes 内部 DNS 名称的格式如下：

- 完全限定域名（FQDN）：`<service-name>.<namespace>.svc.cluster.local`
- 短域名：`<service-name>.<namespace>.svc`
- 更短域名：`<service-name>.<namespace>`

对于上面的示例，RabbitMQ 服务的内部 DNS 名称将是：

- 完全限定域名（FQDN）：`rabbitmq.default.svc.cluster.local`
- 短域名：`rabbitmq.default.svc`
- 更短域名：`rabbitmq.default`

### 3. 验证 DNS 名称

你可以在 Kubernetes 集群中的 Pod 内部使用 `nslookup` 或 `dig` 命令来验证这些 DNS 名称。例如，你可以启动一个临时 Pod 并在其中运行 `nslookup` 命令：

```bash
kubectl run -i --tty --rm debug --image=busybox --restart=Never -- sh
```

进入 Pod 后，使用 `nslookup` 或 `dig` 命令：

```sh
nslookup rabbitmq.default.svc.cluster.local
```

输出示例：

```
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      rabbitmq.default.svc.cluster.local
Address 1: 10.96.0.1
```

### 4. 配置 Spring 应用程序

一旦你确定了 RabbitMQ 服务的内部 DNS 名称，就可以在 Spring 应用程序的配置文件中使用此名称：

#### application.properties

```properties
spring.rabbitmq.host=rabbitmq.default.svc.cluster.local
spring.rabbitmq.port=5672
```

#### application.yml

```yaml
spring:
  rabbitmq:
    host: rabbitmq.default.svc.cluster.local
    port: 5672
```

### 总结

通过以上步骤，你可以确定和验证 RabbitMQ 服务的 Kubernetes 内部 DNS 名称，并将其配置到 Spring 应用程序中。这样可以确保你的应用程序能够正确连接到 Kubernetes 集群中的 RabbitMQ 服务。



### 问题修复

#### 1、tke应用状态DiskPressure:True,Ready:True

Kubernetes节点状态描述

在Kubernetes中，节点的状态可以被很多因素影响，以下描述是相关方式解释：

- **`DiskPressure`**：表示节点正在经历磁盘压力。这意味着节点上的磁盘资源出现短缺，例如，磁盘已满或者可写磁盘空间不足。这通常由节点上的系统监控组件（如`kubelet`）评估报告。
- **`Ready`**：表示节点在集群中可用并准备好运行Pod。如果状态是`Ready:True`，节点可以正常用来调度和运行工作负载。

`DiskPressure: True, Ready: True` 的解释

- **`DiskPressure: True`** 表示节点上磁盘资源出现了压力。出现这种情况时，应尽快排查原因并释放磁盘空间。
- **`Ready: True`** 表示尽管当前节点存在磁盘压力，但依然可以接收Pod的调度。

推荐处理步骤

1. **检查磁盘空间**:

   

   - 在节点上运行命令例如`df -h`来查看磁盘使用情况，特别是那些待使用容量超过80%的分区。
   - 如果使用特定存储卷（如Persistent Volume, PV）请检查绑定情况及其使用状态。

2. **数据清理与优化**：

   - 清理临时文件:

     - 确保区域 `/tmp`、 `/var/tmp` 的未使用文件及时清理。

   - 移除不需要的镜像或通过

     ```
     docker system prune
     ```

   - 检查并优化日志的状态和轮换，避免磁盘因日志无限追加爆满。



























































































