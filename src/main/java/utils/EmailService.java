package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.activation.DataSource;
import java.io.File;
import java.util.Properties;
import jakarta.mail.internet.MimeMessage;


public class EmailService {

    private static final String FROM_EMAIL = "yasminekouki84@gmail.com";  //
    private static final String EMAIL_PASSWORD = "khpo rmoj ptng jgjv";  //

    public static void sendQRCodeEmail(String toEmail, String eventName, File qrCodeFile) throws MessagingException {
        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Create session with authenticator and enable debug mode
        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        session.setDebug(true); // Enable debug mode to see what's happening

        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your QR Code for Event: " + eventName);

        // Create the message body part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Dear Participant,\n\n" +
                              "Thank you for participating for the event: " + eventName + "\n" +
                              "Please find attached your QR code for the event for your entrance!\n\n" +
                              "Best regards,\n Craftify Team <3 ");

        // Create attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(qrCodeFile);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(qrCodeFile.getName());

        // Create multipart message
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        // Set the multipart message
        message.setContent(multipart);

        try {
            // Send the message
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
