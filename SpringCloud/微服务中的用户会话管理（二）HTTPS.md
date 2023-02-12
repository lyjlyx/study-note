# 微服务中的用户会话管理（二）



## Https原理讲解



**https是由HTTP + SSL组成，是建立在http连接上的，可以让传输更加的安全**



**我们的一次访问都需要经过很多东西，这些东西都是有风险的**

![image-20220215164132790](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220215164132790.png) 

**访问过程中很可能出现某一层被拦截，伪造了一个新的数据**



**双方可以商定一套加密算法，把明文加密之后得到密文，然后发送密文**

**加密算法(明文)=密文**

![image-20220215165529847](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220215165529847.png) 



**但是上图还是不安全，因为服务器和用户是一对多的，中间拦截商可以获得到加密算法**

![image-20220215165945191](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220215165945191.png) 

**盐是随机的，我们对盐进行加密，让盐不会被暴力破解**



1、对称加密(des、3des、AES)秘钥有一个

2、秘钥的传递是靠第二种算法，非对称加密算法传递的

**秘钥有两个 一个是公钥  一个是私钥**

明文->公钥->加密->密文->私钥->解密->明文(**公钥加密的密文再使用公钥解密是解密不出来的**)

明文->私钥->加密->密文->公钥->解密->明文



**公钥就是拿私钥生成出来的**



**私钥：其实就是一段特别长的字符串，压根就没办法猜到**

**只靠非对称加密也是不行的，浏览器没法判断这个公钥是不是真的由我们的服务器下发下来的**



**公钥加密私钥能解，私钥加密公钥能解，公钥加密公钥解不了**



对于数据进行非对称加密来保证数据安全。



![image-20220216102350288](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216102350288.png) 



只靠非对称加密是不行的，浏览器无法判断公钥是不是真的由服务器下发的。



**hash哈希算法(摘要算法)**

算法(接受入参=数据) = 结果

结果无法反推数据。



**数字签名**

相同的服务的数字签名是一样的，可以证伪



hash算法(公钥) = hash值  -> 公钥的签名/摘要

使用hash算法加密公钥得到的hash值，就是公钥的签名/摘要



**但是只要签名能够被中间拦截服务拿到，那么他还是可以伪造我们的公私钥，所以需要在他们之间有一个不传的东西**



**需要通过一个说明书(证书)来对于公私钥进行二次加密**

**服务端的公钥 用第三方的私钥进行加密，得到了一个密文。用hash算法对服务端的公钥进行加密，得到签名**

**密文和签名加起来就是证书**



浏览器端通过第三方的公钥去解密这个证书 = Server.(公钥+签名+签名算法)



浏览器端需要通过签名算法去校验签名对不对



![image-20220216124814738](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216124814738.png) 



## CA机构插入加密



![image-20220216134558899](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216134558899.png) 

![image-20220216134626227](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216134626227.png) 



**CA的公钥是提前内置在客户端的，客户端是不会去认中间代理下发的伪造的公私钥**



**代理服务器不去修改证书，他只是用CA公钥解开，拿到server的公钥之后再原封不动的把证书传递给客户端以后，数据再从客户端传给服务端的时候，代理服务器去偷看其中的内容？**



客户端如果发送请求到https的接口地址(访问https的时候)，他就会到该服务对应的443接口建立四次握手

客户端向服务端先进行一次通信握手，服务器端回客户端通信信息，并且下发证书，客户端与服务端协商加密算法一次，服务端下发对称加密的秘钥



协商好加密之后，再向服务端的80端口发送请求

![image-20220216135728253](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216135728253.png) 



首先 服务端下发证书到客户端，使用证书的CA私钥加密服务端的公钥，如果在中间环节被非法的代理服务器拦截住，通过CA证书的公钥解开了服务端下发到客户端的公钥(他拿到公钥想干坏事，窥探客户端发送给服务端的信息)，但是客户端使用Ca公钥解开之后(数据是使用服务端和客户端约定的**对称加密**的秘钥去加密的，非法代理服务器想要窥探我们的信息必须要获取到这个秘钥)，会使用Server端发送的**公钥**去给加密信息的秘钥去进行加密，虽然非法代理服务器获取  到了server的公钥，但是他没办法使用这个公钥去解密这个数据

![image-20220216163525195](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216163525195.png)



![image-20220216165820896](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216165820896.png) ![image-20220216165855566](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216165855566.png) 





**在第二步如果第三者将证书替换为自己合法的证书，是不是就可以拿到对称加密的秘钥？如果在第一步的时候第三者就不让你进server的443端口该怎么办？**

- 想要伪造证书是需要获得CA的私钥的，所以是没办法的
- 证书里面有签名信息，证书是伪造不了的。如果是自签名的话浏览器是不会认的，浏览器只认操作系统中所颁发的证书



**如果拦截者自己去向CA签名呢？**

- CA证书的颁发是非常复杂的，是有CA保障的，所以拦截者是不可能这样子操作的



![image-20220216171104348](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216171104348.png) 



## Https真的安全吗？

### 为什么域名可以自动跳到https

**302浏览器会自动跳转到location上面的地址，重定向**

![image-20220216173955671](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216173955671.png) 

**浏览器的307协议不允许location脱离请求地址的范围，就是请求地址http必须只能访问https**

**307的重定向，只允许域名不变，协议变**



**但是这样还是不能解决问题，因为第三者会直接不让你就访问https直接就在中间给你拦截了**

没办法解决，只能在真实网站上给提示，**或者在服务端去规定某些接口，必须使用https的网址访问**

![image-20220216174858997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216174858997.png) 

## OpenSSL

### 查看系统已存证书

certmgr.msc

#### CSR

证书签名请求文件

#### CRT

证书

#### key

私钥

### OPenSSL 自签名

#### 下载

http://slproweb.com/products/Win32OpenSSL.html

#### 证书中的信息

- Country Name (2 letter code) [XX]:CN           #请求签署人的信息
- State or Province Name (full name) []: #请求签署人的省份名字
- Locality Name (eg, city) [Default City]:# 请求签署人的城市名字
- Organization Name (eg, company) [Default Company Ltd]:#请求签署人的公司名字
- Organizational Unit Name (eg, section) []:#请求签署人的部门名字
- Common Name (eg, your name or your server's hostname) []:#这里一般填写请求人的的服务器域名， 

### 服务器端证书

#### 1.生成私钥

找到OpenSSL安装目录下的/bin目录中的OpenSSL.exe

执行命令

`openssl genrsa -des3 -out c:/dev/server.key `

生成私钥，需要提供一个至少4位，最多1023位的密码

#### 2.由私钥创建待签名证书

```
openssl.exe req -new -key c:/dev/server.key -out c:/dev/pub.csr
```

需要依次输入国家，地区，城市，组织，组织单位，Common Name和Email。其中Common Name，可以写自己的名字或者域名，如果要支持https，Common Name应该与域名保持一致，否则会引起浏览器警告。



#### 3.查看证书中的内容

`openssl.exe req -text -in c:/dev/pub.csr -noout`



### 自建CA

我们用的操作系统（windows, linux, unix ,android, ios等）都预置了很多信任的根证书，比如我的windows中就包含VeriSign的根证书，那么浏览器访问服务器比如支付宝www.alipay.com时，SSL协议握手时服务器就会把它的服务器证书发给用户浏览器，而这本服务器证书又是比如VeriSign颁发的，自然就验证通过了。

#### 1.创建CA私钥

`openssl.exe genrsa -out c:/dev/myca.key 2048`

#### 2.生成CA待签名证书

`openssl.exe req -new -key c:/dev/myca.key -out c:/dev/myca.csr`

#### 3.生成CA根证书

`openssl.exe x509 -req -in c:/dev/myca.csr -extensions v3_ca -signkey c:/dev/myca.key -out myca.crt`

#### 4.对服务器证书签名

`openssl x509 -days 365 -req -in c:/dev/pub.csr -extensions v3_req -CAkey c:/dev/myca.key -CA c:/dev/myca.crt -CAcreateserial -out c:/dev/server.crt`



### Nginx配置

```
     server {
             listen       443 ssl;
             server_name  aa.abc.com;

             ssl_certificate      /data/cert/server.crt;
             ssl_certificate_key  /data/cert/server.key;

     }
```

### 信任



![image-20220216223316111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220216223316111.png) 

如何让不安全的https编程安全的





 在系统中安装证书



### 图形化工具

https://sourceforge.net/projects/xca/



## 线上服务器安装配置

gigsgigscloud.com



cn2 gia

### 域名解析

## 免费签名

https://freessl.cn

## SS安装

服务器端

```
wget --no-check-certificate https://raw.githubusercontent.com/teddysun/shadowsocks_install/master/shadowsocksR.sh
chmod +x shadowsocksR.sh
./shadowsocksR.sh 2>&1 | tee shadowsocksR.log
```

卸载

```
./shadowsocksR.sh uninstall
```

运行状态

```
/etc/init.d/shadowsocks status
```

































































































































































































































