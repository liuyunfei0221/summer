

这里使用zk用于注册及文件处理,在开始以下操作前,请确认你已经执行完summer-env\seata-env\zk_seata-env中的操作,并且zk处于启动中.




1.在对应的数据库中执行server-shell中的脚本,创建seata-server需要的数据库及表.

2.解压seata-server-1.4.2.zip文件,得到seata文件夹.

3.将summer-env\seata-env\server-conf中的registry.conf及file.conf两个文件复制到seata\seata-server-1.4.2\conf文件夹中,覆盖原文件即可.

4.将summer-env\seata-env\server-conf中的config.txt复制到seata\seata-server-1.4.2文件夹中.

5.将summer-env\seata-env\libs中的mysql-connector-java-8.0.24.jar复制到\server\seata\seata-server-1.4.2\lib\jdbc文件夹中.
	如果你使用其他版本的数据库,你需要在这里添加对应的jar,并根据对应版本处理代码中的undoLogParser.

6.双击\seata\seata-server-1.4.2\bin中的seata-server.bat启动seata-server.

7.双击apache-zookeeper-3.7.0-bin\bin文件夹中的zkCli.cmd启动client控制台.

8.打开summer-env\seata-env\zk-shell中的seata-node-shell.txt,根据你自己的环境修改对应的数据库相关配置,
	然后复制全部,然后在client控制台右键执行.

9.请自行修改yml中seata的相关配置