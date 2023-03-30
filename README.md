# summer

the summer

## 平台简介

summer是一个实验性的项目。

* 采用前后端分离的模式，前端功能会在后续逐步完成。
* 后端使用spring boot、spring cloud 及 apache-dubbo。
* 业务网关使用spring-cloud-gateway & nginx & lvs，文件网关使用webflux & lvs。
* 服务模块使用spring-boot，spring-webflux。
* 数据访问使用mysql & sharding-jdbc。
* 注册中心使用eureka、zookeeper。
* 分布式事务使用seata柔性事务。

## 未来规划

* 集成k8s。
* shardingsphere混合模式。
* 服务网格。
* apollo配置中心。
* 引入分布式任务调度。
* mysql & hbase & es 的海量数据解决方案。
* seata tcc/状态机。
* 集成监控告警。
* 完善风控体系。
* 集成消息。
* 加入大数据分析。

## 系统服务

~~~
summer     
├── common                               // 公共组件
│   
├── entrance                             
│       └── event                        // 事件网关
│       └── gateway                      // 业务网关
│       └── media                        // 媒体网关
│       └── message                      // 消息网关
│       └── verify                       // 验证码网关
│   
├── readme
│       └── doc                          // 文档
│       └── tools                        // 工具
│
├── registry                             // 注册中心
│   
├── service          
│       └── base                         // 基础/第三方服务
│       └── business                     // 业务服务
│       └── data                         // 数据分析服务
│       └── finance                      // 财务服务
│       └── marketing                    // 营销服务
│       └── member                       // 成员服务
│       └── portal                       // 门户服务
│       └── risk                         // 风控服务
│       └── auth                         // 安全服务
│       └── shine                        // 公益服务
~~~

## 技术栈

* spring boot
* spring webflux
* mybatis
* spring cloud
* apache dubbo
* apache pulsar
* mysql
* snowflake
* apache sharding-jdbc
* alibaba seata
* redis
* zookeeper
* elastic search
* caffeine
* jwt
* mongo
* redisson

## 服务说明

1. common: 公共依赖项，组件，工具类等。
2. gateway: 非文件操作的业务网关。
3. media: 独立的文件网关。
4. verify: 独立的验证码网关。
5. registry: 路由服务注册中心。
6. base: 基础服务及第三方公共服务。
7. business: 业务服务，在这里带入自己的业务。
8. data: 用于数据分析，统计，操作日志等功能的服务。
9. finance: 财务及资金账户相关服务。
10. marketing: 推广/营销服务。
11. member: 成员服务。
12. portal: 首页及门户服务。
13. risk: 风控服务。
14. auth: 用于身份认证，鉴权，功能权限管理的安全服务。
15. shine: 公益服务。

## 有用的话，记得star

------------------------------------------------------------------------------------------------------------------

# summer

the summer

## Introduction to the platform

Summer is a completely open source scaffolding project, which is permanently provided to individuals and enterprises for
free use.

The original intention of summer was to explore the best microservice solutions under the requirements of high
performance, high throughput, high concurrency, high scalability, flexibility and easy maintenance. Summer will continue
to develop data structures, algorithms, and business based on this original intention. The process and technical
realization are optimized, iterated, and completely open source.

* The front-end and back-end separation mode is adopted, and the front-end functions will be gradually completed in the
  follow-up.
* The backend uses spring boot, spring cloud and apache-dubbo.
* The service gateway uses spring-cloud-gateway & nginx & lvs, and the file gateway uses webflux & lvs.
* The service module uses spring-boot and spring-webflux.
* Data access uses mysql & sharding-jdbc.
* The registration center uses eureka and zookeeper.
* The distributed transaction uses seata flexible transaction.

## Future plan

Summer will continue to maintain, iterate, and provide better services and solutions.

* Integrated k8s.
* Shardingsphere mixed mode.
* Service mesh。
* Apollo configuration center.
* Distributed task scheduling.
* The massive data solution of mysql & hbase & es.
* Seata tcc/state machine.
* Integrated monitoring alarm.
* Improve the risk control system.
* Integrated message.
* Big data analysis.

## Modules

~~~
summer     
├── common                               // Public component
│   
├── entrance                             
│       └── gateway                      // Business gateway
│       └── media                        // Media gateway
│       └── verify                       // Verify gateway
│   
├── readme                              
│       └── doc                          // Documentation
│       └── tools                        // Tools
│
├── registry                             // Registry
│   
├── service          
│       └── base                         // Basic/third-party services
│       └── business                     // Business service
│       └── data                         // Data analysis service
│       └── finance                      // Financial services
│       └── marketing                    // Marketing service
│       └── member                       // Member Service
│       └── portal                       // Portal service
│       └── risk                         // Risk Control Service
│       └── auth                         // Security service
│       └── shine                        // Public benefit service
~~~

## Stacks

* spring boot
* spring webflux
* mybatis
* spring cloud
* apache dubbo
* apache pulsar
* mysql
* snowflake
* apache sharding-jdbc
* alibaba seata
* redis
* zookeeper
* elastic search
* caffeine
* jwt
* mongo
* redisson

## Service description

1. common: Common dependencies, components, tool classes, etc.
2. gateway: Service gateway for non-file operations.
3. media: Independent file gateway.
4. verify: Independent verify gateway.
5. registry: Routing service registry.
6. base: Basic services and third-party public services.
7. business: Business services, bring your own business here.
8. data: Services for data analysis, statistics, operation logs and other functions.
9. finance: Financial and capital account related services.
10. marketing: Recommend/marketing services.
11. member: Member services.
12. portal: Home and portal services.
13. risk: Risk control services.
14. auth: Security services for identity authentication, authentication, and functional authority management.
15. Public benefit service.

## If useful, click the star









