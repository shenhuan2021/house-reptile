package com.lifesmile.reptile.service;

import com.lifesmile.reptile.config.EmailConfig;
import com.lifesmile.reptile.entity.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private EmailConfig emailConfig;

    private JavaMailSenderImpl mailSender;

    //阿里云服务器禁用了25端口，所以此处需要设置465端口

    @PostConstruct
    private JavaMailSenderImpl createAliMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(emailConfig.getFromHost());
        sender.setPort(Integer.valueOf(emailConfig.getFromPort()));
        sender.setUsername(emailConfig.getFromEmail());
        sender.setPassword(emailConfig.getFromPassword());
        sender.setDefaultEncoding("Utf-8");
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.port", emailConfig.getFromPort());
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.socketFactory.port", emailConfig.getFromPort());
        sender.setJavaMailProperties(prop);
        return this.mailSender = sender;
    }

    public void sendMail(String toEmail, House house) {
        try {
            String subject = "House Spider 提醒您，最新急售房源信息！";
            StringBuilder message = new StringBuilder("<html><p>详细信息：</p>");
            message.append("<p>标题：" + house.getTitle() + "</p>");
            message.append("<br> 区域：" + house.getCity() + " | " + house.getRegion());
            message.append("<br> 网址：" + house.getUrl());
            message.append("<br> 户型：" + house.getInfo());
            message.append(" <br> 总价：" + house.getTotalPrice());
            message.append(" <br> 单价：" + house.getAveragePrice());
            //message.append(" <br> 大小：" + house.getHouseArea() + "平米");
            message.append(" <br> 街道：" + house.getStreet());
            // message.append(" <br> 朝向：" + house.getTowards());
            //message.append(" <br> 修建：" + house.getCreateDate());
            message.append(" <br> 发布：" + house.getReleaseDate());
            //  message.append(" <br> 电梯：" + house.getElevator());
            message.append("</html>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // 设置utf-8或GBK编码，否则邮件会有乱码
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(emailConfig.getFromEmail(), "LifeSmile");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(message.toString(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
