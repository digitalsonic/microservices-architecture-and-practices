# Section 06

用Zookeeper作为服务注册中心，重构Section 05的代码。

使用Docker在本地启动一个Zookeeper：

```
docker pull zookeeper
docker run --name zookeeper -p 2181:2181 -d zookeeper
```
