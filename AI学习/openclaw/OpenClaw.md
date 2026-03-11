# OpenClaw

## OpenClaw安装

参考链接

```
https://blog.csdn.net/u014754814/article/details/158263452
```



**先更新npm到最新版本**

```
npm install -g npm@10
```



**设置npm允许旧版本（临时解决兼容性问题）**

```
npm config set legacy-peer-deps true
```



**清理缓存**

```
npm cache clean --force
```



**开始安装**

```
npm i -g openclaw --verbose 2>&1 | tee install_log.txt
```



**启动配置**

```
openclaw onboard --install-daemon
```































