# Section 12

在Kubernetes上运行一个简单的TakeOut示例。

* 基于Section 06修改代码，去除Spring Cloud相关内容，简化部分代码
* 修改程序配置，适应k8s容器环境
* 修改Docker打包相关配置，将镜像上传至阿里云的私有镜像
* 增加用于本地minikube和阿里云k8s的部署文件

执行命令打包：
```
mvn clean package docker:build -Dmaven.test.skip
```

在minikube中执行部署命令：
```
kubectl create -f takeout-minikube.yml
```

部署到阿里云（需要事先在k8s上配置私有镜像的secret）：
```
kubectl create -f takeout-aliyun.yml
```

在阿里云上如果部署了Istio，先设置自动注入SideCar：

```
kubectl label namespace default istio-injection=enabled
```

在`istio`目录中有一些Istio的演示，先运行`kubectl create -f istio-destination.yml`创建Destination，随后再运行其他的Yaml。

可以用下面的命令开启Grafana，观察服务的调用情况：
```
kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') 3000:3000 &
```
