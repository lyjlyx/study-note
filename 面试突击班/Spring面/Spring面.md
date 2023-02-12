# Spring面试

![image-20220310134944241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220310134944241.png)



## 1、谈谈Spring IOC的理解，原理与实现

**总：**

控制反转：

原来的对象是由使用者来控制，有了容器Spring之后，可以把整个对象交给Spring来帮我们进行管理

DI：依赖注入，把对应属性的值注入到对象中，可以用过@Autowired，popularBean完成属性值的注入

容器：

存放具体存储对象，使用map结构来存储，在Spring中一般存在三级缓存，singletonObjects存放完整的bean对象。

整个bean的生命周期，从创建到使用到销毁的过程都是由容器来管理(bean的生命周期)。

**分：**

一般聊IOC容器的时候要涉及到容器的创建过程(BeanFactory，DefaultListableBeanFactory)，向Bean工厂中设置一些参数(BeanPostProcessor，Aware接口的子类)

加载解析bean对象，准备要创建的bean对象的定义对象beanDefinition(xml,或者注解的解析过程)





















































