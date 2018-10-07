# Section 11

容器化整个服务

* 增加Dockerfile，将服务打包为Docker镜像
* 修改程序配置，适应容器环境
* 增加docker-compose.yml，演示Docker Compose

执行命令打包：
```
mvn clean package docker:build -Dmaven.test.skip
```

通过docker命令启动：
```
docker run -d --name rabbit -p 5672:5672 rabbitmq:3.6-management
docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
docker run -d --name zookeeper -p 2181:2181 zookeeper:3.5

docker run --link rabbit:rabbit --link zookeeper:zookeeper --link zipkin:zipkin --name waiter-service takeout/waiter-service
docker run --link rabbit:rabbit --link zookeeper:zookeeper --link zipkin:zipkin --name chef-service takeout/chef-service
docker run --link rabbit:rabbit --link zookeeper:zookeeper --link zipkin:zipkin -p 8080:8080 --name customer-service takeout/customer-service
```

通过docker-compose启动：
```
docker-compose -f docker-compose.yml up -d
docker-compose scale chef-service=3
docker-compose logs --tail 10 --follow customer-service
```