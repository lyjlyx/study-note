# MySQL调优



**索引不生效的问题：**

![img](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/2022080215084833.png) 



![image-20220613212304894](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220613212304894.png) 



## 数据库表的创建、表的约束、索引、数据库 

正规的表结构设计需要使用第三方工具powerdesigner



### 数据库设计三范式 

**解决数据冗余**

#### 第一范式(确保每列保持原子性)

第一范式是最基本的范式，如果数据库表中的 **所有字段值都是不可分解的原子值，就说明该数据库表满足了第一范式**

**列不可再分**



#### 第二范式(确保表中的每列都和主键相关)

第二范式在第一范式的基础上更近一层，第二范式需要确保数据库表中的每一列都和主键相关，而不能只与主键的某一部分相关(主要这对联合主键而言)

![image-20220407224038935](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220407224038935.png) 



#### 第三范式(表里面的列不能出现其他表的非主键字段)

**要求一个数据库表中不包含在其他表中已包含的非关键字信息**

![image-20220407224432112](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220407224432112.png)



**表依赖关系**



注册驱动

SPI



## mysql基础层次

mysql3层，Client、Server、存储引擎



优化方式

RBO基于规则的优化

CBO基于成本的优化



## 索引

![image-20220406133042312](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220406133042312.png) 

**索引一般存在什么地方？**

本地的磁盘文件中，

局部性原理：

时间局部性

空间局部性

磁盘预读：



索引怎么加载，先去加载对应的文件，4k4k的读取。

![image-20220406134231032](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220406134231032.png) 



![image-20220406134404975](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220406134404975.png) 



![image-20220410201553092](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410201553092.png) 

**非叶子节点只存储key的值，没有value**

**key不一定是主键，但是	肯定是索引键，如果你查询的普通字段也是索引列的话，那么先去这颗B+树上找对应的叶子节点然后查询到主键，然后再去主键的B+树上查找**



回表(查了索引，还要查当前表中主键索引)、覆盖索引、组合索引、最左匹配原则

### 索引的分类

mysql索引的五种类型：主键索引、唯一索引、组合索引、普通索引、全文索引

- 主键索引
  - 主键是一种唯一性索引，它必须制定为Primary Key，各表只能有一个主键索引
- 唯一索引
  - 索引的所有值都只能出现一次，即必须唯一，值可以为空
- 普通索引
  - 基本的索引类型，值可以为空，没有唯一性的限制(**覆盖索引**)
- 全文索引 MyISAM支持，Innodb在5.6之后支持
  - 全文索引的索引类型为FULLTEXT，全文索引可以在varchar、char、text类型的列上创建
- 组合索引
  - 多列值组成一个索引，专门用于组合搜索(**最左匹配原则**)



**自增id是通过什么来维护的？**

自增锁来维护，如果没有特殊要求，要把id主键设为自增，这样子更方便维护。



第三个格子的4k空间已经满了，要插入53这个数据，因为数据是有序性的，所以需要把第三个格子分裂成不同的页数，把53排进去

![image-20220410182250901](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410182250901.png) 

页分裂

第三个格子会造成浪费

![image-20220410182454627](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410182454627.png) 

如果又把53给删了

![image-20220410182523819](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410182523819.png) 

第三个格子和第四个格子可以进行合并

都会进行IO操作

**覆盖索引：本来要进行查询的时候要遍历两次B+树，使用覆盖索引可以简化成一次**



搜索引擎，倒排索引



建组合索引的时候要遵从最左匹配原则，要选择一个合理的顺序



**索引下推**

在回表之前就帮我们把数据进行了一次筛选



#### 面试难点问题

- 回表
- 覆盖索引
- 最左前缀
- 索引下推



### mysql存储引擎的区别

![image-20220410201655160](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410201655160.png) 

**行锁比较细**



### 索引维护

![image-20220410210301138](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410210301138.png) 



## MySQL架构

![image-20220410210552541](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410210552541.png) 



优化：RBO基于规则的优化，CBO基于成本的优化，要选择不同的优化策略，现在主要使用CBO

![image-20220410211158452](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410211158452.png)



**不推荐使用查询缓存**

![image-20220410211216545](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410211216545.png)

![image-20220410211328838](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410211328838.png)

![image-20220410211431774](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410211431774.png) 



**binlog、redolog、undolog**

ACID：原子性、一致性、隔离性、持久性

​			undolog

服务端和存储引擎端所使用的日志是不一样的，在服务器里面放的日志叫做binlog，而对于存储引擎中是有redolog和undolog的

binlog属于是服务端，所以不管是innodb也好还是myisam也好都可以使用，而redolog则只适合innodb，



### RedoLog

![image-20220410211813659](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410211813659.png) 

WAL：write ahead log（预写日志）



**先写到内存，然后在合适的时间把内存的数据同步到磁盘。**



**用户空间、内核空间**

每次写数据的时候都会写到logBuffer（redo log buffer, undo log buffer）里面，LogBuffer不是把数据直接写到磁盘中的，而是先写到内核空间里面的一个OS Buffer(操作系统的缓存空间)，相当于从一快缓存到另一块缓存，速度很快。操作系统空间会不定期的执行fsync()同步写的过程，他会把OS Buffer里面的数据写到磁盘中，执行fsync()操作的时候才是真正意义的将数据写到磁盘中了。



 **如果在写完redo log日志之后数据还没有溢写（没写入到磁盘）的话，此时数据库断电了怎么办？**

下次数据在启动的时候会根据 redo log来进行数据恢复。



![image-20220410212127562](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410212127562.png) 



以不同的方式进行溢写

![image-20220410212447849](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410212447849.png) 



哪种方式最安全？

第二种最安全，不管什么操作，只要提交就写，数据不会丢失。但是第一种和第三种性能比较好



**RedoLog**是固定大小的

![image-20220611174045595](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220611174045595.png) 



循环写的过程中有两个指针，一个叫checkpoint，一个叫Write pos

**Redolog是循环写的过程，log文件自然有限，如果数据量太大超过文件大小了怎么办？**

**需要先把前面的数据先写到磁盘中，虽然很浪费时间，但依然还是要先进行修改，修改完之后就把这些信息擦除掉。**



### UndoLog

回滚日志

![image-20220410213336728](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410213336728.png) 

要不然全部执行成功，要不然全部执行失败



### BinLog

服务端的日志文件

![image-20220410213643577](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410213643577.png) 

原始逻辑：既会记录我们的操作，也会记录我们的数据

binlog是默认不开启的

![image-20220410213946536](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410213946536.png) 

![image-20220410214045697](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410214045697.png) 



![image-20220410214320495](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410214320495.png) 

**执行流程**

1. 执行器先从引擎中找到数据，如果在内存中，直接返回，如果不在内存中，查询后返回。
2. 执行器拿到数据之后会先修改数据，然后调用引擎接口重新写入数据
3. 引擎将数据更新到内存，同时写上到redo中，此时处于prepare阶段，并通知执行器执行完成，随时可以操作
4. 执行器生成这个操作的binlong
5. 执行器调用引擎的事务提交接口，引擎把刚刚写完的redo改成commit状态，更新完成。

为什么写入数据的时候要分两阶段提交？

为了保证数据的一致性

### Redolog 的两阶段提交

![image-20220410214854912](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410214854912.png) 





## MySQL的执行计划

连接器->语法词法分析器->优化器->执行器

![image-20220411123440501](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411123440501.png) 

explain  extended:过滤



id越大越先执行，id如果相同那么就按照顺序执行



![image-20220411130905111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411130905111.png) 

SIMPLE简单查询，不包含子查询

PRIMARY 外层的查询会标记为PRIMARY

DEPENDENT UNION    外部查询需要依赖于里面的值	

![image-20220411131415814](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411131415814.png) 

UNION RESULT：结果集合，从union表获取结果的select

SUBQUERY：在select或者列表中包含子查询(如果子查询中只有一个值的话那么就是SUBQUERY，如果是多个值那么就是DEPENDENT SUBQUERY)

DERIVED：衍生、派生。from子句中出现的子查询，也叫做派生类，查询虚拟表

![image-20220411132319484](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411132319484.png) 

UNCACHEABLE UNION：表示union的查询结果不能被缓存

UNCACHEABLE SUBQUERY：表示子查询的结果不能被缓存

![image-20220411132452489](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411132452489.png) 

![image-20220411132707676](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411132707676.png) 

![image-20220411132938987](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411132938987.png)

![image-20220411133014248](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411133014248.png) 



AST(抽象语法树)

常用的语法切分工具

calcite.apache.org

![image-20220411133428449](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411133428449.png) 

partitions(分区) 



type(类型)

![image-20220411133953762](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411133953762.png) 

![image-20220411134120240](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411134120240.png) 

![image-20220411134425738](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411134425738.png) 

![image-20220411135251567](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411135251567.png) 

一般不用

![image-20220411215643092](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220411215643092.png)



## MySQL锁的机制



ACID

A原子性通过undolog来实现，I隔离性通过MVCC+锁机制实现，D持久性通过redolog来实现，

**锁是计算机协调多个进程或现场并发访问某一资源的机制。**在数据库中，除了传统的计算机资源(如CPU、RAM、I/O等)的争用以外，数据也是一种供许多用户共享的资源。如何保证数据并发访问的一致性、有效性是所有数据必须解决的一个问题，锁冲突也是影响数据库并发访问性能的一个重要因素。从这个角度来说，锁对数据库而言显得尤其重要，也更加复杂。

相对于其他数据库而言，mysql的锁机制比较简单，其最显著的特点是不同的存储引擎支持不同的锁机制。比如：MyISAM和MEMORY存储引擎采用的是表级锁(table-level locking);InnoDB存储引擎支持行级锁。

**表级锁：**开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高，并发度最低。

**行级锁：**开销大，加锁慢，会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度也最高。

从上述特点可见，很难笼统的说哪种锁更好，只能就具体应用的特点来说哪种更合适！仅从锁的角度来说：表级锁更适合于以查询为主，只有少量按索引条件更新数据的应用，如Web应用；而行级锁则更适合于有大量按索引条件并发更新少量不同数据，同时又有并发查询的应用，如一些在线事务处理(OLTP)系统。

OLTP online transaction process、OLAP online analysis process 在线分析



### MyISAM表锁

有两种锁的模式：表共享读锁(Table Read Lock)、表独占写锁(Table Write Lock)

![image-20220412131420727](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412131420727.png) 

![image-20220412131637226](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412131637226.png) 

![image-20220412131626907](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412131626907.png) 



![image-20220412131853196](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412131853196.png) 

![image-20220412132041986](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412132041986.png) 	



为什么读锁会阻塞写操作？保障数据一致性，避免脏读。

![image-20220412132601256](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412132601256.png)

concurrent_insert 是AUTO

![image-20220412134515264](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412134515264.png) 

### InnoDB锁

![image-20220412200606838](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412200606838.png) 



原子性：**undolog实现原子性，隔离性通过锁机制，持久性通过redolog实现，三者共同达成一致性的目标**

![image-20220412202008617](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412202008617.png) 



![image-20220412202104208](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412202104208.png) 



 mysql默认的隔离级别是：**可重复读**

![image-20220412202335578](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412202335578.png) 

**innoDB的行锁模型及加锁方法**



**共享锁：**又称读锁，允许一个事务去读一行，组织其他事务获得相同数据集的排它锁，若事务T对数据对象A加上S锁，则事务T可以读A但不能修改A，其他事务只能在对A加S锁，而不能加X锁，直到T释放A上的S锁。这保证了其他事务可以读A，但在T释放A上的S锁之前不能对A做任何修改。

![image-20220412202658711](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412202658711.png) 



mysql InnoDB引擎默认的修改数据语句：update、delete、insert都会自动给涉及到的数据加上排它锁，select语句默认不会加任何锁类型，如果排它锁可以使用select...for update语句，加共享锁可以使用select...lock in share mode语句。**所以加过排它锁的数据行在其他事务中是不能修改数据的，也不能通过for update 和lock in share mode 锁的方式查询数据，但可以通过select...from...查询数据，因为普通查询没有任何锁机制。**



![image-20220412204253407](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412204253407.png) 



1、在不通过索引条件查询的时候，innodb使用的是表锁而不是行锁

```sql
create table tab_no_index(id int,name varchar(10)) engine=innodb;
insert into tab_no_index values(1,'1'),(2,'2'),(3,'3'),(4,'4');
```

|                           session1                           |                           session2                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| set autocommit=0<br />select * from tab_no_index where id = 1; | set autocommit=0<br />select * from tab_no_index where id =2 |
|      select * from tab_no_index where id = 1 for update      |                                                              |
|                                                              |     select * from tab_no_index where id = 2 for update;      |

session1只给一行加了排他锁，但是session2在请求其他行的排他锁的时候，会出现锁等待。原因是在没有索引的情况下，innodb只能使用表锁。

```
-- 关闭当前会话的自动提交
set autocommit=0;
-- 全局关闭
set global autocommit
```

**session2为什么也要加for update？**

for update表示的是排它锁的意思，在session添加排它锁的时候是给id=1的一行加了行锁，session2是在id=2的这行也加行锁，意味着使用行锁的话这两个之间是不会互相干扰的，但是当我们锁定第一行之后再锁定第二行，第二行没办法进行重复锁定，所以使用的是表锁而不是行锁。(因为在没有索引的情况下，InnoDB只能使用表锁)

2、创建带索引的表进行条件查询，innodb使用的是行锁

```sql
create table tab_with_index(id int,name varchar(10)) engine=innodb;
alter table tab_with_index add index id(id);
insert into tab_with_index values(1,'1'),(2,'2'),(3,'3'),(4,'4');
```

|                           session1                           |                           session2                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| set autocommit=0<br />select * from tab_with_indexwhere id = 1; | set autocommit=0<br />select * from tab_with_indexwhere id =2 |
|     select * from tab_with_indexwhere id = 1 for update      |                                                              |
|                                                              |     select * from tab_with_indexwhere id = 2 for update;     |

![image-20220412210633344](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412210633344.png) 

3、由于mysql的行锁是针对索引加的锁，不是针对记录加的锁，所以虽然是访问不同行的记录，但是依然无法访问到具体的数据

```sql
insert into tab_with_index  values(1,'4');
```

|                           session1                           |                           session2                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
|                       set autocommit=0                       |                       set autocommit=0                       |
| select * from tab_with_index where id = 1 and name='1' for update |                                                              |
|                                                              | select * from tab_with_index where id = 1 and name='4' for update<br />虽然session2访问的是和session1不同的记录，但是因为使用了相同的索引，所以需要等待锁 |

###  总结

**对于MyISAM的表锁，主要讨论了以下几点：** 
（1）共享读锁（S）之间是兼容的，但共享读锁（S）与排他写锁（X）之间，以及排他写锁（X）之间是互斥的，也就是说读和写是串行的。  

（2）在一定条件下，MyISAM允许查询和插入并发执行，我们可以利用这一点来解决应用中对同一表查询和插入的锁争用问题。 
（3）MyISAM默认的锁调度机制是写优先，这并不一定适合所有应用，用户可以通过设置LOW_PRIORITY_UPDATES参数，或在INSERT、UPDATE、DELETE语句中指定LOW_PRIORITY选项来调节读写锁的争用。 
（4）由于表锁的锁定粒度大，读写之间又是串行的，因此，如果更新操作较多，MyISAM表可能会出现严重的锁等待，可以考虑采用InnoDB表来减少锁冲突。

**对于InnoDB表，本文主要讨论了以下几项内容：** 
（1）InnoDB的行锁是基于索引实现的，如果不通过索引访问数据，InnoDB会使用表锁。 
（2）在不同的隔离级别下，InnoDB的锁机制和一致性读策略不同。

在了解InnoDB锁特性后，用户可以通过设计和SQL调整等措施减少锁冲突和死锁，包括：

- 尽量使用较低的隔离级别； 精心设计索引，并尽量使用索引访问数据，使加锁更精确，从而减少锁冲突的机会；
- 选择合理的事务大小，小事务发生锁冲突的几率也更小；
- 给记录集显式加锁时，最好一次性请求足够级别的锁。比如要修改数据的话，最好直接申请排他锁，而不是先申请共享锁，修改时再请求排他锁，这样容易产生死锁；
- 不同的程序访问一组表时，应尽量约定以相同的顺序访问各表，对一个表而言，尽可能以固定的顺序存取表中的行。这样可以大大减少死锁的机会；
- 尽量用相等条件访问数据，这样可以避免间隙锁对并发插入的影响； 不要申请超过实际需要的锁级别；除非必须，查询时不要显示加锁；
- 对于一些特定的事务，可以使用表锁来提高处理速度或减少死锁的可能。



### mvcc多版本并发控制*

​	MVCC，全称Multi-Version Concurrency Control，即多版本并发控制，MVCC是一种并发控制的方法，一般在数据库管理系统中，实现对数据库的并发访问，在编程语言中实现事务内存。



#### 当前读（读取的是最新版本的数据）

​	像select lock in share mode(共享锁),select for update（排它锁）; update,insert,delete(排它锁)这些操作都是一种当前读，为什么叫当前读？就是他读取的是记录的最新版本，读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。



#### 快照读（读取的是历史版本的数据）

​	**他存在的原因是提高数据库的并发，并提高查询能力**

​	像不加锁的select操作就是快照读，即不加锁的非阻塞读；快照读的前提是隔离级别不是串行级别，串行级别下的快照读会退化成当前读；之所以出现快照读的情况，是基于提高并发性能的考虑，快照读的实现是基于多版本的并发控制，即MVCC，可以认为MVCC是行锁的一个变种，但他在很多情况下，避免了加锁操作，降低了开销；既然是基于多版本，即快照读可能读到的并不一定是数据的最新版本，而有可能是之前的历史版本。

当前行的历史数据，这些数据在undolog

当事务需要回滚的时候，就要找到当前行的上一个历史数据。所以可以直接从undolog里面直接取出当前数据行的结果返回。



无论什么样的隔离级别，数据读取的结果只有两种

1、跟原来的数据是一致的（历史数据、快照）

2、读取到了新数据（当前最新版本）



#### 当前读、快照读、MVCC之间的关系

​	MVCC多版本并发控制指的是维持一个数据的多个版本，使得读写操作没有冲突，快照读是MySQL为实现MVCC的一个非阻塞读功能，MVCC模块在mysql中的具体实现是由：三个隐式字段、undo日志、read view三个组件来实现的。

 

#### MVCC解决的问题

数据库并发场景有三种，分别为：

![image-20220607214713260](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220607214713260.png) 



#### MVCC实现原理

mvcc的实现原理主要是依赖于记录中的三个隐藏字段，undolog，read view来实现的

**DB_TRX_ID**

6个字节，最近修改事务id，记录创建这条记录或者最后一次修改该记

录的事务id

**DB_ROLL_PTR**

7字节，回滚指针，指向这条记录的上一个版本，用于配合undolog,指向上一个旧版本

**DB_ROW_ID**

6字节，隐藏的主键，如果数据没有主键，那么innodb会自动生成一个6字节的ROW_ID



数据库的行记录在进行删除的时候，不会直接把记录删掉，而是有一个删除标记（也是一个隐藏字段）。



![image-20220607223516030](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220607223516030.png) 

 

**undolog**

undolog被称之为回滚日志，表示在进行insert，delete，update操作的时候产生的方便回滚的日志

当进行insert操作的时候，产生的undolog只在事务回滚的时候需要，并且在事务提交之后可以被立刻丢弃

![image-20220607224036606](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220607224036606.png) 



#### read view

是事务进行快照读操作的时候产生的读视图，他最大的作用是用来做可见性判断的（之前做的修改能够对现在的读产生影响）。

![image-20220607225522901](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220607225522901.png) 

![image-20220607225532677](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220607225532677.png) 

#### MVCC的整体处理流程

假设有四个事务同时在执行，如下图所示：

|  事务1   |  事务2   |  事务3   |    事务4     |
| :------: | :------: | :------: | :----------: |
| 事务开始 | 事务开始 | 事务开始 |   事务开始   |
|  ......  |  ......  |  ......  | 修改且已提交 |
|  进行中  |  快照读  |  进行中  |              |
|  ......  |  ......  |  ......  |              |

从上述表格中，我们可以看到，当事务2对某行数据执行了快照读，数据库为该行数据生成一个Read View视图，可以看到事务1和事务3还在活跃状态，事务4在事务2快照读的前一刻提交了更新，所以，在Read View中记录了系统当前活跃事务1，3，维护在一个列表中。同时可以看到up_limit_id的值为1，而low_limit_id为5，如下图所示：

![image-20220608092803472](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608092803472.png)

在上述的例子中，只有事务4修改过该行记录，并在事务2进行快照读前，就提交了事务，所以该行当前数据的undolog如下所示：

![image-20220608092815048](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608092815048.png)

![image-20220608092919414](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608092919414.png)



​		当事务2在快照读该行记录的是，会拿着该行记录的DB_TRX_ID去跟up_limit_id,lower_limit_id和活跃事务列表进行比较，判读事务2能看到该行记录的版本是哪个。

​		具体流程如下：先拿该行记录的事务ID（4）去跟Read View中的up_limit_id相比较，判断是否小于，通过对比发现不小于，所以不符合条件，继续判断4是否大于等于low_limit_id,通过比较发现也不大于，所以不符合条件，判断事务4是否处理trx_list列表中，发现不再次列表中，那么符合可见性条件，所以事务4修改后提交的最新结果对事务2 的快照是可见的，因此，事务2读取到的最新数据记录是事务4所提交的版本，而事务4提交的版本也是全局角度的最新版本。如下图所示：

![image-20220608131952695](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608131952695.png) 

![image-20220608131930625](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608131930625.png)

当上述的内容都看明白了的话，那么大家就应该能够搞清楚这几个核心概念之间的关系了，下面我们讲一个不同的隔离级别下的快照读的不同。

#### RC、RR级别下的InnoDB快照读有什么不同

**RC（读已提交）和RR（可重复读）**

​		因为Read View生成时机的不同，从而造成RC、RR级别下快照读的结果的不同

​		1、在RR级别下的某个事务对某条记录的第一次快照读会创建一个快照即Read View,将当前系统活跃的其他事务记录起来，此后在调用快照读的时候，还是使用的是同一个Read View,所以只要当前事务在其他事务提交更新之前使用过快照读，那么之后的快照读使用的都是同一个Read View,所以对之后的修改不可见

​		2、在RR级别下，快照读生成Read View时，Read View会记录此时所有其他活动和事务的快照，这些事务的修改对于当前事务都是不可见的，而早于Read View创建的事务所做的修改均是可见

​		3、在RC级别下，事务中，每次快照读都会新生成一个快照和Read View,这就是我们在RC级别下的事务中可以看到别的事务提交的更新的原因。

​		**总结：在RC隔离级别下，每个快照读都会生成并获取最新的ReadView视图，而在RR隔离级别下，则是同一个事务中的第一个快照读才会创建ReadView，之后的快照读获取的都是同一个ReadView** 



### 描述一下事务的隔离级别

数据库的事务支持四个特性ACID

原子性undolog、一致性、隔离性、持久性

事务的隔离级别有四种：分别是读未提交、读已提交、可重复度、序列化。分别解决了脏读、幻读、不可重复读的问题

![image-20220608132837017](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220608132837017.png) 

SQL 标准定义了四个隔离级别：

- READ-UNCOMMITTED(读取未提交)： 事务的修改，即使没有提交，对其他事务也都是可见的。事务能够读取未提交的数据，这种情况称为脏读。
- READ-COMMITTED(读取已提交)： 事务读取已提交的数据，大多数数据库的默认隔离级别。当一个事务在执行过程中，数据被另外一个事务修改，造成本次事务前后读取的信息不一样，这种情况称为不可重复读。
- REPEATABLE-READ(可重复读)： 这个级别是MySQL的默认隔离级别，它解决了脏读的问题，同时也保证了同一个事务多次读取同样的记录是一致的，但这个级别还是会出现幻读的情况。幻读是指当一个事务A读取某一个范围的数据时，另一个事务B在这个范围插入行，A事务再次读取这个范围的数据时，会产生幻读
- SERIALIZABLE(可串行化)： 最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。

事务隔离机制的实现基于锁机制和并发调度。其中并发调度使用的是MVVC（多版本并发控制），通过保存修改的旧版本信息来支持并发一致性读和回滚等特性。

因为隔离级别越低，事务请求的锁越少，所以大部分数据库系统的隔离级别都是READ-COMMITTED(读取提交内容):，但是你要知道的是InnoDB 存储引擎默认使用 **REPEATABLE-READ（可重读）**并不会有任何性能损失。







## MySQL主从复制

![image-20220412214027784](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412214027784.png) 

### 1、什么是mysql的主从复制？

​	mysql主从复制是指数据可以从一个mysql数据库服务器主节点复制到一个或多个从节点，mysql默认采用异步复制方式，这样从节点不用一直访问主服务器来更新自己的数据，数据的更新可以在远程连接上进行，从节点可以复制主数据库中的所有数据库或者特定的数据库，或者特定的表。

### 2、mysql主从复制的原理

![image-20220412215006817](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412215006817.png) 

![image-20220412215711897](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412215711897.png) 

![image-20220412215414646](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412215414646.png) 



![image-20220412215810142](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220412215810142.png) 

**一主一从、一主多从、主主复制、多主一从、联级复制**



![image-20220413130950068](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413130950068.png) 



### 8、重启从服务器并进行相关配置

```shell
#重启mysql服务
service mysqld restart
#登录mysql
mysql -uroot -p
#连接主服务器
change master to master_host='192.168.85.11',master_user='root',master_password='123456',master_port=3306,master_log_file='master-bin.000001',master_log_pos=154;
#启动slave
start slave
#查看slave的状态
show slave status\G(注意没有分号)
```

### 9、此时可以在主服务器进行相关的数据添加删除工作，在从服务器看相关的状态



 ![image-20220413131255506](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413131255506.png) 



![image-20220413131648943](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413131648943.png) 



![image-20220413132004804](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413132004804.png) 



### 4、mysql主从同步延时分析

​		mysql的主从复制都是单线程的操作，主库对所有DDL和DML产生的日志写进binlog，由于binlog是顺序写，所以效率很高，slave的sql thread线程将主库的DDL和DML操作事件在slave中重放。DML和DDL的IO操作是随机的，不是顺序，所以成本要高很多，另一方面，由于sql thread也是单线程的，当主库的并发较高时，产生的DML数量超过slave的SQL thread所能处理的速度，或者当slave中有大型query语句产生了锁等待，那么延时就产生了。

​		解决方案：

​		1.业务的持久化层的实现采用分库架构，mysql服务可平行扩展，分散压力。

​		2.单个库读写分离，一主多从，主写从读，分散压力。这样从库压力比主库高，保护主库。

​		3.服务的基础架构在业务和mysql之间加入memcache或者redis的cache层。降低mysql的读压力。

​		4.不同业务的mysql物理上放在不同机器，分散压力。

​		5.使用比主库更好的硬件设备作为slave，mysql压力小，延迟自然会变小。

​		6.使用更加强劲的硬件设备 



**mysql 5.7之后用 MTS	并行复制技术，永久解决复制延时的问题， 自学。**



## MySQL读写分离

mysql Proxy 不建议在生产环境中使用 不稳定

![image-20220413133116996](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413133116996.png) 

![image-20220413133228079](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413133228079.png)

### 2、读写分离的配置

##### 1、硬件配置

```
master 192.168.85.11
slave  192.168.85.12
proxy  192.168.85.14
```

##### 2、首先在master和slave上配置主从复制

##### 3、进行proxy的相关配置

```shell
#1、下载mysql-proxy
https://downloads.mysql.com/archives/proxy/#downloads
#2、上传软件到proxy的机器
直接通过xftp进行上传
#3、解压安装包
tar -zxvf mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit.tar.gz
#4、修改解压后的目录
mv mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit mysql-proxy
#5、进入mysql-proxy的目录
cd mysql-proxy
#6、创建目录
mkdir conf
mkdir logs
#7、添加环境变量
#打开/etc/profile文件
vi /etc/profile
#在文件的最后面添加一下命令
export PATH=$PATH:/root/mysql-proxy/bin
#8、执行命令让环境变量生效
source /etc/profile
#9、进入conf目录，创建文件并添加一下内容
vi mysql-proxy.conf
添加内容
[mysql-proxy]
user=root
proxy-address=192.168.85.14:4040
#当前写的节点是什么
proxy-backend-addresses=192.168.85.11:3306
#当前读的节点是哪个
proxy-read-only-backend-addresses=192.168.85.12:3306
#lua脚本
proxy-lua-script=/root/mysql-proxy/share/doc/mysql-proxy/rw-splitting.lua
log-file=/root/mysql-proxy/logs/mysql-proxy.log
log-level=debug
daemon=true
#10、开启mysql-proxy
mysql-proxy --defaults-file=/root/mysql-proxy/conf/mysql-proxy.conf
#11、查看是否安装成功，打开日志文件
cd /root/mysql-proxy/logs
tail -100 mysql-proxy.log
#内容如下：表示安装成功
2019-10-11 21:49:41: (debug) max open file-descriptors = 1024
2019-10-11 21:49:41: (message) proxy listening on port 192.168.85.14:4040
2019-10-11 21:49:41: (message) added read/write backend: 192.168.85.11:3306
2019-10-11 21:49:41: (message) added read-only backend: 192.168.85.12:3306
2019-10-11 21:49:41: (debug) now running as user: root (0/0)

```

##### 4、进行连接

```shell
#mysql的命令行会出现无法连接的情况，所以建议使用客户端
mysql -uroot -p123 -h192.168.85.14 -P 4040
```

不建议在生产环境中使用是因为如果主挂了，之后再重启主，就无法插入数据了，代理直接就变成了从,因为我们配置了只有主可以插入。



通过代理 来实现数据库的读写分离



### 使用amoeba实现mysql的读写分离

####  1、什么是amoeba？

​		Amoeba(变形虫)项目，专注 分布式数据库 proxy 开发。座落与Client、DB Server(s)之间。对客户端透明。具有负载均衡、高可用性、sql过滤、读写分离、可路由相关的query到目标数据库、可并发请求多台数据库合并结果。

主要解决：

• 降低 数据切分带来的复杂多数据库结构

• 提供切分规则并降低 数据切分规则 给应用带来的影响

• 降低db 与客户端的连接数

• 读写分离

#### 2、为什么要用Amoeba

目前要实现mysql的主从读写分离，主要有以下几种方案：

1、  通过程序实现，网上很多现成的代码，比较复杂，如果添加从服务器要更改多台服务器的代码。

2、  通过mysql-proxy来实现，由于mysql-proxy的主从读写分离是通过lua脚本来实现，目前lua的脚本的开发跟不上节奏，而且没有完美的现成的脚本，因此导致用于生产环境的话风险比较大，据网上很多人说mysql-proxy的性能不高。

3、  自己开发接口实现，这种方案门槛高，开发成本高，不是一般的小公司能承担得起。

4、  利用阿里巴巴的开源项目Amoeba来实现，具有负载均衡、高可用性、sql过滤、读写分离、可路由相关的query到目标数据库，并且安装配置非常简单。国产的开源软件，应该支持，目前正在使用，不发表太多结论，一切等测试完再发表结论吧，哈哈！

#### 3、amoeba安装

##### 1、首先安装jdk，直接使用rpm包安装即可

##### 2、下载amoeba对应的版本https://sourceforge.net/projects/amoeba/，直接解压即可

##### 3、配置amoeba的配置文件

**dbServers.xml**

```xml
<?xml version="1.0" encoding="gbk"?>

<!DOCTYPE amoeba:dbServers SYSTEM "dbserver.dtd">
<amoeba:dbServers xmlns:amoeba="http://amoeba.meidusa.com/">

		<!-- 
			Each dbServer needs to be configured into a Pool,
			If you need to configure multiple dbServer with load balancing that can be simplified by the following configu
ration:			 add attribute with name virtual = "true" in dbServer, but the configuration does not allow the element with n
ame factoryConfig			 such as 'multiPool' dbServer   
		-->
		
	<dbServer name="abstractServer" abstractive="true">
		<factoryConfig class="com.meidusa.amoeba.mysql.net.MysqlServerConnectionFactory">
			<property name="connectionManager">${defaultManager}</property>
			<property name="sendBufferSize">64</property>
			<property name="receiveBufferSize">128</property>
				
			<!-- mysql port -->
			<property name="port">3306</property>
			
			<!-- mysql schema -->
			<property name="schema">msb</property>
			
			<!-- mysql user -->
			<property name="user">root</property>
			
			<property name="password">123456</property>
		</factoryConfig>

		<poolConfig class="com.meidusa.toolkit.common.poolable.PoolableObjectPool">
			<property name="maxActive">500</property>
			<property name="maxIdle">500</property>
			<property name="minIdle">1</property>
			<property name="minEvictableIdleTimeMillis">600000</property>
			<property name="timeBetweenEvictionRunsMillis">600000</property>
			<property name="testOnBorrow">true</property>
			<property name="testOnReturn">true</property>
			<property name="testWhileIdle">true</property>
		</poolConfig>
	</dbServer>

	<dbServer name="writedb"  parent="abstractServer">
		<factoryConfig>
			<!-- mysql ip -->
			<property name="ipAddress">192.168.85.11</property>
		</factoryConfig>
	</dbServer>
	
	<dbServer name="slave"  parent="abstractServer">
		<factoryConfig>
			<!-- mysql ip -->
			<property name="ipAddress">192.168.85.12</property>
		</factoryConfig>
	</dbServer>
	<dbServer name="myslave" virtual="true">
		<poolConfig class="com.meidusa.amoeba.server.MultipleServerPool">
			<!-- Load balancing strategy: 1=ROUNDROBIN , 2=WEIGHTBASED , 3=HA-->
			<property name="loadbalance">1</property>
			
			<!-- Separated by commas,such as: server1,server2,server1 -->
			<property name="poolNames">slave</property>
		</poolConfig>
	</dbServer>
</amoeba:dbServers>
```

**amoeba.xml**

```xml
<?xml version="1.0" encoding="gbk"?>

<!DOCTYPE amoeba:configuration SYSTEM "amoeba.dtd">
<amoeba:configuration xmlns:amoeba="http://amoeba.meidusa.com/">

	<proxy>
	
		<!-- service class must implements com.meidusa.amoeba.service.Service -->
		<service name="Amoeba for Mysql" class="com.meidusa.amoeba.mysql.server.MySQLService">
			<!-- port -->
			<property name="port">8066</property>
			
			<!-- bind ipAddress -->
			<!-- 
			<property name="ipAddress">127.0.0.1</property>
			 -->
			
			<property name="connectionFactory">
				<bean class="com.meidusa.amoeba.mysql.net.MysqlClientConnectionFactory">
					<property name="sendBufferSize">128</property>
					<property name="receiveBufferSize">64</property>
				</bean>
			</property>
			
			<property name="authenticateProvider">
				<bean class="com.meidusa.amoeba.mysql.server.MysqlClientAuthenticator">
					
					<property name="user">root</property>
					
					<property name="password">123456</property>
					
					<property name="filter">
						<bean class="com.meidusa.toolkit.net.authenticate.server.IPAccessController">
							<property name="ipFile">${amoeba.home}/conf/access_list.conf</property>
						</bean>
					</property>
				</bean>
			</property>
			
		</service>
		
		<runtime class="com.meidusa.amoeba.mysql.context.MysqlRuntimeContext">
			
			<!-- proxy server client process thread size -->
			<property name="executeThreadSize">128</property>
			
			<!-- per connection cache prepared statement size  -->
			<property name="statementCacheSize">500</property>
			
			<!-- default charset -->
			<property name="serverCharset">utf8</property>
			
			<!-- query timeout( default: 60 second , TimeUnit:second) -->
			<property name="queryTimeout">60</property>
		</runtime>
		
	</proxy>
	
	<!-- 
		Each ConnectionManager will start as thread
		manager responsible for the Connection IO read , Death Detection
	-->
	<connectionManagerList>
		<connectionManager name="defaultManager" class="com.meidusa.toolkit.net.MultiConnectionManagerWrapper">
			<property name="subManagerClassName">com.meidusa.toolkit.net.AuthingableConnectionManager</property>
		</connectionManager>
	</connectionManagerList>
	
		<!-- default using file loader -->
	<dbServerLoader class="com.meidusa.amoeba.context.DBServerConfigFileLoader">
		<property name="configFile">${amoeba.home}/conf/dbServers.xml</property>
	</dbServerLoader>
	
	<queryRouter class="com.meidusa.amoeba.mysql.parser.MysqlQueryRouter">
		<property name="ruleLoader">
			<bean class="com.meidusa.amoeba.route.TableRuleFileLoader">
				<property name="ruleFile">${amoeba.home}/conf/rule.xml</property>
				<property name="functionFile">${amoeba.home}/conf/ruleFunctionMap.xml</property>
			</bean>
		</property>
		<property name="sqlFunctionFile">${amoeba.home}/conf/functionMap.xml</property>
		<property name="LRUMapSize">1500</property>
		<property name="defaultPool">writedb</property>
		
		<property name="writePool">writedb</property>
		<property name="readPool">myslave</property>
		<property name="needParse">true</property>
	</queryRouter>
</amoeba:configuration>
```

##### 4、启动amoeba

```shell
/root/amoeba-mysql-3.0.5-RC/bin/launcher
```

#### 4、测试amoeba

```sql
--测试的sql
--在安装amoeba的服务器上登录mysql
mysql -h192.168.85.13 -uroot -p123 -P8066
--分别在master、slave、amoeba上登录mysql
use msb
select * from user;
--在amoeba上插入数据
insert into user values(2,2);
--在master和slave上分别查看表中的数据
select * from user;
--将master上的mysql服务停止，继续插入数据会发现插入不成功，但是能够查询
--将master上的msyql服务开启，停止slave上的mysql，发现插入成功，但是不能够查询
```

## Mycat

http://www.mycat.org.cn/

配置

![image-20220413135855016](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413135855016.png)

![image-20220413135905560](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413135905560.png) 

![image-20220413140052382](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413140052382.png)





## mysql性能监控



### 性能监控器01

使用show profile查询解析	工具，可以指定具体的type

使用performance schema来更加容易的监控mysql

![image-20220326200014369](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326200014369.png) 





```mysql
#设置profiles
show profiles;

#打印你最近执行的SQL语句所执行的时间
show profile

#通过query_id指定查询那条SQL语句所执行的时间
show profile query [ID]

#看耗费的系统资源
show profile cpu

#看所有
show profile all

#show profile在慢慢的被淘汰，5版本可以放心用
```

![image-20220326205324445](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326205324445.png) 

![image-20220326205352523](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326205352523.png) 



### 性能监控器02



mysql性能模块

**Performance Schema**

```mysql
show databases 列表中就有 performance schema  里面包含了87张表
```

![image-20220326215057319](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326215057319.png) 

mysql默认开启 performance schema



用于监控一个mysql server在一个较低级别的运行过程中的资源消耗、资源等待等情况。

> mysql支持的存储引擎有 InnoDb、MyISAM、Memory(不支持持久化)

![image-20220326215605623](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326215605623.png) 

performance_schema中的事件与写入二进制日志中的事件是不一样的：这个数据库中的所有表的数据是不会进行持久化的，不会往磁盘里面存储，放到内存中。当mysql服务关闭后数据自动清空，下次开始的时候会重新监控服务，重新填充数据。

**instruments生产者：用于采集mysql中各种各样的操作产生的事件信息，对应配置表中的配置项我们可以称为监控采集配置项**

**consumer消费者：对应的消费者表用于存储来自instruments采集的数据，对应配置表中的配置项我们可以称为消费存储配置项**

#### performance_schema表的分类

![image-20220326220918507](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326220918507.png) 

![image-20220326221053598](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326221053598.png) 



```sql
UPDATE setup_instruments SET ENABLED = 'YES', TIMED = 'YES' where name like 'wait%';
UPDATE setup_consumers SET ENABLED = 'YES' where name like '%wait%';
```



**具体的解释可以看MYSQL performance schema详解.md文件中的内容**

**注意：在performance_schema 库中还包含了很多其他的库和表**



### mysql性能监控03



**show processlist查看当前连接**



#### 数据库连接池

DDPC、C3P0、Druid



## 数据类型的优化



### 更小的通常更好

**应该尽量使用可以正确存储数据的最小数据类型，更小的数据类型通常更快，因为它们占用更少的磁盘、内存和CPU缓存，并且处理时需要的CPU周期更少，但是要确保没有低估要存储的值的范围，如果无法确认是哪个数据类型，就选择你任务不会超过范围的最小类型。**

- .frm

  表示当前存储所对应的表结构

- .ibd

  存储的具体的数据文件

![image-20220326225659378](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326225659378.png) 

![image-20220326230037765](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220326230037765.png) 



### 简单就好

简单数据类型的操作通常需要更少的CPU周期，例如

1. 整型比字符操作代价更低，因为字符集和校对规则是字符比较比整型比较更复杂

2. 使用mysql自建类型而不是字符串来存储日期和时间

3. 用整型存储IP地址

   案例：创建两张相同的表，改变日期的数据类型，查看SQL语句执行的速度



### 尽量避免使用null

如果查询中包含可为null的列，对mysql来说很难优化，因为可为null 的列使得索引、索引统计和值比较都更加复杂，坦白来说，通常情况下null的列改为not null 带来的性能提升比较小，所有没有必要将所有表的schema进行修改，但是应该尽量避免设计成可为null的列。



### 实际细则

#### 实际类型的优化_整型

可以使用的几种整数类型：TINYINT、SMALLINT、MEDIUMINT、INT、BIGINT分别使用8、16、24、32、64位存储空间，尽量使用满足需求的最小数据类型



#### 实际类型的优化_字符型

char、varchar、text、blob

1. 使用最小的符合需求的长度。
2. varchar(n) n小于等于255使用额外一个字节保存长度，n>255使用额外两个字节保存长度
3. varchar(5)与varchar(255)保存同样的内容，硬盘存储空间相同，但内存空间占用不同，是指定的大小。
4. varchar在mysql5.6之前变更长度，或者从255以下变更到255以上时，都会导致锁表

![image-20220327195202134](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327195202134.png) 



在操作系统中有两个基本概念

1、局部性原理;

2、磁盘预读(每次在进行数据提取的时候，虽然我只需要读取一个字节的数据，但是操作系统会把这一个字节所在块里面的4k都进来，因为有可能这4k中可能存在我下次要取的数据，所以就读出来)

![image-20220327200246819](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327200246819.png) 



#### 实际类型的优化_字符型BLOB、TEXT

mysql把每个BLOB和TEXT值当作一个独立的对象处理。

两者都是为了存储很大数据而设计的字符串类型，分别采用二进制和字符方式存储。



#### 实际类型的优化_时间戳

![image-20220327201912497](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327201912497.png) 

![image-20220327201929093](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327201929093.png) 

![image-20220327202042401](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327202042401.png) 





#### 实际类型的优化_枚举类



有时候可以使用枚举类来代替常用的字符串类型，mysql存储枚举类型会非常的紧凑，会根据列表值的数据压缩到一个或两个字节中，mysql在内部会将每个值在列表中的位置保存为整数，并且在表的.frm文件中保存"数字-字符串"映射关系的查找表

![image-20220327203638662](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327203638662.png) 

![image-20220327203705979](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327203705979.png) 



**enum的列名千万不要命名为数字**



#### 实际类型的优化_特殊类型 

一般情况下int的长度是没有用到的，但是还是需要规范。

因为用int类型进行存储的时候实际上占用的是几个几节，只要字节数没有超过最大字节数的话他就不会报错。



## 合理适用范式和反范式

![image-20220327211214238](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327211214238.png) 

![image-20220327211639148](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327211639148.png) 

**范式设计**

![image-20220327212012616](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327212012616.png) 

![image-20220327212111441](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327212111441.png) 

**列不可分、不能存在传递依赖、表里面其他列的值必须依赖于其他列的主键**



## 主键的选择

- 代理主键

  与业务无关的，无意义的数字序列

- 自然主键

  事实属性中的自然唯一标识

- 推荐使用代理主键

  ![image-20220327212818643](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327212818643.png)



## 字符集的选择

mysql如果设置成了utf-8的话，就只能存储两个字符的中文 

![image-20220327213336120](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327213336120.png)



## 存储引擎的选择

![image-20220327213738360](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327213738360.png) 





**存储引擎之间的区别**

![image-20220327213843784](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220327213843784.png)



 **存储引擎代表的意思其实就是  数据文件的一个组织形式**



## 适当的数据冗余

 ![image-20220328123542714](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220328123542714.png) 

**物化视图**

SQL语句提前执行出来的结果提前的放到一张物理表里面





## 适当拆分

当我们的表中存在类似于TEXT或者是很大的VARCHAR类型的大字段的时候，如果我们大部分访问这张表的时候都不需要这个字段，我们就该义无反顾的将其拆分到另外的独立表中，以减少常用的数据所占用的存储空间。这样子做的一个明显好处就是每个数据块中可以存储的数据条数可以大大增减，既减少物理IO次数，也能大大提高内存中的缓存命中率。

**垂直切分**

根据对应的业务来切分

**水平切分**

沙丁分片



## mysql执行计划(mysql执行计划.md)



如何判断执行的过程，如何去优化

![image-20220329132143107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220329132143107.png)

**详细查看：mysql执行计划.md**



![image-20220329133346942](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220329133346942.png) 



range是最低要满足的一个标准，表示的是范围查询，最好能达到ref

![image-20220329133619476](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220329133619476.png) 



 ![image-20220329133721427](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220329133721427.png) 

possible_keys在执行当前查询语句的时候有可能会用到的查询索引、key表示实际应用的业务索引是哪个。



key_len表示的是索引的长度，索引长度越短越好，索引越多代表的是占用的存储空间会越多，也意味着IO的次数和量会增减，在一定程度上会拖垮SQL语句执行的效率。



## mysql_通过索引进行优化

mysql底层用的是B+树

hash表-> 散列算法，hash表其实也有一些问题的，我们要想要把hash表对应的值都加载到内存中，才能加快我们对应的查询效果，他会根据我们的一个下标来直接读取数据的，所以hash表对内存是一个比较严重的消耗。查到hash表数据的时候是有一个等值判断的。

 hash是根据指定的下标来读取数据的，对内存是一个比较严重的消耗



二叉树会造成我们对应的树节点过深，越深以为着IO次数越多

![image-20220413194735085](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413194735085.png) 



AVL二插平衡树树在插入数据的时候会进行一个旋转操作，他要保持最长子树和最短子树高度之差不能超过1，数据越多就会造成1~N次的旋转，旋转非常浪费性能，**查询效率高，插入删除修改效率极低**

### 红黑树

**是AVL树的变种，牺牲了一部分查询性能来满足插入性能的提升**

最长子树只要不超过最短子树的两倍即可。

通过加入变色的行为来减少旋转的次数，任何一条单分支里面不能出现两个红色节点

![image-20220413195149328](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413195149328.png) 

![红黑树](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/%E7%BA%A2%E9%BB%91%E6%A0%91.png)

![image-20220413203838514](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413203838514.png) 



### B+树

**特点：**

1. 所有键值分布在整棵树中
2. 搜索有可能在非叶子节点结束，在关键字全集内做一次查找，性能逼近二分查找
3. 每个节点最多拥有m个子树
4. 根节点至少有2个子树
5. 分支节点至少拥有m/2颗子树(除根节点和叶子节点外都是分支节点)
6. 所有叶子节点都在同一层，每一个节点最多可以有m-1个key，并且以升序排列

![image-20220413204959760](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413204959760.png) 

只允许我们的叶子节点中存储数据，而非叶子节点不存储数据只存key

**注意：在B+Tree上有两个指针，一个指向根节点，另一个指向关键字最小的叶子节点，而且所有叶子节点(即数据节点)之间是一种链式环结构。因此可以对B+Tree进行两种查找运算：一种是对于主键的范围查找和分页查找，另一种是从根节点开始，进行随机查找**



为什么不使用B*树？B *树在非叶子节点中也会建立一个连接，但是非叶子节点上面是没有数据在的，所以没有必要使用B * 数



**InnoDB数据和索引是放一起的，MyISAM数据和索引是分开来存放的**

![image-20220413211725575](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413211725575.png) 

**当使用InnoDB这种存储引擎的时候，他所对应的一个叶子节点中存储的就是实际的一整行数据。**





![image-20220413211828789](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220413211828789.png)

****

**如果使用MyISAM的时候，因为他们位于两个不同的文件，所以在查找 数据的时候，最后一个叶子节点放的不是一个具体的数据，而是实际的这条数据所在的文件的文件地址。需要先去到当前数据的文件地址，在根据当前地址去对应的myd文件中找到所对应的数据行，才能把数据读出来。(多一次IO)**



8以后就没有查询缓存了



## 索引基础

索引的用处：

1. 快速查找匹配的WHERE子句的行
2. 从consideration中消除行，如果可以在多个索引之间进行选择，mysql通常会使用找到最少行的索引
3. 如果表具有多列索引，则优化器可以使用索引的任何最左前缀来找行
4. 当有表连接的时候，从其他表检索行数据
5. 查找特定索引列的min或max值
6. 如果排序或分组时可在可用索引的最左前缀上完成的，则对表进行排序和分组
7. 在某些情况下，可以优化查询以检索值而无需查询数据行



索引的基本分类：**主键索引、唯一索引、全文索引、普通索引、组合索引**

**数据库建索引的时候并不是给主键建的，而是给唯一键建的**



普通索引列叶子节点会存储数据和主键

因为使用索引列覆盖要查询的字段，如果查询条件使用的是普通索引(或是联合索引的最左原则字段)，查询结果是索引的列或是主键，不用回表操作，直接返回结果，减少IO磁盘读写读取正行数据



如何建组合索引

![image-20220414132817762](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414132817762.png) 

使用
name age    age建立组合索引，因为name在磁盘中占用的磁盘空间更大，单独为name创建一个索引会占用更多的磁盘空间。



**回表、覆盖索引、最左匹配、索引下推**

```sql
索引下推，谓词下推
select t1.name, t2.name from t1 join t2 on t1.id = t2.id
```

并不是只要写了SQL语句就用了索引，而是需要用上索引列之后才有可能使用索引

```
#索引下推
https://baijiahao.baidu.com/s?id=1716515482593299829&wfr=spider&for=pc
```



## 索引匹配方式

![image-20220414201348991](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414201348991.png) 

​	![image-20220414201505061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414201505061.png) 

 **最左前缀匹配**![image-20220414203849610](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414203849610.png) 

![image-20220414203854877](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414203854877.png) 



### 哈希索引

![image-20220414205045622](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414205045622.png) 



**哈希冲突：如何避免？**

![image-20220414205651071](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414205651071.png) 

CRC32做哈希

 ![image-20220414210812026](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414210812026.png) 



### 聚簇索引与非聚簇索引

**聚簇索引：不是单独的索引类型，而是一种数据存储的方式，指的是数据行相邻的键值紧凑的存储在一起**

![image-20220414211323663](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414211323663.png) 

**非聚簇索引：数据文件跟索引文件分开存放**



### 覆盖索引

![image-20220414212115555](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220414212115555.png) 



**回表：一般情况下，当我们创建索引之后，字段都是跟主键相关的，但是如果给普通的列创建索引之后，他所对应的叶子节点里面存储的数据放的是主键值，我们需要根据这个主键值回到我们的主键树里面再去查找整个行的记录。**

**覆盖索引：我们在查询的数据是我们对应的一个索引列的话，这个地方就会出现我们的覆盖索引**

![image-20220415092716221](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220415092716221.png) 



![image-20220415131207083](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220415131207083.png) 



![image-20220415131633552](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220415131633552.png) 



```sql
-- 计算完成之后可以创建前缀索引
alter talbe citydemo add key(city(7))
-- 注意：前缀索引是第一种能使索引更小更快的有效方法，但是也包含缺点：mysql无法使用前缀索引做order by 和group by
```

![image-20220415133729825](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220415133729825.png) 

hyperloglog



![image-20220415134901630](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220415134901630.png) 

![image-20220416102627592](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416102627592.png) 

Using index condition 使用了索引的某些相关查询



![image-20220416103210645](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416103210645.png) 



![image-20220416103221397](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416103221397.png)

 Using filesort：意味着并没有用到索引进行排序，要使用额外的文件进行排序操作。因为如果第一个值是一个范围查找的话，最左前缀匹配的时候，不管后面的条件是否是我们需要的他都不会进行一个查找，都失效了

![image-20220416103529094](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416103529094.png) 

当我们创建组合索引的时候，他默认情况下是一个升序排序，所以后面在进行排序的时候这些列的排序方式都必须跟索引是一模一样的，如果跟索引不一样就不会用这个条件了。

![image-20220416103725037](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416103725037.png) 

组合索引中group by  一个desc 一个asc就没办法进行排序

![image-20220416103843746](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416103843746.png) 



![image-20220416104343241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416104343241.png) 

#### 总结：

**我们在使用排序的时候，如果where条件和order by 里面的一些列如果能够组成一个最左前缀匹配的话，那么他就会使用索引排序，但是如果第一列是一个范围的话，那么就没办法用了。  如果order by里面的顺序与我们所索引中对应的升序降序不一样的话，也没有办法进行索引排序的使用**



### union all,in,or索引

他们都能使用索引，但是推荐使用in

![image-20220416105543810](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416105543810.png) 

![image-20220416105729223](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416105729223.png) 



使用exists的时候，exists相当于是一个双重的for循环



### 范围列可以用到索引

范围类的条件是 >、<、>=、<=、between

范围列可以用到索引，但是范围列后面的列无法用到索引，索引最多用于一个范围列

**为什么前面用范围查询后面用=后面的索引就失效了呢？**

**最左匹配原则，每次在查找的时候都是先查找左边的索引，当最左边的查找完之后才会查找后面的索引，他们是绑定到一起的，如果最左边的索引已经有了范围了，那么就意味着取的数据块就不是同一个了，如果不是同一个数据块就意味着第一个索引的一个数据对应着n多个后面的索引，就没办法再按照后面的索引字段检索了。**



index name 和 index age 有什么区别吗？

他们两个的存储空间有区别，在于一个name可能有n多个age来匹配，一个age也有可能有n多个name来匹配， age比name占用的存储空间会更小，因为age的重复值比name来的小，但是几乎可以忽略掉。



### 强制类型转换会全表扫描

![image-20220416115135482](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416115135482.png) 

![image-20220416115457850](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416115457850.png) 



### 数据区分度不高字段不宜建立索引

**更新会变更B+树，更新频繁的字段建索引会大大降低数据库性能，不利于维护**

**类似于性别这类区分不大的属性，建立索引是没有意义的，不能有效的过滤数据**

**一般区分度在80%以上的时候就可以建立索引，区分度可以使用count(distinct(列名))/count(*)来计算**



### 列的索引不许为null三张表join

创建索引的列不允许为NULL，可能会得到不符合预期的结果。



在使用inner join的时候最好把表连接列设置成索引

![join的多种方式 (3).jpg](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/join%E7%9A%84%E5%A4%9A%E7%A7%8D%E6%96%B9%E5%BC%8F(3).jpg)

A join B并不会一定是先查A再关联B，mysql会有他自己的一套优化。我们可以通过constraint来强制他必须先A再B。(最好是小表匹配join大表)

![image-20220416151104698](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220416151104698.png) 



### 答疑

当使用内连接的时候，两种方式一样

当使用左外连接的时候，会把左表的数据全部查出，

当使用右外连接的时候，会把右表的数据全部查出，

and是在表连接前过滤A表或B表里面哪些记录符合连接条件，同时会兼顾left join 还是right join。即假如是左连接的话，如果左边表的某条记录不符合连接条件，那么它不进行连接，但是仍然留在结果集中(此时右边部分的链接结果为NULL)，on条件是在生成临时表时使用的条件，他不管on中的条件是否为真，都会返回左边表中的记录

**在进行左外连接的时候，理论上来说会把左表的全部数据，都匹配出来，不管右边表是否有与之对应的数据，但是后面 又加了一个and条件，相当于是在这个结果集的基础之上看下这个and条件是否满足，如果满足的话就显示出来，如果不满足就显示成NULL**

```sql
select * from emp m inner join dept d on 1!=1 
结果:NULL
```



### limit能够提高效率

**limit 不能说是用来分页的，他是用来限制输出的，分页只是限制输出里面的最基本的应用**

能使用limit的时候尽量使用limit。

limit在过滤的时候相当于是有一个指针，一行一行的遍历，他必须要遍历前面的指定行数之后，才能取到对应的数据，所以如果是20000,5的话就会非常非常慢



### 单表索引建议控制在5个以内

并不是建立越多的索引查询速率就越快，因为我们建的索引越多，就意味着树越大，IO量就会越大



### 单索引字段数不允许超过5个

建立组合索引的时候里面的字段最好不要太多，最左匹配原则。



**创建索引的时候应该避免以下错误概念：1、索引越多越好；2、过早优化，在不了解系统的情况下进行优化**



### 索引监控

![image-20220417211539999](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220417211539999.png) 

![image-20220417211622715](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220417211622715.png) 	



### 查询慢的原因

如果表中的数据不是很多的话其实差别不大

网络、CPU、IO、上下文切换、系统调用、生成统计信息、锁等待时间



InnoDB实际上锁的是索引，所以他既支持行锁，也支持表锁。



**自增锁、间隙锁**

![image-20220418212904605](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220418212904605.png) 

![image-20220418213153549](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220418213153549.png) 



![image-20220419132039512](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419132039512.png) 



![image-20220419132108774](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419132108774.png) 

![image-20220419132419040](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419132419040.png) 

查询缓存在8.0的时候已经淘汰掉了

![image-20220419133448628](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419133448628.png) 



### 优化器策略

![image-20220419134104294](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419134104294.png)

**优化器最有效的查询方式是：CBO、RBO**

CBO:基于成本的优化

RBO:基于规则的优化



mysql使用的是基于成本的优化器，在优化的时候会尝试预测一个查询使用某种查询计划时候的成本，并选择其中成本最小的一个。



```sql
#最后一次所耗费时间的成本
show status like 'last_query_cost';
```



![image-20220419205549010](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419205549010.png) 



![image-20220419205812802](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419205812802.png) 

执行存储过程或者用户自定义函数的成本



![image-20220419210309164](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419210309164.png) 



![image-20220419210658539](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419210658539.png) 

索引和列是否可以为空通常可以帮助mysql优化这类表达式：例如，要找到某一列的最小值，只需要查询索引的最左端的记录即可，不需要全文扫描比较。

system>const>ref>range>index>all



===================LYX===================================没太懂

**索引覆盖扫描：每次在做一个查询的时候，必须要将所有查询的列，都包含在我们的索引字段里面**



**等值传播：如果两个列的值通过等式关联，那么mysql能够把其中一个列的where条件传递到另一个上。**

![image-20220419212524403](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419212524403.png) 

 	

优化该SQL：能不能只扫描几行的rows 

![image-20220419231004513](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419231004513.png) 



### 关联与排序优化

![image-20220419232715529](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220419232715529.png) 



**我们在做关联查询的时候要把小表放到我们的左边，这样的话能够增加我们的查询效率？这个其实是不对的**

MySQL优化器优化过后的效果，可能比我们强制执行的效果会更好 

![image-20220425133648331](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425133648331.png) 

![image-20220425133736662](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425133736662.png) ![image-20220425134133487](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425134133487.png) 

**单次查询：先读取查询所需要的所有列，然后再根据给定列进行排序，最后直接返回排序结果，此方式只需要一次顺序IO读取所有的数据，而无需任何的随机IO，问题在于查询列忒儿多的时候，会占用大量的存储空间，无法存储大量的数据。**



**当需要排序的列的总大小加上order by的列大小超过max_length_for_sort_data 定义的字节，MySQL会选择双次排序，反之使用单次排序，当然，用户可以设置此参数的值来选择排序的方式。**



### 优化特定类型的查询

#### 优化count查询

![image-20220425210639263](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425210639263.png) 

MySQL中是没有区别的，总有人认为myisam的count函数比较快，这是有前提条件的，只有没有任何where条件的count(*)才是比较快的。

使用近似值

![image-20220425210958861](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425210958861.png) 



![image-20220425211039894](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220425211039894.png) 

#### 优化关联查询

确保on或者using子句中的列上有索引，在创建索引的时候就要考虑到关联的顺序



确保任何的group by和order by中的表达式只涉及到一个表中的列，这样MySQL才有可能使用索引来优化这个过程。

表达式中能够使用索引的话尽量都使用索引，效率会比较高。



#### 优化子查询

**子查询的优化最重要的优化建议是尽可能的使用关联查询来代替**

尽量不要使用子查询，因为子查询会生成临时表，每次select出来的数据都会生成一个临时表，临时表其实也是一个io。

在某些情况下，我们在进行数据的查询是，如果可以用子查询或者可以用join 关联查询的时候可以做一个衡量。



#### 优化group by 和distinct

在很多场景下，mysql使用相同的方法来优化group by 和distinct的查询，使用索引是最有效的方式，但是有很多的情况下无法使用索引，可以使用临时表或者文件排序来分组。

**如果对关联查询做分组，并且是按照查找表中的某个列进行分组，那么可以采用查找表的标识列分组的效率比其他列更高。**

标识列



![image-20220506221743329](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220506221743329.png) 



![image-20220506222123582](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220506222123582.png) 



#### 推荐使用用户自定义变量

**@@**：系统标量

**@**：

![image-20220506222240275](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220506222240275.png) 





## 执行过程的优化_其他优化



### 优化limit分页

![image-20220506223216794](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220506223216794.png) 

示例：

![image-20220506222954791](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220506222954791.png) 

**优化此类查询的最简单的办法就是尽可能的使用覆盖索引，而不是查询所有列**

通过执行计划查询

扫描行数为1000                                                                         扫描行数为110

![image-20220507090400501](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507090400501.png) ![image-20220507090410709](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507090410709.png) 



![image-20220507090352825](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507090352825.png) 



### 优化union查询

除非确实需要服务器消除重复的行，否则一定要使用union all， 因此没有all关键字， mysql会在查询的时候给临时表加上distinct关键字，这个操作的代价很高 



#### ====行转列====

![image-20220507090941858](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507090941858.png) 



## 推荐使用自定义变量





```mysql
##基础使用
set @one:=1
##使用自定义变量加SQL
mysql> set @min_actor:=(select min(actor_id) from actor);
Query OK, 0 rows affected (0.24 sec)

mysql> select @min_actor;
+------------+
| @min_actor |
+------------+
|          1 |
+------------+
1 row in set (0.00 sec)
##表达式的计算
mysql> set @last_week := current_date-interval 1 week;
Query OK, 0 rows affected (0.05 sec)

mysql> select @last_week;
+------------+
| @last_week |
+------------+
| 2022-04-30 |
+------------+
1 row in set (0.00 sec)
```

**自定义变量的限制**

1. 无法使用查询缓存  自定义变量只在当前会话有效
2. 不能在使用常量或者标识符的地方使用自定义变量，例如表名、列名或者limit子句  
3. 用户自定义变量的生命周期是在一个连接中有效，所以不能用他们来做连接间的通信  断了数据就没了
4. 不能显示地声明自定义变量的类型 
5. mysql优化器在某些场景下可能会将这些变量优化掉，这可能导致代码不按预想的方式运行   一般情况下是没有问题的
6. 赋值符号:=的优先级非常低，所以在使用赋值表达式的时候应该明确的使用括号 
7. 使用未定义变量不会产生任何语法错误。

**一般情况下使用自定义变量我们需要给定一个默认值，在这个基础之上再给出其他操作**



![image-20220507131344862](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507131344862.png) 

![image-20220507131220622](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507131220622.png) 



![image-20220507131955987](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507131955987.png) 





![image-20220507132225519](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507132225519.png) 

![image-20220507132550834](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507132550834.png)

之前如果没有自定义变量的话 order by确实是最后执行的，但实际上第二条SQL场景的时候，order by是优先执行的，所以要慎用

**解决这个问题的关键在于让变量的赋值和取值发生在执行查询的同一阶段**





## 分区表的应用场景

**分而治之**

**分区适用于表的所有数据和索引，不能仅对数据而不是索引进行分区，反之亦然，也不能仅对表的一部分进行分区**

**垂直切分、水平切分**



分区表的应用场景

1. 表非常大以至于无法全部放在内存中，或者只在表的最后部分有热点数据，其他均是历史数据。(在进行数据的读操作的时候，需要把数据从磁盘读取到内存才能处理，直接在磁盘的话是没办法处理数据的)

2. 分区表的数据更容易维护

   1. 批量删除大量数据可以使用清除整个分区的方式
   2. 对一个独立分区进行优化、检查、修复等操作

3. 分区表的数据可以分布在不同的物理设备上，从而高效地利用多个硬件设备

4. 可以使用分区表来避免某些特殊的瓶颈

   1. innodb的单个索引的互斥访问  ====
   2. ext3文件系统inode锁竞争 ====   

   ![image-20220507140538253](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507140538253.png) 

5. 可以备份和恢复一个独立的分区



## 分区表的原理与类型

​		分区表由多个相关的底层表实现，这个底层表也是由句柄对象标识，我们可以直接访问各个分区。存储引擎管理分区的各个底层表和管理普通表一样（**所有的底层表都必须使用相同的存储引擎**），分区表的索引只是在各个底层表上各自加上一个完全相同的索引。从存储引擎的角度来看，底层表和普通表没有任何不同，存储引擎也无须知道这是一个普通表还是一个分区表的一部分。

​		分区表的操作按照以下的操作逻辑进行：

​		**select查询**

​		当查询一个分区表的时候，分区层先打开并锁住所有的底层表，优化器先判断是否可以过滤部分分区，然后再调用对应的存储引擎接口访问各个分区的数据

​		**insert操作**

​		当写入一条记录的时候，分区层先打开并锁住所有的底层表，然后确定哪个分区接受这条记录，再将记录写入对应底层表

​		**delete操作**

​		当删除一条记录时，分区层先打开并锁住所有的底层表，然后确定数据对应的分区，最后对相应底层表进行删除操作

​		**update操作**

​		当更新一条记录时，分区层先打开并锁住所有的底层表，mysql先确定需要更新的记录再哪个分区，然后取出数据并更新，再判断更新后的数据应该再哪个分区，最后对底层表进行写入操作，并对源数据所在的底层表进行删除操作。**在进行更新的时候最好不要去更新我们的分区列**

​		有些操作时支持过滤的，例如，当删除一条记录时，MySQL需要先找到这条记录，如果where条件恰好和分区表达式匹配，就可以将所有不包含这条记录的分区都过滤掉，这对update同样有效。如果是insert操作，则本身就是只命中一个分区，其他分区都会被过滤掉。mysql先确定这条记录属于哪个分区，再将记录写入对应得曾分区表，无须对任何其他分区进行操作

​		**虽然每个操作都会“先打开并锁住所有的底层表”，但这并不是说分区表在处理过程中是锁住全表的，如果存储引擎能够自己实现行级锁，例如innodb，则会在分区层释放对应表锁。**



其实分区表跟普通的表是一样的，只是在执行操作之前加入了一个判断，判断数据在哪个分区，做一个判断不会有多长时间的，但是IO量是没有办法突破的瓶颈



![image-20220507211406453](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507211406453.png) 

4个分区

![image-20220507213023379](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507213023379.png) 

子分区

![image-20220507213506230](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507213506230.png) 

​	分区会按照我们指定的规则把数据插入到对应的数据文件中，而且那些数据文件不一定是在同一台机器上存储的，可以分配在不同的服务器上。

## 如何使用分区表

**全量扫描数据，不要任何索引**

![image-20220507214529747](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507214529747.png) 

**索引数据，并分离热点**

![image-20220507214720783](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507214720783.png) 

**并不是所有人都能查到冷数据的，他需要一个权限的分配**

如果需要从非常大的表中查询出某一段时间的记录，而这张表中包含很多年的你是数据，数据是按照时间排序的，此时应该如何查询数据呢？

因为数据量巨大，肯定不能再每次查询的时候都扫描全表。考虑到索引在空间和维护上的消耗，也不希望使用索引，即使使用索引，会发现他会产生大量的碎片，还会产生大量的随机IO，但是当数据量超大的时候，索引也就无法起作用，此时可以考虑使用分区来解决。



## 在使用分区表的时候需要注意的问题

![image-20220507214933297](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220507214933297.png) 



 **最好按照特点来进行分区**



==============分区没太大听懂=================



## 服务器参数设置 ========还没看

​                    