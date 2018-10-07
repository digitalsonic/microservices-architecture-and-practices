# Section 09

基于Spring Cloud Stream接入了RabbitMQ，实现了完整的下单、付款、制作、取餐的过程。

需要在本地启动一个RabbitMQ：

```
docker run -d --hostname my-rabbit --name some-rabbit -p 9888:15672 -p 5672:5672 rabbitmq:3.6-management
```