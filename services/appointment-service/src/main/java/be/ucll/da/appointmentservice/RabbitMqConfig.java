package be.ucll.da.appointmentservice;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
    @Bean
    public Declarables declarables() {
        return new Declarables(
                new DirectExchange("x.appointment-patient"),
                new Queue("q.validate-patient"),
                new Binding("q.validate-patient", Binding.DestinationType.QUEUE, "x.appointment-patient", "patient-service", null),

                new FanoutExchange("x.appointment-validate-patient-reply"),
                new Queue("q.appointment-validate-patient-reply"),
                new Binding("q.appointment-validate-patient-reply", Binding.DestinationType.QUEUE, "x.appointment-validate-patient-reply", "appointment-service", null)
        );
    }
}
