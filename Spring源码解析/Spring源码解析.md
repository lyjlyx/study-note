# Spring源码解析



## Spring是什么？

**spring是一个框架 并且是一个生态，可扩展性非常强**

IOC更多的应该叫一个思想，DI的话相当于是一个手段

![image-2022062415422989](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624154229896.png) 

![image-2022082608342466](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826083424616.png) 



spring源码的实现过程

**BeanDefinition对象 bean的描述信息**

**加载xml->解析xml文件->封装bean对象（BeanDefinition bean的定义信息）->实例化->放到容器中->从容器中获取**



**容器（Map）**

![image-20220624155401911](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624155401911.png) 



## IOC如何处理

容器中定义好一些bean之后可以通过getBean()直接获取的



定义规范、方便扩展。BeanDefinitionReader接口加载对应的Bean定义信息，解析处理环节



通过父子标签的嵌套关系，拆分成对应的Document对象。

![image-20220826085923381](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826085923381.png) 

**BeanDefinitionReader是一个抽象接口**



**Spring中bean是否是单例的？**

由scope指定



**有多种**

![image-20220624161601877](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624161601877.png) 



**最基本的反射方法**

![image-20220826090332342](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826090332342.png) 



**BeanDefinition通过反射的方式创建对象，这样子方式可以获取到当前对象所有的属性等等信息，比较灵活。用new比较死板**

![image-20220624162207572](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624162207572.png) 





### *BeanFactory

**Bean工厂，整个容器的根接口，也是容器的入口。**

类图

![image-20220826091527080](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826091527080.png) 



**BeanDefinition反射到实例化，这个反射其实是个虚的，他分为很多复杂的步骤**

![image-20220624162734277](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624162734277.png) 



**BeanDefinition实例化生成具体对象**



在容器创建过程中，需要动态的改变bean的信息怎么办？

```xml
<property name=url value=${jdbc.url}>
</property>
```



#### BeanFactoryPostProcessor



如果想随时修改BeanDefinition怎么办？

一、就要使用**BeanFactoryPostProcessor**



BeanFactoryPostProcessor、BeanPostProcessor这两个接口是不一样的，产生的作用也是不一样的，但是他们有一个统一的目的，就是后置处理（增强）器

 

PostProcessor分为两类：**BeanFactoryPostProcessor、BeanPostProcessor**

![image-20220624163420007](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624163420007.png)

![image-20220624163457210](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624163457210.png) 

例子：

PlaceholderConfigurerSupport 替换占位符用

![image-20220624163716127](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624163716127.png)

![image-20220624163724947](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624163724947.png) 



### 实例化

**我们要将实例化和初始化这两个区分开来，他们不是同一个东西。**



Spring实例化创建对象

![image-20220624164719160](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624164719160.png) 

一定是构造器执行完成之后才会去调用init-method方法的

**实例化环节，给堆里面开辟一个空间**





### 初始化



填充属性


调用set方法往里面设置属性值，例如：populate（填充属性）。

**设置Aware接口的属性**



#### Aware（意识到、感知）



实例化完成之后就是填充属性（populateBean()）

**设置Aware接口的属性**

ApplicationContext、ApplicationContextPostProcessor



***AbstractApplicationContext后续会重点讲**



**ignoreDependencyInterface忽略，为什么要忽略？**

![image-20220826204048643](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826204048643.png) 



![image-20220826204208955](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220826204208955.png) 



## BeanPostProcessor



增强器



**1、前置增强**

BeanPostProcessor:before



**2、执行init-method方法**



**3、后置增强**

BeanPostProcessor:after



**4、完整对象（context.getBean）**

**修改bean的信息，后置处理器**

里面包含了两个方法

![image-20220624170014644](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624170014644.png)

![image-20220624170032921](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624170032921.png) 



Bean的生命周期

![image-20220624170138377](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624170138377.png) 



![image-20220624170546259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624170546259.png) 

AOP的动态代理方式是Cglib、Jdk的动态代理

![image-20220624170730349](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624170730349.png) 



AbstractAutoProxyCreator自动代理创建器一定是实现了BeanPostProcessor。所以他里面有after和before方法。



## Aware接口的作用



Aware接口

```java
/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory;

/**
 * 一个标记的超级接口，指示一个bean能够符合条件的去被我们当前的Spring容器注册到，
 * 他是一个特殊框架的一个对象，通过一个回调风格的方法，可以获取到当前这个容器中所有的组件对象。
 *
 *  标记的超接口，指示一个bean有资格由Spring容器通过回调样式的方法获取特定框架对象。
 *  实际的方法签名由各个子接口决定，但是应该这样做 通常只包含一个接受单个参数的void方法
 *  
 * A marker superinterface indicating that a bean is eligible to be notified by the
 * Spring container of a particular framework object through a callback-style method.
 * The actual method signature is determined by individual subinterfaces but should
 * typically consist of just one void-returning method that accepts a single argument.
 *
 * <p>Note that merely implementing {@link Aware} provides no default functionality.
 * Rather, processing must be done explicitly, for example in a
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}.
 * Refer to {@link org.springframework.context.support.ApplicationContextAwareProcessor}
 * for an example of processing specific {@code *Aware} interface callbacks.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 */
public interface Aware {

}

```



在使用当前对象的时候，可以获取当前容器中有的属性值。



为相关属性设值

**invokeAwareMethods**

![image-20220624171701072](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624171701072.png) 



**Aware接口到底有什么用？**

当Spring容器创建的bean对象在进行具体操作的时候，如果需要容器的其他对象，此时可以将对象实现Aware接口来满足当前的需要。



![image-20220624171944498](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624171944498.png) 

## SpringBean



我们需要将SpringBean做一个区分

**普通对象（我们自定义的对象）、Spring容器对象（内置对象）**

![image-20220624172254821](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624172254821.png) 

![image-20220828112616552](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828112616552.png) 

普通对象相当于：我们自定义自己需要的对象

内置对象相当于：Spring需要的对象



**invokeBeanFactoryPostProcessors**

实例化并且执行所有已经实例化过的BeanPostProcessor Bean对象







## **观察者模式**



BeanFactory的子类实现应该竟可能多的去支持一个标准的Bean的生命周期接口

![image-20220624172828444](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624172828444.png) 



**在不同的阶段要处理不同的工作，应该怎么办？使用观察者模式**

观察者模式：**监听器，监听事件，多播器（广播器）后续会讲**



## 接口



**关键接口：**

**BeanFactory、Aware、BeanDefinition、BeanDefinitionReader、BeanFactoryPostProcessor、BeanPostProcessor**



**Environment**环境----->（*StandardEnvironment）程序在启动的过程中，往往要获取当前系统中的某些属性值，或者环境变量值。例如：

获取系统属性值，方便从里面拿东西

System.getEnv()

System.getProperties()

FactoryBean*



**在整个容器还没有启动的时候就已经开始获取我们当前环境的变量，方便后续操作的时候从里面拿东西**

![image-20220828114019429](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828114019429.png) 



Environment在Context容器中定义了一个全局的值

![image-20221213204944520](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221213204944520.png) 

![image-20221213205046626](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221213205046626.png) 



**BeanDefinitionRegistry**

![image-20221213205510886](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221213205510886.png) 





**BeanFactory和FactoryBean的区别吗？**

**他们都是用来创建对象的**，当使用BeanFactory的时候必须要遵循完整的创建过程，这些过程是由Spring来管理控制的，而FactoryBean只需要调用getObject就可以返回具体对象，整个对象的创建过程都是由用户自己来控制的，更加灵活。

![image-20220624175256495](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220624175256495.png) 

BeanFactory和FactoryBean 的区别，他们都是用来创建对象的，当使用BeanFactory的时候就必须要遵循完整的Bean创建过程，就是完整的生命周期，这些过程都是由Spring来管理控制的。

而使用FactoryBean只需要调用getObject就可以返回具体的对象，整个对象的创建过程都是由用户自己来控制的，更加的灵活。



比较核心的点是在于，我们在获取容器对象的时候他每次在创建对象之前会做一个当前对象的判断。



## 接口对应的方法



 ![image-20221221220156716](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221221220156716.png)



![image-20220828151609555](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828151609555.png) 

![image-20220828151728547](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828151728547.png) 

![image-20220828151717526](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828151717526.png) 



具体是哪里会调用getObject()这个方法呢？如下图

![image-20220625142036312](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625142036312.png) 



![image-20221215214410594](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221215214410594.png) 

![image-20221215214444707](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221215214444707.png)

![image-20221215214642682](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221215214642682.png) 

![image-20221215214621015](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221215214621015.png) 



FactoryBean中的对象并不是容器创建好之后创建的

![image-20220625142221158](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625142221158.png) 

A对象是由AFactoryBean 来创建的 

而是通过factory.getObject()方法创建出来的

A对象并不是整个容器创建好之后才创建的

![image-20220625142355634](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625142355634.png) 

**意义**：

**就是当我们需要bean的时候是通过当前的工厂里面的getObject方法来创建的，而不需要去遵循bean的整个生命周期**



## AbstractAppliactionnContext源码讲解



这个方法包含了Spring启动的核心流程

***refresh()**

![image-20220625143951766](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625143951766.png) 

**很多super的方法一定要去看父类的构造方法中到底做了些什么事情**



![image-20221222154438402](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221222154438402.png) 





这两个接口非常关键

**ListableBeanFactory**



**ConfigurableBeanFactory**



![image-20220828164006413](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828164006413.png) 

 



在创建具体的一个对象的时候，是有父容器和子容器相关的概念的。

![image-20220828165458959](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828165458959.png)



![image-20220625151609398](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625151609398.png) 



![image-20221222160710522](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221222160710522.png)





## 做容器刷新前的准备工作

```java
			/**
			 * 前戏：做容器刷新前的准备工作
			 * 1、设置容器的启动时间
			 * 2、设置活跃状态为true
			 * 3、设置关闭状态为false
			 * 4、获取Environment，并加载当前系统的属性值到Environment对象中
			 * 5、准备监听器和事件的集合对象，默认为空的集合
			 */
```



## bean工厂增强器源码（BeanFactoryPostProcessor）



![image-20221224225333131](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221224225333131.png) 



![image-20221224225638630](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221224225638630.png) 

Aware是为了获取系统容器中的某些对象。invokeAwareMethod()



BFPP

![image-20220625153315532](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625153315532.png) 

因为实例化之后再修改BeanDefinition是没有意义的，因为在进行实例化的时候就是要根据我们的beanDefinition来获取里面的属性值，执行相关操作。

在实例化之前还需要进行一些基本的准备工作





## 实例化前期工作





![image-20221225222325911](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225222325911.png)



![image-20221225222754088](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225222754088.png) 

![image-20221225222805161](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225222805161.png) 



![image-20221225223101064](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223101064.png) 





![image-20220625193003299](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625193003299.png)



![image-20221225223155162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223155162.png) 

![image-20221225223333034](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223333034.png) 



![image-20221225223426358](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223426358.png) 



![image-20221225223457416](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223457416.png) 



![image-20221225223610514](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223610514.png) 

![image-20221225223808703](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225223808703.png) 



![image-20221225224036105](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225224036105.png) 

![image-20221225224119703](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225224119703.png) 

![image-20221225224228379](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225224228379.png) 

![image-20221225224250902](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221225224250902.png) 





## 实例化



使用dependsOn可以改变bean的加载顺序，正常是加载A、B、C这样的顺序，如果AdependsOn于B、C，那么就会先加载B、C

![image-20220828230615929](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220828230615929.png) 



doCreateBean()

![image-20221226162655008](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226162655008.png) 





反射的灵活性非常的高，反射如果只是使用几次是不会性能低的，如果重复调用了万级次数的话才会体现出来。

![image-20220625193415403](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625193415403.png)  

![image-20221226162930306](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226162930306.png) 

![image-20221226163029078](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226163029078.png) 

![image-20221226163431840](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226163431840.png) 



![image-20221226163422137](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226163422137.png) 



![image-20221226163615210](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226163615210.png)

实例化只完成一件事情，在堆中创建一快空间，当前对象的属性值都是默认值。
![image-20220829093801107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829093801107.png) 





解决循环依赖的代码

![image-20221226163823567](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221226163823567.png) 

![image-20221227215611967](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221227215611967.png) 



**提前暴露，构造器形式的循环依赖没办法解决，如果是set的话是可以解决的，因为把实例化和初始化分开了。**

**暴露的是只完成实例化，但是未完成初始化的步骤，也就是默认值为空的对象给他暴露出去、**





![image-20220625194024868](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194024868.png) 

**为什么要使用三级缓存**？

关键点在于，getEarlyBeanReference方法里面。



![image-20220625194225885](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194225885.png) 

填充属性

![image-20221228225055919](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221228225055919.png) 



![image-20221229084005415](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084005415.png) 

设置AWare接口的属性值，执行对应的属性填充操作

![image-20220625194633317](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194633317.png) 

执行BeanPostProcessor前置处理方法

![image-20220625194723552](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194723552.png) 

![image-20221229084041026](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084041026.png) 

![image-20220625194731715](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194731715.png) 

当我们在实例化一个bean对象的时候，可能包含n多个PostProcessor



执行initMethod方法

![image-20220625194826177](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194826177.png) 



执行BeanPostProcessor 的after方法

![image-20220625194906123](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625194906123.png)



![image-20221229084121969](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084121969.png) 



销毁

![image-20220625195054329](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625195054329.png) 



记录创建对象过程中的状态

![image-20220625195338773](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625195338773.png) 

![image-20221229084520037](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084520037.png) 

![image-20221229084441860](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084441860.png) 



![image-20221229084633612](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084633612.png) 



**如果一个类实现了FactoryBean这个接口的话，要强调一件事情就是他要创建两个对象。先创建FactoryBean的一个对象，有这个对象之后在调用getObject()才会返回我们自定义创建的对象。**

![image-20221229084806762](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221229084806762.png) 



## spring的具体细节工作

![image-20220625205156245](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625205156245.png) 



用来解析当前系统需要运行的时候所需要的一些资源。

![image-20221230083006702](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221230083006702.png) 

![image-20220625214157091](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625214157091.png)  

![image-20220625214259877](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625214259877.png) 

![image-20221230083101943](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221230083101943.png) 

![image-20221230083244959](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221230083244959.png) 





资源加载器



![image-20221230083456461](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221230083456461.png) 



![image-20220829141023902](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829141023902.png)



变量替换（一般没人这么干，只是提供了方式）

![image-20220625233800058](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220625233800058.png) 

![image-20221230084056485](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221230084056485.png) 



### 属性值设置

![image-20230102163630202](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102163630202.png) 

![image-20230102163650793](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102163650793.png) 

![image-20230102163719126](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102163719126.png) 



替换占位符，配置文件

![image-20230102170245834](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102170245834.png) 

![image-20230102172358700](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102172358700.png) 

![image-20230102172844503](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102172844503.png) 

![image-20230102172902145](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102172902145.png) 

 ![image-20230102172950883](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102172950883.png) 





![image-20220626000310184](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626000310184.png) 

**AbstractRefreshableConfigApplicationContext**



### InitPropertySources讲解



![image-20230102174020316](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102174020316.png) 



该方法在**ClassPathXmlApplicationContext**中是一个空实现，自己实现扩展。

![image-20220626000854869](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626000854869.png) 



调用的是当前父类的构造方法

![image-20220829145016384](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145016384.png)

```java
package com.msb.init;

import com.msb.MyBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author LYX
 * @description
 * @date 2022/8/29 14:31
 */
public class MyClassPathXmpApplicationContext extends ClassPathXmlApplicationContext {

	public MyClassPathXmpApplicationContext(String... configLocations) {
		super(configLocations);
	}

	@Override
	protected void initPropertySources() {
		System.out.println("自定义扩展initPropertySources");
		//设置必要的属性值
		getEnvironment().setRequiredProperties("username");
	}

	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		super.setAllowBeanDefinitionOverriding(false);
		super.setAllowCircularReferences(false);//设置完这两个属性之后，下一步还需要再调用父类的属性值。
		//在这里也可以注册我们自己的BeanFactoryPostProcessor
		super.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
		super.customizeBeanFactory(beanFactory);
	}

	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		System.out.println("扩展实现postProcessBeanFactory方法");
	}
}

```



![image-20220829145122688](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145122688.png)  



![image-20220829145046473](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145046473.png) 



**报错误**

![image-20220829145247058](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145247058.png) 

原因：没有找到对应的属性值 [abc]



![image-20220829145347316](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145347316.png) 

设置为username

![image-20220829145420794](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829145420794.png) 



例子：自己扩展当前属性的环境变量值

![image-20230102181123070](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102181123070.png) 

![image-20220626000751951](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626000751951.png) 



### getEnvionment讲解



![image-20230102225402691](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102225402691.png) 

![image-20230102225416408](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102225416408.png) 



上下文中设置RequiredProperties属性，下面的代码才有意义

```java
	protected void initPropertySources() {
		System.out.println("自定义扩展initPropertySources");
		//设置必要的属性值
		getEnvironment().setRequiredProperties("username");
	}
```

![image-20230102225432604](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102225432604.png) 

![image-20220626001644696](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626001644696.png) 



**验证需要的属性文件是否都已经放入环境中**

![image-20220626001339174](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626001339174.png) 

判断，没有值的换会抛异常

![image-20220626001705202](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626001705202.png) 



```
applicationListeners虽然在spring源码中是空的，但是在SpringBoot的中有对其进行添加
```

![image-20230102230105605](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102230105605.png) 



![image-20220829151303829](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829151303829.png)

![image-20230102230257524](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102230257524.png) 



**SpringBoot** 里面有14个

![image-20220829151447234](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829151447234.png) 







### prepareRefresh讲解

1、设置容器的启动时间
2、设置活跃 true、关闭状态为false
3、获取Environment对象，并加载当前系统属性值到Environment对象中
4、准备监听器和事件的集合对象，默认为空的集合

![image-20220626002451311](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626002451311.png) 

![image-20220626002456339](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626002456339.png)





### Bean工厂讲解



![image-20220626002523806](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626002523806.png)



![image-20230102231554267](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230102231554267.png)  



![image-20220626002748242](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626002748242.png)



![image-20220626002738176](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626002738176.png) 

![image-20230103222121015](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103222121015.png) 



allow...的属性值现在都是等于true

![image-20220829163800641](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829163800641.png) 



下图的allow...属性都是null

![image-20220829164008018](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829164008018.png) 



更改为“**不自动处理我们的循环依赖问题**”，我们只需要重写customizeBeanFactory这个方法就行

![image-20220829165054523](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829165054523.png) 

 

![image-20220829165219137](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829165219137.png) 



修改对应的属性

```java
	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		super.setAllowBeanDefinitionOverriding(false);
		super.setAllowCircularReferences(false);//设置完这两个属性之后，下一步还需要再调用父类的属性值。
		//在这里也可以注册我们自己的BeanFactoryPostProcessor
		super.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
		super.customizeBeanFactory(beanFactory);
	}
```

![image-20220626003956575](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626003956575.png) 





### **loadBeanDeifinitions讲解

![image-20230103225655825](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103225655825.png) 

**套娃**

![image-20220829165434997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829165434997.png) 

![image-20230103230056147](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230056147.png) 

**适配器模式**

![image-20230103230118601](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230118601.png) 

![image-20230103230144200](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230144200.png) 

![image-20230103230154346](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230154346.png) 

![image-20230103230218273](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230218273.png) 

![image-20230103230229539](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230103230229539.png) 

......



为什么要进行那么多方法的重载？

为了加载我们bean的定义信息 

![image-20220829165658873](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829165658873.png) 



![image-20220626101450561](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626101450561.png) 



```java
//实体处理器   用他来读取我们本地的xsd或者dtd文件来完成相关的解析工作
beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
```

![image-20220829194946994](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829194946994.png) 

![image-20220829195333589](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195333589.png) 



![image-20220829195440728](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195440728.png) 



只是指定了配置文件，没有对其进行一个解析处理的工作

![image-20220829195545143](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195545143.png)

![image-20220829195646691](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195646691.png) 

![image-20220829195741288](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195741288.png) 



这个文件是在什么时候开始读取的呢？

![image-20220829195927458](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195927458.png) 

![image-20220829195954826](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829195954826.png) 



idea会默认帮我们调用一些信息

![image-20230104085217352](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104085217352.png) 



![image-20220626102838145](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626102838145.png) 





![image-20230104085523091](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104085523091.png)  

![image-20230104085549833](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104085549833.png)  



configLocations在当前这个类就设置过了，所以可以直接拿来用。

![image-20230104085610934](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104085610934.png) 

之前设置过 **前后是关联的**

![image-20220829200808079](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220829200808079.png) 



## Spring配置文件加载过程 （4节）





### DefaultListableBeanFactory讲解

![image-20220626110307077](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626110307077.png) 



### loadBeanDeifinitions讲解



xml文件标签属性的文档规范大部分都是由dtd或者xsd来定义的，xsd用得比较多

![image-20230104224647884](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104224647884.png) 





加载bean 的定义信息

![image-20230104230018150](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230104230018150.png) 



![image-20230105082237462](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105082237462.png) 



![image-20230105082503325](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105082503325.png) 





不管是什么类型的配置文件，都有一个BeanDefinitionReader用来读取他的相关配置信息

![image-20230105083430648](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105083430648.png) 

![image-20230105083553477](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105083553477.png) 



![image-20230105083617060](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105083617060.png) 

![image-20230105083912927](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105083912927.png) 

![image-20230105084013792](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084013792.png) 



循环配置文件数组，处理每一个单一的具体对象

![image-20230105084030521](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084030521.png) 



![image-20230105084131629](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084131629.png) 



资源类加载器是否实现了ResourcePatternResolver

![image-20230105084302203](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084302203.png) 

![image-20230105084346707](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084346707.png) 





![image-20230105084523641](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084523641.png) 



resourcePatternResolver 这个对象的创建是在前面的 ClassPathXmlApplication对象的构造方法的supper里面创建的一直点supper 然后点this()，如下 第二张图

![image-20230105084622445](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084622445.png) 

![image-20230105084754687](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084754687.png) 



![image-20230105084830762](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105084830762.png) 



指定ClassPath下的某一个路径，可以匹配前缀解析

![image-20230105085212815](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085212815.png) 



![image-20230105085447566](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085447566.png) 

 ![image-20230105085626413](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085626413.png) 

![image-20230105085800762](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085800762.png) 







loadBeanDefinition方法从String[]->String->Resource[]->Resource，最终将Resource读取成为了一个Document文档，根据文档的节点信息，封装成一个一个的BeanDefinition对象。

![image-20230105085928588](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085928588.png) 

![image-20230105085939071](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105085939071.png) 



![image-20230105090150736](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105090150736.png) ![image-20230105090201713](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230105090201713.png) 





### 创建对象封装到BeanDefinitions

最主要的工作是为了进行实例化操作，对元素标签进行解析操作



![image-20220626120902170](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626120902170.png) 

![image-20230106211800439](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106211800439.png) 

![image-20230106211911950](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106211911950.png) 



 核心处理流程代码 

![image-20230106212105028](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106212105028.png) 

可以在配置文件里面处理profile

![image-20230106212226534](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106212226534.png) 



![image-20230106212608771](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106212608771.png)  

![image-20230106215311603](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106215311603.png) 

一行一行的在读xml配置文件

![image-20230106215503038](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106215503038.png) 

![image-20230106215514400](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106215514400.png) 

![image-20230106215628363](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106215628363.png) 



相关处理工作

根据命名空间来选择对应的解析处理

![image-20230106215814402](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106215814402.png) 

![image-20230106220013628](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106220013628.png) 



![image-20230106220033687](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106220033687.png) 



![image-20230106220121490](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106220121490.png) 



![image-20230106230849753](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106230849753.png)

![image-20230106231505515](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106231505515.png) 



**GenericBeanDefinition**和**RootBeanDefinition**是一个平级的关系

![image-20230106231549409](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106231549409.png) 



![image-20230106232146838](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106232146838.png) 

![image-20230106232850812](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106232850812.png) 

![image-20230106232859687](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106232859687.png) 

![image-20230106233505952](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106233505952.png) 

![image-20230106233659150](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230106233659150.png) 

后续都是一些数据的解析工作，其实都是差不多的，没必要深究。





### 对bean元素详细解析

![image-20220626124617084](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626124617084.png) 



该方法结束之后意味着，我们已经完成了从我们的xml文件中解析到了对应的一些标签和元素，将这些元素都转成了一个BeanDefinition，再将这些BeanDefinition放到了BeanFactory里面，此时BeanFactory里面已经包含了一些Bean的定义信息了。

![image-20220901082607296](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220901082607296.png) 



![image-20220901082800752](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220901082800752.png) 

![image-20230107221853395](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107221853395.png) 

![image-20230107221903010](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107221903010.png) 



![image-20230107222134173](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222134173.png) 

![image-20230107222147022](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222147022.png) 

![image-20230107222205151](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222205151.png) 

![image-20230107222404554](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222404554.png) 



![image-20230107222621934](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222621934.png) 

![image-20230107222749773](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107222749773.png) 

```
beanDefinitionMaps 相当于是放BeanDefinition对象的map集合，他是必须要根据名字来获取BeanDefinition的而我们要根据beanDefinitionNames里面的name来获取map里面的BeanDefinition

```

![image-20230107223035066](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107223035066.png) 

例如下图： 所有的BeanDefinition都是通过beanDefinitionNames集合中的名字来获取的。

![image-20230107223200549](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230107223200549.png) 





## Spring自定义标签解析过程



### 回顾

这两边的loadBeanDefinition()使用的是同一个（在spring中很多代码都是复用的）

![image-20230109143259393](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109143259393.png)

![image-20230109143609507](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109143609507.png) 



自定义bean标签所涉及到的解析方法

![image-20230109144127275](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109144127275.png) 





### parsecustomElement解析



***命名空间处理器  NameSpaceHandler**

![image-20230109144929330](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109144929330.png) 





META-INF -> Spring.schemas

handlers -> spring.handlers

xxx.dtd、xxx.tsd。从网络环境中进行相关的下载工作，映射成本地的资源文件。

![image-20220903103754246](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220903103754246.png) 

![image-20220903103902908](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220903103902908.png) 



要在获取当前这个处理类之前把这些handler类都拿过来。

![image-20230109144814867](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109144814867.png) 



![image-20230109145038790](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109145038790.png) 

初始化类的标签

![image-20220903104054506](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220903104054506.png) 

 

![image-20220902221020897](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220902221020897.png) 

1、加载spring.handlers配置文件

2、将配置文件内容加载到map集合中

3、根据指定的key去获取对应的处理器



**如果需要自定义标签的话，我们应该做以下步骤：**

**1、创建一个对应的解析器处理类（在init方法中添加parser类）**

**2、创建一个普通的spring.handler配置文件，让应用程序能够完成加载工作**

**3、创建对应标签的parser类。（对当前标签的其他属性值进行解析工作）**

![image-20230109210841553](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210841553.png) 

![image-20230109210852295](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210852295.png) 

![image-20230109210902806](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210902806.png) 



![image-20230109210940768](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210940768.png) 

![image-20230109210947958](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210947958.png) 

![image-20230109210952372](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109210952372.png) 



![image-20220903105556725](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220903105556725.png) 

![image-20230109212644765](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109212644765.png) 



 DefaultNamespaceHandlerResolver

![image-20230109212701033](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109212701033.png) 

getHandlerMappings() 方法与解析.schemas文件的PluggableSchemaResolver 类似

![image-20230109212727767](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109212727767.png) 



![image-20230109212803281](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109212803281.png) 



当执行完DefaultNamespaceHandlerResolver的构造方法之后

![image-20230109213005631](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109213005631.png) 

 namespaceHandlerResolver属性中就有对应的值了

![image-20230109213039654](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109213039654.png) 

 

这边能显示的原因是因为**DefaultNamespaceHandlerResolver**构造方法中有toString方法,由idea给我们执行的。

![image-20230109213056143](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109213056143.png) 

![image-20230109213219259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109213219259.png) 



![image-20230109214116377](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109214116377.png) 

![image-20230109214230776](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230109214230776.png) 



###  元素解析器

**如果需要自定义标签的话，我们应该做以下步骤：**

**1、创建一个对应的解析器处理类（在init方法中添加parser类）**

**2、创建一个普通的spring.handler配置文件，让应用程序能够完成加载工作**

**3、创建对应标签的parser类。（对当前标签的其他属性值进行解析工作）** 

![image-20230110082957965](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110082957965.png) 



![image-20230110083230143](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083230143.png) 

![image-20230110083238023](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083238023.png) 

通过名称取到对应的Parser对象

![image-20230110083622326](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083622326.png) 

![image-20230110083639723](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083639723.png) 

![image-20230110083649469](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083649469.png) 

![image-20230110083702197](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083702197.png) 

![image-20230110083716369](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083716369.png) 



往beanDefinitionMaps和beanDefinitionNames集合里面放对应的属性值

![image-20230110083954111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110083954111.png) 

![image-20230110084838083](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110084838083.png) 





### 自定义标签完成解析工作





参考该类，我们就可以自定义标签

![image-20220904100610269](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220904100610269.png) 

Handler参考该类

![image-20220904101417588](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220904101417588.png) 



**spring.schemas、spring.handlers两个配置文件命名问题，会因为这段代码而报错**

![image-20220904152137797](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220904152137797.png) 

![image-20230111193334523](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111193334523.png) 



**开始自定义**

要实现的功能：

```
自定义一个<lyx:user username email  age> </lyx:user>，完成整个标签的处理工作
```



![image-20230110202504974](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230110202504974.png) 

```java
package com.msb.selftag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @author LYX
 * @description
 * @date 2022/9/4 10:06
 */
public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		//获取标签具体对应的属性值
		String userName = element.getAttribute("userName");
		String email = element.getAttribute("email");
		String password = element.getAttribute("password");

		if (StringUtils.hasText(userName)) {
			builder.addPropertyValue("userName", userName);
		}
		if (StringUtils.hasText(email)) {
			builder.addPropertyValue("email", email);
		}
		if (StringUtils.hasText(password)) {
			builder.addPropertyValue("password", password);
		}
	}

	//返回属性值所对应的对象
	@Override
	protected Class<?> getBeanClass(Element element) {
		return User.class;
	}

}
```

```java
package com.msb.selftag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author LYX
 * @description
 * @date 2022/9/4 10:13
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
	}
}

```

```java
package com.msb.selftag;

/**
 * @author LYX
 * @description 定义一个标签类
 * @date 2022/9/4 10:04
 */
public class User {
	private String userName;
	private String email;
	private String password;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

```

application.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:lyx="http://www.lyx.com/schema/user"

	   xmlns:context="http://www.springframework.org/schema/context"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
	   http://www.springframework.org/schema/beans/spring-beans.xsd

		http://www.lyx.com/schema/user http://www.lyx.com/schema/user.xsd
		http://www.springframework.org/schema/context
		https://www.springframework.org/schema/context/spring-context.xsd">

	<!--为什么开启这个的时候就能够识别到自己注册的类呢？-->
	<context:component-scan base-package="com.msb"></context:component-scan>

	<lyx:user id="lyx" userName="罗逸轩" email="lyx@qq.com" password="123456"/>

	<!--声明这个对象为spring管理的一个bean对象-->
<!--	<bean class="com.msb.selfBdrpp.MyBeanDefinitionPostProcessor">-->

<!--	</bean>-->

	<bean id="person2" class="com.msb.init.Person">
		<constructor-arg index="0" value="1"></constructor-arg>
		<constructor-arg index="1" value="2"></constructor-arg>
	</bean>


	<bean id="person" class="com.msb.init.Person">
		<property name="id" value="1"/>
		<property name="name" value="王八蛋"/>
	</bean>
	<!--该对象交由Spring来进行管理-->
	<!--	<bean class="com.msb.MyBeanFactoryPostProcessor"></bean>-->


<!--	<bean id="studentConverter" class="com.msb.selfconvert.StudentConverter"/>-->
<!--	<bean id="conversionService" class="org.springframework.core.convert.support.ConversionServiceFactory">-->
<!--		<property name="converters">-->
<!--			<set>-->
<!--				<ref bean="studentConverter"/>-->
<!--			</set>-->
<!--		</property>-->
<!--	</bean>-->

</beans>
```

user.xsd

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<!--这里就是指定自己的解析文件，我们最好指定成自己的东西-->
<schema xmlns="http://www.w3.org/2001/XMLSchema"
		targetNamespace="http://www.lyx.com/schema/user"
		xmlns:tnx="http://www.lyx.com/schema/user"
		elementFormDefault="qualified">
	<element name="user">
		<complexType>
			<attribute name="id" type="string"/>
			<attribute name="userName" type="string"/>
			<attribute name="email" type="string"/>
			<attribute name="password" type="string"/>
		</complexType>
	</element>
</schema>
```

spring.schemas

```
http\://www.lyx.com/schema/user.xsd=META-INF/user.xsd
```

spring.handlers

```
http\://www.lyx.com/schema/user=com.msb.selftag.UserNamespaceHandler
```



核心处理工作

![image-20230111193837512](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111193837512.png) 





## Spring的bean工厂准备工作





### prepareBeanFactory

![image-20230111194009103](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111194009103.png) 

给某些属性做一些初始化的工作

![image-20230111195224901](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111195224901.png) 





SPel表达式处理流程：

一开始需要一个处理类：StandardBeanExpressionResolver->在这个处理类的内部有一个Spel表达式的解析类->里面又包含了一个当前解析类所包含的配置类。

![image-20220904161058236](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220904161058236.png) 



![image-20230111200256310](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111200256310.png) 

![image-20230111200401799](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111200401799.png) 

![image-20230111200313607](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111200313607.png) 

![image-20230111200424953](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111200424953.png)  



在填充属性步骤的时候才会调用该方法

![image-20230111200435987](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111200435987.png) 





### 如何扩展实现自定义属性编辑器



![image-20230111201849182](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111201849182.png) 

![image-20230111202019497](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230111202019497.png) 





1、需要提前把要用的对象注册进去。自定义一个实现了PropertyEditorRegister接口的编辑器

2、需要让spring能够识别到此编辑器，自定义实现一个属性编辑器的注册器，实现了PropertyEditorRegistrar接口

3、让spring能够识别到对应的注册器



如果要实现自定义属性编辑器的话就要重写这个方法

![image-20220905092724002](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905092724002.png) 

1、自定义一个实现了PropertyEditorSupport

2、让spring能够识别到此编辑器，自定义实现一个属性编辑器的注册器，实现PropertyEditorRegistrar接口

3、让Spring能够识别到对应的注册器

![image-20220905093440226](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905093440226.png) 

![image-20220905093545193](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905093545193.png) 

问题：ResourceEditorRegistrar为什么能找到CustomerEditorConfigurer这个类

因为CustomerEditorConfigurer实现了BeanFactoryPostProcessor

![image-20220905093935162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905093935162.png) 



**开始实现自定义属性编辑器**

定义对象

```java
package com.msb.secondselfeditor;

/**
 * TODO
 *
 * @author Karry
 * @date 2023-01-11  20:47
 */
public class SecondAddress {

	private String province;
	private String city;
	private String town;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Override
	public String toString() {
		return "SecondAddress{" +
				"province='" + province + '\'' +
				", city='" + city + '\'' +
				", town='" + town + '\'' +
				'}';
	}
}

```

```java
package com.msb.secondselfeditor;

import com.msb.selfEditor.Address;

/**
 * TODO
 *
 * @author Karry
 * @date 2023-01-11  20:48
 */
public class SecondCustomer {
	private String name;
	private Address address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "SecondCustomer{" +
				"name='" + name + '\'' +
				", address=" + address +
				'}';
	}
}



```

自定义编辑器

```java
package com.msb.secondselfeditor;


import java.beans.PropertyEditorSupport;

/**
 * 自定义编辑器
 * @author Karry
 * @date 2023-01-11  20:48
 */
public class SecondAddressPropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] s = text.split("_");
		SecondAddress address = new SecondAddress();
		address.setProvince(s[0]);
		address.setCity(s[1]);
		address.setTown(s[2]);
		this.setValue(address);
	}

}

```

自定义注册器

```java
package com.msb.secondselfeditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * 定义注册器
 * @author Karry
 * @date 2023-01-11  20:52
 */
public class SecondAddressPropertyEditorRegister implements PropertyEditorRegistrar {
	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(SecondAddress.class, new SecondAddressPropertyEditor());
	}
}

```

xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!--	创建一个bean-->
	<bean id="customer" class="com.msb.secondselfeditor.SecondCustomer">
		<property name="name" value="罗逸轩"></property>
		<property name="address" value="福建省_龙岩市_长汀县"></property>
	</bean>
	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
		<property name="propertyEditorRegistrars">
			<bean class="com.msb.secondselfeditor.SecondAddressPropertyEditorRegister"/>
		</property>
	</bean>

	<!--	填充我们自己的类 放到Configurer中-->
	<!--	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">-->
	<!--		<property name="propertyEditorRegistrars">-->
	<!--			&lt;!&ndash;CustomEditorConfigurer类的这个属性PropertyEditorRegistrar[] propertyEditorRegistrars;是一个数组&ndash;&gt;-->
	<!--			<list>-->
	<!--				<bean class="com.msb.selfEditor.AddressPropertyEditorRegistrar"></bean>-->
	<!--			</list>-->
	<!--		</property>-->
	<!--	</bean>-->

	<!--第二种配置方式，直接通过CustomEditorConfigurer中的
	Map<Class<?>, Class<? extends PropertyEditor>> customEditors;这个属性进行扩展-->
	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
		<property name="customEditors">
			<map>
				<entry key="com.msb.selfEditor.Address">
					<value>com.msb.selfEditor.AddressPropertyEditor</value>
				</entry>
			</map>
		</property>

	</bean>

</beans>
```







### BeanFactoryPostProcessor

![image-20220626131023289](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626131023289.png) 



![image-20220905105039455](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905105039455.png) 

![image-20230112083830789](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112083830789.png) 



转换对应的属性值的方法（处理按照自定义规则的属性值，类似 xxx_xxx_xxx的value）

![image-20230112084447563](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084447563.png) 

![image-20230112084634671](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084634671.png) 



![image-20230112084817997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084817997.png) 

![image-20230112084839739](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084839739.png) 

![image-20230112084852778](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084852778.png) 

![image-20230112084930107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230112084930107.png) 





### 自定义属性编辑器的其他配置方式

原：

```java
		<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
			<property name="propertyEditorRegistrars">
				<!--CustomEditorConfigurer类的这个属性PropertyEditorRegistrar[] propertyEditorRegistrars;是一个数组-->
				<list>
					<bean class="com.msb.selfEditor.AddressPropertyEditorRegistrar"></bean>
				</list>
			</property>
		</bean>
```



**直接通过CustomerEditors设置进去**

![image-20230129085902220](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129085902220.png) 

![image-20220905111820097](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905111820097.png)

​	

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!--	创建一个bean-->
	<bean id="customer" class="com.msb.secondselfeditor.SecondCustomer">
		<property name="name" value="罗逸轩"></property>
		<property name="address" value="福建省_龙岩市_长汀县"></property>
	</bean>
	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
		<property name="propertyEditorRegistrars">
			<bean class="com.msb.secondselfeditor.SecondAddressPropertyEditorRegister"/>
		</property>
	</bean>

	<!--	填充我们自己的类 放到Configurer中-->
	<!--	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">-->
	<!--		<property name="propertyEditorRegistrars">-->
	<!--			&lt;!&ndash;CustomEditorConfigurer类的这个属性PropertyEditorRegistrar[] propertyEditorRegistrars;是一个数组&ndash;&gt;-->
	<!--			<list>-->
	<!--				<bean class="com.msb.selfEditor.AddressPropertyEditorRegistrar"></bean>-->
	<!--			</list>-->
	<!--		</property>-->
	<!--	</bean>-->

	<!--第二种配置方式，直接通过CustomEditorConfigurer中的
	Map<Class<?>, Class<? extends PropertyEditor>> customEditors;这个属性进行扩展-->
	<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
		<property name="customEditors">
			<map>
				<entry key="com.msb.selfEditor.Address">
					<value>com.msb.selfEditor.AddressPropertyEditor</value>
				</entry>
			</map>
		</property>

	</bean>

</beans>
```



### addBeanPostProcessor



**添加一个BeanPostProcessor后置处理器**

![image-20230129191820382](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129191820382.png) 



![image-20230129191938545](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129191938545.png) 

![image-20230129192018383](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192018383.png) 

![image-20230129192030000](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192030000.png) 



因为源码中invokeAwareMethods方法的实现及其简单，但是Aware所做到的扩展又不止这些，所以肯定会有别的地方对他进行扩展。实在invokeAwareMethod方法执行之后。

![image-20230129192154236](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192154236.png) 



**ApplicationContextAwareProcessor这里实现Aware其他相关的实现类**

![image-20230129192443314](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192443314.png) 



**如果我们的自定义类中实现了这几个接口，那么它设置值的时候是在postProcessore里面来设置的，而不是在invokeMoethods方法里面设置的。**

![image-20230129192452046](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192452046.png) 





**定义一个MyAwareProcess实现BeanPostProcessor**

**在BeanPostProcessor里面有一个方法postProcessorBeforeInitialization在此方法中可以设置其他的属性值**

![image-20220905122906097](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905122906097.png) 



![image-20230129192946425](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129192946425.png) 



invokeAwareMethods的访问修饰符为private的，设计上就是这样设计的，所以扩展的时候需要上述的方法定义postProcessor来自定义扩展

![image-20230129193451586](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129193451586.png) 



自定义aware接口示例

```java
public class SecondMyAwareProcessor implements BeanPostProcessor {

	private final ConfigurableApplicationContext applicationContext;

	/**
	 * Create a new ApplicationContextAwareProcessor for the given context.
	 */
	public SecondMyAwareProcessor(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		AccessControlContext acc = null;

		if (System.getSecurityManager() != null) {
			acc = this.applicationContext.getBeanFactory().getAccessControlContext();
		}

		((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);

		return bean;
	}
}
```



![image-20230129194159962](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129194159962.png) 



![image-20230129194547051](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129194547051.png) 



继承AbstractBeanFactory类，重写方法addBeanPostProcessor

![image-20230129195225188](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129195225188.png)





### ignoreDependencyInterface

**ApplicationContextAwareProcessor此类用来完成某些Aware对象的注入**





![image-20230129200729035](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129200729035.png) 



**反射在进行值处理的时候有两种方式：**

1、获取该属性对应的set方法来进行操作；

2、获取到该属性对象field的set方法设置

![image-20230129201240523](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129201240523.png) 

![image-20230129201529942](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129201529942.png) 

![image-20230129201536116](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129201536116.png) 



![image-20220905141417731](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905141417731.png) 



invokeAwareMethods方法中的Aware   所以需要下面的方法对他进行处理

![image-20220905141343544](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905141343544.png) 

```java
//设置要忽略自动装配的接口，因为这些几口的实现是由容器通过set方法进行注入的，
//所以在使用autowrie进行注入的时候需要将这些接口进行忽略
```



![image-20230129201906307](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129201906307.png) 



文章：打开BeanFactory ignoreDependencyInterface方法的正确姿势

```
https://www.jianshu.com/p/3c7e0608ff1f
```



![image-20230129202500266](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129202500266.png) 

![image-20230129202511454](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230129202511454.png) 

集合存取的原理





## Spring的beanFactoryPostProcessor的执行



@Autowried是通过什么方式设置进去的？

![image-20230130083322070](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130083322070.png) 

该注解可以加的Target位置，很多地方都可以加

如果是在字段上的话他可以直接获取到他的field，可以直接通过field.set设置进去。

如果标注在set方法上的话就是通过set方法直接设置进去的。需要注意当前的注解是在哪个地方生效的



### 内容回顾

主要都是围绕prepareBeanFactory()方法来讲解的。



#### postProcessBeanFactory

![image-20230130084554530](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130084554530.png) 

![image-20230130084649127](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130084649127.png) 



为了让后续的子类对他进行相关扩展的，继承重写该类的方法

![image-20230130084714980](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130084714980.png) 

例子

![image-20230130084856309](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130084856309.png) 

![image-20230130084846543](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130084846543.png) 





### *invokeBeanFactoryPostProcessor



**增强器、后置处理器**

主要是用来操作BeanFactory当前这个对象的

![image-20230130085043008](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230130085043008.png) 



![image-20230131084702799](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230131084702799.png) 



该接口处理的是**BeanDefinitionRegistry**这个类，BeanDefinitionRegistry提供了BeanDefinition的一些增删改查的操作。



**BeanDefinitionRegistry所针对的是BeanDefinition，而BeanFactoryPostProcessor所针对的是BeanFactory，而BeanFactory里面包含了BeanDefinition，所以他在进行操作的时候所针对的对象是不同的，而BeanFactory里面包含了一些Bean的定义信息**



registry并不是操作map和names的，因为Map里面放的是BeanDefinition，而Names里面放的是BeanDefinition的名称，他们执行两个集合，而registry是用来操作BeanDefinition的，只不过在前期的工厂对象准备的时候，我们把这些BeanDefinition设置到工厂里面去了，但具体的增删改查我们则需要从map里面取到，取到之后才能做具体的操作

![image-20220905145109688](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905145109688.png) 

![image-20220905145810829](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905145810829.png) 



BeanFactoryPostProcessor接口处理的是ConfigurableListableBeanFactory

![image-20220905145122248](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905145122248.png) 

BeanFactory包含了BeanDefinition



![image-20220905150408098](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905150408098.png) 

注册BeanFactoryPostProcessor的几种方式

![image-20220905150530901](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905150530901.png) 

第一种

![image-20220905150248123](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905150248123.png) 

![image-20220905150302403](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905150302403.png) 

第二种：

![image-20220905150233656](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905150233656.png) 

以上两种方式都可以



### invokeBeanFactoryPostProcessor执行流程



**1、BeanDefinitionRegistryPostProcessor**

上面这个接口集合是下面这个接口集合的子集，上面继承了下面的接口

**2、BeanFactoryPostProcessor**



![image-20230201090044591](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201090044591.png) 

**getBeanFactoryPostProcessor**



**先处理外部集合，再处理子集，再处理父集**



#### 在整个Spring的运行期间可以有多个BFPP，那么应该按照什么顺序执行呢？

一般我们使用的是@Ordered来去排序他们的执行。



![image-20220905151321689](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905151321689.png) 



**首先处理PriorityOrdered->再处理Ordered->最后还有一个没有实现任何排序接口的对象。**

![image-20220905151742653](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905151742653.png) 

**如果一个雷实现了BeanDefinitionRegistryPostProcessor，那么他的postProcessBeanFactory()方法可以和其他的BeanFactoryPostProcessor一起执行。**

所以我们可以把它们放到一个集合里面，然后循环调用执行就行了。



postProcessBeanFactory()



### 排序



**记住，所有的Registry都是对前面这个对象的增删该查的操作**

![image-20230201201949735](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201201949735.png) 

![image-20230201202109252](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201202109252.png) 

 ![image-20230201202037529](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201202037529.png) 



如果是一个BeanDefinitionRegistryProcessor的话，那么就会先执行postProcessBeanDefinitionRegistry方法，然后给他放到集合中

![image-20220905154923563](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905154923563.png) 



![image-20230201202433224](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201202433224.png) 



![image-20220905160310376](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905160310376.png) 



BeanFactoryPostProcessor是用来处理我们的BeanFactory的，而BeanPostProcessor是用来处理我们的bean对象的。

当我们bean实例化完成之后，我们就要再对我们的BeanPostProcessor进行处理了。虽然在前期处理的时候往里面添加了很多BeanPostProcessor，但是只是为了后面做准备的。



BeanFactoryPostProcessor我们在进行修改的时候，修改的是BeanFactory里面存在的所有对象，而BeanDefinitionRegistryPostProcessor是用来修改我们的BeanDefinition的，两个东西锁针对的对象是不同的。



![image-20230201205333800](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230201205333800.png) 





### 优先排序及回答问题

![image-20220905173349232](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905173349232.png) 



获取了两遍的集合 是为什么？

![image-20230202082824372](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202082824372.png) 

![image-20230202082837780](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202082837780.png) 



**为什么再处理**

因为在这之间执行了invokeBeanDefinitionRegistryPostProcessors() 里面有可能会有新增的BeanDefinitionRegistryPostProcessor

![image-20230202083345882](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202083345882.png) 

前置条件没有满足的时候，就需要这一步骤去操作

![image-20220905202820434](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905202820434.png) 



![image-20230202083558156](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202083558156.png) 



后续还是会执行invokeBeanDefinitionRegistryPostProcessors方法，后续就不处理了吗？	

![image-20230202083756791](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202083756791.png) 

后续会有收尾工作执行



排序比较器

![image-20230202084726351](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202084726351.png) 

![image-20230202084740558](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202084740558.png) 

![image-20230202084747294](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202084747294.png) 



![image-20230202085029525](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202085029525.png) 



调用执行postProcessBeanFactory方法（上面执行的都是postProcessBeanDefinitionRegistry()方法） 先调用子类统一方法执行再调用父类的统一方法执行。

![image-20230202085107746](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202085107746.png) 

![image-20230202085118778](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202085118778.png) 



regularPostProcessors集合只有一个地方用到，如果不是BeanDefinitionRegistryPostProcessor类型的接口的话就把他加进去

![image-20230202085446682](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202085446682.png) 

 registryProcessors集合是为了方便我们统一执行BeanDefinitionRegistryPostProcessor里面的postProcessBeanFactory方法





刚刚获取的操作的都是BeanDefinitionRegistryPostProcessor，现在我们应该要处理BeanFactoryPostProcessor，我们在对他进行操作的时候也要遵循刚刚的处理流程。

![image-20230202085925334](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202085925334.png) 





### 为什么再处理？

再处理是否都是处理BeanFactoryPostProcessor类型接口？



如果一个bean实现了BeanDefinitionRegistryPostProcessor 里面会处理两个方法，在上面的时候会执行registry的方法，post的方法会扔到后面去执行。

![image-20230202195130541](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202195130541.png) 

![image-20230202195141709](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202195141709.png) 



但是一个bean对象只实现了BeanFactoryPostProcesoor没有实现BDRPP，那么到后面就找不到这些东西了。 

所以需要在后面再处理BeanFactoryPostProcessor，不然他们的方法就没东西执行了

![image-20230202194711453](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202194711453.png) 



其实就是先处理BeanDefinitionRegistryPostProcessor，因为里面包含两个方法，一个是自己里面的postProcessBeanDefinitionRegistry()，还有一个就是继承了BeanFactoryPostProcessor接口的postProcessBeanFactory()方法，但是又考虑到如果一个类只实现了BeanFactoryPostProcessor接口的话postProcessBeanFactory方法没东西去执行，所以在后面又处理了一下postProcessBeanFactory()方法。



#### *ConfigurationClassPostProcessor 

该类是一个后置处理器的类，主要功能是参与BeanFactory的建造，主要功能如下
   1、解析加了@Configuration的配置类
   2、解析@ComponentScan扫描的包
   3、解析ComponentScans扫描的包
   4、解析*@Import注解

![image-20230202195859072](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202195859072.png) 

![image-20230202195949871](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202195949871.png) 



**那么ConfigurationClassPostProcessor他是如何被识别到的呢？**

当执行到prepareBeanFactory方法的时候，beanDefinitonMap里面已经包含了5个internel处理类了

![image-20230202201148401](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202201148401.png) 



那么这5个处理类要去哪里找他怎么弄进去的呢？

在解析这个标签的时候 component-scan的时候

![image-20230202201324054](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202201324054.png) 

component-scan为自定义标签

![image-20230202201816417](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202201816417.png) 

![image-20230202201931134](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202201931134.png) 



![image-20230202201918104](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202201918104.png) 



![image-20230202202740107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202202740107.png) 

![image-20230202202835500](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202202835500.png) 

![image-20230202202926216](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202202926216.png) 

![image-20230202203005506](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202203005506.png) 

![image-20230202203039012](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202203039012.png) 



**invokeBeanFactoryPostProcessors方法执行**

![image-20230202203219493](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202203219493.png) 



进到这里面的时候就相当于已经开始在执行postProcessBeanDefinitionRetistry方法了

![image-20230202203319966](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202203319966.png) 

![image-20230202203447501](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230202203447501.png) 





















## Spring的BeanFactoryPostProcessor的执行2 



### BeanFactoryPostProcessor再次讲解



**BeanFactoryPostProcessor可以理解为是一个增强类，也可以理解为是一个后置处理器**

![image-20220905222105034](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905222105034.png) 



BeanFactory里面的所有东西理论上只要我们实现了**BeanFactoryPostProcessor**这个接口我们都是可以改的

他有一个子接口为**BeanDefinitionRegistryPostProcessor**



最终会调用这个方法来执行postProcessBeanFactory

![image-20230203084936087](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203084936087.png) 



![image-20220905222443349](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905222443349.png) 





如果手动添加自定义实现了**BeanFactoryPostProcessor**接口，那么像让Spring识别到的话有两种方式：

1、定义在Spring的配置文件中，让Spring自动识别

2、调用具体的addBeanFactoryPostProcessor方法



1、他会把他生命成一个beanDefinition此时就把他转配进去

![image-20230203085337605](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203085337605.png) 

2、当我们自定义MyClassPath的时候customizeBeanFactory方法中可以调用父类方法addBeanFactoryPostProcessor添加

![image-20230203085441951](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203085441951.png) 

两种方式任选其一



完整的图

![image-20230203085723250](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203085723250.png) 











![image-20220905224409423](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220905224409423.png) 

如果归属于BeanDefinitionRegistryPostProcessor这个子类，就会优先调用postProcessBeanDefinitionRegistry方法，先让这个方法开始执行，如果没有的话那么就剩下postProcessBeanFactory() 方法，这个方法可以放到后面一起处理，而不用挨个的去调用。



![image-20220906081637534](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906081637534.png) 

![image-20220906082259610](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906082259610.png) 



如何验证上图呢，首先就要先实现BeanDefinitionRegistryPostProcessor的子类，只有这样的子类在进行识别的时候才能找到对应的处理逻辑。

![image-20220906085015444](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906085015444.png) 

![image-20220906085032861](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906085032861.png) 

![image-20230203193224945](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203193224945.png) 



 父类BeanFactoryPostProcessor处理的时候只要实现一个方法，而BeanDefinitionRegistryPostProcessor子类实现了他，则要完成两个方法的实现。![image-20230203193335167](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203193335167.png) 

所以当我们进行判断的时候，如果你属于BeanDefinitonRegistryPostProcessor的时候会优先调用postProcessorBeanDefinitionRegistry方法，先让他开始执行。而postProcessorBeanFactory是放到后面一起执行的。



执行实现了BeanDefinitionRegistryPostProcessor的接口类，在整个容器工厂中通过类型查找的。

![image-20230203193910529](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203193910529.png) 





![image-20230203194405062](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203194405062.png) 

![image-20230203195035623](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203195035623.png) 

1、先找到BeanDefinitionRegistryPostProcessor对应的接口

![image-20230203195015259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203195015259.png) 



### postProcessBeanDefinitionResistry处理逻辑



**接上节，为什么要重复识别？**

![image-20230203195304131](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203195304131.png) 



为了防止在执行postProcessorBeanDefinitionRegistry方法的时候又有别的BeanDefinitionRegistryPostProcessor相关类在里面被加进去。在该方法中怎么加进去的呢？



第一种设置

![image-20230203195710036](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203195710036.png) 



invokeBeanFactoryPostProcessor需要先进行实例化才能再执行的

**疑问：他是如何去调用postProcessBeanFactory方法的呢？他不应该是createBean才实例化吗？没有实例化是怎么去调用方法的？**

getBeanNamesForType返回的是名称，而有了整个名称的时候我们调用了beanFactory.getBean方法

![image-20220906085358862](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906085358862.png) 

有执行实例化操作

![image-20230203203313531](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230203203313531.png) 

首先获取到了这些容器的名称，然后通过getBean方法去出发实例化（他里面会调用createBean方法）。

invokeBeanFactoryPostProcessor需要先进行实例化才能再执行的，而实例化就是再getBean那里触发的。















### postProcessBeanFactory



regularPostProcessors 存的是add进去的BFPP

  ![image-20220906133800045](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906133800045.png)



![image-20220906135243447](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220906135243447.png)

   

getBeanNameForType在BeanDefinitionRegistryPostProcessor和BeanFactoryPostProcessor执行了多次就不会重复获取相同的，我们在下面实现了BeanFacotryPostProcessor的时候他就不会重复增加其他的BeanFactoryPostProcessor了吗？

![image-20230205110658286](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230205110658286.png) 

但是真实情况下面是没有的



我们要看传入的参数

![image-20230205111018163](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230205111018163.png) 

 ConfigurableListableBeanFactory这个对象是不能添加BeanFactoryPostProcessor的，他里面没有beanFacotryPostProcessor这些东西，所以他是没办法往里面添加的。所以他不会新增中间任何处理的BFPP。



子类BeanDefinitionBeanPsotProcessor可以添加bean的定义，包括BFPP，父类的定义拿到的是工厂的定义进行修改。

![image-20230205111749429](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230205111749429.png) 





为什么这两行代码重复执行？

![image-20230205113920660](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230205113920660.png) 

如果我们实现了PriorityOrdered，他算不算Ordered类的？











### BeanDefiniton1



![image-20230206142926378](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206142926378.png) 

在postProcessorBeanFactory方法中修改对象属性值

```java
public class MySecondBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println(this.getClass().getName() + "执行postProcessBeanDefinitionRegistry方法");

		// 第一种设置方法
		// bean的名称，拿到BeanDefinition
//		 registry.registerBeanDefinition("loethin", new RootBeanDefinition(Teacher.class));
		//第二种设置方法
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(MySecondSelfBeanDefinitionRegistryPostProcessor.class);
		bdb.addPropertyValue("name", "铁哥们");
		registry.registerBeanDefinition("teacher", bdb.getBeanDefinition());
		System.out.println(this.getClass().getName() + "执行了postProcessBeanDefinitionRegistry 方法");
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println(this.getClass().getName() + "执行postProcessBeanFactory方法");
		BeanDefinition msb = beanFactory.getBeanDefinition("teacher");
		msb.getPropertyValues().getPropertyValue("name").setConvertedValue("不是铁哥们了");
		System.out.println("执行 postProcessBeanFactory 修改name的值");
	}
}
```

![image-20230206143520900](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206143520900.png) 



注解配置文件混合使用的情况下

```java
//如果想让当前这个类被识别到的话应该怎么做？
@Component
public class PersonService {
}

//添加ComponentScan扫描
<!--为什么开启这个的时候就能够识别到自己注册的类呢？-->
<context:component-scan base-package="com.msb"></context:component-scan>
```





**BeanDefinition是一个接口：包含了常规的Bean定义信息，GenericBeanDefinition、RootBeanDefinition**



![image-20230206144845306](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206144845306.png) 

注意：如果我们识别了一个被扫描的组件的话，当前这个组件的上面还应该包含一些其他的相关信息。

![image-20220907082856213](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907082856213.png) 

注解扫描类

![image-20230206145131338](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206145131338.png) 



扫描注册包含注解的类，但是这只是第一步加载

![image-20230206145354644](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206145354644.png) 

![image-20230206145444132](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206145444132.png) 

![image-20220907085456042](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907085456042.png) 



![image-20220907083608028](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907083608028.png) 

当上面的代码识别完Component注解类之后，后续还需要再进行其他注解的一些识别，因为一个类上面不止是只能放一个注解的。

![image-20220907083553384](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907083553384.png) 



其他的注解都是通过**ConfigurationClassPostProcessor**这个来实现的

![image-20220907083742580](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907083742580.png) 

 



 **org.springframework.context.annotation.internalConfigurationAnnotationProcessor**

![image-20230206150017516](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206150017516.png) 

![image-20230206150145507](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206150145507.png) 

![image-20230206150210723](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206150210723.png) 

![image-20230206150541810](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206150541810.png) 

![image-20230206150846963](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206150846963.png) 

**holder其实就是一个BeanDefinition的包装类，里面多包涵了两个信息，是beanNam和aliases。**

![image-20230206151316909](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206151316909.png) 

![image-20230206151347722](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206151347722.png) 

![image-20230206151555977](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206151555977.png) 



在Spring中，定义类的名字的时候是有一个规范的

![image-20230206151755185](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206151755185.png) 

![image-20230206151808785](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206151808785.png) 

![image-20230206152043071](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206152043071.png) 







### BeanDefinition2

![image-20230206225250103](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206225250103.png) 

![image-20230206225312161](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206225312161.png) 

![image-20230206225453190](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206225453190.png) 

![image-20230206225512303](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206225512303.png) 

![image-20220907212442049](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220907212442049.png) 



![image-20230206230841232](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206230841232.png) 

![image-20230206230612363](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206230612363.png) 

![image-20230206230914963](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206230914963.png) 





其他注解的处理都比较好理解，但是@Import会比较费解一些。

![image-20230206231107064](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206231107064.png) 





SpringBoot源码  ![image-20230206232119801](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230206232119801.png)















## Spring的 ConfigurationClassPostProcessor的讲解





### ConfigurationClassPostProcessor解析1

他是一个配置类的后置处理器

当我们开启@ComponentScan这个注解的时候，前期会自动注入一个internalConfigurationAnnotationProcessor类。

 ConfigurationClassPostProcessor优先执行的方法是 postProcessorBeanDefinitionRegistry

![image-20220908082651982](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220908082651982.png) 



![image-20230207084351177](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207084351177.png) 

![image-20230207084409493](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207084409493.png) 

![image-20230207084421556](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207084421556.png) 

![image-20230207084438365](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207084438365.png) 



![image-20220908083147214](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220908083147214.png) 



**如果是从配置文件中直接读的BeanDefinition那么他的名字叫GenericBeanDefinition，如果是以注解的方式注册进来的话就叫做ScannedGenericBeanDefinition**



**获取相关元数据对象AnnotationMetadata**

![image-20230207085930560](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207085930560.png) 



![image-20230207085823111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207085823111.png) 

![image-20230207090316392](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207090316392.png) 

![image-20230207090342775](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207090342775.png) 



总体步骤：

1、是否有@Configuration

2、是否包含有@ComponentScan、@ComponentScan、@Import、@ImportResource

3、是否包含有@Bean



往当前BeanDefinition里面设置一个定义信息，

![image-20220908084539661](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220908084539661.png)

![image-20230207090723094](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207090723094.png) 



![image-20220908084558929](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220908084558929.png)  



**得到上面的集合之后，后续就是对注解中的属性参数做相关处理解析工作了**

![image-20230207091123621](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207091123621.png) 



![image-20230207202811100](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207202811100.png) 

![image-20230207203455830](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207203455830.png) 



![image-20230207203623521](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207203623521.png) 

![image-20230207204416804](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207204416804.png) 



开始解析注解的工作

![image-20230207204817000](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207204817000.png) 

![image-20230207205128148](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207205128148.png) 

![image-20230207205151944](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207205151944.png) 



是否被注解@Conditional(xxxx.class)修饰， 当前类是否要跳过解析

![image-20230207205215036](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207205215036.png) 





**对当前类相关的注解都进行解析工作**

![image-20230207205913139](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207205913139.png) 

![image-20230207210121810](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230207210121810.png) 





### 内部类

**类里面包含的内部类中加了注解，并且类还能无线套娃。**

![image-20220908091050854](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220908091050854.png) 

会通过递归去处理这样的情况



在这个类里面我们可以包含自己的类





### ConfigurationClassPostProcessor解析2



processConfiguration和前面parse 方法里面的processConfiguration方法是一样的，相当于是一个递归操作

![image-20230208083326924](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208083326924.png) 

![image-20230208083437819](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208083437819.png) 



我们在定义注解的时候，注解里面锁包含的类有可能也包含注解，所以需要递归去调用。他是一个层层迭代的关系。

![image-20220909082610573](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220909082610573.png) 



解析@PropertySource注解

![image-20230208083739597](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208083739597.png) 

举例

![image-20230208083855231](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208083855231.png) 



![image-20230208084252200](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084252200.png) 



 ![image-20230208084542997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084542997.png) 



ComponentScan解析

![image-20230208084800241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084800241.png) 

![image-20230208084813740](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084813740.png) 

![image-20230208084834049](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084834049.png) 

![image-20230208084934163](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208084934163.png) 

​	 

 通过上一步扫描包com.msb  有可能扫描粗来的bean中可能也添加了ComponentScan或者ComponentScans注解，所以这里需要循环遍历，进行 parse()的递归，继续解析，直到解析出的类上有ComponentScan或者ComponentScans 

![image-20230208085152211](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085152211.png) 



**解析Import标签**

![image-20230208085315015](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085315015.png) 

里面的方法也是一个递归的调用

![image-20230208085412037](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085412037.png) 

![image-20230208085608061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085608061.png) 



执行processImport方法的时候，里面的getImports()方法里面使用的collectImports方法也是有递归在里面的

![image-20230208085719891](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085719891.png) 

![image-20230208085747128](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085747128.png) 

![image-20230208085757106](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085757106.png) 



接口上也能定义@Bean的注解了

![image-20230208085900506](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208085900506.png) 

![image-20230208090013065](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208090013065.png) 









### MyComponentScan



第一个先处理外部注解的工作，再处理内部的

![image-20230208125719800](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208125719800.png) 

![image-20230208130425595](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208130425595.png) 



@Configuration里面会包含一个@Component这个注解的

![image-20220909083544271](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220909083544271.png) 

![image-20230208130752538](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208130752538.png) 

![image-20230208130802108](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208130802108.png) 

![image-20230208131853700](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208131853700.png) 



![image-20230208132233607](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132233607.png) 



![image-20230208132250603](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132250603.png) 

扫描代码很简单

![image-20230208132348919](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132348919.png) 

![image-20230208132421718](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132421718.png) 

![image-20230208132550537](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132550537.png) 

![image-20230208132508533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208132508533.png) 







### ConfigurationClassPostProcessor解析3



加载外部文件

![image-20230208201039988](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201039988.png) 

![image-20230208201105673](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201105673.png) 

![image-20230208201152253](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201152253.png) 



![image-20230208201159688](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201159688.png) 

![image-20230208201428452](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201428452.png) 

**上述其实只是把外部文件加到集合里面了，并没有对里面的值进行相关的处理**

![image-20230208201913679](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208201913679.png) 

![image-20230208203248566](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208203248566.png) 



**如果没有@Configuration 注解的话，@PropertySource 还生效吗**

**不能，不加Configuration注解的话Spring是进不去的**



**加了**

![image-20230208203840729](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208203840729.png) 



**去除**

![image-20230208203907217](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208203907217.png) 

![image-20230208203954333](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208203954333.png) 







#### @Bean是如何处理的



 ![image-20220911155554990](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220911155554990.png) 

![image-20230208205535633](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208205535633.png) 

![image-20230208205552274](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208205552274.png) 

![image-20230208205636647](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208205636647.png) 

![image-20230208210030963](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208210030963.png) 



**解析所对应的被注解修饰的BeanDefinition**

**解析我们所需要的被注解给修饰的BeanDefinition**![image-20230208210121352](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230208210121352.png) 

整体工作最终还是为了完成我们的BeanDefinition，在实例化之前，我们必须把这个应用程序所存在的所有BeanDefinition都解析完成。

每次在完成对象的实例化的时候，我们必须要有BeanDefinition，如果没有是完成不了的



xml文件开始进行解析工作->注解->解析加载











## Spring源码-注册BeanPostProcesser



### Import到底是如何解析的



**ConfigurationClassPostProcessor**



**递归处理**（在@SpringBootApplication里面中的@EnableAutoConfiguration，这两个里面都有@Import注解）

![image-20230209084759589](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209084759589.png) 

![image-20230209084557920](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209084557920.png) 



getImports()

![image-20230209090524067](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209090524067.png) 





**获取当前有哪些类需要进行相关的导入操作**

![image-20230209090735256](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209090735256.png) 

![image-20230209090744365](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209090744365.png) 



imports用来存放import注解的类，visited 用来存放递归关系的



![image-20220912113055599](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912113055599.png) 



SpringBoot

对启动类的每个注解递归的往里查，看看有没有@Import注解的类

![image-20230209090930711](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209090930711.png) 





通过ImportSelector导入key，去spring.factories文件中找到对应的value 导入我们具体的配置类

![image-20230209091431696](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209091431696.png) 

![image-20230209091451414](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209091451414.png) 

![image-20230209091510165](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209091510165.png) 



![image-20220912143838176](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912143838176.png) 



processImports：导入具体的配置类，同时完成了具体的实例化工作。

![image-20230209130231121](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209130231121.png) 

![image-20230209130255565](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209130255565.png) 

该类下的jar包中的META-INF的spring.factories文件

![image-20230209130346728](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209130346728.png) 

![image-20230209130433770](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209130433770.png) 

EnableAutoConfiguration

![image-20230209130443410](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209130443410.png) 

![image-20230209131151339](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131151339.png) 

**AutoConfigurationImportSelector 为了导入我们的一些配置类**

getCandidateConfigurations：当我们在处理BFPP的时候里面有一个ConfigurationPostProcessor这个类主要是用来处理我们一些注解的解析工作，里面包含了@Component、@PropertySource、@ComponentScan、@Bean、@Import， 当我们在解析@Import的时候我们会在我们的启动类开始一步一步递归的查找，这个时候里面会识别到一个@ImportSelector类，在这个类进行解析的时候会有一个延迟加载的属性，在延迟加载的时候会有一个getImports方法，在这个方法里面会获取到一个AutoConfigurationEntry这个对象，在获取Entry对象的时候会调用里面的getCandidateConfigurations方法，此时能把配置文件所对应的属性值都加载回来，来完成整个自动装配的解析流程。



![image-20230209131006254](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131006254.png) 



![image-20230209131349958](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131349958.png) 

ImportSelector

![image-20230209131212515](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131212515.png) 

此接口是Spring中导入外部配置的核心接口，根据给定的条件（通常是一个或多个注解属性）判断要导入哪个配置类如果该接口的实现类同时实现了一些Aware接口，那么在调用selectImports方法之前先调用上述接口中的回调方法如果需要在所有的@Configuration处理完再导入，可以实现DeferredImportSelector。

![image-20230209131516172](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131516172.png) 

![image-20230209131711140](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131711140.png) 

processImports：具体作用是为了导入一些配置类，同时完成了具体类的实例化功能。



![image-20230209131915127](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209131915127.png) 



![image-20230209204911516](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209204911516.png) 

![image-20230209204928264](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209204928264.png) 

![image-20230209205141860](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205141860.png) 

![image-20230209205210162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205210162.png) 

![image-20230209205255051](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205255051.png) 

AutoConfigurationEntry

![image-20230209205334420](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205334420.png) 





![image-20230209205428499](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205428499.png) 

![image-20230209205612094](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205612094.png) 

![image-20230209205620473](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205620473.png) 

![image-20230209205644218](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205644218.png) 



加载Spring文件

![image-20230209205726947](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205726947.png) 

![image-20230209205740643](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205740643.png) 

cache

![image-20230209205816816](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205816816.png) 



cache中有13个对应的元素，我们可以通过这个配置把它们都取出来

![image-20230209205916582](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209205916582.png) 

得出结果

![image-20230209210121528](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209210121528.png) 



**自定义加载类**

```java
package com.mashibing;

import org.springframework.context.annotation.Configuration;

/**
 * @author LYX
 * @description
 * @date 2022/9/12 14:58
 */
@Configuration
public class MyAutoConfiguration {
}

```

![image-20230209210825883](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209210825883.png) 

定义factories

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 com.mashibing.MyAutoConfiguration
```

![image-20230209211234299](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209211234299.png) 

![image-20230209211329877](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209211329877.png)



实现类

![image-20230209212255635](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230209212255635.png) 



### registerBeanPostProcessors



![image-20220912153932806](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912153932806.png) 

 获取到internal的两个对象![image-20220912160035260](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912160035260.png) ![image-20220912155635518](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912155635518.png)

根据下面的名词，识别到具体对应的类

![image-20220912155657427](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912155657427.png) 

![image-20220912155721238](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912155721238.png) 



![image-20230210084834300](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210084834300.png) 





在Spring中有两种类，一种是Spring自己的还有一种是我们定义的，所以Spring会提供属性值做校验区分

![image-20230210085636581](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210085636581.png)

其实只是做了一些记录信息，没有任何的实际意义

![image-20230210085740810](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210085740810.png) 

![image-20230210085756126](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210085756126.png) 

![image-20230210091514969](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210091514969.png) 



![image-20230210130824686](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210130824686.png)



BeanPostProcessor所针对的处理对象是bean，里面有两个方法一个是before 一个是after

![image-20220912162344409](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912162344409.png) 



BeanPostProcessor所针对的处理对象是bean对象

实例化开始、初始化结束、完成使用、销毁



销毁相关：![image-20220912162658864](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912162658864.png) 

**DestructionAwareBeanPostProcessor 销毁前调用的**

![image-20230210131453775](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210131453775.png) 

![image-20230210131812135](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230210131812135.png) 





对于要合并处理BeanDefinition的时候要做的处理工作。

postProcessMergedBeanDefinition合并BeanDefinition，合并相关bean 的定义信息

![image-20230213143244656](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213143244656.png) 

清空BeanDefinition信息

![image-20230213143437389](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213143437389.png)

**为什么要合并？ 是因为我们在存在一个BeanDefinition的时候一个bean他是可能存在父类的，有时候我们需要把父类和子类的一些信息进行相关的合并操作。**



寻找整个里面存在的构造器

![image-20230213145025595](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213145025595.png)

解决循环依赖问题

![image-20230213145018338](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213145018338.png)



![image-20230213145651804](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213145651804.png)













## Spring的消息资源和监听器的初始化



### 回顾及initMessageSource



初始化消息资源

![image-20220912190644681](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220912190644681.png) 

该方法会在SpringMVC的时候通过国际化的代码重点讲。

![image-20230213225250449](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213225250449.png)

![image-20230213225953338](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213225953338.png) 

![image-20230213230034299](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213230034299.png) 



![image-20230213230342533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230213230342533.png)



### 观察者模式



![image-20230214082705650](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214082705650.png) 





原始观察者模式     被观察者、观察者

被观察者中需要存储一个观察者的一个集合，执行不同方法的时候要调用观察者的方法进行处理

观察者：里面需要定义一个方法，看到被观察者不同行为的时候触发的反应

![image-20220916085516356](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220916085516356.png) 

 

![image-20220916085353845](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220916085353845.png) 



 被观察者---------------------------------------->观察者

应该存储一个观察者集合						执行不同方法的时候，要调用观察者的方法进行处理

![image-20230214083255747](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214083255747.png) 





监听事件、监听器、多播器、监听源



### 观察者模式代码演示



```java
package com.msb.secondobserve;

/**
 * 被观察者，可能会被n多个观察者观察
 *
 * @author LYX
 * @description
 * @date 2023/2/14 8:39
 */
public interface SObserable {

	public void addObserver(SObserver observer);

	public void deleteObserver(SObserver observer);

	void notifyObserver(String str);
}

package com.msb.secondobserve;

/**
 * @author LYX
 * @description
 * @date 2023/2/14 8:41
 */
public interface SObserver {

	public void make(String str);

}

package com.msb.secondobserve;

/**
 * @author LYX
 * @description
 * @date 2023/2/14 8:52
 */
public class SGoodMan implements SObserver{
	@Override
	public void make(String str) {
		System.out.println("开始行动------------" + str);
	}
}

package com.msb.secondobserve;

/**
 * @author LYX
 * @description
 * @date 2023/2/14 8:53
 */
public class SGoodMan2 implements SObserver {
	@Override
	public void make(String str) {
		System.out.println("goodman2开始行动-----------" + str);
	}
}


package com.msb.secondobserve;

import java.util.ArrayList;

/**
 * @author LYX
 * @description
 * @date 2023/2/14 8:41
 */
public class SBadMan implements SObserable {

	private ArrayList<SObserver> observers = new ArrayList<>();

	@Override
	public void addObserver(SObserver observer) {
		observers.add(observer);
	}

	@Override
	public void deleteObserver(SObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObserver(String str) {
		for (SObserver sObserver : observers) {
			sObserver.make(str);
		}
	}

	public void run() {
		System.out.println("罪犯要逃跑了");
		this.notifyObserver("追击罪犯");

	}


	public void play() {
		System.out.println("罪犯在玩");
		this.notifyObserver("不要做任何事情");
	}
}


package com.msb.secondobserve;

/**
 * @author LYX
 * @description
 * @date 2023/2/14 8:54
 */
public class Test {
	public static void main(String[] args) {
		//创建被观察者
		SBadMan sBadMan = new SBadMan();
		//创建观察者
		SGoodMan sGoodMan = new SGoodMan();
		SGoodMan2 sGoodMan2 = new SGoodMan2();


		//向被观察者中添加观察者
		sBadMan.addObserver(sGoodMan);
		sBadMan.addObserver(sGoodMan2);

		//等待某些罪犯出发行为
		sBadMan.run();
	}
}


==========================================================
结果:
罪犯要逃跑了
开始行动------------追击罪犯
goodman2开始行动-----------追击罪犯

```







### 事件驱动

![image-20230214131431164](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214131431164.png)

![image-20230214131 408222](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214131408222.png)

```java
package com.msb.secondobserve.test02;

import java.util.Observable;

/**
 * @author LYX
 * @description Java util里面的观察者类
 * @date 2023/2/14 8:59
 */
public class SBadMan extends Observable {

	@Override
	public void notifyObservers() {
		super.notifyObservers();
	}
}

```



原生的观察者模式其实很好理解，但是Spring中的观察者，加了他自己的东西



在传统的观察者模式中会有两个对象，一个是 被观察者，还有一个观察者就可以了。



Spring中定义了   

事件（被观察者的动作）、监听器、多播器、事件源



![image-20230214131709489](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214131709489.png) 



#### 事件

Event，把被观察者具体要执行的动作拿出来当做了一个事件  

 

**具体要执行的动作**

![image-20220918141219611](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918141219611.png) 



#### 监听器

观察者，可能存在多个，接收不同的事件来做不同的处理动作

![image-20220918141526661](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918141526661.png) 



#### 多播器

可能会有n多个的监听器，多播器是为了通知n多个监听器去执行处理

把被观察者遍历观察者通知消息的操作拿出来委托给一个多播器来进行消息通知，或者说通知观察者进行不同的操作。

替换原来的for循环



#### 事件源

发表时间的源头。

谁来调用或者执行发布具体的事件。



**将普通的观察者模式解耦**

把事件先定义出来，把事件能做什么操作拿出来。把监听器作为一个观察者，后面由监听器单独来处理观察者要做的一些处理操作，多播器来完成我们的处理操作，相当于原来被观察者中的for循环。事件源来进行发布事件。



**时间驱动逻辑执行过程：**

1、事件源来发布不同的事件

2、事件源发布之后本来应该交给监听器来执行监听操作的，但是 当发布事件之后会调用多播器的方法来进行事件广播操作，由多播器去触发具体的监听器去执行操作

3、监听器接收到具体的事件之后，可以验证匹配是否能够处理当前事件，如果可以，直接处理，如果不行，不做任何操作。



**实际代码处理：**

1、**提前准备好N多个事件**

2、**初始化多播器（创建多播器对象，此多播器对象中应该包含一个监听器的集合）  initApplicationEventMulticaster**

3、**准备好一系列的监听器**    

4、**向多播器中注册进去已有的监听器   registerListeners**

5、**准备事件发布，来通知多播器循环调用监听器进行相关的逻辑处理工作   publishEvent** 



![image-20220918195535353](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918195535353.png) 





### 事件驱动代码演示



**ApplicationEvent**

![image-20230214194946970](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214194946970.png)



首先创建多播器对象

![](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214195801110.png) 





 **在这之前我们没有定义过applicationEventMulticaster**

![image-20230214200433170](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214200433170.png)



**SimpleApplicationEventMulticaster的父类 AbstractApplicationEventMulticaster类中有一个add方法，方法中会添加监听器类。**



![image-20230214200912249](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214200912249.png)



![image-20230214200937840](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214200937840.png)



![image-20230214201004437](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214201004437.png)



![image-20220918180311527](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918180311527.png)

![image-20220918180316580](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918180316580.png) 





向多波器注册进去已有的监听器registerListeners

![image-20220918180911968](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918180911968.png) 



**完成监听器的添加操作**

![image-20220918181224080](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918181224080.png) 



这两个集合的区别就是， 我们可以有一个对象集合，也可以有一个名字集合，我们能够通过名字集合来获取相关的实例。

![image-20220918181208533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918181208533.png) 

![image-20230214201755549](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214201755549.png) 



![image-20230214201951471](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214201951471.png) 



![image-20230214202135412](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214202135412.png)

![image-20230214202121315](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214202121315.png) 

与之前的BFPP和BPP是一样的处理，可以创建对象集合也可以创建名字集合，通过名字集合遍历进行实例化填充进去的这样两种方式



后续的源码处理也是这样子的

![image-20220918181609855](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918181609855.png) 



![image-20230214202549194](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214202549194.png) 



![image-20230214202818196](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214202818196.png) 



修改bean顺序

可以通过beanFactory.addBeanPostProcessor方法来修改顺序，或者删了再加都行





![image-20230214203412750](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214203412750.png) 



事件源来发布事件

![image-20220918185440413](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918185440413.png) 

![image-20230214203626540](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214203626540.png)

![image-20230214203722966](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230214203722966.png)



定义了实体的具体执行过程

不管是使用上面的方式还是下面的方式，最终调用的方法都是invokeListener

![image-20220918185823413](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918185823413.png) 





![image-20220918190037020](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220918190037020.png) 



**initApplicationEventMulticaster和applicationEventMulticaster这两个是同一个东西吗？**

不是同一个，他们的作用域和作用空间都是不一样的。





**initialMulticaster初始化  他是什么时候完成具体的注册的**

![image-20220914101256022](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220914101256022.png) 



![image-20220914101703517](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220914101703517.png) 



 ![image-20220914101929552](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220914101929552.png)



### 总结及问题解答



![image-20230215084504847](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215084504847.png)

![image-20230215084545785](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215084545785.png)



![image-20230215085059851](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085059851.png) 

![image-20230215085220506](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085220506.png)

![image-20230215085202481](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085202481.png)



当我们发现在当前类突然出现了一个属性之后，一定是在当前这个类完成初始化的时候做的事情

![image-20230215085328155](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085328155.png)

所以我们需要看看当前这个类EventPublishingRunListener是在什么时候完成具体的实例化操作的，所以我们需要往回看

![image-20230215085456997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085456997.png) 



SpringApplicationRunListener在前面有进行实例化操作

![image-20230215085559348](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085559348.png)



![image-20230215085714137](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085714137.png)

![image-20230215085845552](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085845552.png)

![image-20230215085906557](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085906557.png)

![image-20230215085918011](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230215085918011.png)



![image-20230216083330543](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216083330543.png)

![image-20230216083354257](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216083354257.png)

![image-20230216083457292](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216083457292.png)





## Spring的bean创建流程（一）



### finishBeanFactoryInitialization方法

完成整个bean的实例化环节

![image-20220919101507782](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919101507782.png) 



**getBean->doGetBean->createBean->doCreateBean**



在这个方法之前哪些地方调用了getBean()  ？

**BeanFactoryPostProcessor、BeanPostProcessor**



![image-20220919102424574](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919102424574.png) 



 

![image-20230216084551067](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216084551067.png)



**转换服务**

![image-20230216085300535](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216085300535.png) 



 转换器提前储备![image-20220919102555906](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919102555906.png) 

![image-20230216085410415](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216085410415.png) 

**这些转换器都是Spring预先给我们提供好的**



![image-20230216085703504](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216085703504.png) 





Spring给我提供了三个converter转换的父类接口，我们可以通过这些接口来自定义类型转换

![image-20220919103504270](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919103504270.png) 



**自定义实现的时候需要用哪一个接口**

由S类型的数据，转换成T类型的数据

![image-20230216085902305](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216085902305.png)

 

**两个以上的对应类型转换**

![image-20230216090038281](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216090038281.png) 



![image-20230216090051243](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216090051243.png)

![image-20230216090122623](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216090122623.png) 



多对多

![image-20230216090432038](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216090432038.png) 

我们对应的一个子类型

![image-20230216090455249](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216090455249.png)



Converter：一对一的类型转换；GenericConverter：一对多的类型转换；ConverterFactory多对多的类型转换。



conditional 是有要多他进行条件判断，进行一个match的判断

![image-20230216131733216](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216131733216.png)





**自定义  添加Converter官网实例：**

![image-20220919105253914](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919105253914.png) 

![image-20230216194140239](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216194140239.png)

```java
	<!--该对象交由Spring来进行管理-->
	<!--	<bean class="com.msb.MyBeanFactoryPostProcessor"></bean>-->
	<bean id="studentConverter" class="com.msb.secondselfConverter.StudentConverter"/>
	<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
		<property name="converters">
			<set>
				<ref bean="studentConverter"/>
			</set>
		</property>
	</bean>
```



![image-20230216194242605](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216194242605.png)



![image-20230216194832936](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216194832936.png) 

![image-20230216195516765](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216195516765.png) 



![image-20230216200006519](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216200006519.png) 





![image-20220919154747595](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919154747595.png) 



![image-20220919153058354](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919153058354.png)





### prelnstantiateSingletons类



![image-20230216200150255](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216200150255.png) 

![image-20230216210859570](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216210859570.png) 



BenDefinition

​	GenericBeanDefinition

​	RootBeanDefinition


![image-20230216211258348](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230216211258348.png) 

![image-20230217085024622](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085024622.png) 

![image-20230217085131720](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085131720.png)




mergedBeanDefinitions集合里面有值，那么证明他是在前面就已经放进去了，是在BDRPP里面放的

![image-20230217085239200](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085239200.png)





### finishBeanFactoryInitialization类

![image-20230217085401892](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085401892.png)


![image-20230217085421533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085421533.png)

![image-20230217085507961](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085507961.png) 


放缓存里，后续就不再需要经历一遍过程了。

![image-20230217125832061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217125832061.png)

mergedBeanDefinition一开始是没值的，从invokeBeanFactoryPostProcessors 进去


当第一次进来的时候mergedBeanDefinition 是什么值都没有的，意味着我们没有办法从缓存里面取了，意味着我们必须要自己去创建了

**不管怎么创建或者在哪里创建，我们最终都是要拿到的是RootBeanDefinition这个对象，必须要包含或者合并完父容器相关类型的数据，不然后续没有办法进行计算**


getMergedBeanDefinition方法的调用是一个递归的过程

![image-20230217130613894](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130613894.png)

![image-20230217130625570](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130625570.png)

![image-20230217130638511](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130638511.png)

![image-20230217130652055](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130652055.png)

![image-20230217130709029](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130709029.png)

![image-20230217130721499](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217130721499.png)

![image-20230217085731523](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085731523.png) 



![image-20230217085906325](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217085906325.png)

![image-20230217090121340](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217090121340.png) 


Spring中用了很多map结构，为了提高效率，把一些对象进行了缓存，不需要在后面再重复获取


![image-20230217090706057](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217090706057.png)

![image-20230217090130750](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217090130750.png)

![image-20230217090312401](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230217090312401.png)



### 缓存会不会引起缓存的泄露和getMergedLocalBeanDefinition



缓存对象的时候，要缓存怎么样的对象？

我们需要考虑好场景

如果对象的属性值一直都在变化，我们不要对他进行缓存

会引发内存泄露。



使用缓存是为了提高代码的利用率和效率，这只是一个思想，我们不能硬套



为什么要这样设计？为什么要取父的定义，取子的可以吗？



**刚刚在进行使用的时候，使用了递归的方式调用，问题是递归的过程是怎么样的？**

1、先从缓存中获取，缓存中有，直接返回。

2、缓存中没有，提前创建。

如果beanDefinitionMap中有包含parent的时候，我们就会把parent提前创建好，等到后面要使用的时候就可以直接拿去用了，不需要重新创建了。



**getMergedLocalBeanDefinition -> 他的意义在于，在实例化之前，要把所有的基础的BeanDefinition对象，转换成RootBeanDefinition对象进行缓存，后续在马上需要实例化的时候直接获取定义信息，而定义信息中如果包含父类，那么必须要先创建父类才能有子类，父类如果没有的话子类怎么创建。**







### BeanFactory和FactoryBean



必须要满足 非抽象、单例、非懒加载 这几个条件才会去创建

![image-20230218220806060](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218220806060.png)

![image-20230218215956179](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218215956179.png) 



**BeanFactory和FactoryBean**

**都是对象工厂，用来创建具体对象的。**



如果使用BeanFactory的接口那么必须要严格遵守SpringBean的生命周期接口，从实例化、到初始化、再到invokeAwareMethod.invokeInitMethod、before、after.......，像这样的一系列流程我们都必须要遵守。

此流程非常复杂且麻烦，如果需要一种更加便捷简单的方式创建，则需要使用FactoryBean这个接口，不需要遵循此创建的顺序。



#### FactoryBean



有3个方法getObjectType、isSingleton、getObject

![image-20220919155355939](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919155355939.png) 



getObjectType：返回类型

isSingleton：判断是否单例

getObject：直接返回对象



![image-20220919155924362](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919155924362.png) 

```java
public class SecondMyFactoryBean implements FactoryBean<User> {
	@Override
	public User getObject() throws Exception {
		return new User();
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
```





定义好之后如何交给Spring进行控制？

![image-20220919160158430](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919160158430.png) 

![image-20220919160227951](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919160227951.png) 



```xml
	<bean id="myFactoryBean" class="com.msb.secondFactoryBean.SecondMyFactoryBean">
	</bean>

```

![image-20230218222834159](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218222834159.png)



直接如上面那样获取是没用的，他会获取不到，我们需要添加& 地址符才能够获取，但是为什么呢 ？

![image-20230218222935472](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218222935472.png)

![image-20230218222958012](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218222958012.png) 

**当使用FactoryBean接口来创建对象的时候，我一共创建了几个对象？**

两个，第一个对象是实现了FactoryBean接口的子类对象，第二个是通过getObject方法返回的对象



两个对象是由谁来管理？我们自己还是Spring？



![image-20220919161712997](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919161712997.png) 

![image-20220919161753446](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919161753446.png) 



![image-20220919162052955](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162052955.png) 

创建对象为MyFactoryBean了（SecondMyFactoryBean只是我学习的次数，统一不看前面的次数）

![image-20230218223443073](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230218223443073.png) 



![image-20220919162555083](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162555083.png) 

![image-20220919162715634](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162715634.png) 

![image-20220919174516283](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919174516283.png) 

![image-20220919162909251](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162909251.png) 



![image-20220919162934922](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162934922.png) 



![image-20220919162947349](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919162947349.png) 



![image-20220919163143597](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919163143597.png) 

![image-20220919163159613](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919163159613.png) 



实现了FactoryBean接口的对象在一级缓存中，但是调用getObject所创建的对象是放到factoryBeanObjectCache里面的，没有在一、二、三级缓存中

![image-20220919163433660](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919163433660.png) 



![image-20220919163707842](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919163707842.png) 

**如果不是单例对象的话，那么需要每次调用的时候重新创建，缓存中不会保存当前对象。**





**getBean->doGetBean->createBean->doCreateBean**





我们在使用BeanFactory获取对象的时候必须要调用getObject方法，那么在什么吗时候调用的呢？

在下图 其实已经实例化完成了

![image-20230220143729981](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220143729981.png)

但是SingletonObjects中竟然没有User对象值

![image-20230220143958129](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220143958129.png)

![image-20230220143944592](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220143944592.png) 



执行完方法，我们回到下一步，进入到getBean方法中，我们传入的是&myFactoryBean这个值

![image-20230220144037132](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144037132.png)

![image-20230220144100224](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144100224.png)

![image-20230220144156778](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144156778.png) 

 transformedBeanName 方法将我们的&符给去除掉了，变成了myFactoryBean

![image-20230220144211617](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144211617.png)

![image-20230220144320319](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144320319.png)

回到原来的问题，User去哪里了？

重新执行main方法，将User对象通过getBean("myFactoryBean")出来看看是什么效果

```java
MyClassPathXmpApplicationContext mpa = new MyClassPathXmpApplicationContext("factoryBean.xml");
SecondMyFactoryBean myFactoryBean = (SecondMyFactoryBean) mpa.getBean("&myFactoryBean");
System.out.println(myFactoryBean);
User user = (User) mpa.getBean("myFactoryBean");
System.out.println(user.getName());
```



![image-20230220144723547](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144723547.png)

![image-20230220144747023](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144747023.png) 



![image-20230220144819982](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144819982.png)



![image-20230220144847541](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144847541.png)

![image-20230220144918559](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220144918559.png)

![image-20230220145251892](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220145251892.png)



![image-20230220145542981](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220145542981.png) 

会执行FactoryBean的getBean方法

![image-20230220145556059](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220145556059.png)

![image-20230220145753415](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220145753415.png)

放到具体的缓存对象中

在下图我们可以看到myFactoryBean指向的是User

![image-20230220145851174](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220145851174.png)



![image-20230220150007436](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150007436.png)

执行完后我们发现singletonObjects几种中并不存在User对象

![image-20230220150119949](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150119949.png)

放到factoryBeanObjectCache集合中

![image-20230220150210866](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150210866.png)

意味着以同一个名字在进行针对的时候，我们针对的对象是一模一样的，只不过获取的位置是不一样的，一个是从一级缓存获取，另外一个是从factoryBeanObjectCache中获取，相当于把对象独立存储了。

所以User也是Spring帮我们管理的，但是要注意的是存放对象的缓存集合不同，实现了FactoryBean接口的对象是在一级缓存，但是调用getObject获取的对象在factoryBeanObjectCache。虽然都是Spring帮忙管理，但是找到地方都是完全不同的。

![image-20230220150613826](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150613826.png)



那么再次获取的时候，我们是否还是重新创建还是从缓存中取呢？

![image-20230220150716132](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150716132.png) 

会直接从缓存中取，有这个对象了就直接返回了。

![image-20230220150938276](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220150938276.png) 

![image-20230220151040468](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220151040468.png)

但是如果我们把它改为非singleton的情况下还会往缓存里面放吗？

![image-20230220151710178](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220151710178.png)



![image-20230220152255712](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220152255712.png) 

直接干活，不put对象

![image-20230220152306019](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220152306019.png) 



如果是一个多例对象的话，Spring就不会帮我们管理了，需要每次调用的时候重新创建，缓存中不会保存当前对象







## Spring的bean创建流程（二）







### 回顾

 ![image-20230220193338088](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230220193338088.png)

#### finishBeanFactoryInitialization



getBean->doGetBean->createBean->doCreateBean



在Spring中创建对象的方式有这几种：new、反射、**factoryMethod、supplier**



**如果一个bean对象实现了FactoryBean接口，那么在整个spring的生命周期中会创建两个对象，第一个标识实现了FactoryBean接口的子类，由Spring帮我们创建，会调用getObject方法来返回一个对象，此对象的创建过程就是由getObjectForBeanInstance方法调用实现的。**

 

![image-20220919193744692](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919193744692.png) 



如果一个bean对象实现了FactoryBean接口，那么在整个Spring的生命周期中会创建两个对象，第一个表示实现了FactoryBean接口的子类，由Spring帮我们创建，此对象的创建过程是由getObjectForBeanInstance()方法来调用实现的。

![image-20230221083318097](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221083318097.png) 



在SpringMVC中父子容器的概念才很多

![image-20230221083834706](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221083834706.png) 

但是如果我们包含父子容器了，应该干什么事呢？

如果子里面没有就去父里面找。



将创建好的和没创建好的放到不同的集合里面

![image-20230221084059024](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221084059024.png) 

![image-20230221084138739](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221084138739.png) 



![image-20230221084320071](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221084320071.png)

**我们做操作的时候可以每次都根据beanName获取新的RootBeanDefinition，但是一种更有效率的方式是在第一次处理的时候放到缓存中，之后可以对缓存中的对象进行获取，不需要每次都创建新的。**

**可以在第一次处理的时候就放到缓存中，之后可以对缓存中的对象进行获取，不需要每次都创建新的。**

![image-20220919194201331](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919194201331.png) 



mergedBeanDefinitions在前面BFPP那块也有添加东西进去，通过getBeanNameForType 的时候赋值的。

![image-20230221084522829](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221084522829.png)



stale判断当前对象是否是新鲜值，如果是的话就直接用，不是的话就进行合并，变成新鲜的对象。







### getDependsOn和创建bean的实例对象



![image-20230221084934476](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221084934476.png) 

bean在创建的时候依赖于某个类

![image-20230221085605145](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221085605145.png) 



正常我们定义了多少个bean，就需要有几个bean 的创建，理论上来说他们谁先谁后是没有什么关系的，但是如果这个bean需要依赖于其他bean的时候怎么办？是不是意味着，其他bean必须要优先于当前bean来创建，就意味着要先创建好，创建好之后我们在看下面的bean 应该做如何的处理工作。

![image-20220919194934511](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919194934511.png) 





**ObjectFactory是一个函数式接口，当调用其中的getObject方法的时候，才会将实际传递的匿名内部类中的实现逻辑来进行执行**

**新生状态->开始创建->创建过程中->创建结束**

![image-20230221090109654](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221090109654.png) 

![image-20230221085954942](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221085954942.png) 

调用getObject() 执行函数式接口方法

![image-20230221090328987](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221090328987.png)



**新生状态->开始创建->创建过程中->创建结束后->完整对象**

**实例化-> 填充属性-> 执行aware接口方法->执行init方法->执行后置处理器方法**



![image-20220919200141504](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220919200141504.png) 



![image-20230221091234536](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221091234536.png) 



![image-20230221130933465](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221130933465.png)



![image-20230221131022082](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221131022082.png)



doPrivileged 权限验证 这个方法再java.sql的DriverManager中也有使用，他们这套实现机制都是一模一样的。

![image-20230221131611247](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221131611247.png)

![image-20230221131735608](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221131735608.png)

![image-20230221131901206](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221131901206.png) 



![image-20230221132007499](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132007499.png) 

![image-20230221132145595](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132145595.png)

beanDefinition检查

![](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132247071.png)

![image-20230221132517002](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132517002.png)

当我们拥有Class这个对象的时候，我们就可以对当前的对象里面的属性进行任意的修改了。

![image-20230221132557735](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132557735.png)



![image-20230221132845145](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132845145.png)



![image-20230221132908761](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221132908761.png)





### 创建Bean的实例对象



#### lookup-method



**prepareMethodOverrides()**

![image-20230221195652789](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221195652789.png)

![image-20230221195221948](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221195221948.png) 



```java
public class Fruit {
	public Fruit() {
		System.out.println("i got fruit");
	}
}

public class Apple extends Fruit{
	public Apple() {
		System.out.println("I got new Apple");
	}
}

public class Banana extends Fruit{
	public Banana() {
		System.out.println("I got new Banana");
	}
}

public abstract class FruitPlate {
	// 获取新鲜的水果
	public abstract Fruit getFruit();
}
```

![image-20220920082910532](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920082910532.png) 

此代码为第二遍的配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="apple" class="com.msb.secondlookup.SApple">

	</bean>
	<bean id="banana" class="com.msb.secondlookup.SBanana">

	</bean>
	<bean id = "fruitplate1" class="com.msb.secondlookup.SFruitPlate">
		<lookup-method name="getFruit" bean="apple"></lookup-method>
	</bean>
	<bean id = "fruitplate2" class="com.msb.secondlookup.SFruitPlate">
		<lookup-method name="getFruit" bean="banana"></lookup-method>
	</bean>
</beans>
```

```java
public class STestMethodOverride {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("sMethodOverride.xml");
		SFruitPlate fruitplate1 = (SFruitPlate)ac.getBean("fruitplate1");
		fruitplate1.getFruit();
		SFruitPlate fruitplate2 = (SFruitPlate)ac.getBean("fruitplate2");
		fruitplate2.getFruit();
	}
}
```

result:

```
> Task :spring-debug:STestMethodOverride.main()
I got Fruit
I got SApple
I got Fruit
I got Banana
w: Detected multiple Kotlin daemon sessions at build\kotlin\sessions

```



**Spring中默认的对象都是单例的，Spring会在一级缓存中持有该对象，方便下次直接获取，那么如果是原型作用于的话，会创建一个信的对象**

**如果想在一个单例模式的bean下引用一个原型模式的bean，该怎么办？**

**所以在此时，就需要引用lookup-method标签来解决此问题**



解析lookup-method标签

![image-20230221201917687](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221201917687.png) 

这里是false 他到底是怎么弄的呢？

![image-20230221202117362](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202117362.png) 



**验证及准备覆盖方法，lookup-method、replace-method**
**当需要配置的bean对象中包含了lookup-method和replace-method标签的时候，会产生覆盖操作**

```java
try {
	mbdToUse.prepareMethodOverrides();
}
catch (BeanDefinitionValidationException ex) {
	throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(),
			beanName, "Validation of method overrides failed", ex);
}
```

FruitPlate才是使用的对象

![image-20230221202255031](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202255031.png) 

![image-20230221202325442](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202325442.png) 

![image-20230221202344152](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202344152.png) 

![image-20230221202405568](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202405568.png) 

![image-20230221202508259](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202508259.png) 



**获取实例化策略**

getInstantiationStrategy()

![image-20230221202959607](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221202959607.png) 

![image-20230221203028785](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203028785.png) 

![image-20230221203043477](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203043477.png) 

当我们获取完实例化策略 ，才能进行实例化

![image-20230221203113487](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203113487.png) 



![image-20230221203137576](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203137576.png) 





![image-20220920083850613](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920083850613.png) 



![image-20230222082429933](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222082429933.png)

![image-20230222082444931](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222082444931.png) 



![image-20230221203249134](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203249134.png) 



![image-20230222082652173](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222082652173.png) 



![image-20230221203411776](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230221203411776.png) 

![image-20230222082824511](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222082824511.png) 



![image-20230222083551494](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222083551494.png) 



![image-20220920083840655](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920083840655.png) 



fruitPlate返回的就是一个动态代理的对象

![image-20230222083851245](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222083851245.png) 

该如何调用里面的方法呢？



![image-20230222083927210](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222083927210.png) 



执行getFruit方法的时候直接跳到了CglibSubclassingInstantiationStrategy类中的intercept方法中了

![image-20230222083945790](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222083945790.png)  



![image-20230222084058735](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222084058735.png)



lo中返回的是对应对象的名称，然后再调用getBean方法执行对应bean里面的方法

![image-20230222084312921](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222084312921.png) 



验证获取到的对象是否是同一个，是同一个

![image-20230222084457512](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222084457512.png) 

 

**当需要配置的bean对象中包含了lookup-method和replace-method标签的时候会产生覆盖操作。**



**当把bean设置为原型模式（多例）的时候，用的还是同一个吗？**

![image-20220920090110450](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920090110450.png) 

**看调用了几次getBean（lo里面的getBean）**



第一次创建的对象

![image-20230222085351333](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222085351333.png) 

第二次创建的对象

![image-20230222085543641](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222085543641.png) 



lookup-method多例的时候：**我们在引用的时候当前这个对象是在调用某个具体的方法的时候创建的，这个对象不会被缓存到，每次走的都是最新的对象创建的流程，而不会有缓存对象的存在。**

**通过拦截器的方式，在我们每次需要的时候都去创建最新的对象，而不会把原型对象缓存起来**



spring会通过拦截器的方式，在我们每次需要的时候都会去创建最新的对象，而不会把原型对象缓存起来。

![image-20220920090721156](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920090721156.png)  ![image-20220920090704432](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920090704432.png) 









一个单例对象 如果没有配置lookup-method标签，我们能引用这个对象吗？

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="apple" class="com.msb.secondlookup.SApple">
		<property name="sBanana" ref="banana"></property>
	</bean>
	<bean id="banana" class="com.msb.secondlookup.SBanana" scope="prototype">
	</bean>
</beans>
```

```java
public class SApple extends SFruit{

	private SBanana sBanana;

	public SBanana getsBanana() {
		return sBanana;
	}

	public void setsBanana(SBanana sBanana) {
		this.sBanana = sBanana;
	}

	public SApple() {
		System.out.println("I got SApple");
	}
}
```

Apple本身是单例的，但是他里面引用了原型模式的Banana

![image-20230222130406870](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222130406870.png) 

result:

```
com.msb.secondlookup.SBanana@38425407
com.msb.secondlookup.SBanana@38425407
```

![image-20230222130626063](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222130626063.png)

result:

```
com.msb.secondlookup.SBanana@3e694b3f
com.msb.secondlookup.SBanana@3e694b3f
```



**从结果来看两个获取的是一样的对象**

**所以不行，需要配置lookup-method才能使得每次获取的都是最新的。**

**虽然banana是一个原型的，按照道理来说应该是每次都创建新的，但是在创建apple对象的时候这个apple会被缓存，那就意味着banana也会被缓存。**

**所以当我们配了lookup-method的时候，配上多例，每次在执行方法的时候用的都是新的东西**



#### replace-method

![image-20230222130955770](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222130955770.png) 

![image-20230222131003717](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222131003717.png)

后续改一下配置文件就行了。



**如果一个对象是单例，一个对象不是单例是多例，而每次想获取的时候想要保证获取的都是新的对象，这种情况下用多例是解决不了的，所以当我们引入lookup-method的时候，虽然我们是property原型模式，但是可以保证每次用的都是一个新的对象。**



#### 题外话BFPP中的类ConfigurationClassPostProcessor的postProcessorBeanFactory方法：

postProcessorBeanFactory方法在实现的时候有调用enhanceConfigurationClasses方法

![image-20220920091400212](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920091400212.png) 

![image-20230222131746521](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222131746521.png) 

![image-20230222131823490](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222131823490.png) 

在这里为什么要使用动态代理的方式实现？

其实是跟@Bean标签有关系的。

![image-20220920091533317](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220920091533317.png)



 为什么在spring中有那么多的cglib



![image-20221007161143926](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007161143926.png) 



## Spring的bean创建流程（三）



整个bean的创建过程

**getBean->doGetBean->createBean->doCreateBean**



### doGetBean方法



invokeBeanFactoryPostProcessors的时候会调用doGetBean

![image-20230222195252182](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222195252182.png) 

 

自定义的BRPP

![image-20230222195547786](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222195547786.png)

getSingleton

![image-20230222195746833](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222195746833.png)



![image-20230222195932226](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222195932226.png) 



![image-20230222195917145](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222195917145.png) 



执行getObject，调用lambda表达式里面的方法，调用createBean()

![image-20220926164924340](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220926164924340.png)



![image-20230222200046991](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222200046991.png) 

![image-20230222200227174](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222200227174.png) 



BeanPostProcessor

![image-20230222200614795](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222200614795.png) 



看当前这个类是否有InstantiationAwareBeanPostProcessor这个接口

![image-20230222200445107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230222200445107.png)







### 实例化实现方法

自定义InitiationAwareBeanPostProcessor

```java
public class SBeforeInstantiation {

	public void doSomeThing() {
		System.out.println("执行" + this.getClass().getName() + "的doSomething方法...");
	}

}
```

```java
package com.msb.sResolveBeforeInstantiation;

import com.msb.resolveBeforeInstantiation.BeforeInstantiation;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author LYX
 * @description
 * @date 2023/2/23 8:29
 */
public class SMyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {


	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		System.out.println("beanName: " + beanName + "执行实例化之前 postProcessBeforeInstantiation 方法");
		if (beanClass == BeforeInstantiation.class) {
			// 通过cglib生成动态代理
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(beanClass);
			// 设置回调方法
			enhancer.setCallback(new SMyMethodInterceptor());
			SBeforeInstantiation sBeforeInstantiation = (SBeforeInstantiation) enhancer.create();
			System.out.println("创建代理对象" + sBeforeInstantiation);
			return sBeforeInstantiation;
		}
		return null;
	}

	/**
	 * 实例化之后做的事情
	 * @param bean the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return
	 * @throws BeansException
	 */
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		System.out.println("beanName: " + beanName + "执行实例化之后 postProcessAfterInstantiation 方法");
		return false;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName: " + beanName + "初始化之前 postProcessBeforeInitialization 方法");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName: " + beanName + "初始化之后 postProcessAfterInitialization 方法");
		return bean;
	}

	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
		// 属性值的处理
		System.out.println("beanName: " + beanName + "执行 postProcessProperties 方法");
		return pvs;
	}

}

```

```java
public class SMyMethodInterceptor implements MethodInterceptor {


	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("目标方法执行之前：" + method);
		Object o1 = methodProxy.invoke(o, objects);
		System.out.println("目标方法执行之后：" + method);
		return o1;
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">


	<bean id="sBeforeInstantiation" class="com.msb.sResolveBeforeInstantiation.SBeforeInstantiation"/>
	<bean id="sMyInstaitiationAwareBeanPostProcessor" class="com.msb.sResolveBeforeInstantiation.SMyInstantiationAwareBeanPostProcessor"/>
</beans>
```

```java
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("sResolveBeforeInstantiation.xml");
		BeforeInstantiation bean = ac.getBean(BeforeInstantiation.class);
		bean.doSomeThing();
	}
```



### 如何创建对象



**当执行到invokeBeanFactoryPostProcessors的时候会创建对象吗？**

不会，因为上面的例子是BPP的实现子类，所以不会创建对象。在registerBeanPostProcessors里面会调用执行。

![image-20221007162928035](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007162928035.png) 



![image-20230223132732777](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223132732777.png) 



**我们自定义的BeanPostProcessor，Spring并不会提前帮我们生成代理对象，会按照doCreateBean的方式去严格去创建**

![image-20230223193446951](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223193446951.png)

![image-20230223193530692](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223193530692.png) 

 

if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors())  这个判断是进不去的，所以resolbeBeforeInstantiation()方法没有处理bean，直接返回的。

这里会判断我们当前执行的逻辑中是否包含了有可能提前创建对象的BeanPostProcessors。

是否会执行doCreateBean取决于在之前定义的BPP里面是否包含了提前创建Bean对象这么 一个BPP，如果不包含才往下走。

![image-20230223193601141](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223193601141.png)

![image-20230223193743888](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223193743888.png)

我们自定义的BPP他并不会帮我生成代理对象，而是按照严格的步骤往下走。



**完成了registerBeanPostProcessors的注册之后，后面的对象就会有InstantiationAware对象了。**

**后面就会有我们自己定义的BPP了。会在最后一个方法finishBeanFactoryInitialization中创建，也还是通过doCreateBean的方式创建。**



![image-20230223194003326](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230223194003326.png) 



使用beforeInstantiation的bean

![image-20230224083214415](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083214415.png) 

![image-20230224083307768](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083307768.png)



![image-20230224083416053](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083416053.png) 



调用实例化之前的方法

![image-20230224083509349](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083509349.png) 

![image-20230224083611726](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083611726.png) 



![image-20230224083740498](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083740498.png)

进入到我们自定义的实例对象 实例化之前的方法 

![image-20230224083756286](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083756286.png)

生成动态代理的对象

![image-20230224083916098](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083916098.png)



当前这个bean已经创建成功了

![image-20230224083958451](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224083958451.png) 



对象创建完毕 下面的doGetBean就可以不用执行了，直接返回bean对象

![image-20230224084149725](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224084149725.png) 



加到一级缓存里面去，加到一级缓存的对象是代理对象

![image-20230224085108698](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085108698.png)

![image-20230224085215893](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085215893.png)

![image-20230224085305769](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085305769.png)

![image-20230224085318178](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085318178.png)



创建代理对象的代码放到before或者after里面是没有区别的把？

有区别，如果放到after里面执行的话这个bean是没有对象的

![image-20230224085423454](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085423454.png)

如果bean没有对象了，那后置处理方法就不会进行执行了。所以必须放在之前。



判断当前执行逻辑里面是否包含了提前创建好bean对象这样的一个BeanPostProcessor，如果不包含就接着往下走。

如果这边不使用动态代理生成对象有什么问题吗？

![image-20221007165241177](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007165241177.png) 

**如果不使用动态代理的话就不能使用拦截方法了，因为我们设置了一个Interceptor()回调方法，我们例子中所对应的方法doSomething就不会调用了。但是new出来的对象还是会被spring管理，还是会往一级缓存放，但是在方法调用的时候就不会有这些前置处理的逻辑了。**



#### **在Spring中有几种方式可以帮我们创建对象？**

1. 自定义BeanPostProcessor生成代理对象（InstantiationAwareBeanPostProcessor）。
2. 通过反射创建对象。
3. 通过factoryMethod创建对象。
4. FactoryBean的方式创建对象（调用getObject方法）。
5. 通过supplier（供给器）创建对象。

![image-20230224085954701](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224085954701.png) 

##### factoryMethod



factorybean和factorymethod表示的其实是两种不同的创建方式，一种是实例工厂，一种是静态工厂。

![image-20230224090050396](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230224090050396.png)

实例工厂、静态工厂

![image-20221007170114482](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007170114482.png) 



##### **supplier方式创建对象**

![image-20221007170801916](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007170801916.png)

 instanceSupplier 现在是空的，如果我想执行的话该怎么执行呢？如何给他赋值呢？

![image-20221007170910156](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007170910156.png) 



**supplier为空？我们应该如何给他设置进去？**

在BeanDefinition的实现类中的父类AbstractBeanDefinition中。

![image-20221007200500179](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007200500179.png) 

![image-20221007200639265](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007200639265.png) 



例子：

```java
public class User {
	private String name;

	public User() {
	}

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				'}';
	}

}
```

```java
public class SupplierBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		//获取当前bean的定义信息
		BeanDefinition user = beanFactory.getBeanDefinition("user");
		GenericBeanDefinition gbd = (GenericBeanDefinition) user;
		gbd.setInstanceSupplier(CreateSupplier::createUser);
		gbd.setBeanClass(User.class);
	}
}
```

```java
public class CreateSupplier {
	public static User createUser() {
		return new User("zhangsan");
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="user" class="com.msb.supplier.User"/>
	<bean class="com.msb.supplier.SupplierBeanFactoryPostProcessor"/>
</beans>
```

![image-20221007213102937](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007213102937.png) 



![image-20221007213326128](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007213326128.png) 

**Supplier和FactoryBean 创建对象有相似之处**

FactoryBean 抽象出一个接口规范，所有的对象必须要通过getObject方法获取 ->他是一个接口规范的实现

Supplier 随便定义创建对象的方法，不止局限于getObject ->只是BeanDefinition的一个属性值



**factoryMethod 创建对象**



```java
public class Person {
	private int id;
	private String name;
	private int age;
	private String gender;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
```

```java
public class PersonInstanceFactory {
	public Person getPerson(String name) {
		Person person = new Person();
		person.setId(1);
		person.setName(name);
		return person;
	}
}
```

```java
public class PersonStaticFactory {
	public static Person getPerson(String name) {
		Person person = new Person();
		person.setId(1);
		person.setName(name);
		return person;
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="person" class="com.msb.factoryMethod.PersonStaticFactory" factory-method="getPerson">
		<!--	可以为方法指定参数	-->
		<constructor-arg value="lisi"></constructor-arg>
	</bean>
	<bean id="personInstanceFactory" class="com.msb.factoryMethod.PersonInstanceFactory"></bean>
	<!--
		factory-bean:指定使用哪个工厂实例
		factory-method:指定使用哪个工厂实例的方法
		-->
	<bean id="person2" class="com.msb.factoryMethod.Person" factory-bean="personInstanceFactory" factory-method="getPerson">
		<constructor-arg value="wangwu"></constructor-arg>
	</bean>
</beans>
```



![image-20221007215333291](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007215333291.png) 

![image-20221007215448569](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007215448569.png) 



![image-20221007222504900](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221007222504900.png)





### 实例化

BeanDefinition接口有两个很重要的子类，一个是GenericBeanDefinition、一个是RootBeanDefinition

![image-20230228083843851](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228083843851.png) 



GenericBeanDefinition中没有setSupplier类似的方法，那么我们要去RootBeanDefinition中查看，RootBeanDefinition中也没类似方法，但是他有一个子类 AbstractBeanDefinition

![image-20230228084140563](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228084140563.png) 

![image-20230228084225304](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228084225304.png) 



对应类图，我们可以看到GenericBeanDefinition也有集成AbstractBeanDefinition这个抽象类，所以我们在操作相关实现的时候就要对他进行强转，强转成GenericBeanDefinition类型的类

![image-20230228084412752](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228084412752.png) 

![image-20230228084642180](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228084642180.png)

SUser.class

```java
public class SUser {
	private String name;
	public SUser() {
	}

	public SUser(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SUser{" +
				"name='" + name + '\'' +
				'}';
	}
}
```

```java
public class SSupplierBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanDefinition sUser = beanFactory.getBeanDefinition("sUser");
		GenericBeanDefinition gb = (GenericBeanDefinition) sUser;
		gb.setInstanceSupplier(SCreateSupplier::createUser);
		gb.setBeanClass(SUser.class);
	}
}
```

```java
public class SCreateSupplier {
	public static SUser createUser() {
		return new SUser("lisi");
	}
}
```

main

```java
public class STestSupplier {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("Ssupplier.xml");
		SUser bean = ac.getBean(SUser.class);
		System.out.println(bean.getName());
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="sUser" class="com.msb.Ssupplier.SUser">
	</bean>
	<bean class="com.msb.Ssupplier.SSupplierBeanFactoryPostProcessor"/>

</beans>
```

运行结果

![image-20230228090733960](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228090733960.png) 





### 创建实例包的过程

![image-20230228131310699](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228131310699.png) 

#### supplier创建对象

![image-20230301085310677](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301085310677.png)

![image-20230228130706849](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130706849.png)



为什么是null？

![image-20230228130741375](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130741375.png) 

当前Bean不是User这个Bean

![image-20230228130812539](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130812539.png) 

![image-20230228130837425](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130837425.png) 



![image-20230228130857810](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130857810.png) 



supplier调用的时候其实是调用我们的lambda方法 instance = instanceSupplier.get();

![image-20230228130929109](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228130929109.png)

![image-20230228131018332](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228131018332.png) 

![image-20230228131002988](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228131002988.png) 

**用supplier与FactoryBean 有相似之处。**

**FactoryBean：抽象出一个接口规范，所有的对象必须要通过getObject方法来获取--->接口规范实现**

**supplier：随便定义创建对象的方法，不止局限于getObject--->只是beanDefinition的一个属性值（instanceSupplier）**



#### factoryMethod创建对象 



 ![image-20230228131329966](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228131329966.png) 



![image-20230228131658720](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228131658720.png) 





SPersonStaticFactory

```java
public class SPersonStaticFactory {

	public static SPerson getPerson(String name) {
		SPerson sPerson = new SPerson();
		sPerson.setId(1);
		sPerson.setName(name);
		return sPerson;
	}

}
```

SPersonInstanceFactory

```java
public class SPersonInstanceFactory {

	public SPerson getPerson(String name) {
		SPerson sPerson = new SPerson();
		System.out.println("factory");
		sPerson.setId(1);
		sPerson.setName(name);
		return sPerson;
	}

}
```

SPerson

```java
public class SPerson {

	private int id;
	private String name;
	private int age;
	private String gender;


	@Override
	public String toString() {
		return "SPerson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", gender='" + gender + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
```

SFactoryMethod.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<!--  静态工厂	-->
 	<bean id="sPerson1" class="com.msb.Sfactorymethod.SPersonStaticFactory" factory-method="getPerson">
		<!--	constructor-arg 可以为方法指定的参数	-->
		<constructor-arg value="ermazi"/>
	</bean>
	<bean id="sPersonInstanceFactory" class="com.msb.Sfactorymethod.SPersonInstanceFactory"/>
 	<!--
 	实例工厂
 	factory-bean:指定使用哪个工厂实例
 	factory-method:指定使用按个工厂实例的方法
 	-->
	<bean id="sPerson2" class="com.msb.Sfactorymethod.SPerson" factory-bean="sPersonInstanceFactory" factory-method="getPerson">
		<constructor-arg value="zhuzhuzhu"/>
	</bean>

</beans>
```

```java
public class STestFactoryMethod {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("SFactoryMethod.xml");
		SPerson person = ac.getBean("sPerson1", SPerson.class);
		System.out.println(person);
		SPerson sPerson2 = ac.getBean("sPerson2", SPerson.class);
		System.out.println(sPerson2);
	}

}
```



**在整个容器中，他会帮我们创建几个bean对象**

3个，person、personInstanceFactory、person2。

![image-20230228195425380](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228195425380.png)

![image-20230228195459624](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228195459624.png) 



创建构造器的处理器   

![image-20230228195610984](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228195610984.png)



person2走的

![image-20230228200415996](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228200415996.png)

person1走的

![image-20230228195949546](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228195949546.png)



![image-20230228200905330](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228200905330.png) 



反射获取

![image-20230228200922236](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228200922236.png)

![image-20230228200938052](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228200938052.png) 

会把包含的所有父类方法都获取出来

![image-20230228201019456](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201019456.png)

遍历完之后只剩一个

![image-20230228201112493](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201112493.png)

进行实例化

![image-20230228201408140](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201408140.png)



![image-20230228201435734](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201435734.png)



执行factoryMethod.invoke()方法，调用getPerson()

![image-20230228201519864](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201519864.png)



![image-20230228201719572](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230228201719572.png)



##### factoryMethod创建对象流程图

![instantiateUsingFactoryMethod大致流程](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/instantiateUsingFactoryMethod%E5%A4%A7%E8%87%B4%E6%B5%81%E7%A8%8B.jpg)





##### **细节**



创建参数的数组，可看可不看

![image-20230301085614417](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301085614417.png)



不管当前的工厂是静态工厂还是实力工厂也好，最终我们都是要明确的知道getPerson这个方法到底是哪一个，因为我们只有明确知道是哪个方法之后我们才能更好的调用，并且生成具体的对象。









## spring的bean创建流程（四）



### 通过反射创建对象



![image-20221008084203929](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084203929.png) 

![image-20221008084245905](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084245905.png) 

![image-20221008084256200](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084256200.png) 

![image-20221008084332283](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084332283.png) 

![image-20230301193740628](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301193740628.png) 

![image-20221008084408366](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084408366.png) 



![image-20230301193847005](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301193847005.png) 





如果supplier和factoryMethod执行的过程都没有创建具体的对象，就需要通过反射的方式来创建对象了

![image-20230301131437248](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301131437248.png) 

![image-20230301194009134](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301194009134.png)





**我们在进行实例化的时候我们必须要找到我们指定的构造函数是哪一个，一个类里面可以定义无限个构造函数，所以在前面ConstructResolve的时候来判断我们使用的构造函数是哪一个，来进行具体的执行，在前面已经给他缓存起来了。**

![image-20230301131552244](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301131552244.png) 



![image-20230301131605818](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301131605818.png) 



两个person对象因为scope为prototype的缘故，所以第二个对象还会再走一遍第一个对象的逻辑，前一个person的构造方法会缓存下来

![image-20230301194554857](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301194554857.png) 



![image-20230301194131589](C:%5CUsers%5C97151%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20230301194131589.png)



**构造器注入和普通属性注入是一样的吗**？

一样的，只是构造器注入的时候我们需要选择要使用的构造器是哪一个





### 创建构造器实例



```java
public class SPerson {

	private int id;
	private String name;
	private int age;
	private String gender;

	public SPerson() {
	}

	public SPerson(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "SPerson{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", gender='" + gender + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
```



```java
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("sPerson.xml");
		SPerson sPerson = ac.getBean("sPerson", SPerson.class);
		System.out.println(sPerson);
		SPerson sPerson2 = ac.getBean("sPerson", SPerson.class);
		System.out.println(sPerson2);

	}
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="sPerson" class="com.msb.Sfactorymethod.SPerson" scope="prototype">
<!--		<property name="id" value="123"/>-->
<!--		<property name="name" value="zhangsangou"/>-->
		<constructor-arg name="id" value="123123"/>
		<constructor-arg name="name" value="玩儿吗"/>
	</bean>
</beans>
```





### 讲解构造器的过程

![image-20230301195714773](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301195714773.png) 



自动装配的方式

![image-20221008084902565](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084902565.png) 

![image-20221008084845440](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221008084845440.png) 

![image-20230301195854777](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301195854777.png)

 ![image-20230301200246745](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301200246745.png)

![image-20221009084632736](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221009084632736.png) 

![image-20230301200313734](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301200313734.png) 



如果参数和构造器都为空的话，应该把当前对象或者说这个类里面的所有构造器都取出来看看到底哪个是匹配的。

![image-20230301201142515](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301201142515.png)

相当于是候选构造器

![image-20230301201302630](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301201302630.png) 



![image-20230301203002351](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301203002351.png)



下次再获取的时候可以从缓存中取

![image-20230301203351240](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301203351240.png)





设计巧妙的步骤  这个排序非常重要，他是通过我们获取的参数结果来排序的，比如参数是：2、1、0  当2被匹配到了，后面的就不需要再去处理了

![image-20230301203517015](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230301203517015.png)











### 构造器实例化和工厂实例化有什么区别





![image-20230302082327042](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302082327042.png) 

上面和下面所处理的东西是一样的东西，但是调用的方法不一样

![image-20230302082511428](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302082511428.png)

![image-20230302082527470](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302082527470.png)



要满足这个条件他才能识别得到

![image-20221010084452142](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221010084452142.png) 





### BeanPostProcessor

![image-20230302083333120](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083333120.png) 

![image-20230302083343935](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083343935.png)

![image-20230302083312840](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083312840.png)



#### **SmartInstantiationAwareBeanPostProcessor**

![image-20230302083546550](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083546550.png) 

决定使用哪个构造器

![image-20230302083604494](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083604494.png)



**AutowiredAnnotationBeanPostProcessor**

![image-20230302083629128](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083629128.png)

 ![image-20230302083756251](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302083756251.png)

获取构造器集合
1、如果有多个Autowired，required为true  不管有没有默认构造方法，会报异常
2、如果只有一个Autowired，required为false  没有默认构造方法，会报警告
 其他情况都可以，但是以有Autowired的构造方法优先，然后才是默认构造方法。

![image-20230302084907943](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302084907943.png)

一个类使用多个Autowired来看看是否会报错

![image-20230302085218840](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085218840.png) 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

	<context:component-scan base-package="com.msb"/>

	<bean id="sPerson" class="com.msb.Sfactorymethod.SPerson" scope="prototype">
<!--		<property name="id" value="123"/>-->
<!--		<property name="name" value="zhangsangou"/>-->
		<constructor-arg name="id" value="123123"/>
		<constructor-arg name="name" value="玩儿吗"/>
	</bean>
</beans>
```

会报错

![image-20230302085653315](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085653315.png) 



```
Exception in thread "main" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sPerson': Invalid autowire-marked constructor: public com.msb.Sfactorymethod.SPerson(). Found constructor with 'required' Autowired annotation already: public com.msb.Sfactorymethod.SPerson(int,java.lang.String)
```



这样就不会报错

![image-20230302085922212](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085922212.png)

![image-20230302090201202](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302090201202.png)

其实不是扫描的问题，其实是要看识别注解的东西是否生效了注解。

![image-20230302085443794](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085443794.png)

![image-20230302085458182](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085458182.png)

要看AutowiredAnnotationBeanPostProcessor

![image-20230302085516782](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302085516782.png)

 determineCandidateConstructors筛选构造方法
**从bean的后置处理器中为自动装配寻找构造方法，有且仅有一个有参构造或者有且仅有@Autowired注解构造。**



**autowireConstructor 还会对当前取到的构造器进行一个筛选工作**

![image-20230302090503744](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302090503744.png)



![image-20230302090707867](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302090707867.png)

![image-20230302090720663](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302090720663.png)

看子类

findPrimaryConstructor   @Primary注解

![image-20230302090736701](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230302090736701.png)

**不管如何解析，最终的目的都是为了取得对应的构造器，取到构造器就直接返回了，不用再执行最下面的instantiateBean 方法了**





### 反射实例化

![image-20221011122713083](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011122713083.png) 



普通的类在进行实例化的时候是必然要通过反射的方式来进行调用的

 getInstantiationStrategy().instantiate(mbd, beanName, this);

![image-20230303083951061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303083951061.png)



该实例化接口表示三种不同的实例化方式
1、通过无参构造方法进行实例化

2、通过指定参数的构造方法进行实例化

3、通过工厂方法进行实例化

![image-20230303084315891](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303084315891.png)



![image-20221011123046704](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011123046704.png) 

![image-20230303084457023](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303084457023.png)



子类SimpleInstantiationStrategy实现了三种的实例化方案

![image-20221011123124362](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011123124362.png)

 CglibSubclassingInstantiationStrategy 为SimpleInstantiationStrategy的子类实现 

![image-20221011123552405](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011123552405.png) 

![image-20221011123653772](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011123653772.png) 

![image-20230303084802870](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303084802870.png) 



通过cglib的方式来进行具体的创建

![image-20230303084926391](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303084926391.png) 

 ![image-20230303085047010](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303085047010.png)



#### 实例化策略





1、Simple实例化策略

	1. 无参构造实例化
	2. 有参构造实例化
	3. 工厂方法实例化

2、Simple的子类又有一些延伸的扩展，cglib实例化策略

 	1. 动态代理对象
 	  	1. 无参构造
 	  	2. 有参构造



![image-20221011124300121](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011124300121.png) 

![image-20221011124312364](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011124312364.png) 

![image-20221011124402023](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011124402023.png) 



![image-20221011124814416](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011124814416.png) 



#### BeanWrapper

对象在创建完成之后是在堆里面开辟了块空间，开辟完空间之后还需要进行属性的填充

所以要先将实例对象进行一个包装，再进行属性的类型转换等等操作

![image-20221011125328716](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011125328716.png) 





![image-20221011152525130](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011152525130.png) 

![image-20221011152537060](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011152537060.png)



![image-20221011152727977](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011152727977.png) 



执行完成之后，我们的实例化就已经完成了

![image-20230303131530277](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303131530277.png)



后续该进行初始化了



#### @PostConstruct

![image-20230303132053108](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303132053108.png)



![image-20230303132144929](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303132144929.png) 





实例化前后

初始化前后

属性值的处理

![image-20230303132336914](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303132336914.png)



![image-20221011153026704](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011153026704.png) 

![image-20230303132524350](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303132524350.png) 



**MergedBeanDefinitionPostProcessor都有统一的前缀AnnotationBeanPostProcessor，就是用来处理我们一些相关注解的描述的**

![image-20230303132728583](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303132728583.png) 



![image-20230303200507585](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303200507585.png) 



LifecycleMetadata生命周期的元数据，从初始化到销毁就是一个生命的周期。

![image-20230303200533769](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303200533769.png)

![image-20230303200706087](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303200706087.png) 

![image-20230303200720241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303200720241.png) 



**要找到某个对象他的初始化method是什么，销毁的method是什么**

![image-20230303200850860](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303200850860.png)



![image-20230303201037148](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303201037148.png)



**遍历当前类，找到当前类所定义的注解有哪些**



**@PostConstruct**

用来修饰一个非静态的void方法



**@PreDestory**

bean被关闭的时候执行被这个注解修饰的方法



操作实例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
   <context:component-scan base-package="com.msb"/>
</beans>
```

```JAVA
package com.msb;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * TODO
 *
 * @author Karry
 * @date 2023-03-03  20:16
 */
@Component("sPostConstructPerson")
public class SPostConstructPerson {

	private Integer id;
	private String name;

	public SPostConstructPerson() {
		System.out.println("执行SPostConstructPerson的无参构造方法");
	}

	public SPostConstructPerson(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	@PreDestroy
	public void destroy() {
		System.out.println("执行 SPostConstructPerson 的 destroy方法");
	}

	@PostConstruct
	public void init() {
		System.out.println("SPostConstructPerson 的 init方法");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

```

```java
public class SPostConstructTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("STest.xml");
		SPostConstructPerson spostConstructPerson = ac.getBean("sPostConstructPerson", SPostConstructPerson.class);
		System.out.println(spostConstructPerson);
		ac.close();
	}
}
```

结果

```
> Task :spring-debug:SPostConstructTest.main()
执行SPostConstructPerson的无参构造方法
SPostConstructPerson 的 init方法
com.msb.SPostConstructPerson@70e38ce1
执行 SPostConstructPerson 的 destroy方法
```



![image-20221011154304578](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011154304578.png) 

![image-20221011154439344](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011154439344.png) 



什么时候Destroy会被执行？

![image-20221011154531256](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011154531256.png)







解析执行流程 以person对象为例

![image-20230303204419962](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204419962.png)

![image-20230303204501511](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204501511.png)

![image-20230303204625673](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204625673.png)

 

![image-20230303204647162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204647162.png)



![image-20230303204707154](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204707154.png) 



 

![image-20230303204741090](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204741090.png) 

 

Arrays.asList(this.initAnnotationType, this.destroyAnnotationType)  里面的执行结果是@PostConstruct和@PreDestroy这两个注解

![image-20221011155058538](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011155058538.png)

![image-20221011155047612](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011155047612.png) 

![image-20221011155401833](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011155401833.png)

 

![image-20230303204914260](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303204914260.png)

得到方法进行遍历判断，方法上面是否添加的@PostConstruct或者@PreDestroy注解

![image-20230303205002054](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303205002054.png)



![image-20230303205031843](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303205031843.png)



**initAnnotationType和destroyAnnotationType是在哪里赋值的？**

![image-20221011160124217](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221011160124217.png) 



**metadata其实只是中间数据，最后还是要把数据都写会BeanDefinition，在整个bean的创建过程中BeanDefinition是贯彻始终的**

在这里的时候init方法和destroy方法是还没有调用执行的。

![image-20230303210619026](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303210619026.png)

![image-20230303210632082](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303210632082.png)

![image-20230303210802714](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303210802714.png)



**在哪里调用init和destroy方法？**

![image-20230303211515682](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211515682.png)

![image-20230303211505783](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211505783.png)

![image-20230303211534723](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211534723.png)

![image-20230303211553668](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211553668.png) 

![image-20230303211609423](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211609423.png)

**最终会调用的方法**

![image-20230303211354335](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211354335.png)



 **initAnnotationType和destroyAnnotationType 是在什么时候进行@PostConstruct和@PreDestroy赋值的呢？**

![image-20230303211741265](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211741265.png)

![image-20230303211837370](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303211837370.png) 





![image-20230303212012493](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303212012493.png) 

![image-20230303212036019](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303212036019.png)

往BeanDefinition对象注册

![image-20230303212106610](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303212106610.png) 

把数据设置到BeanDefinition中

![image-20230303213132951](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303213132951.png)

![image-20230303213254099](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303213254099.png) 

![image-20230303213330197](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230303213330197.png)





## Spring的bean创建流程(五)



### BeanPostProcessor



![image-20220626132520146](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626132520146.png) 

spring通过postProcessMergedBeanDefinition方法找出所有需要注入的字段，同时做缓存

![image-20220626133731851](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626133731851.png) 



![image-20220626134625971](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626134625971.png)

![image-20220626134837174](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626134837174.png)

为什么initAnnotationType 指的就是@PostConstruct，而destroyAnnotationType 指的是@PreDestroy，他是在哪里完成的一个赋值的工作。

![image-20230307085850299](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307085850299.png)

![image-20230307090102102](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307090102102.png)

 

![image-20230307090341918](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307090341918.png)

 

 



![image-20220626134904225](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626134904225.png) 



### checkConfigMembers

将我们当前的访问修饰符等等注册到BeanDefinition里面去，方便后续调用

![image-20230307131152334](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307131152334.png)

方便后续的BeanDefinition能够进行直接的调用操作

![image-20220626135034073](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626135034073.png) 

![image-20230307131250692](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307131250692.png)



@PostConsturce、@PreDestroy注解的处理方式其实跟@Resource的处理方式是一样的

![image-20220626135204938](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626135204938.png) 

find->build





###  buildResourceMetadata

![image-20221013085005584](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221013085005584.png)

![image-20230307131717256](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307131717256.png)

 

![image-20220626135517800](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626135517800.png) 



do while 是为了遍历查找父类     只是最基本的识别，并没有完成注入的工作

![image-20230307131937149](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307131937149.png) 

![image-20230307132523734](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307132523734.png) 

![image-20230307132534590](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230307132534590.png) 





加载注解的相关解析配置工作

CommonAnnotationBeanPostProcessor（@PostConstruct、@PreDestroy、@Resource）



### AutowiredAnnotationBeanPostProcessor

加载注解的相关解析配置工作

CommonAnnotationBeanPostProcessor

->@Resource

->InitDestroyAnnotationBeanPostProcessor

​		->@PreDestroy

​		->@PostConstruct

![image-20221013085247164](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221013085247164.png) 



AutowiredAnnotationBeanPostProcessor

->@Autowired

->@Value



用来处理@Autowired、@Value注解的

先find、再build

![image-20230308082716591](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308082716591.png) 

 ![image-20230308082735188](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308082735188.png)



开始进行相关的识别工作

![image-20230308082814183](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308082814183.png) 



autowiredAnnotationTypes中包含了两个注解值@Autowired、@Value

![image-20221017162807612](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017162807612.png) 

![image-20221017162729695](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017162729695.png) 



调用构造器将相应注解@Autowired和@Value注册到集合中，还有@Inject

![image-20221017162936624](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017162936624.png) 

![image-20220626141600322](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626141600322.png) 

![image-20230308083316414](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308083316414.png)



**所有的注解处理和解析工作一定是在后续的某个步骤统一来完成的。**

![image-20221017163556982](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017163556982.png) 

![image-20220626141856244](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626141856244.png)  



**inject**是按照类型来注入的。

将当前值设置到field里面去

![image-20230308083745475](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308083745475.png)

![image-20221017163704917](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017163704917.png)

 

![image-20221017164206768](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017164206768.png) 



![image-20230308083928304](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308083928304.png)

![image-20230308083946594](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308083946594.png)



resolveDependency就是从容器或者工厂里面获取对应的依赖值的

![image-20230308084109369](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308084109369.png)



为什么要在中间加下图的这一步操作呢？有什么意义？

**初始化处理完之后，后面就需要进行初始化操作了，区分哪些是被注解处理的和哪些是被xml文件处理的**

![image-20221017164655594](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221017164655594.png) 



如果配置冲突了呢？

验证会出现什么问题

![image-20230308084804111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308084804111.png)

 ![image-20230308084817013](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308084817013.png)

不会报错 使用了xml类型的注入

![image-20230308085102258](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308085102258.png)

**当出现两个相同名称实例，spring会覆盖其中一个，xml优先级高于注解；xml中同时配置两个相同id的bean，直接校验不通过报错**



### 初始化和循环依赖

**如何去处理循环依赖？**



![image-20221021082943197](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221021082943197.png) 



**当实例化完成之后，要开始初始化赋值操作了，但是赋值的时候，值的类型有可能是引用类型，需要从spring容器中获取具体的某个对象来完成赋值操作，而此时，需要引用的对象可能被创建了，也可能没被创建，如果被创建了，那么直接获取即可，如果没有创建，在整个过程中就会涉及到对象的创建过程，而内部对象的创建过程中又会有其他的依赖，其他的依赖中有可能包含当前的对象，而此时当前对象还没有创建完成，所以此时产生了循环依赖的问题。**



**A对象中有B对象的属性，B对象中有A对象的属性**

![image-20230308090044691](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308090044691.png) 

![image-20220626180428673](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626180428673.png) 



**如何解决？**

​	额外申请一块缓存空间用来存放具体的对象

​	提前暴露出来，申请一块缓存出来，用来存放具体的对象。

​	但是这个时候的对象是一个**只完成了实例化，但是未完成初始化的对象**，是个**半成品**不能用



![image-20220626180707057](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626180707057.png) 

所以将对象进行区分：

分为**半成品缓存**和**成品缓存**都是使用的map结构来存储

![image-20230308194226832](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308194226832.png) 



**我们在获取对象的时候分为了两个步骤，实例化和初始化。**

**问题是：**

**初始化环节的填充属性阶段有没有可能会改变当前对象的地址空间？**

**不会，因为实例化完成之后地址已经生成，不可能改变。**



**但是，我们不得不考虑另外一种情况，Spring在使用的时候会配置AOP，需要会生成具体的代理对象**



#### 问题

#### **1、生成代理对象之前，要不要生成普通对象？**

**要，配置aop的类是不是普通bean对象？是，既然是一个普通对象，那所有后续的工作都要经过完整的创建步骤**

![image-20220626205406741](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626205406741.png) 

所以这个bean对象是肯定要创建的 。



**但是代理不是在resolveBeforeInstantiation的时候就创建了吗？**

**这边的创建和我们后面的循环依赖创建是没关系的，我们要通过这个方法创建对象的话就必须要自定义BeanPostProcessor实现InstationAwareBeanPostProcessor接口，才能创建这个bean定义，而循环依赖是后面才进行的操作。**

![image-20221024102536286](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024102536286.png) 

 





#### 2、**我们能否确定什么时候会调用具体的对象？无论是普通对象还是代理对象**

不能



#### 3、**有没有可能，在Spring中同时存在同一个beanName的普通对象和代理对象？**

如果有的话，那么我在调用的时候是根据beanName来获取对象。



成品和代理对象中都可以获取我们具体的对象。



此时我要获取到两个对象吗？那么我应该优先用哪个？

**如果有的话我们应该分块存储**

![image-20221021091232570](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221021091232570.png) 

**那么可以创建两个缓存，分别存储普通对象和代理对象。**



现在代理对象是一个完整的对象还是一个半成品对象？

如果是一个半成品的话，那么我们代理半成品有什么意义吗？

给别人注入的。



![image-20230308195043057](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230308195043057.png) 



#### 4、问题：成品和代理对象的这两个缓存，我优先获取哪个？

**优先获取代理对象，然后才是普通对象，是个错误的结论**

**如果有限获取代理对象，然后再获取普通对象，我们就需要做一个判断，判断当前在进行调用的时候到底是需要普通对象还是代理对象，但是代理对象和普通对象的关系是被包含的关系，所以意味着代理对象中包含了普通对象所具备的所有功能，可以得出一个结论就是如果一个对象要被代理的，那么此时普通对象就可以不存在了，只要代理对象即可。**



代理对象中应该包含了普通对象所具备的所有功能，如果一个对象需要被代理了，那么此时普通对象就可以不存在了，只要代理对象即可。



**也就是说在后续创建代理对象之后，就可以使用代理对象来覆盖普通对象，普通对象就可以不存在了。**



回到第二个问题中，什么时候要调用对象？

**说白了我在调用对象的时候，如果检测到当前对象需要被代理，那么直接创建代理对象覆盖即可。**



**怎么能够在随时需要的时候创建代理对象呢？**

**类似于观察者，但是不是观察者，我们传递进去一个生成代理对象的一个匿名内部类，当需要调用的时候，直接调用匿名内部类（lambda）来生成代理对象，就是类似于回调机制。**



**所以我们需要一个额外的空间来存储代理对象，生成代理对象的缓存。**

![image-20230309083834584](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230309083834584.png) 

 





#### 5、在刚刚的步骤中说到了初始化，初始化包含哪个环节？

​	**1、填充属性；**

​	**2、执行Aware接口所对应的方法；**

​	**3、执行BeanPostProcessor中的before方法；**

​	**4、执行init-method方法；**

​	**5、执行BeanPostProcessor中的after方法**



**上述步骤执行完成之后是为了获取到一个完整的成品对象，但是在初始化前 我们能够确定哪一个对象需要生成代理对象吗？**

**不能确定，并且我们知道三级缓存只是一个回调机制，所以能否把所有的bean所需要的创建代理对象的lambda表达式都放到三级缓存中？**我可以不用，但是你不能没有

**可以将所有bean对象需要创建代理对象的lambda表达式放到三级缓存中，后续如果我需要调用，直接从三级缓存中调用执行即可，如果不需要，则不处理，在生成完整对象之后可以把三级缓存中的lambda表达式个清除掉（往一级缓存中放成品的时候就可以清除）。**



#### 6、我在什么时候要生成具体的代理对象呢？（代码实操）

​	1、**在进行属性注入的时候，调用该对象生成的时候检测是否需要被代理，如果需要，直接创建代理对象**

​	2、**在整个过程中，没有其他的对象有当前类的依赖，那么在生成最终的完整对象之前生成代理对象即可（BeanPostProcessor中的after方法）**

分了两个地方对他进行注入的，分别对应不同的调用时机。



三个对象A、B、C，规定B、C需要生成代理对象，而A引用B，但是没有对C的引用。A在创建的时候需要注入B对象，而B就需要去生成代理对象。而C就是一个单独的存在，而我们检测到的C对象他也是需要生成代理对象，要对他进行配置。

![image-20230309085616298](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230309085616298.png) 

如果B、C之间有引用的话，那么B再引用C，这个时候C也是需要创建的

![image-20230309085644462](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230309085644462.png) 

那B为什么不在after里面调用呢？

因为A在创建对象的时候就需要填充B属性，所以已经等不到B在after那时候创建了，在此之前就需要用到了。



**一级缓存的对象我们称为成品对象，里面既包含了普通不需要代理的对象，同时又包含了代理对象，这些都会放到一级缓存中去**

​	

**提前暴露对象：说的就是二级缓存中的对象，只完成了实例化，但未初始化的对象**



**三级缓存在进行对象查找的时候，顺序是什么样子的？**

**先找一级，一级没有找二级，二级没有找三级**

**一个对象有没有可能不经过二级缓存直接从三级缓存到一级缓存？**

有可能（后面会讲）



**不要三级缓存可以吗**？

**如果单纯为了解决循环依赖问题，那么使用二级缓存足够解决问题了，三级缓存存在的意义是为了代理，如果没有代理对象，二级缓存足以解决问题。**

![image-20220626214205094](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20220626214205094.png) 





**如何解决循环依赖问题？**

**最本质的问题是在于，实例化和初始化是分步骤分开处理的，当完成实例化之后就可以让其他对象引用当前对象了，只不过当前对象不是一个完整对象而已，后续需要完成此对象的剩余步骤。**

**直接获取半成品对象的引用地址，保证对象能够被找到，而半成品对象在堆空间中是否有设置的属性值，无所谓。**



**B实例化后要赋值给A的B属性，那么B生成代理对象之后，A中的B是如何处理的。**

在赋给A的B属性的时候，B已经确定好了他到底是一个普通对象还是代理对象了

![image-20230310083934437](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310083934437.png) 



所有的对象在进行创建的时候都要遵循bean的生命周期，那么在刚开始的时候会进行实例化的操作，然后在BeanPostProcessor的后置处理方法里完成代理对象的生成，**但是实际循环依赖的发生点是在创建代理对象之前，也就是populatebean方法里，这个时候对象还不会生成代理对象，只会有原始对象**，所以上课的时候我说生成代理对象前面都会有原始对象



不管是否需要代理，我们都会往三级缓存中添加lambda表达式。

![image-20221024155214674](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024155214674.png) 

![image-20221024155053061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024155053061.png) 



## spring的bean创建流程(六)



### Spring的bean创建流程图和知识点

![image-20230310193452009](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310193452009.png) 

![image-20230310194320756](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194320756.png)

如果能够进入到该if判断中，不管当前对象是否需要创建一个代理类，最终都会往三级缓存里面放东西。后续需要就会调用，不需要就不调用

![image-20230310194351645](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194351645.png)



 this.allowCircularReferences 再最开始的时候有用到

![image-20230310194502893](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194502893.png) 

![image-20230310194624847](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194624847.png)



![image-20230310194656588](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194656588.png) 



在这里面设置了我们具体的值，我们可以自己进行修改

![image-20230310194710330](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194710330.png)





### 填充属性



非常重要的两个方法：populateBean、initializeBean

![image-20230310194818887](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310194818887.png)

![image-20230310195320889](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310195320889.png)



**给属性赋值**

**分类**：

1、基本数据类型：

​	**直接完成赋值操作**

​		4类8种基本数据类型------->这里面需要考虑到类型转换的问题

2、引用类型：

​	**先从容器中获取具体的对象值，如果有，直接赋值，如果没有就创建**



**数组、list、set、map、properties（但是他不算是基本数据类型）既可以放基本数据类型，也可以放引用数据类型**



**注入的方式**（他是属于引用类型的）：

1、不注入

2、按照类型完成注入

3、按照名称完成注入

4、按照构造器完成注入

​	注入的方式

![image-20230310195504874](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310195504874.png) 



**属性填充**

![image-20230310200010143](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310200010143.png)



### populatebean属性方法

![image-20230310200143706](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310200143706.png)

#### populateBean()

```java
package com.msb.populatebean;

import com.msb.selfEditor.Address;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author LYX
 * @description
 * @date 2022/10/24 16:03
 */
public class Person {
	private int id;
	private String name="dahuang";
	private int age;
	private String gender;
	private Address address;
	private String[] hobbies;
	private List<Book> books;
	private Set<Integer> sets;
	private Map<String, Object> maps;
	private Properties properties;

	public Person() {
	}

	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Person(int id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Person(int id, String name, int age, String gender) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
	}

	public Person(int id, String name, int age, String gender, Address address) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.address = address;
	}

	public Person(int id, String name, int age, String gender, Address address, String[] hobbies) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.address = address;
		this.hobbies = hobbies;
	}
}

```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="person" class="com.msb.populatebean.Person" autowire="byName"/>
	<bean id="person" class="com.msb.populatebean.Person" autowire="byType"/>
	
	<bean id="address" class="com.msb.populatebean.Address">
		<property name="province" value="福建"/>
		<property name="city" value="龙岩"/>
		<property name="town" value="长汀"/>
	</bean>

</beans>
```

main

```java
public class TestPopulate {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("populatebeanxml.xml");
	}

}
```



**到doCreateBean方法中开始 进入到populateBean()方法**

![image-20230310201052003](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310201052003.png)

![image-20230310201521314](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310201521314.png)



提前代理对象 ![image-20230310201617194](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310201617194.png)



这块是给我们自己去实现的，其他的类都是返回true不做任何的处理

![image-20230310201751580](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310201751580.png)

![image-20230310201936282](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230310201936282.png)





可以在postProcessAfterInstantiation方法中改变bean对象的属性值

![image-20221024161835877](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024161835877.png) 

![image-20221024162243324](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024162243324.png) 



![image-20221024163003009](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024163003009.png) 

![image-20221024162950833](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024162950833.png) 

![image-20221024163147290](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024163147290.png) 



![image-20221024163854574](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024163854574.png) 





###  propertyvalues的使用

![image-20230312141254061](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312141254061.png) 



PropertyValue的类图相对来说是比较复杂的

 ![image-20230312141408960](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312141408960.png)

![image-20230312141437296](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312141437296.png)





```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean id="person3" class="com.msb.populatebean.Person">
		<property name="id" value="666"/>
		<property name="name" value="qewrwqe"/>
	</bean>
	<bean id="spopulateBeanPerson" class="com.msb.sPopulateBean.SpopulateBeanPerson" autowire="byName"/>
	<bean id="spopulateBeanPerson2" class="com.msb.sPopulateBean.SpopulateBeanPerson" autowire="byType"/>

	<bean id="address" class="com.msb.populatebean.Address">
		<property name="province" value="福建"/>
		<property name="city" value="龙岩"/>
		<property name="town" value="长汀"/>
	</bean>
</beans>
```



 **当前bean的PropertyValue是默认没有值的的，但是如果你在xml上有定义给他赋值就会有，他才会帮我们封装**

![image-20230312142131272](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312142131272.png) 

![image-20230312142234778](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312142234778.png) 



![image-20230312142603221](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312142603221.png) 



根据名字、类型进行自动装配。

![image-20230312143327823](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312143327823.png)



他怎么才能知道哪些属性要自动装配呢？

#### byName

![image-20221024164234533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221024164234533.png) 

 



**区分引用类型还是基本数据类型**

![image-20230312144010110](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144010110.png)



获取当前对象的所有属性值

![image-20230312144035882](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144035882.png)

 

 pd.getWriteMethod()：获取具体对象的时候我们要判断里面有没有get set方法

![image-20230312144327429](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144327429.png) 

判断是否基本数据类型。

![image-20230312144335147](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144335147.png) 



返回的都是非简单类型（非基本数据类型）属性

![image-20230312144530467](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144530467.png) 

![image-20230312144826604](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144826604.png)



执行创建bean的一套流程

![image-20230312144858247](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312144858247.png) 



创建依赖关系

![image-20230312145017874](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312145017874.png) 



![image-20230312145214080](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312145214080.png) 

获取到具体的依赖关系

![image-20230312145250913](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312145250913.png)



为什么要存储上面的依赖关系？

**如果只创建了一次其实这个依赖关系没什么用，但是如果多次的话就有意义了，后续可以根据具体key直接获取，而且他是放到DefaultSingletonBeanRegistry里面，这个类里面放的是三级缓存，把对应依赖关系都提前存好了。**



map结构的数据再当前容器中已经存在了

systemEnvironment是一个map结构

![image-20230312145901365](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312145901365.png) 

![image-20230312145728462](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312145728462.png)

所以当前的map结构是不能存进去的，因为他是byName的



![image-20230312150040493](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312150040493.png)



#### byType

![image-20230312164407075](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312164407075.png)

![image-20221025084540317](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221025084540317.png) 



在前面autowireByName的时候有一个registerDependentBean方法与他类似

![image-20230312164723903](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312164723903.png) 

![image-20230312164954605](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312164954605.png)

![image-20230312165051111](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312165051111.png) 





**为什么包装类可以作为类型转换器？**

因为BeanWrapper里面有一个具体的实现接口 TypeConverter

![image-20221025085127424](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221025085127424.png) 

![image-20221025085328982](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221025085328982.png) 

![image-20221026083840507](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221026083840507.png) 

![image-20221026083833578](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221026083833578.png)



  查找与type匹配的候选bean对象，构建成Map key=bean名,val = Bean对象

![image-20221026084126398](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221026084126398.png) 



**这一步是结果值的判断**

获取当前的单例对象，并且把当前结果放到candidates中

![image-20230312172731580](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312172731580.png) 





![image-20221026084855180](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221026084855180.png)

 

![image-20230312173106798](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312173106798.png) 



**会注入进去，按照类型注入的时候所有在当前容器中存在的map结构都能够找到**

![image-20221026085252994](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221026085252994.png) 



![image-20230312173421308](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312173421308.png)



按照类型进行注入的时候，他会按照类型进行注入，在当前容器中存在的key v的键值对我们都能够找到，只要是map类型就能匹配到，匹配到了就能够放进去。

![image-20230312173535390](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312173535390.png) 

 

按照类型进行注入的时候他只能获取一个具体的对象结果，如果按照是多个的 话根本不知道获取哪一个，而对应map的时候里面放的value又是一层map，把他给区分开了。

![image-20230312173927194](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230312173927194.png) 



所以map的话最好不要按照类型进行注入，因为他会多出很多东西来



**所以我们再进行bean注入的时候，需要确定好当前业务需要的是什么再选择注入的方式，如果我们需要的是某个指定的对象的话就用name，如果我们需要类型的话就用type，但是像map这种类型注入的时候可能会有很多奇怪的对象，这个需要注意**



### PropertyDescriptor数组

![image-20230313202727135](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313202727135.png)



postPropertyProperties方法在我们实现了InstantiationAwareBeanPostProcessor 接口的时候可以重写这个方法。

![image-20230313203205911](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313203205911.png)

实现InstantiationAwarePostProcessor接口

![image-20230313203335004](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313203335004.png)

可以对当前的对象中的值进行修改的操作。

![image-20230313203507636](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313203507636.png)

![image-20230313203606533](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313203606533.png)

![image-20230313203629479](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313203629479.png)





案例描述：

![image-20230313204258383](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313204258383.png) 

![image-20230313204612882](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313204612882.png) ![image-20230313204623760](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313204623760.png)

![image-20230313204249425](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313204249425.png)



这个时候personController的personService属性为null

![image-20230313204550842](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313204550842.png)

![image-20230313210037343](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210037343.png)

![image-20230313205939861](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313205939861.png)

![image-20230313210204966](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210204966.png)

![image-20230313210224521](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210224521.png) 

![image-20230313210306745](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210306745.png)

![image-20230313210335703](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210335703.png)



![image-20230313210714630](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210714630.png)

![image-20230313210730708](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313210730708.png)

![image-20230313211018162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313211018162.png) 

PersonDao->PersonService->PersonController

要把personDao创建完成之后后续的才能够继续创建



![image-20230313211206500](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313211206500.png)



### 创建bean对象的核心方法

**创建bean对象的核心方法有哪些：**

**getBean-> doGetBean -> createBean -> doCreateBean -> createBeanInstance（进行实例化的） -> populateBean（填充具体的属性值）**

![image-20230313211452367](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313211452367.png)



**personController->personService->personDao**

第一次在执行的时候肯定是先去创建personController这个对象，对他进行实例化。personController实例化完成之后就会执行populateBean()方法了，在执行populateBean()的时候会执行personService实例化,personService实例化完成之后会执行personService的populateBean方法，这个时候就又会执行personDao的实例化，personDao实例化完成之后就会执行personDao的populateBean。从哪进去，他就会回到哪。



![image-20230313221326321](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313221326321.png)

![image-20230313221340507](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313221340507.png)

![image-20230313221352565](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313221352565.png)



![image-20230313221653915](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313221653915.png)



应用属性值，对当前值进行解析

![image-20230313221855628](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313221855628.png)

![image-20230313222008530](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230313222008530.png)



## Spring的bean创建流程(七)



### Spring的bean创建流程applypropertyvalues方法讲解

![image-20230314083147077](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314083147077.png) 

为什么applyPropertyValues的代码会非常多？

因为我们在定义一个类的时候我们根本没办法预判到当前这个类存在多少属性，并且知道属性是什么类型的，所以必须吧这些点都考虑全面。



![image-20230314084352168](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314084352168.png)

 

![image-20230314084611007](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314084611007.png) 





![image-20230314084746685](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314084746685.png)

![image-20230314084838899](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314084838899.png)



为了不影响原来封装好了的集合里面的属性值。

后面的值或者属性不管怎么进行类型转换，都不会影响原来封装好的集合里面的对象属性值

![image-20230314084936157](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314084936157.png)





### 遍历属性

name的value为空也需要进行处理

![image-20230314193407815](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314193407815.png)



![image-20221116084553892](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221116084553892.png) 



resolve ValueIfNecessary ![image-20230314193730777](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314193730777.png)

比对判断了非常多的类型

![image-20230314193751715](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314193751715.png)



![image-20230314193942041](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314193942041.png)



将值解析完成之后放到我们之前准备好的深拷贝的集合里面，到后面统一进行处理

![image-20230314194307554](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314194307554.png)



针对引用类型，进行具体值对象的创建过程

![image-20230314194717198](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230314194717198.png)



**整体类似于一个递归的过程**





### 创建对象的流程

A->实例化->初始化->填充属性->判断是否是引用类型...



![image-20230315083714012](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315083714012.png) 

先创建A对象，A对象经过get A之后先对A队形进行实例化，实例化完成之后填充属性，填充属性的时候发现需要一个引用类型的对象，所以下一步该引用对象B也跟A对象一样经过get 再实例化 （或者直接从容器中取）



实例化、初始化、填充属性，有的话容器里面拿没有的话就创建，这些过程一直在递归循环。没办法具体循环递归几次，根据我们配置来决定的。

不管是new还是反射都是根据对应的构造方法来创建对象的。

 

![image-20230315200656924](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315200656924.png) 

 交由valueResolver根据pv解析出originalValue所封装的对象

![image-20230315201010545](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201010545.png) 

![image-20230315200944256](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315200944256.png)



又调用了resolveValueIfNecessary()方法

![image-20230315201111084](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201111084.png) 

![image-20230315201125018](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201125018.png)

![image-20230315201348547](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201348547.png) 



保证创建的对象都是独立唯一的

![image-20230315201541011](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201541011.png) 



创建Bean的对象。 

![image-20230315201644967](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201644967.png)



调整当前bean 的名称

![image-20230315201424822](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201424822.png)



大递归嵌套小递归

![image-20230315201952607](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315201952607.png)

上图流程是list的处理过程，其实还有set、Map....其他的集合的处理过程，也是类似的。

sourceMap

![image-20221118090424126](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221118090424126.png) 



给当前的propertys里面任何一种数据类型做一个匹配工作

![image-20230315203109250](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203109250.png) 



setPropertyValues才是开始真正的赋值工作

![image-20230315203214003](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203214003.png)



![image-20230315203303149](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203303149.png) 



获取属性的访问权限 getPropertyAccessorForPropertyPath(propertyName)![image-20230315203454991](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203454991.png)



![image-20230315203616888](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203616888.png)



![image-20230315203958897](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315203958897.png)

![image-20230315204013977](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230315204013977.png)













### populateBean属性功能



![image-20230316131438438](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316131438438.png)



![image-20221121140840796](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221121140840796.png) 

![image-20221121140824030](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221121140824030.png) 





**当 postProcessAfterInstantiation方法为true的时候，他还会继续往下执行代码，如果往下走的话就会继续走属性的处理，并且会走到applyPropertyValues方法，该方法会把属性值覆盖掉（因为里面有调用set方法）**

![image-20230316131513541](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316131513541.png)

![image-20230316131612142](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316131612142.png) 

![image-20230316131945897](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316131945897.png) 

![image-20230316132014989](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316132014989.png) 

**调用postProcessAfterInstantiation方法来完成属性的赋值工作，可以直接终止后续的值处理工作，也可以让后续的属性来完成覆盖操作，都取决于我们自己，是返回false还是返回true**

![image-20230316132146219](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316132146219.png)



**1、如上里面的例子：调用postProcessAfterInstantiation方法来完成属性的赋值工作，可以直接终止后续的值处理工作，也可以让后续的属性直接完成覆盖操作，取决于自己。（一般没人这么干）**

​	**postProcessAfterInstantiation**()

**2、根据配置文件的Autowire属性来决定使用名称注入还是类型注入**

​	**autowireByType()**

​	**autowireByName()**

**3、将对象中定义的@Autowired注解进行解析，并完成对象或者属性的注入**

​	**postProcessProperties() -> AutowiredAnnotationBeanPostProcessor-> inject()**

**4、根据property标签定义的属性值，完成各种属性值的解析和赋值工作**

​	**applyPropertyValues()**



![image-20221121142409046](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221121142409046.png) 



**为什么不把bean缓存到pvs里面， 这样就可以直接通过缓存直接调用了？**

populateBean方法中都是使用pvs这个对象来进行赋值的，但是他并没有修改我们的mbd(MergedBeanDefinition)。

![image-20230316132742338](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316132742338.png)

各个对象在创建的时候我们都要保存的时候最原始的BeanDefinition对象，所以是元数据，为了相互独立不要产生影响，不要想着把他改变。

类似deepCopy集合，创建了一个深拷贝对象，也是为了不影响原来的值。

![image-20230316133158976](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316133158976.png) 





### initializebean方法的功能



![image-20230316195919715](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230316195919715.png)

![image-20230317130426657](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317130426657.png) 

![image-20230317130524048](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317130524048.png) 



![image-20230317130535323](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317130535323.png) 



1、执行调用Aware接口对应的方法

​	1.BeanNameAware

​	2.BeanClassLoaderAware

​	3.BeanFactoryAware

2、执行before的初始化方法

​	1.在ApplicationContextAwareProcessor 接口中实现处理

​	2. 在CommonAnnotationBeanPostProcessor接口中实现	->	InitDestroyAnnotationBeanPostProcessor包含了两个注解	->	@PostConstructor

​																																																									  @PreDestroy

3、调用执行init-method     

​		1.实现了initializingBean接口之后调用afterPropertiesSet方法

​		2.调用执行用户自定义的初始化方法 init-method

4、执行after的初始化方法

​				AbstractAutoProxyCreator	->  主要是实现AOP这个功能



@PostConstruct在调用的时候也定义了init-method方法，而后面也会调用init-method方法，那么如果两边都定义了的话，init-method会调用几次？

只有一次，初始化方法不可能打印两次的。那么如何做限制定义他只能执行一次？应该是添加了标记为标注。



当我们处理@PostConstruct和@PreDestroy注解的时候都会执行相关的register方法

![image-20230317131030586](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131030586.png)

在check方法里面完成的注册

![image-20230317131056165](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131056165.png) 

 

![image-20230317131112967](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131112967.png)

其中包含了这个集合属性 externallyManagedInitMethods

![image-20230317131208252](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131208252.png)

回到initializeBean方法->invokeInitMethod-> invokeCustomerInitMethod

![image-20230317131410238](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131410238.png)

![image-20230317131518462](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131518462.png)

if语句中有一个判断

![image-20230317131618833](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131618833.png)

与上两个注解处理的一样，都是处理同一个具体的属性值，类似于一个缓存

![image-20230317131638241](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131638241.png)





invokeAwareMethods

![image-20230317131725327](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131725327.png)

![image-20230317131921190](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317131921190.png) 



invokeAwareMethods为什么只处理这三个方法呢？ 

![image-20221121202024105](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221121202024105.png) 



**因为前面已经忽略了三个了**

 ![image-20230317132055306](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132055306.png) 

![image-20230317132112721](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132112721.png) 

![image-20230317132154013](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132154013.png) 

找DefaultListableBeanFactory的父类构造方法

![image-20230317132201526](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132201526.png) 

这三个方法刚刚好对应invokeAwareMethods方法里的执行

![image-20230317132238226](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132238226.png)





 这几个是在ApplicationContextAwareProcessor中处理的

![image-20230317132413501](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132413501.png)

![image-20230317132452656](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132452656.png)

分为了两次 

如果自定义实现类中实现了这6个接口，他是通过我们的BeanPostProcessor的前置处理方法来实现的。
并不是在我们的invokeMethodAware里面进行相关的实现

![image-20230317132532516](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230317132532516.png) 







### Beanfactory功能



BeanFactory接口大体分为两个分支

BeanFactory：

​	1、AbstractBeanFactory

​		1.ListableBeanFactory

​		2.ConfigurableBeanFactory

​	2、ApplicationContext

​		1.ClassPathXmlApplicationContext

​		2.AnnotationConfigApplicationContext

​		3.FileSystemApplicationContext

![image-20230320154604089](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154604089.png) 

![image-20230320154758660](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154758660.png)

![image-20230320154808496](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154808496.png)

 ![image-20230320154847995](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154847995.png)

![image-20230320154909065](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154909065.png)

下面的ignore是属于beanFactory里面的东西的

![image-20230320154934469](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154934469.png)



这块相当于是ConfigurableBeanFactory部分的分类

![image-20230320154956162](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320154956162.png)

![image-20230320155029586](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320155029586.png)



![image-20230320155354054](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320155354054.png)



![image-20230320160330391](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320160330391.png)

![image-20230320160620748](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320160620748.png) 



CommonAnnotationBeanPostProcessor对应的处理是@PostConstruct、@PreDestroy、@Resource对应的 注解扫描和解析工作

AutowiredAnnotationBeanPostProcessor 对应的是@Autowired注解的扫描

ApplicationListenerDetector  查看是否属于ApplicationListener这个接口





![image-20221122084748743](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221122084748743.png) 

 ![image-20230320161441336](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320161441336.png)

![image-20230320162118195](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320162118195.png) 

虽然在代码块中遍历了很多次，但是大部分都是直接返回一个bean对象，如果需要做处理，就需要我们自定义做扩展实现了。

![image-20230320163742646](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320163742646.png) 



相关实现  这里报红因为ApplicationListenerDetector访问修饰符问题，但是意思就是那个意思。

![image-20230320164238497](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320164238497.png)



![image-20221122085049725](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221122085049725.png) ![image-20221122085025132](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221122085025132.png) 

对象实现该接口，允许最后一次对对象里面的属性进行修改

![image-20221122085238789](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221122085238789.png) 



执行验证所有的configuration配置，并且进行最终的初始化工作，在返回对象之前允许我们最后再改一次，可以最终修改bean 的属性值

![image-20230320164711430](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320164711430.png)

![image-20230320165023462](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320165023462.png) 

![image-20230320165031865](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320165031865.png)

![image-20230320165442681](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320165442681.png)





![image-20230320164454850](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320164454850.png)



执行完initializeBean方法之后相当于我们获取到了完整对象了，后面就需要把他加入的缓存里面去，方便我们后续对他进行拿取

![image-20230320165712341](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230320165712341.png)



如果使用@Bean我们自己new的对象交给Spring管理，Spring会帮我们调用initializeBean这个方法吗？







## Spring的bean创建流程总结及循环依赖问题



### bean对象放到容器里面



往三级缓存添加具体属性值的时候，添加的判断。

![image-20221125085003366](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221125085003366.png)

 

earlySingletonReference只有检测到有循环依赖的情况下他才会不为空

![image-20230321083522407](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321083522407.png)



实际依赖的bean是否是空的

![image-20230321083711472](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321083711472.png) 



为了后续在进行对象销毁的时候，进行调用的东西

![image-20230321083820544](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321083820544.png)



是否适合被销毁

![image-20221125084949350](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221125084949350.png) 





### Bean生命周期讲解

从xml或者注解开始，创建基本的对象



1、xml注解解析 ，**定义好xml文件，自定义java类之后**

2、创建容器对象obtainFreshBeanFactory （里面包含了几个基本的操作）

​	2.1、创建容器DefaultListableBeanFactory

​	2.2、设置某些属性值

​	2.3、加载配置文件，loadBeanDefinitions

​			2.3.1、为了解析配置文件，先把他转换成一个Document对象

​				2.3.1.1、Document中有一个对应的元素Element

​					2.3.1.1.1、Element分为两种类型，赋值之后会对对应元素进行解析：1、parseDefaultElement（bean标签）；2、parseCustomerElement（自定义的、aop开头的、context）标签的解析；

​				**2.3.1、2.3.1.1、2.3.1.1.1完成之后就是一个BeanDefinition对象，虽然解析出来了一个BeanDefinition，但是他是归属于GenericBeanDefinition，跟父容器进行合并（后续调用才会出发），最终得到的是RootBeanDefinition**



**定义好xml文件，自定义java类之后**

xml注解->创建容器对象obtainFreshBeanFactory->创建容器DefaultListableBeanFactory

​																						->设置某些容器值

​																						->加载配置文件loadBeanDefinitions

![image-20221207222707946](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221207222707946.png)

![image-20230321131548703](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321131548703.png) 

![image-20221207222916107](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20221207222916107.png) 

**1、prepareBeanFactory   给容器工厂设置某些属性值（比如：SPEL表达式、忽略注入的接口、注册的BeanPostProcessor）**

**2、invokeBeanFactoryPostProcessor->调用执行BFPP，可以修改或者引入其他的BeanDefinition，但是需要注意的是BFPP针对的操作对象是BeanFactory  ->例如ConfigurationClassPostProcessor（ImportResource、ComponentScan、Bean......）用来完成对相关注解的解析工作**

**3、registerBeanPostProcessor------>实例化BPP-----> 完成BeanPostProcessor的注册工作，方便后续在实例化完成之后调用before和after方法**

**4、finishBeanFactoryInitialization -----> 完成对对象的创建工作 ------->先从容器中找，找不到再创建----> 将需要创建的bean对象放到数组中，挨个进行创建**

**5、finishBeanFactoryInitialization------> 完成对对象的创建工作---先从容器中找，找不到再创建--->将需要创建的bean对象放到数组中，挨个进行创建**

![image-20230321200809173](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321200809173.png)



**getBean----->doGetBean**

![image-20230321201128565](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321201128565.png) 

 ![image-20230321201424258](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321201424258.png)



**进行具体的实例化操作----->createBean---->doCreateBean--------->createBeanInstance----> Supplier**

​																																					**------>factoryMethod**

​																																					 **------>通过反射的方式创建**

​																																					 **-------> BPP动态代理的方式(在前面步骤做的)**

 

![image-20230321201544936](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321201544936.png)

**createBeanInstance-------> applyMergedBeanDefinitionPostProcessors   注册生命周期接口（对于@PostConstruct和@PreDestroy和@Resource和@Autowired和@Value等相关注解的解析操作）**

 



![image-20230321201757308](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321201757308.png)



**applyMergedBeanDefinitionPostProcessors   ----->populateBean 完成了填充属性的步骤 ------>填充属性**

​																										**------>创建需要依赖的bean对象 -----> getBean ---->doGetBean---->MergedBeanDefinition......**





![image-20230321202441038](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20230321202441038.png)

populateBean --------> initializeBean  --------> 进行初始化工作--------> invokeAwareMethod --------->  BeanNameAware

​																																							----------->BeanClassLoaderAware

​																																							----------->BeanFactoryAware

​																----------->执行BPP的before方法------------>ApplicationAwarePostProcessor-------->继续实现某些Aware接口的set方法

​																														-----------> CommonAnnotationBeanPostProcessor(PostConstruct、PreDestroy)

​																----------->invokeInitMethod-------------->是否实现InitializationBean接口----------> afterPropertiesSet----->最后一次修改我们的属性值

​																												------------->执行用户自定义的init-method方法

​																----------->执行BeanPostProcessor的after方法  -------> aop

 

initializeBean ---------> 获取对象来进行相关操作

销毁流程--------------->DestructionAwareBeanPostProcessor -> postProcessBeforeDestruction

销毁流程--------------->DisposableBean

销毁流程--------------->自定义 destroyMethod









































































































