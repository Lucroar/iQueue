package com.Lucroar.iQueue.Otp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailSender  {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailSender(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(String toEmailAdd, String otp) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        String subject = "iQueue OTP";
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", otp);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM-dd-yyyy hh:mm:ss a");
        String now = LocalDateTime.now().format(format);
        variables.put("time", "Generated Time: " + now);
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("otp_email_content.html", context);

        helper.setFrom("iqueue.system.001@gmail.com");
        helper.setTo(toEmailAdd);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
//        helper.addInline("topBar", new ClassPathResource("/static/images/topBar.jpg"));
//        helper.addInline("emailLogo", new ClassPathResource("/static/images/email_logo.png"));
        mailSender.send(message);
    }
}
