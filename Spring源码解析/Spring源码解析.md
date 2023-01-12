Spring源码解析



## Spring是什么？

**spring是一个框架 并且是一个生态，可扩展性非常强**

IOC更多的应该叫一个思想，DI的话相当于是一个手段

![image-20220624154229896](image/image-20220624154229896.png) 

![image-20220826083424616](image/image-20220826083424616.png) 



spring源码的实现过程

**BeanDefinition对象 bean的描述信息**

**加载xml->解析xml文件->封装bean对象（BeanDefinition bean的定义信息）->实例化->放到容器中->从容器中获取**



**容器（Map）**

![image-20220624155401911](image/image-20220624155401911.png) 



## IOC如何处理

容器中定义好一些bean之后可以通过getBean()直接获取的



定义规范、方便扩展。BeanDefinitionReader接口加载对应的Bean定义信息，解析处理环节



通过父子标签的嵌套关系，拆分成对应的Document对象。

![image-20220826085923381](image/image-20220826085923381.png) 

**BeanDefinitionReader是一个抽象接口**



**Spring中bean是否是单例的？**

由scope指定



**有多种**

![image-20220624161601877](image/image-20220624161601877.png) 



**最基本的反射方法**

![image-20220826090332342](image/image-20220826090332342.png) 



**BeanDefinition通过反射的方式创建对象，这样子方式可以获取到当前对象所有的属性等等信息，比较灵活。用new比较死板**

![image-20220624162207572](image/image-20220624162207572.png) 





### *BeanFactory

**Bean工厂，整个容器的根接口，也是容器的入口。**

类图

![image-20220826091527080](image/image-20220826091527080.png) 



**BeanDefinition反射到实例化，这个反射其实是个虚的，他分为很多复杂的步骤**

![image-20220624162734277](image/image-20220624162734277.png) 



BeanDefinition实例化生成具体对象



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

![image-20220624163420007](image/image-20220624163420007.png)

![image-20220624163457210](image/image-20220624163457210.png) 

例子：PlaceholderConfigurerSupport 替换占位符用



![image-20220624163716127](image/image-20220624163716127.png)

![image-20220624163724947](image/image-20220624163724947.png) 



### 实例化



**我们要将实例化和初始化这两个区分开来，他们不是同一个东西。**



Spring实例化创建对象

![image-20220624164719160](image/image-20220624164719160.png) 

一定是构造器执行完成之后才会去调用init-method方法的

**实例化环节，给堆里面开辟一个空间**





### 初始化



填充属性


调用set方法往里面设置属性值，例如：populate（填充属性）。

**设置Aware接口的属性**



#### Aware（意识到、感知）



实例化完成之后就是填充属性（populate()）

**设置Aware接口的属性**

ApplicationContext、ApplicationContextPostProcessor



***AbstractApplicationContext后续会重点讲**



**ignoreDependencyInterface忽略，为什么要忽略？**

![image-20220826204048643](image/image-20220826204048643.png) 



![image-20220826204208955](image/image-20220826204208955.png) 



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

![image-20220624170014644](image/image-20220624170014644.png)

![image-20220624170032921](image/image-20220624170032921.png) 



Bean的生命周期

![image-20220624170138377](image/image-20220624170138377.png) 



![image-20220624170546259](image/image-20220624170546259.png) 

AOP的动态代理方式是Cglib、Jdk的动态代理

![image-20220624170730349](image/image-20220624170730349.png) 



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

![image-20220624171701072](image/image-20220624171701072.png) 



**Aware接口到底有什么用？**

当Spring容器创建的bean对象在进行具体操作的时候，如果需要容器的其他对象，此时可以将对象实现Aware接口来满足当前的需要。



![image-20220624171944498](image/image-20220624171944498.png) 

## SpringBean



我们需要将SpringBean做一个区分

**普通对象（我们自定义的对象）、Spring容器对象（内置对象）**

![image-20220624172254821](image/image-20220624172254821.png) 

![image-20220828112616552](image/image-20220828112616552.png) 

普通对象相当于：我们自定义自己需要的对象

内置对象相当于：Spring需要的对象



**invokeBeanFactoryPostProcessors**

实例化并且执行所有已经实例化过的BeanPostProcessor Bean对象







## **观察者模式**



BeanFactory的子类实现应该竟可能多的去支持一个标准的Bean的生命周期接口

![image-20220624172828444](image/image-20220624172828444.png) 



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

![image-20220828114019429](image/image-20220828114019429.png) 



Environment在Context容器中定义了一个全局的值

![image-20221213204944520](image/image-20221213204944520.png) 

![image-20221213205046626](image/image-20221213205046626.png) 



**BeanDefinitionRegistry**

![image-20221213205510886](image/image-20221213205510886.png) 





**BeanFactory和FactoryBean的区别吗？**

**他们都是用来创建对象的**，当使用BeanFactory的时候必须要遵循完整的创建过程，这些过程是由Spring来管理控制的，而FactoryBean只需要调用getObject就可以返回具体对象，整个对象的创建过程都是由用户自己来控制的，更加灵活。

![image-20220624175256495](image/image-20220624175256495.png) 

BeanFactory和FactoryBean 的区别，他们都是用来创建对象的，当使用BeanFactory的时候就必须要遵循完整的Bean创建过程，就是完整的生命周期，这些过程都是由Spring来管理控制的。

而使用FactoryBean只需要调用getObject就可以返回具体的对象，整个对象的创建过程都是由用户自己来控制的，更加的灵活。



比较核心的点是在于，我们在获取容器对象的时候他每次在创建对象之前会做一个当前对象的判断。



## 接口对应的方法



 ![image-20221221220156716](image/image-20221221220156716.png)



![image-20220828151609555](image/image-20220828151609555.png) 

![image-20220828151728547](image/image-20220828151728547.png) 

![image-20220828151717526](image/image-20220828151717526.png) 



具体是哪里会调用getObject()这个方法呢？如下图

![image-20220625142036312](image/image-20220625142036312.png) 



![image-20221215214410594](image/image-20221215214410594.png) 

![image-20221215214444707](image/image-20221215214444707.png)

![image-20221215214642682](image/image-20221215214642682.png) 

![image-20221215214621015](image/image-20221215214621015.png) 



FactoryBean中的对象并不是容器创建好之后创建的

![image-20220625142221158](image/image-20220625142221158.png) 

A对象是由AFactoryBean 来创建的 

而是通过factory.getObject()方法创建出来的

A对象并不是整个容器创建好之后才创建的

![image-20220625142355634](image/image-20220625142355634.png) 

**意义**：

**就是当我们需要bean的时候是通过当前的工厂里面的getObject方法来创建的，而不需要去遵循bean的整个生命周期**



## AbstractAppliactionnContext源码讲解



这个方法包含了Spring启动的核心流程

***refresh()**

![image-20220625143951766](image/image-20220625143951766.png) 

**很多super的方法一定要去看父类的构造方法中到底做了些什么事情**



![image-20221222154438402](image/image-20221222154438402.png) 





这两个接口非常关键

**ListableBeanFactory**



**ConfigurableBeanFactory**



![image-20220828164006413](image/image-20220828164006413.png) 

 



在创建具体的一个对象的时候，是有父容器和子容器相关的概念的。

![image-20220828165458959](image/image-20220828165458959.png)



![image-20220625151609398](image/image-20220625151609398.png) 



![image-20221222160710522](image/image-20221222160710522.png)





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



![image-20221224225333131](image/image-20221224225333131.png) 



![image-20221224225638630](image/image-20221224225638630.png) 

Aware是为了获取系统容器中的某些对象。invokeAwareMethod()



BFPP

![image-20220625153315532](image/image-20220625153315532.png) 

因为实例化之后再修改BeanDefinition是没有意义的，因为在进行实例化的时候就是要根据我们的beanDefinition来获取里面的属性值，执行相关操作。

在实例化之前还需要进行一些基本的准备工作





## 实例化前期工作





![image-20221225222325911](image/image-20221225222325911.png)



![image-20221225222754088](image/image-20221225222754088.png) 

![image-20221225222805161](image/image-20221225222805161.png) 



![image-20221225223101064](image/image-20221225223101064.png) 





![image-20220625193003299](image/image-20220625193003299.png)



![image-20221225223155162](image/image-20221225223155162.png) 

![image-20221225223333034](image/image-20221225223333034.png) 



![image-20221225223426358](image/image-20221225223426358.png) 



![image-20221225223457416](image/image-20221225223457416.png) 



![image-20221225223610514](image/image-20221225223610514.png) 

![image-20221225223808703](image/image-20221225223808703.png) 



![image-20221225224036105](image/image-20221225224036105.png) 

![image-20221225224119703](image/image-20221225224119703.png) 

![image-20221225224228379](image/image-20221225224228379.png) 

![image-20221225224250902](image/image-20221225224250902.png) 





## 实例化



使用dependsOn可以改变bean的加载顺序，正常是加载A、B、C这样的顺序，如果AdependsOn于B、C，那么就会先加载B、C

![image-20220828230615929](image/image-20220828230615929.png) 



doCreateBean()

![image-20221226162655008](image/image-20221226162655008.png) 





反射的灵活性非常的高，反射如果只是使用几次是不会性能低的，如果重复调用了万级次数的话才会体现出来。

![image-20220625193415403](image/image-20220625193415403.png)  

![image-20221226162930306](image/image-20221226162930306.png) 

![image-20221226163029078](image/image-20221226163029078.png) 

![image-20221226163431840](image/image-20221226163431840.png) 



![image-20221226163422137](image/image-20221226163422137.png) 



![image-20221226163615210](image/image-20221226163615210.png)

实例化只完成一件事情，在堆中创建一快空间，当前对象的属性值都是默认值。
![image-20220829093801107](image/image-20220829093801107.png) 





解决循环依赖的代码

![image-20221226163823567](image/image-20221226163823567.png) 

![image-20221227215611967](image/image-20221227215611967.png) 



**提前暴露，构造器形式的循环依赖没办法解决，如果是set的话是可以解决的，因为把实例化和初始化分开了。**

**暴露的是只完成实例化，但是未完成初始化的步骤，也就是默认值为空的对象给他暴露出去、**





![image-20220625194024868](image/image-20220625194024868.png) 

**为什么要使用三级缓存**？

关键点在于，getEarlyBeanReference方法里面。



![image-20220625194225885](image/image-20220625194225885.png) 

填充属性

![image-20221228225055919](image/image-20221228225055919.png) 



![image-20221229084005415](image/image-20221229084005415.png) 

设置AWare接口的属性值，执行对应的属性填充操作

![image-20220625194633317](image/image-20220625194633317.png) 

执行BeanPostProcessor前置处理方法

![image-20220625194723552](image/image-20220625194723552.png) 

![image-20221229084041026](image/image-20221229084041026.png) 

![image-20220625194731715](image/image-20220625194731715.png) 

当我们在实例化一个bean对象的时候，可能包含n多个PostProcessor



执行initMethod方法

![image-20220625194826177](image/image-20220625194826177.png) 



执行BeanPostProcessor 的after方法

![image-20220625194906123](image/image-20220625194906123.png)



![image-20221229084121969](image/image-20221229084121969.png) 



销毁

![image-20220625195054329](image/image-20220625195054329.png) 



记录创建对象过程中的状态

![image-20220625195338773](image/image-20220625195338773.png) 

![image-20221229084520037](image/image-20221229084520037.png) 

![image-20221229084441860](image/image-20221229084441860.png) 



![image-20221229084633612](image/image-20221229084633612.png) 



**如果一个类实现了FactoryBean这个接口的话，要强调一件事情就是他要创建两个对象。先创建FactoryBean的一个对象，有这个对象之后在调用getObject()才会返回我们自定义创建的对象。**

![image-20221229084806762](image/image-20221229084806762.png) 



## spring的具体细节工作

![image-20220625205156245](image/image-20220625205156245.png) 



用来解析当前系统需要运行的时候所需要的一些资源。

![image-20221230083006702](image/image-20221230083006702.png) 

![image-20220625214157091](image/image-20220625214157091.png)  

![image-20220625214259877](image/image-20220625214259877.png) 

![image-20221230083101943](image/image-20221230083101943.png) 

![image-20221230083244959](image/image-20221230083244959.png) 





资源加载器



![image-20221230083456461](image/image-20221230083456461.png) 



![image-20220829141023902](image/image-20220829141023902.png)



变量替换（一般没人这么干，只是提供了方式）

![image-20220625233800058](image/image-20220625233800058.png) 

![image-20221230084056485](image/image-20221230084056485.png) 



### 属性值设置

![image-20230102163630202](image/image-20230102163630202.png) 

![image-20230102163650793](image/image-20230102163650793.png) 

![image-20230102163719126](image/image-20230102163719126.png) 



替换占位符，配置文件

![image-20230102170245834](image/image-20230102170245834.png) 

![image-20230102172358700](image/image-20230102172358700.png) 

![image-20230102172844503](image/image-20230102172844503.png) 

![image-20230102172902145](image/image-20230102172902145.png) 

 ![image-20230102172950883](image/image-20230102172950883.png) 





![image-20220626000310184](image/image-20220626000310184.png) 

**AbstractRefreshableConfigApplicationContext**



### InitPropertySources讲解



![image-20230102174020316](image/image-20230102174020316.png) 



该方法在**ClassPathXmlApplicationContext**中是一个空实现，自己实现扩展。

![image-20220626000854869](image/image-20220626000854869.png) 



调用的是当前父类的构造方法

![image-20220829145016384](image/image-20220829145016384.png)

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



![image-20220829145122688](image/image-20220829145122688.png)  



![image-20220829145046473](image/image-20220829145046473.png) 



**报错误**

![image-20220829145247058](image/image-20220829145247058.png) 

原因：没有找到对应的属性值 [abc]



![image-20220829145347316](image/image-20220829145347316.png) 

设置为username

![image-20220829145420794](image/image-20220829145420794.png) 



例子：自己扩展当前属性的环境变量值

![image-20230102181123070](image/image-20230102181123070.png) 

![image-20220626000751951](image/image-20220626000751951.png) 



### getEnvionment讲解



![image-20230102225402691](image/image-20230102225402691.png) 

![image-20230102225416408](image/image-20230102225416408.png) 



上下文中设置RequiredProperties属性，下面的代码才有意义

```java
	protected void initPropertySources() {
		System.out.println("自定义扩展initPropertySources");
		//设置必要的属性值
		getEnvironment().setRequiredProperties("username");
	}
```

![image-20230102225432604](image/image-20230102225432604.png) 

![image-20220626001644696](image/image-20220626001644696.png) 



**验证需要的属性文件是否都已经放入环境中**

![image-20220626001339174](image/image-20220626001339174.png) 

判断，没有值的换会抛异常

![image-20220626001705202](image/image-20220626001705202.png) 



```
applicationListeners虽然在spring源码中是空的，但是在SpringBoot的中有对其进行添加
```

![image-20230102230105605](image/image-20230102230105605.png) 



![image-20220829151303829](image/image-20220829151303829.png)

![image-20230102230257524](image/image-20230102230257524.png) 



**SpringBoot** 里面有14个

![image-20220829151447234](image/image-20220829151447234.png) 







### prepareRefresh讲解

1、设置容器的启动时间
2、设置活跃 true、关闭状态为false
3、获取Environment对象，并加载当前系统属性值到Environment对象中
4、准备监听器和事件的集合对象，默认为空的集合

![image-20220626002451311](image/image-20220626002451311.png) 

![image-20220626002456339](image/image-20220626002456339.png)





### Bean工厂讲解



![image-20220626002523806](image/image-20220626002523806.png)



![image-20230102231554267](image/image-20230102231554267.png)  



![image-20220626002748242](image/image-20220626002748242.png)



![image-20220626002738176](image/image-20220626002738176.png) 

![image-20230103222121015](image/image-20230103222121015.png) 



allow...的属性值现在都是等于true

![image-20220829163800641](image/image-20220829163800641.png) 



下图的allow...属性都是null

![image-20220829164008018](image/image-20220829164008018.png) 



更改为“**不自动处理我们的循环依赖问题**”，我们只需要重写customizeBeanFactory这个方法就行

![image-20220829165054523](image/image-20220829165054523.png) 

 

![image-20220829165219137](image/image-20220829165219137.png) 



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

![image-20220626003956575](image/image-20220626003956575.png) 





### **loadBeanDeifinitions讲解

![image-20230103225655825](image/image-20230103225655825.png) 

**套娃**

![image-20220829165434997](image/image-20220829165434997.png) 

![image-20230103230056147](image/image-20230103230056147.png) 

**适配器模式**

![image-20230103230118601](image/image-20230103230118601.png) 

![image-20230103230144200](image/image-20230103230144200.png) 

![image-20230103230154346](image/image-20230103230154346.png) 

![image-20230103230218273](image/image-20230103230218273.png) 

![image-20230103230229539](image/image-20230103230229539.png) 

......



为什么要进行那么多方法的重载？

为了加载我们bean的定义信息 

![image-20220829165658873](image/image-20220829165658873.png) 



![image-20220626101450561](image/image-20220626101450561.png) 



```java
//实体处理器   用他来读取我们本地的xsd或者dtd文件来完成相关的解析工作
beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
```

![image-20220829194946994](image/image-20220829194946994.png) 

![image-20220829195333589](image/image-20220829195333589.png) 



![image-20220829195440728](image/image-20220829195440728.png) 



只是指定了配置文件，没有对其进行一个解析处理的工作

![image-20220829195545143](image/image-20220829195545143.png)

![image-20220829195646691](image/image-20220829195646691.png) 

![image-20220829195741288](image/image-20220829195741288.png) 



这个文件是在什么时候开始读取的呢？

![image-20220829195927458](image/image-20220829195927458.png) 

![image-20220829195954826](image/image-20220829195954826.png) 



idea会默认帮我们调用一些信息

![image-20230104085217352](image/image-20230104085217352.png) 



![image-20220626102838145](image/image-20220626102838145.png) 





![image-20230104085523091](image/image-20230104085523091.png)  

![image-20230104085549833](image/image-20230104085549833.png)  



configLocations在当前这个类就设置过了，所以可以直接拿来用。

![image-20230104085610934](image/image-20230104085610934.png) 

之前设置过 **前后是关联的**

![image-20220829200808079](image/image-20220829200808079.png) 



## Spring配置文件加载过程 （4节）





### DefaultListableBeanFactory讲解

![image-20220626110307077](image/image-20220626110307077.png) 



### loadBeanDeifinitions讲解



xml文件标签属性的文档规范大部分都是由dtd或者xsd来定义的，xsd用得比较多

![image-20230104224647884](image/image-20230104224647884.png) 





加载bean 的定义信息

![image-20230104230018150](image/image-20230104230018150.png) 



![image-20230105082237462](image/image-20230105082237462.png) 



![image-20230105082503325](image/image-20230105082503325.png) 





不管是什么类型的配置文件，都有一个BeanDefinitionReader用来读取他的相关配置信息

![image-20230105083430648](image/image-20230105083430648.png) 

![image-20230105083553477](image/image-20230105083553477.png) 



![image-20230105083617060](image/image-20230105083617060.png) 

![image-20230105083912927](image/image-20230105083912927.png) 

![image-20230105084013792](image/image-20230105084013792.png) 



循环配置文件数组，处理每一个单一的具体对象

![image-20230105084030521](image/image-20230105084030521.png) 



![image-20230105084131629](image/image-20230105084131629.png) 



资源类加载器是否实现了ResourcePatternResolver

![image-20230105084302203](image/image-20230105084302203.png) 

![image-20230105084346707](image/image-20230105084346707.png) 





![image-20230105084523641](image/image-20230105084523641.png) 



resourcePatternResolver 这个对象的创建是在前面的 ClassPathXmlApplication对象的构造方法的supper里面创建的一直点supper 然后点this()，如下 第二张图

![image-20230105084622445](image/image-20230105084622445.png) 

![image-20230105084754687](image/image-20230105084754687.png) 



![image-20230105084830762](image/image-20230105084830762.png) 



指定ClassPath下的某一个路径，可以匹配前缀解析

![image-20230105085212815](image/image-20230105085212815.png) 



![image-20230105085447566](image/image-20230105085447566.png) 

 ![image-20230105085626413](image/image-20230105085626413.png) 

![image-20230105085800762](image/image-20230105085800762.png) 







loadBeanDefinition方法从String[]->String->Resource[]->Resource，最终将Resource读取成为了一个Document文档，根据文档的节点信息，封装成一个一个的BeanDefinition对象。

![image-20230105085928588](image/image-20230105085928588.png) 

![image-20230105085939071](image/image-20230105085939071.png) 



![image-20230105090150736](image/image-20230105090150736.png) ![image-20230105090201713](image/image-20230105090201713.png) 





### 创建对象封装到BeanDefinitions

最主要的工作是为了进行实例化操作，对元素标签进行解析操作



![image-20220626120902170](image/image-20220626120902170.png) 

![image-20230106211800439](image/image-20230106211800439.png) 

![image-20230106211911950](image/image-20230106211911950.png) 



 核心处理流程代码 

![image-20230106212105028](image/image-20230106212105028.png) 

可以在配置文件里面处理profile

![image-20230106212226534](image/image-20230106212226534.png) 



![image-20230106212608771](image/image-20230106212608771.png)  

![image-20230106215311603](image/image-20230106215311603.png) 

一行一行的在读xml配置文件

![image-20230106215503038](image/image-20230106215503038.png) 

![image-20230106215514400](image/image-20230106215514400.png) 

![image-20230106215628363](image/image-20230106215628363.png) 



相关处理工作

根据命名空间来选择对应的解析处理

![image-20230106215814402](image/image-20230106215814402.png) 

![image-20230106220013628](image/image-20230106220013628.png) 



![image-20230106220033687](image/image-20230106220033687.png) 



![image-20230106220121490](image/image-20230106220121490.png) 



![image-20230106230849753](image/image-20230106230849753.png)

![image-20230106231505515](image/image-20230106231505515.png) 



**GenericBeanDefinition**和**RootBeanDefinition**是一个平级的关系

![image-20230106231549409](image/image-20230106231549409.png) 



![image-20230106232146838](image/image-20230106232146838.png) 

![image-20230106232850812](image/image-20230106232850812.png) 

![image-20230106232859687](image/image-20230106232859687.png) 

![image-20230106233505952](image/image-20230106233505952.png) 

![image-20230106233659150](image/image-20230106233659150.png) 

后续都是一些数据的解析工作，其实都是差不多的，没必要深究。





### 对bean元素详细解析

![image-20220626124617084](image/image-20220626124617084.png) 



该方法结束之后意味着，我们已经完成了从我们的xml文件中解析到了对应的一些标签和元素，将这些元素都转成了一个BeanDefinition，再将这些BeanDefinition放到了BeanFactory里面，此时BeanFactory里面已经包含了一些Bean的定义信息了。

![image-20220901082607296](image/image-20220901082607296.png) 



![image-20220901082800752](image/image-20220901082800752.png) 

![image-20230107221853395](image/image-20230107221853395.png) 

![image-20230107221903010](image/image-20230107221903010.png) 



![image-20230107222134173](image/image-20230107222134173.png) 

![image-20230107222147022](image/image-20230107222147022.png) 

![image-20230107222205151](image/image-20230107222205151.png) 

![image-20230107222404554](image/image-20230107222404554.png) 



![image-20230107222621934](image/image-20230107222621934.png) 

![image-20230107222749773](image/image-20230107222749773.png) 

```
beanDefinitionMaps 相当于是放BeanDefinition对象的map集合，他是必须要根据名字来获取BeanDefinition的而我们要根据beanDefinitionNames里面的name来获取map里面的BeanDefinition

```

![image-20230107223035066](image/image-20230107223035066.png) 

例如下图： 所有的BeanDefinition都是通过beanDefinitionNames集合中的名字来获取的。

![image-20230107223200549](image/image-20230107223200549.png) 





## Spring自定义标签解析过程



### 回顾

这两边的loadBeanDefinition()使用的是同一个（在spring中很多代码都是复用的）

![image-20230109143259393](image/image-20230109143259393.png)

![image-20230109143609507](image/image-20230109143609507.png) 



自定义bean标签所涉及到的解析方法

![image-20230109144127275](image/image-20230109144127275.png) 





### parsecustomElement解析



***命名空间处理器  NameSpaceHandler**

![image-20230109144929330](image/image-20230109144929330.png) 





META-INF -> Spring.schemas

handlers -> spring.handlers

xxx.dtd、xxx.tsd。从网络环境中进行相关的下载工作，映射成本地的资源文件。

![image-20220903103754246](image/image-20220903103754246.png) 

![image-20220903103902908](image/image-20220903103902908.png) 



要在获取当前这个处理类之前把这些handler类都拿过来。

![image-20230109144814867](image/image-20230109144814867.png) 



![image-20230109145038790](image/image-20230109145038790.png) 

初始化类的标签

![image-20220903104054506](image/image-20220903104054506.png) 

 

![image-20220902221020897](image/image-20220902221020897.png) 

1、加载spring.handlers配置文件

2、将配置文件内容加载到map集合中

3、根据指定的key去获取对应的处理器



**如果需要自定义标签的话，我们应该做以下步骤：**

**1、创建一个对应的解析器处理类（在init方法中添加parser类）**

**2、创建一个普通的spring.handler配置文件，让应用程序能够完成加载工作**

**3、创建对应标签的parser类。（对当前标签的其他属性值进行解析工作）**

![image-20230109210841553](image/image-20230109210841553.png) 

![image-20230109210852295](image/image-20230109210852295.png) 

![image-20230109210902806](image/image-20230109210902806.png) 



![image-20230109210940768](image/image-20230109210940768.png) 

![image-20230109210947958](image/image-20230109210947958.png) 

![image-20230109210952372](image/image-20230109210952372.png) 



![image-20220903105556725](image/image-20220903105556725.png) 

![image-20230109212644765](image/image-20230109212644765.png) 



 DefaultNamespaceHandlerResolver

![image-20230109212701033](image/image-20230109212701033.png) 

getHandlerMappings() 方法与解析.schemas文件的PluggableSchemaResolver 类似

![image-20230109212727767](image/image-20230109212727767.png) 



![image-20230109212803281](image/image-20230109212803281.png) 



当执行完DefaultNamespaceHandlerResolver的构造方法之后

![image-20230109213005631](image/image-20230109213005631.png) 

 namespaceHandlerResolver属性中就有对应的值了

![image-20230109213039654](image/image-20230109213039654.png) 

 

这边能显示的原因是因为**DefaultNamespaceHandlerResolver**构造方法中有toString方法,由idea给我们执行的。

![image-20230109213056143](image/image-20230109213056143.png) 

![image-20230109213219259](image/image-20230109213219259.png) 



![image-20230109214116377](image/image-20230109214116377.png) 

![image-20230109214230776](image/image-20230109214230776.png) 



###  元素解析器

**如果需要自定义标签的话，我们应该做以下步骤：**

**1、创建一个对应的解析器处理类（在init方法中添加parser类）**

**2、创建一个普通的spring.handler配置文件，让应用程序能够完成加载工作**

**3、创建对应标签的parser类。（对当前标签的其他属性值进行解析工作）** 

![image-20230110082957965](image/image-20230110082957965.png) 



![image-20230110083230143](image/image-20230110083230143.png) 

![image-20230110083238023](image/image-20230110083238023.png) 

通过名称取到对应的Parser对象

![image-20230110083622326](image/image-20230110083622326.png) 

![image-20230110083639723](image/image-20230110083639723.png) 

![image-20230110083649469](image/image-20230110083649469.png) 

![image-20230110083702197](image/image-20230110083702197.png) 

![image-20230110083716369](image/image-20230110083716369.png) 



往beanDefinitionMaps和beanDefinitionNames集合里面放对应的属性值

![image-20230110083954111](image/image-20230110083954111.png) 

![image-20230110084838083](image/image-20230110084838083.png) 





### 自定义标签完成解析工作





参考该类，我们就可以自定义标签

![image-20220904100610269](image/image-20220904100610269.png) 

Handler参考该类

![image-20220904101417588](image/image-20220904101417588.png) 



**spring.schemas、spring.handlers两个配置文件命名问题，会因为这段代码而报错**

![image-20220904152137797](image/image-20220904152137797.png) 

![image-20230111193334523](image/image-20230111193334523.png) 



**开始自定义**

要实现的功能：

```
自定义一个<lyx:user username email  age> </lyx:user>，完成整个标签的处理工作
```



![image-20230110202504974](image/image-20230110202504974.png) 

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

![image-20230111193837512](image/image-20230111193837512.png) 





## Spring的bean工厂准备工作





### prepareBeanFactory

![image-20230111194009103](image/image-20230111194009103.png) 

给某些属性做一些初始化的工作

![image-20230111195224901](image/image-20230111195224901.png) 





SPel表达式处理流程：

一开始需要一个处理类：StandardBeanExpressionResolver->在这个处理类的内部有一个Spel表达式的解析类->里面又包含了一个当前解析类所包含的配置类。

![image-20220904161058236](image/image-20220904161058236.png) 



![image-20230111200256310](image/image-20230111200256310.png) 

![image-20230111200401799](image/image-20230111200401799.png) 

![image-20230111200313607](image/image-20230111200313607.png) 

![image-20230111200424953](image/image-20230111200424953.png)  



在填充属性步骤的时候才会调用该方法

![image-20230111200435987](image/image-20230111200435987.png) 





### 如何扩展实现自定义属性编辑器



![image-20230111201849182](image/image-20230111201849182.png) 

![image-20230111202019497](image/image-20230111202019497.png) 





1、需要提前把要用的对象注册进去。自定义一个实现了PropertyEditorRegister接口的编辑器

2、需要让spring能够识别到此编辑器，自定义实现一个属性编辑器的注册器，实现了PropertyEditorRegistrar接口

3、让spring能够识别到对应的注册器



如果要实现自定义属性编辑器的话就要重写这个方法

![image-20220905092724002](image/image-20220905092724002.png) 

1、自定义一个实现了PropertyEditorSupport

2、让spring能够识别到此编辑器，自定义实现一个属性编辑器的注册器，实现PropertyEditorRegistrar接口

3、让Spring能够识别到对应的注册器

![image-20220905093440226](image/image-20220905093440226.png) 

![image-20220905093545193](image/image-20220905093545193.png) 

问题：ResourceEditorRegistrar为什么能找到CustomerEditorConfigurer这个类

因为CustomerEditorConfigurer实现了BeanFactoryPostProcessor

![image-20220905093935162](image/image-20220905093935162.png) 



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

```java
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

![image-20220626131023289](image/image-20220626131023289.png) 



![image-20220905105039455](image/image-20220905105039455.png) 



### 其他配置方式

直接通过CustomerEditors设置进去

![image-20220905111820097](image/image-20220905111820097.png)

​	





### addBeanPostProcessor

ApplicationContextAwareProcessor这里实现Aware其他相关的实现类

![image-20220905112336020](image/image-20220905112336020.png) 

![image-20220905112350013](image/image-20220905112350013.png) 



定义一个MyAwareProcess实现BeanPostProcessor

在postProcessorBeforeInitialization此方法中可以设置其他的属性值



![image-20220905122906097](image/image-20220905122906097.png) 





### ignoreDependencyInterface

反射在进行值处理的时候有两种方式：

1、获取该属性对应的set方法来进行操作；2、获取到该属性对象field的set方法设置

![image-20220905141417731](image/image-20220905141417731.png) 

![image-20220905141343544](image/image-20220905141343544.png) 

```java
//设置要忽略自动装配的接口，因为这些几口的实现是由容器通过set方法进行注入的，
//所以在使用autowrie进行注入的时候需要将这些接口进行忽略
```







## Spring的beanFactoryPostProcessor的执行

@Autowried是通过什么方式设置进去的

### 内容回顾

prepareBeanFactory()方法





### *invokeBeanFactoryPostProcessor



该接口处理的是BeanDefinitionRegistry这个类

registry并不是操作map和names的，因为Map里面放的是BeanDefinition，而Names里面放的是BeanDefinition的名称，他们执行两个集合，而registry是用来操作BeanDefinition的，只不过在前期的工厂对象准备的时候，我们把这些BeanDefinition设置到工厂里面去了，但具体的增删改查我们则需要从map里面取到，取到之后才能做具体的操作

![image-20220905145109688](image/image-20220905145109688.png) 

![image-20220905145810829](image/image-20220905145810829.png) 



BeanFactoryPostProcessor接口处理的是ConfigurableListableBeanFactory

![image-20220905145122248](image/image-20220905145122248.png) 

BeanFactory包含了BeanDefinition



![image-20220905150408098](image/image-20220905150408098.png) 

注册BeanFactoryPostProcessor的几种方式

![image-20220905150530901](image/image-20220905150530901.png) 

第一种

![image-20220905150248123](image/image-20220905150248123.png) 

![image-20220905150302403](image/image-20220905150302403.png) 

第二种：

![image-20220905150233656](image/image-20220905150233656.png) 

以上两种方式都可以



### invokeBeanFactoryPostProcessor执行流程



**BeanDefinitionRegistryPostProcessor**

上面这个接口集合是下面这个接口集合的子集，上面继承了下面

**BeanFactoryPostProcessor**



先处理子集，再处理父集

#### 在整个Spring的运行期间可以有多个BFPP，那么应该按照什么顺序执行呢？

一般我们使用的是@Ordered来去排序他们的执行。

![image-20220905151321689](image/image-20220905151321689.png) 

首先处理PriorityOrdered->再处理Ordered->最后还有一个没有实现任何排序接口的对象。



![image-20220905151742653](image/image-20220905151742653.png) 

所以我们可以把它们放到一个集合里面，然后循环调用执行就行了。



### 排序

**记住，所有的Registry都是对前面这个对象的增删该查的操作**



如果是一个BeanDefinitionRegistryProcessor的话，那么就会先执行postProcessBeanDefinitionRegistry方法，然后给他放到集合中

![image-20220905154923563](image/image-20220905154923563.png) 

 ![image-20220905160310376](image/image-20220905160310376.png) 



BeanFactoryPostProcessor是用来处理我们的BeanFactory对的，而BeanPostProcessor是用来处理我们的bean对象的。

当我们bean实例化完成之后，我们就要再对我们的BeanPostProcessor进行处理了。虽然在前期处理的时候往里面添加了很多BeanPostProcessor，但是只是为了后面做准备的。



BeanFactoryPostProcessor我们在进行修改的时候，修改的是BeanFactory里面存在的所有对象，而BeanDefinitionRegistryPostProcessor是用来修改我们的BeanDefinition的，两个东西锁针对的对象是不同的。



#### 优先排序及回答问题

![image-20220905173349232](image/image-20220905173349232.png) 



#### 为什么再处理

前置条件没有满足的时候，就需要这一步骤去操作

![image-20220905202820434](image/image-20220905202820434.png) 



## spring的BeanFactoryPostProcessor的执行2



### BeanFactoryPostProcessor再次讲解

可以理解为是一个增强类，也可以理解为是一个后置处理器

![image-20220905222105034](image/image-20220905222105034.png) 

BeanFactory里面的所有东西理论上只要我们实现了BeanFactoryPostProcessor这个接口我们都是可以改的

他有一个子接口为BeanDefinitionRegistryPostProcessor



![image-20220905222443349](image/image-20220905222443349.png) 



如果自定义实现了BeanFactoryPostProcessor接口，那么像让Spring识别到的话有两种方式：

1、定义在Spring的配置文件中，让Spring自动识别

2、调用具体的addBeanFactoryPostProcessor方法



![image-20220905224409423](image/image-20220905224409423.png) 

如果归属于BeanDefinitionRegistryPostProcessor这个子类，就会优先调用postProcessBeanDefinitionRegistry方法，先让这个方法开始执行，如果没有的话那么就剩下postProcessBeanFactory() 方法，这个方法可以放到后面一起处理，而不用挨个的去调用。



![image-20220906081637534](image/image-20220906081637534.png) 

![image-20220906082259610](image/image-20220906082259610.png) 



如何验证上图呢，首先就要先实现BeanDefinitionRegistryPostProcessor的子类，只有这样的子类在进行识别的时候才能找到对应的处理逻辑。

![image-20220906085015444](image/image-20220906085015444.png) 

![image-20220906085032861](image/image-20220906085032861.png) 



**疑问：他是如何去调用postProcessBeanFactory方法的呢？他不应该是createBean才实例化吗？没有实例化是怎么去调用方法的？**

![image-20220906085358862](image/image-20220906085358862.png) 

首先获取到了这些容器的名称，然后通过getBean方法去出发实例化（他里面会调用createBean方法）。



### postProcessBeanFactory

regularPostProcessors 存的是add进去的BFPP

  ![image-20220906133800045](image/image-20220906133800045.png)



![image-20220906135243447](image/image-20220906135243447.png)

   



### BeanDefiniton1

```java
@Component//如果想让当前这个类被识别到的话应该怎么做？
public class PersonService {
}

//添加ComponentScan扫描
<!--为什么开启这个的时候就能够识别到自己注册的类呢？-->
<context:component-scan base-package="com.msb"></context:component-scan>
```

BeanDefinition是一个接口：包含了常规的Bean定义信息，GenericBeanDefinition、RootBeanDefinition



注意：如果我们识别了一个被扫描的组件的话，当前这个组件的上面还应该包含一些其他的相关信息。

![image-20220907082856213](image/image-20220907082856213.png) 

![image-20220907085456042](image/image-20220907085456042.png) 



![image-20220907083608028](image/image-20220907083608028.png) 

当上面的代码识别完Component注解类之后，后续还需要再进行其他注解的一些识别，因为一个类上面不止是只能放一个注解的。

![image-20220907083553384](image/image-20220907083553384.png) 

其他的注解都是通过**ConfigurationClassPostProcessor**这个来实现的

![image-20220907083742580](image/image-20220907083742580.png) 



### BeanDefinition2



![image-20220907212442049](image/image-20220907212442049.png) 





## Spring的 ConfigurationClassPostProcessor的讲解





### ConfigurationClassPostProcessor解析1

他是一个配置类的后置处理器

当我们开启@ComponentScan这个注解的时候，前期会自动注入一个internalConfiguration类。

![image-20220908082651982](image/image-20220908082651982.png) 



![image-20220908083147214](image/image-20220908083147214.png) 



![image-20220908084235011](image/image-20220908084235011.png) 



![image-20220908084539661](image/image-20220908084539661.png) 

![image-20220908084533681](image/image-20220908084533681.png) 



![image-20220908084558929](image/image-20220908084558929.png) 





![image-20220908085551522](image/image-20220908085551522.png) 

![image-20220908085537018](image/image-20220908085537018.png)

![image-20220908085529529](image/image-20220908085529529.png) 



### 内部类

类里面包含的内部类中加了注解，并且类还能无线套娃。

![image-20220908091050854](image/image-20220908091050854.png) 

会通过递归去处理这样的情况



### ConfigurationClassPostProcessor解析2



我们在定义注解的时候，注解里面锁包含的类有可能也包含注解，所以需要递归去调用。他是一个层层迭代的关系。

![image-20220909082610573](image/image-20220909082610573.png) 





### MyComponentScan

![image-20220909083544271](image/image-20220909083544271.png) 





### ConfigurationClassPostProcessor解析3



#### @Bean是如何处理的



 ![image-20220911155554990](image/image-20220911155554990.png) 





解析我们所需要的被注解给修饰的BeanDefinition

![image-20220911202227532](image/image-20220911202227532.png) 

整体工作最终还是为了完成我们的BeanDefinition，在实例化之前，我们必须把这个应用程序所存在的所有BeanDefinition都解析完成。









## 注册BeanPostProcesser



### Import到底是如何解析的

递归处理

![image-20220912112820179](image/image-20220912112820179.png) 

获取当前有哪些类需要进行相关的导入操作

![image-20220912113022312](image/image-20220912113022312.png) 

imports用来存放import注解的类，visited 用来存放递归关系的



![image-20220912113055599](image/image-20220912113055599.png) 



通过ImportSelector导入key，去spring.factories文件中找到对应的value 导入我们具体的配置类

![image-20220912143927742](image/image-20220912143927742.png) 

![image-20220912143838176](image/image-20220912143838176.png) 



processImports：导入具体的配置类，同时完成了具体的实例化工作。



**AutoConfigurationImportSelector**



getCandidateConfigurations：当我们在处理BFPP的时候里面有一个ConfigurationPostProcessor这个类主要是用来处理我们一些注解的解析工作，里面包含了@Component、@PropertySource、@ComponentScan、@Bean、@Import， 当我们在解析@Import的时候我们会在我们的启动类开始一步一步递归的查找，这个时候里面会识别到一个@ImportSelector类，在这个类进行解析的时候会有一个延迟加载的属性，在延迟加载的时候会有一个getImports方法，在这个方法里面会获取到一个AutoConfigurationEntry这个对象，在获取Entry对象的时候会调用里面的getCandidateConfigurations方法，此时能把配置文件所对应的属性值都加载回来，来完成整个自动装配的解析流程。





### registerBeanPostProcessors



![image-20220912153932806](image/image-20220912153932806.png) 

 获取到internal的两个对象![image-20220912160035260](image/image-20220912160035260.png) ![image-20220912155635518](image/image-20220912155635518.png)

根据下面的名词，识别到具体对应的类

![image-20220912155657427](image/image-20220912155657427.png) 

![image-20220912155721238](image/image-20220912155721238.png) 



![image-20220912160617665](image/image-20220912160617665.png) 



![image-20220912162344409](image/image-20220912162344409.png) 



BeanPostProcessor所针对的处理对象是bean对象

实例化开始、初始化结束、完成使用、销毁



销毁相关：![image-20220912162658864](image/image-20220912162658864.png) 

**DestructionAwareBeanPostProcessor 销毁前调用的**









## Spring的消息资源和监听器的初始化



### 回顾及initMessageSource

![image-20220912190644681](image/image-20220912190644681.png) 

该方法会在SpringMVC的时候通过国际化的代码重点讲。



### 观察者模式



原始观察者模式     被观察者、观察者

被观察者中需要存储一个观察者的一个集合，执行不同方法的时候要调用观察者的方法进行处理

观察者：里面需要定义一个方法，看到被观察者不同行为的时候触发的反应

![image-20220916085516356](image/image-20220916085516356.png) 

 

![image-20220916085353845](image/image-20220916085353845.png) 





监听事件、监听器、多播器、监听源



### 事件驱动

在传统的观察者模式中会有两个对象，一个是 被观察者，还有一个观察者就可以了。



Spring中定义了   

事件、监听器、多播器、事件源



#### 事件

Event，把被观察者具体要执行的动作拿出来当做了一个事件   

**具体要执行的动作**

![image-20220918141219611](image/image-20220918141219611.png) 



#### 监听器

观察者，可能存在多个，接收不同的事件来做不同的处理动作

![image-20220918141526661](image/image-20220918141526661.png) 



#### 多播器

把被观察者遍历观察者通知消息的操作拿出来委托给一个多播器来进行消息通知，或者说通知观察者进行不同的操作。



#### 事件源

谁来调用或者执行发布具体的事件。



**将普通的观察者模式解耦**



把事件先定义出来，把事件能做什么操作拿出来。把监听器作为一个观察者，后面由监听器单独来处理观察者要做的一些处理操作，多播器来完成我们的处理操作，相当于原来被观察者中的for循环。事件源来进行发布事件。



**逻辑执行过程：**

1、事件源来发布不同的事件

2、当发布事件之后会调用多播器的方法来进行事件广播操作，由多播器去触发具体的监听器去执行操作

3、监听器接收到具体的事件之后，可以验证匹配是否能够处理当前事件，如果可以，直接处理，如果不行，不做任何操作。



**实际代码处理：**

1、**提前准备好N多个事件**

2、初始化多播器（创建多播器对象，此多播器对象中应该包含一个监听器的集合）  initApplicationEventMulticaster

3、准备好一系列的监听器    

4、向多播器中注册进去已有的监听器   registerListeners

5、准备事件发布，来通知多播器循环调用监听器进行相关的逻辑处理工作   publishEvent 



![image-20220918195535353](image/image-20220918195535353.png) 



 在这之前我们没有定义过applicationEventMulticaster

![image-20220918175358751](image/image-20220918175358751.png) 



SimpleApplicationEventMulticaster的父类中有一个add方法，方法中会添加监听器类。

![image-20220918175909044](image/image-20220918175909044.png) 

![image-20220918175936457](image/image-20220918175936457.png) 





![image-20220918180311527](image/image-20220918180311527.png)

![image-20220918180316580](image/image-20220918180316580.png) 



![image-20220918180911968](image/image-20220918180911968.png) 





![image-20220918181224080](image/image-20220918181224080.png) 



这两个集合的区别就是， 我们可以有一个对象集合，也可以有一个名字集合，我们能够通过名字集合来获取相关的实例。

![image-20220918181208533](image/image-20220918181208533.png) 

![image-20220918181442934](image/image-20220918181442934.png)

后续的源码处理也是这样子的

![image-20220918181609855](image/image-20220918181609855.png) 



![image-20220918185440413](image/image-20220918185440413.png) 

![image-20220918185555906](image/image-20220918185555906.png) 



![image-20220918185823413](image/image-20220918185823413.png) 





![image-20220918190037020](image/image-20220918190037020.png) 



**initApplicationEventMulticaster和applicationEventMulticaster这两个是同一个东西吗？**

不是同一个，他们的作用域和作用空间都是不一样的。





**initialMulticaster初始化  他是什么时候完成具体的注册的**

![image-20220914101256022](image/image-20220914101256022.png) 



![image-20220914101703517](image/image-20220914101703517.png) 



 ![image-20220914101929552](image/image-20220914101929552.png)







## Spring的bean创建流程（一）

### finishBeanFactoryInitialization类

![image-20220919101507782](image/image-20220919101507782.png) 

**getBean->doGetBean->createBean->doCreateBean**



哪些地方调用了getBean()  

BeanFactoryPostProcessor、BeanPostProcessor

![image-20220919102424574](image/image-20220919102424574.png) 



 转换器提前储备![image-20220919102555906](image/image-20220919102555906.png)



Spring给我提供了三个converter转换的父类接口，我们可以通过这些接口来自定义类型转换

![image-20220919103504270](image/image-20220919103504270.png) 



Converter：一对一的类型转换；GenericConverter：一对多的类型转换；ConverterFactory多对多的类型转换。



自定义添加Converter官网实例：

![image-20220919105253914](image/image-20220919105253914.png) 

![image-20220919110022522](image/image-20220919110022522.png) 





![image-20220919154747595](image/image-20220919154747595.png) 



![image-20220919153058354](image/image-20220919153058354.png)



### 缓存会不会引起缓存的泄露和getMerged

缓存对象的时候，要缓存怎么样的对象？

如果对象的属性值一直都在变化，我们不要对他进行缓存

会引发内存泄露。



**刚刚在进行使用的时候，使用了递归的方式调用，问题是递归的过程是怎么样的？**

先从缓存中获取，缓存中有，直接返回。缓存中没有，提前创建。



**getMergedLocalBeanDefinition->他的意义在于，在实例化之前，要把所有的基础的BeanDefinition对象，转换成RootBeanDefinition对象进行缓存，后续再马上需要实例化的时候直接获取定义信息，而定义信息中如果包含父类，那么必须要先创建父类才能有子类，父类如果没有的话子类怎么创建。**







### BeanFactory和FactoryBean

都是对象工厂，用来创建具体对象的。

如果使用BeanFactory的接口那么必须要严格遵守SpringBean的生命周期，从实例化、到初始化、到invokeAwareMethod.invokeInitMethod、before、after.......，此流程非常复杂且麻烦，如果需要一种更加便捷简单的方式创建，则需要使用FactoryBean这个接口，不需要遵循此创建的顺序。



#### FactoryBean

![image-20220919155355939](image/image-20220919155355939.png) 

getObjectType：返回类型

isSingleton：判断是否单例

getObject：直接返回对象



![image-20220919155924362](image/image-20220919155924362.png) 

定义好之后如何交给Spring进行控制？

![image-20220919160158430](image/image-20220919160158430.png) 

![image-20220919160227951](image/image-20220919160227951.png) 



&地址符

![image-20220919161402392](image/image-20220919161402392.png) 

**当使用FactoryBean接口来创建对象的时候，我一共创建了几个对象？**

两个，一个是实现了FactoryBean接口的子类对象，二是通过getObject方法返回的对象



![image-20220919161712997](image/image-20220919161712997.png) 

![image-20220919161753446](image/image-20220919161753446.png) 



![image-20220919162052955](image/image-20220919162052955.png) 





![image-20220919162555083](image/image-20220919162555083.png) 

![image-20220919162715634](image/image-20220919162715634.png) 

![image-20220919174516283](image/image-20220919174516283.png) 

![image-20220919162909251](image/image-20220919162909251.png) 



![image-20220919162934922](image/image-20220919162934922.png) 



![image-20220919162947349](image/image-20220919162947349.png) 



![image-20220919163143597](image/image-20220919163143597.png) 

![image-20220919163159613](image/image-20220919163159613.png) 



实现了FactoryBean接口的对象在一级缓存中，但是调用getObject所创建的对象是放到factoryBeanObjectCache里面的，没有在一、二、三级缓存中

![image-20220919163433660](image/image-20220919163433660.png) 



![image-20220919163707842](image/image-20220919163707842.png) 

**如果不是单例对象的话，那么需要每次调用的时候重新创建，缓存中不会保存当前对象。**





**getBean->doGetBean->createBean->doCreateBean**



## Spring的bean创建流程（二）

new、反射、factoryMethod、supplier

**如果一个bean对象实现了FactoryBean接口，那么在整个spring的生命周期中会创建两个对象，第一个标识实现了FactoryBean接口的子类，由Spring帮我们创建，会调用getObject方法来返回一个对象，此对象的创建过程就是由getObjectForBeanInstance方法调用实现的。**

 

![image-20220919193744692](image/image-20220919193744692.png) 



![image-20220919194207333](image/image-20220919194207333.png) 

我们做操作的时候可以每次都根据beanName获取新的RootBeanDefinition，但是一种更有效率的方式是在第一次处理的时候放到缓存中，之后可以对缓存中的对象进行获取，不需要每次都创建新的。

![image-20220919194201331](image/image-20220919194201331.png) 



### getDependsOn和创建bean的实例对象

![image-20220919194934511](image/image-20220919194934511.png) 



**ObjectFactory是一个函数式接口，当调用其中的getObject方法的时候，才会将实际传递的匿名内部类中的实现逻辑来进行执行**

**新生状态->开始创建->创建过程中->创建结束**



实例化-> 填充属性-> 执行aware接口方法->执行init方法->执行后置处理器方法



![image-20220919200141504](image/image-20220919200141504.png) 





### lookup-method、replace-method

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

![image-20220920082910532](image/image-20220920082910532.png) 

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

![image-20220920083850613](image/image-20220920083850613.png) 

![image-20220920083840655](image/image-20220920083840655.png) 



![image-20220920084527376](image/image-20220920084527376.png) 



![image-20220920084655746](image/image-20220920084655746.png) 



**当把bean设置为原型模式的时候**

![image-20220920090110450](image/image-20220920090110450.png) 

spring会通过拦截器的方式，在我们每次需要的时候都会去创建最新的对象，而不会把原型对象缓存起来。





![image-20220920090721156](image/image-20220920090721156.png)  ![image-20220920090704432](image/image-20220920090704432.png) 

所以需要配置lookup-method才能使得每次获取的都是最新的。



题外话：

![image-20220920091400212](image/image-20220920091400212.png) 



![image-20220920091533317](image/image-20220920091533317.png) 

为什么在spring中有那么多的cglib





![image-20221007161143926](image/image-20221007161143926.png) 



## Spring的bean创建流程（三）

### doGetBean方法

 ![image-20220926164924340](image/image-20220926164924340.png)



### 如何创建对象

![image-20221007162554728](image/image-20221007162554728.png) 

```java
public class BeforeInstantiation {

	public void doSomeThing() {
		System.out.println("执行 do some thing......");
	}

}
```

```java
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

	/**
	 * 实例化之前
	 *
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName  the name of the bean
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

		System.out.println("beanName" + beanName + "----执行postProcessBeforeInstantiation");

		//使用cglib实现动态代理。
		if (beanClass == BeforeInstantiation.class) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(beanClass);
			enhancer.setCallback(new MyMethodInterceptor());
			BeforeInstantiation bi = (BeforeInstantiation) enhancer.create();
			System.out.println("创建代理对象:" + bi);
			return bi;
		}
		return null;
	}

	/**
	 * 实例化之后
	 *
	 * @param bean     the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return
	 * @throws BeansException
	 */
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

		System.out.println("beanName" + beanName + "----------执行postProcessAfterInstantiation方法");
		return false;
	}

	/**
	 * 初始化之前
	 *
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName" + beanName + "----------执行postProcessBeforeInitialization方法");

		return bean;
	}

	/**
	 * 初始化之后
	 *
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("beanName" + beanName + "----------执行postProcessAfterInitialization方法");
		return bean;
	}


	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
		System.out.println("beanName" + beanName + "----------执行postProcessProperties方法");
		return pvs;
	}

}
```

```java
public class MyMethodInterceptor implements MethodInterceptor {
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

		System.out.println("目标方法执行之前：" + method);
		Object o1 = methodProxy.invokeSuper(o, objects);
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

	<bean class="com.msb.resolveBeforeInstantiation.BeforeInstantiation" id="beforeInstantiation"/>
	<bean class="com.msb.resolveBeforeInstantiation.MyInstantiationAwareBeanPostProcessor" id="awareBeanPostProcessor"/>

</beans>
```



**当执行到invokeBeanFactoryPostProcessors的时候会创建对象吗？**

不会，因为上面的例子是BPP的实现子类，所以不会创建对象。在registerBeanPostProcessors里面会调用执行。

![image-20221007162928035](image/image-20221007162928035.png) 

**我们自定义的BeanPostProcessor，Spring并不会提前帮我们生成代理对象，会按照doCreateBean的方式去严格去创建**



完成了registerBeanPostProcessors的注册之后，后面就会有我们自己定义的BPP了。会在最后一个方法finishBeanFactoryInitialization中创建，也还是通过doCreateBean的方式创建。



这里会判断我们当前执行的逻辑中是否包含了有可能提前创建对象的BeanPostProcessors。

是否会执行doCreateBean取决于在之前定义的BPP里面是否包含了提前创建Bean对象这么 一个BPP，如果不包含才往下走。

![image-20221007164531690](image/image-20221007164531690.png) 

后面加到一级缓存中的是一个代理对象。



如果这边不使用动态代理生成对象有什么问题吗？

![image-20221007165241177](image/image-20221007165241177.png) 

如果不使用动态代理的话就不能使用拦截方法了，因为我们设置了一个Interceptor()，我们例子中所对应的方法doSomething就不会调用了。但是new出来的对象还是会被spring管理，还是会往一级缓存放，但是在方法调用的时候就不会有这些前置处理的逻辑了。



#### **在Spring中有几种方式可以帮我们创建对象？**

1. 自定义BeanPostProcessor生成代理对象（InstantiationAwareBeanPostProcessor）。
2. 通过反射创建对象。
3. 通过factoryMethod创建对象。
4. FactoryBean的方式创建对象（调用getObject方法）。
5. 通过supplier（供给器）创建对象。



实例工厂、静态工厂

![image-20221007170114482](image/image-20221007170114482.png) 



**supplier方式创建对象**

![image-20221007170801916](image/image-20221007170801916.png)

 

![image-20221007170910156](image/image-20221007170910156.png) 



**supplier为空？我们应该如何给他设置进去？**

在BeanDefinition的实现类中的父类AbstractBeanDefinition中。

![image-20221007200500179](image/image-20221007200500179.png) 

![image-20221007200639265](image/image-20221007200639265.png) 



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

![image-20221007213102937](image/image-20221007213102937.png) 



![image-20221007213326128](image/image-20221007213326128.png) 

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



![image-20221007215333291](image/image-20221007215333291.png) 

![image-20221007215448569](image/image-20221007215448569.png) 



![image-20221007222504900](image/image-20221007222504900.png)



![instantiateUsingFactoryMethod大致流程](image/instantiateUsingFactoryMethod大致流程.jpg)



## spring的bean创建流程（四）



### 通过反射创建对象



![image-20221008084203929](image/image-20221008084203929.png) 

![image-20221008084245905](image/image-20221008084245905.png) 

![image-20221008084256200](image/image-20221008084256200.png) 

![image-20221008084332283](image/image-20221008084332283.png) 

![image-20221008084408366](image/image-20221008084408366.png) 



### 创建构造器实例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id = "person" class="com.msb.init.Person" scope="prototype">
		<constructor-arg name="id" value="1"/>
		<constructor-arg name="name" value="lisi"/>
	</bean>

</beans>
```



### 讲解构造器的过程

![image-20221008084902565](image/image-20221008084902565.png) 

![image-20221008084845440](image/image-20221008084845440.png) 

 

![image-20221009084632736](image/image-20221009084632736.png) 



### 构造器实例化和工厂实例化有什么区别

![image-20221009085952388](image/image-20221009085952388.png) 



要满足这个条件他才能识别得到

![image-20221010084452142](image/image-20221010084452142.png) 





### 反射实例化

![image-20221011122713083](image/image-20221011122713083.png) 



![image-20221011123046704](image/image-20221011123046704.png) 



![image-20221011123124362](image/image-20221011123124362.png) 

![image-20221011123552405](image/image-20221011123552405.png) 

![image-20221011123653772](image/image-20221011123653772.png) 

![image-20221011123713152](image/image-20221011123713152.png) 



#### 实例化策略

1、Simple实例化策略

	1. 无参构造实例化
	2. 有参构造实例化
	3. 工厂方法实例化

2、Simple的子类又有一些延伸的扩展，cglib实例化策略

 	1. 动态代理对象
 	  	1. 无参构造
 	  	2. 有参构造



![image-20221011124300121](image/image-20221011124300121.png) 

![image-20221011124312364](image/image-20221011124312364.png) 

![image-20221011124402023](image/image-20221011124402023.png) 



![image-20221011124814416](image/image-20221011124814416.png) 





对象在创建完成之后是在堆里面开辟了块空间，开辟完空间之后还需要进行属性的填充

所以要先将实例对象进行一个包装，再进行属性的类型转换等等操作

![image-20221011125328716](image/image-20221011125328716.png) 



![image-20221011152525130](image/image-20221011152525130.png) 

![image-20221011152537060](image/image-20221011152537060.png)



![image-20221011152727977](image/image-20221011152727977.png) 



![image-20221011153026704](image/image-20221011153026704.png) 





![image-20221011154304578](image/image-20221011154304578.png) 

![image-20221011154439344](image/image-20221011154439344.png) 



什么时候Destroy会被执行？

![image-20221011154531256](image/image-20221011154531256.png) 

![image-20221011155058538](image/image-20221011155058538.png) 

![image-20221011155047612](image/image-20221011155047612.png) 



![image-20221011155401833](image/image-20221011155401833.png) 



**initAnnotationType和destroyAnnotationType是在哪里赋值的？**

![image-20221011160124217](image/image-20221011160124217.png) 



**metadata其实只是中间数据，最后还是要把数据都写会BeanDefinition，在整个bean的创建过程中BeanDefinition是贯彻始终的**





## BeanPostProcessor



![image-20220626132520146](image/image-20220626132520146.png) 

spring通过postProcessMergedBeanDefinition方法找出所有需要注入的字段，同时做缓存

![image-20220626133731851](image/image-20220626133731851.png) 

![image-20220626134232118](image/image-20220626134232118.png) 



![image-20220626134419761](image/image-20220626134419761.png) 





![image-20220626134625971](image/image-20220626134625971.png) 





 ![image-20220626134837174](image/image-20220626134837174.png) 



![image-20220626134904225](image/image-20220626134904225.png) 



### checkConfigMembers

方便后续的BeanDefinition能够进行直接的调用操作

![image-20220626135034073](image/image-20220626135034073.png) 



![image-20220626135204938](image/image-20220626135204938.png) 



###  buildResourceMetadata

![image-20221013085005584](image/image-20221013085005584.png) 

![image-20220626135517800](image/image-20220626135517800.png) 



加载注解的相关解析配置工作

CommonAnnotationBeanPostProcessor（@PostConstruct、@PreDestroy、@Resource）



### AutowiredAnnotationBeanPostProcessor



![image-20221013085247164](image/image-20221013085247164.png) 



用来处理@Autowired、@Value注解的

先find、再build

![image-20220626141358428](image/image-20220626141358428.png) 

![image-20220626141408811](image/image-20220626141408811.png) 

![image-20220626141433686](image/image-20220626141433686.png) 



![image-20221017162807612](image/image-20221017162807612.png) 

![image-20221017162729695](image/image-20221017162729695.png) 



调用构造器将相应注解注册到集合中

![image-20221017162936624](image/image-20221017162936624.png) 

![image-20220626141600322](image/image-20220626141600322.png) 



**所有的注解处理和解析工作一定是在后续的某个步骤统一来完成的。**

![image-20221017163556982](image/image-20221017163556982.png) 

![image-20220626141856244](image/image-20220626141856244.png)  



inject是按照类型来注入的。



![image-20221017163704917](image/image-20221017163704917.png) 

![image-20221017164206768](image/image-20221017164206768.png) 

![image-20220626141920036](image/image-20220626141920036.png) 



![image-20220626142534334](image/image-20220626142534334.png) 



**区分哪些是被注解处理的和哪些是被xml文件处理的**

![image-20221017164655594](image/image-20221017164655594.png) 





如果配置冲突了呢？

 

### 初始化和循环依赖

![image-20221021082943197](image/image-20221021082943197.png) 



**当实例化完成之后，要开始初始化赋值操作了，但是赋值的时候，值的类型有可能是引用类型，需要从spring容器中获取具体的某个对象来完成赋值操作，而此时，需要引用的对象可能被创建了，也可能没被创建，如果被创建了，那么直接获取即可，如果没有创建，在整个过程中就会涉及到对象的创建过程，而内部对象的创建过程中又会有其他的依赖，其他的依赖中有可能包含当前的对象，而此时当前对象还没有创建完成，所以此时产生了循环依赖的问题。**



A对象中有B对象的属性，B对象中有A对象的属性

![image-20220626180428673](image/image-20220626180428673.png) 



**如何解决？**

​	提前暴露出来，申请一块缓存出来，用来存放具体的对象。

​	但是这个时候的对象是一个**只完成了实例化，但是未完成初始化的对象**，是个**半成品**不能用

![image-20220626180707057](image/image-20220626180707057.png) 

所以将对象进行区分：分为**半成品缓存**和**成品缓存**都是使用的map结构来存储



我们在获取对象的时候分为了两个步骤，实例化和初始化。

**问题是：初始化环节的填充属性阶段有没有可能会改变当前对象的地址空间？**

不会，因为实例化完成之后地址已经生成，不可能改变。

 

**但是，我们不得不考虑另外一种情况，spring在使用的时候会配置AOP，需要会生成具体的代理对象**

#### **1、生成代理对象之前，要不要生成普通对象？**

要，配置aop的类是不是普通bean对象？是，既然是一个普通对象，那所有后续的工作都要经过完整的创建步骤

![image-20220626205406741](image/image-20220626205406741.png) 

所以这个bean对象是肯定要创建的。





**但是代理不是在resolveBeforeInstantiation的时候就创建了吗？**
![image-20221024102536286](image/image-20221024102536286.png) 

这边的创建和我们的循环依赖创建是没关系的，我们要通过这个方法创建对象的话就必须要自定义BeanPostProcessor实现InstationAwareBeanPostProcessor接口，才能创建这个bean定义，而循环依赖是后面才进行的操作。



 





#### 2、**我们能否确定什么时候会调用具体的对象？无论是普通对象还是代理对象**

不能



#### 3、**有没有可能，在Spring中同时存在同一个beanName的普通对象和代理对象？**

如果有的话，那么我在调用的时候是根据beanName来获取对象，此时我要获取到两个对象吗？那么我应该用哪个？

**我们应该分块存储**

![image-20221021091232570](image/image-20221021091232570.png) 

**那么可以创建两个缓存，分别存储普通对象和代理对象。**



#### 4、问题：上面的这两个缓存，我优先获取哪个？

**优先获取代理对象，然后才是普通对象，是个错误的结论**

代理对象中应该包含了普通对象所具备的所有功能，如果一个对象需要被代理了，那么此时普通对象就可以不存在了，只要代理对象即可。



**也就是说在后续创建代理对象之后，就可以使用代理对象来覆盖普通对象，普通对象就可以不存在了。**



回到第二个问题中，什么时候要调用对象？

我在调用对象的时候，如果检测到当前对象需要被代理，那么直接创建代理对象覆盖即可。



**怎么能够在随时需要的时候创建代理对象呢？**

**类似于观察者，但是不是观察者，传递进去一个生成代理对象的一个匿名内部类，当需要调用的时候，直接调用匿名内部类（lambda）来生成代理对象，就是类似于回调机制**



所以我们需要一个额外的共建来存储代理对象，生成代理对象的缓存。

![image-20221024105119822](image/image-20221024105119822.png) 





#### 5、在刚刚的步骤中说到了初始化，初始化包含哪个环节？

​	1、填充属性；

​	2、执行Aware接口所对应的方法；

​	3、执行BeanPostProcessor中的before方法；

​	4、执行init-method方法；

​	5、执行BeanPostProcessor中的after方法

**上述步骤执行完成之后是为了获取到一个完整的成品对象，但是在初始化前 我们能够确定哪一个对象需要生成代理对象吗？**

不能确定，并且我们知道三级缓存只是一个回调机制，**所以能否把所有的bean所需要的创建代理对象的lambda表达式都放到三级缓存中？**

可以将所有bean对象需要创建代理对象的lambda表达式放到三级缓存中，后续如果我需要调用，直接从三级缓存中调用执行即可，如果不需要，在生成完整对象之后可以把三级缓存中的lambda表达式个清除掉（往一级缓存中放成品的时候就可以清除）。



#### 6、我在什么时候要生成具体的代理对象呢？（代码实操）

​	1、**在进行属性注入的时候，调用该对象生成的时候检测是否需要被代理，如果需要，直接创建代理对象**

​	2、**在整个过程中，没有其他的对象有当前类的依赖，那么在生成最终的完整对象之前生成代理对象即可（BeanPostProcessor中的after方法）**

**提前暴露对象：说的就是二级缓存中的对象，只完成了实例化，但未初始化的对象**



**三级缓存在进行对象查找的时候，顺序是什么样子的？**

先找一级，一级没有找二级，二级没有找三级



**有没有可能直接从三级缓存到一级缓存？**

有可能（后面会讲）



**不要三级缓存可以吗**？

如果单纯为了解决循环依赖问题，那么使用二级缓存足够解决问题了，三级缓存存在的意义是为了处理代理，如果没有代理对象，二级缓存足以解决问题。

![image-20220626214205094](image/image-20220626214205094.png) 





**如何解决循环依赖问题？**

最本质的问题是在于，实例化和初始化是分步骤分开处理的，当完成实例化之后就可以让其他对象引用当前对象了，只不过当前对象不是一个完整对象而已，后续需要完成此对象的剩余步骤。

直接获取半成品对象的引用地址，保证对象能够被找到，而半成品对象在堆空间中是否有设置的属性值，无所谓。



B实例化后赋值给A的B属性，那么B生成代理对象之后，A中的B是如何处理的。

B已经确定是代理对象了



所有的对象在进行创建的时候都要遵循bean的生命周期，那么在刚开始的时候会进行实例化的操作，然后在BeanPostProcessor的后置处理方法里完成代理对象的生成，**但是实际循环依赖的发生点是在创建代理对象之前，也就是populatebean方法里，这个时候对象还不会生成代理对象，只会有原始对象**，所以上课的时候我说生成代理对象前面都会有原始对象



不管是否需要代理，我们都会往三级缓存中添加lambda表达式。

![image-20221024155214674](image/image-20221024155214674.png) 

![image-20221024155053061](image/image-20221024155053061.png) 







## Spring的bean创建流程图和知识点



### populateBean 填充bean的属性

给属性赋值



分类：

1、基本数据类型：直接完成赋值操作

2、引用类型：先从容器中获取具体的对象值，如果有，直接赋值，如果没有就创建



注入的方式：

1、不注入

2、按照类型完成注入

3、按照名称完成注入

4、按照构造器完成注入



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

到doCreateBean方法中开始 进入到populateBean()方法

![image-20221024161424008](image/image-20221024161424008.png) 



![image-20221024161458540](image/image-20221024161458540.png) 

可以在postProcessAfterInstantiation方法中改变bean对象的属性值



![image-20221024161835877](image/image-20221024161835877.png) 

![image-20221024162243324](image/image-20221024162243324.png) 



![image-20221024163003009](image/image-20221024163003009.png) 

![image-20221024162950833](image/image-20221024162950833.png) 

![image-20221024163147290](image/image-20221024163147290.png) 



![image-20221024163854574](image/image-20221024163854574.png) 

#### byName

![image-20221024164234533](image/image-20221024164234533.png) 



#### byType



![image-20221025084540317](image/image-20221025084540317.png) 

为什么包装类可以作为类型转换器？

因为BeanWrapper里面有一个具体的实现接口 TypeConverter



![image-20221025085127424](image/image-20221025085127424.png) 

![image-20221025085328982](image/image-20221025085328982.png) 

![image-20221026083840507](image/image-20221026083840507.png) 

![image-20221026083833578](image/image-20221026083833578.png) 

![image-20221026084126398](image/image-20221026084126398.png) 

结果值的判断





![image-20221026084855180](image/image-20221026084855180.png)

 

按照类型注入的时候所以在当前容器中存在的map结构都能够找到

![image-20221026085252994](image/image-20221026085252994.png) 

![image-20221026084828170](image/image-20221026084828170.png)



### propertydes ciptor数组



### 创建bean对象的核心方法

**getBean-> doGetBean -> createBean -> doCreateBean -> createBeanInstance -> populateBean**















## Spring的bean创建流程七



### Spring的bean创建流程applypropertyvalues方法讲解

### ![image-20221115083618424](image/image-20221115083618424.png)

 

### 遍历属性

![image-20221116084553892](image/image-20221116084553892.png) 



### 创建对象的流程



sourceMap

![image-20221118090424126](image/image-20221118090424126.png) 





### populateBean属性功能



![image-20221121140840796](image/image-20221121140840796.png) 

![image-20221121140824030](image/image-20221121140824030.png) 

1、如上里面的例子：调用postProcessAfterInstantiation方法来完成属性的赋值工作，可以直接终止后续的值处理工作，也可以让后续的属性直接完成覆盖操作，取决于自己。（一般没人这么干）

​	postProcessAfterInstantiation

2、根据配置文件的Autowire属性来决定使用名称注入还是类型注入

​	autowireByType

​	autowireByName

3、将对象中定义的@Autowired注解进行解析，并完成对象或者属性的注入

​	postProcessProperties -> AutowiredAnnotationBeanPostProcessor

4、根据property标签定义的属性值，完成各种属性值的解析和赋值工作

​	applyPropertyValues



![image-20221121142409046](image/image-20221121142409046.png) 





### initializebean方法的功能



1、执行调用Aware接口对应的方法

​	1.BeanNameAware

​	2.BeanClassLoaderAware

​	3.BeanFactoryAware

2、执行before的初始化方法

​	1.ApplicationContextAwareProcessor

​	2.CommonAnnotationBeanPostProcessor	->	InitDestroyAnnotationBeanPostProcessor	->	@PostConstructor

​																																												@PreDestroy

3、调用执行init-method    

​	1.实现类initializingBean接口之后调用afterPropertiesSet方法

​	2.调用执行用户自定义的初始化方法 init-method

4、执行after的初始化方法

​	AbstractAutoProxyCreator	-> AOP







![image-20221121202024105](image/image-20221121202024105.png) 

![image-20221121202014678](image/image-20221121202014678.png) 







### beanfactory功能

BeanFactory：

​	1、AbstractBeanFactory

​		1.ListableBeanFactory

​		2.ConfigurableBeanFactory

​	3、ApplicationContext

​		1.ClassPathXmlApplicationContext

​		2.AnnotationconfigApplicationContext

​		3.FileSystemApplicationContext



![image-20221121204207438](image/image-20221121204207438.png) 





 ![image-20221122084748743](image/image-20221122084748743.png) 

 ![image-20221122085049725](image/image-20221122085049725.png) ![image-20221122085025132](image/image-20221122085025132.png) 

对象实现该接口，允许最后一次对对象里面的属性进行修改

![image-20221122085238789](image/image-20221122085238789.png) 





## Spring的bean创建流程总结及循环依赖问题 （四）



### bean对象放到容器里面



![image-20221125085003366](image/image-20221125085003366.png) 

![image-20221125084949350](image/image-20221125084949350.png) 





### Bean生命周期讲解



定义好xml文件，自定义java类之后

xml注解->创建容器对象obtainFreshBeanFactory->创建容器DefaultListableBeanFactory

​																						->设置某些容器值

​																						->加载配置文件loadBeanDefinitions

![image-20221207222707946](image/image-20221207222707946.png) 

![image-20221207222916107](image/image-20221207222916107.png) 

prepareBeanFactory   给容器工厂设置些属性值



invokeBeanFactoryPostProcessor->调用执行BFPP，可以修改或者引入其他的BeanDefinition，但是需要注意的是BFPP针对的操作对象是BeanFactory  ->  ConfigurationClassPostProcessor用来完成对相关注解的解析工作



registerBeanPostProcessor ---实例化BPP---> 完成BeanPostProcessor的注册工作，方便后续在实例化完成之后调用before和after方法



finishBeanFactoryInitialization  -> 完成对对象的创建工作 ----先从容器中找，找不到再创建----> 将需要创建的bean对象放到数组中，挨个进行创建



getBean -------> doGetBean





进行具体的实例化操作----->createBean---->doCreateBean--------->createBeanInstance----> Supplier

​																																					------>factoryMethod

​																																					 ------>通过反射的方式创建

​																																					 -------> BPP代理的方式

---> applyMergedBeanDefinitionPostProcessors   注册生命周期接口（对于@PostConstruct和@PreDestroy和@Resource和@Autowired和@Value等相关注解的解析操作）



----->populateBean    填充属性

















































































































