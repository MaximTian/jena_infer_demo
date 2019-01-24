## Jena 推理

### Jena简介

Apache Jena（后文简称Jena），是一个开源的Java语义网框架（open source Semantic Web Framework
for Java），用于构建语义网和链接数据应用。Jena提供了RDFS、OWL和通用规则推理机，其中RDFS和OWL推理机也是基于Jena自身的通用规则推理机实现推理。

### 运行环境

jdk-1.8

apache-maven

### 代码结构

src文件夹下demo文件夹

1. jenaMain：程序主体
2. utils：基本的数据处理模块，包含输入格式的转换、文件的读取。

resources文件

1. rawData文件夹：主要存放三元组数据triple_data.txt和查询文件query.txt
   - triple_data格式为：[entity1 entity2 relation], 以tab分隔。格式转换的内容保存在triple_data.ttl文件中
   - query格式为：entity。即待推理的实体名称列表
2. demo.rules文件：推理规则文件，里面的公式均人为定义
3. query_result.txt：实体查询的结果
4. triple_data.ttl：由triple_data.txt生成的ttl格式文件

### 使用说明

1. demo.rules规则文件格式说明：

规则格式如下：

[rule_name: (?A relation:r1 ?B), (?A relation:r2 ?C), ... -> (?B relation: rn, ?C), ...]

规则推理以"->"为标识符，左边为已有的规则(可以多条规则)，右边为推理的规则(可多条规则)。

示例:

```html
[r2: (?A relation:丈夫 ?B), (?A relation:儿子 ?C) -> (?B relation:儿子 ?C)]
```
2. ttl文件格式说明：

ttl文件开头部分需要声明各种元素的命名以及对应的url。格式如下：

@prefix value_name:   url

示例：

```html
@prefix entity:   <http://jena_demo/entity#> .
```

这里value_name为entity， "< http://jena_demo/entity# > ."为url部分，注意必须以" ."结尾。

关系三元组格式为：

value_name: e1 relation_name: r1 value_name: e2 （tab分隔）

示例：

```html
entity:张飞   relation:二哥    entity:关羽 .
```

3. 查询文件

查询文件主要输入待推理的实体对象。jena通过读入待推理对象，可自动根据规则输出与该对象相关的规则。查询文件为resources/rawData/query.txt

4. 查询结果

文件的输出内容主要存放在query_result.txt中，基本格式为：

(entity1，relation1，entity2)

### 运行流程

将整理的三元组内容、已经定义好的规则和待查询的实体分别写入triple_train.txt、demo.rules和query.txt文件后，在java编辑器下执行jenaMain.java可运行整个jena推理程序。Demo执行过程如下：

1. 首先将triple_data.txt内容转换成triple_data.ttl格式文件

2. 根据ttl内容和规则文件demo.rules生成jena推理机

3. 读取query的实体名单。实体以jena查询语句的格式输入推理机中，获取实体相关内容

4. 推理的结果以三元组的形式输出到query_result.txt中

   其中demo.rules里面的规则可以根据需要进行自定义，需要按照规则格式进行严格定义。


