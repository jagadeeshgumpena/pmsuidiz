
  package com.dizitiveit.pms.service;
  
  import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.mail.SimpleMailMessage; 
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
  
  @Service 
  public class EmailServiceImpl implements EmailService {
  
  @Autowired 
  public JavaMailSender emailSender;
  
  public void sendSimpleMessage( String to, String subject, String text) {
  SimpleMailMessage message = new SimpleMailMessage(); 
  message.setTo(to);
  message.setSubject(subject); 
  message.setText(text);
  emailSender.send(message); 
  }
  
  @Async
  public void sendEmail(SimpleMailMessage email) {
	  emailSender.send(email);
  }
  
  }
 