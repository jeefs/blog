   			//实例化任务队列
			Server, err = machinery.NewServer(cnf)
			if err != nil {
				panic(err.Error())
			}
			err = os.MkdirAll(logPath, 0744)
			if err != nil {
				fmt.Println("创建任务队列日志文件路径失败,请求后端排除错误")
				panic(err.Error())
			}
			//任务日志
			file, err := os.OpenFile(logPath+"/calculateMardTask.log", os.O_CREATE|os.O_APPEND|os.O_WRONLY, 0744)
			if err != nil {
				fmt.Println("打开任务队列日志文件失败,请求后端排除错误")
				panic(err.Error())
			}
			log.SetOutput(file)
			//mard值计算任务,立即投递
			calculateMardTask := func() error {
				perId, err := glucoseServer.CalculateDeviceMard()
				message := Message{}
				if err != nil {
					message.Error = err.Error()
					message.Data = map[string]interface{}{
						"info":  "计算失败",
						"perId": perId,
						"ctime": time.Now().Format("2006-01-02 15:04:05"),
					}
					bytes, _ := json.Marshal(message)
					messageStr := fmt.Sprintf(string(bytes))
					err = errors.New(messageStr)
					//错误日志
					log.Printf("ERROR:计算设备mard值失败,%v", messageStr)
				} else {
					message.Data = map[string]interface{}{
						"info":  "计算成功",
						"ctime": time.Now().Format("2006-01-02 15:04:05"),
					}
					bytes, _ := json.Marshal(message)
					messageStr := fmt.Sprintf(string(bytes))
					//成功日志
					log.Printf("SUCCESS:计算设备mard值成功,%v", messageStr)
				}
				return err
			}
			calculateMardSignature := &tasks.Signature{
				Name: "calculateMardTask",
				//RetryCount: 3,     //任务重试次数
				Immutable: true, //不可变任务
			}
			//注册任务
			err = Server.RegisterTasks(map[string]interface{}{
				"calculateMardTask": calculateMardTask,
			})
			//定时调用,每小时统计一次
			err = Server.RegisterPeriodicTask("0 * * * *", "calculateMardTask", calculateMardSignature)
			if err != nil { //注册任务失败
				panic(err.Error())
			}
			//创建worker
			worker := Server.NewWorker("worker01", 10)
			go func() {
				err = worker.Launch()
				if err != nil {
					panic(err.Error())
				}
			}()
