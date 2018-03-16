package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.dao.UserDAO;

import com.example.model.GoodsBasket;
import com.example.model.Order;
import com.example.model.User;

import javax.mail.internet.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.*;

@Service
public class SendMail  {

	@Autowired
    public JavaMailSender emailSender;
	@Autowired
	public TemplateEngine templateEndgine;
    @Autowired
    private User CurrentUser;
	@Autowired
	private UserDAO userDao;
    @Autowired
    private OrderService orderService;
 
	public void sendEmail( String to, String subject, String text, boolean isHtml) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            //helper.setFrom(emailSender.createMimeMessage());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            
            try {
            	 emailSender.send(mimeMessage);
                System.out.println("Mail sended");
            } catch (MailException mailException) {
                System.out.println("Mail send failed.");
                mailException.printStackTrace();
            }
            
        } catch (MessagingException e) {
            System.out.print("message send error "+ e);
        }
    }


	public void sendEmailCustomer(int id) {
         
		Order order = orderService.getOrder(id);	
		List<GoodsBasket> goods = order.getGoodsBasket();
		
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("goodsBasket", goods);
		String html = templateEndgine.process("MailTemplate", context);
		
		sendEmail(order.getEmail(), "Изменение статуса заказа №"+order.getId(), html, true );
    }
	
	
}
