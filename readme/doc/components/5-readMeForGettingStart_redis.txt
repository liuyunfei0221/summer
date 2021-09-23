1.复制summer-env\5-redis-env中的Redis-x64-3.2.100.zip文件到你期望的安装目录,解压文件.

2.根据自己的需要配置redis.windows.conf及redis.windows-service.conf，然后双击redis-server.exe启动即可.

3.请自行修改yml中redis的相关配置


cache:
  serverMode: SINGLE
  nodes: [ 172.20.20.2:6379,172.20.20.2:6380,172.20.20.3:6379,172.20.20.3:6380,172.20.20.4:6379,172.20.20.4:6380 ]
  password: sfakjfiju99jjf87LJSDDU8
  maxRedirects: 4
  host: localhost
  port: 6379
  minIdle: 4
  maxIdle: 64
  maxTotal: 128
  maxWaitMillis: 16
  autoReconnect: true
  bufferUsageRatio: 32.0
  cancelCommandsOnReconnectFailure: false
  pingBeforeActivateConnection: false
  requestQueueSize: 64
  publishOnScheduler: true
  tcpNoDelay: true
  connectTimeout: 4
  keepAlive: false
  suspendReconnectOnProtocolFailure: false
  fixedTimeout: 4
  commandTimeout: 4
  shutdownTimeout: 8
  shutdownQuietPeriod: 4
  entryTtl: 60