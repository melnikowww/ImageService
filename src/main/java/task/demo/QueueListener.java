package task.demo;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import task.demo.service.MailService;


@Component
@AllArgsConstructor
public class QueueListener {

    private final MailService mailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);
    @RabbitListener(queues = "welcomeQueue")
    public void welcomeProcess(String msg) {
        LOGGER.info("Message: \"" + msg + "\" is successfully added to welcomeQueue!");
        try {
            mailService.welcomeMail(msg);
        } catch (Exception ex) {
            LOGGER.info("ERROR: " + ex);
        }

    }
    @RabbitListener(queues = "uploadQueue")
    public void uploadProcess(String msg) {
        LOGGER.info("Message: \"" + msg + "\" is successfully added to uploadQueue!");
        mailService.uploadMail(msg);
    }
    @RabbitListener(queues = "downloadQueue")
    public void downloadProcess(String msg) {
        LOGGER.info("Message: \"" + msg + "\" is successfully added to downloadQueue!");
        mailService.downloadMail(msg);
    }
}
