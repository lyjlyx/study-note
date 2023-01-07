# Maven

**Maven中的每个项目都相当于是一个对象，各个之间的关系包含了：依赖、继承、聚合**

**创建.m2文件夹**

  mvn help:system

![image-20220305203704459](image/image-20220305203704459.png)





## Maven仓库之远程仓库



阿里云镜像：粘到mirrors标签中

![image-20220305205437605](image/image-20220305205437605.png)



### 下载优先级别

先本地->到配置文件中指定的仓库中

![image-20220305210502302](image/image-20220305210502302.png)

![image-20220305210802862](image/image-20220305210802862.png)



## Maven工程类型

![image-20220305211626415](image/image-20220305211626415.png)





![image-20220305211900342](image/image-20220305211900342.png)

![image-20220305213215727](image/image-20220305213215727.png)

![image-20220305213304613](image/image-20220305213304613.png)



![image-20220305213759777](image/image-20220305213759777.png)



![image-20220305221748583](image/image-20220305221748583.png)



如果父工程中加入了**<scope>import</scope>**相当于强制的指定了版本号

![image-20220305221906559](image/image-20220305221906559.png)





![image-20220305222432556](image/image-20220305222432556.png)



![image-20220305222550849](image/image-20220305222550849.png)

**maven打包，设置好配置之后配置文件就会被打包到classes目录下**

![image-20220305222632070](image/image-20220305222632070.png)

![image-20220305222656497](image/image-20220305222656497.png)

## 常见插件Tomcat插件

![image-20220305223008692](image/image-20220305223008692.png)

![image-20220305223051585](image/image-20220305223051585.png) 

显示Tomcat启动成功



![image-20220305223136107](image/image-20220305223136107.png)


































































































