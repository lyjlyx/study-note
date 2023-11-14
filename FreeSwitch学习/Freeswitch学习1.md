# FreeSwitch学习

开源freeswitch管理界面

```
https://github.com/laoyin/freeswitch_admin_ui.git
```

## 一、常用总结

### 1、常用命令

```
fsctl loglevel [1-7] #设置日志级别
reloadxml #重载xml配置文件
regex 被匹配字符串|正则表达式 #校验正则表达式是否正确，正确为true 错误为false

```



## 二、什么是SIP协议，和Freeswitch是什么关系？

### 1、理解

所谓协议是一种解决双方理解不一致的方法。其他地方的方言各有各自的发音，互相之间理解会有障碍，那么普通话就是一种协议，解决了方言方言之间的理解难度。



#### 什么是SIP协议，和FreeSwitch什么关系

**SIP**是什么？Session Initiation Protocol，会话初始化协议，sip协议不仅仅是用在了Freeswitch，任何有会话管理的服务，均可以借鉴和使用SIP协议。

现在用非正式语言介绍一下ssip的协议。

```
在我们日常生活中，我们想要找一个人互相聊天，首先你要找到这个人、你的声音得传递到对方那里，对方能听到你的声音，同时害的要去理解你的语言（方言、语种），帮你定位到对象的第sip，你们两个协商使用英语沟通还是汉语沟通，使用电话设备还是电脑web的是Sip,最终说话的传递及传输介质是rtc。sip协议做的是，能够让你定位到你想聊天的对象，帮你检测你聊天对象是否可达，帮你管理你们通话的会话、状态，帮你结束你们的聊天进程等等。
```

举个例子：

```
你想谈一场跨国恋爱，你在中国，你的心仪对象在日本(OVER，阵亡)
假设你知道你心仪对象的住址门牌号 (有戏)然后你发现你找不到，也过不去(OVER，阵亡)
假设你知道你心仪对象的住址门牌号，然后你也借助了国际力量，分机飞往了日本，然后你发现，对象人家家里设置了门禁系统，(OVER，阵亡)
假设你知道你心仪对象的住址门牌号，然后你也借助了国际力量，分机飞往了日本，然后你发现，然后你有了安全密码，门打开了，你发现你不会日语(OVER，阵亡)
```

那么如何才能顺利的和心意的对象进行沟通交流呢？

你需要知道对方的地址（通信唯一键），你需要奥获得对方是否在线，你需要有对方接受你信号的权限，你还需要拥有和对方理解一致的语言。

**SIP协议中，每一个通信对象，首先都得拥有一个类似邮箱地址的协议IP，如果你心意的对象就在你了解的范围内，你们两个可以直接通过SIP直连，如果你的对象是不在你的接触范围内，你需要邮政公司、外交部机构等等类似场景来告知你如何找到他。找到之后，你们还得约定同一种沟通语言，然后你们才可以愉快的沟通交流。**



使用sngrep来抓包sip协议来解释sip协议的概念。

![image-20230927163101551](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230927163101551.png) 

其中左边的183.是一端的SIP地址，另一端是172.的sip协议地址，规范写法如下：sip:你的号码@ip:port

![image-20230927164147642](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230927164147642.png)

 SIP类似http协议为文本协议，通用一个空行，来区分session管理和rtp

一个完整的SIP协议流程如下

![image-20231013142226954](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013142226954.png) 

INVITE表示邀请

![image-20231013142438747](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013142438747.png) 

From 表示sip来源地址的信息，因为我们使用Freeswitch gateway方式外呼，同时配置网管为register false，默认为Freeswitch@ip:port

To 表示请求的地址方。

Contact对方可以根据此进行回访。

**1xx 一般表示临时状态，一般可以用于振铃。 180，183用于SDP协商**

![image-20231013142901650](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013142901650.png) 



200表示成功响应，ACK进行确认，BYE表示挂断方，200表示成功响应。

![image-20231013142923061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013142923061.png) 

SIP类似于HTTP,也有状态码

1XX 表示临时响应，比如：180,183等等，

2XX 表示成功响应

3XX 表示重定向

4XX 表示客户端错误

5XX 表示服务器错误

6XX 表示全局错误



#### SDP协议

**SDP用于在RTP绘画中描述多媒体通信会话的属性，如会话的起始时间、结束时间、参与者的IP地址和端口号，以及使用的编码格式等等。SDP本身并不传输任何媒体数据，它只是描述了如何在RTP会话中发送和接收媒体数据。**

**SDP协议主要用于媒体协商，简单做一个SDP参数介绍**

参数详解

  ![image-20231013143646837](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013143646837.png)

v 表示：**version**

o表示： Origin **起源**。 username, sess-id, sess-version,nettype, addrtype,unicast-address

s表示：Session name

c表示：Connection data **连接数据**。rtp流发送给该地址。

t表示时间

m 表示Media Type，**媒体类型**，audio音频，10028表示端口，RTP、AVP 并不是编码，而是涉及到流媒体数据传输和处理的协议和规范，0 表示PCMU   大于95的属于动态编码，使用a=进行说明。

1.RTP（实时传输协议）：这是一个网络传输协议，它被设计用来为应用程序提供端到端的实时传输服务，如音频、视频或模拟数据等。RTP 通常在 UDP 协议之上工作，尽管它也可以在其他传输协议之上使用。

2.AVP（音视频配置文件）：它是 RTP 的一个配置文件，定义了 RTP 和 RTCP 的一些默认设置和规范。AVP 在 SDP（会话描述协议）中一般用来描述音频和视频会话的特性。

对应编码有

```
SDP（Session Description Protocol，会话描述协议）是一种用于描述多媒体会话（如音频、视频、文字等）的网络协议。SDP本身并不定义编码类型，而是为会话提供了一种方式来描述应使用哪种编码类型。编码类型通常在SDP的"m="（媒体描述）和"a="（属性）字段中定义。
对于音频和视频数据，常见的编码类型包括但不限于以下几种：
音频编码：
PCM：脉冲编码调制，是一种未压缩的音频格式。
AAC：高级音频编码，提供了高质量的音频压缩。
MP3：一种常见的音频压缩格式。
Opus：一种适用于互联网实时通信的音频编码格式。
G.711, G.722, G.723, G.726, G.728, G.729等：这些都是用于语音通信的编码格式。常用于VoIP等场景。
视频编码：
H.264（AVC）：一种广泛使用的视频压缩格式。
H.265（HEVC）：高效视频编码，是H.264的继任者，提供了更高的压缩效率。
VP8, VP9：由Google开发的视频压缩格式。
AV1：开源、免费的视频编码格式，旨在取代VP9和HEVC。
```

a=rtpmap: 0  PCMU/8000

a=rtpmap:101 telephone-event/8000
a=fmtp:101 0-15

对M编码类型进行说明。0 表示PCMU/8000，101表示telephone-event/8000表示DTMF事件

a=sendrecv



**当你开始进入Freeswitch世界、SIP世界的时候，你会遇见各种各样的问题，我们曾经通过抓包发现新买的网管设计竟然发送SIP协议出现异常情况，而且还是偶发性，因此一定要耐性，理解好基础，一切按照你的排查为主，积极主动地配合客户、运营商一同将问题解决。**



#### RTP协议

RTP协议-Real-time Transport Protocol，实时传输协议，它允许在IP网络上传输音频和视频数据，RTP被设计为提供端到端的网络传输服务，用于多播或者单播网络服务中应用。RTP主要是被用在多媒体流应用中，比如IP电话、视频会议和媒体分发等情况。

```
RTP数据包结构主要包含以下部分：

版本号（Version）：标识RTP版本，常见的是版本2。
填充位（Padding）：标识数据包尾部是否有填充字节。
扩展位（Extension）：标识是否有头扩展字段。
CSRC计数器（CSRC Count）：标识CSRC标识符的个数。
标记（Marker）：一般用于标识音频和视频的关键帧。
载荷类型（Payload Type）：标识RTP载荷的类型，如音频或视频编码格式。
序列号（Sequence Number）：用于标识发送的RTP数据包的序列号，用于接收端检测数据包的丢失和排序。
时间戳（Timestamp）：用于同步和调整数据的播放速率。
同步源标识符（SSRC）：标识同步源，用于区分不同的媒体流。
在处理RTP数据包时，需要按照RTP协议的规定，正确解析出每个字段的值，才能正确处理音频和视频数据。
```



[https://www.rfc-editor.org/rfc/rfc3550]我们使用的Freeswitch就使用rtp协议传输音频、视频数据。在freeswitch中，RTP协议建立在UDP协议之上，RTP协议非常重要，通过协商后，根据SDP协议，RTP会按照双方约定的端口、媒体协议进行数据发送。我们看看rfc3550的规范，rtp协议包定义的数据包

![image-20231013160050576](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013160050576.png)

介绍一下协议包参数的内容

```
V（version）：RTP版本号，一般是2
P（padding）：是否有附加信息
X（extension）：如果是1，表示固定的头部后存在一个扩展信息头部数据
CC：多少个CSRC标记
M：
PT：plyload type，RTP包携带的信息类型。这个非常重要，决定了双方是否能够接受对方的媒体流
seque number:序列号，每个RTP包发送后都会加1，根据此序列可以来界定是否存在丢包等等信息。
timestamp：时间戳
SSRC：数据源表示。
CSRC list：多路混发时候有效，将多个源数据合并到一个RTP流中
```



WireShark抓包情况

![image-20231013160508936](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013160508936.png) 

 **抓包详情**

![image-20231013160940346](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013160940346.png) 



#### SIP与NAT让人头痛的电话接听问题

我们在配置完成freeswitch简单配置后，经常会遇到接听后30秒挂断的问题，初学习Freeswitch的同学真的是深恶痛绝。

其实如果我们了解一下sngrep抓包和sip协议细节，就基本能够处理和解决。

一般的30秒挂断，通常一种情况是ACK服务触达。

我们知道SIP协议正常的点流程类似这样：

 ![image-20231013162103980](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013162103980.png)

出现异常情况，就是客户端或者服务端发送200或者ACK，没有收到回复。

这里有个标准，RFC规定ACK消息要发送给200 OK消息的Contact指定地址上。因此出现此类问题，就查看一下200消息的contract地址是否是正确的地址。



**解决办法：为客户端开启STUN或服务器启用NAT探测**

扩展STUN是什么：

```
STUN（Session Traversal Utilities for NAT）是一种网络协议，用于帮助在网络地址转换（NAT）后面的设备发现其公开的Internet协议（IP）地址，并确定任何防火墙是否允许它们从Internet上的对等方接收传入的数据。
NAT后的主机向公网的STUN服务器发送一个UDP消息，然后就在NAT设备上开启一个出口，同时STUN服务器会告诉客户端这个出口对应的IP地址和端口。客户端收到后，会将此地址和端口号告诉对应的语音服务器，语音服务器就从这个入口进入和客户端进行交互。
```

 STUN服务不能解决对称NAT穿越问题。

sngrep一般情况对于SIP信令的抓取非常的完美，但是RTP一般情况下需要使用tcpdump进行抓包

我们可以尝试对整个网卡进行抓包

```
tcpdump -i eth0 -w ./traget-prod-your-file.cap
```



然后接口wireshark进行分析处理，我们简单检测SIP,通过wireshark的过滤功能

```
wireshark的过滤脚本示例：
根据源IP过滤：在过滤器输入框中输入 ip.src == 192.168.1.1（请替换为你的具体IP地址）。
根据目标IP过滤：在过滤器输入框中输入 ip.dst == 192.168.1.1。
根据源和目标IP过滤：在过滤器输入框中输入 ip.addr == 192.168.1.1。
根据TCP端口过滤：在过滤器输入框中输入 tcp.port == 80（请替换为你的具体端口号）。
根据UDP端口过滤：在过滤器输入框中输入 udp.port == 53。
根据IP和端口同时过滤：在过滤器输入框中输入 (ip.addr == 192.168.1.1) && (tcp.port == 80)。
请注意，过滤器表达式是大小写敏感的，所以你需要确保输入的是小写字母。
在输入过滤表达式后，按下回车键或点击"应用"按钮，Wireshark就会根据你的过滤条件显示数据包。
```

 ![image-20231013164054606](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013164054606.png)

简单使用协议加端口udp.prot in {你的sip端口，我们这里使用9060端口}

通过查看183的消息，我们查看到对应rtp端口

 ![image-20231013164449357](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013164449357.png)

以及invite时候携带的sdp信息

![image-20231013164510685](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013164510685.png) 

重新输入过滤命令

udp.prot in {9060 31252 31458}

![image-20231013164552323](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231013164552323.png) 

合理使用各种抓包工具，有助于我们处理和解决音频通话过程中的问题，因此一定要多尝试抓包工具和多修改Freeswitch系统配置，在实践中学习和掌握sip、sdp协议等等。



### 2、Freeswitch安装

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



### 3、Freeswitch配置详解 



#### 基本参数

freeswitch的conf目录下，有以下非常重要的配置目录文件，包括

autoload_config 

dialplan

directory

mrcp_profiles

sip_profiles

var.xml

freeswitch.xml

其中freeswitch.xml是配置文件的起始文件，通过freeswitch.xml引入了包括var.xml等文件。

![image-20231018154807962](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231018154807962.png) 

其中var.xml配置了大量的变量，提供给其他文件进行引用。

```
<X-PRE-PROCESS cmd="set" data="default_password=1234"/>
```

比如directory中大量引用default_password，因此为了安全起见，最好将默认1234的密码改为复杂的密码



配置系统默认编码协议

```，
<X-PRE-PROCESS cmd="set" data="global_codec_prefs=OPUS,G722,PCMU,PCMA,H264,VP8"/>
<X-PRE-PROCESS cmd="set" data="outbound_codec_prefs=OPUS,G722,PCMU,PCMA,H264,VP8"/>
```



绑定外呼出网IP

```
<X-PRE-PROCESS cmd="set" data="external_rtp_ip=ip地址"/>
<X-PRE-PROCESS cmd="set" data="external_sip_ip=ip地址"/>
```



绑定外呼出网IP

```
<!-- Internal SIP Profile -->
  <X-PRE-PROCESS cmd="set" data="internal_auth_calls=true"/>
  <X-PRE-PROCESS cmd="set" data="internal_sip_port=5060"/>
  <X-PRE-PROCESS cmd="set" data="internal_tls_port=5061"/>
  <X-PRE-PROCESS cmd="set" data="internal_ssl_enable=false"/>

  <!-- External SIP Profile -->
  <X-PRE-PROCESS cmd="set" data="external_auth_calls=false"/>
  <X-PRE-PROCESS cmd="set" data="external_sip_port=5080"/>
  <X-PRE-PROCESS cmd="set" data="external_tls_port=5081"/>
  <X-PRE-PROCESS cmd="set" data="external_ssl_enable=false"/>
```



配置internal，external，sip，profile占用的端口

autoload_configs目录下，moudules.conf.xml，配置Freeswitch启动时候自动加载的模块

![image-20231019115642473](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019115642473.png) 

其实使用较多的是 **acl.config.xml（具体会在下面的acl配置进行说明）、switch.conf.xml、event_socket.conf.xml、db.conf.xml**



**switch.conf.xml**会配置一些系统信息，比如限制最大session数量、日志级别、数据db-handles数量等等。

![image-20231019120706234](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019120706234.png) 



配置**event_socket.conf**配置esl的信息

![image-20231019120818341](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019120818341.png) 

其中8021是esl默认端口，密码默认为cluecon为初始密码，切记如果对外开放密码需要改为复杂密码，apply-inbound-acl此esl服务允许连接客户端满足的acl，在acl配置文件中配置。

```
FreeSWITCH的ESL（Event Socket Library）是一个用于与FreeSWITCH交互的库。它允许开发者通过TCP socket向FreeSWITCH服务器发送命令或API调用，并接收FreeSWITCH服务器的事件。ESL是FreeSWITCH的一个重要组件，它提供了一种机制，使得外部程序能够控制和监视FreeSWITCH的状态。
ESL主要有两种工作模式：
Inbound：在这种模式下，外部应用程序连接到FreeSWITCH的ESL并发送命令。这种模式通常用于实现远程控制FreeSWITCH的功能。
Outbound：在这种模式下，FreeSWITCH连接到外部应用程序并发送事件。这种模式通常用于实现复杂的呼叫控制逻辑，例如IVR（Interactive Voice Response）系统。
ESL提供了多种语言的绑定，包括C，Perl，Python，Ruby，Lua等，使得开发者可以使用自己熟悉的编程语言来开发FreeSWITCH的应用程序。
```

**db.conf.xml**配置数据库连接odbc信息

 ![image-20231019121905018](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019121905018.png)



**acl.conf.xml**配置ip权限

![image-20231019121948544](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019121948544.png) 

**这里是我们创建的一个event_acl，允许所有ip请求8021端口，这个在event_socket.config.xml中使用**

![image-20231019122151783](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019122151783.png) 

acl的配置方式，allow或者deny



#### sip-profile配置

在sip_profile目录下系统默认会存在internal、externale目录,以及**external.xml,internal.xml,external-ipv6.xml,internal-ipv6.xml**

其中external-ipv6.xml,internal-ipv6.xml为支持ipv6而默认初始化的xml，如果你的服务器只需要支持ipv4,可以将此配置进行重命名等。



在sip-profile中，我们比较关心的几个配置<gateway> </gateway>配置网管和第三方进行通信时使用，包括对接各类运营商，对接语音网关。

![image-20231019194751530](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231019194751530.png) 

上述简单使用两种方式，一种注册方式，一种非注册方式单要求对方运行你的sip请求。

**每次更改profile文件指示，运行sofia profile [internal] rescan**

注意internal要替换成你的profile名称

如果是同一个gateway名称进行了修改，那么rescan并不能更新，需要你先进行gateway删除

```
sofia profile internal killgw gateway1
然后再rescan。
```

<settings></seetings> seetings里面包含了很多sip profile的系统配置

internal.xml

![image-20231020151143720](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231020151143720.png) 

![image-20231020151218183](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231020151218183.png) 

![image-20231020162947400](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231020162947400.png) 



比如我们要使用fail2ban进行恶意请求拦截，那么需要将log-auth-failures设置为true

![image-20231020182832841](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231020182832841.png) 

这是外网rtp-ip和sip-ip



#### freeswitch架构

![2a2fba27eef6bd18adcb6382a1844764](C:/Users/97151/Desktop/2a2fba27eef6bd18adcb6382a1844764.png) 



我们可以通过上面这张架构图，可以清晰的看到freeswitch的所有核心内容。

我们应用Freeswitch的时候，最长接触的就是endpoint、DB、event socket、各类嵌入式语音、拨号计划、asr、tts等等。



Endpoint：对应的是各类通信使用的协议，比如sip实现方式，系统的Sofia模块关于endpoint的理解，我们在后面的源码解析里，会有更加深刻的理解。

DB：Freeswitch默认使用的是sqlit数据库，为了更好的并发官网建议使用postgresql数据库，需要我们配置系统odbs来实现数据库替换。

Event socket：通过inbound或者outbound方式和freeswitch进行通信，能够自定义第三方服务来实时控制freeswitch，发送各类命令，监听各类事件。



拨号计划：diaplan，可以把他称为呼叫路由，根据配置的拨号计划条件，来执行你想要的应用，达到你的通话目的。

Format：编码格式，通过load不同的模块，可以支持各类音视频编码，比如loadmod_shout支持mp3等等。



那一通电话是如何作用在freeswitch各个功能模块的呢？我们在sip协议中通过了一通电话的流程，在这一章我们来分析电话在Freeswitch各个功能模块的处理路程。



我们知道freeswitch通过sip协议对接运营商的中继服务，从而实现电话的接通。那么首先想到的是Freeswitch的sip协议属于哪个模块中。

mod_sofia实现了freeswitch处理sip协议的流程，因此如何和运营商或者其他服务进行对接，一定是和mod_sofia模块有关。

在Freeswitch中，mod_sofia是实现了mod_endpoint接口的服务，因此在说mod_sofia之前我们先得了解一下Freeswitch中的mod_endpoint

```c
struct switch endpoint interface {
        
       /*! the interface's name */
       const char *interface name;
        
       /*! channel abstraction methods */
       switch_io_routines_t *io routines;
        
       /*! state machine methods */
       switch_state_handler_table_t *state_handler;
        
       /*! private information */
       void *private_info;
       switch_thread_rwlock_t *rwlock;
       int refs;
       switch_mutex_t *reflock;
        
       /* parent */
       switch_loadable_module_interface_t *parent;
        
       /* to facilitate linking */
       struct switch_endpoint_interface *next;
       switch_core_recover_callback_t recover_callback;
       
};
```

直接翻译endpoint有时候很难解释Freeswitch的设计，所以这里吧freeswitch进行解读，我们看到的每一个endpoint都需要实现以上endpoint interface接口，包括mod_sofia实现的sip协议。

包含接口名称、io_routines、state_handler。这几个非常中药柜基本能监视endpoint在freeswitch中的功能。其中io_routines

```
在计算机网络中，"endpoint"通常指的是与网络连接的设备或节点，例如电脑、手机、或是服务器等。每个endpoint都有一个唯一的地址，这样网络上的其他设备就能找到并与它进行通信。
在编程和web开发中，"endpoint"可能指的是一个URL，其中一个应用程序可以访问和与之交互的。例如，如果你有一个API（应用程序接口），每个功能（如获取数据、更新数据、删除数据等）可能都有一个独立的URL，这些URL就被称为"endpoints"。例如，`https://api.example.com/users`可能是一个用于获取用户信息的endpoint。
在网络安全中，"endpoint"通常指的是任何可以接入网络的设备，如电脑、手机、平板电脑和服务器等。这些设备可能成为攻击的目标，因此需要特别关注endpoint的安全。
总的来说，"endpoint"的具体含义取决于上下文，但大多数情况下，它指的是网络通信的起点或终点。
```



```c
struct switch io routines {
  /*creates an outgoing session from given session, callerprofile */
  switch_io_outgoing_channel_t outgoing_channel;
  /*! read a frame from a session */
  switch_io_read_frame_t read frame
  /*! write a frame to a session */
  switch_io_write_frame_t write_frame;
  /*!send a kill signal to the session's channel */
  switch_io_kill_channel_t kill_channel;
  /*!send a string of DTMF digits to a session's channel */
  switch_io send dtmf t send_dtmf;
  /*! receive a message from another session */
  switch_io_receive_message_t receive_message;
  /*!queue a message for another session */
  switch_io_receive_event_t receive_event;
  /*! change a sessions channel state */
  switch_io_state_change t state_change;
  /*! read a video frame from a session */
  switch_io_read_video_frame_t read_video_frame;
  /*! write a video frame to a session */
  switch_io_write_video_frame_t write_video_frame;
  /*read a video frame from a session */
  switch_io_read_text_frame_t read_text_frame;
  /*! write a video frame to a session */
  switch_io_write_text_frame_t write_text_frame;
  /*! change a sessions channel run state */
  switch_io_state_run_t state_run;
  /*!get sessions jitterbuffer */
  switch_io_get_jb_t get_jb;
  void *padding[10];
}
```

就包含了一通电话的流程，发起一通电话你得实现outgoing_channel方法，电话接通后媒体如何读取和发送，你得实现read_freame和wirte_frame等。



而mod_sofia实现的是基于sip协议的endpoint，因为我们想要接通和拨打电话，一定和mod_sofia密切相关。

**为了支持各类媒体协议，Freeswitch独立除了codec对应接口，实现对应的媒体处理。**



为了支持呼叫之后通过可定义的方式进行接通、转接、执行各类命令，Freeswitch把这里中间的过程称之为dialplan，即拨号计划。

为了进行消息通知，freeswitch实现了event接口，可以实现各类事件的生产-消费模型。



为了支持插件语言，freeswitch实现了各类第三方语言编译器，解释器。能够在Freeswitch执行对应的脚本语言，包括Python、lua、Java等等。





#### 拨号计划

就是我们常说的通话路由，通过拨号计划，Freeswitch将对应的channel专项固定的处理。拨号计划并不是脚本语言，他的目标是简单帮你找到你需要的应用。我们可以把他简单的叫做通话路由规则。



freeswitch默认是多环境拨号计划，默认情况为public和default。



其中public context 主要是开放给所有非认证请求，而default默认必须是需要注册的分机用户才能呼叫。

你可以把他和profile对应起来，但是并不是完全一一对应。比如默认internal和external profile。



比如：你创建一个sip_profile文件

```xml
<profile name="your_sip_profile">
	<param name="context" value="your_sip_profile_context"/>
</profile>
```



对应的路由规则xml文件，可以通过context来区分

```xml
<context name="your_sip_profile_context">
	<extension name="yourextension1">
		<condition field="destination_number" expression="^1001$">
			<action application="bridge" data="user/1001"/>
		</condition>
	</extension>
</context>
```

每一个context下都可以创建多个<extension>表示不同的规则路由。

condition表示条件，expression和正则表达式进行结合，就可以创建很多有意思的电话路由。



#### 一、sip对接运营商网管线路

a:在profile中创建gateway，用以描述指定运营商sip地址ip和端口，以及认证消息

```xml
<profile>
	<settings>
		...
	</settings>
	<gateways>
		<gateway name = "outbound">
			<param name="realm" value="ip:port"/>
			<param name="register" value="false"/>
		</gateway>
	</gateways>
</profile>
```

b:外呼出去的简单dialplan

```xml
<extension name="call_out">
	<condition field="destination_number" expression="(^[0-1][0-9]{10,11}$)">
		<action application="log" data="22~~~~~~~~~~${call_id_number}"/>
		<action application="set" data="call_teimout=20"/>
		<action application="bridge" data="{origination_caller_id_number=yournumber**,hangup_after_bridge=true}sofia/gateway/outbound/$1"/>
		<action application="set" data="test=${hangup_cause}"/>
		<action application="log" data="1 A-leg hangup_cause:${hangup_cause}"/>
	</condition>
</extension>
```

正则表达式``(^[0-1][0-9]{10,11}$)``表示0或者1开头，然后0-9之间的数字可以出现10-11次。

下方$1代表的是整个这个正  则表达式括号内的数字，即你呼叫的号码



#### 二、电话录音

```xml
<extension name="test-route1">
	<condition field="destination_number" expression="(^99030135$)">
		<action application="set" data="RECORD_COPYRIGHT=(C)2011"/>
		<action application="set" data="RECORD_SOFTWARE=FreeSWITCH"/>
		<action application="log" data="RECORD_ARTIST=FreeSWITCH"/>
		<action application="set" data="RECORD_COMMENT=FreeSWITCH"/>
		<action application="set" data="RECORD_DATE=${strftime(%Y-%m-%d %H:%M)}"/>
		<action application="record_session" data="/data/recordings/archive/${call_uuid}.wav"/>
		<action application="bridge" data="user/99030135"/>
	</condition>
</extension>
```

电话录音application,使用record_session应用，同时使用${}来表达Freeswitch的通道变量。



#### 三、dialplan转接ivr

比如你定义了自己的ivr后。

![image-20231031182634005](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031182634005.png) 

![image-20231031182643860](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031182643860.png) 

那我们的diaplan应该怎么写

 ![image-20231031191943250](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031191943250.png) 



![image-20231031191952477](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031191952477.png) 



#### 四、diaplan转接lua、Python等等脚本

![image-20231031192018584](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031192018584.png) 

这里需要注意对应的diaplan.lua脚本应该放入在script目录下



#### 五、使用curl等等application

freeswitch的diaplan拥有很多复杂的功能，比如anti-acion与action的配置，condition嵌套condition，这些都可以在《Freeswitch权威指南》中有详细介绍。

但是这边仍然希望能够将复杂的逻辑交于代码实现，那么就可以借助curl来实现很多好玩的diaplan。

使用curl，首先得load_mod_curl，然后我们写

![image-20231031192441001](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031192441001.png) 

我们可以通过http请求将对应的数据发送给我们写的后端服务，然后通过esl进行command形式来实现我们想要的目的。

因此复杂的diaplan使用curl是一个非常好的选择，当然你可以尝试lua、Python等等脚本去实现。



#### 六、使用lua、Python脚本语言

使用脚本语言lua、python来做复杂的diaplan是一个非常好的选择，当然这需要掌握lua、python脚本语法。

这里就不多赘述。



### Java ESL连接

我们通常用客户端形式连接到freeswitch，使用的是Java语言

pom依赖

```xml
<dependency>
    <groupId>org.freeswitch.esl.client</groupId>
    <artifactId>org.freeswitch.esl.client</artifactId>
    <version>0.9.2</version>
</dependency>
```

然后就可以在服务中进行创建esl inbound 方式连接到freeswitch。

![image-20231031194434673](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231031194434673.png)

使用client连接后，监听``IEslEventListener``后，就可以监听各类通道事件了。

通常的通道事件，create、answer、bridge、hangup等

```
CHANNEL_CREATE, //通道创建事件
HEARTBEAT,//心跳包
SESSION_HEARTBEAT,
CHANNEL_ANSWER,
CHANNEL_HANGUP_COMPLETE,
PLAYBACK_STOP
```



创建完成的client，可以发送同步命令和异步命令两种方式。

```
sendAsyncApiCommand
sendSyncApiCommand
```



有了这两个方法，我们可以通过Java程序来操作各类freeswitch命令，达到自己的目标。比如使用uuid_transfer进行转接：

```
String command = "uuid_transfer" + uuid + "-bleg" + extension;
EslMessage result = freeswitchClient.sendAsyncApiCommand(command, "");
```

esl可以做很多有意思的功能，比如为了节约客服的宝贵时间，批量给客户进行外呼，等客户接通后转接给空闲的坐席。

还比如，使用esl进行各类命令控制，callcenter的坐席上下线状态的动态控制，强制转接、监听进行质检。只要你想到的各类命令，通过一门语言进行esl链接，能够非常好的完成你的目标。

当然我们也可以使用其他语言的esl连接，包括c、python、golang等等。



### freeswitch 测试

#### 1、测试外呼是否正常

```
orgiginate {origination_caller_id_number=网关落地号码}
sofia/gateway/outbound/手机号 &echo
```

echo应用是奖你的声音回传给你



#### 2、sngrep

安装sngrep后，通过sngrep命令监听sip信息，定位Ip端口是否正常，sip ip是否有问题。

![image-20231101142329566](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101142329566.png) 

![image-20231101164844942](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101164844942.png) 

#### 3、tcpdump

使用tcpdump进行抓包，将抓取的数据包使用wireshark进行分析

抓包后导入到wireshark，那么如何进行分析sip消息呢？非常简单，在Filter中输入框输入sip即可。

![image-20231101165026802](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165026802.png) 

当然使用wireshark分析虽然比Freeswitch里面开启siptrace on 便捷，但仍然比不上sngrep命令，不过wireshark分析sdp、rtp等等拥有更好的能力。

在主菜单选择-Telephone-> VOIP Calls。

![image-20231101165153241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165153241.png) 

选择对应的电话，可以清晰的看到电话的基础信息。

![image-20231101165232104](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165232104.png) 

点击flow sequence，马上就可以看见可视化的sip信息

![image-20231101165303363](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165303363.png) 

而对于rtp协议来说，wireshark不仅仅提供分析工具，还能够进行播放

![image-20231101165400325](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165400325.png) 



#### 4、fail2ban

fail2ban非常好用，我们可以使用他来拦截恶意的请求。



扫描

ubuntu、debian系统直接apt install fail2ban即可。

然后需要再freeswitch的各个sip profile里面配置

```
<param name="log-auth-failures" value="true"/>
```

一旦注册失败会产生如下日志：
![image-20231101165536491](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231101165536491.png) 



#### 5、分机号添加流程

```
https://blog.csdn.net/angellee1988/article/details/101615596
```

FreeSwitch默认设置了20个用户，如果需要更多的用户，那么只需要简单的三步就可以完成。

在conf/directory/default/中增加一个用户配置文件
修改拨号计划（Dialplan）使其它用户可以呼叫它
重新加载配置使其生效
要添加用户Jason，分机号是1020，只需要到conf/directory/default目录下，将1000.xml拷贝到1020.xml，然后打开1020.xml，将所有1000都改为1020，并把effective_caller_id_name的值改为Jason，然后保存退出。

如：<variable name="effective_caller_id_name" value="Jason"/>

接下来，打开 conf/dialplan/default.xml，找到

```
 <condition fied=“destionation_number” expression=“^(10[01][0-9]) $” > 行，将其改为：

<condition field=“destionation_number” expresstion=“^(10[01][0-9]|1020) $” >
```

保存退出，回到控制台，然后执行reloadxml命令或按快捷键F6，使新的配置生效，那么新用户1020便添加成功。

如果你在某个运营商拥有SIP账号，就可以配置拨打外部电话。

























































