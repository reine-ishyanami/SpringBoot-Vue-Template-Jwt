# 前后端分离项目（JWT方案）
基于[SpringBoot-Vue-Template-Jwt](https://github.com/itbaima-study/SpringBoot-Vue-Template-Jwt)
进行了一些魔改，只实现了基本登录注册功能

## 魔改总结

### 后端
1. 将构建工具从`maven`更换为`gradle`
2. 原项目中用到`fastjson2`的地方更换为`jackson`
3. 原项目中用到`mybatis-plus`的地方更换为`jpa`
4. 原项目中使用switch判断进行邮件对象构建处改用`enum`实现
5. 使用ThreadLocal存储用户数据
6. 原项目中用到`rabbit-mq`处改用`redis`的`stream`实现消息订阅发布

### 前端
1. 原项目中用到`element-plus`的地方更换为`vuetify`
2. 使用自定义事件控制弹窗