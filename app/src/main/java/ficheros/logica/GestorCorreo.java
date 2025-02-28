package ficheros.logica;

import java.util.Properties;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ficheros.apykeys.Apykeys;


public class GestorCorreo {
	
	private static String emailFrom = Apykeys.getEmailFrom();
	private static String passwordFrom = Apykeys.getPasswordFrom();
	private String emailTo;
	private String subject;
	private String content;
	
	private Properties mProperties;
	private Session mSession;
	private MimeMessage mCorreo;
	
	/**
	 * Constructor de un gestor de correo por defecto
	 */
	public GestorCorreo() {
		mProperties = new Properties();
		
	}
	
	/**
	 * Crea un email con la configuracion establecida
	 * @param receptor : String
	 * @return
	 */
	public int createEmail(String receptor) {
		emailTo = receptor;
		subject = "Correo de verificacion EasyDepot";
		int token = (int) (Math.random()*9999)+1000;
		content = "Tu token es " + token;
		
		// Configuracion del correo
		
		mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user",emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");
		
		mSession = Session.getDefaultInstance(mProperties);
		
		
		try {
			mCorreo = new MimeMessage(mSession);
			mCorreo.setFrom(new InternetAddress(emailFrom));
			mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			mCorreo.setSubject(subject);
			mCorreo.setText(content, "ISO-8859-1", "html");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendEmail();
		return token;
	}
	
	/**
	 * Envia un email con la configuracion establecida
	 */
	private void sendEmail() {
		// siempre que hay un evento donde se mande un paquete a internet hay que crear un hilo
		new Thread(() -> {
			try {
				Transport mTransport = mSession.getTransport("smtp");
				mTransport.connect(emailFrom, passwordFrom);
				mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
				mTransport.close();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
