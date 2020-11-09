package es.source.code.activity;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class MailSender {
    private String context;
    public MailSender(String context){
        this.context  = context;
    }
    public void MailSender() throws MessagingException{
        Properties properties = new Properties();
        //设置服务器地址和端口，网上搜的到
        properties.put("smtp.163.com", "25");
        //是否验证
        properties.put("mail.smtp.auth",true);
        Session session=Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        //设置发件人
        message.setFrom(new InternetAddress("13588254772@163.com"));
        //设置收件人
        InternetAddress toAddress = new InternetAddress("1161602163@qq.com");
        message.addRecipient(Message.RecipientType.TO, toAddress);
        //标题
        message.setSubject("android test");
        //纯文本的话用setText()就行，不过有附件就显示不出来内容了
        //MimeBodyPart textBody = new MimeBodyPart();
        //textBody.setContent(content,"text/html;charset=gbk");
        message.setText(context);
        message.setContent("this is content","text/html;charset=gbk");
        //发送时间
        message.setSentDate(new Date());
        //发送的内容，文本和附件
        message.saveChanges();
        //创建邮件发送对象，并指定其使用SMTP协议发送邮件
        Transport transport=session.getTransport("smtp");
        //登录邮箱，账号密码
        transport.connect("smtp.163.com","13588254772@163.com", "zyjdyx123");
        //发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
    }
}


