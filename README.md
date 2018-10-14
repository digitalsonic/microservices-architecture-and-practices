# 微服务架构与实战

QConShanghai 2018深度培训第二天演示代码。

## 代码说明

* Section 01 - 一个最基本的微服务
* Section 02 - 重构了Section 01的服务，丰富了服务内容
* Section 03 - 为Section 02的服务编写了一个基于RestTemplate的客户端
* Section 04 - 用Spring Data REST重构了Section 02和Section03的代码
* Section 05 - 使用Spring Cloud Netflix，通过Eureka实现服务注册与发现
* Section 06 - 使用Zookeeper代替Section 05的Eureka实现服务注册与发现
* Section 07 - 演示Hystrix断路器
* Section 08 - 使用Zookeeper作为配置中心，加载集中配置
* Section 09 - 增加RabbitMQ，使用Spring Cloud Stream，新增了chef-service
* Section 10 - 演示了Spring Cloud Sleuth的使用，基于OpenZipkin实现服务追踪
* Section 11 - 将Section 10的所有服务打包成Docker镜像，并提供DockerCompose配置
* Section 12 - 基于Section 06重构代码，去除Spring Cloud依赖，演示在k8s上运行代码

