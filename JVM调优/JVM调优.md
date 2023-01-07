2022.05.08

# JVM调优

**Java跨平台的语言**

![image-20220305173456848](image\image-20220305173456848.png)

**JVM跨语言的平台**

![image-20220305171246287](image\image-20220305171246287.png)

**任何语言只要能编译成class文件就能在JVM中运行**

![image-20220305171321728](image\image-20220305171321728.png) 



**JVM其实是一个规范**

**是虚拟出来的一台计算机**

- 字节码指令集
- 内存管理：栈、堆、方法区等

![image-20220305172710812](image\image-20220305172710812.png)

## JDK JRE JVM

![image-20220305173208183](image\image-20220305173208183.png)

- JVM相当于是一个执行的平台
- JRE运行时环境，包含核心类库，比如：Object、String......
- JDK Java开发工具库



## Class文件格式



### Class File Format

使用BinEd插件来打开16进制的.class文件

![image-20220508131503301](image\image-20220508131503301.png) ![image-20220508132152648](image\image-20220508132152648.png) 

![image-20220508132130797](image\image-20220508132130797.png) 



### Class文件解读

![image-20220508154552436](image\image-20220508154552436.png) 



#### **magic**

#### **minor version**

#### **major version**

#### **constant_pool_count 常量池数量**

#### **constant_pool长度为constant_pool_count -1 的一张表   从1开始的**

#### **access_flags 修饰符**



![image-20220508141803985](image\image-20220508141803985.png) 

 

1. access_flags	![image-20220508134353939](image\image-20220508134353939.png) 
2. ![image-20220508153638227](image\image-20220508153638227.png) 



#### **this_class 当前class文件指向常量池里面的内容**

![image-20220508134637011](image\image-20220508134637011.png) 

详细内容

![image-20220508134703212](image\image-20220508134703212.png) 



![image-20220508135014885](image\image-20220508135014885.png) 

上图**访问标识**为什么是0X0021

![image-20220508135146414](image\image-20220508135146414.png)

![image-20220508135542445](image\image-20220508135542445.png) 



#### constant_pool

##### constant_method_info



方法引用的信息

![image-20220508144255256](image\image-20220508144255256.png) 



![image-20220508145110994](image\image-20220508145110994.png) 

![image-20220508151044968](image\image-20220508151044968.png) 

![image-20220508140958774](image\image-20220508140958774.png) 

![image-20220508150327097](image\image-20220508150327097.png)



此处有个笔误，但是不影响下面的分析

![image-20220508143808876](image\image-20220508143808876.png) 

![image-20220508143654131](image\image-20220508143654131.png) 





![image-20220508143731319](image\image-20220508143731319.png) 



##### 方法具体的实现

![image-20220508154405283](image\image-20220508154405283.png) 

![image-20220508154353801](image\image-20220508154353801.png) 



### method

****

![image-20220508155536394](image\image-20220508155536394.png) 

![image-20220508155550498](image\image-20220508155550498.png) 



**method中最终的是Code   执行的是哪条指令**

![image-20220508155625843](image\image-20220508155625843.png) 



2a  aload_0 (把this压栈)   b7  invokespecial(在下图中 invokespecial 调用了 位置为0001常量池的第一项[java.lang.object] 调用他的构造方法)

![image-20220508160418006](image\image-20220508160418006.png) 

![image-20220508155734274](image\image-20220508155734274.png) 

![image-20220508223252420](image\image-20220508223252420.png) 

![image-20220508155704896](image\image-20220508155704896.png) 

b1就是return

![image-20220508155910915](image\image-20220508155910915.png) 



![image-20220508160015970](image\image-20220508160015970.png) 



**在任何情况下，一个类要是没有写任何东西，编译器会自动给我们加一个无参构造方法，构造子类之前必须先构造父类，默认会调用父类的无参构造方法**



首先会调用父类的构造方法，然后把自己成员变量进行初始化

![image-20220508163340089](image\image-20220508163340089.png) 

**magic -> minor version -> major version -> constant_pool_count -> constant_pool->access flags -> this_class->super class->interface count -> interfaces -> fields_count -> fields -> methods_count -> methods_info ->attribute_count -> attribute(*Code)**



## 类加载器 Class Loading 、Linking 、Initializing

**一个class文件是怎么从硬盘里到内存里头，并且开始准备执行的。**



### **类加载器**

类加载过程

​	1.loading 把一个class文件load到内存中去

​	2.linking

​		1.verification		校验（符不符合class文件的标准）

​		2.Preparation*	把class文件的静态变量赋默认值

​		3.Resolution		class文件常量池里面用到的符号引用，要转换成直接的内存地址

​	3.Initializing	静态变量的初始值

![image-20220508165210955](image\image-20220508165210955.png) 



**JVM所有的class都是通过类加载器来加载到内存的**

![image-20220508165740146](image\image-20220508165740146.png)

**上图并不是一个继承关系**



getClassLoader如果是null的话，那么这个类使用的类加载器就是最顶层的类加载器  Bootstrap  他是由C++实现的。

![image-20220508171040509](image\image-20220508171040509.png) 



**加载器的加载器不是他的父类，这个不能混淆。**



### Class Loading

**把一个class文件load到内存**

**JVM所有的class都是被类加载器加载到内存中的**



**一个class文件被load内存中的时候其实都创建了两块内容**

第一块：把二进制的东西扔到了内存中

第二块：生成了class类的一个对象 ，这个class类对象指向了第一块的内容(metaspace=====JVM元空间 查资料了解)

通过class对象帮我们解析好调用的方法，然后去二进制文件中调用



MethodArea 方法区：1.8之前交PermG，1.8之后交MetaSpace



class类对象在MetaSpace中



通过class类对象来帮我们解析，他会去访问对应的二进制字节码，翻译成相应的指令去执行（反射也是一样）



#### 双亲委派*

**一个class需要被load到内存时候的执行过程，例如A.class，如果有自定义的CustomClassLoader就去自定义的CustomClassLoader去找，他内部维护了一个缓存，调用loadClass()方法看看有没有将A.class加载进去，如果加载进去了就返回，如果没有他并不是二话不说直接加载，而是去他的父加载器往上找，委托AppClassLoader，如果AppClassLoader的缓存中没有这个类，则继续委托ExtensionClassLoader找，如果ExtensionClassLoader还是没有则委托Bootstrap，因为Bootstrap是最顶级了，所以相当于这几个类加载器缓存中都没有A.class这个类。那么Bootstrap就往回再委托，Extension->AppClassLoader->CustomerClassLoader  当CustomerClassLoader真正的把A.class加载进去的时候才算成功，否则抛异常 ClassNotFoundException。**

![image-20220508172830318](image\image-20220508172830318.png) 

**缓存可以认为是他自己内部维护的一个容器**



##### 为什么要双亲委派？

主要：是为了安全问题，如果你自己定义的内存都可以直接load到内存的话，我们就可以自己定义一个类库，将原生的类库全部覆盖掉，当用户使用这个类库的时候，就可以在里面搞小动作了。

次要：上层加载过了，后面就不用再加载了，资源浪费可以减少

- 父加载器
  - **父加载器不是“类加载器的加载器”也不是“类加载器的父类加载器”**
- 双亲委派是一个孩子向父亲方向，然后父亲再向孩子方向的双亲委派过程
- 思考：为什么要搞双亲委派
  - java.lang.String类有自定义类加载器加载行不行？ 安全

#### 类加载器范围

**Launcher**

![image-20220508211317280](image\image-20220508211317280.png) 



### ClassLoader源码解析

![image-20220508212025148](image\image-20220508212025148.png)

![image-20220508212957716](image\image-20220508212957716.png) 

![image-20220508212910105](image\image-20220508212910105.png) 



![image-20220508213215976](image\image-20220508213215976.png) 

![image-20220508213353202](image\image-20220508213353202.png) 

![image-20220508213456657](image\image-20220508213456657.png) 



### 自定义类加载器

```java
/**
 * @author LYX
 * @description
 * @date 2022/5/8 21:35
 */
public class T002_CustomClassLoader extends ClassLoader {

    public static void main(String[] args) {
     
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("com.msb.jvm.bytecode.T01_bytecode01".replaceAll(".", "/").concat(".class"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            int b = 0;

            while ((b = fis.read()) != 0) {
                bao.write(b);
            }

            byte[] bytes = bao.toByteArray();
            bao.close();
            fis.close();
            //把文件流转换成class对象
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }
}
```

![image-20220508214430838](image\image-20220508214430838.png) 



### 加密

为了防止自己的class类文件被反编译，可以将他的二进制字节流加密



### 编译器![image-20220508220230562](image\image-20220508220230562.png) 



**混合使用解释器和热点代码编译**

为什么不直接编译成代码呢？

因为Java的解释器在解释一些简单的代码方面已经不输于编译器了，如果很多class文件都直接编译的话，那么启动过程会非常非常的漫长。



#### 总结：

##### LazyLoading 五种情况

- new get static put static invoke static 指令，访问final变量除外
- java.lang.reflect对类进行反射调用
- 初始化子类的时候，父类首先初始化
- 虚拟机启动时，被执行的主类必须初始化
- 动态语言支持java.lang.invoke.MethodHandler解析的结果为REF_getstatic REF_putstatic REF_invokestatic的方法句柄时，该类必须初始化



**ClassLoader的源码**

1. findIncache->parent.loadClass->findClass()



**自定义类加载器**

1. extend ClassLoader
2. overwrite findClass() -> defineClass(byte[] -> Class clazz)
3. 加密



**混合执行，编译执行、解释执行**

1. 检测热点代码:   -XX:CompileThreshold=10000



### parent是如何指定的？

任何一个ClassLoader都有一个parent成员变量，变量中并没有指定这个parent是谁，而是通过方法指定的



如何指定parent

![image-20220509133555344](image\image-20220509133555344.png) 

![image-20220509133859861](image\image-20220509133859861.png)  



  **默认的ClassLoader指定**

![image-20220509134109254](image\image-20220509134109254.png) 

![image-20220509134140900](image\image-20220509134140900.png) 



**默认的就是AppClassloader** 

![image-20220509134336387](image\image-20220509134336387.png) 



**如何打破双亲委派机制？**

重写loadClass方法

什么时候要打破？

1. JDK1.2之前自定义ClassLoader都必须重写loadClass() (不管)
2. **ThreadContextClassLoader可以实现基础类调用实现类代码，通过thread.setContextClassLoader指定**
3. **热启动热部署   Tomcat里面每一个webApplication都有一个自己的ClassLoader**
   
   1. **osgi tomcat 都有自己的模块指定ClassLoader(可以加载同一类库的不同版本)**
   
   

**双亲委派不管是load多少次，他都会去已经加载过的这个类里面去找，如果找着了他就不会再重新加载**

![image-20220509135525886](image\image-20220509135525886.png) 



 **只是重写findClass是打破不了双亲委派的，必须要重写loadClass**

**如果只是在同一个ClassLoader里面去load他们是相等的，所以要重新new一个CLassLoader加载的两个对象才是不同的。**

 

**打破双亲委派机制是不是就不安全了呢？**

有些时候是为了需求不得不打破 比如jdbc 和tomcat都是为了各自的需要而不得不打破双亲委派，对安全不会造成问题。在我看来，jdk内部用双亲委派，是为了jdk本身代码的安全，比如不让你自己搞一个String来玩，当然你想搞还是可以搞的，只是完全没有意义



### Linking 



#### verification(校验)

**检验文件是否符合JVM规定**



#### preparation

**将class文件的静态变量赋默认值**



#### resolution

**这个class文件里面的常量池里用到的那些符号引用，要给他转换成直接的内存地址**



**将类、方法、属性等符号引用解析为直接引用**

**常量池中的各种符号引用解析为指针、偏移量等内存地址的直接引用**



### Initializing

**静态变量赋值为初始值，调用静态代码块**

**调用类初始化代码，给静态变量赋初始值**



![image-20220509212122740](image\image-20220509212122740.png) 

```sql
--执行过程
--调用T.count的时候，ClassLoader会把T.class load到内存中(默认的AppClassLoader)，load完之后进行校验preparation，把T里面的静态成员变量赋默认值，这时候t为null  count=0,所以这个时候没有调用构造方法,count不++，再进行resolution解析，再进行Initializing初始化，给count 和 t赋初始值，这时候count=2  t=new T(), new T() 调用了构造方法，count ++ 因此count = 3
```





![image-20220509212202429](image\image-20220509212202429.png) 



像private int m = 8;这个成员变量是需要对象new出来的时候才能调用的，而对象new出来就分为两步，第一步是给对象申请内存的过程，申请完内存之后他里面的成员变量会先赋默认值，所以这时候m=0。申请完内存赋完默认值之后，下一步就是调用构造方法，调构造方法才会给他赋初始值，这个时候m=8。



**先赋默认值，再赋初始值**



小结：

class  load:默认值->初始值

new Object:申请内存->默认值->初始值

**创建一个对象先去申请内存空间地址，申请完后赋默认值之后再赋初始值。**





## 单例模式 双重检查

![image-20220509214859108](image\image-20220509214859108.png) 



## 硬件层数据一致性

![image-20220509215622846](image\image-20220509215622846.png)

 ![image-20220509220037954](image\image-20220509220037954.png) 



**如何解决硬件层数据一致性问题？**

1、总线锁，第一个cpu去访问这个数据的时候，他就持有总线的这把锁，别的cpu就不能访问这个值了(老CPU的做法)

缺点：效率低，因为第一个持有总线锁的时候，其他的cpu只能等着他释放

2、Intel 使用的是MESI协议实现的 

![image-20220509220352514](image\image-20220509220352514.png) 



Modified：**被更改过；**

Exclusive：**如果这个内容为我独享**

Shared：**这个内容我读的时候别人也在读**

Invalid：**在我读的时候被别的CPU改过了**

**通过MESI协议让各个CPU之间的缓存保持一致性**，并没有完全解决锁总线问题

**现在CPU的数据一致性实现是通过   缓存锁(MESI等等其他缓存一致性协议)+总线锁**



## 缓存行 伪共享

读取缓存以cache line为基本单位，目前多数的实现64个字节。

**位于同一缓存行的两个不同数据，被两个不同的CPU锁定，产生互相影响伪共享问题。**

![image-20220510090844061](image\image-20220510090844061.png) 

伪共享问题程序：JUC/c_028_FalseSharing



**缓存行对齐**

**没有使用缓存行对齐的代码**

```java
public class T01_CacheLinePadding {

    private static class T{
        public volatile long x = 0L;

    }

    public static T[] arr = new T[2];
    //位于同一个缓存行
    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    //不断的变换对其T中的x值 这种写法是位于同一个缓存行的写法
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[0].x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[1].x = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_10000L);
    }
}

result: 结果在30左右
已连接到目标 VM, 地址: ''127.0.0.1:52539'，传输: '套接字''
25
与目标 VM 断开连接, 地址为: ''127.0.0.1:52539'，传输: '套接字''

进程已结束，退出代码为 0
```



**使用缓存行对齐**

```java
public class T02_CacheLinePadding {

    private static class Padding {
        //缓存行对齐填充  虽然占用了很多内存，但是运行速度上是加快的
        public volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    private static class T extends Padding {
        public volatile long x = 0L;

    }

    public static T02_CacheLinePadding.T[] arr = new T02_CacheLinePadding.T[2];

    //位于同一个缓存行
    static {
        arr[0] = new T02_CacheLinePadding.T();
        arr[1] = new T02_CacheLinePadding.T();
    }

    //不断的变换对戏T中的x值 这种写法是位于同一个缓存行的写法
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[0].x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[1].x = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_10000L);

    }
}

result:结果在10 左右
8
与目标 VM 断开连接, 地址为: ''127.0.0.1:52588'，传输: '套接字''
进程已结束，退出代码为 0
```

![image-20220510123005072](image\image-20220510123005072.png) 

**使用缓存行的对齐能够提高效率**



## 乱序问题

 ![image-20220510131241141](image\image-20220510131241141.png)



**cpu为了提高效率，会在一条指令执行过程中 (比如说去内存读数据，内存的速度比cpu慢最少100倍)，去同时执行另一条指令，前提是两条指令没有依赖关系，打乱执行顺序。**



WCBuffer  write combining:合并写操作

![image-20220510131603793](image\image-20220510131603793.png) 

JUC/029_WriteCombing

一个位置一个字节

由于合并写的位置只有4个位置，当我们往里更新的时候，比如只更新3个，那么他只要写满了，就只写扔到缓存中了，但是如果是很多的话，那么就会每写满一次扔一次。



```java
package com.msb.jvm.c_029_WriteCombining;

public final class WriteCombining {

    private static final int ITERATIONS = Integer.MAX_VALUE;
    private static final int ITEMS = 1 << 24;
    private static final int MASK = ITEMS - 1;

    private static final byte[] arrayA = new byte[ITEMS];
    private static final byte[] arrayB = new byte[ITEMS];
    private static final byte[] arrayC = new byte[ITEMS];
    private static final byte[] arrayD = new byte[ITEMS];
    private static final byte[] arrayE = new byte[ITEMS];
    private static final byte[] arrayF = new byte[ITEMS];

    public static void main(final String[] args) {

        for (int i = 1; i <= 3; i++) {
            System.out.println(i + " SingleLoop duration (ns) = " + runCaseOne());
            System.out.println(i + " SplitLoop  duration (ns) = " + runCaseTwo());
        }
    }

    public static long runCaseOne() {
        long start = System.nanoTime();
        int i = ITERATIONS;

        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayA[slot] = b;
            arrayB[slot] = b;
            arrayC[slot] = b;
            arrayD[slot] = b;
            arrayE[slot] = b;
            arrayF[slot] = b;
        }
        return System.nanoTime() - start;
    }
	//充分利用了合并写技术
    public static long runCaseTwo() {
        long start = System.nanoTime();
        int i = ITERATIONS;
        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayA[slot] = b;
            arrayB[slot] = b;
            arrayC[slot] = b;
        }
        i = ITERATIONS;
        while (--i != 0) {
            int slot = i & MASK;
            byte b = (byte) i;
            arrayD[slot] = b;
            arrayE[slot] = b;
            arrayF[slot] = b;
        }
        return System.nanoTime() - start;
    }
}

```

结果：

![image-20220510132246309](image\image-20220510132246309.png) 



## 乱序证明

```java
public class Test01_Disorder {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) {

        for (long i = 0; i < Long.MAX_VALUE; i++) {
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            CountDownLatch latch = new CountDownLatch(2);

            Thread one = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = 1;
                    x = b;

                    latch.countDown();
                }
            });
            Thread two = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    y = a;

                    latch.countDown();
                }
            });

        }
    }
}
```



## 硬件级别保证有序

**CPU内存屏障** =========不太懂

X86

sfence   save fence  前面写操作必须在后面写操作之前完成

lfence  load fence

mfence  mix fence

![image-20220510133446339](image\image-20220510133446339.png) 

![image-20220510133806011](image\image-20220510133806011.png) 



**如果有缓存锁的情况下还需要总线锁吗？**

需要，因为有的数据一个缓存装不下，这个时候就需要锁总线

 

![image-20220510214008787](image\image-20220510214008787.png) 



## Volatile实现细节

![image-20220510214654452](image\image-20220510214654452.png) 

1、字节码层面

其实就只是加了一个这个标记ACC_VOLATILE

2、**JVM层面**

在volatile前面和后面加了屏障

![image-20220510214839908](image\image-20220510214839908.png) 

volatile的读写操作都加上屏障0 

3、OS和硬件层面

![image-20220510215208641](image\image-20220510215208641.png) 

![image-20220510215111278](image\image-20220510215111278.png) 

Windows lock指令实现

**volatile在字节码层面编译完的时候只是在class文件上加上了ACC_VOLATILE这么一个flag，当虚拟机读到这个flag的时候，他就会在这个volatile内存区读写之前都加屏障。加完屏障之后的硬件实现，在Windows是用lock指令实现的，而在Linux上需要查资料去看。**



## synchronize实现细节

1、**字节码层面**

ACC_SYNCHRONIZED 修饰符



synchronize生成之后的字节码：**monitorenter（监视器的进入）、monitorexit（监视器的退出）**

![image-20220511091643949](image\image-20220511091643949.png) 



**synchronize(this){}块完了之后有一个exit，但是一旦catch到异常的时候，运行时异常我们不需要明确的写出来，这种异常的时候就会在exit**

![image-20220511092007642](image\image-20220511092007642.png) 

2、JVM层面

​	**C、C++调用了操作系统提供的同步机制**

3、OS和硬件层面

​	**x86  lock comxch指令来实现的**

![image-20220511092642198](image\image-20220511092642198.png) 





## 排序规范   =====有时间可以去了解

 ![image-20220511093132906](image\image-20220511093132906.png) 

**as if serial：不管如何重排序，单线程执行的结果都不会改变**



## 对象的创建过程

![image-20220511093821654](image\image-20220511093821654.png) 

1、具体参考  标题Class Loading Linking Initializing

大概：首先先把这个class load到这个内存，T t的过程，后面就是new T()的过程	 

2、通过命令去观察虚拟机的配置

![image-20220511131046487](image\image-20220511131046487.png) 



### 普通对象

1、对象头 markword 8个字节

2、ClassPoint指针 

![image-20220511131410433](image\image-20220511131410433.png) 

3、实例数据

​	引用类型：

![image-20220511131528306](image\image-20220511131528306.png) 

4、Padding对齐，8的倍数

### 数组对象

1、对象头 markword 8个字节

2、ClassPoint 指针

3、数组长度：4个字节

4、数组数据

5、对齐 8的倍数



### 对象的大小 

《对象的大小》这节课的开头有讲==========

T.class文件要load到内存的时候是有一个agent机制在里面的。这个代理想做啥做啥





## 对象头具体包括什么

markword 8个字节 64位  每位代表的状态

![image-20220511133737365](image\image-20220511133737365.png)



在无锁的情况下，看锁的标志位是否是偏向的，这个对象的hashCode分为两种情况，

第一种是重写过：重写过的hashCode就是你重写过的值

第二种是没有重写过：就是默认的根据布局来计算出来的的值，这种的hashCode我们称之为IdentityHashCode



![image-20220511134427580](image\image-20220511134427580.png)

因为分代年龄是4位，他最大的数是15



**问题：**

**当Java处于偏向锁、重量级锁状态的时候，hashCode值存储在哪**

当一个对象计算过identity hash code，他就无法进入偏向锁状态，需要注意下，当调用锁对象的Object#hash 或System.identityHashCode() 方法会导致该对象的偏向锁或轻量级锁升级。这是因为在Java中一个对象的hashcode是在调用这两个方法才生产的，如果是无锁状态则存放在mark word中，如果是重量级锁则存放在对应的monitor中，而偏向锁是没有地方能存放该信息的，所以必须升级。



#### Identity Hash code的问题

当一个对象计算过IdentityhashCode之后，不能进入偏向锁状态的解释

![image-20220511135420584](image\image-20220511135420584.png) 



## 对象怎么定位

![image-20220511163929545](image\image-20220511163929545.png) 

T t = new T()

1. **句柄池 ,  这个t指向了两个指针，这两个指针的其中一个指向了真正的对象，另外一个呢指向了T.class**
2. **直接指针 ,直接指向了真正的对象，真正的对象又有一个指针指向了T.class**

没有优劣之分，看虚拟机实现，Hotspot使用的就是直接指针

第二种查找的时候效率相对高，第一种在GC垃圾回收的时候效率比较高(GC有牵扯算法)。



### 对象怎么分配



GC的时候再开始讲![image-20220511164701829](image\image-20220511164701829.png)



## JVM Runtime Data Area And JVM Instructions



### JVM Runtime Data Area

PC程序计数器

> 存放指令位置
>
> 虚拟机的运行，类似于这样的循环
>
> while(not end) {
>
> ​	取PC中的位置，找到对应位置的指令
>
> ​	执行该指令；
>
> ​	PC++;
>
> }



JVM stacks JVM虚拟机管理的栈 每个线程对应一个栈，每一个方法对应一个栈帧



native method stacks  不管他



Direct Memory 直接内存，从JVM虚拟机内部可以直接访问操作系统的内存

> JVM可以直接访问的内核空间的内存(OS 管理的内存)
>
> NIO 提高效率，实现0拷贝 zero copy



Method Area

1、Perm Space 永久区(<1.8之前Method Area指的就是Perm Space)

2、Meta Space 元数据区 (>=1.8 之后Method Area 指的是Meta Space)

上面两个都是不同的Method Area的实现。



 每一个线程都有自己的Program Counter 都有自己的 Virtual Machine Stack 和 Netty Method Stack ，但是他们共享的都是堆，已经Method Area![image-20220511213304685](image\image-20220511213304685.png) 

 

### *栈帧

栈帧  Frame

一个线程栈里面装的是一个一个的栈帧，那么一个栈帧里面装的东西如下图

![image-20220511213729161](image\image-20220511213729161.png)

1、Local Variables  局部变量

2、Operand Stacks 操作数栈 每一个栈帧都有一个自己的Operand Stacks 

3、Dynamic Linking  指到我们运行时常量池的符号链接（方法叫什么名，类型是啥） 看有没有解析，解析了就直接用，没有解析就动态解析 。例如：A方法里面调用了B方法，这个B方法去常量池找的链接就是Dynamic Linking

4、return address  返回值的地址



**a()->b()，方法a调用方法b，b方法的返回值应该放在什么地方**

一个线程有自己的线程栈，每个线程栈装了一个一个的栈帧，每个栈帧里面有自己的操作数栈和Dynamic Linking



**每一个方法对应一个栈帧**

![image-20220511214408391](image\image-20220511214408391.png) 

![image-20220511215353842](image\image-20220511215353842.png) 

![image-20220511220328734](image\image-20220511220328734.png) 



1、bipush  8 压栈

2、istore_1  把栈顶的值出栈，出栈之后给他放到下标值Nr.   为1的局部变量表中，就是上图的 i值(红框)

int i = 8;  的完成    1、2

3、iload_1   从局部变量表往外拿值，把局部变量表下标 为1 的数拿出来压栈。就是把i=8的值再拿出来，压到栈中

4、iinc 1 by 1 把局部变量表下标为1 中的数拿出来 + 1，这时候局部变量表中i 的数值为9，但是栈里面的值还是8

5、istore_1  这时候又把栈中的8 拿回来赋值 给局部变量表中的i ， 局部变量表中的i从9赋值成了8，因为最后得到的结果就是8



因为i++是在局部变量表中完成的，而那个时候8被压到栈中了，导致局部变量表中的值为9，而栈中的值为8，当8出栈的时候8把9覆盖了。



![image-20220511225315546](image\image-20220511225315546.png) 

i++和++i唯一的区别就是把iinc 1 by 1放到了iload_1的前面去 



#### 补充

基于栈的指令集

基于寄存器的指令集



### 栈的执行流程

**一个方法对应一个栈帧，每个栈帧里面都有自己的 局部变量表、操作数栈、动态连接、返回地址**



![image-20220512092011848](image\image-20220512092011848.png) 



![image-20220512092153887](image\image-20220512092153887.png) 

为什么是istore_2？

因为this在我们的局部变量表中是已经存在了的，只要这个方法不是static方法这个this就存在 

**验证：**

![image-20220512092605982](image\image-20220512092605982.png) 



![image-20220512123003152](image\image-20220512123003152.png)



![image-20220512123557067](image\image-20220512123557067.png) 

new完了一个对象的地址，这个地址会压栈

dup(duplication)的意思是这个对象地址在栈中再复制一个，意思是这两个地址都指向了一个对象。

invokespecial<init> 执行构造方法会把复制出来的上面那个对象地址弹出去，因为invokespecial会用掉其中一个对象地址



前四条是Hello_02 h = new Hello_02() 完成创建对象的操作

![image-20220512131658043](image\image-20220512131658043.png) 

pop：(第二个为例)是因为在调用m1方法返回的时候，会往main方法的**栈顶**上放了一个100，在main方法调用完成之后，由于栈顶上有一个返回值，不管有没有用都直接往外弹，后再return

为什么是istore_2呢 是因为main的栈帧中 第0个位置是 args  第1个是 h  第2个才是 i  



#### 递归

![image-20220512132922669](image\image-20220512132922669.png)

iconst_1 是把 1 这个值压栈

if_icmpne(i compare not equels) 把栈里面的两个int值 弹出来比较(这时候栈中已经没有值了) 如果不等于1就，就跳到 第七条指令中,iload_1又把这个值进行压栈，iload_1再把3压进去，aload_0把this也压栈，iload_1在把3 压栈，iconst_1把1压栈， isub就是把3 和 1 弹出去执行相减，相减的值为2  扔到栈中，然后再invokevirtual 执行m() 方法，这个时候就要把2  和this 弹出去调用m(2) 方法

重复上述步骤后执行到了m(1)，if_icmpne发现值相同，把两个值弹出，走第5步，把iconst_1压栈，ireturn 把这个值返回给m(2)的栈帧，m(2)会继续往下执行。

imul 值相乘



<clinit>指的是静态语句块

<init> 指的是构造方法

![image-20220512134637517](image\image-20220512134637517.png) 





### invoke指令 ===========

1、InvokeStatic

2、InvokeVirtual  自带多态

3、InvokeInterface

4、InvokeSpecial

代表的可以直接定位，不需要多态的方法

private 方法，  构造方法

5、InvokeDynamic(JVM最难的指令)

lambda表达式或者反射或者其他动态语言 Scala Kotlin，或者CGLib ASM，动态产生的class，会用到的指令

![image-20220512214336733](image\image-20220512214336733.png) 



> 如何证明1.7字符串常量位于Perm，而1.8位于Heap？
>
> 结合GC，一直创建字符串常量，观察堆和Meta Space的情况



## *垃圾

**熟悉GC的常用算法，熟悉常见垃圾收集器，具有实际JVM调优实战经验**



没有任何引用指向的对象都叫做垃圾

![image-20220512220023329](image\image-20220512220023329.png) 



**reference count 引用数量为0的时候 这个对象就会被当成垃圾**



**Root Searching 根可达算法**

从根对象开始搜

![image-20220512220219924](image\image-20220512220219924.png) 

到底哪些是根对象？

**JVM Stack JVM栈，**

**native method stack 本地方法栈，** 

**run-time constant pool 常量池，**

**static reference in method area 在方法区内部使用的静态引用**

Clazz



总的来说就是：

**程序启动的时候马上就需要的对象就叫做根对象**。

![image-20220512220956610](image\image-20220512220956610.png) 



### *GC常用垃圾算法  (背过)

**Mark-sweep（标记清除）**

**Copying（拷贝算法）**

**Mark-Compact（标记压缩）**



#### **标记清除**

![image-20220512221828780](image\image-20220512221828780.png) 



![image-20220512222023506](image\image-20220512222023506.png) 

**算法相对简单，存活对象比较多的情况下效率高**

**两遍扫描：**

**第一次进行标记，第二次进行清除**



**第一遍找出那些有用的，第二遍是把那些没用的找出来然后再清理**

**效率偏低**

**容易产生碎片**



#### Copying

![image-20220512222336035](image\image-20220512222336035.png) 

**内存一分为二，把有用的拷贝到一份，然后另一份全部清除掉**



![image-20220512222435504](image\image-20220512222435504.png) 

优点：

适用于存活对象较少的情况，只扫描一次，效率提高，没有碎片

缺点：

空间浪费，移动复制对象，需要调整对象引用



#### 标记压缩



把所有有用没用的都压缩到一边

![image-20220512222704256](image\image-20220512222704256.png) 

![image-20220512222952957](image\image-20220512222952957.png) 

优点：

**不会产生碎片，方便对象分配，不会产生内存减半**

缺点：

**扫描两次，还需要移动对象，效率偏低**





## 堆内存逻辑分区

JVM Hotspot 用的是分带算法（分年轻代和老年代）

![image-20220513132235640](image\image-20220513132235640.png) 

把内存分为新生代和老年代

eden区和surivivor两个区  比例为8：1：1



![image-20220513132159719](image\image-20220513132159719.png)



**JVM内存分代模型(用于分代垃圾回收算法)**

部分垃圾回收器使用的模型

>除Epsilon ZGC Shenandoah之外的GC都是使用逻辑分代模型
>
>G1是逻辑分代，物理不分代
>
>除此之外 不仅逻辑分代 而且物理分代
>
>Hotspot在逻辑上是使用分代算法



**eden 伊甸区是我们真正new出来对象往里头扔的区域**

**survivor区 回收一次之后 放到survivor**



### 一个对象从出生到消亡

![image-20220513092632676](image\image-20220513092632676.png) 

**一个对象产生之后是尝试在栈上分配的，栈上分配如果分配不下的话会进入到伊甸区，伊甸区经过一次垃圾回收之后，进入survivor1区，survivor1区再经过一次垃圾回收之后就进入到survivor2区，与此同时伊甸区的某些对象也跟着进入到了survivor2区，然后就是两个survivor区的互相进入，等什么时候年龄够了之后，就会进入到old区。**



**问题：**对象随着年龄为什么会在survivor区移动？

对象的年龄如果不随着GC次数变化的话，没办法筛选那些年龄太大的(很多次的GC都干不掉他就说明他正常情况下很难被干掉，所以只能先把他放到一个更大的区域不去理他)

至于为什么Eden区里面要用两个S区，关键在于，如果只有一个区，他把能干掉的都干掉之后会有空间碎片，要整理的话效率不好。就干脆弄两个S区，等某个S去的清理掉一部分之后，把活着的挨着排复制到另一个S区中

![image-20220513094146250](image\image-20220513094146250.png) 

对的

### GC的概念

![image-20220513093147124](image\image-20220513093147124.png)

凡是在年轻代进行回收的，就叫做Minor GC/YGC，凡是在老年代或者是整个区域进行回收的，就叫做Major GC，FullGC



## 栈上分配

为了对表C所以设计了一个栈上分配的标准



**栈上分配**

- 线程私有小对象
- 无逃逸   就在某一段代码里面使用，出了这段代码就没有对象认识他了
- 支持标量替换   (使用普通的类型来代替整个对象)
  - 例如一个对象T里面 有int m,int n 就可以完全使用这两个类型来替代这个对象了
- 无需调整

**线程本地分配TLAB(Thread Local Allocation Buffer)** 

- 占用Eden 默认1%
- 多线程的时候不用竞争eden就可以申请空间，提高效率
- 小对象
- **无需调整**

**老年代**

- 大对象



**代码测试**

```java
public class Test01_TLAB {

    User u;

    class User {
        int id;
        String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    void  alloc (int i) {
        //这对象只能在alloc这个方法中使用 没有逃逸
        System.out.println(new User(i, "name" + i));
        //逃逸了
        System.out.println(u = new User(i, "name" + i));
    }

    public static void main(String[] args) {
        Test01_TLAB t = new Test01_TLAB();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_10000; i++) {
            t.alloc(i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
```

![image-20220513131054100](image\image-20220513131054100.png) 

栈上分配肯定会比堆上分配快很多，栈一弹出就没了，和垃圾回收没有任何的关系



## 对象如何进入老年代 ==========

![image-20220513131406355](image\image-20220513131406355.png) 



S1+伊甸区 经过一次YGC拷贝到S2的时候如果超过了S2的50%了，那么这里面年龄最大的对象就会直接进入old区。

![image-20220513131747789](image\image-20220513131747789.png) 



![image-20220513132036784](image\image-20220513132036784.png) 

![image-20220513132756457](image\image-20220513132756457.png) 

**分配担保**

YGC期间 survivor区空间不够了，通过空间担保直接进入老年代

### 关于年轻代和老年代的问题  =====



## 常见的垃圾回收器

![image-20220513132949412](image\image-20220513132949412.png) 

**常见垃圾回收器的组合**

![image-20220513133226966](image\image-20220513133226966.png) 

Serial  是JDK诞生的时候就有的，指的是单线程的。

Parallel 指的是多线程

1. JDK诞生 Serial追随 提高效率，诞生了PS，为了配合CMS，诞生了PN，CMS是1.4版本后期引入，CMS是里程碑式的GC，它开启了并发回收的过程，但是CMS毛病较多，因此目前没有任何一个JDK版本默认是CMS
   并发垃圾回收是因为无法忍受STW
2. Serial 年轻代 串行回收
3. PS 年轻代 并行回收
4. ParNew 年轻代 配合CMS的并行回收
5. SerialOld 
6. ParallelOld
7. ConcurrentMarkSweep 老年代 并发的， 垃圾回收和应用程序同时运行，降低STW的时间(200ms)
   CMS问题比较多，所以现在没有一个版本默认是CMS，只能手工指定
   CMS既然是MarkSweep，就一定会有碎片化的问题，碎片到达一定程度，CMS的老年代分配对象分配不下的时候，使用SerialOld 进行老年代回收
   想象一下：
   PS + PO -> 加内存 换垃圾回收器 -> PN + CMS + SerialOld（几个小时 - 几天的STW）
   几十个G的内存，单线程回收 -> G1 + FGC 几十个G -> 上T内存的服务器 ZGC
   算法：三色标记 + Incremental Update
8. G1(10ms)
   算法：三色标记 + SATB
9. ZGC (1ms) PK C++
   算法：ColoredPointers + LoadBarrier
10. Shenandoah
    算法：ColoredPointers + WriteBarrier
11. Eplison
12. PS 和 PN区别的延伸阅读：
    ▪[https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html#GUID-3D0BB91E-9BFF-4EBB-B523-14493A860E73](https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html)
13. 垃圾收集器跟内存大小的关系
    1. Serial 几十兆
    2. PS 上百兆 - 几个G
    3. CMS - 20G
    4. G1 - 上百G
    5. ZGC - 4T - 16T（JDK13）

1.8默认的垃圾回收：PS + ParallelOld





### Serial (基本不用了) + Serial Old

**当serial开始干活的时候，所有的工作线程全部停止**

stop the world (STW)

safe point 安全点

![image-20220513133748015](image\image-20220513133748015.png) 

![image-20220513134001962](image\image-20220513134001962.png) 



### Parallel Scavenge +  PO (PS PO)   Parallel 并行

Java不做任何设置的话默认的就是使用这两种

![image-20220513134142515](image\image-20220513134142515.png) 

**多线程清理垃圾**

![image-20220513134213682](image\image-20220513134213682.png) 



### ParNew +CMS

Parallel New

![image-20220513134426318](image\image-20220513134426318.png)



他和Parallel Scavenge 的区别就是做了一些增强，以便和CMS配合使用(变种了一下)

![image-20220513134921330](image\image-20220513134921330.png) 



### *CMS   ====

concurrent make sweep  并发

从线程的角度

![image-20220514124909949](image\image-20220514124909949.png) 

**垃圾回收的线程和工作线程是同时进行的**

并发垃圾回收是因为无法忍受STW

现在的内存不像以前一样那么小，可以多线程执行。



**初始标记**：就是给最根的节点标记  **STW**

**并发标记：**IBM有过统计，80%的GC时间就是浪费在这里的，所以把这块最浪费时间的和我们的应用程序同时运行

**重新标记：**多数的垃圾都是在并发标记的时候标记完的，在并发标记的过程中产生的新的垃圾，在重新标记中给他标记。重新标记的过程中需要停一下，因为一直在产生新垃圾，虽然产生的不多， 不过要标记清楚的话所以需要线程停止。停止之后对新产生的垃圾进行重新标记。所以这也是一个 **STW** 的过程。不过这个过程和初始标记的过程一样，由于新产生的垃圾并不多，所以要调整的地方也不多，所以这个过程呢停止的时间也不长。

**并发清理：**并发清理的过程中也会产生新的垃圾，新产生的垃圾叫 **浮动垃圾**。浮动垃圾就只能等着CMS再次启动的时候把他给清理了。



当老年代分配不下的时候触发CMS了。

![image-20220514131124082](image\image-20220514131124082.png) 



#### CMS的问题

![image-20220514132754135](image\image-20220514132754135.png)

![image-20220514132140534](image\image-20220514132140534.png) 



CMS一旦不行了，就只能让Serial Old上场了，

并发清理的时候会产生很多的浮动垃圾，老年代满了，浮动垃圾也还没有清理完，一旦老年代产生了很多很多的碎片时，从年轻代过来的对象已经找不到空间了**PromotionFailed**，CMS就会把Serial Old请出来，所有线程都要停住，让他用一根线程在里面做标记压缩。

Concurrent Mode Failure

解决办法：

降低触发CMS的阈值

让他有足够的空间进行Promotion，让他有足够的空间产生浮动垃圾。

> --XX:CMSInitiatingOccupancyFraction 92%   可以降低这个值，让CMS保持老年代有足够的空间



FGC:**整体内存需要回收，当old区内存不够了再找别的区的地方也找不着了，就会出发FGC**

### 优化环境

1. 有一个50万PV的资料类网站（从磁盘提取文档到内存）原服务器32位，1.5G
   的堆，用户反馈网站比较缓慢，因此公司决定升级，新的服务器为64位，16G
   的堆内存，结果用户反馈卡顿十分严重，反而比以前效率更低了
   1. 为什么原网站慢?
      很多用户浏览数据，很多数据load到内存，内存不足，频繁GC，STW长，响应时间变慢
   2. 为什么会更卡顿？
      内存越大，FGC时间越长
   3. 咋办？
      PS -> PN + CMS 或者 G1
2. 系统CPU经常100%，如何调优？(面试高频)
   CPU100%那么一定有线程在占用系统资源，
   1. 找出哪个进程cpu高（top）
   2. 该进程中的哪个线程cpu高（top -Hp）
   3. 导出该线程的堆栈 (jstack)
   4. 查找哪个方法（栈帧）消耗时间 (jstack)
   5. 工作线程占比高 | 垃圾回收线程占比高
3. 系统内存飙高，如何查找问题？（面试高频）
   1. 导出堆内存 (jmap)
   2. 分析 (jhat jvisualvm mat jprofiler ... )
4. 如何监控JVM
   1. jstat jvisualvm jprofiler arthas top...



### Remark阶段的算法



CMS使用的是三色标记 + Incremental update

![image-20220514145309308](image\image-20220514145309308.png) 



### **总结**：

1. 部分垃圾回收器使用的模型

   > 除Epsilon ZGC Shenandoah之外的GC都是使用逻辑分代模型
   >
   > G1是逻辑分代，物理不分代
   >
   > 除此之外不仅逻辑分代，而且物理分代

2. 新生代 + 老年代 + 永久代（1.7）Perm Generation/ 元数据区(1.8) Metaspace

   1. 永久代 元数据 - Class
   2. 永久代必须指定大小限制 ，元数据可以设置，也可以不设置，无上限（受限于物理内存）
   3. 字符串常量 1.7 - 永久代，1.8 - 堆
   4. MethodArea逻辑概念 - 永久代、元数据

3. 新生代 = Eden + 2个suvivor区 

   1. YGC回收之后，大多数的对象会被回收，活着的进入s0
   2. 再次YGC，活着的对象eden + s0 -> s1
   3. 再次YGC，eden + s1 -> s0
   4. 年龄足够 -> 老年代 （15 CMS 6）
   5. s区装不下 -> 老年代

4. 老年代

   1. 顽固分子
   2. 老年代满了FGC Full GC

5. GC Tuning (Generation)

   1. 尽量减少FGC
   2. MinorGC = YGC
   3. MajorGC = FGC

6. 对象分配过程图
   ![](D:\TyporaNote\马士兵教育\技术\JVM调优\对象分配过程详解.png)

7. 动态年龄：（不重要）
   https://www.jianshu.com/p/989d3b06a49d

8. 分配担保：（不重要）
   YGC期间 survivor区空间不够了 空间担保直接进入老年代
   参考：https://cloud.tencent.com/developer/article/1082730





#### JVM调优第一步，了解JVM常用命令参数

1. JDK诞生 Serial追随 提高效率，诞生了PS，为了配合CMS，诞生了PN，CMS是1.4版本后期引入，CMS是里程碑式的GC，它开启了并发回收的过程，但是CMS毛病较多，因此目前任何一个JDK版本默认是CMS
   并发垃圾回收是因为无法忍受STW
2. Serial 年轻代 串行回收
3. PS 年轻代 并行回收
4. ParNew 年轻代 配合CMS的并行回收
5. SerialOld 
6. ParallelOld
7. ConcurrentMarkSweep 老年代 并发的， 垃圾回收和应用程序同时运行，降低STW的时间(200ms)
   CMS问题比较多，所以现在没有一个版本默认是CMS，只能手工指定
   CMS既然是MarkSweep，就一定会有碎片化的问题，碎片到达一定程度，CMS的老年代分配对象分配不下的时候，使用SerialOld 进行老年代回收
   想象一下：
   PS + PO -> 加内存 换垃圾回收器 -> PN + CMS + SerialOld（几个小时 - 几天的STW）
   几十个G的内存，单线程回收 -> G1 + FGC 几十个G -> 上T内存的服务器 ZGC
   算法：三色标记 + Incremental Update
8. G1(10ms)
   算法：三色标记 + SATB
9. ZGC (1ms) PK C++
   算法：ColoredPointers + LoadBarrier
10. Shenandoah
    算法：ColoredPointers + WriteBarrier
11. Eplison
12. PS 和 PN区别的延伸阅读：
    ▪[https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html#GUID-3D0BB91E-9BFF-4EBB-B523-14493A860E73](https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html)
13. 垃圾收集器跟内存大小的关系
    1. Serial 几十兆
    2. PS 上百兆 - 几个G
    3. CMS - 20G
    4. G1 - 上百G
    5. ZGC - 4T - 16T（JDK13）

1.8默认的垃圾回收：PS + ParallelOld



#### 常见的垃圾回收算法

1. 标记清除(mark sweep)：位置不连续，产生碎片，效率偏低(两边扫描)
2. 拷贝算法(copying) 没有碎片  浪费空间
3. 标记压缩(markcompact) 没有碎片，效率偏低(两遍扫描，指针需要调整)

![image-20220514202949099](image\image-20220514202949099.png) 



## JVM常用命令行参数  ============



### 常见垃圾回收器组合参数设定

* -XX:+UseSerialGC = Serial New (DefNew) + Serial Old
  * 小型程序。默认情况下不会是这种选项，HotSpot会根据计算及配置和JDK版本自动选择收集器
* -XX:+UseParNewGC = ParNew + SerialOld
  * 这个组合已经很少用（在某些版本中已经废弃）
  * https://stackoverflow.com/questions/34962257/why-remove-support-for-parnewserialold-anddefnewcms-in-the-future
* -XX:+UseConc<font color=red>(urrent)</font>MarkSweepGC = ParNew + CMS + Serial Old
* -XX:+UseParallelGC = Parallel Scavenge + Parallel Old (1.8默认) 【PS + SerialOld】
* -XX:+UseParallelOldGC = Parallel Scavenge + Parallel Old
* -XX:+UseG1GC = G1
* Linux中没找到默认GC的查看方法，而windows中会打印UseParallelGC 
  * java +XX:+PrintCommandLineFlags -version
  * 通过GC的日志来分辨

* Linux下1.8版本默认的垃圾回收器到底是什么？

  * 1.8.0_181 默认（看不出来）Copy MarkCompact
  * 1.8.0_222 默认 PS + PO



* JVM的命令行参数参考：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html

* HotSpot参数分类

  > 标准： - 开头，所有的HotSpot都支持
  >
  > 非标准：-X 开头，特定版本HotSpot支持特定命令
  >
  > 不稳定：-XX 开头，下个版本可能取消

  java -version

  java -X

  

```java
public class HelloGC01 {
    public static void main(String[] args) {
        System.out.println("Hello GC");
        List list = new LinkedList();
        for (;;) {
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}
```



* 1. 区分概念：内存泄漏memory leak，内存溢出out of memory
  2. java -XX:+PrintCommandLineFlags HelloGC
  3. java -Xmn10M -Xms40M -Xmx60M -XX:+PrintCommandLineFlags -XX:+PrintGC  HelloGC
     PrintGCDetails PrintGCTimeStamps PrintGCCauses
  4. java -XX:+UseConcMarkSweepGC -XX:+PrintCommandLineFlags HelloGC
  5. java -XX:+PrintFlagsInitial 默认参数值
  6. java -XX:+PrintFlagsFinal 最终参数值
  7. java -XX:+PrintFlagsFinal | grep xxx 找到对应的参数
  8. java -XX:+PrintFlagsFinal -version |grep GC



![image-20220514211454839](image\image-20220514211454839.png) 



### PS GC日志详解

![image-20220514221947701](image\image-20220514221947701.png) 

heap dump部分：

```java
eden space 5632K, 94% used [0x00000000ff980000,0x00000000ffeb3e28,0x00000000fff00000)
     起始地址->整体空间结束地址         后面的内存地址指的是，起始地址，使用空间结束地址，整体空间结束地址
```

![image-20220514222431303](image\image-20220514222431303.png) 

![image-20220514223314510](image\image-20220514223314510.png) 

total = eden + 1个survivor

reserved：为整个Metaspace做了一个保留

cmmitted：先 占用几块

capacity：Metaspace的总容量

used：目前使用了多少



## 调优前的基础概念

**吞吐量：用户代码时间/（用户代码执行时间 + 垃圾回收时间）**

**响应时间：STW越短，响应时间越好**

吞吐量越大，就说明gc运行的次数越少，真正有用的用户程序运行的次数更多，占比更大

而响应时间呢，就是暂停时间，每次gc，由于需要标记哪些对象是垃圾，哪些不是垃圾，总有无法避免的stw。对于gc来说，频繁的清理垃圾带来的stw的代价，总要比隔很久一次的清理带来的stw的代价要小得多（这里可以类比，是你每天收拾屋子，停下来判断哪些是垃圾的耗时比较多；还是你半年收拾一次屋子，判断垃圾的耗时比较多）因此gc越频繁，响应时间就越短

这就是两个矛盾的地方



**所谓的调优，首先要确定追求啥？吞吐量优先？还是响应时间优先？还是在满足一定的响应时间的情况下，要求达到多大的吞吐量。**



**其实大多是的系统追求的就是响应时间**



问题：

科学计算，吞吐量。数据挖掘，thrput。吞吐量优先的一般：（PS + PO）

响应时间：网站 GUI API （1.8 G1）



G1的吞吐量可能比CMS少15%左右，但是G1的响应时间肯定是比 CMS + PareNew高的 



### 什么是调优？

1. 根据需求进行JVM规划和预调优
2. 优化JVM运行环境
3. 解决JVM运行过程中出现的各种问题

调优，从业务场景开始，没有业务场景的调优都是不行的

无监控(压力测试能够看到结果)，不调优

压测



### 调优从规划开始

* 调优，从业务场景开始，没有业务场景的调优都是耍流氓

* 无监控（压力测试，能看到结果），不调优

* 步骤：
  1. 熟悉业务场景（没有最好的垃圾回收器，只有最合适的垃圾回收器）
     1. 响应时间、停顿时间 [CMS G1 ZGC] （需要给用户作响应）
     2. 吞吐量 = 用户时间 /( 用户时间 + GC时间) [PS]
  2. 选择回收器组合
  3. 计算内存需求（经验值 1.5G 16G）
  4. 选定CPU（越高越好）
  5. 设定年代大小、升级年龄
  6. 设定日志参数
     1. -Xloggc:/opt/xxx/logs/xxx-xxx-gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause
     2. 或者每天产生一个日志文件
  7. 观察日志情况



### 预规划案例  ============

![image-20220515104228836](image\image-20220515104228836.png) 

找一天的高峰期，加入5:00~7:00，再找一个小时内的高峰期，1000订单/秒



- 案例2：12306遭遇春节大规模抢票应该如何支撑？


> 12306应该是中国并发量最大的秒杀网站：
>
> 号称并发量100W最高
>
> CDN -> LVS -> NGINX -> 业务系统 -> 每台机器1W并发（单击10K问题====） 100台机器
>
> 普通电商订单 -> 下单 ->订单系统（IO）减库存 ->等待用户付款
>
> 12306的一种可能的模型： 下单 -> 减库存 和 订单(redis kafka) 同时异步进行 ->等付款
>
> 减库存最后还会把压力压到一台服务器
>
> 可以做分布式本地库存 + 单独服务器做库存均衡
>
> 所谓的大流量的处理方法：分而治之

- 怎么得到一个事务会消耗多少内存

> 1、弄台机器，看能承受多少的TPS？是不是达到目标？扩容或调优，让他达到
>
> 2、用压测来确定



### 优化环境

![image-20220515131856317](image\image-20220515131856317.png) 

1、为什么原网站会慢？

频繁GC，很多用户浏览数据，很多数据load到内存中，内存不足，频繁GC，STW响应时间变慢。

2、为什么会更卡顿？

内存越大，FGC的时间越长

3、那应该如何调优？

​	Parallel Scavenge 换成 PN + CMS 或者 G1

4、系统CPU经常100%，如何调优？(**面试高频**)

​	CPU 100%那么一定有线程在占用系统资源

​	1.找出哪个进程CPU高。 (top 命令)

​	2.该进程中的哪个线程CPU高。 (top -Hp)

​	3.如果是Java程序的话，那么找出该线程的堆栈 (jstack)

​	4.查找哪个方法(栈帧) 消耗时间 (jstack 信息)

​	5.工作线程占高|垃圾回收线程占比高

5、系统内存飙高，如何查找问题？(**面试高频**常问，如何答)

​	1.导出堆内存 (jmap)

​	2.分析(jhat  jvisualvm  mat  jprofiler......)

6、如何监控JVM

​	1.jstat、jvisualvm、jprofiler、arthas、top......





### 方法区Method Area 的实现



Method Area 方法区

​	永久代 Perm Generation

​	 元数据区 MetaSpace

#### 区别：

Perm Generation通过指令改变他的大小，但是定了之后，虚拟机一旦启动这个大小就变不了了。所以他经常会产生溢出现象

MetaSpace 受限于物理内存

字符串常量在1.8之前是放到Perm Generation里面的，1.8之后是放到堆里头的



### 课前复习



![image-20220515161231635](image\image-20220515161231635.png) 



## 风险评控



**模拟一个风控的场景**

```java
package com.msb.jvm.GC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 从数据库中读取信用数据，套用模型，并把结果进行记录和传输
 */
public class T15_FullGC_Problem01 {

    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        int age = 5;
        Date birthdate = new Date();

        public void m() {}
    }

    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) throws Exception {
        executor.setMaximumPoolSize(50);

        for (;;){
            modelFit();
            Thread.sleep(100);
        }
    }

    private static void modelFit(){
        List<CardInfo> taskList = getAllCardInfo();
        taskList.forEach(info -> {
            // do something
            executor.scheduleWithFixedDelay(() -> {
                //do sth with info
                info.m();

            }, 2, 3, TimeUnit.SECONDS);
        });
    }

    private static List<CardInfo> getAllCardInfo(){
        List<CardInfo> taskList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CardInfo ci = new CardInfo();
            taskList.add(ci);
        }

        return taskList;
    }
}

```

```java
首先编译.java文件
javac -d . T15_FullGC_Problem01.java
```

1. java -Xms200M -Xmx200M -XX:+PrintGC com.msb.jvm.GC.T15_FullGC_Problem01

2. 一般是运维团队首先受到报警信息（CPU Memory）

3. top命令观察到问题：内存不断增长 CPU占用率居高不下  

4. top -Hp 观察进程中的线程，哪个线程CPU和内存占比高

   ![image-20220515170023728](image\image-20220515170023728.png) 

    ![image-20220515170044570](image\image-20220515170044570.png) 

5. **jps定位具体java进程**

   jstack 定位线程状况，**重点关注：WAITING BLOCKED**

   jstack会把这个线程中的所有进行都列出来

   ![image-20220515170517140](image\image-20220515170517140.png) 

   

   eg：

   ![image-20220515191832700](image\image-20220515191832700.png) 

   waiting on <0x0000000088ca3310> (a java.lang.Object)  这个就是我们synchronize(o)的o对象

   ![image-20220515191904610](image\image-20220515191904610.png) 

   因为他在等待这把锁的释放

   

   假如有一个进程中100个线程，很多线程都在waiting on 某一把锁<xx> ，

   一定要找到是哪个线程持有这把锁

   

   怎么找？搜索jstack dump的信息，找<0x0000000088ca3310> ，看哪个线程持有这把锁RUNNABLE

   作业：1：写一个死锁程序，用jstack观察 2 ：写一个程序，一个线程持有锁不释放，其他线程等待

   

6. 为什么阿里规范里规定，线程的名称（尤其是线程池）都要写有意义的名称

   要是出了问题就更好的定位是哪个线程的状态有异常了

   ![image-20220515192600470](image\image-20220515192600470.png) 

7. jinfo pid 

   ![image-20220515194608494](image\image-20220515194608494.png) 

8. jstat -gc 动态观察gc情况 / 阅读GC日志发现频繁GC / arthas观察 / jconsole/jvisualVM/ Jprofiler（最好用）
   jstat -gc 4655 500 : 每个500个毫秒打印GC的情况
   如果面试官问你是怎么定位OOM问题的？如果你回答用图形界面（错误）
   1：已经上线的系统不用图形界面用什么？（cmdline arthas）
   2：图形界面到底用在什么地方？测试！测试的时候进行监控！（压测观察）

9. jmap - histo 4655 | head -20，执行该命令有一些影响，但是不高，查找有多少对象产生

10. jmap -dump:format=b,file=xxx pid ：  第66集======

    线上系统，内存特别大，jmap执行期间会对进程产生很大影响，甚至卡顿（电商不适合）
    1：设定了参数HeapDump，OOM的时候会自动产生堆转储文件
    2：<font color='red'>很多服务器备份（高可用），停掉这台服务器对其他服务器不影响</font>
    3：在线定位(一般小点儿公司用不到)

11. java -Xms20M -Xmx20M -XX:+UseParallelGC -XX:+HeapDumpOnOutOfMemoryError com.mashibing.jvm.gc.T15_FullGC_Problem01

    可以使用另外一个图形化工具jprofiler(要钱)

12. 使用MAT / jhat /jvisualvm 进行dump文件分析
     https://www.cnblogs.com/baihuitestsoftware/articles/6406271.html 
    jhat -J-mx512M xxx.dump
    http://192.168.17.11:7000
    拉到最后：找到对应链接
    可以使用OQL查找特定问题对象

13. 找到代码的问题



#### jconsole远程连接

![image-20220515195100868](image\image-20220515195100868.png) 

1. 程序启动加入参数：

   启动JMX协议

   > ```shell
   > java -Djava.rmi.server.hostname=192.168.17.11 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=11111 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false XXX
   > ```

   ![image-20220515195019956](image\image-20220515195019956.png) 

    

2. 如果遭遇 Local host name unknown：XXX的错误，修改/etc/hosts文件，把XXX加入进去

   > ```java
   > 192.168.17.11 basic localhost localhost.localdomain localhost4 localhost4.localdomain4
   > ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
   > ```

3. 关闭linux防火墙（实战中应该打开对应端口）

   > ```shell
   > service iptables stop
   > chkconfig iptables off #永久关闭
   > ```

4. windows上打开 jconsole远程连接 192.168.17.11:11111



#### jvisualvm远程连接  了解  有限制

 https://www.cnblogs.com/liugh/p/7620336.html （简单做法）



这样可定位哪些个类导致了OOM

![image-20220515195354505](image\image-20220515195354505.png) 



**如果面试官问你是怎么定位OOM问题的？如果你回答用图形化页面(错误)**

因为开启JMX远程监控对服务器的运行影响是很高的，代表着有个服务在一直影响我们的住进程，所以一般不会这么干

不用图形化界面用什么？

可以使用命令行 arthas

图形界面到底有没有用？用在什么地方？ 测试！

测试的 时候进行监控(压测观察)

如果已经上线了用什么？



![image-20220515200326933](image\image-20220515200326933.png) 

67节课面试怎么说====



面试怎么说：

先用top命令观察或者top -Hp或者jps命令定位具体的线程，如果是某一个线程出现了死锁的问题，使用jstack定位。



## arthas 在线排查工具

![image-20220516123036919](image\image-20220516123036919.png) 

命令：

jvm

![image-20220516123130338](image\image-20220516123130338.png) 



* 为什么需要在线排查？
  在生产上我们经常会碰到一些不好排查的问题，例如线程安全问题，用最简单的threaddump或者heapdump不好查到问题原因。为了排查这些问题，有时我们会临时加一些日志，比如在一些关键的函数里打印出入参，然后重新打包发布，如果打了日志还是没找到问题，继续加日志，重新打包发布。对于上线流程复杂而且审核比较严的公司，从改代码到上线需要层层的流转，会大大影响问题排查的进度。 
* jvm观察jvm信息
* thread定位线程问题
* dashboard 观察系统情况
* heapdump + jhat分析
* jad反编译
  动态代理生成类的问题定位
  第三方的类（观察代码）
  版本问题（确定自己最新提交的版本是不是被使用）
* redefine 热替换
  目前有些限制条件：只能改方法实现（方法已经运行完成），不能改方法名， 不能改属性
  m() -> mm()
* sc  - search class
* watch  - watch method
* 没有包含的功能：jmap

jhat -J-mx512M xxx.dump/hprof

http://ip:7000

拉到最后：找到对应链接

可以使用OQL查找特定问题对象



![image-20220516131058586](image\image-20220516131058586.png) 



![image-20220516131501072](image\image-20220516131501072.png)



### 解析dump文件



![image-20220516132358513](image\image-20220516132358513.png) 



### arthas的反编译和热替换

**反编译：**

![image-20220516132727008](image\image-20220516132727008.png)

**jad反编译**

可以定位动态代理生成类问题、第三方的类(观察代码)、版本问题(确定自己最新提交的版本是不是被使用)

**redefine热替换**

**目前有些限制条件：只能改方法实现(方法已经运行完成)，不能改方法名，不能改属性。**

实践

![image-20220516133423926](image\image-20220516133423926.png) 

![image-20220516133503939](image\image-20220516133503939.png) 



![image-20220516133540227](image\image-20220516133540227.png) 

**使用arthas查看代码**

![image-20220516133613702](image\image-20220516133613702.png) 

![image-20220516133652157](image\image-20220516133652157.png) 



![image-20220516133711490](image\image-20220516133711490.png) 



![image-20220516133725692](image\image-20220516133725692.png) 

使用他自己的ClassLoader来进行热替换



## OOM案例汇总1   73

OOM产生的原因多种多样，有些程序未必产生OOM，不断FGC(CPU飙高，但内存回收特别少)



## OOM案例汇总2 74

http-header-size 设置得过大

![image-20220517092050287](image\image-20220517092050287.png) 

![image-20220517092101353](image\image-20220517092101353.png)

![image-20220517092202539](image\image-20220517092202539.png) 

![image-20220517092229449](image\image-20220517092229449.png) 

MAT截图

![image-20220517092308035](image\image-20220517092308035.png) 

![image-20220517092359826](image\image-20220517092359826.png) 

![image-20220517092538627](image\image-20220517092538627.png) 

查看堆栈的时候发现是Http11OutputBuffer 这个对象占用的内存特别多，然后通过网上查找资料这个对象为什么占用了那么多，后来才发现不知道是谁把http-header-size参数改了，把他改得特别打，导致每个请求过来都分配了很大的空间



**Distuptor有个可以设置链长度，如果过大，然后对象大，消费完不主动释放，会溢出**

![image-20220517131922343](image\image-20220517131922343.png) 



### lambda表达式导致方法区溢出的问题(Method Area)

![image-20220517123433112](image\image-20220517123433112.png)

每一个lambda表达式对象，都会在内存当中产生一个新的class。 很少发生

![image-20220517123734230](image\image-20220517123734230.png) 

![image-20220517131714982](image\image-20220517131714982.png) 

栈溢出问题：

-Xss设定栈的大小

![image-20220517132515035](image\image-20220517132515035.png) 

 第一种写法好，第二种写法很有可能造成垃圾回收期回收不掉的问题



### 重写finalize引发的频繁GC

![image-20220517132611508](image\image-20220517132611508.png) 

为什么C++程序员会重写finalize？因为C++语言是自己手动回收的，(new   之后需要delete 才会调用**析构函数**)。

析构函数：

C++调用new语句的时候默认调用构造函数，调用delete的时候默认调用析构函数，所以delete是C++要手动回收内存的时候调用的语句。

重写的finalize所做的业务耗时比较长，当GC回收不过来的时候就会频繁的GC



### 如果有一个系统，内存消耗一直不超过10%，但是观察GC日志，发现FGC总是频繁产生，会是什么引起的？

有人显示的调用了System.gc() (这个问题出现太low)

![image-20220517133356273](image\image-20220517133356273.png) 

### new 大量线程，会产生 native thread OOM(比较low)

解决：应该使用线程池，减少堆空间，预留更多内存来产生native thread

JVM内存占物理内存比例 一般都是 50%~80%



## CMS复习

**并发的回收**

G1分而治之

CMS有两个毛病：一个是碎片化、一个是浮动垃圾

尤其是碎片化，一旦严重之后，后面就是一个单线程的FullGC，效率就会非常的低



初始标记

GC roots 阶段是STW的，但是根对象不会有太多，所以STW的时间非常的短

![image-20220517135443855](image\image-20220517135443855.png) 

**并发标记**(CMS Concurrent Mark)

最耗时的时间是从根上找其他的有用对象，这样子是最耗时间的，所以CMS把最耗时间的这部分和正常的工作线程放到一块来做，所以保证了应用程序的响应时间

在并发标记的时候，如有有些对象被重新引用了，CMS也不会把他当成垃圾回收掉，因为有一个重新标记的过程

**重新标记(CMS remark) ** 也会产生STW，但是被重新引用的对象其实不会太多，所以时间也不会太长

![image-20220517135949696](image\image-20220517135949696.png)



**并发清理**

在并发清理线程的过程中，工作线程其实也会产生垃圾，这些垃圾被称为浮动垃圾，这些垃圾会放在在下一次GC的时候处理



### 从线程角度来理解CMS

![image-20220517140700024](image\image-20220517140700024.png) 



## G1的特点

- 并发收集   
- 压缩空间不会延长GC的暂停时间
- 更易预测的GC暂停时间
- 适用不需要实现很高的吞吐量的场景



colored pointers 颜色指针

在JVM里面，如果一个指针没有进行压缩的话他是占64个bit，他会在这64个bit中拿出3个bit，在这其中做一些标记，当对象产生变化的过程中用这三个标记位标记，当垃圾回收器进行回收的时候会扫描那些变化过的指针



三色标记：把对象分成了三种颜色，每个不同的颜色来代表着他有什么变化



**G1是一种服务端应用使用的垃圾回收器，目标是用在多核、大内存的机器上，它在大多数情况下可以实现指定的GC暂停时间，同时还能保持较高的吞吐量。**

 **当他发现必须要进行GC回收的时候，他首先会收集Regions里边的存活对象最少的（Garbage First）**![image-20220517210821240](image\image-20220517210821240.png)



每一块区域(Region)在逻辑上依然属于分代，除了Old、Survivor、Eden这三个区，还有一个Humongous大对象区（超过region 的50%），对象有可能会跨两个region

![image-20220517212456389](image\image-20220517212456389.png) 

======

### card table

具体的对象都分配到了一个一个card中

由于做YGC的时候，需要扫描整个old区，效率非常低，所以JVM设计了Card Table，如果一个OLD区Card Table中有指向Y区，就将他设为Dirty，Card下次扫描时，只需要扫描Dirty Card。

在结构上Card Table用BitMap来实现



G1有好多个Card，有哪些Card需要被回收(垃圾最多的Card)，会被装到一个表格里，这个就叫做Collection Set

![image-20220517212534750](image\image-20220517212534750.png)



![image-20220517213920143](image\image-20220517213920143.png) 

问题：**里面的表是一个HashSet表记录着对象的引用，虽然可以使垃圾回收期不需要扫描整个堆了，但是占用的空间也就大了。所以ZGC里面就把这个废了**



![image-20220517214434021](image\image-20220517214434021.png) 



**G1会自动调整新老年代的比例**

![image-20220517215543111](image\image-20220517215543111.png) 



![image-20220517215714234](image\image-20220517215714234.png) 

*G1一定会产生FGC的，对象分配不下的时候就会产生，应该怎么办？

1. 扩内存

2. 提高CPU性能(回收的快，业务了逻辑产生对象的速度固定，垃圾回收越快，内存空间越大)

3. 降低**MixedGC**触发的阈值，让MixedGC提早发生(默认是45%)     **MixedGC** 相当于是一个CMS

   ![image-20220517220026698](image\image-20220517220026698.png) 

   45%整个堆内存整体占用空间已经超过了45%，超过这个空间之后，默认就会启动MixedGC，这个值是可以自己调整的

   ![image-20220517220128392](image\image-20220517220128392.png) 

   

G1的回收过程分为两种：

YGC     FGC





## 并发标记算法

**难点：在标记对象过程中，对象引用关系正在发生改变**

### *三色标记法

![image-20220517221214827](image\image-20220517221214827.png) 



**漏标是指本来是可用对象，但是由于没有遍历到被当成garbage回收掉了**

![image-20220517233410629](image\image-20220517233410629.png) 

![image-20220517233829386](image\image-20220517233829386.png) 

跟踪A指向D的增加或者跟踪B指向D的消失

标记完之后会把这个对象copy到另外一个Region



incremental update (CMS使用)

A一开始是黑色，然后A指向了D，那么就把A重新标记为灰色，在下次扫描的时候就会重新扫描他的成员变量。

SATB

把这个引用保存，下次扫描还扫描这个引用

![image-20220517233910546](image\image-20220517233910546.png)

**为什么G1使用SATB而不使用incremental update？**

第一种得重新再扫描一遍，而SATB只要把放到堆栈中的引用再扫描，和RememberSet配合使用

只要黑色和灰色指向白色的引用消失时，引用都会被push到堆栈，下次扫描时拿到这个引用，由于有Rset的存在，不需要扫描整个堆，去查找指向白色的引用，效率比较高

SATB 配合RSet

RSet会记录着别的引用指向这里面所有对象的引用，发现没有任何引用指向这个白色的对象，那么这个白色对象就成为了垃圾，直接干掉。



GC写屏障：当某个对象被一个引用指向他的时候，会把他记录到RSet中

RSet与赋值的效率：

由于RSet的存在，那么每次给对象赋引用的时候就得做一些额外的操作，这些操作指的是在RSet中做了一些额外的记录(在GC中被称为写屏障)

这个**写屏障** 不等于内存屏障

No Silver Bullet



## CMS日志分析

​							内存大小   堆大小

执行命令：java -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC com.mashibing.jvm.gc.T15_FullGC_Problem01

[GC (Allocation Failure) [ParNew: 6144K->640K(6144K), 0.0265885 secs] 6585K->2770K(19840K), 0.0268035 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 

YGC由ParNew来执行



> ParNew：年轻代收集器
>
> 6144->640：收集前后的对比
>
> （6144）：整个年轻代容量
>
> 6585 -> 2770：整个堆的情况
>
> （19840）：整个堆大小



CMS启动

```java
[GC (CMS Initial Mark) [1 CMS-initial-mark: 8511K(13696K)] 9866K(19840K), 0.0040321 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
	//8511 (13696) : 老年代使用（最大）
	//9866 (19840) : 整个堆使用（最大）
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.018/0.018 secs] [Times: user=0.01 sys=0.00, real=0.02 secs] 
	//这里的时间意义不大，因为是并发执行
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
	//标记Card为Dirty，也称为Card Marking
[GC (CMS Final Remark) [YG occupancy: 1597 K (6144 K)][Rescan (parallel) , 0.0008396 secs][weak refs processing, 0.0000138 secs][class unloading, 0.0005404 secs][scrub symbol table, 0.0006169 secs][scrub string table, 0.0004903 secs][1 CMS-remark: 8511K(13696K)] 10108K(19840K), 0.0039567 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
	//STW阶段，YG occupancy:年轻代占用及容量
	//[Rescan (parallel)：STW下的存活对象标记
	//weak refs processing: 弱引用处理
	//class unloading: 卸载用不到的class
	//scrub symbol(string) table: 
		//cleaning up symbol and string tables which hold class-level metadata and 
		//internalized string respectively
	//CMS-remark: 8511K(13696K): 阶段过后的老年代占用及容量
	//10108K(19840K): 阶段过后的堆占用及容量

[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
	//标记已经完成，进行并发清理
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
	//重置内部结构，为下次GC做准备
```





## *G1日志分析

不推荐指定G1的young区的参数，因为G1会自己动态调整

例如：Y区占的比例是5%~60%这是默认的比例

如果给G1指定了目标停顿时间 假如是20ms，如果一开始的时候比例是40%(10个块有4个块是Y区)，结果发现回收这么多块要50ms，那么G1会动态调整Y区的个数来达到要求时长的停顿目标为止

![image-20220518133307892](image\image-20220518133307892.png)

### G1日志详解

```java
			  复制存活对象                mixedGC开始
[GC pause (G1 Evacuation Pause) (young) (initial-mark), 0.0015790 secs]
//young -> 年轻代 Evacuation-> 复制存活对象 
//initial-mark 混合回收的阶段，这里是YGC混合老年代回收
   [Parallel Time: 1.5 ms, GC Workers: 1] //一个GC线程
      [GC Worker Start (ms):  92635.7]
      [Ext Root Scanning (ms):  1.1]
      [Update RS (ms):  0.0]
         [Processed Buffers:  1]
      [Scan RS (ms):  0.0]
      [Code Root Scanning (ms):  0.0]
      [Object Copy (ms):  0.1]
      [Termination (ms):  0.0]
         [Termination Attempts:  1]
      [GC Worker Other (ms):  0.0]
      [GC Worker Total (ms):  1.2]
      [GC Worker End (ms):  92636.9]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.0 ms]
   [Other: 0.1 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.0 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.0 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 0.0B->0.0B Heap: 18.8M(20.0M)->18.8M(20.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
//以下是混合回收其他阶段
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0000078 secs]
[GC concurrent-mark-start]
//无法evacuation，进行FGC
[Full GC (Allocation Failure)  18M->18M(20M), 0.0719656 secs]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 0.0B->0.0B Heap: 18.8M(20.0M)->18.8M(20.0M)], [Metaspace: 38
76K->3876K(1056768K)] [Times: user=0.07 sys=0.00, real=0.07 secs]

```

























































































## GC常用参数

* -Xmn -Xms -Xmx -Xss
  年轻代 最小堆 最大堆 栈空间
* -XX:+UseTLAB
  使用TLAB，默认打开
* -XX:+PrintTLAB
  打印TLAB的使用情况
* -XX:TLABSize
  设置TLAB大小
* -XX:+DisableExplictGC
  让System.gc()不管用 ，System.gc()是FGC
* -XX:+PrintGC
* -XX:+PrintGCDetails
* -XX:+PrintHeapAtGC
* -XX:+PrintGCTimeStamps
* -XX:+PrintGCApplicationConcurrentTime (低)
  打印应用程序时间
* -XX:+PrintGCApplicationStoppedTime (低)
  打印暂停时长
* -XX:+PrintReferenceGC （重要性低）
  记录回收了多少种不同引用类型的引用
* -verbose:class
  类加载详细过程
* -XX:+PrintVMOptions
* -XX:+PrintFlagsFinal  -XX:+PrintFlagsInitial
  *必须会用   查看垃圾回收器相关参数
* -Xloggc:opt/log/gc.log
* -XX:MaxTenuringThreshold
  GC升代年龄，最大值是15
* 锁自旋次数 -XX:PreBlockSpin 热点代码检测参数-XX:CompileThreshold 逃逸分析 标量替换 ... 
  这些不建议设置

## Parallel常用参数

* -XX:SurvivorRatio

* -XX:PreTenureSizeThreshold
  直接被分配到Old区的大对象到底多大
  
* -XX:MaxTenuringThreshold

  GC升代年龄，最大值是15

* -XX:+ParallelGCThreads
  并行收集器的线程数，同样适用于CMS，一般设为和CPU核数相同

* -XX:+UseAdaptiveSizePolicy
  自动选择各区大小比例

## CMS常用参数

* -XX:+UseConcMarkSweepGC
* -XX:ParallelCMSThreads
  CMS线程数量
* -XX:CMSInitiatingOccupancyFraction   CMS什么时候启动的比例
  使用多少比例的老年代后开始CMS收集，默认是68%(近似值)，如果频繁发生SerialOld卡顿，应该调小，（频繁CMS回收）
* -XX:+UseCMSCompactAtFullCollection
  在FGC时进行压缩  解决碎片化  当进行FGC的时候进行压缩
* -XX:CMSFullGCsBeforeCompaction
  多少次FGC之后进行压缩
* -XX:+CMSClassUnloadingEnabled
* -XX:CMSInitiatingPermOccupancyFraction
  达到什么比例时进行Perm回收
* GCTimeRatio
  设置GC时间占用程序运行时间的百分比
* -XX:MaxGCPauseMillis
  停顿时间，是一个建议时间，GC会尝试用各种手段达到这个时间，比如减小年轻代

## G1常用参数

* -XX:+UseG1GC
* -XX:MaxGCPauseMillis   停顿时间
  建议值，G1会尝试调整Young区的块数来达到这个值
* -XX:GCPauseIntervalMillis
  ？GC的间隔时间 
* -XX:+G1HeapRegionSize
  分区大小，建议逐渐增大该值，1 2 4 8 16 32。
  随着size增加，垃圾的存活时间更长，GC间隔更长，但每次GC的时间也会更长
  ZGC做了改进（动态分配区块大小，他会根据一个加权平均来算出回收的间隔等因素，进行一个动态的区块调整）
* G1NewSizePercent
  新生代最小比例，默认为5%
* G1MaxNewSizePercent
  新生代最大比例，默认为60%
* GCTimeRatio
  GC时间建议比例，G1会根据这个值调整堆空间
* ConcGCThreads
  线程数量
* InitiatingHeapOccupancyPercent
  启动G1的堆空间占用比例



## 纤程

线程和纤程之间的区别就在于一个是内核空间的多个程序并发，一个是用户空间的多个程序并发



使用quasar支持纤程

![image-20220519110551021](image\image-20220519110551021.png) 

![image-20220519111154225](image\image-20220519111154225.png) 

​	