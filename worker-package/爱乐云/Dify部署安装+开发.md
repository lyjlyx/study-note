# Dify部署安装

我们的Dify是部署运行在docker环境中的，所以需要先安装docker，该部署环境为Centos7，4c 16g

## 安装Docker

### 1. 更新系统

首先，确保你的系统软件包是最新的：

```
sudo yum update -y
```

### 2. 移除旧版本

如果你的系统上装有旧版本的Docker（称为`docker`或`docker-engine`），请先将其卸载：

```
sudo yum remove docker docker-common docker-selinux docker-engine
```

### 3. 安装必要的软件包

安装`yum-utils`包，它提供了`yum-config-manager`以及其他一些工具：

```
sudo yum install -y yum-utils
```

### 4. 设置Docker存储库

使用以下命令设置Docker的官方存储库：

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

![image-20250717122036883](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250717122036883.png) 



如果报错了则执行下面

```
yum-config-manager --save --setopt=docker-ce-stable.skip_if_unavailable=true
```

重新执行命令

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

### 5. 安装Docker Engine

安装Docker Community Edition（docker-ce）：

```
sudo yum install -y docker-ce
```

### 6. 启动Docker服务

安装完成后，启动Docker服务并设置为开机自启：

```
sudo systemctl start docker
sudo systemctl enable docker
```



### 7. 验证Docker安装

通过运行`hello-world`镜像来验证Docker是否已安装成功：

```
systemctl status docker
```



## 安装Dify



参考连接

```
https://www.cnblogs.com/jiyuchen1/p/18744711
```



下载源码

```
 git clone https://githubfast.com/langgenius/dify.git  
 git checkout 
```

也可以手动去官网下载  可以选择最新版本的

![image-20250717161543626](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250717161543626.png)

```shell
cd dify-1.6.0
cd docker
cp .env.example .env                                     # 复制一份默认的配置文件，把.env.example  复制为 .env
docker compose -f docker-compose.yaml up -d              # 使用docker compose方式安装
```



执行命令的时候发现连接超时报错

![image-20250718103451740](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250718103451740.png)

镜像拉不下来，无非就是镜像源访问不到，网络不通，现在基本网络不通的情况不存在，如果遇到网络不通的时候，ping一下百度。大概率的问题基本上是镜像源无法访问。所以直接搞镜像源。

```
cd /etc/docker
vi daemon.json     # 这个文件要是没有的话就自己创建
```

```json
{
"registry-mirrors": ["https://docker.registry.cyou",
"https://docker-cf.registry.cyou",
"https://dockercf.jsdelivr.fyi",
"https://docker.jsdelivr.fyi",
"https://dockertest.jsdelivr.fyi",
"https://mirror.aliyuncs.com",
"https://dockerproxy.com",
"https://mirror.baidubce.com",
"https://docker.m.daocloud.io",
"https://docker.nju.edu.cn",
"https://docker.mirrors.sjtug.sjtu.edu.cn",
"https://docker.mirrors.ustc.edu.cn",
"https://mirror.iscas.ac.cn",
"https://docker.rainbond.cc"]
}
```

这个配置上去之后就可以拉取镜像了

安装成功

![image-20250718105141391](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250718105141391.png)



此时我们还不知道dify web端口绑到了宿主机的那个端口，可以看一下容器信息，找web**服务器的端口映射**

![image-20250718105715279](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250718105715279.png)







# 开始使用Dify

直接进入网址输入账号密码登录

```
http://10.120.8.108/apps
```

我们通过设置-模型供应商中可以看到很多模型供应商可以直接安装使用

![image-20250722161029972](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250722161029972.png)



这里我用到的模型有

![image-20250722161057292](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250722161057292.png) 



安装完成之后要先设置好API-KEY，验证通过之后就可以添加模型了



























































