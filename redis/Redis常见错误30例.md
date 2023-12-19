# Redis常见错误30例

### 1、非法地址

错误提示为：**“redis.clients.jedis.exceptions.JedisDataException: ERR illegal address”。**

也有一些提示为：**“Error: Insert the diskette for drive %1.”**。

究其原因是，Redis 实例配置了白名单，但当前访问 Redis 的客户端（IP）不在白名单中。一般是使用了阿里云 Redis 提供客户端白名单功能。



### 2、不兼容 Sentinel 哨兵模式

报错详情为：**“ERR sentinel compatibility mode is disabled”**。

可能原因：未开启 Redis 实例的 Sentinel 兼容。

解决方法：在控制台开启 Sentinel 兼容。



### 3、超过实例的最大连接数

报错信息为：**“ERR max number of clients reached”。**

可能原因：客户端的连接数超过了 Redis 实例的最大连接数。

解决方法：检查客户端是否出现连接泄露，例如在 Jedis 客户端中，使用连接池后未调用 close 函数。通过实例会话查看当前连接实例的会话是否符合预期，如果符合预期，可以升级实例配置，也可以扩大连接数。



### 4、没有使用密码登录认证

报错信息为：**“(error) NOAUTH Authentication required.”。**

可能原因：Redis 实例设置了密码鉴权，但客户端没有提供密码或提供了错误的密码。

解决方法：请使用正确的账号密码进行访问，或者重置为新的密码。



### 5、用户名密码对无效

报错信息为：**“WRONGPASS invalid username-password pair”。**

可能原因：密码错误。

Redis 通过 AUTH 命令用来设置。有如下两种方式对访问 Redis 进行权限控制：

​	a. 通过 requirepass 设置访问密码。

​	b. Redis 6.0 起，支持 ACL 权限控制。

Redis 6.0 之前的版本只支持验证密码方式的权限控制，

格式如下：

​	AUTH <password>

对应于配置文件中的 requirepass 命令。通过配置 requirepass ，可以使 Redis 拒绝未使用 AUTH 验证访问权限的客户端链接。

如果 AUTH 命令验证的密码与配置文件 requirepass 配置的相同，则密码验证通过，服务器返回 OK，并开始接受客户端的命令。

密码验证失败，则返回提示信息重新输入密码。

当使用 Redis ACLs 权限控制时，AUTH 验证格式如下：

AUTH <username> <password>

在 ACLs 模式下，如果传递一个密码参数给 AUTH，那么会隐式设置用户名 "default" ！！



### 6、密码不正确

报错信息为：“**ERR invalid password**”。

异常的产生原因是 Redis 连接密码不正确。

当我们使用 Lettuce 客户端连接 Redis 时，需要提供正确的密码才能成功建立连接。如果密码不正确，Redis 服务器将返回该异常信息。

解决方法：使用正确的密码，或重设密码。



### 7、连接被对端重置可能原因：

客户端连接被关闭，通常是由于客户端缓冲区异常而关闭客户端连接。

解决方法：

检查应用侧代码或调整客户端 Buffer 的大小，更多信息请参见：

```
https://mp.weixin.qq.com/s/MjSXS0tR5hxmyCWmFpzTEA。
```



### 8、未知的域名ip

报错信息为：

**“redis java.net.UnknownHostException“**，或为：**“failed to connect: xxx could not be resolved”**。

可能原因：错误的主机名或 IP 地址: 当我们尝试使用错误的主机名或 IP 地址连接到 Redis 服务器时，就会抛出这个异常。这可能是由于拼写错误、格式错误或指定了不存在的主机名或 IP 地址。

DNS 解析问题: 当我们的网络环境中存在 DNS 解析问题时，也可能导致这个异常的发生。这可能是由于 DNS 服务器故障、网络连接问题或配置错误引起的。

解决方案：

使用正确的主机名或 IP 地址，检查网络和 DNS 配置，检查主机名或 IP 地址。



### 9、内存溢出

报错信息为：

“**OOM command not allowed when used memory > 'maxmemory'**”。

翻译过来就是，当前命令不被允许，因为 reids 使用的内存超过了设置的最大内存，导致了 OOM。

OOM 是内存溢出。举个例子，当前命令执行时要占用 100M 内存，maxmemory 内存为 200M，已使用 180M 内存，执行当前命令时，通过回收垃圾后也存不下当前命令的内容，就会抛出这个异常。

此时，通常有两个解决办法。

​	A、增加 redis 内存。修改 redis.conf 配置项 maxmemory，增加 redis 的内存，如：maxmemory 2gb。maxmemory 默认为 1024MB。

​	B、设置淘汰策略或修改为更合理的淘汰策略。CONFIG SET maxmemory-policy volatile-lru。建议修改 redis.conf，配置 maxmemory-policy volatile-lru 内容，重启后生效。



### 10、未知的命令

报错信息为：

​	**“ERR unknown command 'xxx'”**。

例如，我们最近就遇到了一个“ERR unknown command 'HRANDFIELD'”的错误。

错误表明 Redis 实例不支持 HRANDFIELD 命令。

HRANDFIELD 是 Redis 6.2.0 版本引入的新命令，用于在 Redis 哈希中随机获取字段和值。

解决方法：升级版本，或不使用该命令。



### 11、对指定key的的错误类型操作

报错信息：

**“WRONGTYPE Operation against a key holding the wrong kind of value“。**

可能原因：

​	相对于往 Redis 放入一个 String 类型，结果以 Map 集合类型读取，便会发生该错误。解决方法：可以先试用 type key，查看指定 key 的类型。如果为 hash 类型，则操作这个数据就必须使用 hset、hget 等方法；如果是 zset，使用 zadd、zrange 等操作。



### 12、当前账户不支持执行此命令

报错信息：

​	**“ERR command 'xxx' not support for your account”。**

可能原因：阿里云禁止用户执行某些 Redis 命令。

解决方法：手动在 #no_loose_disabled-commands 参数中移除已配置了被禁止执行的命令。



### 13、用户没有执行命令的操作权限

报错信息：

​	**“NOPERM this user has no permissions to run the 'xxx'”。**

常见的有：

​	**“NOPERM this user has no permissions to run the config SET command”。**

通常生产环境会禁用config SET、FLUSHALL、KEYS、DEBUG SEGFAULT 等命令。

解决方法：手动在 #no_loose_disabled-commands 参数中移除已配置了被禁止执行的命令。或者在 redis.conf 中移除、注释掉重命名命令的相关配置。#rename-command KEYS "

"#rename-command FLUSHALL "

"#rename-command DEBUG  ""



### 14、实例迁移模式下禁止执行某些命令

报错信息：

**“ERR FLUSHDB is not allowed in migrating mode”。**

可能原因：

​	Redis 云盘集群架构实例在执行增加或减少数据节点时，禁止使用 FLUSHDB 或 FLUSHALL 命令。解决方法：等待 Redis 云盘集群架构实例执行增加或减少数据节点结束。



### 15、多个 Key 不在同一个 slot

报错信息：

**“CROSSSLOT Keys in request don't hash to the same slot”。**

可能原因：Redis 集群架构直连模式不支持跨 Slot 执行涉及多 Key 的命令，例如 DEL、MSET、MGET 等。

![image-20231219101117935](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231219101117935.png) 

解决方法： 

​	**使用 hash tag；或者支持使用 CLUSTER KEYSLOT 命令的改命令；或者改造实例为集群架构代理（Proxy）模式。**



### 16、主从模式从不支持写命令

报错信息：

​	**“READONLY You can‘t write against a read only replica.”。**

可能原因：当前访问的 redis 是从（slave）节点，进入 redis 客户端通过 role 指令可以查看 redis 是 master、slave 还是 sentinel。

![image-20231219101215511](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231219101215511.png)

解决方法：redis 默认 slave 节点的 replica-read-only 属性为 yes（只读），将其修改为 no 即可（非集群模式下有效，cluster 模式下无效）；或者直接修改 redis.config 文件，搜索 replica-read-only 设置为 no 即可。



### 17、只读模式不支持写入

报错信息：

​	**“ERR READONLY you can't write against a read only instance”**。

可能原因：

​	Redis 实例在主备切换、升降配或小版本升级时，将出现秒级的连接闪断和 30 秒以内的只读状态。

解决方法：

​	属于正常现象，实例会自动恢复，无需进行任何操作，注意你的应用需要支持重连机制和异常处理的能力。



### 18、ip不在白名单中

报错信息：

​	**“ERR client ip is not in whitelist”。**

可能原因：

​	未将客户端的 IP 地址添加至 Redis 实例的白名单中。

解决方法：

​	将客户端的 IP 地址添加入至 Redis 实例的白名单中。



### 19、实例不支持写入和读取

报错信息：

​	**“NOWRITE You can't write against a non-write redis”。**

​	或者：

​	**“NOREAD You can't read against a non-read redis”。**

可能原因：

​	实例处于欠费或到期状态，实例状态显示为已锁定。解决方法：充值，或对到期的实例进行续费。



### 20、语法错误

报错信息：

​	**“ERR syntax error”。**

可能原因：

​	命令语法错误，例如需要传入 4 个参数，实际仅传入 3 个参数。

解决方法：

​	检查命令格式是否正确



### 21、多个命令必须在同一个槽中

报错信息：

**“ERR 'xxx' command keys must in same slot“。**

可能原因：使用 lua 脚本等，通过事务或脚本执行命令时要求所有 Key 必须在同一个 Slot 中，否则将返回上述错误。

解决方法：改造事务或脚本。



### **22、请求被拒绝**

报错信息：

​	**“ERR request refused, too many pending request, now count xxx, beyond threshold xxx“。**

可能原因：

​	由于客户端使用了不合理的 Pipeline 等，Redis 后端堆积了过多未处理的 Request，新请求被拒绝。

解决方法：

​	减少 Pipeline 的请求数量。



### 23、临时故障

报错信息：

​	**“ERR redis temporary failure“。**

可能原因：

​	部分 Redis 子实例访问超时，可能是网络抖动、连接数到达上限导致断链、实例正在进行主备切换或执行慢查询等导致。

解决方法：

​	修复网络、检查集群状态等。注意，程序需要支持重连机制和异常处理的能力。



### 24、没有匹配的脚本

报错信息：

​	**“NOSCRIPT No matching script. Please use EVAL.“。**

可能原因：

​	在一主多从的环境中使用 evalsha 时，从服务有可能会出现未成功加载 lua 脚本的情况。

解决方法：

​	通过 EVAL 命令或 SCRIPT LOAD 命令将目标脚本缓存至 Redis 中后进行重试。



### 25、普通用户不支持eval命令

报错信息：

​	**“ERR command eval not support for normal user“。**

可能原因：

​	无法执行 EVAL 的相关命令。

解决方法：

​	请将实例的版本升级至最新或正确的版本，或切换不同的 user。



### 26、事务异常

报错信息：

​	**“EXECABORT Transaction discarded because of previous errors“。**

![image-20231219101619294](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231219101619294.png)

可能原因：

​	事务中的命令执行失败，可能是命令语法错误或运行错误等。

解决方法：

​	检查代码逻辑，修复错误命令。



### 27、客户端缓冲区异常

报错信息：

​	**“Unexpected end of stream“。**

![image-20231219101631465](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231219101631465.png)

可能原因：

1. 多个线程使用一个 Jedis 连接；

2. 客户端缓冲区满了，包括：普通客户端缓冲区(normal)、slave 客户端缓冲区(slave)、发布订阅缓冲区(pubsub)；

3. 长时间闲置连接

解决方法：

​	检查代码逻辑，让一个线程使用一个 Jedis 连接，可以使用 JedisPool 管理 Jedis 连接，实现线程安全，避免出现这种情况。排查是否是上述客户端缓冲区满了或长时间闲置连接原因。云数据库 Redis 版默认的 timeout 值为 0，目前不支持修改。client-output-buffer-limit 默认值为500MB，为阿里云优化后的合理值。如果超过该值，说明用户返回的值过多，出于性能和稳定性考虑，建议优化应用程序。



### 28、无法从连接池获取到连接

报错信息：

**“Could not get a resource from the pool“。**

可能原因：

​	Jedis 资源最大数量由 maxTotal 值决定，JedisPool 默认的 maxTotal 值为 8。出现这种情况的可能有多种因素，比如连接泄露、业务并发量大（maxTotal 值设置得过小）、Jedis 连接阻塞、Jedis 连接被拒绝、网络不稳等。

解决方法：

​	增加 maxTotal 大小，检查代码逻辑，修复问题代码。



### 29、类型转换失败

报错信息：

**“java.lang.Long cannot be cast to java.util.List“。**

![image-20231219101645284](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20231219101645284.png)

可能原因：

​	A 代码 put 了一个 long 类型的值，B 代码读取出来转换为 List 值。

解决方法：

​	用户排查自身代码是否存在问题，比如 get、hgetAll 等返回不同的类型检查。



### 30、正在加载持久化文件

报错信息：

​	**“LOADING Redis is loading the dataset in memory“。**

可能原因：

​	调用 Redis 时，Redis 正在加载持久化文件，无法进行正常的读写。一般出现在 Redis 启动程序正常读写阶段。

解决方法：

​	如果 Redis 出现故障，在他重启时，尽量等它启动完毕在操作，另外 aof、rdb 等文件尽可能的小。



### 31、管理员命令普通用户不能执行

报错信息：

​	**“ERR command role not support for normal user“。**

可能原因：

​	命令 role 不能被普通用户执行。

解决方法：

​	不能使用该命令，换其他命令或其他技术方案。





















































































































