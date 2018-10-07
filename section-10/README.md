# Section 10

基于Section 09的代码，使用Spring Cloud Sleuth基于OpenZipkin实现了服务追踪，使用Web方式提交信息。

* customer-service稍作修改，使用POST请求来触发后续操作
* 所有服务接入spring-cloud-starter-zipkin
* 在本地9411端口启动一个OpenZipkin

```
docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
```