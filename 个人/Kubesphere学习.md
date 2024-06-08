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





































































































