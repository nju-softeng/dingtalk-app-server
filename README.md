<p align="center"><img width="100" src="https://i.loli.net/2020/11/12/8pP5y6eHwX1VfLd.png" alt="logo"></p>

<p align="center">
  <a href="https://github.com/zhanyeye/dingtalk-springboot/actions?query=workflow%3AProd">
    <img src="https://github.com/zhanyeye/dingtalk-springboot/workflows/Prod/badge.svg?branch=master" alt="Prod Status">
  </a>
  <a href="https://github.com/zhanyeye/dingtalk-springboot/actions?query=workflow%3ATest">
    <img src="https://github.com/zhanyeye/dingtalk-springboot/workflows/Test/badge.svg?branch=test">
  </a>
</p>

<h1 align="center">Dingtalk Springboot</h1>

### 目标与期望

基于钉钉微应用开发的实验室绩效管理系统，实现实验室的绩效、学分、论文评审管理与钉钉对接。  
主要功能有：绩效、学分申请与审核，论文评审投票及学分管理，实验室助研金计算等。

### 涉及的技术

<table>
  <tbody>
    <tr>
      <td align="center" valign="middle">
        <a href="https://spring.io/projects/spring-boot" target="_blank">
          <img width="50px" src="https://spring.io/images/spring-initializr-4291cc0115eb104348717b82161a81de.svg">
        </a>
        <p>
          <sub>SpringBoot</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://spring.io/projects/spring-data-jpa#overview" target="_blank">
          <img width="50px" src="https://i.loli.net/2020/11/13/pR8OtwsSacyuDU7.png">
        </a>
        <p>
          <sub>JPA</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://mybatis.org/mybatis-3/" target="_blank">
          <img width="50px" src="https://i.loli.net/2021/01/06/lSeIVLagyb4TFWN.png">
        </a>
        <p>
          <sub>Mybatis</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://dev.mysql.com/downloads/mysql/">
          <img width="50px" src="https://i.loli.net/2020/11/13/GQE3xMAbWd72hVc.png">
        </a>
        <p>
          <sub>MySQL 8</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://ding-doc.dingtalk.com/doc#/faquestions/vzbp02" target="_blank">
          <img width="50px" src="https://i.loli.net/2020/11/13/DVpc9nF2JToQyHg.png">
        </a>
        <p>
          <sub>Dingtalk SDK</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://docs.docker.com/" target="_blank">
          <img width="50px" src="https://i.loli.net/2020/11/13/27eyNzt698aoilM.png">
        </a>
        <p>
          <sub>Docker</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://docs.docker.com/compose/" target="_blank">
          <img width="50px" src="https://i.loli.net/2020/11/13/TcewOXGMWHLiNtE.jpg">
        </a>
        <p>
          <sub>Docker Compose</sub>
        </p>
      </td>
      <td align="center" valign="middle">
        <a href="https://github.com/features/actions" target="_blank">
          <img width="50px" src="https://i.loli.net/2021/01/06/EcsNSzQZl2TPyB6.png">
        </a>
        <p>
          <sub>Github Actions</sub>
        </p>
      </td>
    </tr>
  </tbody>
</table>

+ sdk使用代码：[DingTalkUtils.java](https://github.com/zhanyeye/dingtalk-springboot/blob/master/src/main/java/com/softeng/dingtalk/component/DingTalkUtils.java)
+ 前端代码：[dingtalk-vue](https://github.com/zhanyeye/dingtalk-vue)



### 注意事项

+ 使用了lombok 插件简化代码，idea 需要安装lombok 插件，否则编译过不去
+ 由于目前钉钉小程序只支持 GET/POST, 考虑到兼容性这里的接口全部为GET/POST方式
+ 系统启动时，[初始化操作](https://github.com/zhanyeye/dingtalk-springboot/blob/9e302075e2e8d55eb3736162066bf4bf203232c9/src/main/java/com/softeng/dingtalk/service/InitSys.java#L20)会调用钉钉SDK，拉取钉钉组织的所有用户, **若事先未在开发平台设置出口IP, SDK会报错导致启动失败**


### 系统部署
本项目使用 GitHub Actions 实现 CI，受外网网速限制，没有采用在 GitHub 机器上构件镜像，再拉取到服务器上运行的方式。而是在每次 CI 触发后，GitHub 机器 ssh 登陆服务器执行脚本，来拉取最新代码，构建镜像，并运行容器，具体如下：

1. 从GitHub仓库中拉去最新代码到服务器本地仓库
2. 使用mvn构建项目
3. `docker-compose build` 构建镜像
4. `docker-compose up -d` 在后台启动容器
5. `docker image prune -f` 清理无用的镜像 
   
GitHub Actions 的 CI 脚本如下
+ 生成环境：[.github/workflows/prod.yml](https://github.com/zhanyeye/dingtalk-springboot/blob/master/.github/workflows/prod.yml)
+ 测试环境：[.github/workflows/test.yml](https://github.com/zhanyeye/dingtalk-springboot/blob/master/.github/workflows/test.yml)

docker-compose 编排配置如下： 
+ [docker-compose.yml 配置文件](https://github.com/zhanyeye/dingtalk-springboot/wiki/docker_compose.yml)


