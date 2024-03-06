### k8s常用命令
```
//查看指定空间下的所有pod信息 
kubectl get pod -n my-workspace -o wide 

//彻底删除pod
kubectl get deployment -n 命令空间
kubectl delete deployment xxxx（上面命令得到的name）-n 命令空间
```
