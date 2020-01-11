# dingtalk-springboot
Dingtalk enterprise application back-end (Springboot)

登录流程

1. 用户进入钉钉微应用后,前端 通过`jsapi`从钉钉容器中获取临时授权码`authcode` 

2. 前端携带`authcode`向后端`/api/login`接口发送POST请求
3. 后端携带 `authcode` 向钉钉后台服务器发请求，获取`userid`
4. 从数据库中查找是否有该`userid`的用户
   + 如果没有，则是该用户第一次登录，从钉钉服务器获取用户信息，存入数据库
5. 获取该用户的 `uid`  和 `auditorid` 加密成`token`， 添加到 `response.header`中,响应给前端

