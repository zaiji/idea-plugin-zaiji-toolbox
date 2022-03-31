# 仔鸡的idea插件

## 简介

这是个小插件集

## 开发要求

- 所有自行开发的组件都必须放在com.zaiji.plugin包下
- 所有自行开发的组件必须实现方法[public JPanel getContent()]返回值是组件的根面板
- 所有自行开发的组件必须标明注解（com.zaiji.annotation.PluginComponentInfo）

## 功能列表

- maven相关工具
    - 根据maven的相关info生成【mvn install】命令
    - 根据pom文件批量生成对应的【mvn install】命令
- log4j2配置文件生成工具
  - 可视化生成log4j2的配置文件，支持（xml，yml）格式
- 图片工具
  - 支持图片转base64
  - 支持base64转图片（暂不支持图片另存为）
- 时间工具
  - 日历（当前时间、农历、星期）
  - 时间转换（日期转秒、毫秒；秒、毫秒转日期）
  - 倒计时（功能待优化）
- cron表达式工具
  - 可视化生成、解析cron表达式（功能待完善，页面完成）