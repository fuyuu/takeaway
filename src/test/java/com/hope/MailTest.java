package com.hope;

import com.hope.util.PwdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class MailTest {
    @Autowired
    JavaMailSender sender;
    @Test
    void send() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("3402351070@qq.hope");
        msg.setTo("3402351070@qq.hope");
        msg.setSubject("测试");
        msg.setText("hello");
        sender.send(msg);
    }
    @Test
    void send2() {
        PwdUtil pwdUtil = new PwdUtil();
        System.out.println(pwdUtil.encode("123456"));
    }

}