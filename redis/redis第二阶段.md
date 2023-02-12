# redis第二阶段

## 使用redis实现点赞

使用sadd like:1000 102这样记录哪个用户点了赞

```shell
127.0.0.1:6379> sadd like:1000 101
(integer) 1
127.0.0.1:6379> sadd like:1000 102
(integer) 1
127.0.0.1:6379> sadd like:1000 103
```

使用srem取消点赞



![image-20220706211921492](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220706211921492.png) 



![image-20220706211931599](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220706211931599.png) 

















































