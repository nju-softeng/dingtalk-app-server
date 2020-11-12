<p align="center"><img width="100" src="https://raw.githubusercontent.com/zhanyeye/Figure-bed/win-pic/img/%E7%BB%A9%E6%95%88.png" alt="logo"></p>

<p align="center">
    <a href="https://github.com/zhanyeye/dingtalk-springboot/actions?query=workflow%3AProd"><img src="https://github.com/zhanyeye/dingtalk-springboot/workflows/Prod/badge.svg?branch=master" alt="Prod Status"></a>
  <a href="https://github.com/zhanyeye/dingtalk-springboot/actions?query=workflow%3ATest"><img src="https://github.com/zhanyeye/dingtalk-springboot/workflows/Test/badge.svg?branch=test"></a>
</p>

<h1 align="center">Dingtalk Springboot</h1>



#### 注意

+ 使用了lombok 插件简化代码，idea 需要安装lombok 插件，否则编译过不去
+ 由于目前钉钉小程序只支持 GET/POST, 考虑到兼容性这里的接口全部为GET/POST方式


###### 2020.09.10
修复发起投票时可能出现的bug

###### 2020.08.13
使用`docker-compose`代替`docker run`

###### 2020.06.23
修复老师也出现在绩效列表中的情况

###### 2020.06.14
测试环境的CD搭建


###### 2020.05.23
添加测试分支，主要分支介绍如下
+ master 分支用于部署
+ test 分支用于测试
+ dev 分支用于开发


###### 2020.04.02
使用 Github Actions 持续集成服务 [参考](https://segmentfault.com/a/1190000021914414)

###### 2020.3.26
完成页面：钉钉鉴权登陆，绩效审核与申请，绩效汇总，AC汇总，bug管理，任务绩效管理，论文管理，内部评审投票


