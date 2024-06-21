package task.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(String message, String key) {
        rabbitTemplate.convertAndSend("exchange", key, message);
    }
}
