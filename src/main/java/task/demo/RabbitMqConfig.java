package task.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    Queue welcomeQueue() {
        return new Queue("welcomeQueue", false);
    }
    @Bean
    Queue uploadQueue() {
        return new Queue("uploadQueue", false);
    }
    @Bean
    Queue downloadQueue() {
        return new Queue("downloadQueue", false);
    }
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }
    @Bean
    Binding welcomeBinding(Queue welcomeQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(welcomeQueue).to(topicExchange).with("welcome");
    }
    @Bean
    Binding uploadBinding(Queue uploadQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(uploadQueue).to(topicExchange).with("upload");
    }
    @Bean
    Binding downloadBinding(Queue downloadQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(downloadQueue).to(topicExchange).with("download");
    }
}
