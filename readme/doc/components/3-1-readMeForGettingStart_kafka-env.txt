请确认你已经启动了zk环境.


1.将summer-env\2-kafka-env中的kafka_2.13-2.8.0.tgz复制到你期望的目录中,并解压.

2.根据你的需要修改kafka_2.13-2.8.0\config中的server.properties.

3.修改summer-env\2-kafka-env中的kafka启动脚本,双击以启动kafka,或使用命令行启动kafka.



建议将apache-pulsar作为消息中间件使用, 作为apache下一代顶级消息中间件, pulsar以云原生为方向, 使用存储与计算分离架构,且原生支持异地,新版本支持事务,支持延时队列,动态扩容,
弹性伸缩等特性,除学习成本外,对比kafka优势明显