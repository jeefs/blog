### 概念
Pod 是k8s调度的最小单位，一个pod代表一个应用实例，实例可以扩展多个pod，pod 可存在一个或多个容器
![img](./pic/k8s-logic-level.jpg)


### pod操作
```
kubectl create -f resources/ns.yaml //创建命名空间,namespace是命名空间用来隔离不同pod

kubectl apply -f resources/deployment_nginx.yaml -n tutorial //通过deployment创建pod，同时指定namespace为tutorial

kubectl get pods -n tutorial //获取指定空间下的所有pod

kubectl get pods -n tutorial -o wide //获取指定空间下所有pod更详细信息


kubectl delete deployment nginx-deployment //删除deployment，同时对应的pod会被删除


kubectl logs nginx-deployment-7fb96c846b-2nfv7 -n tutorial // 获取指定pod日志信息

 kubectl exec -it nginx-deployment-7fb96c846b-2nfv7 -n tutorial --container nginx -- /bin/bash  // 进入pod的指定容器，由于一个pod里会有多个容器，需要指定容器名称

```