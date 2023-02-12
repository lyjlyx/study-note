

![image-20211225160410348](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20211225160410348.png)





## 表的完整性约束



### 非外键约束

![image-20211225162037407](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20211225162037407.png)

自增约束只能添加到列（列级约束）后面   不是表约束



![image-20220329212223955](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220329212223955.png) 



```sql
-- 非外键约束
create table t_student(
	sno int (6) auto_increment,
	sname varchar(5) not null,
	sex char(1) default '男',
	age int(3),
	enterdate date,
	classname varchar(10),
	email varchar(15),
	constraint pk_stu primary key (sno), -- pk_stu是主键约束的名字(auto_increment自增约束只能添加到列后面去)
	constraint ck_stu_sex check (sex = '男' || sex = '女'), -- 检查约束
	constraint ck_stu_age check (age >=18 and age <= 50),
	constraint eq_stu_email unique (email) -- 唯一约束
-- 	非空约束和默认约束只能添加到列后面

);

-- 添加数据
insert into t_student value(1,'张三','男',21,'2023-9-1','java01','lyx@qq.com');
insert into t_student value(2,'李四','男',89,'2023-9-1','java01','lyx11@qq.com');
-- 查询
select * from t_student
```

创建表之后添加约束

```sql
-- 非外键约束
create table t_student(
	sno int (6), -- 自增必须要给主键
	sname varchar(5) not null,
	sex char(1) default '男',
	age int(3),
	enterdate date,
	classname varchar(10),
	email varchar(15)
);
-- > 1075 - Incorrect table definition; there can be only one auto column and it must be defined as a key
-- 把自增去掉


-- 在创建表之后添加约束
-- 添加主键
alter table t_student add constraint pk_stu primary key (sno);
-- 修改自增条件
alter table t_student modify sno int (6) auto_increment; 
-- 添加检查约束
alter table t_student add constraint ck_stu_sex check (sex = '男' || sex = '女');
alter table t_student add constraint ck_stu_age check (age >= 18 && age <=30);
alter table t_student add constraint uq_stu_email unique (email);

desc t_student;
```



### 外键约束

![image-20220330130456429](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330130456429.png) 

 

```sql
-- 外键约束

-- 先创建父表
create table t_class (
	cno int(4) primary key auto_increment,
	cname varchar(10) not null,
	root char(4)
)

-- 添加班级数据
insert into t_class values(1, 'java001', 'r803'), (2, 'java002', 'r416'), (3,'大数据002','r003');

-- 字表
create table t_t_student (
	sno int (6) , -- 自增必须要给主键
	sname varchar(5) not null,
	classno int(4) 	-- 取值参考的是t_class表中的cno字段，不要求字段名完全相同，但是类型长度 尽量要相同
)
alter table t_t_student add constraint pk_sno primary key (sno)
alter table t_t_student modify sno int(6) auto_increment

-- 添加学生信息
insert into t_t_student values(null, '张三', 1), (null, '李四', 1), (null,'王五',2)

-- 查看学生表
select * from t_t_student


-- 外键约束需要通过语法添加进去，两张表还没有建立上关联
-- 添加外键约束

-- 外键约束只有表级约束，没有列级约束
drop table t_t_student

create table t_t_student (
	sno int (6) , -- 自增必须要给主键
	sname varchar(5) not null,
	classno int(4), 	-- 取值参考的是t_class表中的cno字段，不要求字段名完全相同，但是类型长度 尽量要相同
	constraint fk_stu_classno foreign key(classno) references t_class(cno)
)

alter table t_t_student add constraint pk_sno primary key (sno)
alter table t_t_student modify sno int(6) auto_increment

-- 在创建表以后添加外键约束
alter table t_t_student add constraint fk_stu_classno foreign key(classno) references t_class(cno)


```

### 外键策略

外键策略需要先删除从表，再删除主表

策略1：no action 不允许操作

通过操作SQL来完成，需要删除的数据所关联的那个值可以先给他置空或者。

策略2：级联操作，操作主表的时候影响从表的外键信息。

先删除之前的外键约束，后重新添加

```sql
alter table t_t_student drop foreign key fk_stu_classno
```

重新添加外键约束

```sql
alter table t_t_student add constraint fk_stu_classno foreign key(classno) references t_class(cno) on update cascade on delete cascade;
```

策略3：置空操作

```sql
alter table t_t_student add constraint fk_stu_classno foreign key(classno) references t_class(cno) on update set null cascade on delete set null cascade;
```

策略2的级联操作和策略3的删除操作可以混合使用





### DDL和DML的补充

![image-20220330134619267](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330134619267.png) 

快速添加

```sql
-- 只表结构相同，但是不添加数据
create table t_student_3
as 
-- 1=2条件永远不满足所以数据不会添加进去
select * from t_student where 1=2
```

![image-20220330134813678](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330134813678.png) 



```sql
-- 清空数据
truncate table t_student
```

![image-20220330134944543](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330134944543.png) 



## DQL



### 最简单的SQL

准备四张表

dept(部门表)，emp(员工表),salgrade(薪资表),bonus(奖金表)

```sql
/*
Navicat MySQL Data Transfer

Source Server         : mybatis
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : demp

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2020-02-11 20:05:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `DEPTNO` int(4) NOT NULL,
  `DNAME` varchar(14) DEFAULT NULL,
  `LOC` varchar(13) DEFAULT NULL,
  PRIMARY KEY (`DEPTNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES ('10', 'ACCOUNTING', 'NEW YORK');
INSERT INTO `dept` VALUES ('20', 'RESEARCH', 'DALLAS');
INSERT INTO `dept` VALUES ('30', 'SALES', 'CHICAGO');
INSERT INTO `dept` VALUES ('40', 'OPERATIONS', 'BOSTON');

-- ----------------------------
-- Table structure for emp
-- ----------------------------
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `EMPNO` int(4) NOT NULL,
  `ENAME` varchar(10) DEFAULT NULL,
  `JOB` varchar(9) DEFAULT NULL,
  `MGR` int(4) DEFAULT NULL,
  `HIREDATE` date DEFAULT NULL,
  `SAL` double(7,2) DEFAULT NULL,
  `COMM` double(7,2) DEFAULT NULL,
  `DEPTNO` int(4) DEFAULT NULL,
  PRIMARY KEY (`EMPNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of emp
-- ----------------------------
INSERT INTO `emp` VALUES ('7369', 'SMITH', 'CLERK', '7902', '1980-12-17', '800.00', null, '20');
INSERT INTO `emp` VALUES ('7499', 'ALLEN', 'SALESMAN', '7698', '1981-02-20', '1600.00', '300.00', '30');
INSERT INTO `emp` VALUES ('7521', 'WARD', 'SALESMAN', '7698', '1981-02-22', '1250.00', '500.00', '30');
INSERT INTO `emp` VALUES ('7566', 'JONES', 'MANAGER', '7839', '1981-02-02', '2975.00', null, '20');
INSERT INTO `emp` VALUES ('7654', 'MARTIN', 'SALESMAN', '7698', '1981-09-28', '1250.00', '1400.00', '30');
INSERT INTO `emp` VALUES ('7698', 'BLAKE', 'MANAGER', '7839', '1981-01-05', '2850.00', null, '30');
INSERT INTO `emp` VALUES ('7782', 'CLARK', 'MANAGER', '7839', '1981-09-06', '2450.00', null, '10');
INSERT INTO `emp` VALUES ('7839', 'KING', 'PRESIDENT', null, '1981-11-17', '5000.00', null, '10');
INSERT INTO `emp` VALUES ('7844', 'TURNER', 'SALESMAN', '7698', '1981-09-08', '1500.00', '0.00', '30');
INSERT INTO `emp` VALUES ('7900', 'JAMES', 'CLERK', '7698', '1981-12-03', '950.00', null, '30');
INSERT INTO `emp` VALUES ('7902', 'FORD', 'ANALYST', '7566', '1981-12-03', '3000.00', null, '20');
INSERT INTO `emp` VALUES ('7934', 'MILLER', 'CLERK', '7782', '1982-01-23', '1300.00', null, '10');

-- ----------------------------
-- Table structure for salgrade
-- ----------------------------
DROP TABLE IF EXISTS `salgrade`;
CREATE TABLE `salgrade` (
  `GRADE` int(11) NOT NULL,
  `LOSAL` double DEFAULT NULL,
  `HISAL` double DEFAULT NULL,
  PRIMARY KEY (`GRADE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of salgrade
-- ----------------------------
INSERT INTO `salgrade` VALUES ('1', '700', '1200');
INSERT INTO `salgrade` VALUES ('2', '1201', '1400');
INSERT INTO `salgrade` VALUES ('3', '1401', '2000');
INSERT INTO `salgrade` VALUES ('4', '2001', '3000');
INSERT INTO `salgrade` VALUES ('5', '3001', '9999');

```



![image-20220330204415274](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330204415274.png)



```sql
select * from salgrade

select * from 

select empno, ename, sal, sal+1000 as "涨薪后", deptno from emp where sal < 2500

select empno, ename, sal, sal+comm, deptno from emp 

-- 去重
select DISTINCT job from emp;

select distinct job, deptno from emp; -- 对后面的所有组合列去重，而不是单独的某一列

-- 排序
select * from emp order by sal asc; -- 默认情况下按照升序排的
select * from emp order by sal desc;
select * from emp order by sal asc, deptno desc; -- 在工资升序的情况下，deptno按照降序排列

```

### where子句

**where加关系运算符**

![image-20220330205054149](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330205054149.png) 

```sql
-- 加binary区分大小写
select * from emp where binary job = 'clierk' 
```



```sql
-- where 子句逻辑运算符 and
select * from emp where sal > 1500 and sal < 3000

select * from emp where sal > 1500 && sal < 3000

select * from emp where sal > 1500 and sal < 3000 order by deptno
```

![image-20220330205626100](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330205626100.png) 

```sql
-- 小括号的使用 ：因为不同的运算符优先级不同，加括号是为了可读性
select * from emp where job = 'SALESMAN' or job = 'CLERK' and sal >= 1500; -- 先and 再or
select * from emp where job = 'SALESMAN' or (job = 'CLERK' and sal >= 1500);
select * from emp where (job = 'SALESMAN' or job = 'CLERK') and (sal >= 1500 or sal <=2000);
```



### 函数的分类

```sql
-- 函数举例
select empno, ename, lower(ename) as '小写', upper(ename) as '大写' from emp

-- 函数的作用是为了提高select的查询能力
-- 注意：函数没有改变自身的数据，而是在真实的数据上面进行加工处理，展示新的结果而已
select max(sal),min(sal),count(sal),sum(sal),avg(sal) from emp
```

![image-20220330211023819](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330211023819.png) 

**除了多行函数(max,min,count,sum,avg)其他的都是单行函数**



### 单行函数(1)

![image-20220330212817744](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220330212817744.png) 

```sql
-- 字符串函数
select ename, length(ename), substring(ename, 2, 3) from emp -- 字符串长度、从1开始下标为2截取3个字符
-- 数值函数
select abs(-5),ceil(5.3),floor(5.9),round(3.14) from dual; -- dual 伪表
select abs(-5),ceil(5.3),floor(5.9),round(3.14); -- 如果没有where条件的话 from dual 可以省略不写
select ceil(sal) from emp;

-- 日期与时间函数
select * from emp;
select curdate(), curtime(); -- 年月日、时分秒

select now(), sysdate(), sleep(3), now(), sysdate()   -- sql执行的时间、系统的时间
-- now()可以表示年月日时分秒，但是插入数据的时候还是要参照表的结构

```

### 单行函数(2)

```sql
-- 流程函数
-- if 相关
select empno, ename, sal, if (sal >= 2500, '高薪', '低薪') as 薪资水平 from emp

select empno, ename, sal, comm, sal+ifnull(comm,0) from emp -- 如果comm是null，那么取值为0

select nullif(1, 1), nullif(1,2) from dual -- 如果value1等于value2，则返回null，否则返回value1

-- case相关
select empno, ename, job,
case job
when 'CLERK' then '店员'
when 'SALESMAN' then '销售'
when 'MANAGER' then '经理'
else '其他'
end as '岗位', sal
from emp

-- case 区间判断
select empno,ename,sal,
case
when sal<=1000 then 'A'
when sal <=2000 then 'B'
when sal <=3000 then 'C'
else 'D'
end as '工资等级' from emp

-- 其他函数
select database(),user(),version() from dual
```



### 多行函数

```sql
-- 多行函数
-- max、min、count针对所有类型  sum、avg只针对数值类型有效

-- count --计数
-- 统计表的记录数 方式1
select * from emp;

select count(*) from emp

-- 统计表的记录数 方式2
select 1 from dual;
select 1 from emp; -- 有多少记录展示多少个1
```

### 分组group by

```sql
-- 统计各个部门的平均工资
select deptno, avg(sal) from emp -- 字段和多行函数是不能同时使用的
select deptno, avg(sal) from emp group by deptno -- 字段和多行函数是不能同时使用的，除非这个字段属于分组
select deptno, avg(sal) from emp group by deptno order by deptno desc

-- 统计各个岗位的平均工资
select job, avg(sal) from emp group by job

select job, lower(job), avg(sal) from emp group by job

```



### having分组后排序

```sql
-- having
-- 统计各个部门的平均工资，只显示平均工资2000以上的   分组以后进行二次筛选
select deptno, avg(sal) as avgsal from emp group by deptno having avgsal> 2000 order by avgsal desc 

-- 统计各个岗位的平均岗位工资，除了MANAGER
-- 方法1
select job, avg(sal) from emp group by job having job != 'MANAGER' 
-- 方法2
select job, avg(sal) from emp where job != 'MANAGER' group by job  

-- where 是在分组前进行过滤的，having 是在分组后过滤的
```



### 单表查询总结



```sql
-- 单表查询总结
-- where > group by > having > order by
-- 注意顺序固定，不可以改变顺序
-- select 语句的执行顺序
-- from ->  on -> join -> where -> group by -> having -> select -> DISTINCT -> order by -> LIMIT

-- 单表查询的联系
-- 列出平均工资最小值 小于2000 的职位
select job, min(sal) minsal from emp group by job having minsal < 2000;
-- 列出平均工资大于1200的部门和工作搭配组合
select deptno, job,avg(sal) from emp group by deptno, job having avg(sal) >1200 order by deptno 
-- 统计人数小于4的部门的平均工资
select count(1) count, avg(sal) , deptno from emp group by deptno having count < 4 order by deptno 

-- 统计各部门的最高工资，排除最高工资小于3000的部门
select  deptno, max(sal) from emp group by deptno having  max(sal) > 3000

```



### 多表查询

#### 交叉连接、自然连接、内连接查询

```sql
-- 多表查询
-- 查询员工的编号、姓名、部门编号
select empno, ename, deptno from emp; -- 12条
select * from dept; -- 4

-- 查询员工的编号、姓名、部门编号、部门名称：dept表中有部门名称 
-- 交叉连接 没有意义 mysql中cross可以省略
select * from emp 
CROSS join dept -- 笛卡尔乘积

-- 自然连接natural join
-- 优点：会自动匹配同名列,同名列只展示一次就可以了
select * from emp natural join dept

-- 缺点：查询字段的时候，没有指定字段所属的数据库表，效率低
-- 解决：指定表名
select emp.empno, emp.ename, emp.sal, dept.dname, dept.loc from emp
natural join dept

-- 表名太长可以取别名
-- 自然连接 natural join缺点：自动匹配表中的所有同名列，但是有时候我们希望只匹配部分同名列
-- 解决： using子句

select * from 
emp e
inner join -- inner可以不写
dept d  
using(deptno) -- 这里不能写natural join了，这里是内连接


-- using关联的字段必须是两张表都有的
-- 解决内连接 on 子句
select *
from emp e
inner join dept d on(d.deptno = e.deptno)

-- 多表连接查询的类型 1、交叉连接 cross join 2、自然连接 natural join 3、内连接 using子句 4、内连接 on子句
-- on用得最多
select *
from emp e
inner join dept d on(d.deptno = e.deptno)
where sal > 3500

-- 条件:
-- 筛选条件 where having
-- 连接条件 on using natural 
-- SQL99语法：筛选条件和链接条件是分开的。

```

#### 外连接

```sql
-- inner join -on 子句 显示的是所有匹配的信息
select * from emp e
inner join dept d
on d.deptno = e.deptno;

select * from dept
-- 问题:
-- deptno 为40的部门没有匹配的员工所以不显示
-- 如果某个员工没有部门，那么就不会显示在查询结果中

-- 外连接：除了显示匹配的之外，还可以显示部门或者不匹配的数据
-- 左外连接：left outer join -- 左边的那个表的信息，即使不匹配也可以查看出效果
select * from emp e
 left outer join dept d
on d.deptno = e.deptno;

-- 右外连接：right outer join -- 右边的那个表的信息，即使不匹配也可以查看出效果
select * from emp e
 right outer join dept d
on d.deptno = e.deptno;

-- 全连接 full outer join 在mysql中不支持，在oracle才支持。 --展示左右表全部不匹配的数据
select * from emp e
full outer join dept d
on d.deptno = e.deptno;

-- 解决mysql不支持全外连接的问题

select * from emp e
 left outer join dept d
on d.deptno = e.deptno
union -- 去重
select * from emp e
 right outer join dept d
on d.deptno = e.deptno;


select * from emp e
 left outer join dept d
on d.deptno = e.deptno
union all -- 不去重 效率高
select * from emp e
 right outer join dept d
on d.deptno = e.deptno;

-- mysql对集合操作支持比较弱，只支持交集操作
-- outer 可以省略不写


```

#### 三表联查

```sql
-- 三表联查
select * from emp;
select * from dept ;
select * from salgrade;


select e.empno, e.ename, e.sal, e.deptno, d.deptno, d.dname, s.* 
from 
emp e
inner join dept d
on e.deptno = d.deptno
inner join salgrade s 
on e.sal > s.losal and e.sal < s.hisal -- e.sal BETWEEN s.losal and s.hisal

```

#### 自关联、92语法(了解)

```sql
select * from emp

-- 自关联 自己和自己连接
select e1.empno, e1.ename as '员工', e1.mgr, e2.ename as '领导' from emp e1
left join emp e2
on e1.mgr = e2.empno

-- 92语法
select e.empno, e.ename, e.sal, e.deptno, d.dname
from emp e, dept d

-- 上面的语句相当于99语法中的cross join，会出现笛卡尔积
select e.empno, e.ename, e.sal, e.deptno, d.dname
from emp e, dept d
where e.deptno = d.deptno

-- 查询员工的编号，员工姓名，薪水，员工部门编号，部门名称，查询出工资大于2000的员工
select e.empno, e.ename, e.sal, e.deptno, d.dname
from emp e, dept d
where e.deptno = d.deptno and e.sal > 2000

-- 总结
-- 92语法会比较麻烦，在92语法中表的链接条件和筛选条件是放在一起的没有分开
-- 查询员工的姓名，岗位，上级编号，上级名称
-- 99语法中提供了更多的查询连接类型：cross、natural、inner、outer
select e1.ename, e1.job, e1.mgr, e2.ename
from emp e1, emp e2
where e1.mgr = e2.empno
```



### 单行子查询

1. 什么是子查询？

   一条SQL语句含有多个select

2. 执行顺讯

   先执行子查询，再执行外查询

3. 不相关子查询

   子查询可以独立运行，称为不相关子查询

4. 不相关子查询分类

   根据子查询的记过行数，可以分为单行子查询和多行子查询

```sql
-- 子查询
-- 查询所有比"CLARK"工资高的员工信息
-- 步骤1："CLARK"工资
select sal from emp where ename = 'CLARK'
-- 步骤2：查询所有比CLARK工资高的员工信息
select * from emp where  sal > (select sal from emp where ename='CLARK')

-- 单行子查询
-- 查询工资高于平均工资的雇员名字和工资
select ename, sal, job from emp where sal > (select avg(sal) from emp)

-- 查询和CLARK同一部门且比他工资低的雇员名字和工资
select ename, sal from emp where job = (select job from emp where ename='CLARK') 
and sal > (select sal from emp where ename ='CLARK')

-- 查询职务和SCOTT相同，比SCOTT雇佣时间早的雇员信息
select * from emp where job = (select job from emp where ename='SCOTT') 
and hiredate = (select hiredate from emp where ename='SCOTT')

```

### 多行子查询

 ![image-20220331222810263](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220331222810263.png) 

```sql
-- 查询【部门20中职务同部门10的雇员一样的】雇员信息
-- 查询雇员信息
-- > Subquery returns more than 1 row异常
select * from emp where deptno = 20 
and job in (select job from emp where deptno = 10)

-- 可以使用in或者使用any
select * from emp where deptno = 20 
and job = any (select job from emp where deptno = 10)

-- 查询工资比所有"SALESMAN"都高的雇员编号、名字和工资
-- 查询雇员的编号、名字和工资
select * from emp where sal > all(select sal from emp where job = 'SALESMAN')

select * from emp where  sal > (select max(sal) from emp where job = 'SALESMAN')

-- 查询工资低于任意一个CLERK的工资雇员信息
-- 单行子查询
select * from emp where sal < (select max(sal) from emp where job ='CLERK')
```



### 相关子查询

不相关的子查询：子查询可以独立运行，先运行子查询，再运行外查询

相关子查询：子查询不可以独立运行，并且先运行外查询，再运行子查询

不相关子查询的优缺点

优点：简单 功能强大(一些使用不相关子查询不能实现或者实现繁琐的子查询，可以使用相关子查询实现)

缺点：稍难理解

```java
-- 相关子查询
-- 查询最高工资的员工 不相关子查询
select * from emp where sal = (select max(sal) from emp )

-- 查询最高工资的员工(相关子查询)
select * from emp e where sal = (select max(sal) from emp where deptno = e.deptno) order by e.deptno

-- 查询工资高于其所在岗位的平均工资的那些员工(不相关子查询)
select * from emp e where job = 'CLERK' and sal > (select avg(sal) from emp where job= 'CLERK')
Union all 
select * from emp e where job = 'SALESMAN' and sal > (select avg(sal) from emp where job= 'SALESMAN')
Union all 
select * from emp e where job = 'MANAGER' and sal > (select avg(sal) from emp where job= 'MANAGER')
Union all 
select * from emp e where job = 'PRESIDENT' and sal > (select avg(sal) from emp where job= 'PRESIDENT')

-- 查询工资高于其所在岗位的平均工资的那些员工（相关子查询）
select * from emp e where sal > (select avg(sal) from emp where job= e.job)

```



### 事务及其特征

**事务(Transaction)：是用来维护数据库完整性的，他能够保证一系列的mysql操作要么全部执行，要么全不执行。**

**事务的概念**

事务(Transaction)指的是一个操作序列，该操作序列中的过个操作要么都做，那么都不做，是一个不可分割的工作单位，是数据库环境中的逻辑工作单位，由DBMS(数据库管理系统)中的事务管理子系统负责事务的处理

目前常用的存储引擎InnodB(MySQL5.5以后默认的存储引擎)和MyISAM(mysql5.5之前的默认存储引擎)，其中innoDB支持事务处理机制，而MyISAM不支持。

**事务的特性**

原子性、一致性、隔离性、持久性 ACID

```sql
-- 事务及其特征

create table account(
	id int primary key auto_increment,
	uname varchar(10) not null,
	balance double
);

select * from account
insert into account values(null, 'lili', 2000), (null, 'xiaogang', 2000)

-- lili -> xiaogang 200
update account set balance = balance - 200 where id =1;
update account set balance = balance + 200 where id =2;

-- 默认一个DML语句是一个事务，所以上面的操作执行的是两个事务
-- 上面操作执行了两个事务

-- 必须让上面的两个操作控制在一个事务中
-- 手动开启事务
start TRANSACTION;

-- 执行update操作
-- 手动回滚：刚刚做的操作全部取消
rollback;

-- 手动提交
commit;
-- 回滚和提交之前数据库中的数据无论怎么修改他都是修改缓存里面的数据，而不是真实的数据

```



#### 事务并发问题

![image-20220401134852594](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401134852594.png) 

![image-20220401135000483](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401135000483.png)

![image-20220401135033548](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401135033548.png) 

**不可重复度和幻读的区别在于：不可重复读的重点是修改，幻读的重点是在于新增或者删除**



#### 事务隔离级别

![image-20220401135413266](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401135413266.png) 

**可重复度使用得最多**

![image-20220401135503242](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401135503242.png) 



####  视图

视图(view)是一个从单张或者多张基础数据表或其他视图中构建出来的虚拟表。同基础表一样，视图中也包含了一系列带有名称的列和行数据，但是数据中心只是存放视图的定义，也就是动态检索数据的查询语句，而并不存放视图中的数据，这些数据依旧存放于构建视图的基础表中，只有当用户使用视图时采取数据库请求相对应的数据，即视图中的数据是在引用视图时动态生成的。因此视图中的数据依赖于构建视图的基础表，如果基本表中的数据发生了变化，视图中响应的数据也会跟着改变。

好处：简化用户操作，视图可以使用户将注意力集中在所关心的数据上，而不需要关系数据表的结构、与其他表的关联条件以及查询条件等。对机密数据提供安全保护：有了视图，就可以在设计数据库应用系统时，对不同的用户定义不同的视图，避免机密数据出现在不应该看到这些数据的用户视图上。这样视图就会自动提供了对机密数据的安全保护功能

```sql
-- 创建单表视图
create or replace view myview01
as
select empno, ename, job, deptno 
from emp
where deptno = 20
-- 校验
with check option;

-- 查看视图
select * from myview01

-- 在视图中插入数据
insert into myview01(empno, ename,job,deptno) value(9999, 'lili','CLERK', 20);
insert into myview01(empno, ename,job,deptno) value(8888, 'nana','CLERK', 30);
-- > 1369 - CHECK OPTION failed 'mydb.myview01' 加入with check option 加限制,不满足条件的不能加
insert into myview01(empno, ename,job,deptno) value(7777, 'fefe','CLERK', 30);


-- 创建/替换多表视图
create or replace view myview02
as 
select e.empno, e.ename, e.sal,d.deptno,d.dname
from emp e
join dept d
on e.deptno = d.deptno
where sal >2000

select * from myview02

-- 创建统计视图
create or replace view myview3
as
select e.deptno, d.dname, avg(sal), min(sal), count(1)
from emp e
join dept d 
using(deptno)
group by e.deptno


-- 基于视图中创建视图
create or replace view myview4
as
select  * from myview3 where deptno = 20

select * from myview4
```



### 存储过程

 **相当于是一段小程序**

![image-20220401212051334](D:\TyporaNote\马士兵教育\技术\mysql基础\mysql基础内容.assets\image-20220401212051334.png) 

```sql
-- 定义一个存储过程
-- 实现模糊查询操作
select * from emp where ename like '%A'

create PROCEDURE mypro01(name varchar(10))
begin
	if name is null or name = "" then
		select * from emp;
		
		else 
		select * from emp where ename like concat('%',name,'%');
		end if;
end;

-- 删除存储过程
drop procedure mypro01

-- 调用存储过程
call mypro01('')

call mypro01('R')


-- 第一一个有返回值的存储过程
-- 参数前面的in可以省略不写
-- found_rows()mysql定义一个函数，作用返回查询结果的条数
create PROCEDURE mypro02(in name varchar(10), out num int (3))
begin
	if name is null or name = "" then
		select * from emp;
		
		else 
		select * from emp where ename like concat('%',name,'%');
		end if;
		
		select found_rows into num;
end;


-- 调用存储过程
call mypro02(null, @num);
select @num;
```





































































































































 