# summer

the summer


## 平台简介

summer是一个完全开源的脚手架工程，永久提供给个人及企业免费使用。

summer建立的初衷是探寻高性能，高吞吐，高并发，高可扩展，弹性易维护等要求下的最佳微服务解决方案，summer也会本着这个初衷持续不断地 在数据结构，算法，业务流程，技术实现做出优化、迭代，并完全开源。

* 采用前后端分离的模式，前端功能会在后续逐步完成。
* 后端使用spring boot、spring cloud 及 apache-dubbo。
* 业务网关使用spring-cloud-gateway & nginx & lvs，文件网关使用webflux & lvs。
* 服务模块使用spring-boot，spring-webflux。
* 数据访问使用mysql & sharding-jdbc。
* 注册中心使用eureka、zookeeper。
* 分布式事务使用seata柔性事务。


## 未来规划

summer将会持续维护、迭代，提供更好的服务及解决方案。

* 用户隐私数据处理。
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
│       └── file                         // 文件网关
│       └── gateway                      // 业务网关
│   
├── readme                               //  帮助
│       └── doc                          //  文档
│       └── tools                        //  工具
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
│       └── secure                       // 安全服务
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
2. file: 独立的文件网关。
3. gateway: 非文件操作的业务网关。
4. registry: 路由服务注册中心。
5. base: 基础服务及第三方公共服务。
6. business: 业务服务，在这里带入自己的业务。
7. data: 用于数据分析，统计，操作日志等功能的服务。
8. finance: 财务及资金账户相关服务。
9. marketing: 推广/营销服务。
10. member: 成员服务。
11. portal: 首页及门户服务。
12. risk: 风控服务。
13. secure: 用于身份认证，鉴权，功能权限管理的安全服务。
14. shine: 公益服务。


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

* The front-end and back-end separation mode is adopted, and the front-end functions will be gradually completed in the follow-up.
* The backend uses spring boot, spring cloud and apache-dubbo.
* The service gateway uses spring-cloud-gateway & nginx & lvs, and the file gateway uses webflux & lvs.
* The service module uses spring-boot and spring-webflux.
* Data access uses mysql & sharding-jdbc.
* The registration center uses eureka and zookeeper.
* The distributed transaction uses seata flexible transaction.


## Future plan

Summer will continue to maintain, iterate, and provide better services and solutions.

* User privacy data processing.
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
│       └── file                         // File gateway
│       └── gateway                      // Business gateway
│   
├── readme                              
│       └── doc                          //  Documentation
│       └── tools                        //  Tools
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
│       └── secure                       // Security service
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
2. file: Independent file gateway.
3. gateway: Service gateway for non-file operations.
4. registry: Routing service registry.
5. base: Basic services and third-party public services.
6. business: Business services, bring your own business here.
7. data: Services for data analysis, statistics, operation logs and other functions.
8. finance: Financial and capital account related services.
9. marketing: Recommend/marketing services.
10. member: Member services.
11. portal: Home and portal services.
12. risk: Risk control services.
13. secure: Security services for identity authentication, authentication, and functional authority management.
14. Public benefit service.


## If useful, click the star









