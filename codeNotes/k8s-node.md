### 概念
![img](./pic/k8s-logic-level.jpg)

Node是k8s集群中的节点，可以是物理机或虚拟机，一个节点下通常会包含多个pod，可以把节点看作是一台物理机


### 节点操作
```shell
kind create cluster // 在kind中创建节点
kind delete cluster //删除节点
```

创建节点后可以查看节点状态
```shell
kubectl get nodes -o wide //显示节点信息
kubectl describe nodes  //显示节点详细信息
```