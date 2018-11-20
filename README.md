### 文本工厂
- 采用easyexcel+freeMark+javaFX8开发


- 背景  
>帮朋友编写的小工具，一个EXCEL作为数据表，向WORD文档里面填写内容，使用freeMark的语法即“${列名}”作为占位符，Excel第一列的数据
作为占位Key，之后的数据作为value

---
> 下图中上面是excel数据，下面是word文档的占位

![Alttext](https://raw.githubusercontent.com/flowclouds/textfactory/master/src/main/resources/1.first.jpg)


启动：

cd textfactory

mvn jfx:run