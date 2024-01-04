# Freeswitch安装+操作问题汇总

## Freeswitch安装

所有下载资源的cos桶地址（能自己下的就自己下把）

```
https://upload-sources-1251077853.cos.ap-beijing.myqcloud.com/freeswitch/%E8%A7%A3%E5%8E%8B%E5%88%B0%E8%87%AA%E5%B7%B1%E7%94%B5%E8%84%91%E4%B8%8A%E4%BC%A0%E5%88%B0%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%BD%BF%E7%94%A8.zip
```



为什么我们学习freeswitch一定要尝试至少一次的源码编译？因为只有手动进行编译安装，你才能感受到服务端Freeswitch部署的魅力。

安装教程文章地址：

```
https://zhuanlan.zhihu.com/p/603961809?utm_id=0&wd=&eqid=af2256e100010bfc00000004648812f3
```

贴一些有价值的问题参考文章：

```
https://www.cnblogs.com/wuchangsoft/p/16730896.html
```

1、在安装前先安装相关依赖

```
yum install -y git alsa-lib-devel autoconf automake bison broadvoice-devel bzip2 curl-devel libdb4-devel e2fsprogs-devel erlang flite-devel g722_1-devel gcc-c++ gdbm-devel gnutls-devel ilbc2-devel ldns-devel libcodec2-devel libcurl-devel libedit-devel libidn-devel libjpeg-devel libmemcached-devel libogg-devel libsilk-devel libsndfile-devel libtheora-devel libtiff-devel libtool libuuid-devel libvorbis-devel libxml2-devel lua-devel lzo-devel mongo-c-driver-devel ncurses-devel net-snmp-devel openssl-devel opus-devel pcre-devel perl perl-ExtUtils-Embed pkgconfig portaudio-devel postgresql-devel python-devel python-devel soundtouch-devel speex-devel sqlite-devel unbound-devel unixODBC-devel wget which yasm zlib-devel libshout-devel libmpg123-devel lame-devel
```

2、下载cmake并进行编译安装，**在/usr/local/src目录下执行**

```
wget https://cmake.org/files/v3.17/cmake-3.17.2.tar.gz
tar vzxf cmake-3.17.2.tar.gz
cd cmake-3.17.2
./configure
make -dj
make install
```

**请按照顺序进行安装 cd /mnt/hgfs/**

3、下载libks并进行编译安装，**在/usr/local/src目录下执行**

**注意：这里有个坑，使用fs.1.10.2版本的libks安装需要使用里面v1.x版本的，而不是直接下载源码使用，原因是该通信包源码中master分支里头的版本是libks2.o的，会导致fs解析不到这个东西，导致安装报错。**

```
make[4]: Entering directory `/usr/local/src/freeswitch-1.10.2.-release/src/mod/applications/mod_signalwire'
Makefile:946: *** You must install libks to build mod_signalwire. Stop.
```

```
yum install libatomic
git clone https://github.com/signalwire/libks.git
git checkout v1.x
#上传源码到/usr/local/src目录
cd libks
cmake .
make -dj
make install
```



4、下载并进行编译安装，**在/usr/local/src目录下执行**

**注意这里也有个坑，同上面libks一样的问题，需要切到v1.x版本的源码**

```
git clone https://github.com/signalwire/signalwire-c.git
git checkout v1.x
#上传源码到/usr/local/src目录下
cd signalwire-c/
cmake .
make -dj
make install
ln -sf /usr/local/lib64/pkgconfig/signalwire_client.pc /usr/lib64/pkgconfig/signalwire_client.pc
```

5、下载x264并进行编译安装，**在/usr/local/src目录下执行**

```
git clone http://git.videolan.org/git/x264.git
./configure
make -dj
make install
```

注意：这一步会出现问题

```
Found no assembler
Minimum version is nasm-2.13
If you really want to compile without asm, configure with --disable-asm.
```

因为他需要更加高级的编译器，所以我们这边手动下载安装最新的nasm

```
在CentOS 7上安装NASM 2.13，您可以按照以下步骤操作：
1. 首先，打开终端。
2. 更新您的系统软件包到最新版本，使用以下命令：
    sudo yum update -y
3. 下载NASM 2.13的源代码包，使用以下命令：
    wget https://www.nasm.us/pub/nasm/releasebuilds/2.13.03/nasm-2.13.03.tar.gz
    注意：上述链接可能会因为NASM版本更新而失效，如果链接失效，可以在NASM的官方网站上找到最新的链接。
4. 解压下载的源代码包，使用以下命令：
    tar -zxvf nasm-2.13.03.tar.gz
5. 进入解压后的目录，使用以下命令：
    cd nasm-2.13.03
6. 编译和安装NASM，使用以下命令：
    ./configure
    make
    sudo make install
7. 检查NASM是否已经成功安装，使用以下命令：
    nasm -v
    如果NASM已经成功安装，这个命令将会显示出NASM的版本信息。
请注意，上述步骤需要有root权限才能执行。如果您没有root权限，可能需要向您的系统管理员请求权限。
```

**不建议使用git clone将源码拉下来，而是先在另外一台能够访问外网的机子上把源码拉下来，并且通过上传的方式放到对应目录下面**

如果是使用vmware的话，可以通过共享的方式将电脑和虚拟机直接的传输打通。教程在此

```
https://www.zhihu.com/question/523495335/answer/3170023486?utm_id=0
```



6、编译安装mod_av模块

```
wget http://download1.rpmfusion.org/free/el/updates/7/x86_64/x/x264-libs-0.148-24.20170521gitaaa9aa8.el7.x86_64.rpm
wget http://download1.rpmfusion.org/free/el/updates/7/x86_64/x/x264-devel-0.148-24.20170521gitaaa9aa8.el7.x86_64.rpm
rpm -hiv x264-libs-0.148-24.20170521gitaaa9aa8.el7.x86_64.rpm
rpm -hiv x264-devel-0.148-24.20170521gitaaa9aa8.el7.x86_64.rpm
cd /usr/local/src
git clone https://gitee.com/nwaycn/libav.git
cd libav
./configure --enable-pic --enable-shared  --enable-libx264 --enable-gpl --extra-libs="-ldl"
make                                                                                  
make install
cp /usr/local/lib/pkgconfig/libavcodec.pc    /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libavdevice.pc   /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libavfilter.pc   /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libavformat.pc   /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libavresample.pc /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libavutil.pc     /usr/lib64/pkgconfig/
cp /usr/local/lib/pkgconfig/libswscale.pc    /usr/lib64/pkgconfig/
ldconfig  #动态链接库管理命令，目的让动态链接库为系统所共享
```

7、在freeswitch官网下载源码，选择1.10.2版本。下载地址

```
https://freeswitch.org/confluence/display/FREESWITCH/FreeSWITCH+1.10.x+Release+notes
```

![image-20231013182053518](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013182053518.png) 



8、源码上传到CentOS7系统中

![image-20231013182453084](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013182453084.png) 

9、进行解压，解压指令：tar -zxvf freeswitch-1.10.2.-release.tar.gz

进入到解压的目录，开始编译安装

```
cd freeswitch-1.10.2.-release
./configure 
make
make install
```



10、在执行make时，遇到报错 **You must install libopusdev to build mod opus. Stop**

 ![image-20231014175538250](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231014175538250.png)

```
cd /etc/yum.repos.d/
```

创建文件

```
touch linuxtech.repo
```

使用vi 打开linuxtech.repo文件

```
vi linuxtech.repo
```

把下面的内容复制进去

```
[linuxtech]
name=LinuxTECH
baseurl=http://pkgrepo.linuxtech.net/el6/release/
enabled=1
gpgcheck=1
gpgkey=http://pkgrepo.linuxtech.net/el6/release/RPM-GPG-KEY-LinuxTECH.NET
```

进行安装libopus-devel

```
yum install libopus-devel
```

升级opus，下载opus-1.3.1安装包并进行编译安装，在/usr/local/src目录下执行

```
wget https://archive.mozilla.org/pub/opus/opus-1.3.1.tar.gz
tar xvfz opus-1.3.1.tar.gz
cd opus-1.3.1
./configure
make
make install
cp /usr/lib/pkgconfig/opus.* /usr/lib64/pkgconfig/
```

再次进入到freeswitch源码解压目录下

```
make clean
./configure
make
make install
```

11、最后安装编译出现freeswitch logo表示安装成功了

 ![image-20231018104247359](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231018104247359.png)

12、freeswitch默认编译安装的目录在/usr/local/freeswitch

![image-20231018104345136](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231018104345136.png) 

```
cd /usr/local/freeswitch/
```

13、启动验证

后台启动

```
freeswitch -nc -nonat
```

执行命令说该命令不存在

![image-20231018105909629](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231018105909629.png) 

将环境变量配置上去

```
 export PATH=$PATH:/usr/local/freeswitch/bin
```

查看进程和5060端口

![image-20231018110002851](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231018110002851.png) 





## Freeswitch常见问题整理



Freeswitch库编译异常问题解决

```
https://www.jianshu.com/p/ea01c731f28b
```

![image-20231129152724848](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231129152724848.png) 



### Freeswitch基本问题汇总

```
http://aizuda.com/article/1089440
```



### Freeswitch源码打包安装和下载地址

另外一种安装方式

```
https://freeswitch.signalwire.com/repo/yum/centos-release/7/x86_64/repodata/repomd.xml:
[Errno 14] HTTPS Error 401 - Unauthorized
```

```
https://kling.cn/detail/62cfe6716e277.html
```

```
echo "lren" > /etc/yum/vars/signalwireusername
echo "pat_VcjJ8BHwyd3woyRzrmjHhtp8 " > /etc/yum/vars/signalwiretoken
```



### fs开发使用esl连接报错

```
 mod_event_socket.c:2663 IP ::ffff:10.120.2.237 Rejected by acl "loopback.auto"
```

解决：

/usr/local/freeswitch/conf/autoload_configs

```xml
#在event_socket.conf.xml配置文件中，取消该行注释：
<param name="apply-inbound-acl" value="loopback.auto"/>
#在acl.conf.xml中增加：
<list name="loopback.auto" default="allow">
<node type="allow" cidr="172.31.20.0/32"/>
</list>
```



### 坐席配置和坐席默认密码修改

坐席默认放到目录：/usr/local/freeswitch/conf/directory/default

![image-20240103163124515](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240103163124515.png) 

坐席的默认密码为：1234，在目录/usr/local/freeswitch/conf中的vars.xml

![image-20240103163158432](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240103163158432.png) 



### 两方说话听不见的问题

进入到/usr/local/freeswitch/conf/sip_profiles目录

修改external.xml

![image-20240103175839181](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240103175839181.png) 

把这两个ip改为实际的ip

![image-20240103180029182](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20240103180029182.png) 





## Freeswitch操作命令汇总

启动Freeswitch命令：

```
freeswitch -nc -nonat
```

关闭Freeswitch：

```
freeswitch -stop
```

控制台连接命令：

```
fs_cli -H 10.120.2.108 -P 8021 -p lspace97531
```

