package be.ucll.da.appointmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public JavaTimeModule javaTimeModule(){
        return new JavaTimeModule();
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

                // Patient service
                new DirectExchange("x.appointment-patient"),
                new Queue("q.validate-patient"),
                new Binding("q.validate-patient", Binding.DestinationType.QUEUE, "x.appointment-patient", "patient-service", null),

                new FanoutExchange("x.patient-validated"),
                new Queue("q.appointment-validate-patient-reply"),
                new Binding("q.appointment-validate-patient-reply", Binding.DestinationType.QUEUE, "x.patient-validated", "appointment-service", null),

                // Doctor service
                new DirectExchange("x.appointment-doctor"),
                new Queue("q.check-doctors-employed"),
                new Binding("q.check-doctors-employed", Binding.DestinationType.QUEUE, "x.appointment-doctor", "doctor-service", null),

                new FanoutExchange("x.doctors-employed-checked"),
                new Queue("q.appointment-doctors-employed-reply"),
                new Binding("q.appointment-doctors-employed-reply", Binding.DestinationType.QUEUE, "x.doctors-employed-checked", "doctor-service", null),

                // Room service
                new DirectExchange("x.appointment-room"),
                new Queue("q.reserve-room"),
                new Binding("q.reserve-room", Binding.DestinationType.QUEUE, "x.appointment-room", "room-service", null),

                new FanoutExchange("x.room-reserved"),
                new Queue("q.appointment-rooms-reserve-reply"),
                new Binding("q.appointment-rooms-reserve-reply", Binding.DestinationType.QUEUE, "x.room-reserved", "room-service", null),

                // Accounting Service
                new DirectExchange("x.open-account"),
                new Queue("q.open-account"),
                new Binding("q.open-account", Binding.DestinationType.QUEUE, "x.open-account", "accounting-service", null),

                new FanoutExchange("x.account-created"),
                new Queue("q.appointment-accounting-account-created-reply"),
                new Binding("q.appointment-accounting-account-created-reply", Binding.DestinationType.QUEUE, "x.account-created", "accounting-service", null),

                new FanoutExchange("x.account-not-created"),
                new Queue("q.appointment-accounting-account-not-created-reply"),
                new Binding("q.appointment-accounting-account-not-created-reply", Binding.DestinationType.QUEUE, "x.account-not-created", "accounting-service", null),

                QueueBuilder.durable("q.notification-service").withArgument("x-dead-letter-exchange","x.notification-failure").withArgument("x-dead-letter-routing-key","fall-back").build()

        );
    }
}
