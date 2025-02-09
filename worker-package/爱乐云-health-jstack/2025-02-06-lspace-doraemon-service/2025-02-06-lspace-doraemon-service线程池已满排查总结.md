# 2025-02-06-lspace-doraemon-service线程池已满排查总结

根据jstack日志查看到大量的jdbc WAITING的日志数据，从日志的内容可以分析出来时在等待jdbc的链接，也就是说数据库连接池durid已经满了，反想一下应该是数据库有什么问题，有可能是CPU满了，也有可能有慢sql，所以我们需要去排查数据库服务器

![image-20250207120902874](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250207120902874.png)



从数据库监控可以看到当前时间节点的读写库CPU已经到达了100%，虽然说数据库架构为一读一写，但是当读写库满了之后只读库肯定也满了

![image-20250207121045417](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250207121045417.png) ![image-20250207121212907](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250207121212907.png) 



初步判定为慢sql导致的CPU满载，所以去查看执行中的sql是否有慢sql，筛选当前节点往前的告警日志，再针对得出的sql去修改优化

 ![image-20250207153155575](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250207153155575.png)































