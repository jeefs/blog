### Container 容器
Container（容器）是一种便携式、轻量级的操作系统级虚拟化技术。它使用 namespace 隔离不同的软件运行环境，并通过镜像自包含软件的运行环境，从而使得容器可以很方便的在任何地方运行

### Pod
Kubernetes 使用 Pod 来管理容器，每个 Pod 可以包含一个或多个紧密关联的容器。
Pod 是一组紧密关联的容器集合，它们共享 IPC 和 Network namespace，是 Kubernetes 调度的基本单位。Pod 内的多个容器共享网络和文件系统，可以通过进程间通信和文件共享这种简单高效的方式组合完成服务。

![img](https://4130827217-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2F-LDAOok5ngY4pc1lEDes-887967055%2Fuploads%2Fgit-blob-d22681ecc307ddc2ef198626b120cff1ac83063c%2Fpod%20(5).png?alt=media)


在 Kubernetes 中，所有对象都使用 manifest（yaml 或 json）来定义，比如一个简单的 nginx 服务可以定义为 nginx.yaml，它包含一个镜像为 nginx 的容器：

```
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  containers:
  - name: nginx
    image: nginx
    ports:
    - containerPort: 80
```

### Node 节点

Node 是 Pod 真正运行的主机，可以是物理机，也可以是虚拟机。为了管理 Pod，每个 Node 节点上至少要运行 container runtime（比如 docker 或者 rkt）、kubelet 和 kube-proxy 服务。

![img](https://4130827217-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2F-LDAOok5ngY4pc1lEDes-887967055%2Fuploads%2Fgit-blob-e96ac09eb3f696e082efdf37da56de12652094a0%2Fnode%20(3).png?alt=media)



### Service 服务
Service 是应用服务的抽象，通过 labels 为应用提供负载均衡和服务发现。匹配 labels 的 Pod IP 和端口列表组成 endpoints，由 kube-proxy 负责将服务 IP 负载均衡到这些 endpoints 上。
每个 Service 都会自动分配一个 cluster IP（仅在集群内部可访问的虚拟地址）和 DNS 名，其他容器可以通过该地址或 DNS 来访问服务，而不需要了解后端容器的运行。

![img](https://4130827217-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2F-LDAOok5ngY4pc1lEDes-887967055%2Fuploads%2Fgit-blob-5c445108e5b419300e004379bb2749baa4272644%2F14731220608865.png?alt=media)

```
apiVersion: v1
kind: Service
metadata:
  name: nginx
spec:
  ports:
  - port: 8078 # the port that this service should serve on
    name: http
    # the container on each pod to connect to, can be a name
    # (e.g. 'www') or a number (e.g. 80)
    targetPort: 80
    protocol: TCP
  selector:
    app: nginx
```

### Label 标签
Label 是识别 Kubernetes 对象的标签，以 key/value 的方式附加到对象上（key 最长不能超过 63 字节，value 可以为空，也可以是不超过 253 字节的字符串）。
Label 不提供唯一性，并且实际上经常是很多对象（如 Pods）都使用相同的 label 来标志具体的应用。
Label 定义好后其他对象可以使用 Label Selector 来选择一组相同 label 的对象（比如 ReplicaSet 和 Service 用 label 来选择一组 Pod）。Label Selector 支持以下几种方式：

- 等式，如 app=nginx 和 env!=production
- 集合，如 env in (production, qa)
- 多个 label（它们之间是 AND 关系），如 app=nginx,env=test



![img](https://jimmysong.io/kubernetes-handbook/images/architecture.png)

<center>kubernetes架构</center>



引用 

- https://feisky.gitbooks.io/kubernetes/content/architecture/architecture.html
- https://kubernetes.feisky.xyz/introduction/concepts



