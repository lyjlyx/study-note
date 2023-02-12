## SpringBoot使用

SpringBoot其实就是一个启动器，不算是微服务。

是一个启动点，减少对Spring的配置点。



微服务架构是一种将单体应用程序作为一套小型服务开发的方法，每种应用程序都在自己的进程中运行，并与轻量级机制（通常是HTTP资源API）进行通信。这些服务是围绕业务功能构建的，可以通过全自动部署机制独立部署。这些服务的集中管理最少，可以用不同的编程语言编写，并使用不同的数据存储技术。



@ComponentScan可以设置SpringBoot启动的扫描包



SpringBoot配置文件的启动顺序：

bootstrap.properties->bootstrap.yml->application.properties->application.yml



![image-20220628215356745](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220628215356745.png) 



### 配置文件位置的优先级

![image-20220628220314420](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220628220314420.png) 

![image-20220628220625088](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220628220625088.png) 



### SpringBoot整合Servlet



使用spring.factories进行自动装配



#### Servlet

```java
@WebServlet(name = "myServlet", urlPatterns = "/srv")
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("11");
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
        super.doPost(req, resp);
    }
}
```

启动类注册对应的servletBean

```java
@ServletComponentScan
@SpringBootApplication
public class StudySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBootApplication.class, args);
    }

    //将自定义servlet添加到SpringBoot容器中，当配置的url mappings之后，servlet自己的配置就不会生效
    @Bean
    public ServletRegistrationBean<MyServlet> getServletRegistrationBean() {
        ServletRegistrationBean<MyServlet> bean = new ServletRegistrationBean<>(new MyServlet());
        bean.setLoadOnStartup(1);
        return bean;
    }
}
```



#### Filter

```java
@WebFilter(filterName = "myFilter", urlPatterns = "/filter")
public class MyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("dofilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
```

#### Listener



```java
public class MyHttpSessionListener implements HttpSessionListener {

    static int online = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("you人");
        online ++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("销毁");
    }
}
```

```java
    @Bean
    public ServletListenerRegistrationBean<MyHttpSessionListener> getListener() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new MyHttpSessionListener());
        System.out.println("listener");
        return bean;
    }
```



![image-20220628231850682](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220628231850682.png) 







## Druid





### 多数据源配置和动态切换



ThreadLocal

![image-20220629195548247](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220629195548247.png) 



### SpringBoot整合mybatis











## SpringBoot初始参数的设置

![image-20220822103630920](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822103630920.png)



### 执行run方法

一、启动当前应用程序 -> 二、SpringApplication.run() -> 三、new SpringApplication()

​	1、配置ResourceLoader

​	2、判断当前应用程序的类型（servlet、reactive、none）

​	3、获取初始化器的实例对象

​	4、获取监听器的实例对象 初始化11个对象

​	5、获取当前应用程序的主类，开始执行

四、run()方法启动

​	1、设置启动时的时间

​	2、设置应用上下文

​	3、设置异常报告器

​	4、设置java.awt.headless系统参数为true

用starting进行一个启动

五、创建监听器对象，SpringApplicationRunListener，从配置文件中读取到EventPublishingRunListener

创建EventPublishingRunListener对象并且创建出SimpleApplicationEventMulticaster，将Application的11个监听器给到了当前对象，此时才能方便我们从中获取出符合监听事件的几个监听器。

六、装配命令行参数

七、准备应用程序运行的环境



### 监听器的启动

所有的监听器在运行的过程中需要监听某些事件，各种不同的监听器监听的事件是不一样的。

在SpringApplication对象创建的时候，创建了11个监听器，但是这11个监听器的功能是不同的，换句话说就是监听的事件是不同的，当开始启动监听器的时候，要判断当前监听器是否能监听该事件，如果可以，正常启动；不行，则直接抛弃。



EventPublishingRunListener



### 准备spring环境

![image-20220822145023697](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822145023697.png) 



![image-20220822145224213](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822145224213.png) 

![image-20220822145424999](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822145424999.png) 

 ![image-20220822145827498](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822145827498.png) 



![image-20220822150217020](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822150217020.png) 



### 加载yaml和properties文件

![image-20220822151334708](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822151334708.png) 

 ![image-20220822151348058](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822151348058.png)	![image-20220822151410773](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822151410773.png) 



## SpringBoot（五）



![image-20220822160646758](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822160646758.png) 

![image-20220822160659457](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822160659457.png) 



![image-20220822162135722](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822162135722.png)

![image-20220822162122353](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822162122353.png) 



![image-20220822163126049](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822163126049.png) 

![image-20220822163145645](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822163145645.png) 





![image-20220822163817534](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220822163817534.png) 





















































































