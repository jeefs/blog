### init logger 
```
package logger

import (
	"github.com/natefinch/lumberjack"
	"go.uber.org/zap/zapcore"
	"log"
)

var Logger *log.Logger

func SetUp() {
	if Logger == nil {
		logWriter := zapcore.NewMultiWriteSyncer(getLogWriter())
		Logger = log.New(logWriter, "\r\n", log.Ldate|log.Ltime|log.Lshortfile)
	}
}

func getLogWriter() zapcore.WriteSyncer {
	lumberJackLogger := &lumberjack.Logger{
		Filename:   "./log/task.log",       //日志文件的位置
		MaxSize:    10,    //在进行切割之前，日志文件的最大大小（以MB为单位）
		MaxBackups: 5, //保留旧文件的最大个数
		MaxAge:     30,     //保留旧文件的最大天数
		Compress:   false,   //是否压缩/归档旧文件
	}
	return zapcore.AddSync(lumberJackLogger)
}
```

### using
```
initOnce.Do(func() {
    SetUp()
})
Logger.Printf("error is:%v",err.Error())
```
