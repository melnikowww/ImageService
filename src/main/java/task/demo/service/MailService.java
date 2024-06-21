package task.demo.service;

public interface MailService {

    void welcomeMail(String msg);
    void uploadMail(String msg);
    void downloadMail(String msg);
    void sendEmail(String toAddress, String subject, String message);
}
