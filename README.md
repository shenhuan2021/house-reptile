# house-spider
贝壳找房二手房爬虫，Springboot + Webmagic + SendEmail

## 简介
* 可指定城市、区域、小区、街道进行爬取
* 可制定关键词过滤（多个使用逗号隔开）：比如急售，捡漏等，主要用于捡漏
* 根据筛选条件，筛选出适合的数据发送到制定邮箱（可以多个，使用逗号隔开)

## 配置文件
    server:
      port: 8085
    
    search:
      city: xx
      regions: xx
      keyword: 急,xx #使用逗号分隔
    
    email:
      fromPassword: xxx
      fromEmail: xxx@163.com
      fromPort: 465
      fromHost: smtp.163.com
      toAddr: xxx@163.com，xxx@xx.com  #使用逗号分隔

## 使用maven打包
打完包house-reptile-v1.0.0.jar
把配置文件application.yml放到同一个目录

## 启动脚本：
nohup java -jar house-reptile-v1.0.0.jar  --spring.config.location=application.yml >  nohup.out 2>&1 &

## 座等捡漏