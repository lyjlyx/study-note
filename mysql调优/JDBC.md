

# JDBC及代码分层

 ![image-20220409155520655](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409155520655.png) 

JDBC是Java的一套访问控制数据库里面数据的一套标准，是面向接口的

建立连接、SQL语句请求、返回结果、断开连接



![image-20220409164007754](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409164007754.png) 



厂商驱动包

![image-20220409164639758](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409164639758.png) 

厂商实现了Driver接口

![image-20220409164946520](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409164946520.png) 

![image-20220409165016181](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409165016181.png) 

![image-20220409165608227](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409165608227.png) 

![image-20220409165625707](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409165625707.png) 

也可以使用classes12.jar包

![image-20220409171320424](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409171320424.png) 



**SPI(Service Provider Interface)**

是jdk内置的一种服务提供发现机制。SPI是一种动态替换发现的机制，比如有个接口，想运行时动态的给他添加实现，你只需要添加一个实现。我们经常遇到的就是java.sql.Driver接口，其他不同厂商可以针对同一接口做出不同的实现，mysql和postgresql都有不同的实现提供给用户，而Java的SPI机制可以为某个接口寻找服务实现。

![SPI](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/SPI.png)

![image-20220409172908027](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409172908027.png) 

![image-20220409173110289](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409173110289.png) 

**statement用于执行静态SQL语句并返回其生产的结果的对象**

**默认情况下，每个statement对象只能有一个ResultSet对象同时打开**

```java
package com.mashibing;

import java.sql.*;

/**
 * @author LYX
 * @description 如果需要建立连接，java提供了一套标准，数据库厂商来进行实现，包含实现子类
 *
 * @date 2022/4/9 16:50
 */
public class JDBCTest {
    public static void main(String[] args) {
        try {
            //1.加载驱动
            //当执行了当前代码之后会返回一个class对象，在此对象创建过程中会调用具体类的静态代码块
            Class<?> aClass = Class.forName("oracle.jdbc.OracleDriver");
            //2.建立连接
            //第1步中已经将driver对象注册到了DriverManager中，所以此时可以直接通过DriverManager来获取数据库的链接了
            //需要输入链接数据库的参数  url  username  password
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "C_TEST", "123456");
            //3.测试连接是否成功
            System.out.println(connection);
            //4.定义SQL语句
            //只要填写正常执行的SQL已经即可
            String sql = "select * from C_QWER";
            //5.准备静态处理块对象，将SQL已经放置到静态处理块中,可以理解为SQL语句放置的对象
            //statement:在执行SQL语句的过程中需要一个对象来存放SQL语句，将对象进行执行的时候调用的是数据库的服务
            //          数据库会从当前对象中拿到对应的SQL语句进行执行
            //执行的三种方式
            //execute:任何SQL已经都可以执行
            //executeQuery:只能执行查询已经
            //executeUpdate:只能执行增删改查
            Statement statement = connection.createStatement();
            //6.执行SQL语句 返回的对象是结果集合
            //将结果放到resultSet中，是返回结果的一个集合
            //需要经过循环迭代，才能获取到其中的一个集合
            ResultSet resultSet = statement.executeQuery(sql);
            //7.循环处理
            //使用while循环，有两种获取具体值的方式，
            // 第一种，通过索引下标编号来获取
            // 第二种，通过列名来获取
            // 推荐使用列名获取
            // 列名一般不会发生修改
            while (resultSet.next()) {
                //oracle是从1下标开始的
                String anInt = resultSet.getString(1);
                System.out.println(anInt);
                String ename = resultSet.getString("a");
                System.out.println(ename);
            }

            //8.关闭连接
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
```



```java
//默认情况下设置事务是否自动提交
connection.setAutoCommit(true)

```



**使用prepareStatement来防止SQL注入**

![image-20220409200110052](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409200110052.png) 



![image-20220409200328075](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409200328075.png) 



### 反射技术实现及封装



不仅仅局限于公共访问修饰符，所有的访问修饰符都能够拿到

![image-20220409204046726](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220409204046726.png) 



**反射在一定程度上破坏了封装性，需要合理使用**

//设置该属性能不能被外界访问

field.setAccessible(true)

Method 包含的范围是当前对象及父类对象的所有公共方法



**构造方法比较特殊，不能通过反射获取**

```java
package com.mashibing.reflect;

import com.mashibing.DBUtil;
import com.mashibing.TestClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author LYX
 * @description
 * @date 2022/4/10 14:06
 */
public class BaseDaoImpl {
    /**
     * 统一查询表的方法
     *
     * @param sql    不同的SQL语句
     * @param params 参数
     * @param clazz  SQL已经查询返回的对象
     * @return
     */
    public List getRows(String sql, Object[] params, Class clazz) {

        List list = new ArrayList();
        Connection connection = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;


        //建立连接
        connection = DBUtil.getConnection();
        //创建statement对象
        try {
            statement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            //开始执行查询操作 resultset中有返回的结果,需要将返回的结果放置到不同的对象中
            resultSet = statement.executeQuery();
            //获取结果集合的元数据对象
            ResultSetMetaData metaData = resultSet.getMetaData();
            //判断查询到的每一行记录中包含多少个列
            int columnCount = metaData.getColumnCount();
            //循环遍历resultset
            while (resultSet.next()) {
                //创建放置具体结果属性的对象
                Object obj = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //从结果集合中获取单一列的值
                    Object valueObj = resultSet.getObject(i + 1);
                    //获取列的名称
                    String columnName = metaData.getColumnName(i + 1).toUpperCase(Locale.ROOT);
                    //获取类中的属性
                    Field declaredField = clazz.getDeclaredField(columnName);
                    //获取类中属性对应的set方法
                    Method method = clazz.getMethod(getSetName(columnName), declaredField.getType());

                    if (valueObj instanceof Number) {
                        Number number = (Number) valueObj;
                        String name = declaredField.getType().getName();
                        if ("int".equals(name) || "java.lang.Integer".equals(name)) {
                            method.invoke(obj, number.intValue());
                        } else if ("byte".equals(name) || "java.lang.Byte".equals(name)) {
                            method.invoke(obj, number.intValue());
                        } else if ("short".equals(name) || "java.lang.Short".equals(name)) {
                            method.invoke(obj, number.intValue());
                        } else if ("long".equals(name) || "java.lang.Long".equals(name)) {
                            method.invoke(obj, number.intValue());
                        } else if ("float".equals(name) || "java.lang.Float".equals(name)) {
                            method.invoke(obj, number.intValue());
                        } else if ("double".equals(name) || "java.lang.Double".equals(name)) {
                            method.invoke(obj, number.intValue());
                        }
                    } else {
                        method.invoke(obj, valueObj);
                    }
                }
                list.add(obj);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(connection, statement, resultSet);
        }
        return list;
    }


    /**
     * 获取set方法名
     *
     * @param name
     * @return
     */
    public String getSetName(String name) {
        return "set" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public static void main(String[] args) {

        BaseDaoImpl baseDao = new BaseDaoImpl();
        List rows = baseDao.getRows("select A1,B1,C1,D1,E1 from C_QWER where A1 = ?", new Object[]{"adf"}, TestClass.class);

        for(Iterator iterator = rows.iterator(); iterator.hasNext();) {
            TestClass t = (TestClass) iterator.next();
            System.out.println(t);
        }

    }

}

```



### DBUtils及数据库连接池

```java
package com.mashibing.apacheDbUtils;

import com.mashibing.Emp;
import com.mashibing.util.MysqlDbUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author LYX
 * @description
 * @date 2022/4/10 15:18
 */
public class DbUtilsTest {

    public static Connection connection;


    public static void testCon() {
        connection = MysqlDbUtil.getConnection();

        String sql = "select * from emp where empno = ?";
        QueryRunner queryRunner = new QueryRunner();
        try {
            Emp query = queryRunner.query(connection, sql, new BeanHandler<Emp>(Emp.class), 7369);
            System.out.println(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void testConList() {
        connection = MysqlDbUtil.getConnection();

        String sql = "select * from emp";
        QueryRunner queryRunner = new QueryRunner();
        try {
            //                                                      返回什么类型的数据就用什么类型的handler
            List<Emp> query= queryRunner.query(connection, sql, new BeanListHandler<Emp>(Emp.class));
            for (Emp emp : query) {
                System.out.println(emp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //testCon();
        testConList();
    }

}

```



一开始有个初始值的链接个数，要是不够了那么就动态的增长，直到到了最大值就不能够申请新连接了，每个连接都有一个时间限制，当你超过一定时间之后，就要给他释放

初始值、扩容量、最大值、死亡时间

**数据库连接池的目的是为了减伤频繁开关连接的时间，提高整个系统的响应能力，通过分析发现应该具备以下几个属性**

1. 初始大小
2. 每次扩容的大小
3. 最大个数
4. 空闲连接的死亡时间

各种数据库连接池

DBCP、C3P0、Druid、hikariCP



#### C3P0

```java
package com.mashibing.pool.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author LYX
 * @description
 * @date 2022/4/10 16:46
 */
public class C3P0Test {



    public static void getConnection() {

    }

    public static void main(String[] args) {

        /**
         * 直接在类的方法中设置连接参数，一般没人使用，不太建议这样做，最好直接使用配置文件
         */
        //ComboPooledDataSource cpds = new ComboPooledDataSource();
        //try {
        //    cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        //
        //    cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=UTC");
        //    cpds.setUser("root");
        //    cpds.setPassword("hwlhaa");
        //    Connection connection = cpds.getConnection();
        //    System.out.println(connection);
        //} catch (PropertyVetoException e) {
        //    e.printStackTrace();
        //} catch (SQLException throwables) {
        //    throwables.printStackTrace();
        //}

    }
}

```

**配置文件名必须要交c3p0.properties**

![image-20220410165720549](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220410165720549.png)

```properties
#JDBC具备自己的规范，在JDBC4之前是必须要填写驱动名称的，但之后版本不需要填写
c3p0.driverClass=com.mysql.jdbc.Driver
c3p0.url=jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=UTC
c3p0.user=root
c3p0.password=hwlhaa
```



#### Druid

**是一个JDBC组件库，包含数据库连接池、SqlParser等组件，被大量业务和技术产品使用或集成，经历过最严苛线上业务场景考研。**















































































































































































































































































































































