package com.benchmark.core.util;

/**
 * TODO Put here a description of what this class does.
 *
 * @author ssamaji.
 *         Created Jun 23, 2018.
 */
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import com.benchmark.core.constants.CommonConstants;

import java.util.*;

public class SendMail {
//reportFileName = TestExecutionResultFileName
	public static boolean execute(String reportFileName,String body, String subject) throws Exception {
		String path=reportFileName;
		String sub = subject;
		String emailbody = body;
		String[] to={CommonConstants.MAIL_TO_EMAIL_ID};
		String[] cc={CommonConstants.EMAIL_CC_EMAIL_ID};
		String[] bcc={CommonConstants.MAIL_USER_NAME};

		SendMail.sendMail(CommonConstants.MAIL_USER_NAME,CommonConstants.MAIL_PASSWORD,
			"smtp.gmail.com","465","true","true",true,"javax.net.ssl.SSLSocketFactory","true",
			to,cc,bcc,sub,emailbody,path,reportFileName);
		return true;
	}

	public static boolean sendMail(String userName, String passWord, String host, String port, 
			String starttls, String auth,boolean debug, String socketFactoryClass, String fallback,
			String[] to, String[] cc, String[] bcc, String subject,String text, String attachmentPath,String attachmentName){

		//Object Instantiation of a properties file.
		Properties props = new Properties();

		props.put("mail.smtp.user", userName);

		props.put("mail.smtp.host", host);

		if(!"".equals(port)){
			props.put("mail.smtp.port", port);
		}

		if(!"".equals(starttls)){
			props.put("mail.smtp.starttls.enable",starttls);
			props.put("mail.smtp.auth", auth);
		}

		if(debug){
			props.put("mail.smtp.debug", "true");
		}else{
			props.put("mail.smtp.debug", "false");
		}

		if(!"".equals(port)){
			props.put("mail.smtp.socketFactory.port", port);
		}
		if(!"".equals(socketFactoryClass)){
			props.put("mail.smtp.socketFactory.class",socketFactoryClass);
		}
		if(!"".equals(fallback)){
			props.put("mail.smtp.socketFactory.fallback", fallback);
		}

		try{

			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(debug);
			MimeMessage msg = new MimeMessage(session);
			msg.setText(text);
			msg.setSubject(subject);

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(messageBodyPart);

			msg.setContent(multipart);
			msg.setFrom(new InternetAddress(userName));

			for(int i=0;i<to.length;i++){
				msg.addRecipient(Message.RecipientType.TO, new	InternetAddress(to[i]));
			}

			for(int i=0;i<cc.length;i++){
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}

			for(int i=0;i<bcc.length;i++){
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}

			msg.saveChanges();

			Transport transport = session.getTransport("smtp");

			transport.connect(host, userName, passWord);

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;

		} catch (Exception mex){
			mex.printStackTrace();
			return false;
		}
	}
}